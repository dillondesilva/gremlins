package gremlins;

import processing.core.PApplet;
import processing.core.PImage;

public class Fireball extends Projectile {
    public PImage image;

    public float velocityX;
    public float velocityY;

    public Fireball(PApplet app, float startX, float startY, float xVelocity, float yVelocity, int heading) {
        super(app, startX, startY, "fireball.png", heading);

        this.velocityX = xVelocity;
        this.velocityY = yVelocity;

        try {
            this.image = app.loadImage(app.getClass().getResource("fireball.png").getPath().replace("%20", ""));
        } catch (Exception e) {
            // Do nothing as we are in testing;
        }
    }

    public void move() {
        this.posX = this.posX + this.velocityX;
        this.posY = this.posY + this.velocityY;

        this.bounds.setBounds(this.posX, this.posY);
    }
}
