/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spaceshipgame;

import javafx.scene.image.Image;
import java.util.Random;
import javafx.application.Application;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

/**
 *
 * @author Cedric
 */
public class SpaceGame extends Application{

    
    //variables
    public static final Random RAND = new Random(); 
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int PLAYER_SIZE = 60; 
    
    static final Image PLAYER_IMG = new Image("file:images\\player.png"); 
    static final Image EXPLOSION_IMG = new Image("file:images\\explosion.png") {};
    
    static final int EXPLOSION_W = 128;
    static final int EXPLOSION_ROWS = 3;
    static final int EXPLOSION_COL = 3;
    static final int EXPLOSION_H = 128;
    static final int EXPLOSION_STEPS = 15;
    
    static final Image ENEMIES_IMG[] = {
        new Image("file:images\\shooter_enemy.png"),
        new Image("file:images\\regular_enemy.png")   
    };
    
    final int MAX_ENEMIES = 10, MAX_SHOTS = MAX_ENEMIES*2;
    boolean gameOver = false;
    private GraphicsContext gc; 
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public class Spaceship {
    
    private int posX, posY, size;
        private boolean exploding, destroyed;
        private Image img;
        private int explosionStep = 0;

        public Spaceship(int posX, int posY, int size, boolean exploding, boolean destroyed, Image img) {
            this.posX = posX;
            this.posY = posY;
            this.size = size;
            this.img = img;
        }
        
        public Bullet shoot(){
            return new Bullet(posX+size/2 - Shot.size/2, posY - Shot.size); 
        }
        
        public void update() {
            if(exploding) explosionStep++; 
            destroyed = explosionStep > EXPLOSION_STEPS; 
        }
        
        public void draw(){
            if(exploding){
                gc.drawImage(EXPLOSION_IMG, explosionStep % EXPLOSION_COL * EXPLOSION_W, (explosionStep / EXPLOSION_ROWS)*EXPLOSION_H+1, EXPLOSION_W, EXPLOSION_H, posX, posY, size, size);
            }else{
                gc.drawImage(img, posX, posY, size, size);
            }
        }
        
        public boolean hit(Spaceship other){
            int d = distance(this.posX+size/2, this.posY+size/2, other.posX + other.size/2, other.posY + other.size /2); 
            return d < other.size /2 + this.size; 
        }
    
}
   
        
        
    
    
}
