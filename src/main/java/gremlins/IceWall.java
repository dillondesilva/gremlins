package gremlins;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import processing.core.PApplet;

public class IceWall extends Wall {

    public Instant lastSpawn;
    public boolean isActive;
    public boolean brickDestroyInProgress;
    public boolean inEffect;
    public int wallHealth;

    public Instant lastInEffectTime;
    
    public IceWall(PApplet app, float x, float y) {
        super(app, x, y, "icewall.png", "ice");

        this.lastSpawn = Instant.now();
        this.isActive = false;
        this.brickDestroyInProgress = false;
        this.inEffect = false;

        this.wallHealth = 3;

        this.lastInEffectTime = Instant.now();
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
        Integer randomGroundTileIdx = (int)Math.floor(Math.random() * groundTiles.size());
        Ground groundTile = groundTiles.get(randomGroundTileIdx);
        
        this.posX = groundTile.posX;
        this.posY = groundTile.posY;

        this.bounds.setBounds(this.posX, this.posY);
        this.wallHealth = 3;
        this.image = this.context.loadImage(this.context.getClass().getResource("icewall.png").getPath().replace("%20", "")); 
        this.isActive = true;
        
        this.lastSpawn = Instant.now();
    }

    public boolean getInEffect() {
        return this.inEffect;
    }

    public void damage() {
        this.wallHealth -= 1;
        if (this.wallHealth > 0) {
            String imgSrc = String.format("icewall_destroyed%d.png", 3 - wallHealth);
            this.image = this.context.loadImage(this.context.getClass().getResource(imgSrc).getPath().replace("%20", ""));
        }
    }

    public void tick() {
        if (this.inEffect == true) {
            Instant currentTime = Instant.now();
            Duration timeSinceLastInEffect = Duration.between(this.lastInEffectTime, currentTime);
            
            if (timeSinceLastInEffect.toMillis() > 3000) {
                this.inEffect = false;
                this.lastInEffectTime = Instant.now();
            } 
        }

        if (this.wallHealth == 0) {
            this.brickDestroyInProgress = false;
            this.isActive = false;
        } else if (this.brickDestroyInProgress == true) {
            this.damage();
        }
    }

    public void draw() {
        this.context.image(this.image, this.posX, this.posY);
    }
}
