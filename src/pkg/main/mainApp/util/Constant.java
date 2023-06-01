package pkg.main.mainApp.util;

import java.awt.*;

/**
 *  Constant
 */
public class Constant {
    // Window
    public static final String Game_Title = "Tank War";

    public static final int Frame_Height = 600;
    public static final int Frame_Width = 800;

    // Get height * Width 
    public static final int SCREEN_W = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int SCREEN_H = Toolkit.getDefaultToolkit().getScreenSize().height;

    public static final int Frame_X = SCREEN_W-Frame_Width>>1;
    public static final int Frame_Y = SCREEN_H-Frame_Height>>1;


    // Menu
    public static final int STATE_MENU = 0;
    public static final int STATE_HELP = 1;
    public static final int STATE_ABOUT = 2;
    public static final int STATE_RUN = 3;
    public static final int STATE_OVER = 4;
    public static final int STATE_WIN = 5;
    public static final int STATE_CROSS = 6;

    public  static final String[] MENUS = {
            "Start Game",
            "Continue",
            "Help",
            "About",
            "Exit"
    };

    public static final String Over_Str0 = "Press 'Esc' to exit";
    public static final String Over_Str1 = "Press 'Enter' back to Menus";

    // Font
    public static final Font Game_Font = new Font("Times New Roman",Font.BOLD,24);
    public static final Font Small_Font = new Font("Times New Roman",Font.BOLD,12);

    public static final int Repaint_Interval = 30;
    // Enemy No
    public static final int Enemy_Max_Count = 10;
    public static final int Enemy_Born_Interval = 5000;

    public static final int Enemy_AI_Interval = 3000;
    public static final double Enemy_Fire_Chance = 0.03;


}
