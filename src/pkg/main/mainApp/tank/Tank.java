package pkg.main.mainApp.tank;

import java.awt.*;

import java.util.ArrayList;
import java.util.List;

import pkg.main.mainApp.game.Bullet;
import pkg.main.mainApp.game.Explode;
import pkg.main.mainApp.game.GameFrame;
import pkg.main.mainApp.maptile.Maptile;
import pkg.main.mainApp.util.*;


// Tank
public abstract class Tank {

    // Direction
    public static final int Dir_Up = 0;
    public static final int Dir_Down = 1;
    public static final int Dir_Left = 2;
    public static final int Dir_Right = 3;    
    // Radius
    public static final int Radius = 20;
    // Speed FPS = 30ms
    public static final int Default_Speed = 4;
    // Tank Status
    public static final int State_Stand = 0;
    public static final int State_Move = 1;
    public static final int State_Die = 2;
    // Tank default
    public static final int DEFAULT_HP = 100;
    private int maxHP = DEFAULT_HP;

    private int x,y;

    private int hp = DEFAULT_HP;
    private String name;

    private int atk;
    public static final int ATK_MAX = 25;
    public static final int ATK_MIN = 15;
    private int speed = Default_Speed;
    private int dir;
    private int state = State_Stand;
    private Color color;
    private boolean isEnemy = false;

    private BloodBar bar = new BloodBar();

    // Bullet
    private List<Bullet> bullets = new ArrayList();
    // Holding the explosion status
    private List<Explode> explodes = new ArrayList<>();


    // Creat Own Tank
    public Tank(int x, int y, int dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        initTank();
    }

    // For Pooling
    public Tank() {initTank();}

    private void initTank(){
        color = MyUtil.getRandomColor();
        name = MyUtil.getRandomName();
        atk = MyUtil.getRandomNumber(ATK_MIN,ATK_MAX);
    }


    /**
     * draw tank
     * @param g
     */
    public void draw(Graphics g){
        logic();

        drawImgTank(g);
        
        drawBullets(g);

        drawName(g);

        bar.draw(g);
    }

    private void drawName(Graphics g){
        g.setColor(color);
        g.setFont(Constant.Small_Font);
        g.drawString(name, x - Radius, y -35);
    }

    /*
     * Tank Image
     * 
     */

    public abstract void drawImgTank(Graphics g); 

    private void drawTank(Graphics g){
        g.setColor(color);
        // Draw Tank Circle
        g.fillOval(x-Radius, y-Radius, Radius<<1, Radius<<1); 
        int endX = x;
        int endY = y;       
        switch (dir) {
            case Dir_Up:
                endY = y - Radius * 2;
                g.drawLine(x-1, y, endX-1, endY);
                g.drawLine(x+1, y, endX+1, endY);
                break;
            case Dir_Down:
                endY = y + Radius * 2;    
                g.drawLine(x-1, y, endX-1, endY);
                g.drawLine(x+1, y, endX+1, endY);
                break;
            case Dir_Left:
                endX = x - Radius * 2;
                g.drawLine(x, y+1, endX, endY+1);
                g.drawLine(x, y-1, endX, endY-1);                        
                break;
            case Dir_Right:
                endX = x + Radius * 2;
                g.drawLine(x, y+1, endX, endY+1);
                g.drawLine(x, y-1, endX, endY-1);     
                break;
        }
        g.drawLine(x, y, endX, endY);
    }

    // Tank Logic
    private void logic(){
        switch (state) {
            case State_Stand:
                break;
            case State_Move:
                move();
                break;
            case State_Die:
                break;
        }
    }

    private int oldX = -1, oldY = -1;

    // Move
    private void move(){
        oldX = x;
        oldY = y;
        switch(dir){
            case Dir_Up:
                y -= speed;
                if (y < Radius + GameFrame.titleBarH) {y = Radius + GameFrame.titleBarH;}
                break;
            case Dir_Down:
                y += speed;
                if (y>Constant.Frame_Height -Radius) {y = Constant.Frame_Height - Radius;}
                break;
            case Dir_Left:
                x -= speed;
                if (x<Radius) {x = Radius;}
                break;
            case Dir_Right:
                x += speed;
                if (x>Constant.Frame_Width -Radius) {x = Constant.Frame_Width - Radius;}
                break;
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

    public int getHp() {
        return hp;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }
    public int getAtk() {
        return atk;
    }
    public void setAtk(int atk) {
        this.atk = atk;
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
    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public boolean isEnemy() {
        return isEnemy;
    }
    public void setEnemy(boolean isEnemy) {
        this.isEnemy = isEnemy;
    }
    public List<Bullet> getBullets() {
        return bullets;
    }
    public void setBullets(List<Bullet> bullets) {
        this.bullets = bullets;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tank{" +
                "x=" + x +
                ", y=" + y +
                ", hp=" + hp +
                ", atk=" + atk +
                ", speed=" + speed +
                ", dir=" + dir +
                ", state=" + state +
                '}';
    }


   /*
    * Fire
    * Create Bullet Object, assign attribute
    * Add to the list of Bullets
    */

    // Last Fire Time
    private long fireTime;
    // FIRE_INTERVAL
    public static final int FIRE_INTERVAL = 200;

    public void fire(){
        if(System.currentTimeMillis() - fireTime >FIRE_INTERVAL) {
            int bulletX = x;
            int bulletY = y;
            switch (dir) {
                case Dir_Up:
                    bulletY -= Radius;
                    break;
                case Dir_Down:
                    bulletY += Radius;
                    break;
                case Dir_Left:
                    bulletX -= Radius;
                    break;
                case Dir_Right:
                    bulletX += Radius;
                    break;
            }
            // Get from pool
            Bullet bullet = BulletsPool.get();
            // Bullet 
            bullet.setX(bulletX);
            bullet.setY(bulletY);
            bullet.setDir(dir);
            bullet.setAtk(atk);
            bullet.setColor(color);
            bullet.setVisible(true);
            bullets.add(bullet);

            // Recording the firetime 
            fireTime = System.currentTimeMillis();
        }
    }

    /**
     * For loop the bullet
     *  @param g
     */

    private void drawBullets(Graphics g){
        
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            bullet.draw(g);
        }

        // Clean the invisible bullet and return them to the pool
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            if (!bullet.isVisible()) { 
                Bullet remove = bullets.remove(i);
                i--;
                BulletsPool.returnBack(remove);}
        }
    }

    /*
     * Clean bullet when deal with tank
     */

    public void bulletsReturn(){
        for (Bullet bullet : bullets) {
            BulletsPool.returnBack(bullet);
        }
        bullets.clear();
    }

    // My Tank and Bullet Collision
    public void collideBullets(List<Bullet> bullets){
        // Check the bullet
        for (int i = 0; i < bullets.size(); i++) {
            Bullet bullet = bullets.get(i);
            int bulletY = bullet.getY();
            int bulletX = bullet.getX();
            if (MyUtil.isCollide(this.x, y, Radius, bulletX, bulletY)) {
                // Bullet Disappear
                bullet.setVisible(false);
                // Get atk
                hurt(bullet);
                // Exposion, tank as center
                addExplode(x,y+Radius);
            }
        }
    }

    private void addExplode(int x,int y){
        // Exposion, tank as center
        Explode explode = ExplodesPool.get();
        explode.setX(x);
        explode.setY(y);
        explode.setVisible(true);
        explode.setIndex(0);
        explodes.add(explode);
    }


    // Get Atk
    private void hurt(Bullet bullet){
        int atk = bullet.getAtk();
        System.out.println("atk = " + atk);
        hp -= atk;
        if (hp<0){hp = 0; die();}
    }

    // Tank die
    private void die(){
        // Ememy
        if (isEnemy) {
            GameFrame.killEnemyCount ++;
            // Return Object pooling
            EnemyTanksPool.returnBack(this);
            // Cross this level?
            if (GameFrame.isCrossLevel()){
                // Is the Last Level 
                if(GameFrame.isLastLevel()){
                    // Cross it 
                    GameFrame.setGameState(Constant.STATE_WIN);
                }else {
                    // Next Level
                    GameFrame.startCrossLevel();
                }
            }
        }else{
            delaySecondsToOver(3000);
        }
    }


    // Is die?
    public boolean isDie(){
        return hp <= 0;
    }


    public void drawExplodes(Graphics g){
        for (int i = 0; i < explodes.size(); i++) {
            Explode explode = explodes.get(i);
            explode.draw(g);
        }
        // Invisible explode return to the pool
        for (int i = 0; i < explodes.size(); i++) {
            Explode explode = explodes.get(i);
            if (!explode.isVisible()) {
                Explode remove = explodes.remove(i);
                ExplodesPool.returnBack(remove);
                i -- ; // Make sure is new object 
            }
        }
    }
    // Inter Class for HP
    class BloodBar{
        public static final int BAR_LENGTH = 50;
        public static final int BAR_HEIGHT = 3;

        public void draw(Graphics g){
            // Base Color
            g.setColor(Color.YELLOW);
            g.fillRect(x - Radius, y - Radius - BAR_HEIGHT*2, BAR_LENGTH, BAR_HEIGHT);
            // Red for HP
            g.setColor(Color.RED);
            g.fillRect(x - Radius, y - Radius - BAR_HEIGHT*2, hp*BAR_LENGTH/maxHP, BAR_HEIGHT);
            // White for frame
            g.setColor(Color.WHITE);
            g.drawRect(x - Radius, y - Radius - BAR_HEIGHT*2, BAR_LENGTH, BAR_HEIGHT);
            
        }
    }

    // Collision between tank bullets and all map tiles
    public void bulletsCollideMapTiles(List<Maptile> tiles){
        // Switched from foreach loop to basic for loop
        for (int i = 0; i < tiles.size(); i++) {
            Maptile tile = tiles.get(i);
            if(tile.isCollideBullet(bullets)){
                // Add explosion effect
                addExplode(tile.getX()+Maptile.radius,tile.getY() +Maptile.tileW);
                // Handling undestroyed cement tiles
                if(tile.getType() == Maptile.TYPE_HARD)
                    continue;
                // Set tile visibility to false
                tile.setVisible(false);
                // Return the tile to the object pool
                MapTilePool.returnBack(tile);
                // Switch to the game over screen after one second if the base is destroyed
                if(tile.isHouse()){
                    delaySecondsToOver(3000);
                }
            }
        }
    }

    /**
     * Delay for a specified number of milliseconds and switch to the game over screen.
     * @param millisSecond - Delay time in milliseconds
     */
    private void delaySecondsToOver(int millisSecond){
        new Thread(){
            public void run() {
                try {
                    Thread.sleep(millisSecond);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                GameFrame.setGameState(Constant.STATE_OVER);
            }
        }.start();
    }

    /**
    * Method to check collision between a map tile and the current tank.
    * Extract 8 points from the tile and check if any of them collide with the tank.
    * The points are ordered starting from the top-left corner and traversed clockwise.
    */
    public boolean isCollideTile(List<Maptile> tiles){
        final int len = 2;
        for (int i = 0; i < tiles.size(); i++) {
            Maptile tile = tiles.get(i);
            // Skip collision detection if the tile is not visible or if it's a cover tile
            if(!tile.isVisible() || tile.getType() == Maptile.TYPE_COVER)
                continue;
            // Point 1: Top-left corner
            int tileX = tile.getX();
            int tileY = tile.getY();
            boolean collide = MyUtil.isCollide(x, y, Radius, tileX, tileY);
            // Return true if collision occurs, otherwise continue checking the next point
            if(collide){return true;}
            // Point 2: Top-center point
            tileX += Maptile.radius;
            collide = MyUtil.isCollide(x, y, Radius, tileX, tileY);
            if(collide){return true;}
            // Point 3: Top-right corner
            tileX += Maptile.radius;
            collide = MyUtil.isCollide(x, y, Radius, tileX, tileY);
            if(collide){return true;}
            // Point 4: Middle-right point
            tileY += Maptile.radius;
            collide = MyUtil.isCollide(x, y, Radius, tileX, tileY);
            if(collide){return true;}
            // Point 5: Bottom-right corner
            tileY += Maptile.radius;
            collide = MyUtil.isCollide(x, y, Radius, tileX, tileY);
            if(collide){return true;}
            // Point 6: Bottom-center point
            tileX -= Maptile.radius;
            collide = MyUtil.isCollide(x, y, Radius, tileX, tileY);
            if(collide){return true;}
            // Point 7: Bottom-left point
            tileX -= Maptile.radius;
            collide = MyUtil.isCollide(x, y, Radius, tileX, tileY);
            if(collide){return true;}
            // Point 8: Left-center point
            tileY -= Maptile.radius;
            collide = MyUtil.isCollide(x, y, Radius, tileX, tileY);
            if(collide){return true;}
        }
        return false;
    }

    public void back(){
        x = oldX;
        y = oldY;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }
}
