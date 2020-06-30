/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gui;

import java.awt.Graphics2D;

/**
 *
 * @author GeoSonicDash
 */
public interface GUI {
    public void standardGUI();
    void drawGUI(Graphics2D g2);   
    public boolean isVisible();
    public void setVisible(boolean isVisible);
}
