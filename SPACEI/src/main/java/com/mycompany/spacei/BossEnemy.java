/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.spacei;

/**
 *
 * @author TJ
 */
import static com.mycompany.spacei.App.RAND;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

 
public class BossEnemy extends Spaceship {
    private int health;
    private boolean shootingRegularBullet;
    private int shootingCooldown; // Cooldown for shooting regular bullets
    private final int maxShootingCooldown = 30; // Adjust the cooldown duration

    public BossEnemy(int posX, int posY, int size, Image image) {
        super(posX, posY, size, image);
        health = 10; // Initial health
        shootingCooldown = 0;
    }

    public int getHealth() {
        return health;
    }

    public void takeDamage() {
        health--;
        if (health <= 0) {
            explode();
        }
    }

    public boolean isDefeated() {
        return destroyed;
    }

    public void moveLeft() {
        posX -= 5;
    }

    public void moveRight() {
        posX += 5;
    }

    @Override
    public Bullet shoot() {
        if (shootingCooldown > 0) {
            shootingCooldown--;
            return null; // Boss is in cooldown, don't shoot
        }
        if (shootingRegularBullet) {
            // Shooting regular bullets
            shootingRegularBullet = false;
            shootingCooldown = maxShootingCooldown; // Set cooldown after shooting
             return new Bullet(posX + size / 2 - Bullet.size / 2, posY + size, false, true, Color.YELLOW); // Default shooting behavior
        } else {
            // Enter shooting phase with a 5% chance
            if (RAND.nextInt(100) < 5) {
                shootingRegularBullet = true;
            }
            return new Bullet(posX + size / 2 - Bullet.size / 2, posY + size, false, true, Color.YELLOW); // Regular shooting behavior
        }
    }

  public boolean hit(Bullet bullet) {
    double distance = distance(bullet.posX + Bullet.size / 2, bullet.posY + Bullet.size / 2,
            this.posX + size / 2, this.posY + size / 2);
    return distance < size / 2 + Bullet.size / 2 && !bullet.toRemove;
}

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }

    
}

//    // Calculate the direction towards the player
//    double directionX = (App.WIDTH / 2) - (posX + size / 2);
//    double directionY = (App.HEIGHT - posY - size / 2);
//
//    // Normalize the direction vector
//    double length = Math.sqrt(directionX * directionX + directionY * directionY);
//    directionX /= length;
//    directionY /= length;
//
//    // Set the bullet's speed and position
//    double laserSpeed = 8; // Adjust the speed as needed
//    double laserPosX = posX + size / 2 - Bullet.size / 2;
//    double laserPosY = posY + size / 2 - Bullet.size / 2;
//
//    // Create a new SpecialBullet with the calculated parameters
//    return new SpecialBullet(laserPosX, laserPosY, directionX * laserSpeed, directionY * laserSpeed, Color.RED);