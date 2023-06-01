package pkg.main.mainApp.game;

import java.awt.*;

import pkg.main.mainApp.util.MyUtil;

/*
 * 
 * Explosion
 */

public class Explode {
    public static final int Explode_Frame_Count = 12;
    // import
    private static Image[] img;
    // Explode Size
    private static int explpdeWidth;
    private static int explodeHeight;

    static{
        img = new Image[Explode_Frame_Count/3];
        for (int i = 0; i < img.length; i++) {
            img[i] =MyUtil.createImage("res/boom_" + i + ".png");
        }
    }

    // Explotion
    private int x,y;

    // Current FPS [0,11]
    private int index;

    private boolean visible =true;
  
    public Explode() {
        index = 0;
    }

    public Explode(int x, int y) {
        this.x = x;
        this.y = y;
        index = 0;
    }



    public void draw(Graphics g){
        // Explosion pic size
        if(explodeHeight <= 0){
            explodeHeight = img[0].getHeight(null);
            explpdeWidth = img[0].getWidth(null)>>1;
        }
        // visibility
        if (!visible) return;
        g.drawImage(img[index/3],x-explpdeWidth, y-explodeHeight, null);
        index ++;
        // Last one is invisible
        if (index >= Explode_Frame_Count) {
            visible =false;
        }
    }

    // Getter & Setter
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

    public boolean isVisible() {
        return visible;
    }


    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getIndex() {
        return index;
    }


    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Explode{" +
                "x=" + x +
                ", y=" + y +
                ", index=" + index +
                ", visible=" + visible +
                '}';
    }
}
