package gremlins;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PApplet;

/**
 * Engine is a class containing all the functionality to 
 * combine various colliders to have the required interactions
 * for the game
 */
public class Engine {
    public App context;

    public Engine(App app) {
        this.context = app;
    }

    public static boolean[] checkWallCollisions(Collider collider, ArrayList<StoneWall> stonewalls, ArrayList<BrickWall> brickwalls, IceWall icewall) {
        // Boolean array for each direction
        boolean[] isCollidingWithWall = new boolean[] {false, false, false, false};

        // Iterate over alll the stonewalls and check for collisions
        for (Wall stonewall: stonewalls) {
            boolean isLeftColliding = CollisionDetector.isLeftColliding(collider, stonewall);
            boolean isRightColliding = CollisionDetector.isRightColliding(collider, stonewall);
            boolean isTopColliding = CollisionDetector.isTopColliding(collider, stonewall);
            boolean isBottomColliding = CollisionDetector.isBottomColliding(collider, stonewall);

            if (isLeftColliding == true) {
                isCollidingWithWall[0] = true;
            } 

            if (isRightColliding == true) {
                isCollidingWithWall[1] = true;
            } 

            if (isTopColliding == true) {
                isCollidingWithWall[2] = true;
            } 

            if (isBottomColliding == true) {
                isCollidingWithWall[3] = true;
            }
        }

        // Iterate over brickwalls and check for collisions
        for (Wall brickwall: brickwalls) {
            boolean isLeftColliding = CollisionDetector.isLeftColliding(collider, brickwall);
            boolean isRightColliding = CollisionDetector.isRightColliding(collider, brickwall);
            boolean isTopColliding = CollisionDetector.isTopColliding(collider, brickwall);
            boolean isBottomColliding = CollisionDetector.isBottomColliding(collider, brickwall);

            if (isLeftColliding == true) {
                isCollidingWithWall[0] = true;
            } 

            if (isRightColliding == true) {
                isCollidingWithWall[1] = true;
            } 

            if (isTopColliding == true) {
                isCollidingWithWall[2] = true;
            } 

            if (isBottomColliding == true) {
                isCollidingWithWall[3] = true;
            }
        }

        if (icewall.isActive == true) {
            boolean isLeftColliding = CollisionDetector.isLeftColliding(collider, icewall);
            boolean isRightColliding = CollisionDetector.isRightColliding(collider, icewall);
            boolean isTopColliding = CollisionDetector.isTopColliding(collider, icewall);
            boolean isBottomColliding = CollisionDetector.isBottomColliding(collider, icewall);

            if (isLeftColliding == true) {
                isCollidingWithWall[0] = true;
            } 

            if (isRightColliding == true) {
                isCollidingWithWall[1] = true;
            } 

            if (isTopColliding == true) {
                isCollidingWithWall[2] = true;
            } 

            if (isBottomColliding == true) {
                isCollidingWithWall[3] = true;
            }
        }
        

        return isCollidingWithWall;
    }

    /**
     * Checks for fireball collisions with stone and brick walls
     * @param fireball
     * @param stonewalls
     * @param brickwalls
     * @return
     */
    public static boolean handleFireball(Fireball fireball, ArrayList<StoneWall> stonewalls, ArrayList<BrickWall> brickwalls) {
        Iterator<BrickWall> brickWallItr =  brickwalls.iterator();
        boolean isFireballDestroyed = false;

        while (brickWallItr.hasNext()) {
            Wall wall = brickWallItr.next();

            Boolean[] isCollidingWithWall = CollisionDetector.checkCollisions(fireball, wall);
            if (isCollidingWithWall[fireball.headingDirection] == true) {
                return true;
            }     
        }

        Iterator<StoneWall> stonewallItr = stonewalls.iterator();

        while (stonewallItr.hasNext()) {
            StoneWall stonewall = stonewallItr.next();

            Boolean[] isCollidingWithWall = CollisionDetector.checkCollisions(fireball, stonewall);
            if (isCollidingWithWall[fireball.headingDirection] == true) {
                return true;
            }     
        }

        return isFireballDestroyed;
    }

    public static boolean checkIceWallActivated(Fireball fireball, IceWall icewall) {
        Boolean[] isCollidingWithIceWall = CollisionDetector.checkCollisions(fireball, icewall);

        if (icewall.getInEffect() == false) {
            if (isCollidingWithIceWall[fireball.headingDirection] == true) {
                icewall.brickDestroyInProgress = true;
                icewall.inEffect = true;
                icewall.lastInEffectTime = Instant.now();

                return true;
            }
        }      
        
        return false;
    }

    public static boolean handleSlime(Slime slime, ArrayList<StoneWall> stonewalls, ArrayList<BrickWall> brickwalls) {
        Iterator<BrickWall> brickWallItr =  brickwalls.iterator();
        boolean isFireballDestroyed = false;

        while (brickWallItr.hasNext()) {
            Wall wall = brickWallItr.next();

            Boolean[] isCollidingWithWall = CollisionDetector.checkCollisions(slime, wall);
            if (isCollidingWithWall[slime.headingDirection] == true) {
                return true;
            }     
        }

        Iterator<StoneWall> stonewallItr = stonewalls.iterator();

        while (stonewallItr.hasNext()) {
            StoneWall stonewall = stonewallItr.next();

            Boolean[] isCollidingWithWall = CollisionDetector.checkCollisions(slime, stonewall);
            if (isCollidingWithWall[slime.headingDirection] == true) {
                return true;
            }     
        }

        return isFireballDestroyed;
    }

    /**
     * Handles projectile collisions with walls (either stone or brick)
     * @param projectile
     * @param stonewalls
     * @param brickwalls
     * @return
     */
    public static boolean handleProjectile(Projectile projectile, ArrayList<StoneWall> stonewalls, ArrayList<BrickWall> brickwalls) {
        Iterator<BrickWall> brickWallItr =  brickwalls.iterator();
        boolean isProjectileDestroyed = false;

        while (brickWallItr.hasNext()) {
            Wall wall = brickWallItr.next();

            Boolean[] isCollidingWithWall = CollisionDetector.checkCollisions(projectile, wall);
            if (isCollidingWithWall[projectile.headingDirection] == true) {
                return true;
            }     
        }

        Iterator<StoneWall> stonewallItr = stonewalls.iterator();

        while (stonewallItr.hasNext()) {
            StoneWall stonewall = stonewallItr.next();

            Boolean[] isCollidingWithWall = CollisionDetector.checkCollisions(projectile, stonewall);
            if (isCollidingWithWall[projectile.headingDirection] == true) {
                return true;
            }     
        }

        return isProjectileDestroyed;
    }

    public static Object[] checkFireballSlimeCollisions(ArrayList<Fireball> fireballs, ArrayList<Slime> slimes) {
        Iterator<Fireball> fireballItr = fireballs.iterator();
        Iterator<Slime> slimeItr = slimes.iterator();

        while (fireballItr.hasNext()) {
            Fireball fireball = fireballItr.next(); 
            while (slimeItr.hasNext()) {
                Slime slime = slimeItr.next();

                boolean isColliding = checkProjectileOnProjectile(fireball, slime);
  
                if (isColliding == true) {
                    fireballItr.remove();
                    slimeItr.remove();
                }
            }
        }

        Object[] postCheckFireballSlimeData = new Object[] {fireballs, slimes};
        return postCheckFireballSlimeData;
    }

    public static boolean checkProjectileOnProjectile(Projectile projectileA, Projectile projectileB) {
        boolean isColliding = CollisionDetector.isColliding(projectileA, projectileB);
        return isColliding;
    }

    public static ArrayList<BrickWall> renderBrickWalls(Fireball fireball, ArrayList<BrickWall> brickwalls) {
        Iterator<BrickWall> brickwallItr = brickwalls.iterator();

        while (brickwallItr.hasNext()) {
            BrickWall brickwall = brickwallItr.next();

                
            Boolean[] isCollidingWithWall = CollisionDetector.checkCollisions(fireball, brickwall);
            if (isCollidingWithWall[fireball.headingDirection] == true) {
                brickwall.brickDestroyInProgress = true;

                return brickwalls; 
            }     
        }

        return brickwalls; 
    }

    public static Object[] checkPlayerGremlinCollision(Player player, ArrayList<Gremlin> gremlins) {
        Iterator<Gremlin> gremlinsItr = gremlins.iterator();

        boolean isPlayerHit = false;
        while (gremlinsItr.hasNext()) {
            Gremlin gremlin = gremlinsItr.next();

            if (gremlin.isAlive == true) {
                Boolean[] isCollidingWithWall = CollisionDetector.checkCollisions(player, gremlin); 

                for (Boolean col: isCollidingWithWall) {
                    if (col == true) {
                        gremlinsItr.remove();
                        
                        isPlayerHit = true;
                        player.damage();
                        player.resetToOrigin();
                        break;
                    }
                } 
            }
        }

        Object[] playerGremlinCollisionData = new Object[]{gremlins, isPlayerHit};

        return playerGremlinCollisionData;
    }

    public static Object[] checkFireballGremlinCollision(Fireball fireball, ArrayList<Gremlin> gremlins) {
        Iterator<Gremlin> gremlinsItr = gremlins.iterator();

        boolean isFireballDestroyed = false;
        while (gremlinsItr.hasNext()) {
            Gremlin gremlin = gremlinsItr.next();

            Boolean[] isCollidingWithWall = CollisionDetector.checkCollisions(fireball, gremlin); 

            for (Boolean col: isCollidingWithWall) {
                if (col == true) {
                    gremlin.kill();
                    isFireballDestroyed = true;
                }
            }
        }

        Object[] fireballGremlinCollisionData = new Object[]{gremlins, isFireballDestroyed};

        return fireballGremlinCollisionData;
    }

    public static Object[] checkSlimePlayerCollision(Player player, ArrayList<Slime> activeSlimes) {
        Iterator<Slime> slimeItr = activeSlimes.iterator();

        boolean isSlimeDestroyed = false;
        while (slimeItr.hasNext()) {
            Slime slime = slimeItr.next();

            Boolean[] isCollidingWithWall = CollisionDetector.checkCollisions(slime, player); 

            for (Boolean col: isCollidingWithWall) {
                if (col == true) {
                    player.damage();
                    player.resetToOrigin();
                    isSlimeDestroyed = true;
                }
            }
        }

        Object[] playerSlimeCollisionData = new Object[]{activeSlimes, isSlimeDestroyed};

        return playerSlimeCollisionData;
    }


    public static ArrayList<Slime> monitorGremlinSpawns(Player player, ArrayList<Gremlin> gremlins, ArrayList<Ground> groundTiles, ArrayList<Slime> activeSlimes) {
        Iterator<Gremlin> gremlinsItr = gremlins.iterator();

        while (gremlinsItr.hasNext()) {
            Gremlin gremlin = gremlinsItr.next();

            boolean isShootAvailable = gremlin.isShootAvailable();
            boolean requiresRespawn = gremlin.requiresRespawn();

            if (requiresRespawn == true) {
                gremlin.respawn(player, groundTiles);      
            }

            if (isShootAvailable == true) {
                Slime slime = new Slime(gremlin.context, gremlin.posX, gremlin.posY, gremlin.headingDirection);
                activeSlimes.add(slime);
            } 
        }

        return activeSlimes;

    }

    public static boolean checkPlayerAtExit(Player player, EscapeDoor escapeDoor) {
        Boolean isDoorColliding = CollisionDetector.isColliding(player, escapeDoor);  

        if (isDoorColliding == true) {
            return true;
        }

        return false;
    }

    public static void checkPlayerGainsLife(Player player, Life life) {
        Boolean isLifeColliding = CollisionDetector.isColliding(player, life);  

        if ((isLifeColliding == true) && (life.isActive == true)) {
            player.health.numLives += 1;
            life.isActive = false;
        }
    }

    public static void handleLifePowerUpSpawn(Player player, Life life, ArrayList<Ground>groundTiles) {
        boolean requiresRespawn = life.requiresRespawn();

        if (requiresRespawn == true) {
            life.respawn(player, groundTiles);
        }
    }

    public static void handleIceWallSpawn(Player player, IceWall icewall, ArrayList<Ground>groundTiles) {
        boolean requiresRespawn = icewall.requiresRespawn();

        if (requiresRespawn == true) {
            icewall.respawn(player, groundTiles);
        }
    }

    public static int getOppositeHeading(int heading) {
        if (heading == 0) {
            return 1;
        } else if (heading == 1) {
            return 0;
        } else if (heading == 2) {
            return 3;
        } else {
            return 2;
        }
    }

    public static boolean isShootAvailable(App context, Player player) {
        Instant currentTime = Instant.now();

        Duration timeSinceLastShot = Duration.between(player.lastFireTime, currentTime);
         
        if (timeSinceLastShot.toMillis() > player.fireballCooldown) {
            context.fireballProgress.isActive = false;
            player.lastFireTime = Instant.now();
            
            return true;
        }

        return false;
    }

    public static float getFireballProgress(Player player) {
        Instant currentTime = Instant.now();
        Duration timeSinceLastShot = Duration.between(player.lastFireTime, currentTime); 
        
        float progress = (float)(timeSinceLastShot.toMillis() / player.fireballCooldown);

        return progress;
    }

    public static float getIceWallProgress(IceWall icewall) {

        Instant currentTime = Instant.now();
        Duration timeSinceLastInEffect = Duration.between(icewall.lastInEffectTime, currentTime); 


        float progress = (float)(timeSinceLastInEffect.toMillis() / 3000.0);
        
        return progress;
    }

    public static ArrayList<Fireball> attemptFireballShoot(App app, Player player, ArrayList<Fireball> activeFireballs) {
        if (isShootAvailable(app, player) == true) {
            if (player.directionFacing.equals("Right")) {
                Fireball newFireball = new Fireball(app, player.posX, player.posY, 4, 0, 1);
                activeFireballs.add(newFireball);
            } else if (player.directionFacing.equals("Left")) {
                Fireball newFireball = new Fireball(app, player.posX, player.posY, -4, 0, 0);
                activeFireballs.add(newFireball);
            } else if (player.directionFacing.equals("Up")) {
                Fireball newFireball = new Fireball(app, player.posX, player.posY, 0, -4, 2);
                activeFireballs.add(newFireball);
            } else {
                Fireball newFireball = new Fireball(app, player.posX, player.posY, 0, 4, 3);
                activeFireballs.add(newFireball);
            }

            app.fireballProgress.isActive = true;
        }
       
        return activeFireballs;
    }

    /**
     * Adjusts the player position when key is released till it falls neatly
     * into a new tile spot
     * @param player
     * @return Boolean value about whether further adjustment is needed or not
     */
    public static boolean adjustPlayerPosition(Player player) {
        if ((player.posX % 20 != 0) || (player.posY % 20 != 0)) {
            player.move();
            return true;
        }

        System.out.println(player.posX);
        System.out.println(player.posY);

        player.velocityX = 0;
        player.velocityY = 0;

        return false;
    }

    // public static boolean checkIceWallActivated(Fireball fireball, IceWall icewall) {
    //     boolean isColliding = CollisionDetector.isColliding(fireball, icewall);

    //     if ((icewall.isActive == true) && (isColliding == true)) {
    //         return true;
    //     }

    //     return false;
    // }
}
