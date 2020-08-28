/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.playerMenu;

import static game.Launcher.debugStat;
import static game.Launcher.statusScreen;
import game.gui.Menu;
import game.gui.Menu.MenuType;
import game.input.PlayerInput;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import game.gui.GUI;

/**Controls the player's over world menu.
 *
 * @author GeoSonicDash
 */
public class PlayerMenu implements GUI {
    private int xRef;
    private int yRef;
    private int statusPosition;
    private boolean visible;
    private int currentMenuIndex;
    private ArrayList<Menu> menus;
    private Menu currentMenu;
    private OWMenuManager owMManager;
    public PlayerMenu(OWMenuManager owMManager) {
        this.visible = false;
        this.currentMenuIndex = 0;
        this.statusPosition = 0;
        this.owMManager = owMManager;
        this.menus = new ArrayList<Menu>(3);
        createMenu();       
    }
    
    private void createMenu() {
        menus.add(0, new Menu(MenuType.MENUTYPE_VERTICAL));
        menus.get(0).addOption(0, 0, "Status");
        menus.get(0).addOption(0, 1, "Inventory"); 
        menus.add(1, new Menu(MenuType.MENUTYPE_VERTICAL));   
        currentMenu = menus.get(currentMenuIndex);
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
                }
                g2.drawImage(currentMenu.getSelectionArrow(), xRef+50-14, yRef+100+(currentMenu.getYIndex()*100)-14, 14, 25, null);
                break;
            case 1:
                xRef = 250;
                yRef = 100;
                g2.setColor(Color.BLUE);
                g2.fillRect(xRef, yRef, 1050, 600);
                g2.setFont(statusScreen);
                g2.setColor(Color.WHITE);
                owMManager.getPlayerManager().getCharacter(statusPosition).drawStatusStats(g2, xRef, yRef);                
                g2.setFont(debugStat);                
                g2.drawString(currentMenu.getChoices().get(0).get(0), xRef+50, yRef+575);
                g2.drawImage(currentMenu.getSelectionArrow(), xRef+50-14, yRef+575+(currentMenu.getYIndex()*100)-14, 14, 25, null);                
                break;                
            default:
                break;
        }
        g2.setFont(debugStat);
        g2.drawString("displayedMenu: "+currentMenuIndex, 1250, 475);
        currentMenu.draw(g2);
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
        statusPosition--;
        if(statusPosition  == -1) {
            statusPosition = owMManager.getPlayerManager().getPartySize()-1;
        }
        else if(statusPosition == owMManager.getPlayerManager().getPartySize()-1) {
            statusPosition = 0;
        }
            
        if(currentMenu != null) {
           currentMenu.leftPress(); 
        }        
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
            currentMenu.xPress();    
        }       
        if(currentMenuIndex == 0 && currentMenu.getSelectedChoice().equals("Status")) {           
            currentMenu.resetMenuPosition();
            currentMenuIndex++;            
        }
        else if(currentMenuIndex == 0 && currentMenu.getSelectedChoice().equals("Inventory")) {           
            currentMenu.resetMenuPosition();
            owMManager.switchMenu(1);
        } 
        if(currentMenuIndex == 0 && currentMenu.getSelectedChoice().equals("Exit")) {
            statusPosition = 0;
            currentMenu.resetMenuPosition();              
            owMManager.exitMenu();
        } 
        else if(currentMenuIndex == 1 && currentMenu.getSelectedChoice().equals("Exit")) {
            currentMenu.resetMenuPosition();
            currentMenuIndex--;
        }
        currentMenu = menus.get(currentMenuIndex);
    }
}
