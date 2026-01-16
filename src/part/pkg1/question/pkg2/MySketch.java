/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package part.pkg1.question.pkg2;
/**
 *
 * @author nanxinyu
 */
import processing.core.PApplet;
import processing.core.PImage;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.IOException;

public class MySketch extends PApplet {
    Nuwa player;
    Monster[] monsters=new Monster[3];
    PImage bgImg; // full-background.png
    PImage platformImg; // tile 1.png
    PImage enemyImg; // enemyImg.png
    PImage doorImg;
    PImage skyImg;
    PImage dialogImg;
    PImage[] elementImgs=new PImage[5]; // gold, wood, water, fire, earth
    
    float doorX= 650, doorY=160;
    float[][] stoneCoords = {
        {150, 95}, 
        {300, 150}, 
        {450, 220},
        {600, 120},
        {700, 200}
    };
    float[][] platforms={
        {300, 180}, {580, 150}, {100, 130}, {370, 180}
    };
    boolean[] stoneCollected = new boolean[5];
    float[][] elementPos=new float[5][2];
    boolean[] isDragging=new boolean[5];
    boolean[] isPlaced=new boolean[5];
    float holeX=384, holeY=100;
    
    int score = 0;
    int gameState = 0; // 0=Start, 1=Play, 2=Win, 3=Game Over
    
    public void settings() {
        size(768, 384);
    }
    
    public void setup() {
        bgImg = loadImage("images/full-background.png");
        platformImg = loadImage("images/tile 1.png");
        enemyImg = loadImage("images/enemyImg.png");
        doorImg = loadImage("images/door.png");
        skyImg = loadImage("images/sky.png");
        dialogImg = loadImage("images/dialog box.png");
        
        elementImgs[0] = loadImage("images/gold.png");
        elementImgs[1] = loadImage("images/wood.png");
        elementImgs[2] = loadImage("images/water.png");
        elementImgs[3] = loadImage("images/fire.png");
        elementImgs[4] = loadImage("images/earth.png");
        
        player = new Nuwa(this, 50, 230);
        
        monsters[0]=new Monster(this, 335, 145, 50, enemyImg);
        monsters[1]=new Monster(this, 615, 115, 50, enemyImg);
        monsters[2]=new Monster(this, 135, 95, 50, enemyImg);
        
        resetElements();
        readGameRecord();
    }
    
    public void draw() { 
        if (gameState == 0) {
            drawStartScreen();
        } else if (gameState == 1) {
            playGame();
        } else if (gameState == 4) {
            repairSky();
        } else if (gameState == 2) {
            drawWinScreen();
        } else if (gameState == 3) {
            drawGameOverScreen();
        }
    }

    public void drawStartScreen() {
        background(0);
        fill(255);
        textSize(30);
        textAlign(CENTER);
        text("Nuwa's Adventure\nPress ENTER to Start", width / 2, height/2);
    }

    public void playGame() {
        if (bgImg != null) {
            image(bgImg, 0, 0, width, height);
        }
        player.display();
        
        for (float[] p : platforms) {
            image(platformImg, p[0], p[1], 100, 20);
            if (player.x + 30 > p[0] && player.x < p[0] + 100) {
                if (player.y + 60 > p[1] && player.y + 60 <= p[1] + 25 && player.vy > 0) {
                    player.y = p[1] - 60;
                    player.vy = 0;
                }
            }
        }
        // 收集满5个彩石显示门
        if (score >= 5) {
            image(doorImg, doorX, doorY, 80, 100);
            if (checkCollision(player.x, player.y, doorX+40, doorY+50, 50)) {
                gameState=4;
            }
        }
            
        for (int i = 0; i < stoneCoords.length; i++) {
            if (!stoneCollected[i]) {
                fill(0, 200, 255);
                ellipse(stoneCoords[i][0], stoneCoords[i][1], 20, 20);   
                if (checkCollision(player.x, player.y, stoneCoords[i][0], stoneCoords[i][1], 40)) {
                    stoneCollected[i] = true;
                    score++;
                }
            }
        }
        
        for (Monster m : monsters) {
            m.display();
            if (checkCollision(player.x, player.y, m.x, m.y, 35)) {
                player.takeDamage();
                if (player.health <= 0) gameState = 3;
            }
        }
         
        // UI
        fill(255);
        textSize(20);
        textAlign(LEFT);
        text("Stones: "+score+"/5", 20, 40);
        fill(255, 0, 0);
        text("Health: "+player.health, 20, 70);
        
        if (keyPressed) {
            if (keyCode == LEFT) player.move(-1);
            if (keyCode == RIGHT) player.move(1);
        }
    }
    
    private boolean showDialog=true;
    public void repairSky() {
        image(skyImg, 0, 0, width, height);
        
        if (showDialog) {
            image(dialogImg, 100, 250, 568, 100);
            fill(0);
            textSize(16);
            textAlign(CENTER);
            text("You've collected 5 elements. Now drag them to the sky hole\nto repair the world! Press ENTER to start.", width/2, 305);
        } else {
            noFill();
            stroke(255, 200, 0);
            strokeWeight(3);
            ellipse(holeX, holeY, 80, 80);
            
            int finishedCount=0;
            for (int i=0; i<5; i++) {
                if (isDragging[i]) {
                    elementPos[i][0]=mouseX-25;
                    elementPos[i][1]=mouseY-25;
                }
                if (!isPlaced[i]) {
                    image(elementImgs[i], elementPos[i][0], elementPos[i][1], 50, 50);
                } else {
                    finishedCount++;
                }
            }
            if (finishedCount == 5) {
                gameState=2;
                saveGameRecord();
            }
        }
    }
    
    public void mousePressed() {
        if (gameState == 4 && !showDialog) {
            // 倒序检测，确保点到的是最上面的图片
            for (int i=0; i<5; i++) {
                if (mouseX > elementPos[i][0] && mouseX < elementPos[i][0]+50 &&
                    mouseY > elementPos[i][1] && mouseY < elementPos[i][1]+50) {
                    isDragging[i]=true;
                    break;                
                }
            }
        }
    }
    
    public void mouseReleased() {
        if (gameState==4) {
            for (int i=0; i<5; i++) {
                if (isDragging[i]) {
                    if (dist(elementPos[i][0]+25, elementPos[i][1]+25, holeX, holeY) < 60) {
                        isPlaced[i]=true;
                    }
                    isDragging[i]=false;
                }
            }
        }
    }
    
    public void keyPressed() {
        if (keyCode==UP && gameState == 1) player.jump();
        if (keyCode == ENTER) {
            if (gameState==0) gameState=1;
            else if (gameState==3) resetGame();
            else if (gameState == 4 && showDialog) showDialog=false;
        }
    }
    
    public void resetGame() {
        gameState=1; score=0; showDialog=true;
        player.health=3; player.x=50; player.y=230;
        for (int i=0; i<5; i++) stoneCollected[i]=false;
        resetElements();
    }
    
    private void resetElements() {
        for (int i=0; i<5; i++) {
            elementPos[i][0]=150 + i * 110;
            elementPos[i][1]=300;
            isPlaced[i]=false;
            isDragging[i]=false;
        }
    }
        
    public void drawWinScreen() {
        background(255, 215, 0);
        fill(0);
        textAlign(CENTER);
        text("YOU WON!\nSky Repaired!", width/2, height/2);
    }
    
    
    public void drawGameOverScreen() {
        background(50, 0, 0);
        fill(255);
        textAlign(CENTER);
        text("GAME OVER\nPress ENTER to Restart", width/2, height/2);
    }
    
    public boolean checkCollision(float x1, float y1, float x2, float y2, float range) {
        return dist(x1, y1, x2, y2)<range;
    }
    
    public void readGameRecord() {
        try{
            File file=new File("game-log.txt");
            if (file.exists()) {
                Scanner scanner=new Scanner(file);
                if (scanner.hasNextLine()) {
                    String data=scanner.nextLine();
                    println("Last Game Record: "+data);
                }
                scanner.close();
            }
        } catch (Exception e) {
            println("Error reading file: "+e.getMessage());
        }
    }
    
    public void saveGameRecord() {
        try{
            FileWriter fw=new FileWriter("game_log.txt", true);
            PrintWriter pw=new PrintWriter(fw);
            
            pw.println("Won at time: "+hour()+":"+minute());
            
            pw.close();
            println("Game saved successfully.");
        } catch (IOException e) {
            println("Error saving file: "+e.getMessage());
        }
    }
}