/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.items;

import java.util.ArrayList;

/**
 *
 * @author GeoSonicDash
 */
public class SellableItem extends DefaultItem {
    private int baseCost;
    private int baseSell;
    public SellableItem(ArrayList<String> stats, ItemType itemType) {
        super(stats,itemType);
        this.baseCost = Integer.valueOf(stats.get(7));
        this.baseSell = Integer.valueOf(stats.get(8));
    }
   
}
