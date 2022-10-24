package gremlins;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;

import processing.core.PApplet;


public class TestEngine {
    public PApplet app;

    public Player player;

    public ArrayList<StoneWall> stonewalls;
    public ArrayList<BrickWall> brickwalls;

    @BeforeAll
    public static void setup() {
        //Creating a fake context

        // stonewalls.add(stoneWallA);
        // stonewalls.add(stoneWallB);
    }

    @Test
    public void testPlayerWallCollisions() {
        app = new PApplet();

        stonewalls = new ArrayList<StoneWall>();
        brickwalls = new ArrayList<BrickWall>();

        StoneWall stoneWallA = new StoneWall(app, 0, 0);
        StoneWall stoneWallB = new StoneWall(app, 80, 80);

        stonewalls.add(stoneWallA);
        stonewalls.add(stoneWallB);
        player = new Player(app, 20, 0, 3, 0.3333);
        boolean[] wallCollisionData = Engine.checkWallCollisions(player, stonewalls, brickwalls, new IceWall(app, 50, 50));
        // assert(app.player.posX == 20);
        boolean[] expectedWallCollisionData = new boolean[] {true, false, false, false};
        assertArrayEquals(wallCollisionData, expectedWallCollisionData);
    }

    @Test
    public void testPlayerGremlinCollisions() {
        app = new PApplet();

        ArrayList<Gremlin> gremlins = new ArrayList<Gremlin>();

        Gremlin gremlinA = new Gremlin(app);
        Gremlin gremlinB = new Gremlin(app);

        gremlinA.posX = 20;
        gremlinA.posY = 40;
        gremlinA.bounds.setBounds(20, 40);

        gremlinB.posX = 20;
        gremlinB.posY = 40;
        gremlinB.bounds.setBounds(20, 40);

        gremlins.add(gremlinA);
        gremlins.add(gremlinB);

        player = new Player(app, 20, 25, 3, 0.333);

        Object[] gremlinCollisionData = Engine.checkPlayerGremlinCollision(player, gremlins);
        gremlins = (ArrayList<Gremlin>)gremlinCollisionData[0];

        boolean isPlayerHit = (boolean)gremlinCollisionData[1];

        assertEquals(gremlins.size(), 1);
        assertEquals(isPlayerHit, true);
    }

    @Test
    public void testHandleFireball() {
        app = new PApplet();

        Fireball fireballA = new Fireball(app, 0, 0, 20, 0, 1);
        
        stonewalls = new ArrayList<StoneWall>();
        brickwalls = new ArrayList<BrickWall>();

        StoneWall stoneWallA = new StoneWall(app, 40, 0);
        BrickWall brickWallA = new BrickWall(app, 40, 0);

        stonewalls.add(stoneWallA);
        brickwalls.add(brickWallA);

        boolean isFireballDestroyed = Engine.handleFireball(fireballA, stonewalls, brickwalls);
        assertEquals(isFireballDestroyed, false);


        fireballA.posX = fireballA.posX + fireballA.velocityX;
        fireballA.posY = fireballA.posY + fireballA.velocityY;
        fireballA.bounds.setBounds(fireballA.posX, fireballA.posY);
 
        isFireballDestroyed = Engine.handleFireball(fireballA, stonewalls, brickwalls);
        assertEquals(isFireballDestroyed, true);
    }

    // @Test
    // public void testHandleSlime() {
    //     app = new PApplet();

    //     Slime slimeA = new Sl(app, 0, 0, 20, 0, 1);
        
    //     stonewalls = new ArrayList<StoneWall>();
    //     brickwalls = new ArrayList<BrickWall>();

    //     StoneWall stoneWallA = new StoneWall(app, 40, 0);
    //     BrickWall brickWallA = new BrickWall(app, 40, 0);

    //     stonewalls.add(stoneWallA);
    //     brickwalls.add(brickWallA);

    //     boolean isFireballDestroyed = Engine.handleFireball(fireballA, stonewalls, brickwalls);
    //     assertEquals(isFireballDestroyed, false);


    //     fireballA.posX = fireballA.posX + fireballA.velocityX;
    //     fireballA.posY = fireballA.posY + fireballA.velocityY;
    //     fireballA.bounds.setBounds(fireballA.posX, fireballA.posY);
 
    //     isFireballDestroyed = Engine.handleFireball(fireballA, stonewalls, brickwalls);
    //     assertEquals(isFireballDestroyed, true); 
    // }

}
