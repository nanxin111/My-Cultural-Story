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
import java.util.ArrayList;

public class MySketch extends PApplet {
    private Nuwa nuwa;
    private PImage bg;
    private int collectedCount = 0;
    private int stage = 0;
    
    float[] itemX = {100, 300, 500, 700, 400};
    float[] itemY = {400, 350, 450, 300, 200};
    boolean[] collected = {false, false, false, false, false};
    
    public void settings() {
        size(800, 600);
    }
    
    public void setup() {
        bg = loadImage("images/gold.png");
        nuwa = new Nuwa(this, 50, 480, 5, "images/Nuwa.png");
    }
    
    public void draw() {   
        if (stage == 0) {
            background(0);
            fill(255);
            textAlign(CENTER);
            text("Nuwa Mends the Heavens\nPress ENTER to Start", width/2, height/2);
        }
        else if (stage == 1) {
            image(bg, 0, 0, width, height);
            
            for (int i = 0; i < 5; i++) {
                if (!collected[i]) {
                    fill(255, 200, 0);
                    ellipse(itemX[i], itemY[i], 20, 20);
                    
                    if (dist(nuwa.x, nuwa.y, itemX[i], itemY[i]) < 40) {
                        collected[i] = true;
                        collectedCount++;
                    }
                }
            }
            
            fill(255, 100, 0, 150);
            rect(700, 100, 80, 80);
            fill(255);
            textSize(15);
            text("Altar", 750, 90);
            
            nuwa.updatePhysics();
            nuwa.draw();
        
            if (keyPressed) {
                if (keyCode == LEFT) nuwa.move(-1);
                if (keyCode == RIGHT) nuwa.move(1);
            }
            
            fill(255);
            textSize(20);
            text("Stones: " + collectedCount + " / 5", 100, 50);
            
            if (collectedCount == 5 && dist(nuwa.x, nuwa.y, 700, 100) < 60) {
                stage = 2;
            }
        }
        else if (stage == 2) {
            background(0, 150, 255);
            fill(255);
            text("The Sky is Repaired!\nVictory!", width/2, height/2);
        }
    }
    
    public void keyPressed() {
        if (stage == 0 && keyCode == ENTER) {
            stage = 1;
        }
        
        if (stage == 1 && keyCode == UP) {
            nuwa.jump();
        }
    }
}
