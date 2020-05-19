/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.sonic;
import game.items.*;
import java.awt.Graphics2D;
import java.util.ArrayList;
/*
    Author: GeoDash897  Date:10/5/19    Updated:10/5/19
*/
public class Inventory {//This controls Sonic's inventory
    private static ArrayList<DefaultItem> inventory = new ArrayList<DefaultItem>();
    private static int inventoryLimit = 10;
    public void addItem(DefaultItem defaultItem) {
        if(inventory.size() < inventoryLimit) {
            inventory.add(defaultItem); 
        }  
    }
    public void removeItem(int position) {
        inventory.remove(position);
    }
    public void drawItem(int position, Graphics2D g2) { 
        
    }
}
