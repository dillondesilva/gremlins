package gremlins;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;

import processing.core.PApplet;


public class TestApp {
    public static PApplet app;

    public static Player player;

    public static ArrayList<StoneWall> stonewalls;
    public static ArrayList<BrickWall> brickwalls;

    @BeforeAll
    public static void setup() {
        // Creating a fake context
        // app = new App();

        // StoneWall stoneWallA = new StoneWall(app, 0, 0);
        // StoneWall stoneWallB = new StoneWall(app, 40, 40);

        // stonewalls.add(stoneWallA);
        // stonewalls.add(stoneWallB);

        // player = new Player(app, 20, 0, 3);
    }

    @Test
    public void testAppBuilds() {
        boolean isAppBuilt = true;

        try {
            app = new App();
        } catch (Exception e) {
            isAppBuilt = false;
        }

        assert(isAppBuilt == true);
        // StoneWall stoneWallA = new StoneWall(app, 0, 0);
        // StoneWall stoneWallB = new StoneWall(app, 40, 40);

        // stonewalls.add(stoneWallA);
        // stonewalls.add(stoneWallB);


        // player = new Player(app, 20, 0, 3);

        // boolean[] wallCollisionData = Engine.checkWallCollisions(player, stonewalls, brickwalls);

        // boolean[] expectedWallCollisionData = new boolean[] {true, false, false, false};
        // assertArrayEquals(wallCollisionData, expectedWallCollisionData);
    }

    @Test
    public void testAppDraw() {
        boolean doesAppDraw = true;

        try {
            app = new App();
            app.setup();
            app.draw();
        } catch (Exception e) {
            doesAppDraw = false;
        }

        assert(doesAppDraw == true);
    }

}
