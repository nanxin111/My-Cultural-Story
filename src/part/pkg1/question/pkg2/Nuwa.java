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
public class Nuwa {
    public float x, y;
    public float vy=0;
    private float speed;
    private PApplet app;
    private PImage image;
    
    public Nuwa(PApplet p, float x, float y, float speed, String imagePath) {
        this.app=p;
        this.x=x;
        this.y=y;
        this.speed=speed;
        this.image=app.loadImage(imagePath);
    }
    
    public void move (int dir) {
        this.x += dir * speed;
    }
    
    public void jump() {
        this.vy = -12;
    }
    
    public void updatePhysics() {
        vy += 0.5;
        y += vy;
        
        if (y > 480) {
            y = 480;
            vy = 0;
        }
    }
    
    public void draw () {
        app.image(image, x, y, 50, 80);
    }
}