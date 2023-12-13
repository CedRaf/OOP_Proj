/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.spacei;

/**
 *
 * @author TJ
 */
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SpecialBullet extends Bullet {

    // Additional parameters for the special bullet, if any

    private double vx;
    private double vy;

    public SpecialBullet(double posX, double posY, double vx, double vy, Color bulletColor) {
        super(posX, posY, false, true, bulletColor);
        this.vx = vx;
        this.vy = vy;
    }

    
    // Override or add methods specific to SpecialBullet, if necessary

    // Example: Override the draw method to customize the appearance
    @Override
    public void draw(javafx.scene.canvas.GraphicsContext gc) {
        gc.setFill(Color.ORANGE);
        gc.fillOval(posX, posY, size, size);
    }
}
