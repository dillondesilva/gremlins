package gremlins;

import processing.core.PApplet;
import processing.core.PImage;

public class Wall extends Collider {
    public PImage image;
    public String wallType;

    public Wall(PApplet app, float locX, float locY, String imgSrc, String type) {
        super(app, locX, locY);

        try {
            this.image = app.loadImage(app.getClass().getResource(imgSrc).getPath().replace("%20", "")); 
        } catch (Exception e) {
            // Do nothing
        }
        // this.image = app.loadImage(app.getClass().getResource(imgSrc).getPath().replace("%20", ""));
        this.wallType = type;
    }
    
    public void draw(PApplet app) {
        app.image(this.image, this.posX, this.posY);
    }
}
