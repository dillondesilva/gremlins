package gremlins;


import processing.core.PApplet;
import processing.core.PImage;

public class Player extends Collider {
    public PImage image;
    public String directionFacing;

    public float velocityX;
    public float velocityY;

    public Health health; 

    public Player(PApplet app, float x, float y, int lives) {
        super(app, x, y);

        this.image = app.loadImage(app.getClass().getResource("wizard0.png").getPath().replace("%20", ""));
        this.velocityX = 0;
        this.velocityY = 0;
        this.directionFacing = "Right";

        this.health = new Health(app, lives);
    }

    public void moveTo(PApplet app, float locX, float locY) {
        app.image(this.image, locX, locY);
    }

    public void draw(PApplet app) {
        app.image(this.image, this.posX, this.posY);
    }

    public void moveLeft(PApplet app) {
        this.image = app.loadImage(app.getClass().getResource("wizard0.png").getPath().replace("%20", ""));
   
        this.velocityX = -2;
        this.velocityY = 0;

        this.directionFacing = "Left";
    }

    public void moveRight(PApplet app) {
        this.image = app.loadImage(app.getClass().getResource("wizard1.png").getPath().replace("%20", ""));

        this.velocityX = 2;
        this.velocityY = 0;
     
        this.directionFacing = "Right";
    }

    public void moveDown(PApplet app) {
        this.image = app.loadImage(app.getClass().getResource("wizard2.png").getPath().replace("%20", ""));
  
        this.velocityX = 0;
        this.velocityY = 2;

        this.directionFacing = "Down";
    }

    public void moveUp(PApplet app) {
        this.image = app.loadImage(app.getClass().getResource("wizard3.png").getPath().replace("%20", ""));
        this.velocityX = 0;
        this.velocityY = -2;

        this.directionFacing = "Up";
    }

    public void damage() {
        this.health.numLives -= 1;
        System.out.println("hit");
    }

    public void move() {
        this.posX += this.velocityX;
        this.posY += this.velocityY;

        this.bounds.shiftBounds(this.velocityX, this.velocityY);
    }
}
