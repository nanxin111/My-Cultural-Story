/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package part.pkg1.question.pkg2;
import processing.core.PApplet;
import processing.core.PImage;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.IOException;
/**
 * The main game class inherits from Processing's PApplet.
 * Functions:
 * -Game state control (start/instructions/play/end, etc)
 * -Playrt and enemy management
 * -Collision detection
 * -File reading (dialogue)
 * -File writing (game save)
 * @author nanxinyu
 */
public class MySketch extends PApplet {
    private Nuwa player;
    private Monster[] monsters=new Monster[3];
    // Images
    private PImage bgImg; // full-background.png
    private PImage platformImg; // tile 1.png
    private PImage enemyImg; // enemyImg.png
    private PImage doorImg; // door.png
    private PImage skyImg; // sky.png
    private PImage dialogImg; // dialog box.png
    private PImage startImg; // start.png
    private PImage backstoryImg; // backstory.png
    private PImage endImg; // end.png
    private PImage restartImg; // restart.png
    private PImage[] elementImgs=new PImage[5]; // gold, wood, water, fire, earth
    // The door Coordinate
    private float doorX= 650, doorY=160;
    // Colored Stone Coordinates
    private float[][] stoneCoords = {
        {150, 95}, 
        {300, 150}, 
        {450, 220},
        {600, 120},
        {700, 200}
    };
    // Platforms Coordinates
    private float[][] platforms={
        {300, 180}, {580, 150}, {100, 130}, {370, 180}
    };
    private boolean[] stoneCollected = new boolean[5]; // Whether the colored stones have been collected.
    private float[][] elementPos=new float[5][2]; // Current position of element
    private boolean[] isDragging=new boolean[5]; // Is it being dragged?
    private boolean[] isPlaced=new boolean[5]; // Has it already been placed into the Hole?
    private float holeX=384, holeY=100; // The hole Coordinate
    
    private int score = 0; // Number of colored stones collected
    private int gameState = 0; // 0=Start, 1=Play, 2=EndImg, 3=GameOver, 4=Repair, 5=Backstory, 6=Gameplay, 7=RestartImg
    private String[] allDialogLines=new String[30]; // All dialogue text
    private String currentDisplayText=""; // Current dialog
    private boolean showDialog=true; // Should a dialog box be displayed?
    /**
     * Set window size
     */
    public void settings() {
        size(768, 384);
    }
    /**
     * Initialize game resources
     */
    public void setup() {
        // Loading image resources
        bgImg = loadImage("images/full-background.png");
        platformImg = loadImage("images/tile 1.png");
        enemyImg = loadImage("images/enemyImg.png");
        doorImg = loadImage("images/door.png");
        skyImg = loadImage("images/sky.png");
        dialogImg = loadImage("images/dialog box.png");
        startImg = loadImage("images/start.png");
        backstoryImg = loadImage("images/backstory.png");
        endImg = loadImage("images/end.png");
        restartImg = loadImage("images/restart.png");
        // Load images of the five elements
        elementImgs[0] = loadImage("images/gold.png");
        elementImgs[1] = loadImage("images/wood.png");
        elementImgs[2] = loadImage("images/water.png");
        elementImgs[3] = loadImage("images/fire.png");
        elementImgs[4] = loadImage("images/earth.png");
        // Create Player
        player = new Nuwa(this, 50, 230);
        // Create enemies
        monsters[0]=new Monster(this, 335, 145, 50, enemyImg);
        monsters[1]=new Monster(this, 615, 115, 50, enemyImg);
        monsters[2]=new Monster(this, 135, 95, 50, enemyImg);
        
        resetElements(); // Initialize element position
        loadDialogFromFile(); // Read dialogue text at startup
    }
    /**
     * Each frame is called to control the overall game flow.
     */
    public void draw() { 
        if (gameState == 0) {
            drawStartScreen();
        } else if (gameState == 5 || gameState == 6) {
            drawInfoScreen();
        } else if (gameState == 1) {
            playGame();
        } else if (gameState == 4) {
            repairSky();
        } else if (gameState == 2) {
            drawEndScreen();
        } else if (gameState == 7) {
            drawRestartScreen();
        } else if (gameState == 3) {
            drawGameOverScreen();
        }
    }

    public void drawStartScreen() {
        image(startImg, 0, 0, width,height);
    }
    
    public void drawEndScreen() {
        image(endImg, 0, 0, width, height);
    }
    
    public void drawRestartScreen() {
        image(restartImg, 0, 0, width, height);
    }
    
    public void drawInfoScreen() {
        image(backstoryImg, 0, 0, width,height);
        fill(0);
        textSize(16);
        textAlign(CENTER, CENTER);
        text(currentDisplayText, 100, 50, width-200, height-100);
        
        fill(100);
        textSize(25);
        text("Press ENTER to return", width/2, height-30);
    }
    /**
     * Main game operation logic
     */
    public void playGame() {
        image(bgImg, 0, 0, width, height); // background
        player.display(); // display player
        // Detect whether the player has fallen onto the platform from above; if so, let the player stand on the platform.
        // 检测玩家是否从上方落到平台上，如果是，就让玩家站在平台上。
        for (float[] p : platforms) {
            image(platformImg, p[0], p[1], 100, 20); // display platforms
            // 检测角色的左右范围是否和平台重叠。
            if (player.x + 30 > p[0] && player.x < p[0] + 100) {
                // 检测角色的脚底是否穿过了平台的表面。
                // 确保角色只有在下落时才会触发站在平台上的动作（防止跳跃时从下方穿过平台被吸在上面）。
                if (player.y + 60 > p[1] && player.y + 60 <= p[1] + 25 && player.vy > 0) {
                    player.y = p[1] - 60; // This is where the players "land".
                    player.vy = 0;
                }
            }
        }
        // The door will appear after collecting 5 colored stones.
        // 收集满5个彩石显示门
        if (score >= 5) {
            image(doorImg, doorX, doorY, 80, 100);
            // 如果玩家距离门中心点的距离小于 30 像素，就判定为“碰到了”。
            if (checkCollision(player.x, player.y, doorX+40, doorY+50, 30)) {
                gameState=4;
                // 进入补天环节，加载20-21行文字
                currentDisplayText=getLines(20, 21);
            }
        }
            
        for (int i = 0; i < stoneCoords.length; i++) {
            if (!stoneCollected[i]) {
                fill(0, 200, 255); // light blue
                ellipse(stoneCoords[i][0], stoneCoords[i][1], 20, 20); // Draw a circle with a diameter of 20 pixels.
                // The event will trigger if the player's center point is within 40 pixels of the center point of the stone.
                if (checkCollision(player.x, player.y, stoneCoords[i][0], stoneCoords[i][1], 40)) {
                    stoneCollected[i] = true; // The stone will not be drawn in the next frame.
                    score++;
                }
            }
        }
        // 遍历在 setup 中创建的 monsters 数组（里面有 3 个 Monster 对象）。
        for (Monster m : monsters) {
            m.display(); // 调用了 Monster.java 里的 display() 方法，在 Monster 类中，display() 内部先调用了 moveAI()
            // 如果玩家中心和怪物中心的距离小于 35 像素，判定为“撞上了”。
            if (checkCollision(player.x, player.y, m.x, m.y, 35)) {
                player.takeDamage(); // 调用了Nuwa.java里的takeDamage()方法
                if (player.health <= 0) gameState = 3; // 没命就GameOver
            }
        }
         
        // UI
        fill(255); // black
        textSize(20);
        textAlign(LEFT); // 左对齐
        text("Stones: "+score+"/5", 20, 40);
        fill(255, 0, 0); // red
        text("Health: "+player.health, 20, 70);
        // Keyboard movement
        if (keyPressed) {
            if (keyCode == LEFT) player.move(-1);
            if (keyCode == RIGHT) player.move(1);
        }
    }
    // Handles the sky repair mini-game logic
    public void repairSky() {
        image(skyImg, 0, 0, width, height); // draw sky background
        
        if (showDialog) {
            // display dialog box and text
            image(dialogImg, 84, 230, 600, 140);
            fill(0);
            textSize(16);
            textAlign(CENTER, CENTER);
            text(currentDisplayText, 124, 225, 520, 100);
        } else {
            noFill(); // no inner color
            stroke(255, 200, 0); // set stroke color to yellow
            strokeWeight(3); // set border thickness
            ellipse(holeX, holeY, 80, 80); // draw the circle/hole
            
            int finishedCount=0; // track placed elements
            for (int i=0; i<5; i++) {
                // Update position if dragging
                if (isDragging[i]) {
                    elementPos[i][0]=mouseX-25; // 如果当前石头被鼠标点中了（isDragging 为真），它的坐标就变成鼠标坐标（减去 25 是为了让鼠标处于图片中心）。
                    elementPos[i][1]=mouseY-25;
                }
                // 如果石头还没被放进洞里，就用 image() 把它画出来；如果已经放进去了，就不再画它。
                if (!isPlaced[i]) {
                    image(elementImgs[i], elementPos[i][0], elementPos[i][1], 50, 50);
                } else {
                    // 如果石头已经放好了（isPlaced 为真），计数器就加 1。
                    finishedCount++;
                }
            }
            // 全拖完就去gameState=2（End）
            if (finishedCount == 5) {
                gameState=2;
                saveGameRecord();
            }
        }
    }
    
    public void mousePressed() {
        if (gameState == 0) {
            // 假设BACKSTORY按钮在左侧（290， 170）宽高170，30
            if (mouseX>290 && mouseX<460 && mouseY>170 && mouseY<200) {
                currentDisplayText=getLines(1, 7); // Load backstory
                gameState=5;
            } else if (mouseX>290 && mouseX<460 && mouseY>240 && mouseY<270) { // 假设GAMEPLAY 按钮在右侧 (290, 150) 宽高 200, 80
                currentDisplayText=getLines(10, 17); // Load gameplay info
                gameState = 6;
            }
        } else if (gameState == 4 && !showDialog) {
            // Logic for picking up stones in repair mode
            for (int i=0; i<5; i++) {
                if (mouseX > elementPos[i][0] && mouseX < elementPos[i][0]+50 &&
                    mouseY > elementPos[i][1] && mouseY < elementPos[i][1]+50) {
                    isDragging[i]=true; // Start dragging
                    break;                
                }
            }
        } else if (gameState==7) {
            // Restart button on restart screen
            if (mouseX>200 && mouseX<560 && mouseY>240 && mouseY<270) {
                resetToStart(); // Go back to main menu（start.png)
            }
        }
    }
    
    /**
     * Handles mouse release to "drop" elements into the sky hole.
     */
    public void mouseReleased() {
        if (gameState==4) {
            for (int i=0; i<5; i++) {
                if (isDragging[i]) {
                    // Check if dropped near the hole
                    if (dist(elementPos[i][0]+25, elementPos[i][1]+25, holeX, holeY) < 60) {
                        isPlaced[i]=true; // Snap to place
                    }
                    isDragging[i]=false; // Stop dragging anyway
                }
            }
        }
    }

    /**
     * Handles keyboard shortcuts for jumping and state transitions.
     */
    public void keyPressed() {
        if (keyCode==UP && gameState == 1) player.jump();
        if (keyCode == ENTER) {
            if (gameState==0) gameState=1; // 初始页面回车直接开始
            else if (gameState==5 || gameState==6) gameState=0; // 说明页面回车返回初始
            else if (gameState==3) resetGame(); // Restart after death
            else if (gameState == 4 && showDialog) showDialog=false; // Close dialog
            else if (gameState == 2) gameState=7; // Proceed to restart screen
        }
    }
    /**
     * Resets basic game variables for a new attempt.
     */
    public void resetGame() {
        gameState=1; score=0; showDialog=true;
        player.health=3; player.x=50; player.y=230;
        for (int i=0; i<5; i++) {
            stoneCollected[i]=false;
        }
        resetElements(); // Reset stone positions
    }
    /**
     * Fully resets the game back to the menu screen.
     */
    public void resetToStart() {
        resetGame();
        gameState=0;
    }
    /**
     * Resets positions of the five elements.
     */
    private void resetElements() {
        for (int i=0; i<5; i++) {
            elementPos[i][0]=150 + i * 110; // 横向排列
            elementPos[i][1]=300;
            isPlaced[i]=false;
            isDragging[i]=false;
        }
    }
    /**
     * Renders the Game Over
     */
    public void drawGameOverScreen() {
        background(50, 0, 0); // dark red
        fill(255); // black
        textAlign(CENTER);
        text("GAME OVER\nPress ENTER to Restart", width/2, height/2);
    }
    /**
     * Helper to check circular collision between two points.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param range
     * @return 
     */
    public boolean checkCollision(float x1, float y1, float x2, float y2, float range) {
        return dist(x1, y1, x2, y2)<range; // distance check
    }
    
    /**
     * Loads text lines from an file into the dialog array.
     */
    public void loadDialogFromFile() {
        try {
            File file=new File(sketchPath("dialog.txt"));
            if (file.exists()) {
                Scanner scanner=new Scanner(file);
                int lineCount=0;
                while (scanner.hasNextLine()) {
                    allDialogLines[lineCount]=scanner.nextLine();
                    lineCount++;
                }
                scanner.close();
            }
        } catch (Exception e) {
            println("Error reading dialog.txt");
        }
    }
    
    /**
     * Retrieves specific lines from the dialog array.
     * @param start 1-based start line
     * @param end 1-based end line
     * @return 
     */
    public String getLines(int start, int end) {
        String result="";
        for (int i=start-1; i<end; i++) {
            result += allDialogLines[i]+"\n";
        }
        return result;
    }
    /**
     * Saves the victory times to a text file.
     */
    public void saveGameRecord() {
        try{
            // Create a FileWriter with 'append' mode set to true
            FileWriter fw=new FileWriter("game_log.txt", true);
            PrintWriter pw=new PrintWriter(fw);
            // Write a new line with current hour and minute
            pw.println("Won at time: "+hour()+":"+minute());
            pw.close(); // close
            println("Game saved successfully.");
        } catch (IOException e) {
            println("Error saving file: "+e.getMessage());
        }
    }
}