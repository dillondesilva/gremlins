package gremlins;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PImage;

public class Life extends Collider {
    public Instant lastSpawn;
    public PImage image;

    public boolean isActive;
    
    public Life(PApplet app, float x, float y) {
        super(app, x, y);

        this.lastSpawn = Instant.now();
        this.image = app.loadImage(app.getClass().getResource("life.png").getPath().replace("%20", ""));
    }

    public boolean requiresRespawn() {
        if (isActive == false) {
            Instant currentTime = Instant.now();
            Duration timeSinceLastSpawn = Duration.between(this.lastSpawn, currentTime);
            
            if (timeSinceLastSpawn.toMillis() > 10000) {
                return true;
            }
        }

        return false;
    }

    public void respawn(Collider player, ArrayList<Ground> groundTiles) {
        for (Ground groundTile: groundTiles) {
            double distanceToPlayer = CollisionDetector.getDistanceBetween(player, groundTile);
            
            if ((distanceToPlayer > 100) && (distanceToPlayer < 200)) {
                this.posX = groundTile.posX;
                this.posY = groundTile.posY;

                this.bounds.setBounds(this.posX, this.posY);
        
                this.isActive = true;
                this.lastSpawn = Instant.now();

                break;
            }
        }
    }

    public void draw() {
        this.context.image(this.image, this.posX, this.posY);
    }

}
