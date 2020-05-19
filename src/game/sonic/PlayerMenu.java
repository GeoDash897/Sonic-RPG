/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.sonic;

import static game.Launcher.debugStat;
import static game.Launcher.statusScreen;
import game.gui.Menu;
import game.gui.Menu.MenuType;
import game.input.PlayerInput;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 *
 * @author GeoSonicDash
 */
public class PlayerMenu {
    private static int xRef;
    private static int yRef;
    private static boolean visible;
    private static int currentMenuIndex;
    private static ArrayList<Menu> menus;
    private static Menu currentMenu;
    public PlayerMenu() {
        visible = false;
        currentMenuIndex = 0;
        menus = new ArrayList<Menu>(2);
        createMenu();       
    }
    
    private void createMenu() {
        menus.add(0, new Menu(MenuType.MENUTYPE_VERTICAL));
        menus.get(0).addOption(0, 0, "Hello This is A Test!");
        menus.get(0).addOption(0, 1, "Exit");
        menus.add(1, new Menu(MenuType.MENUTYPE_VERTICAL));
        menus.get(1).addOption(0, 0, "Exit");
    }
    public void standard() {        
        if(visible) {           
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
            currentMenu = menus.get(currentMenuIndex);
        }        
    }
    
    public void draw(Graphics2D g2) {
        switch (currentMenuIndex) {
            case 0:
                xRef = 1250;
                yRef = 0;
                g2.setColor(Color.BLUE);
                g2.fillRect(xRef, yRef, 350, 400);
                g2.setColor(Color.WHITE);
                for(int i = 0; i < currentMenu.getChoices().size(); i++) {
                    for(int j = 0; j < currentMenu.getChoices().get(i).size(); j++) {
                        g2.drawString(currentMenu.getChoices().get(i).get(j), xRef+50, yRef+100+(j*100));
                    }
                }   g2.drawImage(currentMenu.getSelectionArrow(), xRef+50-14, yRef+100+(currentMenu.getYIndex()*100)-14, 14, 25, null);
                break;
            case 1:
                xRef = 250;
                yRef = 100;
                g2.setColor(Color.BLUE);
                g2.fillRect(xRef, yRef, 1050, 600);
                g2.setFont(statusScreen);
                g2.setColor(Color.WHITE);
                g2.drawString("Status", xRef+410, yRef+50);
                g2.drawString("Level", xRef+30, yRef+150);
                g2.drawString("EXP", xRef+30, yRef+250);
                g2.drawString("Money", xRef+30, yRef+350);
                g2.drawString("ExpToLU", xRef+600, yRef+150);
                g2.setFont(debugStat);                
                g2.drawString(currentMenu.getChoices().get(0).get(0), xRef+50, yRef+525);
                g2.drawImage(currentMenu.getSelectionArrow(), xRef+50-14, yRef+525+(currentMenu.getYIndex()*100)-14, 14, 25, null);
                Image temp = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Victory Pose B.gif");
                g2.drawImage(temp,xRef+200,yRef-30,576,576,null); 
                break;
            default:
                break;
        }
        g2.setFont(debugStat);
        g2.drawString("displayedMenu: "+currentMenuIndex, 1250, 475);
        currentMenu.draw(g2);
    }
    
    public static boolean isVisible() {
        return visible;
    }
    
    public static void setVisible(boolean temp) {
        visible = temp;
    }
    
    public void leftPress() {
        if(currentMenu != null)
        currentMenu.leftPress();
    }
    
    public void rightPress() {
        if(currentMenu != null)
        currentMenu.rightPress();
    }
    
    public void upPress() {
        if(currentMenu != null)
        currentMenu.upPress();
    }
    
    public void downPress() {
        if(currentMenu != null)
        currentMenu.downPress();
    }
    
    public void xPress() {
        if(currentMenu != null) {
            currentMenu.zPress();    
        }       
        if(currentMenuIndex == 0 && currentMenu.getSelectedChoice().equals("Hello This is A Test!")) {           
            currentMenu.resetMenuPosition();
            currentMenuIndex++;            
        }
        if(currentMenuIndex != 0 && currentMenu.getSelectedChoice().equals("Exit")) {
            currentMenu.resetMenuPosition();
            currentMenuIndex--;
        }
        else if(currentMenuIndex == 0 && currentMenu.getSelectedChoice().equals("Exit")) {
            currentMenu.resetMenuPosition();
            visible = false;
        }
    }
}
