/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gui;

import game.gui.Menu.MenuType;
import game.input.PlayerInput;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author GeoSonicDash
 */
public class PlayerMenu extends Menu {
    public PlayerMenu(MenuType mT) {
        super(mT);
        createMenu();
    }
    
    public void createMenu() {
        super.addOption(0, 0, "Test");
        super.addOption(0, 1, "Test2");
    }
    @Override
    public void standard() {
        if(PlayerInput.getLeftPress()) {
            leftPress();
        } 
        if(PlayerInput.getRightPress()) {
            rightPress();
        }
        if(PlayerInput.getUpPress()) {
            upPress();
        }
        if(PlayerInput.getDownPress()) {
            downPress();
        }
    }
    
    @Override
    public void draw(Graphics2D g2) {         
        g2.setColor(Color.BLUE);
        g2.fillRect(1250, 0, 350, 400);
        //g2.fillRect(1000, 0, 500, 500);
        g2.setColor(Color.WHITE);
        for(int i = 0; i < super.getChoices().size(); i++) {
            for(int j = 0; j < super.getChoices().get(i).size(); j++) {
                g2.drawString(super.getChoices().get(i).get(j), 1300, 100+(j*100));
            }
        }
        //Length: 56
        //Width: 100
        g2.drawImage(super.getSelectionArrow(), 1300-14, 100+(super.getYIndex()*100)-14, 14, 25, null);
        super.draw(g2);
    }
    
    @Override
    public void leftPress() {
        super.leftPress();
    }
    
    @Override
    public void rightPress() {
        super.rightPress();
    }
    
    @Override
    public void upPress() {
        super.upPress();
    }
    
    @Override
    public void downPress() {
        super.downPress();
    }
}
