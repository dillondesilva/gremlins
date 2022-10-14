package gremlins;

public class CollisionDetector {
    private static boolean isInYBounds(Collider colliderA, Collider colliderB) {

        boolean isColliderPartialAbove = (colliderA.bounds.bottom > colliderB.bounds.top) & (colliderA.bounds.bottom < colliderB.bounds.bottom);
        boolean isColliderPartialBelow = (colliderA.bounds.top > colliderB.bounds.top) & (colliderA.bounds.top < colliderB.bounds.bottom);
        boolean isColliderExactBetween = (colliderA.bounds.top == colliderB.bounds.top) & (colliderA.bounds.bottom == colliderB.bounds.bottom);
        
        boolean isInYRange = (isColliderPartialAbove) || (isColliderPartialBelow) || (isColliderExactBetween);
        return isInYRange;
    }

    private static boolean isInXBounds(Collider colliderA, Collider colliderB) {

        boolean isColliderPartialLeft = (colliderA.bounds.right > colliderB.bounds.left) && (colliderA.bounds.right < colliderB.bounds.right);
        boolean isColliderPartialRight = (colliderA.bounds.left > colliderB.bounds.left) && (colliderA.bounds.left < colliderB.bounds.right);
        boolean isColliderExactBetween = (colliderA.bounds.left == colliderB.bounds.left) && (colliderA.bounds.right == colliderB.bounds.right);

        boolean isInXRange = isColliderPartialLeft || isColliderPartialRight || isColliderExactBetween;
        return isInXRange;
    }

    public static boolean isRightColliding(Collider colliderA, Collider colliderB) {
        boolean isInYRange = isInYBounds(colliderA, colliderB);
        boolean isRightBoundColliding = (colliderA.bounds.right + 2 > colliderB.bounds.left) & (colliderA.bounds.right + 2 < colliderB.bounds.right);
        if (isRightBoundColliding == true) {
            if (isInYRange == true) {
                return true; 
            }   
        }

        return false;
    }

    public static boolean isLeftColliding(Collider colliderA, Collider colliderB) {
        boolean isInYRange = isInYBounds(colliderA, colliderB);
        boolean isLeftBoundColliding = (colliderA.bounds.left - 2 > colliderB.bounds.left) & (colliderA.bounds.left - 2 < colliderB.bounds.right);

        if (isLeftBoundColliding == true) {
            // System.out.printf("LeftBoundColliding %b\n", isLeftBoundColliding);
            // System.out.println("hit");
            if (isInYRange == true) {
                // System.out.printf("LeftBoundColliding %b\n", isLeftBoundColliding);
                // System.out.printf("InYRange: %b\n", isInYRange);
                return true; 
            }   
        }

        return false;
    }

    public static boolean isTopColliding(Collider colliderA, Collider colliderB) {
        boolean isInXRange = isInXBounds(colliderA, colliderB);
        boolean isTopBoundColliding = (colliderA.bounds.top - 2 > colliderB.bounds.top) && (colliderA.bounds.top - 2 < colliderB.bounds.bottom);

        if (isInXRange == true) {
            // System.out.printf("InXRange: %b\n", isInXRange);
            if (isTopBoundColliding == true) {
                // System.out.printf("topBoundColliding %b\n", isTopBoundColliding);
                // System.out.printf("InYRange: %b\n", isInXRange);
                return true; 
            }   
        }

        return false;
    }

    public static boolean isBottomColliding(Collider colliderA, Collider colliderB) {
        boolean isInXRange = isInXBounds(colliderA, colliderB);
        boolean isBottomBoundColliding = (colliderA.bounds.bottom + 2 > colliderB.bounds.top) & (colliderA.bounds.bottom + 2 < colliderB.bounds.bottom);
        if (isBottomBoundColliding == true) {
            if (isInXRange == true) {
                return true; 
            }   
        }

        return false;
    }

    public static boolean[] checkCollisions(Collider colliderA, Collider colliderB) {
        // Boolean array for each direction
        boolean[] isCollidingWithWall = new boolean[] {false, false, false, false};

        boolean isLeftColliding = CollisionDetector.isLeftColliding(colliderA, colliderB);
        boolean isRightColliding = CollisionDetector.isRightColliding(colliderA, colliderB);
        boolean isTopColliding = CollisionDetector.isTopColliding(colliderA, colliderB);
        boolean isBottomColliding = CollisionDetector.isBottomColliding(colliderA, colliderB);

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

        return isCollidingWithWall; 
    }

    public static double getDistanceBetween(Collider colliderA, Collider colliderB) {
        double deltaX = colliderA.posX - colliderB.posX;
        double deltaY = colliderA.posY - colliderB.posY;
       
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }
}