package gremlins;

import processing.core.PApplet;

public class Progress {

    public float posX;
    public float posY;

    public float length;
    public float width;
    
    public float progress;
    public boolean isActive;

    public PApplet context;

    public Progress(PApplet app, float x, float y, float l, float w) {
        this.posX = x;
        this.posY = y;

        this.length = l;
        this.width = w;

        this.progress = 0;
        this.isActive = false;
        this.context = app;

    }

    public void draw() {
        this.context.fill(153);
        this.context.rect(this.posX, this.posY, this.length, this.width);
        
        this.context.fill(0); 
        float lengthOfProgress = this.progress * length;
        this.context.rect(this.posX, this.posY, lengthOfProgress, this.width);
    }
}
