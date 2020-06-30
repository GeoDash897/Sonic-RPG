/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.items;

import game.gui.Menu;
import game.gui.Menu.MenuType;
import java.util.ArrayList;

/**
 * 
 * @author GeoSonicDash
 */
public class Inventory {//This controls inventory
    private int inventoryLimit;
    private ArrayList<DefaultItem> inventory;
    private Menu inventoryMenu;
    public Inventory(int inLimit) {
        inventoryLimit = inLimit;
        inventory = new ArrayList<DefaultItem>(inventoryLimit); 
        inventoryMenu = new Menu(MenuType.MENUTYPE_VERTICAL);
        addStuff();
    }
    
    private void addStuff() {
        addItem(LoadItems.loadItem("POTION"));
        addItem(LoadItems.loadItem("BIG_POTION"));
        addItem(LoadItems.loadItem("BIG_POTION"));
    }
    
    public void addItem(DefaultItem add) {
        if(inventory.size() < inventoryLimit) {
            inventory.add(add);
            inventoryMenu.addOption(0, inventory.size()-1, add.getName());           
        }
    }
    
    public void removeItem(int position) {
        inventory.remove(position);
        inventoryMenu.removeOption(0, position);
    }
    
    public DefaultItem retriveItem(int position) {
        return inventory.get(position);
    }
    
    public Menu getInventoryMenu() {
        return inventoryMenu;
    }
}
