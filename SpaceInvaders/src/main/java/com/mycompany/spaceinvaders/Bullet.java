package com.mycompany.spaceinvaders;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

public class Bullet {

    public boolean toRemove;
    private final boolean isPlayerBullet;
    private final boolean isShooterEnemyBullet;
    private final Color bulletColor;
    double posX, posY, speed = 5;
    static final int size = 6;

    public Bullet(double posX, double posY, boolean isPlayerBullet, boolean isShooterEnemyBullet, Color bulletColor) {
        this.posX = posX;
        this.posY = posY;
        this.isPlayerBullet = isPlayerBullet;
        this.isShooterEnemyBullet = isShooterEnemyBullet;
        this.bulletColor = bulletColor;
    }

    public void updatePlayerBullet(double speedMultiplier) {
        if (isPlayerBullet) {
            posY -= (speed+speedMultiplier);
        } else {
            posY += (speed+speedMultiplier);
        }
    }
    public void update() {
        if (isPlayerBullet) {
            posY -= speed;
        } else {
            posY += speed;
        }
    }

    public void draw(GraphicsContext gc) {
        if (isShooterEnemyBullet) {
            gc.setFill(bulletColor);
            gc.fillOval(posX, posY, size, size);
        } else {
            if (App.score >= 50 && App.score <= 70 || App.score >= 120) {
                gc.setFill(bulletColor);
                speed = 50;
                gc.fillRect(posX - 5, posY - 10, size + 10, size + 30);
            } else {
                gc.setFill(isPlayerBullet ? Color.RED : bulletColor);
                gc.fillOval(posX, posY, size, size);
            }
        }
    }

    public boolean hit(Spaceship spaceship) {
        double distance = distance(this.posX + size / 2, this.posY + size / 2, spaceship.posX + spaceship.size / 2,
                spaceship.posY + spaceship.size / 2);
        return distance < spaceship.size / 2 + size / 2;
    }

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }
}