/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.playerMenu;

import game.gui.GUI;
import game.sonic.Sonic;
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
    private Sonic sonic;
    public OWMenuManager(Sonic sonic) {
        this.sonic = sonic;
        this.index = 0;
        guis = new ArrayList<GUI>(2);
        guis.add(new PlayerMenu(this));
        guis.add(new InventoryMenu(this));
    }
    @Override
    public void standardGUI() {
        if(isVisible) {
            if(Sonic.getOWARAllowInput()) {
                Sonic.setOWARAllowInput(false);
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
        Sonic.setOWARAllowInput(true);
    }
    
    @Override
    public boolean isVisible() {
        return isVisible;
    }
    
    public Sonic getSonic() {
        return sonic;
    }

    @Override
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }
    
}
