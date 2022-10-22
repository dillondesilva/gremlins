package gremlins;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;

import processing.core.PApplet;

public class TestCollider {
    public static PApplet app;

    @BeforeAll
    public static void setup() {
        // Creating a fake context
        app = new PApplet();
    }

    @Test
    public void testLeftCollision() {
        Collider colliderA = new Collider(this.app, 40, 20);
        Collider colliderB = new Collider(this.app, 20, 20);
        
        Collider colliderC = new Collider(this.app, 300, 200);
        Collider colliderD = new Collider(this.app, 20, 40);
        Collider colliderE = new Collider(this.app, 38, 20);

        boolean isALeftCollidingB = CollisionDetector.isLeftColliding(colliderA, colliderB);
        boolean isALeftCollidingC = CollisionDetector.isLeftColliding(colliderA, colliderC);
        boolean isALeftCollidingD = CollisionDetector.isLeftColliding(colliderA, colliderD);
        boolean isALeftCollidingE = CollisionDetector.isLeftColliding(colliderA, colliderE);

        assert(isALeftCollidingB == true);
        assert(isALeftCollidingC == false);
        assert(isALeftCollidingD == false);
        assert(isALeftCollidingE == true);
    }

    @Test
    public void testRightCollision() {
        Collider colliderA = new Collider(this.app, 40, 20);
        Collider colliderB = new Collider(this.app, 20, 20);
        
        Collider colliderC = new Collider(this.app, 300, 200);
        Collider colliderD = new Collider(this.app, 20, 40);
        Collider colliderE = new Collider(this.app, 38, 20);

        boolean isARightCollidingB = CollisionDetector.isRightColliding(colliderB, colliderA);
        boolean isARightCollidingC = CollisionDetector.isRightColliding(colliderC, colliderA);
        boolean isARightCollidingD = CollisionDetector.isRightColliding(colliderD, colliderA);
        boolean isARightCollidingE = CollisionDetector.isRightColliding(colliderE, colliderA);

        assert(isARightCollidingB == true);
        assert(isARightCollidingC == false);
        assert(isARightCollidingD == false);
        assert(isARightCollidingE == true);
    }

    @Test
    public void testUpCollision() {
        Collider colliderA = new Collider(this.app, 20, 20);
        Collider colliderB = new Collider(this.app, 20, 0);

        boolean isATopCollidingB = CollisionDetector.isTopColliding(colliderA, colliderB);

        assert(isATopCollidingB == true);
    }

    @Test
    public void testBottomCollision() {
        Collider colliderA = new Collider(this.app, 40, 20);
        Collider colliderB = new Collider(this.app, 40, 40);

        boolean isABottomCollidingB = CollisionDetector.isBottomColliding(colliderA, colliderB);

        assert(isABottomCollidingB == true);
    }
}
