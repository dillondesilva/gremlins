package gremlins;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import gremlins.Engine;

import java.io.*;

import java.io.File;
import java.io.FileNotFoundException;


public class App extends PApplet {

    public static final int WIDTH = 720;
    public static final int HEIGHT = 720;
    public static final int SPRITESIZE = 20;
    public static final int BOTTOMBAR = 60;

    public static final int FPS = 60;

    public static final Random randomGenerator = new Random();

    public String configPath;
    
    public BrickWall brickwall;
    public StoneWall stonewall;

    public Player player;

    public boolean isStart = true;

    public char[][] map = new char[37][37];

    public ArrayList<Fireball> activeFireballs = new ArrayList<Fireball>();
    public ArrayList<Gremlin> activeGremlins = new ArrayList<Gremlin>();
    public ArrayList<Wall> activeWalls = new ArrayList<Wall>();

    public ArrayList<StoneWall> activeStoneWalls = new ArrayList<StoneWall>();
    public ArrayList<BrickWall> activeBrickWalls = new ArrayList<BrickWall>();
    public ArrayList<Ground> groundTiles = new ArrayList<Ground>();

    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
    */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
    */
    public void setup() {
        drawMap();
        frameRate(FPS);

        // Load images during setup
        // this.stonewall = new StoneWall(this);
        // this.brickwall = new BrickWall(this);

        try {
            File levelOneData = new File("level1.txt");
            Scanner levelScanner = new Scanner(levelOneData).useDelimiter("\n");

            int rowCount = 0;
            int colCount = 0;

            while (levelScanner.hasNext()) {
                String tileRow = levelScanner.next();
                int tileRowCount = 0;

                while (tileRowCount < tileRow.length()) {
                    char tile = tileRow.charAt(tileRowCount);
                    // System.out.printf("Row Count: %d, Col Count: %d, Character: %c\n", rowCount, colCount, tile);
                    if (colCount > 36) {
                        colCount = 0;
                        rowCount++;
                    }

                    if (tile == 'W') {
                        float x = colCount * 20;
                        float y = rowCount * 20;

                        this.player = new Player(this, colCount * 20, rowCount * 20, 3);
                        // this.player.moveTo(this, colCount * 20, rowCount * 20);
                    } else if (tile == 'G') {
                        Gremlin gremlin = new Gremlin(this);

                        gremlin.posX = colCount * 20;
                        gremlin.posY = rowCount * 20;

                        gremlin.moveTo(this, colCount * 20, rowCount * 20);
            
                        boolean[] collidingWalls = Engine.checkWallCollisions(gremlin, activeStoneWalls, activeBrickWalls);
                        int newGremlinHeading = (int)Math.floor(Math.random() * (3 + 1));
                        while (collidingWalls[newGremlinHeading] == true) {
                            newGremlinHeading = (int)Math.floor(Math.random() * (3 + 1)); 
                        }
                        
                        gremlin.changeHeading(newGremlinHeading);

                        activeGremlins.add(gremlin);
                    }

                    map[rowCount][colCount] = tile;
                    tileRowCount++;
                    colCount++; 
                }

            }

            levelScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("level2.txt doesn't exist");
        }

        
        //this.gremlin = loadImage(this.getClass().getResource("gremlin.png").getPath().replace("%20", ""));
        //this.slime = loadImage(this.getClass().getResource("slime.png").getPath().replace("%20", ""));
        //this.fireball = loadImage(this.getClass().getResource("fireball.png").getPath().replace("%20", ""));
        
        JSONObject conf = loadJSONObject(new File(this.configPath));
        drawMap();
    }

    public boolean detectRightCollision(Collider collider) {
        int rowCount = 0;


        while(rowCount < map.length) {
            int colCount = 0;
            while (colCount < map[1].length) {
               
                char brick = 'B';
                char stone = 'X';

                int leftX = colCount * 20;
                int rightX = (colCount * 20) + 20;
                int topY = rowCount * 20;
                int bottomY = (rowCount * 20) + 20;

                float colliderRightX = collider.posX + 20;
                float colliderTopY = collider.posY;
                float colliderBottomY = collider.posY + 20;


                if ((map[rowCount][colCount] == brick) || (map[rowCount][colCount] == stone) ) {
                    if (((colliderRightX + 2 > leftX) && (colliderRightX + 2 < rightX))) {
                        if (((collider.posY > topY) && (collider.posY < bottomY)) || ((colliderBottomY > topY) && (colliderBottomY < bottomY)) || ((colliderTopY == topY) && (colliderBottomY == bottomY))) {
                            return true; 
                        }   
                    }
                }

                colCount++;
            }

            rowCount++;
        } 

        return false;
    }

    public boolean detectLeftCollision(Collider collider) {
        int rowCount = 0;


        while(rowCount < map.length) {
            int colCount = 0;
            while (colCount < map[1].length) {
               
                char brick = 'B';
                char stone = 'X';

                int leftX = colCount * 20;
                int rightX = (colCount * 20) + 20;
                int topY = rowCount * 20;
                int bottomY = (rowCount * 20) + 20;
     
                float colliderLeftX = collider.posX;
                float colliderTopY = collider.posY;
                float colliderBottomY = collider.posY + 20;

                if ((map[rowCount][colCount] == brick) || (map[rowCount][colCount] == stone) ) {
                    if (((colliderLeftX - 2 > leftX) && (colliderLeftX - 2 < rightX))) {
                        if (((colliderTopY > topY) && (colliderTopY < bottomY)) || ((colliderBottomY > topY) && (colliderBottomY < bottomY)) || ((colliderTopY == topY) && (colliderBottomY == bottomY))) {
                            return true; 
                        }   
                    }
                }

                colCount++;
            }

            rowCount++;
        } 

        return false;
    }

    public boolean detectUpCollision(Collider collider) {
        int rowCount = 0;


        while(rowCount < map.length) {
            int colCount = 0;
            while (colCount < map[1].length) {
               
                char brick = 'B';
                char stone = 'X';

                int leftX = colCount * 20;
                int rightX = (colCount * 20) + 20;
                int topY = rowCount * 20;
                int bottomY = (rowCount * 20) + 20;

                float colliderRightX = collider.posX + 20;
  

                if ((map[rowCount][colCount] == brick) || (map[rowCount][colCount] == stone) ) {
                    if (((collider.posX > leftX) && (collider.posX < rightX)) || ((colliderRightX > leftX) && (colliderRightX < rightX)) || ((collider.posX == leftX) && (colliderRightX == rightX))) {
                        if ((collider.posY - 2 > topY) && (collider.posY - 2 < bottomY)) {
                            return true; 
                        }   
                    }
                }

                colCount++;
            }

            rowCount++;
        } 

        return false;
    }

    public boolean detectDownCollision(Collider collider) {
        int rowCount = 0;


        while(rowCount < map.length) {
            int colCount = 0;
            while (colCount < map[1].length) {
               
                char brick = 'B';
                char stone = 'X';

                int leftX = colCount * 20;
                int rightX = (colCount * 20) + 20;
                int topY = rowCount * 20;
                int bottomY = (rowCount * 20) + 20;

                float colliderRightX = collider.posX + 20;
                float colliderBottomY = collider.posY + 20;

                if ((map[rowCount][colCount] == brick) || (map[rowCount][colCount] == stone) ) {
                    if (((collider.posX > leftX) && (collider.posX < rightX)) || ((colliderRightX > leftX) && (colliderRightX < rightX)) || ((collider.posX == leftX) && (colliderRightX == rightX))) {
                        if ((colliderBottomY + 2 > topY) && (colliderBottomY + 2 < bottomY)) {
                            return true; 
                        }   
                    }
                }

                colCount++;
            }

            rowCount++;
        } 

        return false; 
    }

    /**
     * Receive key pressed signal from the keyboard.
    */
    public void keyPressed(){
        if (this.keyCode == 37) {
            this.player.moveLeft(this);
        }

        if (this.keyCode == 38) {
            this.player.moveUp(this);
        } 

        if (this.keyCode == 39) {
            this.player.moveRight(this);
        }

        if (this.keyCode == 40) {
            this.player.moveDown(this);
        } 

        if (this.keyCode == 32) {
            if (this.player.directionFacing.equals("Right")) {
                Fireball newFireball = new Fireball(this, this.player.posX, this.player.posY, 4, 0, 1);
                activeFireballs.add(newFireball);
            } else if (this.player.directionFacing.equals("Left")) {
                Fireball newFireball = new Fireball(this, this.player.posX, this.player.posY, -4, 0, 0);
                activeFireballs.add(newFireball);
            } else if (this.player.directionFacing.equals("Up")) {
                Fireball newFireball = new Fireball(this, this.player.posX, this.player.posY, 0, -4, 2);
                activeFireballs.add(newFireball);
            } else {
                Fireball newFireball = new Fireball(this, this.player.posX, this.player.posY, 0, 4, 3);
                activeFireballs.add(newFireball);
            }
        }
    }
    
    /**
     * Receive key released signal from the keyboard.
    */
    public void keyReleased(){
        this.player.velocityX = 0;
        this.player.velocityY = 0;
    }

    public void drawMap() {
        int rowCount = 0;

        while(rowCount < map.length) {
            int colCount = 0;
            while (colCount < map[1].length) {
               
                char brick = 'B';
                char stone = 'X';
                char player = 'W';


                if (map[rowCount][colCount] == brick) {
                    BrickWall brickwall = new BrickWall(this, colCount*20, rowCount*20);
                    activeWalls.add(brickwall);
                    activeBrickWalls.add(brickwall);
                }

                if (map[rowCount][colCount] == stone) {
                    StoneWall stonewall = new StoneWall(this, colCount*20, rowCount*20);
                    activeStoneWalls.add(stonewall);
                    activeWalls.add(stonewall);
                }

                if (map[rowCount][colCount] == ' ') {
                    Ground groundtile = new Ground(this, colCount * 20, rowCount * 20);
                    groundTiles.add(groundtile);
                }

                colCount++;
            }

            rowCount++;
        }
    }

    public void tick() {
        // background(169, 149, 123);
        // drawMap();
    
        // Iterator<Fireball> fireballItr = activeFireballs.iterator();

        // while (fireballItr.hasNext()) {
        //     Fireball fireball = fireballItr.next();

        //     boolean isLeftColliding = detectLeftCollision(fireball);
        //     boolean isRightColliding = detectRightCollision(fireball);
        //     boolean isUpColliding = detectUpCollision(fireball);
        //     boolean isDownColliding = detectDownCollision(fireball);
        //     System.out.println(isLeftColliding);
        //     if ((isLeftColliding == false) && (isRightColliding == false)) {
        //         fireball.move(this);    
        //     } else {
        //         fireballItr.remove();
        //     }   
        // }

        // for (Gremlin gremlin: activeGremlins) {
        //     gremlin.draw(this);
        // }

        // for (Wall wall: activeWalls) {
        //     wall.draw(this);
        // }  
    }
    /**
     * Draw all elements in the game by current frame. 
	 */
    public void draw() {
        background(169, 149, 123);

        boolean[] playerWallCollisions = Engine.checkWallCollisions(player, activeStoneWalls, activeBrickWalls);
          
        Object[] playerGremlinCollisionInfo = Engine.checkPlayerGremlinCollision(player, activeGremlins);
        boolean isPlayerHit = (boolean)playerGremlinCollisionInfo[1];

        activeGremlins = (ArrayList<Gremlin>)playerGremlinCollisionInfo[0];

        if ((this.player.directionFacing == "Left") && (playerWallCollisions[0] == false)) {
            this.player.move();
        } else if ((this.player.directionFacing == "Right") && (playerWallCollisions[1] == false)) {
            this.player.move();
        } else if ((this.player.directionFacing == "Up") && (playerWallCollisions[2] == false)) {
            this.player.move();
        } else if ((this.player.directionFacing == "Down") && (playerWallCollisions[3] == false)) {
            this.player.move();
        }

        this.player.draw(this);

        Iterator<Fireball> fireballItr = activeFireballs.iterator();

        while (fireballItr.hasNext()) {
            Fireball fireball = fireballItr.next();

            activeBrickWalls = Engine.renderBrickWalls(fireball, activeBrickWalls);
            Object[] fireballGremlinCollisionData = Engine.checkFireballGremlinCollision(fireball, activeGremlins);
            activeGremlins = (ArrayList<Gremlin>)fireballGremlinCollisionData[0];
            boolean isFireballDestroyed = (Engine.handleFireball(fireball, activeStoneWalls, activeBrickWalls));
            
            if (isFireballDestroyed == false) {
                fireball.move(this);    
            } else {
                fireballItr.remove();      
            }   
        }

        for (Gremlin gremlin: activeGremlins) {
            boolean[] collidingWalls = Engine.checkWallCollisions(gremlin, activeStoneWalls, activeBrickWalls);
            int currentGremlinHeading = gremlin.headingDirection;
            if (gremlin.isAlive == true) {
                if ((collidingWalls[currentGremlinHeading] == true)) {
                    int newGremlinHeading = (int)Math.floor(Math.random() * (3 + 1));
                    while (collidingWalls[newGremlinHeading] == true) {
                        newGremlinHeading = (int)Math.floor(Math.random() * (3 + 1)); 
                    }
    
                    gremlin.changeHeading(newGremlinHeading);
                    gremlin.move();
                    gremlin.draw(this);
                } else {
                    gremlin.move();
                    gremlin.draw(this);
                }  
            }
        }
        
        Engine.monitorGremlinSpawns(player, activeGremlins, groundTiles);
        this.player.health.bar();
        // for (Gremlin gremlin: activeGremlins) {
        //     gremlin.draw(this);
        // }
        for (Wall wall: activeStoneWalls) {
            wall.draw(this);
        }

        for (Wall wall: activeBrickWalls) {
            wall.draw(this);
        }

        textSize(20);
        text("Lives: ", 20, 695); 
    }

    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }
}
