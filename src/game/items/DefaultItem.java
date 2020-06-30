/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.items;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

/**
 *
 * @author GeoSonicDash
 */
public class DefaultItem {
    private int xRef;
    private int yRef;
    private int length;
    private int width;
    private String itemName;
    private String itemDescription;
    private Image itemPicture;
    private ItemType itemType;
    public DefaultItem(ArrayList<String> stats, ItemType itemType) {
        this.xRef = Integer.valueOf(stats.get(1));
        this.yRef = Integer.valueOf(stats.get(2));
        this.length = Integer.valueOf(stats.get(3));
        this.width = Integer.valueOf(stats.get(4));
        this.itemName = stats.get(5).replaceAll("_", " ");
        this.itemDescription = stats.get(stats.size()-1);
        this.itemPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\"+stats.get(6).replaceAll("_", " ")+".png");
        this.itemType = itemType;
    }
    
    public String getName() {
        return itemName;
    }
    
    public Image getImage() {
        return itemPicture;
    }
    
    public ItemType getItemType() {
        return itemType;
    }

    public String getItemDescription() {
        return itemDescription;
    }
    
    @Override
    public String toString() {
        return itemDescription;
    }
    
    public enum ItemType {
        ITEMTYPE_DEFAULT,
        ITEMTYPE_SELLABLE
    };
}
