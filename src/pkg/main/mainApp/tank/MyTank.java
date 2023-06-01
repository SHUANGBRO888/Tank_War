package pkg.main.mainApp.tank;

import java.awt.*;

import pkg.main.mainApp.util.MyUtil;

public class MyTank extends Tank{

    // Tank Image
    private static Image[] tankImg;
    
    static{
        tankImg = new Image[4];
        for (int i = 0; i <tankImg.length ; i++) {
            tankImg[i] = MyUtil.createImage("res/tank1_"+i+".png");
        }
    }

    public MyTank(int x, int y, int dir){
        super(x, y, dir);      
    }
    
    @Override
    public void drawImgTank(Graphics g){
       g.drawImage(tankImg[getDir()], getX()-Radius, getY()-Radius, null);
    }
}
