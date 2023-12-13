package com.mycompany.spaceinvaders;

        
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
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;

public class App extends Application {

    //VARIABLES
    public static final Random RAND = new Random();
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int PLAYER_SIZE = 60;

    static final Image PLAYER_IMG = new Image("file:./images/player.png");
    static final Image EXPLOSION_IMG = new Image("file:./images/explosion.png") {};

    static final int EXPLOSION_W = 128;
    static final int EXPLOSION_ROWS = 3;
    static final int EXPLOSION_COL = 3;
    static final int EXPLOSION_H = 128;
    static final int EXPLOSION_STEPS = 15;

    
    static final Image ENEMIES_IMG[] = {
        new Image("file:./images/shooter_enemy.png"),
        new Image("file:./images/regular_enemy.png")
    };

    
    int previousScore = 0;
    boolean increasedEnemies = false;
    int MAX_ENEMIES = 4;
    final int MAX_SHOTS = MAX_ENEMIES * 2;
    boolean gameOver = false;
    private javafx.scene.canvas.GraphicsContext gc;
    
    private BossEnemy boss;
    public static List<Bullet> bossBullets = new ArrayList<>();
    Spaceship player;
    List<Bullet> bullets;
    List<Bullet> shooterBullets;
    List<Universe> univ;
    List<Enemy> enemies;
    List<ShooterEnemy> shooterEnemies;

    private double mouseX;
    static int score;
    static int gold; 

    @Override
    public void start(Stage stage) throws Exception {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(30), e -> run(gc)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        canvas.setCursor(Cursor.MOVE);
        canvas.setOnMouseMoved(e -> mouseX = e.getX());
        canvas.setOnMouseClicked(e -> {
            if (bullets.size() < MAX_SHOTS)
                bullets.add(player.shoot());
            if (gameOver) {
                gameOver = false;
                setup();
            }
        });
        setup();
        stage.setScene(new Scene(new StackPane(canvas)));
        stage.setTitle("Spaceship Pew Pew");
        stage.show();
        
        

    }

    public void setup() {
        univ = new ArrayList<>();
        shooterBullets = new ArrayList<>(); 
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        shooterEnemies = new ArrayList<>();
        MAX_ENEMIES=4;
        player = new Spaceship(WIDTH / 2, HEIGHT - PLAYER_SIZE, PLAYER_SIZE, PLAYER_IMG, Color.YELLOW);
        score = 0;
        gold = 0;
        IntStream.range(0, MAX_ENEMIES / 2).mapToObj(i -> newShooterEnemy()).forEach(shooterEnemies::add);
        IntStream.range(0, MAX_ENEMIES / 2).mapToObj(i -> newEnemy()).forEach(enemies::add);
        
}
    //SOMETHING TODO WITH BOSS
     private void handleBullets(List<Bullet> bullets, List<? extends Spaceship> targets) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            if (bullet.posY < 0 || bullet.toRemove) {
                bullets.remove(i);
                continue;
            }
            bullet.update();
            bullet.draw(gc);

            // Check collisions with enemies
            for (Spaceship target : targets) {
                if (bullet.hit(target) && !target.exploding) {
                    if (target instanceof BossEnemy) {
                        ((BossEnemy) target).takeDamage();
                    } else {
                        score++;
                        target.explode();
                    }
                    bullet.toRemove = true;
                }
            }
        }
    }
     //MAKE PLAYER SMOOTHER
      private void updatePlayerPosition() {
        player.posX = mouseX; // Adjust the interpolation factor as needed
    }
      
      // Reset boss method
private void resetBoss() {
    boss = new BossEnemy(WIDTH / 2, 100, PLAYER_SIZE * 2, new Image("file:./images/boss.png"));
    bossBullets.clear();// Clear boss bullets when resetting
    boss=null;
    // You can also reset any other boss-related variables or states here if needed
}



    private void run(javafx.scene.canvas.GraphicsContext gc) {
        
        gc.setFill(Color.grayRgb(20));
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font(20));
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + score, 60, 20);
        gc.fillText("Gold: " + gold, 60, 40); 
        gc.fillText("Max Enemies: " + MAX_ENEMIES, 80, 60);
              
         //GAME LOGIC     
        if (gameOver && score < 50) {
            gc.setFont(Font.font(35));
            gc.setFill(Color.YELLOW);
            gc.fillText("Game Over! Your score is: " + score + "\n Click to play again", WIDTH / 2, HEIGHT / 2.5);
            
            resetBoss();
            // Remove the boss when the game is over
        boss = null;
        } else if (gameOver && score > 50) {
            gc.setFont(Font.font(35));
            gc.setFill(Color.YELLOW);
            gc.fillText("HAHAHA UR SHIT: " + score + "\n Click to play again", WIDTH / 2, HEIGHT / 2.5);
    
            // Remove the boss when the game is over
            resetBoss();
        }
        
       
        univ.forEach(u -> u.draw(gc)); 
        player.update();
        player.draw(gc);
        player.posX = (int) mouseX;
        
        //ENEMY SPAWNING, SHOOTING, ETC.
        enemies.stream().peek(e -> e.update()).peek(e -> e.draw(gc)).forEach(en -> {
            if (player.hit(en) && !player.exploding) {
                player.explode();
            }
        });
        
        shooterEnemies.stream().peek(e->e.update()).peek(e->e.draw(gc)).forEach(en->{
            if (player.hit(en) && !player.exploding){
                player.explode(); 
            }
        });
        
        //SHOOTER ENEMY BULLETS
        for (ShooterEnemy shooterEnemy : shooterEnemies) {
            Bullet shooterBullet = shooterEnemy.shoot();
            if (shooterBullet != null) {
                shooterBullets.add(shooterBullet);
            }
        }
        
        //HANDLE&DRAW ENEMY SHOOTER BULLETS
        for (int i = shooterBullets.size() - 1; i >= 0; i--) {
            Bullet bullet = shooterBullets.get(i);
            if (bullet.posY < 0 || bullet.toRemove) {
                shooterBullets.remove(i);
                continue;
            }
            bullet.update();
            bullet.draw(gc);
            // Check collisions with the player only
            if (bullet.hit(player) && !player.exploding) {
                player.explode();
                bullet.toRemove = true;
            }
        }
        
        //PLAYER BULLET MECHANICS
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            if (bullet.posY < 0 || bullet.toRemove) {
                bullets.remove(i);
                continue;
            }
            bullet.update();
            bullet.draw(gc);
            
            //checking collisions with enemies
            for (Enemy en : enemies) {
                if (bullet.hit(en) && !en.exploding) {
                    score++;
                    en.explode();
                    bullet.toRemove = true;
                }
            }
            
            for(ShooterEnemy sEn: shooterEnemies){
               if(bullet.hit(sEn) && !sEn.exploding){
                   score++;
                   sEn.explode();
                   bullet.toRemove = true; 
               } 
                 }
            }
        
        
         //HANDLE BOSS LOGIC
        if (score == 10 && boss == null) {
            // Clear shooterBullets when the boss appears
            //shooterBullets.clear();
            boss = new BossEnemy(WIDTH / 2, 100, PLAYER_SIZE * 2, new Image("file:./images/boss.png"));
        }
        
         // If the boss is present, handle boss logic
        if (boss != null) {
            boss.update();
            boss.draw(gc);

            // Boss movement
            if (mouseX > boss.posX + boss.size / 2) {
                boss.moveRight();
            } else if (mouseX < boss.posX + boss.size / 2) {
                boss.moveLeft();
            }

            // Boss shooting
            Bullet bossBullet = boss.shoot();
            if (bossBullet != null) {
                bossBullets.add(bossBullet);
            }
            
            

            // Handle collisions with boss bullets and player
            for (int i = bossBullets.size() - 1; i >= 0; i--) {
                bossBullet = bossBullets.get(i);
                if (bossBullet.posY > HEIGHT || bossBullet.toRemove) {
                    bossBullets.remove(i);
                    continue;
                }
                bossBullet.update();
                bossBullet.draw(gc);
                // Check collisions with the player only
                if (bossBullet.hit(player) && !player.exploding) {
                    player.explode();
                    bossBullet.toRemove = true;
                }
            }

            // Handle collisions with player bullets and boss
            for (int i = bullets.size() - 1; i >= 0; i--) {
                Bullet bullet = bullets.get(i);
                if (bullet.posY < 0 || bullet.toRemove) {
                    bullets.remove(i);
                    continue;
                }
                bullet.update();
                bullet.draw(gc);
                // Check collisions with the boss only
                if (boss.hit(bullet) && !boss.exploding) {
                    boss.takeDamage();
                    bullet.toRemove = true;
                }
            }

            // Draw boss HP
            gc.setFill(Color.RED);
            gc.fillRect(WIDTH / 2 - 50, 10, boss.getHealth() * 10, 10);

            // Check if the boss is defeated
            if (boss.isDefeated()) {
                boss = null; // Reset boss
                score += 10; // Increment the score
            }
        }
        
        updatePlayerPosition();
        handleBullets(bullets, enemies);
        handleBullets(bullets, shooterEnemies);

        if (score > previousScore && score % 10 == 0 && !increasedEnemies) {
           MAX_ENEMIES++;
           increasedEnemies = true;
           previousScore = score; 

           // Adjust enemy count
           while (enemies.size() < MAX_ENEMIES / 2) {
               enemies.add(newEnemy());
           }

           while (shooterEnemies.size() < MAX_ENEMIES / 2) {
               shooterEnemies.add(newShooterEnemy());
           }
       } else if (score % 10 != 0) {
           increasedEnemies = false;
       }

        
        
        //HOW ENEMY SPAWNING IS HANDLED
        for (int i = enemies.size() - 1; i >= 0; i--) {
            if (enemies.get(i).destroyed) { 
                enemies.set(i, newEnemy());      
            }
        }
        
        for (int i = shooterEnemies.size() - 1; i >= 0; i--) {
            if (shooterEnemies.get(i).destroyed) {
                shooterEnemies.set(i, newShooterEnemy());
            }
        }
        
        
        //HOW GAME OVER IS HANDLED
        gameOver = player.destroyed;
        if (RAND.nextInt(10) > 2) {
            univ.add(new Universe());
            
           
        }

        for (int i = 0; i < univ.size(); i++) {
            if (univ.get(i).posY > HEIGHT)
                univ.remove(i);
        }  
        
    }
    
    

    Enemy newEnemy() {
        Enemy enemy = new Enemy(50 + RAND.nextInt(WIDTH - 100), 0, PLAYER_SIZE, ENEMIES_IMG[1]);
        enemy.dropsGold = RAND.nextInt(10) > 5;
        return enemy;
    }
    
    ShooterEnemy newShooterEnemy() {
        ShooterEnemy sEnemy = new ShooterEnemy(50 + RAND.nextInt(WIDTH - 100), 0, PLAYER_SIZE, ENEMIES_IMG[0], Color.YELLOW);
        sEnemy.dropsGold = RAND.nextInt(10) > 5;
        return sEnemy;
    }

       
    public static void main(String[] args) {
        launch(args);
    }
}