/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.spaceinvaders;

/**
 *
 * @author TJ
 */
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

public class Universe {

    double posX, posY;
    private int h, w, r, g, b;
    private double opacity;

    
    
  
    public Universe() {
        posX = App.RAND.nextInt(App.WIDTH);
        posY = App.RAND.nextInt(App.HEIGHT);
        w = App.RAND.nextInt(5) + 1;
        h = App.RAND.nextInt(5) + 1;
        r = App.RAND.nextInt(100) + 150;
        g = App.RAND.nextInt(100) + 150;
        b = App.RAND.nextInt(100) + 150;
        opacity = App.RAND.nextFloat();

        if (opacity < 0)
            opacity *= -1;
        if (opacity > 0.5)
            opacity = 0.5;
    }

    public void draw(GraphicsContext gc) {
        if (opacity > 0.8)
            opacity -= 0.01;
        if (opacity < 0.1)
            opacity += 0.01;
        gc.setFill(Color.rgb(r, g, b, opacity));
        gc.fillOval(posX, posY, w, h);
        posY += 20;
    }
}