package pkg.main.mainApp.maptile;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import pkg.main.mainApp.util.Constant;

public class TankHouse {
    // House X & Y
    public static final int HOUSE_X = (Constant.Frame_Width-3*Maptile.tileW >> 1)+2;
    public static final int HOUSE_Y = Constant.Frame_Height-2 *Maptile.tileW;

    // Six mapTile
    private List<Maptile> tiles = new ArrayList<>();
    public TankHouse() {
        tiles.add(new Maptile(HOUSE_X,HOUSE_Y));
        tiles.add(new Maptile(HOUSE_X,HOUSE_Y+Maptile.tileW));
        tiles.add(new Maptile(HOUSE_X+Maptile.tileW,HOUSE_Y));

        tiles.add(new Maptile(HOUSE_X+Maptile.tileW*2,HOUSE_Y));
        tiles.add(new Maptile(HOUSE_X+Maptile.tileW*2,HOUSE_Y+Maptile.tileW));
        // text Map tile
        tiles.add(new Maptile(HOUSE_X+Maptile.tileW,HOUSE_Y+Maptile.tileW));
        // Set Type for the house
        tiles.get(tiles.size()-1).setType(Maptile.TYPE_HOUSE);
    }

    public  void draw(Graphics g){
        for (Maptile tile : tiles) {
            tile.draw(g);
        }
    }

    public List<Maptile> getTiles() {
        return tiles;
    }
}
