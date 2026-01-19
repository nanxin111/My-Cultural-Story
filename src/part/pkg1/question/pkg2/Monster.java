/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package part.pkg1.question.pkg2;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Monster class that patrols horizontally（横向巡逻）
 * Functions:
 * -Automatic left and right movement
 * -Collision with player results in damage
 * @author nanxinyu
 */
public class Monster extends GameObject {
    private float speed=1; // Movement speed (positive and negative indicate direction)
    private float leftBound; // Patrol the left border巡逻左边界
    private float rightBound; // Patrol the right border巡逻右边界
    private PImage img; // Monster image
    /**
     * Constructor for Monstter
     * @param app Processing
     * @param x
     * @param y
     * @param patrolRange
     * @param img 
     */
    public Monster(PApplet app, float x, float y, float patrolRange, PImage img) {
        super(app, x, y);
        this.leftBound=x-patrolRange;
        this.rightBound=x+patrolRange;
        this.img=img;
    }
    /**
     * Display and update moster movement
     */
    @Override
    public void display() {
        moveAI();
        app.image(img, x, y, 40, 40);
    }
    /**
     * Simple patrol movement
     */
    private void moveAI() {
        x += speed;
        // Reverse direction after reaching the boundary到达边界后反向
        if (x > rightBound || x < leftBound) {
            speed *= -1;
        }
    }
}
