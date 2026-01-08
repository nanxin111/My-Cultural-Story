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
public class Car {
    public int x;
    public int y;
    private int speed;
    private PApplet app; // the canvas used to display graphical elements
    private PImage image;
    
    public Car (PApplet p, int x, int y, int speed, String imagePath) {
        this.app=p;
        this.x=x;
        this.y=y;
        this.speed=speed;
        this.image=app.loadImage(imagePath);
    }
    
    public void move (int dx, int dy) {
        this.x += dx * speed;
        this.y += dy * speed;
    }
    
    public boolean isClicked(int mouseX, int mouseY) {
        int centerX = x+(image.pixelWidth/2);
        int centerY = y+(image.pixelHeight/2);
        float d = PApplet.dist(mouseX, mouseY, centerX, centerY);
      
        System.out.println("image height"+image.pixelHeight);
        System.out.println("image width"+image.pixelWidth);

        return d < 16;
    }
    
    public void displayInfo(PApplet p) {
      app.fill(0);
      app.text("X: "+x, x, y - 50);
      app.text("Y: "+y, x, y - 30);
      app.text("Speed: "+speed, x, y - 10);
    }
    
    public void draw () {
        app.image(image, x, y); // draw the image at the car's position
    }
}
