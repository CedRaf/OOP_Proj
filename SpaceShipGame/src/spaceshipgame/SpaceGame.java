/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spaceshipgame;

import javafx.scene.image.Image;
import java.util.Random;
import javafx.application.Application;
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
    
    
    
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
