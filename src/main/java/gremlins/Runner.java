package gremlins;

import java.util.ArrayList;
import java.util.Iterator;

public class Runner {
    public App context;
    public Runner(App app) {
        this.context = app;
    }

    private void handlePlayerMovement() {
        boolean[] playerWallCollisions = Engine.checkWallCollisions(this.context.player, this.context.activeStoneWalls, this.context.activeBrickWalls, this.context.icewall);
        Player player = this.context.player;

        if (this.context.boundsCheckInProgress == true) {
            this.context.boundsCheckInProgress = Engine.adjustPlayerPosition(player);
        } else if ((player.directionFacing == "Left") && (playerWallCollisions[0] == false)) {
            player.move();
        } else if ((player.directionFacing == "Right") && (playerWallCollisions[1] == false)) {
            player.move();
        } else if ((player.directionFacing == "Up") && (playerWallCollisions[2] == false)) {
            player.move();
        } else if ((player.directionFacing == "Down") && (playerWallCollisions[3] == false)) {
            player.move();
        }
    }

    private void handleActiveFireballs() {
        Iterator<Fireball> fireballItr = this.context.activeFireballs.iterator();
     
        while (fireballItr.hasNext()) {
            Fireball fireball = fireballItr.next();
            
            this.context.activeBrickWalls = Engine.renderBrickWalls(fireball, this.context.activeBrickWalls);
            Object[] fireballGremlinCollisionData = Engine.checkFireballGremlinCollision(fireball, this.context.activeGremlins);
            
            this.context.activeGremlins = (ArrayList<Gremlin>)fireballGremlinCollisionData[0];
            
            boolean isIceWallActivated = Engine.checkIceWallActivated(fireball, this.context.icewall);
            boolean isFireballDestroyed = (Engine.handleFireball(fireball, this.context.activeStoneWalls, this.context.activeBrickWalls)) | isIceWallActivated;
            
            if (isIceWallActivated == true) {
                System.out.println("Activating icewall");
    
                this.context.icewallProgress.isActive = true;
            }

            if (isFireballDestroyed == true) {
                fireballItr.remove();
            } else {
                fireball.move();
            }
        }
    }

    private void handleActiveSlimes() {
        ArrayList<Slime> activeSlimes = this.context.activeSlimes;
        
        ArrayList<StoneWall> activeStoneWalls = this.context.activeStoneWalls;
        ArrayList<BrickWall> activeBrickWalls = this.context.activeBrickWalls;

        Player player = this.context.player;

        Iterator<Slime> slimeItr = activeSlimes.iterator();
    
        while (slimeItr.hasNext()) {
            Slime slime = slimeItr.next();

            boolean isSlimeDestroyed = (Engine.handleSlime(slime, activeStoneWalls, activeBrickWalls));
            Object[] playerSlimeCollisionData = Engine.checkSlimePlayerCollision(player, activeSlimes);
            activeSlimes = (ArrayList<Slime>)playerSlimeCollisionData[0];
            
            if (isSlimeDestroyed == false) {
                slime.move();  
                // slime.draw(this);  
            } else {
                slimeItr.remove();      
            }   
        }
    }

    public void handleActiveGremlins() {
        ArrayList<Gremlin> activeGremlins = this.context.activeGremlins;

        ArrayList<StoneWall> activeStoneWalls = this.context.activeStoneWalls;
        ArrayList<BrickWall> activeBrickWalls = this.context.activeBrickWalls;

        IceWall icewall = this.context.icewall;

        boolean isGremlinsFrozen = this.context.isGremlinsFrozen;

        for (Gremlin gremlin: activeGremlins) {
            boolean[] collidingWalls = Engine.checkWallCollisions(gremlin, activeStoneWalls, activeBrickWalls, icewall);
            int currentGremlinHeading = gremlin.headingDirection;
            ArrayList<Integer> possibleHeadings = new ArrayList<Integer>();

            int heading = 0;
            while (heading < collidingWalls.length) {
                if (collidingWalls[heading] == false) {
                    possibleHeadings.add(heading);
                }

                heading += 1;
            }
            
            if (gremlin.isAlive == true) {
                if ((collidingWalls[currentGremlinHeading] == true)) {
                    int newGremlinHeading = (int)Math.floor(Math.random() * (3 + 1));

                    if (possibleHeadings.size() > 1) {
                        System.out.println(possibleHeadings);
                        int oppositeHeading = Engine.getOppositeHeading(currentGremlinHeading);
                        System.out.println(oppositeHeading);
                        newGremlinHeading = possibleHeadings.get((int)Math.floor(Math.random() * possibleHeadings.size()));

                        while ((newGremlinHeading == oppositeHeading)) {
                            newGremlinHeading = possibleHeadings.get((int)Math.floor(Math.random() * possibleHeadings.size()));
                        } 
                    }
    
                    gremlin.changeHeading(newGremlinHeading);
                } 
            
                if (isGremlinsFrozen == false) {
                    gremlin.move();
                }
            }
        }

        this.context.activeGremlins = activeGremlins;
    }

    private void handleActiveBrickWalls() {
        Iterator<BrickWall> brickwallItr = this.context.activeBrickWalls.iterator();

        while (brickwallItr.hasNext()) {
            BrickWall brickwall = brickwallItr.next();

            boolean isDestroyed = brickwall.tick();
                
            if (isDestroyed == true) {

                brickwallItr.remove();
            }

        }
    }

    private void handleProgressBars() {
        float fireballCooldownProgress = Engine.getFireballProgress(this.context.player);
        float icewallCooldownProgress = Engine.getIceWallProgress(this.context.icewall);
        
        this.context.fireballProgress.progress = fireballCooldownProgress;
        this.context.icewallProgress.progress = icewallCooldownProgress;

        if (fireballCooldownProgress >= 1) {
            this.context.fireballProgress.isActive = false;
        }

        if ((icewallCooldownProgress >= 1) || this.context.icewall.getInEffect() == false) {
            this.context.icewallProgress.isActive = false;
        }

        this.context.isGremlinsFrozen = this.context.icewall.getInEffect();
    }

    private void handleSpecials() {
        Player player = this.context.player;
        Life lifePowerup = this.context.lifePowerup;
        IceWall icewall = this.context.icewall;

        ArrayList<Ground> groundTiles = this.context.groundTiles;

        Engine.handleLifePowerUpSpawn(player, lifePowerup, groundTiles);
        Engine.checkPlayerGainsLife(player, lifePowerup);
        Engine.handleIceWallSpawn(player, icewall, groundTiles);

        icewall.tick();
    }

    public void run() {
        this.handleSpecials();
        this.handleProgressBars();
        this.handlePlayerMovement();
        this.handleActiveFireballs();
        this.handleActiveSlimes();
        this.handleActiveGremlins();
        this.handleActiveBrickWalls();
    }

}
