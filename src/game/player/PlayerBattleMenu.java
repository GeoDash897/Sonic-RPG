/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.player;

import game.gui.GUI;
import game.gui.Menu;
import game.gui.Menu.MenuType;
import game.input.PlayerInput;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author GeoSonicDash
 */
public class PlayerBattleMenu implements GUI {
    private int xRef;
    private int yRef;
    private boolean visible;
    private int currentMenuSection;
    
    private Image battleBlocks;
    
    private BattleMenu previous;
    private BattleMenu currentMenuName;
    private Map<BattleMenu, Menu> menus;
    private Map<BattleMenu, Boolean> drawMenuTrigger;
    private Menu currentMenu;
    
    public PlayerBattleMenu() {
        this.visible = true;
        this.currentMenuName = BattleMenu.BATTLEMENU_MAIN;
        this.menus = new LinkedHashMap<BattleMenu, Menu>();
        this.drawMenuTrigger = new LinkedHashMap<BattleMenu, Boolean>();
        this.battleBlocks = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Battle Blocks_1.png");
        addMenus();
    }
    
    private void addMenus() {
        menus.put(BattleMenu.BATTLEMENU_MAIN, new Menu(MenuType.MENUTYPE_HORIZONTAL));
        menus.get(BattleMenu.BATTLEMENU_MAIN).addOption(0, 0, "Option1");
        menus.get(BattleMenu.BATTLEMENU_MAIN).addOption(1, 0, "Option2");
        menus.get(BattleMenu.BATTLEMENU_MAIN).addOption(2, 0, "Option3");
        menus.get(BattleMenu.BATTLEMENU_MAIN).addOption(3, 0, "Option4");
        drawMenuTrigger.put(BattleMenu.BATTLEMENU_MAIN, true);
        menus.put(BattleMenu.BATTLEMENU_SOLO, new Menu(MenuType.MENUTYPE_VERTICAL));
        menus.get(BattleMenu.BATTLEMENU_SOLO).addOption(0, 0, "Option1");
        menus.get(BattleMenu.BATTLEMENU_SOLO).addOption(0, 1, "Exit");
        drawMenuTrigger.put(BattleMenu.BATTLEMENU_SOLO, false);
        currentMenu = menus.get(currentMenuName);
    }
    
    @Override
    public void standardGUI() {
        if(PlayerInput.checkIsPressedOnce(KeyEvent.VK_LEFT)) {
            leftPress();
        }
        if(PlayerInput.checkIsPressedOnce(KeyEvent.VK_RIGHT)) {
            rightPress();
        }
        if(PlayerInput.checkIsPressedOnce(KeyEvent.VK_UP)) {
            if(currentMenu != null) {
                currentMenu.upPress();
            }
        }
        if(PlayerInput.checkIsPressedOnce(KeyEvent.VK_DOWN)) {
            if(currentMenu != null) {
                currentMenu.downPress();
            }
        }
        if(PlayerInput.checkIsJustReleased(KeyEvent.VK_X)) {
            xPress();
        }
    }

    @Override
    public void drawGUI(Graphics2D g2) {   
        for(Map.Entry<BattleMenu, Menu> entry : menus.entrySet()) {
            BattleMenu temp = entry.getKey();
            if(drawMenuTrigger.get(temp)) {
                drawSpecificMenu(g2, temp);
            }
        }
        currentMenu.draw(g2);
    }

    public void drawSpecificMenu(Graphics2D g2, BattleMenu name) {
        switch(name) {
            case BATTLEMENU_MAIN:
                battleBlocks = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Battle Blocks_"+(currentMenu.getXIndex()+1)+".png");
                g2.drawImage(battleBlocks, 200, 200, 304, 176, null);
                break;
            case BATTLEMENU_SOLO:
                if(currentMenu.getHighlightedChoice().equals("Exit")) {
                    battleBlocks = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Ok Block_2.png");
                    g2.drawImage(battleBlocks, 314, 267, 81, 100, null);
                }
                else {
                    battleBlocks = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Ok Block_1.png");
                    g2.drawImage(battleBlocks, 314, 277, 81, 100, null);
                }                              
            default:
                break;
        }
    }
    
    public void leftPress() {
        if(currentMenu != null) {
            currentMenu.leftPress();
        }
    }
    
    public void rightPress() {
        if(currentMenu != null) {
            currentMenu.rightPress();
        }
    }
    
    public void xPress() {
        if(currentMenu != null) {
            currentMenu.xPress();
        } 
        switch(currentMenuName) {
            case BATTLEMENU_MAIN:
                if(currentMenu.getSelectedChoice().equals("Option1")) {
                    drawMenuTrigger.put(BattleMenu.BATTLEMENU_MAIN, false);
                    currentMenuName = BattleMenu.BATTLEMENU_SOLO;                  
                    drawMenuTrigger.put(BattleMenu.BATTLEMENU_SOLO, true);
                }
                break;
            case BATTLEMENU_SOLO:
                if(currentMenu.getSelectedChoice().equals("Exit")) {
                    drawMenuTrigger.put(BattleMenu.BATTLEMENU_SOLO, false);
                    currentMenuName = BattleMenu.BATTLEMENU_MAIN;                  
                    drawMenuTrigger.put(BattleMenu.BATTLEMENU_MAIN, true);
                    currentMenu.resetMenuPosition();
                }
                break;
            default:                
                break;
        }
        currentMenu = menus.get(currentMenuName);
    }
    
    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setVisible(boolean isVisible) {
        visible = isVisible;
    }
    
    public enum BattleMenu {
        BATTLEMENU_MAIN,
        BATTLEMENU_SOLO,
        BATTLEMENU_SELECTENEMY
    }
}
