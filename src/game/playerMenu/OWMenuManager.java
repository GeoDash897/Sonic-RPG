/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.playerMenu;

import game.gui.GUI;
import game.player.PlayerManager;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author GeoSonicDash
 */
public class OWMenuManager implements GUI {

    private int index;
    private boolean isVisible;
    private ArrayList<GUI> guis;
    private PlayerManager manager;
    public OWMenuManager(PlayerManager manager) {
        this.manager = manager;
        this.index = 0;
        guis = new ArrayList<GUI>(2);
        guis.add(new PlayerMenu(this));
        guis.add(new InventoryMenu(this));
    }
    @Override
    public void standardGUI() {
        if(isVisible) {
            if(manager.getCharacter(0).getOWARAllowInput()) {
                manager.getCharacter(0).setOWARAllowInput(false);
            }
            guis.get(index).standardGUI();
        }        
    }

    @Override
    public void drawGUI(Graphics2D g2) {
        guis.get(index).drawGUI(g2);
    }

    public void switchMenu(int newMenu) {     
        index = newMenu;
    }
    
    public void exitMenu() {
        isVisible = false;
        index = 0;
        manager.getCharacter(0).setOWARAllowInput(true);
    }
    
    public PlayerManager getPlayerManager() {
        return manager;
    }
    
    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
    
}
