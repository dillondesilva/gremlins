package gremlins;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.stream.IntStream; 

public class Health {
    public PApplet context;

    public int numLives;

    public float posX;
    public float posY;

    public PImage image;
    
    public Health(PApplet app, int lives) {
        this.numLives = lives;
        this.context = app;
        this.posX = 100;
        this.posY = 680;
        
        try {
            this.image = app.loadImage(app.getClass().getResource("wizard0.png").getPath().replace("%20", ""));
        } catch (Exception e) {

        }
    }

    public void bar() {
        IntStream.range(0, this.numLives).forEachOrdered(n -> {
            this.context.image(this.image, this.posX + (20 * n), this.posY);
        });
    }
}
