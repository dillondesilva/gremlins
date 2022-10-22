package gremlins;

import processing.core.PApplet;

public class Slime extends Projectile {
    public float velocityX;
    public float velocityY;

    public Slime(PApplet app, float x, float y, int heading) {
        super(app, x, y, "slime.png", heading);

        if (this.headingDirection == 0) {
            this.velocityX = -4;
            this.velocityY = 0;
        } else if (this.headingDirection == 1) {
            this.velocityX = 4;
            this.velocityY = 0;
        } else if (this.headingDirection == 2) {
            this.velocityX = 0;
            this.velocityY = -4;
        } else {
            this.velocityX = 0;
            this.velocityY = 4;
        }
    }

    public void move() {
        this.posX = this.posX + this.velocityX;
        this.posY = this.posY + this.velocityY;

        this.bounds.setBounds(this.posX, this.posY);
    }

    public void draw(PApplet app) {
        app.image(this.image, this.posX, this.posY);
    }
}
