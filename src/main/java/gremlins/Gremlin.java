package gremlins;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import javax.swing.Action;

import processing.core.PApplet;
import processing.core.PImage;

public class Gremlin extends Collider {

    public PImage image;

    public float posX;
    public float posY;

    public float velocityX;
    public float velocityY;

    public boolean isMoving;
    public int headingDirection;

    public boolean isAlive;
    public Instant lastDeathTime;
    public Instant lastFiringTime;

    public Gremlin(PApplet app) {
        super(app, 0, 0);

        try {
            this.image = app.loadImage(app.getClass().getResource("gremlin.png").getPath().replace("%20", ""));
        } catch (Exception e) {
            // do nothing this is for testing
        }

        this.posX = 0;
        this.posY = 0;

        this.velocityX = 0;
        this.velocityY = 0;
        this.headingDirection = 0;

        this.isAlive = true;
        this.lastDeathTime = Instant.now();
        this.lastFiringTime = Instant.now();
    }

    public void changeHeading(int newHeading) {
        this.headingDirection = newHeading;

        if (newHeading == 0) {
            this.velocityX = -1;
            this.velocityY = 0;
        } else if (newHeading == 1) {
            this.velocityX = 1;
            this.velocityY = 0;
        } else if (newHeading == 2) {
            this.velocityX = 0;
            this.velocityY = -1;
        } else if (newHeading == 3) {
            this.velocityX = 0;
            this.velocityY = 1;
        }  
    }

    public void moveTo(PApplet app, float locX, float locY) {
        app.image(this.image, locX, locY);
        this.bounds.setBounds(locX, locY);
    }

    public void draw(PApplet app) {
        app.image(this.image, this.posX, this.posY);
    }

    public void move() {
        this.bounds.shiftBounds(this.velocityX, this.velocityY);
        this.posX += this.velocityX;
        this.posY += this.velocityY;
    }

    public void kill() {
        this.isAlive = false;
        lastDeathTime = Instant.now();
    }

    public boolean isShootAvailable() {
        Instant currentTime = Instant.now();
        Duration timeSinceLastFiring = Duration.between(this.lastFiringTime, currentTime);

        if ((timeSinceLastFiring.toMillis() > 3000) && (this.isAlive == true)) {
            this.lastFiringTime = Instant.now();
            return true;
        }

        return false;
    }

    public boolean requiresRespawn() {
        if (isAlive == false) {
            Instant currentTime = Instant.now();
            Duration timeSinceLastDeath = Duration.between(this.lastDeathTime, currentTime);
            if (timeSinceLastDeath.toMillis() > 1000) {
                return true;
            }
        }

        return false;
    }

    public void respawn(Collider player, ArrayList<Ground> groundTiles) {
        for (Ground groundTile: groundTiles) {
            double distanceToPlayer = CollisionDetector.getDistanceBetween(player, groundTile);
            
            if (distanceToPlayer > 200) {
                this.posX = groundTile.posX;
                this.posY = groundTile.posY;
        
                this.bounds.setBounds(this.posX, this.posY);
        
                this.isAlive = true;

                break;
            }
        }
    }
}
