/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package part.pkg1.question.pkg2;

import processing.core.PApplet;
import processing.core.PImage;

/**
 *
 * @author nanxinyu
 */
public class Nuwa extends GameObject{
    public static final float GRAVITY=0.6f;
    public int health=3;
    public float vy=0;
    private float speed;
    private PImage image;
    
    public Nuwa(PApplet app, float x, float y, float speed) {
        super(app,x ,y);
        this.speed=speed;
        this.image=app.loadImage("images/Nuwa.png");
    }
    
    public Nuwa(PApplet app, float x, float y) {
        this(app, x, y, 5);
    }
    
    public void move (int dir) {
        this.x += dir * speed;
    }
    
    public void jump() {
        if (Math.abs(vy) < 0.2f) {
            this.vy = -12;
        }
    }
    
    public void takeDamage() {
        health--;
        x=50;
        y=300;
        vy=0;
    }
    
    public void updatePhysics() {
        vy += GRAVITY;
        y += vy;
        
        // 假设304是full-backround的地面高度
        if (y > 230) {
            y = 230;
            vy = 0;
        }
    }
    
    @Override
    public void display () {
        updatePhysics();
        if (image != null && image.width > 0) {
            app.image(image, x, y, 40, 60);
        } else {
            app.fill(255, 0, 255);
            app.rect(x, y, 30, 50);
        }
    }
}