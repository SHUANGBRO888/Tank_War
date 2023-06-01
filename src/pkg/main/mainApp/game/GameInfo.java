package pkg.main.mainApp.game;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GameInfo {
    // No. Game Level
    private static int levelCount;
    // Load from properties
    static{
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("level/gameinfo"));
            levelCount = Integer.parseInt(prop.getProperty("levelCount"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getLevelCount() {
        return levelCount;
    }
    
}
