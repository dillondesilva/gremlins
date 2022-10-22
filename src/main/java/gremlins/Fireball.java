package gremlins;

import processing.core.PApplet;
import processing.core.PImage;

public class Fireball extends Collider {
    public PImage image;

    public float velocityX;
    public float velocityY;

    public int headingDirection;

    public Fireball(PApplet app, float startX, float startY, float xVelocity, float yVelocity, int heading) {
        super(app, startX, startY);

        try {
            this.image = app.loadImage(app.getClass().getResource("fireball.png").getPath().replace("%20", ""));
        } catch (Exception e) {
            // Do nothing we are in testing;
        }
        this.velocityX = xVelocity;
        this.velocityY = yVelocity;

        this.headingDirection = heading;
    }

    public void move(PApplet app) {
        this.posX = this.posX + this.velocityX;
        this.posY = this.posY + this.velocityY;

        this.bounds.setBounds(this.posX, this.posY);
        app.image(this.image, this.posX, this.posY);
    }
}
