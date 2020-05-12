/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gui;

import game.input.PlayerInput;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author GeoSonicDash
 */
public class Menu {
    private MenuType menuType;
    private int xIndex;
    private int yIndex;
    private boolean isVisible;
    private ArrayList<HashMap<Integer, String>> choices;  
    private Image selectionArrow;
    public Menu(MenuType mT) {
        selectionArrow = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Selection Arrow.png");
        menuType = mT;
        choices = new ArrayList<HashMap<Integer, String>>();        
    }
    
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
    
    public void draw(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.drawString("xIndex: "+xIndex, 1250, 400);
        g2.drawString("yIndex: "+yIndex, 1250, 425);
        g2.drawString("String: "+getChoice(), 1250, 450);
    }
    
    public void leftPress() {
        xIndex--;
        if(xIndex < 0) {
            xIndex = choices.size()-1;
        }
    }
    
    public void rightPress() {
        xIndex++;
        if(xIndex > choices.size()-1) {
            xIndex = 0;
        }
    }
    
    public void upPress() {
        if(PlayerInput.checkForUpPressOnce()) {
            yIndex--;
            if(yIndex < 0) {
                yIndex = choices.get(xIndex).size()-1;
            }
        }        
    }
    
    public void downPress() {
        if(PlayerInput.checkForDownPressOnce()) {
            yIndex++;            
            if(yIndex > choices.get(xIndex).size()-1) {
                yIndex = 0;
            } 
        }        
    }
    
    public void addOption(int xIndex, int yIndex, String option) {
        if(menuType != MenuType.MENUTYPE_VERTICAL && xIndex > choices.size()-1) {
            choices.add(new HashMap<Integer, String>());    
            yIndex = 0;
        }
        else if(menuType == MenuType.MENUTYPE_VERTICAL && choices.isEmpty()) {
            choices.add(new HashMap<Integer, String>());
            xIndex = 0;    
        }
        choices.get(xIndex).put(yIndex, option);
        
    }
    
    public String getChoice() {
        return choices.get(xIndex).get(yIndex);
    }
    
    public int getXIndex() {
        return xIndex;
    }
    
    public int getYIndex() {
        return yIndex;
    }
    
    public Image getSelectionArrow() {
        return selectionArrow;
    }
    
    public ArrayList<HashMap<Integer, String>> getChoices() {
        return choices;
    }
    
    public enum MenuType {
        MENUTYPE_VERTICAL,
        MENUTYPE_HORIZONTAL,
        MENUTYPE_GRID
    }
}
