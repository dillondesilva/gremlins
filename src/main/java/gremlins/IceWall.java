package gremlins;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import processing.core.PApplet;

public class IceWall extends Wall {
    public Instant lastSpawn;
    public boolean isActive;
    
    public IceWall(PApplet app, float x, float y) {
        super(app, x, y, "icewall.png", "ice");

        this.lastSpawn = Instant.now();
        this.isActive = false;
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
