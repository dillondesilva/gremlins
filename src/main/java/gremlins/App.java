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

    public int numLevels;

    public static final Random randomGenerator = new Random();

    public String configPath;

    public int currentLevel;
    
    public BrickWall brickwall;
    public StoneWall stonewall;

    public Life lifePowerup;
    public IceWall icewall;

    public Player player;

    public boolean isWon = true;
    public int gameActiveState;

    public char[][] map = new char[37][37];

    public ArrayList<Fireball> activeFireballs = new ArrayList<Fireball>();
    public ArrayList<Gremlin> activeGremlins = new ArrayList<Gremlin>();
    public ArrayList<Wall> activeWalls = new ArrayList<Wall>();

    public ArrayList<StoneWall> activeStoneWalls = new ArrayList<StoneWall>();
    public ArrayList<BrickWall> activeBrickWalls = new ArrayList<BrickWall>();
    public ArrayList<Ground> groundTiles = new ArrayList<Ground>();
    public ArrayList<Slime> activeSlimes = new ArrayList<Slime>();

    public EscapeDoor escapeDoor;

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
        frameRate(FPS);
        this.gameActiveState = 0; 

        try {
            JSONObject conf = loadJSONObject(new File(this.configPath));
            JSONArray newObj = conf.getJSONArray("levels");
            
            this.numLevels = newObj.size();
        } catch (Exception e) {
            System.out.println("Error loading game. Please check configuration");
        }

        this.currentLevel = 1;
        initializeLevel(this.currentLevel, 3);

        drawMap();
    }

    public void initializeLevel(int levelNum, int numLives) {
        String levelFileName = String.format("level%d.txt", levelNum);
        
        activeFireballs = new ArrayList<Fireball>();
        activeGremlins = new ArrayList<Gremlin>();
        activeWalls = new ArrayList<Wall>();

        activeStoneWalls = new ArrayList<StoneWall>();
        activeBrickWalls = new ArrayList<BrickWall>();
        groundTiles = new ArrayList<Ground>();
        activeSlimes = new ArrayList<Slime>();

        lifePowerup = new Life(this, 0, 0);
        icewall = new IceWall(this, 0, 0);
        
        try {
            File levelData = new File(levelFileName);
            Scanner levelScanner = new Scanner(levelData).useDelimiter("\n");

            map = new char[37][37];

            int rowCount = 0;
            int colCount = 0;

            while (levelScanner.hasNext()) {
                String tileRow = levelScanner.next();
                int tileRowCount = 0;
                System.out.println(tileRow);
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

                        this.player = new Player(this, colCount * 20, rowCount * 20, numLives);
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
            System.out.printf("%s doesn't exist\n", levelFileName);
        }
    }

    public void loadNextLevel() {
        if (this.currentLevel == this.numLevels) {
            this.gameActiveState = 1;
        } else {
            this.currentLevel += 1;
            System.out.println(this.currentLevel);
            initializeLevel(this.currentLevel, this.player.health.numLives);
            drawMap();     
        }
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

                if (map[rowCount][colCount] == 'E') {
                    escapeDoor = new EscapeDoor(this, colCount * 20, rowCount * 20);
                }

                colCount++;
            }

            rowCount++;
        }
    }

    /**
     * Draw all elements in the game by current frame. 
	 */
    public void draw() {
        background(169, 149, 123);

        if (this.gameActiveState == 0) {

            if (this.player.health.numLives == 0) {
                this.gameActiveState = 2;
            }

            Engine.handleLifePowerUpSpawn(player, lifePowerup, groundTiles);
            Engine.checkPlayerGainsLife(player, lifePowerup);

            Engine.handleIceWallSpawn(player, icewall, groundTiles);
            // Engine.checkPlayerShotWall(player, lifePowerup);
            
            if (icewall.isActive == true) {
                icewall.draw();
            }

            if (lifePowerup.isActive == true) {
                lifePowerup.draw();
            }

            boolean isAtExit = Engine.checkPlayerAtExit(player, escapeDoor);
            activeSlimes = Engine.monitorGremlinSpawns(player, activeGremlins, groundTiles, activeSlimes);
            if (isAtExit == true) {
                loadNextLevel();
            }

            Iterator<BrickWall> brickwallItr = this.activeBrickWalls.iterator();

            while (brickwallItr.hasNext()) {
                BrickWall brickwall = brickwallItr.next();

                boolean isDestroyed = brickwall.tick();
                    
                if (isDestroyed == true) {

                    brickwallItr.remove();
                }
  
            }

            escapeDoor.draw();
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
                ArrayList<Integer> possibleHeadings = new ArrayList<Integer>();

                int heading = 0;
                while (heading < collidingWalls.length) {
                    if (collidingWalls[heading] == true) {
                        possibleHeadings.add(heading);
                    }

                    heading += 1;
                }
                
                if (gremlin.isAlive == true) {
                    if ((collidingWalls[currentGremlinHeading] == true)) {
                        int newGremlinHeading = (int)Math.floor(Math.random() * (3 + 1));

                        if (possibleHeadings.size() > 1) {
                            int oppositeHeading = Engine.getOppositeHeading(currentGremlinHeading);

                            newGremlinHeading = (int)Math.floor(Math.random() * (3 + 1));

                            while ((collidingWalls[newGremlinHeading] == true) && (newGremlinHeading == oppositeHeading)) {
                                newGremlinHeading = (int)Math.floor(Math.random() * (3 + 1)); 
                            } 
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
    
            Iterator<Slime> slimeItr = activeSlimes.iterator();
    
            
            while (slimeItr.hasNext()) {
                Slime slime = slimeItr.next();
    
                boolean isSlimeDestroyed = (Engine.handleSlime(slime, activeStoneWalls, activeBrickWalls));
                Object[] playerSlimeCollisionData = Engine.checkSlimePlayerCollision(this.player, activeSlimes);
                activeSlimes = (ArrayList<Slime>)playerSlimeCollisionData[0];
                
                if (isSlimeDestroyed == false) {
                    slime.move();  
                    slime.draw(this);  
                } else {
                    slimeItr.remove();      
                }   
            }

            for (StoneWall stonewall: activeStoneWalls) {
                stonewall.draw(this);
            }

            
            for (BrickWall brickwall: activeBrickWalls) {
                brickwall.draw(this);
            }

            this.player.health.bar();
            textSize(20);
            text("Lives: ", 20, 695); 
    
            String levelDisplay = String.format("Level: %d/%d", this.currentLevel, this.numLevels);
            text(levelDisplay, 300, 695); 

        } else if (this.gameActiveState == 1) {
            textSize(30);
            text("You Won!!!", 280, 360);     
        } else {
            textSize(30);
            text("Game Over!!!", 280, 360);    
        }
    }

    public static void main(String[] args) {
        PApplet.main("gremlins.App");
    }
}
