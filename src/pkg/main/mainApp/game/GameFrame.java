package pkg.main.mainApp.game;

import static pkg.main.mainApp.util.Constant.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.List;

import pkg.main.mainApp.maptile.GameMap;
import pkg.main.mainApp.tank.EnemyTank;
import pkg.main.mainApp.tank.MyTank;
import pkg.main.mainApp.tank.Tank;
import pkg.main.mainApp.util.MusicUtil;
import pkg.main.mainApp.util.MyUtil;
/*
 * Main Window
 * All Games content within this class
 */
public class GameFrame extends Frame implements Runnable{
    // 1st time for use, improve effiency
    private Image overImg  = null;

    // Define a pic same as window
    private BufferedImage bufImg = new BufferedImage(Frame_Width, Frame_Height, BufferedImage.TYPE_4BYTE_ABGR);
    // Game Statu
    public static int gameState;
    // Menu index
    private static int menuIndex;
    // titleBarh
    public static int titleBarH;
    // Define Tank
    private static Tank myTank;
    // Define Enemies Tank
    private List<Tank> enemies = new ArrayList<>();

    // Record No. Enermy 
    private static int bornEnemyCount;
    
    public static int killEnemyCount;

    // Define Game Map
    private static GameMap gameMap = new GameMap();

    /**
     *  Intialize the windown
     */
    
    public GameFrame() {
        initFrame();
        initEventListener();
        // Start a thread to refresh a window, FPS
        new Thread(this).start();
    }
    /**
     * Next Level
     */
    private void nextLevel() {
        newGame(LevelInfo.getInstance().getLevel()+1);
    }
    // Flash for cross the level
    public static int flashTime;
    public static final int RECT_WIDTH = 40;
    public static final int RECT_COUNT = Frame_Width/RECT_WIDTH+1;
    public static boolean isOpen = false;
    public static void startCrossLevel(){
        gameState = STATE_CROSS;
        flashTime = 0;
        isOpen = false;
    }

     // Draw Cross level
     public void drawCross(Graphics g){
        gameMap.drawBk(g);
        myTank.draw(g);
        gameMap.drawCover(g);

        g.setColor(Color.BLACK);
        // Close the blind
        if(!isOpen) {
            for (int i = 0; i < RECT_COUNT; i++) {
                g.fillRect(i * RECT_WIDTH, 0, flashTime, Frame_Height);
            }
            // Close Flash time 
            if (flashTime++ - RECT_WIDTH > 5) {
                isOpen = true;
                // Initial next map
                gameMap.initMap(LevelInfo.getInstance().getLevel()+1);
            }
        }else{
            // Open the blind
            for (int i = 0; i < RECT_COUNT; i++) {
                g.fillRect(i * RECT_WIDTH, 0, flashTime, Frame_Height);
            }
            if(flashTime-- == 0){
                newGame(LevelInfo.getInstance().getLevel());
            }
        }
    }
     /*
     *  Intialize the Game State
     */
    private void initGame(){
        gameState = STATE_MENU;
    }

    /*
     *  Intialize the index
     */
    private void initFrame(){
        // Set Title
        setTitle(Game_Title);
        // Set Windown Size
        setSize(Frame_Width, Frame_Height);
        // Set the coordinates of the top-left corner  Graphing points in a two-dimensional plane with X and Y.
        setLocation(Frame_X, Frame_Y);
        // Size unchangable 
        setResizable(false);
        // Set Visible
        setVisible(true);

        // Set the titleBarH
        titleBarH = getInsets().top;
    }

    /**
    This method belongs to the Frame class and is inherited by subclasses.
    It is responsible for drawing all visible content on the screen.
    Any content that needs to be displayed on the screen must be called within this method.
    This method cannot be called directly and must be invoked through calling repaint().
    @param g1 the system-provided graphics pen, initialized by the system
    */

    public void update(Graphics g1){
        // 2. Get the pic paint
        Graphics g = bufImg.getGraphics();
        // 3. Using paint on the pic
        g.setFont(Game_Font);
        switch (gameState) {
            case STATE_MENU:
                drawMenu(g);
                break;
            case STATE_HELP:
                drawHelp(g);
                break;
            case STATE_ABOUT:
                drawAbout(g);
                break;
            case STATE_RUN:
                drawRun(g);
                break;
            case STATE_OVER:
                drawOver(g,"GAME OVER");
                break;
            case STATE_WIN:
                drawWin(g);
                break;
            case STATE_CROSS:
                drawCross(g);
                break;
        }

        // 4 . Using system paint on the frame
        g1.drawImage(bufImg, 0, 0, null);
    }

    private void drawOver(Graphics g,String str) {
        // Load one time 
        if (overImg == null) {
            overImg = MyUtil.createImage("res/over.jpg");
        }

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Frame_Width, Frame_Height);

        int imgW = overImg.getWidth(null);
        int imgH = overImg.getHeight(null);

        g.drawImage(overImg, Frame_Width - imgW >> 1, Frame_Height - imgH >> 1, null);

        // Remainder for input 
        g.setColor(Color.WHITE);
        g.drawString(Over_Str0, 10, Frame_Height -20);
        g.drawString(Over_Str1, Frame_Width - 200, Frame_Height -20);

        // Game over text
        g.setColor(Color.WHITE);
        g.drawString(str,Frame_Width/2-30,50);
    }

      /**
     * Victory Page
     * @param g
     */
    private void drawWin(Graphics g){
        drawOver(g,"Game Passed");
    }

    private void drawRun(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,Frame_Width,Frame_Height);

        // Draw Map
        gameMap.drawBk(g);

        drawEnemies(g);

        myTank.draw(g);

        // Draw Cover
        gameMap.drawCover(g);

        drawExplodes(g);

        // Bullet and Tank Collision
        bulletCollideTank();

        // All collision
        bulletAndTanksCollideMapTile();
    }

    // Draw enmeies, if die just remove from the pool
    private void drawEnemies(Graphics g){
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            if (enemy.isDie()) {
                enemies.remove(i);
                i--;
                continue;
            }
            enemy.draw(g);
        }
    }

    private Image helpImg;
    private Image aboutImg;

    private void drawAbout(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,Frame_Width,Frame_Height);
        if(aboutImg == null){
            aboutImg = MyUtil.createImage("res/about.png");
        }
        int width = aboutImg.getWidth(null);
        int height = aboutImg.getHeight(null);

        int x = Frame_Width - width >>1;
        int y = Frame_Height - height >> 1;
        g.drawImage(aboutImg,x,y,null);

        g.setColor(Color.WHITE);
        g.drawString("Press any buttom continue",10,Frame_Height-10);
    }

    private void drawHelp(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0,0,Frame_Width,Frame_Height);
        if(helpImg == null){helpImg = MyUtil.createImage("res/help.png");}

        int width = helpImg.getWidth(null);
        int height = helpImg.getHeight(null);

        int x = Frame_Width - width >>1;
        int y = Frame_Height - height >> 1;
        g.drawImage(helpImg,x,y,null);

        g.setColor(Color.WHITE);
        g.drawString("Press any buttom continue",10,Frame_Height-10);
    }

    // Draw Menu
    private void drawMenu(Graphics g){
        // Black Background
        g.setColor(Color.BLACK);
        g.fillRect(0,0,Frame_Width,Frame_Height);

        final int STR_Width = 76;
        int x = Frame_Width - STR_Width >> 1;
        int y = Frame_Height / 3; 
        
        final int Distance = 50;

        for (int i = 0; i < MENUS.length; i++) {
                if (i == menuIndex) {
                    // Menu to Red
                    g.setColor(Color.RED); 
                } else{
                    g.setColor(Color.WHITE);
                }
                g.drawString(MENUS[i], x, y+ Distance * i);
            }
    }

    /*
     * EventListener
     */

    private void initEventListener() {
        // Create Listener Event
        addWindowListener(new WindowAdapter() {
            // Closing windown, then be used
            @ Override
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });

        // Create Key Listener
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e){
                // get the Key Value
                int keyCode = e.getKeyCode();
                switch (gameState) {
                    case STATE_MENU:
                        keyPressedEventMenu(keyCode);
                        break;
                    case STATE_HELP:
                        keyPressedEventHelp(keyCode);
                        break;
                    case STATE_ABOUT:
                        keyPressedEventAbout(keyCode);
                        break;
                    case STATE_RUN:
                        keyPressedEventRun(keyCode);
                        break;
                    case STATE_OVER:
                        keyPressedEventOver(keyCode);
                        break;
                    case STATE_WIN:
                        keyPressedEventWin(keyCode);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e){
                // get the Key Value
                int keyCode = e.getKeyCode();
                if (gameState == STATE_RUN) {
                    keyReleasedEventRun(keyCode);
                }
            }            
        });    
    }

    // Game all passed
    private void keyPressedEventWin(int keyCode) {keyPressedEventOver(keyCode);}

    // Release the key
    private void keyReleasedEventRun(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                myTank.setState(Tank.State_Stand);
                break;
        }
    }

    // Game Over
    private void keyPressedEventOver(int keyCode) {
        if (keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        } else if (keyCode == KeyEvent.VK_ENTER) {
            setGameState(STATE_MENU);
            // Reset the game
            resetGame();
        }
    }

    // reset Game after game over
    private void resetGame(){
        killEnemyCount = 0;
        menuIndex = 0;
        // Clean bullet
        myTank.bulletsReturn();
        // Reset my tank
        myTank = null;
        // Reset Enemy Tank
        for (Tank enermy: enemies) {
            enermy.bulletsReturn();
        }
        enemies.clear();
        // Clear the Game Map;
        gameMap =null;
    }

    // Key Press
    private void keyPressedEventRun(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                myTank.setDir(Tank.Dir_Up);
                myTank.setState(Tank.State_Move);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                myTank.setDir(Tank.Dir_Down);
                myTank.setState(Tank.State_Move);
                break; 
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                myTank.setDir(Tank.Dir_Left);
                myTank.setState(Tank.State_Move);
                break; 
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                myTank.setDir(Tank.Dir_Right);
                myTank.setState(Tank.State_Move);
                break;
            case KeyEvent.VK_SPACE:
                myTank.fire();
                break;   
        }
    }

    private void keyPressedEventAbout(int keyCode) {setGameState(STATE_MENU);}

    private void keyPressedEventHelp(int keyCode) {setGameState(STATE_MENU);}
    
    // Key under the Menu
    private void keyPressedEventMenu(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (--menuIndex <0) menuIndex = MENUS.length - 1;
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                if (++menuIndex > MENUS.length - 1) menuIndex = 0;
                break; 
            case KeyEvent.VK_ENTER:
            switch(menuIndex){
                case 0:
                    newGame(1);
                    break;
                case 1:
                    // Select level
                    break;
                case 2:
                    setGameState(STATE_HELP);
                    break;
                case 3:
                    setGameState(STATE_ABOUT);
                    break;
                case 4:
                    System.exit(0);
                    break;
            }
            break;    
        }
    }

    private void newGame(int level) {
        enemies.clear();
        if(gameMap == null){
            gameMap = new GameMap();
        }
        gameMap.initMap(level);
        MusicUtil.playStart();
        killEnemyCount = 0;
        bornEnemyCount = 0;
        gameState = STATE_RUN;
        // Creat Tank
        myTank = new MyTank(Frame_Width/3,Frame_Height-Tank.Radius,Tank.Dir_Up);

        // Create a theread to create Enemy
        new Thread(){
            @Override
            public void run(){
                while (true) {
                    if(LevelInfo.getInstance().getEnemyCount()>bornEnemyCount&&enemies.size() < Enemy_Max_Count){
                        Tank enemy = EnemyTank.createEnemy();
                        enemies.add(enemy);
                        bornEnemyCount ++;
                    }
                    try {
                        Thread.sleep(Enemy_Born_Interval);
                    }  catch (InterruptedException e) {
                        e.printStackTrace();
                    };
                    // Create enemy when game is run.
                    if (gameState != STATE_RUN) {
                        enemies.clear();
                        break;
                    }
                }
            }
        }.start();
    }

    @Override
    public void run() {
        while (true) {
            // use repait, then update()
            repaint();
            try {
                Thread.sleep(Repaint_Interval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            };
        }
    }

    // Tank and bullet Collission
    private void bulletCollideTank(){
        // My bullets hit enemy
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.collideBullets(myTank.getBullets());
        }
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            myTank.collideBullets(enemy.getBullets());
        }
    }
    
    //All collision
    private void bulletAndTanksCollideMapTile(){
        // my tank & bullet 
        myTank.bulletsCollideMapTiles(gameMap.getTiles());
        // enemy & bullet
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.bulletsCollideMapTiles(gameMap.getTiles());
        }
        // my tank & maptile
        if(myTank.isCollideTile(gameMap.getTiles())){
            myTank.back();
        }
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            if(enemy.isCollideTile(gameMap.getTiles())){
                enemy.back();
            }
        }
        // clean all
        gameMap.clearDestroyTile();
    }

    

    // All tank explode
    private void drawExplodes(Graphics g){
        for (int i = 0; i < enemies.size(); i++) {
            Tank enemy = enemies.get(i);
            enemy.drawExplodes(g);
        }
        myTank.drawExplodes(g);
    }
    
    public static int getGameState() {
        return gameState;
    }

    public static void setGameState(int gameState) {
        GameFrame.gameState = gameState;
    }


    /**
     * Check if it is the last level.
     * @return true if it is the last level, false otherwise
     */
    public static boolean isLastLevel(){
        // The current level is equal to the total number of levels
        int currLevel = LevelInfo.getInstance().getLevel();
        int levelCount = GameInfo.getLevelCount();
        return currLevel == levelCount;
    }

    /**
     * Check if the level is completed.
     * @return true if all enemies are killed, false otherwise
     */
    public static boolean isCrossLevel(){
        // The number of killed enemies is equal to the number of enemies in the level
        return killEnemyCount == LevelInfo.getInstance().getEnemyCount();
    }
}
