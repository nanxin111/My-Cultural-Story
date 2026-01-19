/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package part.pkg1.question.pkg2;
import processing.core.PApplet;
/**
 * Parent class for all game objects in the sketch
 * @author nanxinyu
 */
public class GameObject {
    protected float x; // horizontal position
    protected float y; // vertical position
    protected PApplet app;
    /**
     * Constructor for GameObject
     * @param p Processing
     * @param x
     * @param y 
     */
    public GameObject(PApplet p, float x, float y) {
        this.app=p;
        this.x=x;
        this.y=y;
    }
    /**
     * Diaplay method for the object
     * Subclass can override this method
     */
    public void display() {
        app.fill(255);
        app.rect(x, y, 20, 20);
    }
}