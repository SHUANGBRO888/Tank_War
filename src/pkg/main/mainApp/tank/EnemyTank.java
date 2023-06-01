package pkg.main.mainApp.tank;

import java.awt.*;

import pkg.main.mainApp.game.GameFrame;
import pkg.main.mainApp.game.LevelInfo;
import pkg.main.mainApp.util.Constant;
import pkg.main.mainApp.util.EnemyTanksPool;
import pkg.main.mainApp.util.MyUtil;

public class EnemyTank extends Tank{

    public static final int TYPE_GREEN = 0;
    public static final int TYPE_YELLOW = 1;
    private int type = TYPE_GREEN;

    private static Image[] greenImg;
    private static Image[] yellowImg;

    // Record 5s start
    private long aiTime;

    static{
        greenImg = new Image[4];
        greenImg[0] = MyUtil.createImage("res/ul.png");
        greenImg[1] = MyUtil.createImage("res/dl.png");
        greenImg[2] = MyUtil.createImage("res/ll.png");
        greenImg[3] = MyUtil.createImage("res/rl.png");
        yellowImg = new Image[4];
        yellowImg[0] = MyUtil.createImage("res/u.png");
        yellowImg[1] = MyUtil.createImage("res/d.png");
        yellowImg[2] = MyUtil.createImage("res/l.png");
        yellowImg[3] = MyUtil.createImage("res/r.png");
    }
    
    private EnemyTank(int x, int y, int dir){
        super(x, y, dir); 
        // Count down when create the AI
        aiTime = System.currentTimeMillis();
        type = MyUtil.getRandomNumber(0,2);
    }   

    // For Pooling
    public EnemyTank(){
        type = MyUtil.getRandomNumber(0,2);
        aiTime = System.currentTimeMillis();
    }

      // Create Enermies Tank
      public static Tank createEnemy(){
        int x = MyUtil.getRandomNumber(0, 2) == 0 ? Radius : Constant.Frame_Width -  Radius;
        int y = GameFrame.titleBarH +  Radius;
        int dir = Dir_Down;

        EnemyTank enemy = (EnemyTank)EnemyTanksPool.get();
        enemy.setX(x);
        enemy.setY(y);
        enemy.setDir(dir);
        enemy.setEnemy(true);
        enemy.setState(State_Move);

        // Set HP according to the Level
        int maxHp = Tank.DEFAULT_HP*LevelInfo.getInstance().getLevelType();
        enemy.setHp(maxHp);
        enemy.setMaxHP(maxHp);
        // Set Enemy Type
        int enemyType = LevelInfo.getInstance().getRandomEnemyType();
        enemy.setType(enemyType);

        return enemy;
    }

    public void drawImgTank(Graphics g){
        ai();
        g.drawImage(type == TYPE_GREEN ? greenImg[getDir()] :
        yellowImg[getDir()],getX()-Radius,getY()-Radius,null);
    }

    // AI 
    private void ai(){
        if (System.currentTimeMillis() - aiTime > Constant.Enemy_AI_Interval) {
            // Random Status every 5s
            setDir(MyUtil.getRandomNumber(Dir_Up, Dir_Right+1));
            setState(MyUtil.getRandomNumber(0, 2) == 0 ? State_Stand: State_Move);
            aiTime = System.currentTimeMillis();
        }
        // Low chance to fire
        if (Math.random() < Constant.Enemy_Fire_Chance) {
            fire();
        }
    }

    // Getter & Setter
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
   


