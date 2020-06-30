/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.playerMenu;

import game.playerMenu.OWMenuManager;
import static game.Launcher.debugStat;
import static game.Launcher.dialogFont;
import static game.Launcher.statusScreen;
import game.gui.Menu;
import game.input.PlayerInput;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import game.gui.GUI;
import game.gui.Menu.MenuType;
import game.items.DefaultItem;
import game.items.DefaultItem.ItemType;
import java.util.LinkedHashMap;
import java.util.Map;

/**Controls the player's over world menu.
 *
 * @author GeoSonicDash
 */
public class InventoryMenu implements GUI {
    private int xRef;
    private int yRef;
    private int yItemMenuOffset;
    private boolean visible;
    private int currentMenuSection;
    private MenuName previous;
    private MenuName currentMenuName;
    private Map<MenuName, Menu> menus;
    private Map<MenuName, Boolean> drawMenuTrigger;
    private Menu currentMenu;
    private DefaultItem item;
    private OWMenuManager owMManager;
    public InventoryMenu(OWMenuManager owMManager) {
        this.visible = false;
        this.currentMenuSection = 0;
        this.owMManager = owMManager;
        this.currentMenuName = MenuName.MENUNAME_INVENTORY_ITEM;
        this.menus = new LinkedHashMap<MenuName, Menu>();
        this.drawMenuTrigger = new LinkedHashMap<MenuName, Boolean>();
        createMenu();       
    }
    
    private void createMenu() {
        menus.put(MenuName.MENUNAME_INVENTORY_ITEM, owMManager.getSonic().getItemInventory().getInventoryMenu());
        drawMenuTrigger.put(MenuName.MENUNAME_INVENTORY_ITEM, true);
        menus.put(MenuName.MENUNAME_INVENTORY_KEY, owMManager.getSonic().getKeyInventory().getInventoryMenu());
        drawMenuTrigger.put(MenuName.MENUNAME_INVENTORY_KEY, false);
        menus.put(MenuName.MENUNAME_ITEM_MENU, new Menu(MenuType.MENUTYPE_VERTICAL));
        drawMenuTrigger.put(MenuName.MENUNAME_ITEM_MENU, false);
        menus.put(MenuName.MENUNAME_ITEM_DESCRIPTION, new Menu(MenuType.MENUTYPE_VERTICAL));
        drawMenuTrigger.put(MenuName.MENUNAME_ITEM_DESCRIPTION, false);
        currentMenu = menus.get(currentMenuName);
    }
    
    /**Controls the logic of the current {@code Menu} that is displayed.
     * 
     */
    @Override
    public void standardGUI() {                
        if(PlayerInput.checkIsPressedOnce(KeyEvent.VK_LEFT)) {
            leftPress();
        } 
        if(PlayerInput.checkIsPressedOnce(KeyEvent.VK_RIGHT)) {
            rightPress();
        }
        if(PlayerInput.checkIsPressedOnce(KeyEvent.VK_UP)) {
            upPress();
        }
        if(PlayerInput.checkIsPressedOnce(KeyEvent.VK_DOWN)) {
            downPress();
        }
        if(PlayerInput.checkIsPressedOnce(KeyEvent.VK_X)) {
            xPress();
        }                     
    }   
    @Override
    public void drawGUI(Graphics2D g2) {
        for(Map.Entry<MenuName, Menu> entry : menus.entrySet()) {
            MenuName key = entry.getKey();
            if(drawMenuTrigger.get(key)) {
                drawCorrectMenu(g2, key);
            }   
        }
        g2.setFont(debugStat);        
        currentMenu.draw(g2);
        g2.drawString("displayedMenu: "+currentMenuName, 900, 475);
        g2.drawString("previous: "+previous, 1250, 500);
        g2.drawString("currentMenuSection: "+currentMenuSection, 1250, 525);
    }
    
    public void drawCorrectMenu(Graphics2D g2, MenuName name) {
        switch(name) {
            case MENUNAME_INVENTORY_ITEM:
                xRef = 500;
                yRef = 100;
                g2.setColor(Color.BLUE);
                g2.fillRect(xRef, yRef, 450, 600);
                g2.setColor(Color.WHITE);            
                g2.setFont(statusScreen); 
                g2.drawString("Items", xRef+175, yRef+50);
                /*If inventory menu has more than 10 items (remember that "Exit" Option is automatically added (so arrayList
                size is actually 11- draw it in groups of 10, else draw all the elements 
                Note!- I had to split it this way since the "Exit" option would be cut off if there was 10 or less items (due
                to it being drawn in groups of 10)*/
                g2.setFont(dialogFont); 
                if(menus.get(name).getChoices().get(0).size() > 11) {                    
                    for(int i = 0; i < menus.get(name).getChoices().size(); i++) {
                        //Need to know how many groups of 10 there are going to be, as well as any leftovers
                        int quotient = menus.get(name).getChoices().get(i).size() / 10;
                        int remainder = menus.get(name).getChoices().get(i).size() % 10;
                        //Drawing a group of 10
                        if(currentMenuSection < quotient) {
                            for(int j = 0+(currentMenuSection*10); j < 0+((currentMenuSection+1)*10); j++) {                        
                                g2.drawString(menus.get(name).getChoices().get(i).get(j), xRef+50, yRef+100+((j-(currentMenuSection*10))*45));
                            }
                            g2.drawImage(menus.get(name).getSelectionArrow(), xRef+50-14, yRef+100+((menus.get(name).getYIndex()-(currentMenuSection*10))*45)-14, 14, 25, null);
                            yItemMenuOffset = (menus.get(name).getYIndex()-(currentMenuSection*10))*45;
                        }
                        else {
                            //Drawing the remaining options that are left (that don't fit in a full group)
                            for(int j = 0+(quotient*10); j < 0+(quotient*10)+remainder; j++) {
                                yItemMenuOffset = (j-(quotient*10))*45;
                                g2.drawString(menus.get(name).getChoices().get(i).get(j), xRef+50, yRef+100+((j-(quotient*10))*45));
                            }
                            g2.drawImage(menus.get(name).getSelectionArrow(), xRef+50-14, yRef+100+((menus.get(name).getYIndex()-(quotient*10))*45)-14, 14, 25, null);
                            yItemMenuOffset = (menus.get(name).getYIndex()-(quotient*10))*45;
                        }
                    }
                }
                else {
                    for(int i = 0; i < menus.get(name).getChoices().size(); i++) {
                        for(int j = 0; j < menus.get(name).getChoices().get(i).size(); j++) {
                            g2.drawString(menus.get(name).getChoices().get(i).get(j), xRef+50, yRef+100+(j*45));
                        }
                    }
                    g2.drawImage(menus.get(name).getSelectionArrow(), xRef+50-14, yRef+100+(menus.get(name).getYIndex()*45)-14, 14, 25, null);
                    yItemMenuOffset = (menus.get(name).getYIndex())*45;
                }              
                break;
            case MENUNAME_INVENTORY_KEY:
                xRef = 500;
                yRef = 100;
                g2.setColor(Color.BLUE);
                g2.fillRect(xRef, yRef, 450, 600);
                g2.setColor(Color.WHITE);   
                g2.setFont(statusScreen); 
                g2.drawString("Key Items", xRef+125, yRef+50);
                g2.setFont(dialogFont);
                for(int i = 0; i < menus.get(name).getChoices().size(); i++) {
                    for(int j = 0; j < menus.get(name).getChoices().get(i).size(); j++) {
                        g2.drawString(menus.get(name).getChoices().get(i).get(j), xRef+50, yRef+100+(j*45));
                    }
                }
                g2.drawImage(menus.get(name).getSelectionArrow(), xRef+50-14, yRef+100+(menus.get(name).getYIndex()*45)-14, 14, 25, null);
                yItemMenuOffset = (menus.get(name).getYIndex())*45;
                break;
            case MENUNAME_ITEM_MENU:
                xRef = 560+g2.getFontMetrics().stringWidth(menus.get(previous).getSelectedChoice());
                yRef = yRef+86+yItemMenuOffset;
                g2.setColor(Color.ORANGE);
                int temp = menus.get(MenuName.MENUNAME_ITEM_MENU).getChoices().get(0).size();
                g2.fillRect(xRef, yRef, 200, temp*45);
                g2.setColor(Color.WHITE);            
                g2.setFont(debugStat);   
                for(int i = 0; i < menus.get(name).getChoices().size(); i++) {
                    for(int j = 0; j < menus.get(name).getChoices().get(i).size(); j++) {
                        g2.drawString(menus.get(name).getChoices().get(i).get(j), xRef+50, yRef+25+(j*50));
                    }
                }
                g2.drawImage(menus.get(name).getSelectionArrow(), xRef+50-14, yRef+25+(menus.get(name).getYIndex()*50)-14, 14, 25, null);
                break;            
            case MENUNAME_ITEM_DESCRIPTION:               
                drawItemDescription(g2);
                break;
            default:
                break;
        }
    }
    
    private void drawItemDescription(Graphics2D g2) {
        xRef = 300;
        yRef = 150;
        g2.setColor(Color.BLUE);
        g2.fillRect(xRef, yRef, 850, 450);
        g2.setColor(Color.WHITE);            
        g2.setFont(dialogFont);
        g2.drawImage(item.getImage(), xRef, yRef, 64, 64, null);
    }
    
    public Menu getRightMenu() {   
        switch(currentMenuName) {
            case MENUNAME_INVENTORY_ITEM:
                item = owMManager.getSonic().getItemInventory().retriveItem(currentMenu.getYIndex());
            break;
            case MENUNAME_INVENTORY_KEY:
                item = owMManager.getSonic().getKeyInventory().retriveItem(currentMenu.getYIndex());
            break;
        }       
        ItemType temp = item.getItemType();
        Menu itemMenu = new Menu(MenuType.MENUTYPE_VERTICAL);
        if(temp == ItemType.ITEMTYPE_DEFAULT) {
            itemMenu.addOption(0, 0, "Description");
        }
        else if(temp == ItemType.ITEMTYPE_SELLABLE) {
            itemMenu.addOption(0, 0, "Use");
            itemMenu.addOption(0, 1, "Description");
        }
        return itemMenu;
    }
    
    @Override
    public boolean isVisible() {
        return visible;
    }    

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }      
    
    public void leftPress() {
        switch(currentMenuName) {
            case MENUNAME_INVENTORY_ITEM: 
                currentMenu.resetMenuPosition();
                drawMenuTrigger.put(MenuName.MENUNAME_INVENTORY_ITEM, false);
                drawMenuTrigger.put(MenuName.MENUNAME_INVENTORY_KEY, true);
                currentMenuName = MenuName.MENUNAME_INVENTORY_KEY;
                currentMenuSection = 0;
                break;
            case MENUNAME_INVENTORY_KEY:
                currentMenu.resetMenuPosition();
                drawMenuTrigger.put(MenuName.MENUNAME_INVENTORY_ITEM, true);
                drawMenuTrigger.put(MenuName.MENUNAME_INVENTORY_KEY, false);
                currentMenuName = MenuName.MENUNAME_INVENTORY_ITEM;
                currentMenuSection = 0;
            default:
                break;
        }
        if(currentMenu != null) {
            currentMenu.leftPress();
        }        
        currentMenu = menus.get(currentMenuName);
    }
    
    public void rightPress() {
        switch(currentMenuName) {
            case MENUNAME_INVENTORY_ITEM: 
                currentMenu.resetMenuPosition();
                drawMenuTrigger.put(MenuName.MENUNAME_INVENTORY_ITEM, false);
                drawMenuTrigger.put(MenuName.MENUNAME_INVENTORY_KEY, true);
                currentMenuName = MenuName.MENUNAME_INVENTORY_KEY;
                currentMenuSection = 0;
                break;
            case MENUNAME_INVENTORY_KEY:
                currentMenu.resetMenuPosition();
                drawMenuTrigger.put(MenuName.MENUNAME_INVENTORY_ITEM, true);
                drawMenuTrigger.put(MenuName.MENUNAME_INVENTORY_KEY, false);
                currentMenuName = MenuName.MENUNAME_INVENTORY_ITEM;
                currentMenuSection = 0;
            default:
                break;
        }
        if(currentMenu != null) {
            currentMenu.rightPress();
        }   
        currentMenu = menus.get(currentMenuName);
    }
    
    public void upPress() {
        if(currentMenuName == MenuName.MENUNAME_INVENTORY_ITEM) {
            if(currentMenu.getYIndex() != 0 && currentMenu.getYIndex() % 10 == 0) {
                currentMenuSection--;
            }            
            else if(currentMenu.getYIndex() == 0 && currentMenu.getYIndex() % 10 == 0) {
                currentMenuSection = currentMenu.getChoices().get(currentMenu.getXIndex()).size() / 10;
            }
        } 
        if(currentMenu != null) {
           currentMenu.upPress(); 
        }        
    }
    
    public void downPress() {   
        if(currentMenu.getYIndex() == currentMenu.getChoices().get(currentMenu.getXIndex()).size()-1) {
            currentMenuSection = 0;
        } 
        if(currentMenu != null) {
            currentMenu.downPress();
        }
        if(currentMenuName == MenuName.MENUNAME_INVENTORY_ITEM) {
            if(currentMenu.getYIndex() != 0 && currentMenu.getYIndex() % 10 == 0) {
                currentMenuSection++;
            }           
        }
    }
    
    public void xPress() {
        if(currentMenu != null) {
            currentMenu.xPress();    
        }       
        if((currentMenuName == MenuName.MENUNAME_INVENTORY_ITEM  || currentMenuName == MenuName.MENUNAME_INVENTORY_KEY)
                && currentMenu.getSelectedChoice().equals("Exit")) {
            currentMenu.resetMenuPosition();
            drawMenuTrigger.put(MenuName.MENUNAME_INVENTORY_ITEM, true);
            drawMenuTrigger.put(MenuName.MENUNAME_INVENTORY_KEY, false);
            currentMenuName = MenuName.MENUNAME_INVENTORY_ITEM;
            currentMenuSection = 0;
            currentMenu = menus.get(currentMenuName);           
            owMManager.switchMenu(0);
        }
        else if((currentMenuName == MenuName.MENUNAME_INVENTORY_ITEM  || currentMenuName == MenuName.MENUNAME_INVENTORY_KEY)
                && !currentMenu.getSelectedChoice().equals("Exit")) {
            Menu itemMenu = getRightMenu();
            drawMenuTrigger.put(MenuName.MENUNAME_ITEM_MENU, true);
            menus.put(MenuName.MENUNAME_ITEM_MENU, itemMenu);
            previous = currentMenuName;
            currentMenuName = MenuName.MENUNAME_ITEM_MENU;            
        }
        else if(currentMenuName == MenuName.MENUNAME_ITEM_MENU && currentMenu.getSelectedChoice().equals("Use")) {     
            int yCurrentItemPosition = menus.get(previous).getYIndex();
            if(previous == MenuName.MENUNAME_INVENTORY_ITEM) {
                 owMManager.getSonic().getItemInventory().removeItem(yCurrentItemPosition);
            }
            else if(previous == MenuName.MENUNAME_INVENTORY_KEY) {
                 owMManager.getSonic().getKeyInventory().removeItem(yCurrentItemPosition);
            }
            currentMenu.resetMenuPosition();         
            drawMenuTrigger.put(MenuName.MENUNAME_INVENTORY_ITEM, true);
            drawMenuTrigger.put(MenuName.MENUNAME_ITEM_MENU, false);
            drawMenuTrigger.put(MenuName.MENUNAME_ITEM_DESCRIPTION, false);
            currentMenuName = previous;
        }
        else if(currentMenuName == MenuName.MENUNAME_ITEM_MENU && currentMenu.getSelectedChoice().equals("Description")) {
            currentMenu.resetMenuPosition();    
            drawMenuTrigger.put(previous, false);
            drawMenuTrigger.put(MenuName.MENUNAME_ITEM_MENU, false);
            drawMenuTrigger.put(MenuName.MENUNAME_ITEM_DESCRIPTION, true);
            currentMenuName = MenuName.MENUNAME_ITEM_DESCRIPTION;
        }
        else if(currentMenuName == MenuName.MENUNAME_ITEM_MENU && currentMenu.getSelectedChoice().equals("Exit")) {
            currentMenu.resetMenuPosition();
            drawMenuTrigger.put(MenuName.MENUNAME_ITEM_MENU, false);
            currentMenuName = previous;
        } 
        else if(currentMenuName == MenuName.MENUNAME_ITEM_DESCRIPTION && currentMenu.getSelectedChoice().equals("Exit")) {
            currentMenu.resetMenuPosition();
            drawMenuTrigger.put(previous, true);
            drawMenuTrigger.put(MenuName.MENUNAME_ITEM_MENU, true);
            drawMenuTrigger.put(MenuName.MENUNAME_ITEM_DESCRIPTION, false);
            currentMenuName = MenuName.MENUNAME_ITEM_MENU;
        }
        currentMenu = menus.get(currentMenuName);
    }
    
    public enum MenuName {
        MENUNAME_INVENTORY_ITEM,
        MENUNAME_INVENTORY_KEY,
        MENUNAME_ITEM_MENU,
        MENUNAME_ITEM_DESCRIPTION
    };
}
