package pkg.main.mainApp.game;

import java.awt.*;

import pkg.main.mainApp.tank.*;
import pkg.main.mainApp.util.Constant;

public class Bullet {
    // Bullet Speed = Tank Speed * 2 
    public static final int Default_Speed = Tank.Default_Speed << 1;
    // Bullet Radius
    public static final int Radius = 4;

    private int x,y;
    private int speed = Default_Speed;
    private int dir;
    private int atk;
    private Color color;

    // Bullet Visible

    private boolean visible = true;


    // Set Bullet

    public Bullet(int x, int y, int dir, int atk, Color color) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.atk = atk;
        this.color = color;
    }

    // For bullet pool
    public Bullet(){}
    
    // Draw
    public void draw(Graphics g){
        if (!visible) return;

        logic();
        g.setColor(color);
        g.fillOval(x-Radius, y-Radius, Radius<<1, Radius<<1); 
    }
    // Logic
    private void logic(){
        move();
    }

    private void move(){
        switch (dir) {
            case Tank.Dir_Up:
                y -= speed;
                if (y <= 0 ) visible = false;
                break;
            case Tank.Dir_Down:
                y += speed;
                if (y > Constant.Frame_Height) visible = false;
                break;
            case Tank.Dir_Left:
                x -= speed;
                if (x <= 0 ) visible = false;
                break;
            case Tank.Dir_Right:
                x += speed;
                if (x > Constant.Frame_Width) visible = false;
                break;
        }
    }

    public int getX() {
        return x;
    }


    public void setX(int x) {
        this.x = x;
    }


    public int getY() {
        return y;
    }


    public void setY(int y) {
        this.y = y;
    }


    public int getSpeed() {
        return speed;
    }


    public void setSpeed(int speed) {
        this.speed = speed;
    }


    public int getDir() {
        return dir;
    }


    public void setDir(int dir) {
        this.dir = dir;
    }


    public int getAtk() {
        return atk;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
