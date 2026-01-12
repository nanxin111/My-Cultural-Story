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
import java.io.PrintWriter;

public class MySketch extends PApplet {
    Nuwa player;
    float[][] stoneCoords = {
        {100, 350}, 
        {250, 200}, 
        {400, 300},
        {550, 150},
        {700, 400}
    };

    boolean[] stoneCollected = new boolean[5];
    int score = 0;
    int gameState = 0;
    
    public void settings() {
        size(800, 600);
    }
    
    public void setup() {
        player = new Nuwa(this, 50, 450);
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
        fill(100, 200, 100);
        rect(0, 500, width, 100);
            
        for (int i = 0; i < stoneCoords.length; i++) {
            if (!stoneCollected[i]) {
                fill(255, 0, 0);
                ellipse(stoneCoords[i][0], stoneCoords[i][1], 30, 30);
                    
                if (checkCollision(player.x, player.y, stoneCoords[i][0], stoneCoords[i][1]) && player.isActive) {
                    stoneCollected[i] = true;
                    score++;
                }
            }
        }
        
        player.display();
        fill(0);
        textSize(20);
        text("Stones: "+score+"/5", 80, 50);
            
        if (keyPressed) {
            if (keyCode == LEFT) player.move(-1);
            if (keyCode == RIGHT) player.move(1);
        }
            
        if (score == 5) {
            gameState = 2;
            saveGameRecord();
        }
    }
        
    public void drawWinScreen() {
        background(255, 215, 0);
        fill(0);
        text("YOU WON!\nSky Repaired!", width/2, height/2);
    }
    
    
    public boolean checkCollision(float px, float py, float sx, float sy) {
        float distance=dist(px, py, sx, sy);
        return distance<50;
    }
    
    public void readGameRecord() {
        try{
            String[] lines=loadStrings("game-log.txt");
            if (lines != null) {
                println("Previous game log: "+lines[0]);
            }
        } catch (Exception e) {
            println("No record found");
        }
    }
    
    public void saveGameRecord() {
        try{
            String[] data={"Game Completed at: "+hour()+":"+minute() };
            saveStrings("game_log.txt", data);
            println("Game saved!");
        } catch (Exception e) {
            println("Error saving file.");
        }
    }
    
    public void keyPressed() {
        if (gameState == 0 && keyCode == ENTER) {
            gameState = 1;
        }
        
        if (gameState == 1 && keyCode == UP) {
            player.jump();
        }
    }
}
