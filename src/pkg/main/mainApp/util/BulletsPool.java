package pkg.main.mainApp.util;

import java.util.ArrayList;
import java.util.List;

import pkg.main.mainApp.game.Bullet;

public class BulletsPool {

    public static final int DEFAULT_POOL_SIZE = 200;
    public static final int POOL_MAX_SIZE = 300;

    // Container
    private static List<Bullet> pool = new ArrayList<>();
    static{
        for (int i = 0; i <DEFAULT_POOL_SIZE ; i++) {
            pool.add(new Bullet());
        }
    }

    /**
     * @return
     */
    public static Bullet get(){
        Bullet bullet = null;
        if(pool.size() == 0){
            bullet = new Bullet();
        }else{
            bullet = pool.remove(0);
        }
        return bullet;
    }
    public static void returnBack(Bullet bullet){
        if(pool.size() == POOL_MAX_SIZE){
            return;
        }
        pool.add(bullet);
    }
}
