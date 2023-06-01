package pkg.main.mainApp.util;

import java.awt.*;
import java.net.URL;

/**
 * Tools
 */
public class MyUtil {
    private  MyUtil(){}

    /**
     * Get a random number within a specified range
     * @param min The minimum value of the range, inclusive
     * @param max The maximum value of the range, exclusive
     * @return Random number
     */
    public static final int getRandomNumber(int min, int max){
        return (int)(Math.random()*(max-min)+min);
    }

    /**
     * Get a random color
     * @return
     */
    public static final Color getRandomColor(){
        int red = getRandomNumber(0,256);
        int blue = getRandomNumber(0,256);
        int green = getRandomNumber(0,256);
        return new Color(red,green,blue);
    }

    /**
     * Determines if a point is within a square,
     * @param rectX The x-coordinate of the center point of the square
     * @param rectY The y-coordinate of the center point of the square
     * @param radius Half of the side length of the square
     * @param pointX The x-coordinate of the point
     * @param pointY The y-coordinate of the point
     * @return If the point is within the square, return true, otherwise false
     */
    public static final boolean isCollide(int rectX,int rectY,int radius,int pointX,int pointY){
        // The distance of x and y axis between the center of the square and the point
        int disX = Math.abs(rectX - pointX);
        int disY = Math.abs(rectY - pointY);
        if (disX <= radius && disY <= radius)
            return true;
        return false;
    }

    /**
    * Create and load an image object based on the resource path of the picture
    * @param path The path of the picture resource
    * @return
    */
    public static final Image createImage(String path){
        return Toolkit.getDefaultToolkit().createImage(path);
    }

    private static final String[] NAMES = {
        "TankBlast", 
        "ArmorClash", 
        "IronStorm", 
        "SteelRumble", 
        "ShellShock", 
        "TankFury", 
        "ArmorAssault", 
        "MetalMayhem", 
        "BattleTreads", 
        "SteelClash",
        "WarMachines", 
        "TankThunder", 
        "ArmorWarfare", 
        "SteelBarrage", 
        "IronBattle",
        "TankTempest", 
        "ShellFury", 
        "IronConflict", 
        "TankConflict",
        "SteelOnslaught",
        "ArmorInferno",
        "ShellStorm",
        "IronRampage",
        "TankTornado",
        "SteelWarpath",
        "ArmorBrawl",
        "TankCrusade",
        "IronSiege",
        "SteelBlitz",
        "TankRebellion"
    };

    /**
     * Get a random name
     * @return
     */
    public static final String getRandomName(){
        return NAMES[getRandomNumber(0,NAMES.length)];
    }
}
