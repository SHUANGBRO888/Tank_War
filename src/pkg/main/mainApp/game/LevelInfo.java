package pkg.main.mainApp.game;

import pkg.main.mainApp.util.MyUtil;

/**
Used to manage the information of the current level: singleton class
Singleton design pattern: If a class only needs to have a unique instance, you can use
the singleton design pattern to design that class.
*/
public class LevelInfo {
    // Private constructor
    private LevelInfo() {}
    
    // Define a static variable of the current class type to point to the unique instance
    private static LevelInfo instance;
    
    // This method has thread safety issues and may create multiple instances in a multi-threaded environment
    public static LevelInfo getInstance() {
        if (instance == null) {
        // Create the unique instance
        instance = new LevelInfo();
        }
        return instance;
    }
    
    // Level number
    private int level;
    // Number of enemies in the level
    private int enemyCount;
    // Required completion time for the level, -1 means no time limit
    private int crossTime = -1;
    // Enemy type information
    private int[] enemyType;
    // Game difficulty level, minimum value is 1
    private int levelType;
    
    public int getLevel() {
    return level;
    }
    
    public void setLevel(int level) {
    this.level = level;
    }
    
    public int getEnemyCount() {
    return enemyCount;
    }
    
    public void setEnemyCount(int enemyCount) {
    this.enemyCount = enemyCount;
    }
    
    public int getCrossTime() {
    return crossTime;
    }
    
    public void setCrossTime(int crossTime) {
    this.crossTime = crossTime;
    }
    
    public int[] getEnemyType() {
    return enemyType;
    }
    
    public void setEnemyType(int[] enemyType) {
    this.enemyType = enemyType;
    }
    
    public int getLevelType() {
    return levelType <= 0 ? 1 : levelType;
    }
    
    public void setLevelType(int levelType) {
    this.levelType = levelType;
    }
    
    /**
    * Get a random enemy type from the enemy type array.
    * Get a random enemy type.
    * @return A random enemy type.
    */
    public int getRandomEnemyType() {
        int index = MyUtil.getRandomNumber(0, enemyType.length);
        return enemyType[index];
    }
}
    
    
    
    
    