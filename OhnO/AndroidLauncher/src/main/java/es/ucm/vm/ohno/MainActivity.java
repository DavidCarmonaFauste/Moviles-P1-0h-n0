package es.ucm.vm.ohno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Prueba de concepto de renderizado activo en Android.
 *
 * Esta clase implementa la actividad principal de la aplicación.
 * En condiciones normales (una aplicación más compleja) la
 * implementación se distribuiría en más clases y se haría más
 * versátil.
 *
 * Para que funcione, en el módulo se debe incluir un directorio
 * de Assets y guardar en él el fichero "Bangers-Regular.ttf" con
 * la fuente de letra, que será cargada en ejecución para pintar un
 * rótulo en pantalla.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Método llamado por Android como parte del ciclo de vida de
     * la actividad. Se llama en el momento de lanzarla.
     *
     * @param savedInstanceState Información de estado de la actividad
     *                           previamente serializada por ella misma
     *                           para reconstruirse en el mismo estado
     *                           tras un reinicio. Será null la primera
     *                           vez.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        _font = Typeface.createFromAsset(this.getAssets(), "Bangers-Regular.ttf");

        // Preparamos el contenido de la actividad.
        _renderView = new MySurfaceView(this);
        setContentView(_renderView);

    } // onCreate

    //--------------------------------------------------------------------

    /**
     * Método llamado por Android como parte del ciclo de vida de la
     * actividad. Notifica que la actividad va a pasar a primer plano,
     * estando en la cima de la pila de actividades y completamente
     * visible.
     *
     * Es llamado durante la puesta en marcha de la actividad (algo después
     * de onCreate()) y también después de un periodo de pausa (notificado
     * a través de onPause()).
     */
    @Override
    protected void onResume() {

        // Avisamos a la vista (que es la encargada del active render)
        // de lo que está pasando.
        super.onResume();
        _renderView.resume();

    } // onResume

    //--------------------------------------------------------------------

    /**
     * Método llamado por Android como parte del ciclo de vida de la
     * actividad. Notifica que la actividad ha dejado de ser la de
     * primer plano. Es un indicador de que el usuario está, de alguna
     * forma, abandonando la actividad.
     */
    @Override
    protected void onPause() {

        // Avisamos a la vista (que es la encargada del active render)
        // de lo que está pasando.
        super.onPause();
        _renderView.pause();

    } // onPause

    //--------------------------------------------------------------------
    //                    Atributos protegidos/privados
    //--------------------------------------------------------------------

    /**
     * Vista principal de la actividad que gestiona, además, el active
     * rendering.
     */
    protected MySurfaceView _renderView;

    /**
     * Fuente usada para escribir el texto que se muestra moviéndose de lado a lado.
     * Se carga en el onCreate y se usa en MySurfaceView.
     */
    protected Typeface _font;

    //--------------------------------------------------------------------
    //                         Clase MySurfaceView
    //--------------------------------------------------------------------

    /**
     * Clase con la vista principal de la actividad, que se incluye en ella
     * ocupando todo el espacio disponible.
     *
     * Implementa también el interfaz Runnable para proporcionar
     * active rendering. Para ello es necesario lanzar la ejecución de una
     * hebra cuyo ciclo de vida se gestionará aquí. Para eso, se proporcionan
     * métodos que deben ser llamados externamente para notificar cambios
     * en el ciclo de vida de la actividad.
     *
     * En condiciones normales (una aplicación más compleja) estas dos
     * funcionalidades estarían separadas.
     */
    class MySurfaceView extends SurfaceView implements Runnable {

        /**
         * Constructor.
         *
         * @param context Contexto en el que se integrará la vista
         *                (normalmente una actividad).
         */
        public MySurfaceView(Context context) {

            super(context);
            _holder = getHolder();
            if (_font != null) {
                // Tenemos fuente. Vamos a escribir texto.
                // Preparamos la configuración de formato en el
                // objeto _paint que utilizaremos en cada frame.
                _paint.setTypeface(_font);
                _paint.setFakeBoldText(true);
                _paint.setColor(0xFFFFFFFF);
                _paint.setTextSize(80);
            }

        } // MySurfaceView

        //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        /**
         * Método llamado para solicitar que se continue con el
         * active rendering. El "juego" se vuelve a poner en marcha
         * (o se pone en marcha por primera vez).
         */
        public void resume() {

            if (!_running) {
                // Solo hacemos algo si no nos estábamos ejecutando ya
                // (programación defensiva, nunca se sabe quién va a
                // usarnos...)
                _running = true;
                // Lanzamos la ejecución de nuestro método run()
                // en una hebra nueva.
                _renderThread = new Thread(this);
                _renderThread.start();
            } // if (!_running)

        } // resume

        //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        /**
         * Método llamado cuando el active rendering debe ser detenido.
         * Puede tardar un pequeño instante en volver, porque espera a que
         * se termine de generar el frame en curso.
         *
         * Se hace así intencionadamente, para bloquear la hebra de UI
         * temporalmente y evitar potenciales situaciones de carrera (como
         * por ejemplo que Android llame a resume() antes de que el último
         * frame haya terminado de generarse).
         */
        public void pause() {

            if (_running) {
                _running = false;
                while (true) {
                    try {
                        _renderThread.join();
                        _renderThread = null;
                        break;
                    } catch (InterruptedException ie) {
                        // Esto no debería ocurrir nunca...
                    }
                } // while(true)
            } // if (_running)

        } // pause

        //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        /**
         * Método que implementa el bucle principal del "juego" y que será
         * ejecutado en otra hebra. Aunque sea público, NO debe ser llamado
         * desde el exterior.
         */
        @Override
        public void run() {

            if (_renderThread != Thread.currentThread()) {
                // ¿¿Quién es el tuercebotas que está llamando al
                // run() directamente?? Programación defensiva
                // otra vez, con excepción, por merluzo.
                throw new RuntimeException("run() should not be called directly");
            }

            // Antes de saltar a la simulación, confirmamos que tenemos
            // un tamaño mayor que 0. Si la hebra se pone en marcha
            // muy rápido, la vista podría todavía no estar inicializada.
            while(_running && getWidth() == 0)
                // Espera activa. Sería más elegante al menos dormir un poco.
                ;

            long lastFrameTime = System.nanoTime();

            long informePrevio = lastFrameTime; // Informes de FPS
            int frames = 0;

            // Bucle principal.
            while(_running) {

                long currentTime = System.nanoTime();
                long nanoElapsedTime = currentTime - lastFrameTime;
                lastFrameTime = currentTime;
                double elapsedTime = (double) nanoElapsedTime / 1.0E9;
                update(elapsedTime);
                // Informe de FPS
                if (currentTime - informePrevio > 1000000000l) {
                    long fps = frames * 1000000000l / (currentTime - informePrevio);
                    System.out.println("" + fps + " fps");
                    frames = 0;
                    informePrevio = currentTime;
                }
                ++frames;

                // Pintamos el frame
                while (!_holder.getSurface().isValid())
                    ;
                Canvas canvas = _holder.lockCanvas();
                render(canvas);
                _holder.unlockCanvasAndPost(canvas);
                /*
                // Posibilidad: cedemos algo de tiempo. es una medida conflictiva...
                try {
                    Thread.sleep(1);
                }
                catch(Exception e) {}
    			*/

            } // while

        } // run

        //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        /**
         * Realiza la actualización de "la lógica" de la aplicación. En particular,
         * desplaza el rótulo a su nueva posición en su deambular de izquierda
         * a derecha.
         *
         * @param deltaTime Tiempo transcurrido (en segundos) desde la invocación
         * anterior (frame anterior).
         */
        protected void update(double deltaTime) {
            int maxX = getWidth() - 600; // 600 : longitud estimada en píxeles del rótulo

            _x += _incX * deltaTime;
            while(_x < 0 || _x > maxX) {
                // Vamos a pintar fuera de la pantalla. Rectificamos.
                if (_x < 0) {
                    // Nos salimos por la izquierda. Rebotamos.
                    _x = -_x;
                    _incX *= -1;
                }
                else if (_x > maxX) {
                    // Nos salimos por la derecha. Rebotamos
                    _x = 2*maxX - _x;
                    _incX *= -1;
                }
            } // while

        } // update

        //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        /**
         * Dibuja en pantalla el estado actual de la aplicación.
         *
         * @param c Objeto usado para enviar los comandos de dibujado.
         */
        protected void render(Canvas c) {

            // Borramos el fondo.
            c.drawColor(0xFF0000FF); // ARGB

            // Ponemos el rótulo (si conseguimos cargar la fuente)
            if (_font != null) {
                c.drawText("RENDERIZADO ACTIVO", (int) _x, 100, _paint);
            } // if (_sprite != null)

        } // render

        //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
        //        Atributos protegidos/privados (de MySurfaceView)
        //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

        /**
         * Posición x actual del texto (lado izquierdo). Es importante
         * que sea un número real, para acumular cambios por debajo del píxel si
         * la velocidad de actualización es mayor que la del desplazamiento.
         */
        protected double _x = 0;

        /**
         * Velocidad de desplazamiento en píxeles por segundo. La sensación de
         * velocidad percibida por el usuario dependerá de la densidad de
         * pantalla (número de píxeles por pulgada).
         */
        int _incX = 50;

        /**
         * Objeto Thread que está ejecutando el método run() en una hebra
         * diferente. Cuando se pide a la vista que se detenga el active
         * rendering, se espera a que la hebra termine.
         */
        Thread _renderThread;

        /**
         * Manejador de la superficie para poder acceder a su contenido.
         */
        SurfaceHolder _holder;

        /**
         * Bandera que indica si está o no en marcha la hebra de
         * active rendering, y que se utiliza para sincronización.
         * Es importante que el campo sea volatile.
         *
         * Java proporciona un mecanismo integrado para solicitar la
         * detencción de una hebra, aunque por simplicidad nosotros
         * motamos el nuestro propio.
         */
        volatile boolean _running = false;

        /**
         * Objeto con la configuración gráfica usada para pintar el texto.
         */
        Paint _paint = new Paint();

    } // class MySurfaceView


} // class MainActivity
