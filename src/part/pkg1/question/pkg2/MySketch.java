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
    
    
    float[][] stoneCoords = {
        {150, 280}, 
        {300, 150}, 
        {450, 220},
        {600, 120},
        {700, 280}
    };
    
    float[][] platforms={
        {280, 180}, {580, 150}, {120, 310}
    };

    boolean[] stoneCollected = new boolean[5];
    int score = 0;
    int gameState = 0; // 0=Start, 1=Play, 2=Win, 3=Game Over
    
    public void settings() {
        size(768, 384);
    }
    
    public void setup() {
        bgImg = loadImage("images/full-background.png");
        platformImg = loadImage("images/tile 1.png");
        enemyImg = loadImage("images/enemyImg.png");
        
        player = new Nuwa(this, 50, 304);
        
        monsters[0]=new Monster(this, 280, 140, 50, enemyImg);
        monsters[1]=new Monster(this, 580, 110, 60, enemyImg);
        monsters[2]=new Monster(this, 400, 304, 100, enemyImg);
        
        readGameRecord();
    }
    
    public void draw() { 
        background(255);
        
        if (gameState == 0) {
            drawStartScreen();
        } else if (gameState == 1) {
            playGame();
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
        } else {
            background(135, 206, 235);
        }
        
        for (float[] p : platforms) {
            image(platformImg, p[0], p[1], 100, 20);
            if (player.x + 20 > p[0] && player.x < p[0] + 100) {
                if (player.y + 60 > p[1] && player.y + 60 < p[1] + 20 && player.vy > 0) {
                    player.y = p[1] - 60;
                    player.vy = 0;
                }
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
        
        player.display();
         
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
        
        if (score == 5) {
            gameState=2;
            saveGameRecord();
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
    
    public void keyPressed() {
        if (keyCode == UP && gameState == 1) {
            player.jump();
        }
        if (keyCode == ENTER) {
            if (gameState==0) gameState=1;
            else if (gameState==3) resetGame();
        }
    }
    
    public void resetGame() {
        gameState=1;
        score=0;
        player.health=3;
        player.x=50;
        player.y=304;
        for (int i=0; i<5; i++) {
            stoneCollected[i]=false;
        }
    }
}
