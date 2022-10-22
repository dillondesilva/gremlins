package gremlins;

import processing.core.PApplet;
import processing.core.PImage;

public class EscapeDoor extends Collider {
    public PImage image;
    
    public EscapeDoor(PApplet app, float x, float y) {
        super(app, x, y);
        this.image = app.loadImage(app.getClass().getResource("door.png").getPath().replace("%20", ""));
    }    

    public void draw() {
        this.context.image(this.image, this.posX, this.posY);
    }
} 
