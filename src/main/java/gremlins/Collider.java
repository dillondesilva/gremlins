package gremlins;

import processing.core.PApplet;

public class Collider {
    public float posX;
    public float posY;

    public Bounds bounds;

    public PApplet context;

    public Collider(PApplet ctx, float x, float y) {
        this.posX = x;
        this.posY = y;

        this.bounds = new Bounds(this.posX, this.posY);

        this.context = ctx;
    }

    public void changePosition(float shiftX, float shiftY) {
        this.posX += shiftX;
        this.posY += shiftY;
    }
}
