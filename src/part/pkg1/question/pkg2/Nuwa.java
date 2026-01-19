/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package part.pkg1.question.pkg2;

import processing.core.PApplet;
import processing.core.PImage;
/**
 * Player character (Nuwa) controlled by user
 * Functions:
 * -Move left and right
 * -Jump
 * -Gravity system
 * -Health system
 * @author nanxinyu
 */
public class Nuwa extends GameObject{
    public static final float GRAVITY=0.6f; // Gravitational constant重力常量
    public static int health=3; // Player's initial health
    public float vy=0; // linear velocity直速度
    private float speed; // Horizontal movement speed
    private PImage image; // Player image
    /**
     * Nuwa's constructor
     * @param app
     * @param x
     * @param y
     * @param speed 
     */
    public Nuwa(PApplet app, float x, float y, float speed) {
        super(app,x ,y);
        this.speed=speed;
        this.image=app.loadImage("images/Nuwa.png");
    }
    /**
     * If MySketch doesn't specify a speed, the default speed is 5.
     * @param app
     * @param x
     * @param y 
     */
    public Nuwa(PApplet app, float x, float y) {
        this(app, x, y, 5);
    }
    /**
     * Move left and right
     * @param dir -1 left, 1 right
     */
    public void move (int dir) {
        this.x += dir * speed;
    }
    /**
     * Jump only when on ground
     */
    public void jump() {
        if (Math.abs(vy) < 0.2f) { // If the player is currently not moving up and down=on the ground.
            this.vy = -12; // Then give the player an "upward speed of 12".
        }
    }
    /**
     * Take damage and reset position
     */
    public void takeDamage() {
        health--;
        x=50;
        y=230;
        vy=0;
    }
    /**
     * When the player is not on the plane, 
     * the player will gradually accelerate and fall from this point (y)
     * until reaching the ground (y=230).
     */
    public void updatePhysics() {
        vy += GRAVITY;
        y += vy; 
        // y=230 is the height of full-backround of ground
        if (y > 230) {
            y = 230;
            vy = 0;
        }
    }
    /**
     * Display player
     */
    @Override
    public void display () {
        updatePhysics();
        if (image != null) {
            app.image(image, x, y, 40, 60);
        } else {
            app.fill(255, 0, 255);
            app.rect(x, y, 30, 50);
        }
    }
}