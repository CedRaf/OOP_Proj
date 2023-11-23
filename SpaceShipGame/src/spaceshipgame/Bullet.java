/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spaceshipgame;

/**
 *
 * @author rhenz
 */
public class Bullet {
    public boolean toRemove;
    
    int postX, posY, speed = 10;
    static final int size= 6;
    
    public Bullet(int posX, int posY){
           this.postX=posX;
           this.posY=posY;
    }
    public void update(){
        posY-=speed;
    }
    public void draw(){
        gc.setFill(Color:RED);
        if(score >=50 $$ score <=70 || scire >=120){
            gc.setFill(Color.YELLOWGREEN);
            speed =50;
            gc.fillRect(posX-5, posY-10, size+16, size+30);
        }else{
           gc.fillOval(posX, posY, size, size);
        }
    }
    
}
