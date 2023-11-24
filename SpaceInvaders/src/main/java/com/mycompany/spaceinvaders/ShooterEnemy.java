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

    public ShooterEnemy(int posX, int posY, int size, Image image) {
        super(posX, posY, size, image);
    }

    @Override
    
    public Bullet shoot() {
        // Adjust the starting position of the bullet based on the enemy's position
        return new Bullet(posX + size / 2 - Bullet.size / 2, posY + size);
    }
    
}