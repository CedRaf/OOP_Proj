/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.spaceinvaders;


import javafx.scene.image.Image;

public class Enemy extends Spaceship {
     double SPEED = (App.score / 10.0) + 2.0; 
     boolean dropsGold; 
     boolean isGoldDropped; 

    public Enemy(int posX, int posY, int size, Image image) {
        super(posX, posY, size, image);
    }
    

    @Override
    public void update() {
        super.update();
        if (!exploding && !destroyed)
            posY += SPEED;
        if (posY > App.HEIGHT)
            destroyed = true;
    }
    
    @Override
    public void explode(){
        if(dropsGold && !isGoldDropped){
            App.gold += App.RAND.nextInt(5)+1;
            isGoldDropped = true; 
        }
        super.explode(); 
    }
}

