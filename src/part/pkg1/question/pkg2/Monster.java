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
public class Monster extends GameObject {
    private float speed=2;
    private float leftBound, rightBound;
    private PImage img;
    
    public Monster(PApplet app, float x, float y, float patrolRange, PImage img) {
        super(app, x, y);
        this.leftBound=x-patrolRange;
        this.rightBound=x+patrolRange;
        this.img=img;
    }
    
    @Override
    public void display() {
        moveAI();
        if (img != null) {
            app.image(img, x, y, 40, 40);
        } else {
            app.fill(255, 0, 0);
            app.rect(x, y, 40, 40);
        }
    }
    
    private void moveAI() {
        x += speed;
        if (x > rightBound || x < leftBound) {
            speed *= -1;
        }
    }
}
