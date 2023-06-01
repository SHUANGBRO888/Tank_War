package pkg.main.mainApp.util;

import java.util.ArrayList;
import java.util.List;

import pkg.main.mainApp.maptile.Maptile;

public class MapTilePool{

    public static final int DEFAULT_POOL_SIZE = 50;
    public static final int POOL_MAX_SIZE = 70;

    private static List<Maptile> pool = new ArrayList<>();
    static{
        for (int i = 0; i <DEFAULT_POOL_SIZE ; i++) {
            pool.add(new Maptile());
        }
    }

    public static Maptile get(){
        Maptile tile = null;
        if(pool.size() == 0){
            tile = new Maptile();
        }else{
            tile = pool.remove(0);
        }
        return tile;
    }

    public static void returnBack(Maptile tile){
        if(pool.size() == POOL_MAX_SIZE){
            return;
        }
        pool.add(tile);
    }
}
