package es.ucm.vm.pcengine;

import es.ucm.vm.engine.AbstractGraphics;
import es.ucm.vm.engine.Rect;

public class Graphics extends AbstractGraphics {
    @Override
    public void setCanvasSize(Rect c, Rect dim) {

    }

    @Override
    public Rect getCanvas() {
        return null;
    }

    @Override
    public void setReferenceCanvas(Rect c) {

    }

    @Override
    public void setCanvasPos(int x, int y) {

    }

    @Override
    public boolean isInCanvas(int x, int y) {
        return false;
    }

    @Override
    public void clear(int color) {

    }

    @Override
    public void setColor(int color) {

    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2, int thickness) {

    }

    @Override
    public void fillRect(int x1, int y1, int x2, int y2) {

    }

    @Override
    public void drawText(String text, int x, int y) {

    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public Rect scale(Rect src, Rect dim) {
        return null;
    }

    @Override
    public void save() {

    }

    @Override
    public void restore() {

    }

    @Override
    public void rotate(float angle) {

    }

    @Override
    public void translate(int x, int y) {

    }

    @Override
    public int repositionX(int x) {
        return 0;
    }

    @Override
    public int repositionY(int y) {
        return 0;
    }

    @Override
    public int reverseRepositionX(int x) {
        return 0;
    }

    @Override
    public int reverseRepositionY(int y) {
        return 0;
    }
}
