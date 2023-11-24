package com.mycompany.spaceinvaders;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

public class Bullet {

    public boolean toRemove;

    double posX, posY, speed = 10;
    static final int size = 6;

    public Bullet(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void update() {
        posY -= speed;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);
        if (App.score >= 50 && App.score <= 70 || App.score >= 120) {
            gc.setFill(Color.YELLOWGREEN);
            speed = 50;
            gc.fillRect(posX - 5, posY - 10, size + 10, size + 30);
        } else {
            gc.fillOval(posX, posY, size, size);
        }
    }

    public boolean hit(Spaceship Spaceship) {
        double distance = distance(this.posX + size / 2, this.posY + size / 2, Spaceship.posX + Spaceship.size / 2,
                Spaceship.posY + Spaceship.size / 2);
        return distance < Spaceship.size / 2 + size / 2;
    }

    private double distance(double x1, double y1, double x2, double y2) {
        return  Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }
}