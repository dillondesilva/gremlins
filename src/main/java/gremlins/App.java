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

    public boolean boundsCheckInProgress;
    public boolean validMoveKeyPressed;

    public Progress fireballProgress;
    public Progress icewallProgress;

    public boolean isGremlinsFrozen;

    public char[][] map = new char[37][37];

    public ArrayList<Fireball> activeFireballs = new ArrayList<Fireball>();
    public ArrayList<Gremlin> activeGremlins = new ArrayList<Gremlin>();
    public ArrayList<Wall> activeWalls = new ArrayList<Wall>();

    public ArrayList<StoneWall> activeStoneWalls = new ArrayList<StoneWall>();
    public ArrayList<BrickWall> activeBrickWalls = new ArrayList<BrickWall>();
    public ArrayList<Ground> groundTiles = new ArrayList<Ground>();
    public ArrayList<Slime> activeSlimes = new ArrayList<Slime>();

    public EscapeDoor escapeDoor;

    public JSONObject configuration;
    public JSONArray levelsData;

    private Runner runner;

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
            configuration = loadJSONObject(new File(this.configPath));
            levelsData = configuration.getJSONArray("levels");
            
            this.numLevels = levelsData.size();
        } catch (Exception e) {
            System.out.println("Error loading game. Please check configuration");
        }

        this.currentLevel = 1;
        initializeLevel(this.currentLevel, 3);

        drawMap();
    }

    public void initializeLevel(int levelNum, int numLives) {
        // String levelFileName = String.format("level%d.txt", levelNum);

        activeFireballs = new ArrayList<Fireball>();
        activeGremlins = new ArrayList<Gremlin>();
        activeWalls = new ArrayList<Wall>();

        activeStoneWalls = new ArrayList<StoneWall>();
        activeBrickWalls = new ArrayList<BrickWall>();
        groundTiles = new ArrayList<Ground>();
        activeSlimes = new ArrayList<Slime>();

        lifePowerup = new Life(this, 0, 0);
        icewall = new IceWall(this, 0, 0);
        
        fireballProgress = new Progress(this, 500, 670, 120, 10);
        icewallProgress = new Progress(this, 500, 690, 120, 10);

        boundsCheckInProgress = false;
        validMoveKeyPressed = false;

        runner = new Runner(this);
        
        JSONObject levelData = this.levelsData.getJSONObject(levelNum - 1);
        double fireballCooldown = (double)levelData.get("wizard_cooldown") * 1000;
        String levelFileName = (String)levelData.get("layout");

        try {
            File layoutFile = new File(levelFileName);
            Scanner levelScanner = new Scanner(layoutFile).useDelimiter("\n");

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

                        this.player = new Player(this, colCount * 20, rowCount * 20, numLives, fireballCooldown);
                        // this.player.moveTo(this, colCount * 20, rowCount * 20);
                    } else if (tile == 'G') {
                        Gremlin gremlin = new Gremlin(this);

                        gremlin.posX = colCount * 20;
                        gremlin.posY = rowCount * 20;

                        gremlin.moveTo(this, colCount * 20, rowCount * 20);
            
                        boolean[] collidingWalls = Engine.checkWallCollisions(gremlin, activeStoneWalls, activeBrickWalls, icewall);
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
        if ((validMoveKeyPressed == false) && (boundsCheckInProgress == false)) {
            if (this.keyCode == 37) {
                validMoveKeyPressed = true;
                this.player.moveLeft(this);
            } else if (this.keyCode == 38) {
                validMoveKeyPressed = true;
                this.player.moveUp(this);
            } else if (this.keyCode == 39) {
                validMoveKeyPressed = true;
                this.player.moveRight(this);
            } else if (this.keyCode == 40) {
                validMoveKeyPressed = true;
                this.player.moveDown(this);
            }      
        }

        if (this.keyCode == 32) {
            activeFireballs = Engine.attemptFireballShoot(this, player, activeFireballs);
        }
    }
    
    /**
     * Receive key released signal from the keyboard.
    */
    public void keyReleased(){
        if (this.keyCode == 37) {
            System.out.println("left release");
            boundsCheckInProgress = true;
            validMoveKeyPressed = false;
        }

        if (this.keyCode == 38) {
            boundsCheckInProgress = true;
            validMoveKeyPressed = false;
        } 

        if (this.keyCode == 39) {
            boundsCheckInProgress = true;
            validMoveKeyPressed = false;
        }

        if (this.keyCode == 40) {
            boundsCheckInProgress = true;
            validMoveKeyPressed = false;
        } 
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

            if (fireballProgress.isActive == true) {
                fireballProgress.draw();  
            }
    
            if (icewallProgress.isActive == true) {
                icewallProgress.draw();  
            }

            Object[] updatedFireballSlimeData = Engine.checkFireballSlimeCollisions(activeFireballs, activeSlimes);
            
            activeFireballs = (ArrayList<Fireball>)updatedFireballSlimeData[0];
            activeSlimes = (ArrayList<Slime>)updatedFireballSlimeData[1];

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

            escapeDoor.draw();
            boolean[] playerWallCollisions = Engine.checkWallCollisions(player, activeStoneWalls, activeBrickWalls, icewall);
              
            Object[] playerGremlinCollisionInfo = Engine.checkPlayerGremlinCollision(player, activeGremlins);
            boolean isPlayerHit = (boolean)playerGremlinCollisionInfo[1];
    
            activeGremlins = (ArrayList<Gremlin>)playerGremlinCollisionInfo[0];

            this.runner.run();
            this.player.draw(this);

            for (Gremlin gremlin: activeGremlins) {
                if (gremlin.isAlive == true) {
                    gremlin.draw(this);
                }
            }

            for (Slime slime: activeSlimes) {
                slime.draw();
            }

            for (StoneWall stonewall: activeStoneWalls) {
                stonewall.draw(this);
            }

            
            for (BrickWall brickwall: activeBrickWalls) {
                brickwall.draw(this);
            }

            for (Fireball fireball: activeFireballs) {
                fireball.draw();
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
