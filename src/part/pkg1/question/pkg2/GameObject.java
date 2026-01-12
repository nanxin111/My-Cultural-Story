/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package part.pkg1.question.pkg2;
import processing.core.PApplet;
/**
 *
 * @author nanxinyu
 */
public class GameObject {
    protected float x, y;
    protected PApplet app;
    public boolean isActive=true;
    
    public GameObject(PApplet p, float x, float y) {
        this.app=p;
        this.x=x;
        this.y=y;
    }
    
    public void display() {
        app.fill(255);
        app.rect(x, y, 20, 20);
    }
}
