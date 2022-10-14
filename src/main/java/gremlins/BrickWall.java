package gremlins;

import processing.core.PApplet;


public class BrickWall extends Wall {
    public int brickHealth;

    public BrickWall(PApplet app, float posX, float posY) {
        super(app, posX, posY, "brickwall.png", "brick");

        this.brickHealth = 4;
    }

    public boolean damage() {
        this.brickHealth -= 1;

        if (brickHealth > 0) {
            String imgSrc = String.format("brickwall_destroyed%d.png", 4 - brickHealth);
            this.image = this.context.loadImage(this.context.getClass().getResource(imgSrc).getPath().replace("%20", ""));
            
            return false;
        } else {
            return true;
        }
    }
}