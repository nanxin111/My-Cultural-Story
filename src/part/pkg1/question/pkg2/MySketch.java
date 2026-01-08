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

public class MySketch extends PApplet {
    private Car car1; // declare a car1 object
    private Car car2;
    private Car selectedCar = null;
    
    public void settings() {
        size(400, 400);
    }
    
    public void setup() {
        background(255); // set the background color to white
        car1 = new Car(this, 50, 100, 2, "images/car.png");
        car2 = new Car(this, 200, 200, 3, "images/car.png");
    }
    
    public void keyPressed() {
        if (keyCode == LEFT) {
            car1.move(-2, 0);
        } else if (keyCode == RIGHT) {
            car1.move(2, 0);
        } else if (keyCode == UP) {
            car1.move(0, -2);
        } else if (keyCode == DOWN) {
            car1.move(0, 2);
        }
    }
    
    public void draw() {
        background(255); // clear the screen
        car1.draw();
        car2.draw();
            
        if (selectedCar!= null) {
            selectedCar.displayInfo(this);
        }
    }
    
    public void mousePressed() {
        if (car1.isClicked(mouseX, mouseY)) {
            selectedCar = car1;
        } else if (car2.isClicked(mouseX, mouseY)) {
            selectedCar = car2;
        } else {
            selectedCar = null;
        }
    }
}
