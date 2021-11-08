package es.ucm.vm.androidengine;

// ANDROID
import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Class to manage the "window" of android. This basically manages the SurfaceHolder to use it for
 * painting.
 */
public class AndroidWindow extends SurfaceView {

    // Surface holder
    protected SurfaceHolder _holder;

    /**
     * Constructor of the window. Retrieves the Surface holder
     * and stores it.
     *
     * @param context (Context) Android View context.
     */
    public AndroidWindow(Context context) {
        super(context);

        // Get holder and store
        _holder = getHolder();
    } // Constructor

    /**
     * Gives access to the surface holder.
     *
     * @return (SurfaceHolder) SurfaceView's SurfaceHolder.
     */
    public SurfaceHolder getSurfaceHolder(){
        return _holder;
    } // getSurfaceHolder

    /**
     * Checks if the current SurfaceHolder is valid and available
     * for painting.
     *
     * @return (boolean) If SurfaceHolder is valid.
     */
    public boolean surfaceValid(){
        return _holder.getSurface().isValid();
    } // surfaceValid

    /**
     * Get the width of the Screen of the phone.
     *
     * @return (int) Width of the screen.
     */
    public int width() {
        return getWidth();
    } // width

    /**
     * Get the height of the Screen of the phone.
     *
     * @return (int) Height of the screen.
     */
    public int height() {
        return getHeight();
    } // height
} // AndroidWindow