package pkg.main.mainApp.maptile;


import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.util.Properties;

import pkg.main.mainApp.game.GameFrame;
import pkg.main.mainApp.game.LevelInfo;
import pkg.main.mainApp.tank.Tank;
import pkg.main.mainApp.util.Constant;
import pkg.main.mainApp.util.MapTilePool;

public class GameMap {

    public static final int MAP_X = Tank.Radius*3;
    public static final int MAP_Y = Tank.Radius*3 + GameFrame.titleBarH;
    public static final int MAP_WIDTH = Constant.Frame_Width-Tank.Radius*6;
    public static final int MAP_HEIGHT = Constant.Frame_Height-Tank.Radius*8-GameFrame.titleBarH;

    // Container for the Maptile
    private List<Maptile> tiles = new ArrayList<>();

    // House
    private TankHouse house;

    public GameMap() {}
    /**
     * Init maptile
     */
    public void initMap(int level){
        tiles.clear();
        try {
            loadLevel(level);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Init house
        house = new TankHouse();
        addHouse();
    }

    /**
     * Loading level 
     * @param level
     */
    private void loadLevel(int level) throws Exception{
        LevelInfo levelInfo = LevelInfo.getInstance();
        levelInfo.setLevel(level);

        Properties prop = new Properties();
        prop.load(new FileInputStream("level/lv_"+level));
        // Loading map info
        int enemyCount = Integer.parseInt(prop.getProperty("enemyCount"));
        // Set NO of enemy
        levelInfo.setEnemyCount(enemyCount);

        // Parse enemyType
        String[] enemyType = prop.getProperty("enemyType").split(",");
        int[] type = new int[enemyType.length];
        for (int i = 0; i < type.length; i++) {
            type[i] = Integer.parseInt(enemyType[i]);
        }
        // Set Enemy Type
        levelInfo.setEnemyType(type);
        // Level difficulty
        String levelType = prop.getProperty("levelType");
        levelInfo.setLevelType(Integer.parseInt(levelType==null? "1" : levelType));

        String methodName = prop.getProperty("method");
        int invokeCount = Integer.parseInt(prop.getProperty("invokeCount"));
        // Reading the 
        String[] params = new String[invokeCount];
        for (int i = 1; i <=invokeCount ; i++) {
            params[i-1] = prop.getProperty("param"+i);
        }
        // Use the read parameters to invoke the corresponding method.
        invokeMethod(methodName,params);
    }

    // Invoke the corresponding method based on the method name and parameters.
    private void invokeMethod(String name,String[] params){
        for (String param : params) {
            // Parse the parameters of each method in each line.
            String[] split = param.split(",");
            // Use an int array to store the parsed content.
            int[] arr = new int[split.length];
            int i;
            for (i = 0; i < split.length-1; i++) {
                arr[i] = Integer.parseInt(split[i]);
            }
            // The interval between blocks is a multiple of the map block.
            final int DIS = Maptile.tileW ;

            // Parse the last double value.
            int dis = (int)(Double.parseDouble(split[i])*DIS);
            switch(name){
                case "addRow":
                    addRow(MAP_X+arr[0]*DIS,MAP_Y+arr[1]*DIS,
                            MAP_X+MAP_WIDTH-arr[2]*DIS,arr[3],dis);
                    break;
                case "addCol":
                    addCol(MAP_X+arr[0]*DIS,MAP_Y+arr[1]*DIS,
                            MAP_Y+MAP_HEIGHT-arr[2]*DIS,
                            arr[3],dis);
                    break;
                case "addRect":
                    addRect(MAP_X+arr[0]*DIS,MAP_Y+arr[1]*DIS,
                            MAP_X+MAP_WIDTH-arr[2]*DIS,
                            MAP_Y+MAP_HEIGHT-arr[3]*DIS,
                            arr[4],dis);
                    break;
            }
        }
    }

    // Add all the tiles of the house to the map container.
    private void addHouse(){
        tiles.addAll(house.getTiles());
    }

    /**
     * Check if a map tile determined by a point overlaps with all the tiles in the 'tiles' collection.
     * @param tiles
     * @param x
     * @param y
     * @return true if there is an overlap, false otherwise
     */
    private boolean isCollide(List<Maptile> tiles, int x ,int y){
        for (Maptile tile : tiles) {
            int tileX = tile.getX();
            int tileY = tile.getY();
            if(Math.abs(tileX-x) < Maptile.tileW && Math.abs(tileY-y) < Maptile.tileW){
                return true;
            }
        }
        return false;
    }

    /**
     *  Draw only the tiles without any cover effect.
     * @param g
     */
    public void drawBk(Graphics g){
        for (Maptile tile : tiles) {
            if(tile.getType() != Maptile.TYPE_COVER)
                tile.draw(g);
        }
    }

    /**
     * Draw only the tiles with a cover effect.
     * @param g
     */
    public void drawCover(Graphics g){
        for (Maptile tile : tiles) {
            if(tile.getType() == Maptile.TYPE_COVER)
                tile.draw(g);
        }
    }

    public List<Maptile> getTiles() {
        return tiles;
    }

    /**
     * Remove all the invisible tiles from the tile container.
     */
    public void clearDestroyTile(){
        for (int i = 0; i < tiles.size(); i++) {
            Maptile tile = tiles.get(i);
            if(!tile.isVisible())
                tiles.remove(i);
        }
    }

    /**
    Add a row of specified type of tiles to the tile container.
 * @param startX - Starting x-coordinate of the tiles to be added
 * @param startY - Starting y-coordinate of the tiles to be added
 * @param endX - Ending x-coordinate of the tiles to be added
 * @param type - Type of the tiles
 * @param DIS - Interval between the center points of the tiles. If it is the width of a block, it means they are continuous; if it is greater than the width, they are non-continuous.
     */
    public void addRow(int startX,int startY, int endX, int type, final int DIS){
        int count  = (endX - startX +DIS )/(Maptile.tileW+DIS);
        for (int i = 0; i <count ; i++) {
            Maptile tile = MapTilePool.get();
            tile.setType(type);
            tile.setX(startX + i * (Maptile.tileW+DIS));
            tile.setY(startY);
            tiles.add(tile);
        }
    }

   /**
   Add a column of tiles to the tile container.
    * @param startX - Starting x-coordinate of the column
    * @param startY - Starting y-coordinate of the column
    * @param endY - Ending y-coordinate of the column
    * @param type - Type of the tiles
    * @param DIS - Interval between the center points of the tiles
    */
    public void addCol(int startX,int startY, int endY, int type, final int DIS){
        int count  = (endY - startY +DIS)/(Maptile.tileW+DIS);
        for (int i = 0; i <count ; i++) {
            Maptile tile = MapTilePool.get();
            tile.setType(type);
            tile.setX(startX );
            tile.setY(startY + i * (Maptile.tileW+DIS));
            tiles.add(tile);
        }
    }

    // Add tiles to the tile container for a specified rectangular region.
    public void addRect(int startX,int startY,int endX, int endY, int type, final int DIS){
        int rows = (endY-startY+DIS)/(Maptile.tileW+DIS);
        for (int i = 0; i <rows ; i++) {
            addRow(startX,startY+i*(Maptile.tileW+DIS),endX,type,DIS);
        }
    }



 
}
