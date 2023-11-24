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

public class App extends Application {

    //variables
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

    final int MAX_ENEMIES = 10, MAX_SHOTS = MAX_ENEMIES * 2;
    boolean gameOver = false;
    private javafx.scene.canvas.GraphicsContext gc;

    Spaceship player;
    List<Bullet> bullets;
    List<Universe> univ;
    List<Enemy> enemies;

    private double mouseX;
    static int score;

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
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        player = new Spaceship(WIDTH / 2, HEIGHT - PLAYER_SIZE, PLAYER_SIZE, PLAYER_IMG);
        score = 0;
        IntStream.range(0, MAX_ENEMIES).mapToObj(i -> newEnemy()).forEach(enemies::add);

    }

    private void run(javafx.scene.canvas.GraphicsContext gc) {
        gc.setFill(Color.grayRgb(20));
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setFont(Font.font(20));
        gc.setFill(Color.WHITE);
        gc.fillText("Score: " + score, 60, 20);

        if (gameOver && score < 50) {
            gc.setFont(Font.font(35));
            gc.setFill(Color.YELLOW);
            gc.fillText("BANGAA NIMO OI\n SCORE NIMO KAY: " + score + "\n Click to play again", WIDTH / 2, HEIGHT / 2.5);
        } else if (gameOver && score > 50) {
            gc.setFont(Font.font(35));
            gc.setFill(Color.YELLOW);
            gc.fillText("MAAYOHA NIMO OI\n SCORE NIMO KAY: " + score + "\n Click to play again", WIDTH / 2,
                    HEIGHT / 2.5);
        }
          univ.forEach(u -> u.draw(gc)); 
//            for(Universe u : univ) {
//                u.draw(gc);
//             }
        player.update();
        player.draw(gc);
        player.posX = (int) mouseX;

        enemies.stream().peek(e -> e.update()).peek(e -> e.draw(gc)).forEach(en -> {
            if (player.hit(en) && !player.exploding) {
                player.explode();
            }
        });
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            if (bullet.posY < 0 || bullet.toRemove) {
                bullets.remove(i);
                continue;
            }
            bullet.update();
            bullet.draw(gc);
            for (Enemy en : enemies) {
                if (bullet.hit(en) && !en.exploding) {
                    score++;
                    en.explode();
                    bullet.toRemove = true;
                }
            }
        }

        for (int i = enemies.size() - 1; i >= 0; i--) {
            if (enemies.get(i).destroyed) {
                enemies.set(i, newEnemy());
            }
        }

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
        return new Enemy(50 + RAND.nextInt(WIDTH - 100), 0, PLAYER_SIZE,
                ENEMIES_IMG[RAND.nextInt(ENEMIES_IMG.length)]);
    }

       
    public static void main(String[] args) {
        launch(args);
    }
}