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
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**Controls very basic menu functions- drawing, navigating, and selecting options 
 * (which are Strings).
 * @author GeoSonicDash
 */
public class Menu {
    private MenuType menuType;
    private int xIndex;
    private int yIndex;
    private ArrayList<HashMap<Integer, String>> choices;  
    private Image selectionArrow;
    private String selectedOption;
    public Menu(MenuType mT) {
        selectionArrow = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Selection Arrow.png");
        menuType = mT;
        choices = new ArrayList<HashMap<Integer, String>>();  
        selectedOption = "";
    }   
    
    public void draw(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.drawString("xIndex: "+xIndex, 1250, 400);
        g2.drawString("yIndex: "+yIndex, 1250, 425);
        g2.drawString("String: "+selectedOption, 1250, 450);
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
        yIndex--;
        if(yIndex < 0) {
            yIndex = choices.get(xIndex).size()-1;
        }
    }        
    
    public void downPress() {
        yIndex++;            
        if(yIndex > choices.get(xIndex).size()-1) {
            yIndex = 0;
        } 
    }        
    
    public void zPress() {
        selectedOption = choices.get(xIndex).get(yIndex);
    }
    
    /**Adds option to a specific index of the menu.
     * The index of the option can change depending on what kind of {@code MenuType} the 
     * menu is (you can't have an xIndex greater than 0 for a vertical Menu).
     * @param xIndex the xIndex of the menu option.
     * @param yIndex the yIndex of the menu option.
     * @param option the menu option itself.
     */
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
    
    public void setSelectedChoice(String temp) {
        selectedOption = temp;
    }
    
    public String getSelectedChoice() {
        return selectedOption;
    }
    
    public int getXIndex() {
        return xIndex;
    }
    
    public int getYIndex() {
        return yIndex;
    }
   
    public void resetMenuPosition() {
        xIndex = 0;
        yIndex = 0;
        selectedOption = "";
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
