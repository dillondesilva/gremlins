package gremlins;

import processing.core.PApplet;
import processing.core.PImage;

public class Projectile extends Collider {
    public PImage image;
    public int headingDirection;

    public Projectile(PApplet app, float x, float y, String imgSrc, int heading) {
        super(app, x, y);
        
        this.image = app.loadImage(app.getClass().getResource(imgSrc).getPath().replace("%20", ""));
        this.headingDirection = heading;
    }
}
