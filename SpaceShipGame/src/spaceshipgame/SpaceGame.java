/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spaceshipgame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author Cedric, TJ, Rhenz , Budz
 */
public class SpaceGame extends Application{

    //variables
    public static final Random RAND = new Random(); 
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    public static final int PLAYER_SIZE = 60; 
    
    static final Image PLAYER_IMG = new Image("file:/images/player.png"); 
    static final Image EXPLOSION_IMG = new Image("file:/.images/explosion.png") {};
    
    static final int EXPLOSION_W = 128;
    static final int EXPLOSION_ROWS = 3;
    static final int EXPLOSION_COL = 3;
    static final int EXPLOSION_H = 128;
    static final int EXPLOSION_STEPS = 15;
    
    static final Image ENEMIES_IMG[] = {
        new Image("file:/images/shooter_enemy.png"),
        new Image("file:/images/regular_enemy.png")   
    };
    
    final int MAX_ENEMIES = 10, MAX_SHOTS = MAX_ENEMIES*2;
    boolean gameOver = false;
    private GraphicsContext gc; 
    
    Spaceship player;
    List<Bullet> bullets;
    List<Universe> univ;
    List<Enemy> enemies;
    
    private double mouseX;
    private int score; 
    
    @Override 
    public void start(Stage stage) throws Exception{
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> run(gc)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        canvas.setCursor(Cursor.MOVE); 
        canvas.setOnMouseMoved(e -> mouseX = e.getX());
	canvas.setOnMouseClicked(e -> {
            if(bullets.size() < MAX_SHOTS) bullets.add(player.shoot());
            if(gameOver) { 
		gameOver = false;
		setup();
			}
		});
	setup();
	stage.setScene(new Scene(new StackPane(canvas)));
	stage.setTitle("Spaceship Pew Pew");
	stage.show();
        
    }
   
    public void setup(){
        univ = new ArrayList<>();
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        player = new Spaceship(WIDTH /2, HEIGHT  - PLAYER_SIZE, PLAYER_SIZE, PLAYER_IMG);
        score = 0;
        IntStream.range(0, MAX_ENEMIES).mapToObj(i->this.newEnemy()).forEach(enemies::add);
         
    }
    
    private void run(GraphicsContext gc){
        gc.setFill(Color.grayRgb(20));
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font(20));
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + score, 60, 20);
        
        if(gameOver){
            gc.setFont(Font.font(35));
            gc.setFill(Color.YELLOW);
            gc.fillText("BANGAA NIMO OI\n SCORE NIMO KAY: " + score + "\n Click to play again", WIDTH/2, HEIGHT/2.5);
        }
        univ.forEach(Universe::draw);
        player.update();
        player.draw();
        player.posX = (int) mouseX; 
        
        enemies.stream().peek(Spaceship::update).peek(Spaceship::draw).forEach(e->{
            if(player.hit(e) && !player.exploding){
                player.explode();
            }
        });
        
        
        for(int i = bullets.size()-1;i>=0;i--){
            Bullet bullet = bullets.get(i);
            if(bullet.posY < 0 || bullet.toRemove){
                bullets.remove(i);
                continue;
            }
            bullet.update();
            bullet.draw();
            for(Enemy en : enemies){
                if(bullet.hit(en) && !en.exploding){
                    score++;
                    en.explode();
                    bullet.toRemove = true;
                }
            }   
        }
        
        for(int i=enemies.size()-1;i>=0;i--){
            if(enemies.get(i).destroyed){
                enemies.set(i,newEnemy()); 
            }
        }
        
        gameOver = player.destroyed;
        if(RAND.nextInt(10)>2){
            univ.add(new Universe());
        }
        
        for(int i=0;i<univ.size();i++){
            if(univ.get(i).posY > HEIGHT)
                univ.remove(i);
        }
        
        
    }
    
    
    public class Spaceship {
    
        int posX, posY, size;
        boolean exploding, destroyed;
        Image img;
        int explosionStep = 0;

        public Spaceship(int posX, int posY, int size, Image img) {
            this.posX = posX;
            this.posY = posY;
            this.size = size;
            this.img = img;
        }
        
        public Bullet shoot(){
            return new Bullet(posX+size/2 - Bullet.size/2, posY - Bullet.size); 
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
        
        public void explode(){
            exploding = true;
            explosionStep = -1; 
        }
    
    }
   
        
    public class Enemy extends Spaceship{
            int SPEED = (score/5)+2;

        public Enemy(int posX, int posY, int size, Image image){
            super(posX, posY, size, image);
        }
        
        @Override
        public void update(){
            super.update();
            if(!exploding &&  !destroyed) posY +=SPEED;
            if(posY > HEIGHT) destroyed = true;
        } 

    }
        

    public class Bullet {
        
        public boolean toRemove;

        int posX, posY, speed = 10;
        static final int size = 6;

        public Bullet(int posX, int posY){
            this.posX=posX;
            this.posY=posY;
        }
        
        public void update(){
            posY-=speed;
        }
       
        public void draw(){
            gc.setFill(Color.RED);
            if(score >=50 && score <=70 || score >=120){
                gc.setFill(Color.YELLOWGREEN);
                speed =50;
                gc.fillRect(posX-5, posY-10, size+10, size+30);
            }else{
                gc.fillOval(posX, posY, size, size);
            }
        }
            
        public boolean hit(Spaceship Spaceship) {
                int distance = distance(this.posX +size /2, this.posY +size/2,
                Spaceship.posX +Spaceship.size /2, Spaceship.posY+Spaceship.size/2);
                return distance < Spaceship.size /2 +size/2;
            }   
        }
    
    public class Universe{
        
        int posX, posY;
        private int h,w,r,g,b;
        private double opacity;
            
        public Universe(){
            posX=RAND.nextInt(WIDTH);
            posY=0;
            w= RAND.nextInt(5) +1;
            h= RAND.nextInt(5) +1;
            r= RAND.nextInt(100) +150;
            g= RAND.nextInt(100) +150;
            b= RAND.nextInt(100) +150;
            opacity = RAND.nextFloat();
            
            if(opacity <0)opacity *=-1;
            if(opacity >0.5) opacity = 0.5;
        }   
        
        public void draw(){
            if(opacity>0.8) opacity -=0.01;
            if(opacity < 0.1) opacity +=0.01;
            gc.setFill(Color.rgb(r,g,b,opacity));
            gc.fillOval(posX, posY, w, h);
            posY+=20;
         
        }
    }
        
    Enemy newEnemy(){
        return new Enemy(50+RAND.nextInt(WIDTH-100),0,PLAYER_SIZE, ENEMIES_IMG[RAND.nextInt(ENEMIES_IMG.length)]); 
    }
    
    int distance(int x1, int y1, int x2, int y2) { 
        return (int) Math.sqrt(Math.pow((x1 - x2),2) + Math.pow((y1 - y2),2));     
    }
    
    
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
