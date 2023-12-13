/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.spaceinvaders;


import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.canvas.GraphicsContext;

public class Spaceship {

    private boolean laserAbilityEnabled;
    private int damageMultiplier;
    private double speedMultiplier;
    double posX, posY, size;
    boolean exploding, destroyed;
    Image img;
    int explosionStep = 0;
    private Color bulletColor;
    
    public Spaceship(double posX, double posY, double size, Image img) {
        this.posX = posX;
        this.posY = posY;
        this.size = size;
        this.img = img;  
    }
    

    public Spaceship(double posX, double posY, double size, Image img, Color bulletColor) {
        this.posX = posX;
        this.posY = posY;
        this.size = size;
        this.img = img;
        this.bulletColor = bulletColor; 
    }

    public Bullet shoot() {
        return new Bullet(posX + size / 2 - Bullet.size / 2, posY - Bullet.size, true, false, bulletColor);
    }

    public void update() {
        if (exploding) explosionStep++;
        destroyed = explosionStep > App.EXPLOSION_STEPS;
    }

    public void draw(GraphicsContext gc) {
        if (exploding) {
            gc.drawImage(App.EXPLOSION_IMG, explosionStep % App.EXPLOSION_COL * App.EXPLOSION_W,
                    (explosionStep / App.EXPLOSION_ROWS) * App.EXPLOSION_H + 1, App.EXPLOSION_W, App.EXPLOSION_H, posX,
                    posY, size, size);
        } else {
            gc.drawImage(img, posX, posY, size, size);
        }
    }

    public boolean hit(Spaceship other) {
        double d;
        d = distance(this.posX + size / 2, this.posY + size / 2, other.posX + other.size / 2,
                other.posY + other.size / 2);
        return d < other.size / 2 + this.size;
    }

    public void explode() {
        exploding = true;
        explosionStep = -1;
    }

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }
    
     public void enableLaserAbility() {
        laserAbilityEnabled = true;
        // Implement the ability effect
    }

    public void increaseDamage() {
        damageMultiplier++;
        // Implement the ability effect
    }

    public void increaseSpeed() {
        speedMultiplier += 0.5; // Adjust as needed
        // Implement the ability effect
    }
    
}