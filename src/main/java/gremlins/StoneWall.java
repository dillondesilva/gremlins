package gremlins;

import processing.core.PApplet;


public class StoneWall extends Wall {
    public StoneWall(PApplet app, float posX, float posY) {
        super(app, posX, posY, "stonewall.png", "stone");
    }
}