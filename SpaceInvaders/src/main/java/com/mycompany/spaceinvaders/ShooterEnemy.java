/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.spaceinvaders;

/**
 *
 * @author TJ
 */

import javafx.scene.image.Image;

public class ShooterEnemy extends Enemy {

    private boolean canShoot = true;
    private long lastShotTime;

    public ShooterEnemy(int posX, int posY, int size, Image image) {
        super(posX, posY, size, image);
    }

    @Override
    public void update() {
        super.update();
        checkShootCooldown();
    }

    private void checkShootCooldown() {
        long currentTime = System.currentTimeMillis();
        if (!canShoot && currentTime - lastShotTime >= getShootCooldown()) {
            canShoot = true;
        }
    }

    private long getShootCooldown() {
        return 2000; 
    }

    @Override
    public Bullet shoot() {
        if (canShoot) {
            lastShotTime = System.currentTimeMillis();
            canShoot = false;
            return new Bullet(posX + size / 2 - Bullet.size / 2, posY + size);
        }
        return null; 
    }
}
