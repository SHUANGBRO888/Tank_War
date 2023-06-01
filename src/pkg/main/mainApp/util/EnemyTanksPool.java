package pkg.main.mainApp.util;


import java.util.ArrayList;
import java.util.List;

import pkg.main.mainApp.tank.EnemyTank;
import pkg.main.mainApp.tank.Tank;

/**
 * Pool
 */
public class EnemyTanksPool {

    public static final int DEFAULT_POOL_SIZE = 20;
    public static final int POOL_MAX_SIZE = 20;

    private static List<Tank> pool = new ArrayList<>();
    static{
        for (int i = 0; i <DEFAULT_POOL_SIZE ; i++) {
            pool.add(new EnemyTank());
        }
    }

    public static Tank get(){
        Tank tank = null;
        if(pool.size() == 0){
            tank = new EnemyTank();
        }else{
            tank = pool.remove(0);
        }
        return tank;
    }

    public static void returnBack(Tank tank){
        if(pool.size() == POOL_MAX_SIZE){
            return;
        }
        pool.add(tank);
    }
}
