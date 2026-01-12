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
        this.vy = -12;
    }
    
    public void updatePhysics() {
        vy += GRAVITY;
        y += vy;
        
        if (y > 450) {
            y = 450;
            vy = 0;
        }
    }
    
    @Override
    public void display () {
        updatePhysics();
        if (image != null && image.width > 0) {
            app.image(image, x, y, 50, 80);
        } else {
            app.fill(255, 0, 255);
            app.rect(x, y, 30, 50);
        }
    }
}