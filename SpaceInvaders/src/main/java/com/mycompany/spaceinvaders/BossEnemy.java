/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.spaceinvaders;

import static com.mycompany.spaceinvaders.App.RAND;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author TJ
 */
public class BossEnemy extends Spaceship {
    private int health;
    private boolean shootingRegularBullet;
    private int shootingCooldown; // Cooldown for shooting regular bullets
    private final int maxShootingCooldown = 30; // Adjust the cooldown duration
    private boolean isDashing = false;
    private int originalPosX;
    private int dashSpeed = 10; // Adjust the dash speed as needed
    private int dashDistance = 200; // Adjust the dash distance as needed
    private int dashCooldown = 200; // Adjust the dash cooldown as needed
    private long lastDashTime = 0;
    
    
    public BossEnemy(int posX, int posY, int size, Image image) {
    super(posX, posY, size, image);
    health = 25; 
    shootingCooldown = 0;
    originalPosX = posX;
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
    if (!isDashing) {
        posX -= 5;
      }
    }

    public void moveRight() {
    if (!isDashing) {
        posX += 5;
     }
    }
    
    private Bullet shootRegularBullet() {
        return new Bullet(posX + size / 2 - Bullet.size / 2, posY + size, false, true, Color.YELLOW);
    }
    
    @Override
   public Bullet shoot() {
       
          if(destroyed || health == 0){
           return null; 
        }
       
        if (shootingCooldown > 0) {
            shootingCooldown--;
            return null; // Boss is in cooldown, don't shoot
        }

        if (shootingRegularBullet) {
            shootingRegularBullet = false;
            shootingCooldown = maxShootingCooldown; // Set cooldown after shooting
            return shootRegularBullet(); // Regular shooting behavior
        } else {
            // Enter shooting phase with a 5% chance
            if (RAND.nextInt(100) < 5) {
                shootingRegularBullet = true;
                return shootRegularBullet(); // Regular shooting behavior
            }
        }
        return null; // Boss is not shooting in this frame
    }

  public boolean hit(Bullet bullet) {
    double distance = distance(bullet.posX + Bullet.size / 2, bullet.posY + Bullet.size / 2,
            this.posX + size / 2, this.posY + size / 2);
    return distance < size / 2 + Bullet.size / 2 && !bullet.toRemove;
}

    private double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
    }
    
    
    public void moveTowards(double targetX) {
        if (posX < targetX) {
            posX += 3; // Adjust the movement speed as needed
        } else if (posX > targetX) {
            posX -= 3; // Adjust the movement speed as needed
        }
    }
    
//  protected void performDash() {
//        if (System.currentTimeMillis() - lastDashTime > dashCooldown) {
//            isDashing = true;
//            lastDashTime = System.currentTimeMillis();
//        }
//
//        if (isDashing) {
//            double targetX = originalPosX - dashDistance;
//
//            // Adjust the movement speed during dash
//            if (posX < targetX) {
//                posX += dashSpeed;
//            } else if (posX > targetX) {
//                posX -= dashSpeed;
//            }
//
//            // Check if the boss reached the dash destination
//            if ((posX <= targetX && dashSpeed > 0) || (posX >= targetX && dashSpeed < 0)) {
//                isDashing = false;
//                posX = targetX;
//            }
//        }
//    }
 
 @Override
    public void update() {
        if (!exploding) {
            shootingCooldown = Math.max(0, shootingCooldown - 1); // Decrease shootingCooldown
           // performDash();
            super.update();
        }
    }
}
