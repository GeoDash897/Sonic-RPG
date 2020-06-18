/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.items;

import java.awt.Image;

/**
 *
 * @author GeoSonicDash
 */
public class DefaultItem {
    private int xRef;
    private int yRef;
    private int length;
    private int width;
    private int itemBaseCost;
    private int itemBaseSell;
    private String itemName;
    private String itemDescription;
    private Image itemPicture;
    public DefaultItem() {
        
    }
    public void createItem(int xRef, int yRef, int length, int width, int itemBaseCost, int itemBaseSell, String itemName, String itemDescription
    ,Image itemPicture) {
        this.xRef = xRef;
        this.yRef = yRef;
        this.length = length;
        this.width = width;
        this.itemBaseCost = itemBaseCost;
        this.itemBaseSell = itemBaseSell;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPicture = itemPicture;
    }
}
