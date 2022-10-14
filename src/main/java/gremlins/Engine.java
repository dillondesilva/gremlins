package gremlins;

import java.util.ArrayList;
import java.util.Iterator;

import gremlins.CollisionDetector;
import processing.core.PApplet;

public class Engine {
    public static boolean[] checkWallCollisions(Collider collider, ArrayList<StoneWall> stonewalls, ArrayList<BrickWall> brickwalls) {
        // Boolean array for each direction
        boolean[] isCollidingWithWall = new boolean[] {false, false, false, false};

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
        

        return isCollidingWithWall;
    }

    public static boolean handleFireball(Fireball fireball, ArrayList<StoneWall> stonewalls, ArrayList<BrickWall> brickwalls) {
        Iterator<BrickWall> brickWallItr =  brickwalls.iterator();
        boolean isFireballDestroyed = false;

        while (brickWallItr.hasNext()) {
            Wall wall = brickWallItr.next();

            boolean[] isCollidingWithWall = CollisionDetector.checkCollisions(fireball, wall);
            if (isCollidingWithWall[fireball.headingDirection] == true) {
                return true;
            }     
        }

        Iterator<StoneWall> stonewallItr = stonewalls.iterator();

        while (stonewallItr.hasNext()) {
            StoneWall stonewall = stonewallItr.next();

            boolean[] isCollidingWithWall = CollisionDetector.checkCollisions(fireball, stonewall);
            if (isCollidingWithWall[fireball.headingDirection] == true) {
                return true;
            }     
        }

        return isFireballDestroyed;
    }

    public static ArrayList<BrickWall> renderBrickWalls(Fireball fireball, ArrayList<BrickWall> brickwalls) {
        Iterator<BrickWall> brickwallItr = brickwalls.iterator();

        while (brickwallItr.hasNext()) {
            BrickWall brickwall = brickwallItr.next();

            boolean[] isCollidingWithWall = CollisionDetector.checkCollisions(fireball, brickwall);
            if (isCollidingWithWall[fireball.headingDirection] == true) {
                boolean isDestroyed = brickwall.damage();

                if (isDestroyed == true) {
                    brickwallItr.remove();
                }

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
                boolean[] isCollidingWithWall = CollisionDetector.checkCollisions(player, gremlin); 

                for (boolean col: isCollidingWithWall) {
                    if (col == true) {
                        gremlinsItr.remove();
                        
                        isPlayerHit = true;
                        player.damage();
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

            boolean[] isCollidingWithWall = CollisionDetector.checkCollisions(fireball, gremlin); 

            for (boolean col: isCollidingWithWall) {
                if (col == true) {
                    gremlin.kill();
                    isFireballDestroyed = true;
                }
            }
        }

        Object[] fireballGremlinCollisionData = new Object[]{gremlins, isFireballDestroyed};

        return fireballGremlinCollisionData;
    }


    public static void monitorGremlinSpawns(Player player, ArrayList<Gremlin> gremlins, ArrayList<Ground> groundTiles) {
        Iterator<Gremlin> gremlinsItr = gremlins.iterator();

        while (gremlinsItr.hasNext()) {
            Gremlin gremlin = gremlinsItr.next();
            boolean requiresRespawn = gremlin.requiresRespawn();

            if (requiresRespawn == true) {
                gremlin.respawn(player, groundTiles);      
            }
        }
    }
}
