package gremlins;

import processing.core.PApplet;
import processing.core.PImage;

public class Projectile extends Collider {
    public PImage image;
    public int headingDirection;

    public Projectile(PApplet app, float x, float y, String imgSrc, int heading) {
        super(app, x, y);
        this.headingDirection = heading;

        try {
            this.image = app.loadImage(app.getClass().getResource(imgSrc).getPath().replace("%20", ""));
        } catch (Exception e) {
            // Do nothing as we are in testing;
        }
    }

    public void draw() {
        this.context.image(this.image, this.posX, this.posY);
    }

}
