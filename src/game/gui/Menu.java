/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

/**Controls very basic menu functions- drawing, navigating, and selecting options 
 * (which are Strings).
 * @author GeoSonicDash
 */
public class Menu {
    private MenuType menuType;
    private int xIndex;
    private int yIndex;
    private ArrayList<ArrayList<String>> choices;  
    private Image selectionArrow;
    private String selectedOption;
    public Menu(MenuType mT) {
        selectionArrow = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Selection Arrow.png");
        menuType = mT;
        choices = new ArrayList<ArrayList<String>>();  
        choices.add(new ArrayList<String>());
        choices.get(0).add(0, "Exit");
        selectedOption = "";
    }   
    
    public void draw(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.drawString("xIndex: "+xIndex, 1250, 400);
        g2.drawString("yIndex: "+yIndex, 1250, 425);
        g2.drawString("Selected Choice: "+selectedOption, 1250, 450);
        g2.drawString("Highlighted Choice: "+choices.get(xIndex).get(yIndex), 1250, 475);
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
    
    public void xPress() {
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
            choices.add(new ArrayList<String>());    
            yIndex = 0;
        }
        else if(menuType == MenuType.MENUTYPE_VERTICAL) {
            xIndex = 0;
            if(yIndex < 0) {
                yIndex = 0;
            }
        }
        try {
            choices.get(xIndex).set(yIndex, option);
        }
        catch(IndexOutOfBoundsException  ex) {
            choices.get(xIndex).add(yIndex, option);
        }
    }
    
    public void swapOption(int originalX, int originalY, int newX, int newY) {
        if(menuType == MenuType.MENUTYPE_VERTICAL && choices.get(originalX).get(originalY) != null) {
            if(newX == 0 && newY >= 0 && newY <= choices.get(originalX).size()) {
                String optionAtOldSpot = choices.get(originalX).get(originalY);
                String optionAtNewSpot = choices.get(newX).get(newY);
                choices.get(newX).remove(newY);
                choices.get(newX).add(newY, optionAtOldSpot);
                choices.get(originalX).remove(originalY);
                choices.get(originalX).add(originalY, optionAtNewSpot);
            }
        }
    }
    
    public String getOption(int xIndex, int yIndex) {
        return choices.get(xIndex).get(yIndex);
    }
    
    public void removeOption(int xIndex, int yIndex) {
        choices.get(xIndex).remove(yIndex);
    }
    
    public String getSelectedChoice() {
        return selectedOption;
    }
    
    public void setSelectedChoice(String temp) {
        selectedOption = temp;
    }
    
    public String getHighlightedChoice() {
        return choices.get(xIndex).get(yIndex);
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
    
    public ArrayList<ArrayList<String>> getChoices() {
        return choices;
    }
    
    public enum MenuType {
        MENUTYPE_VERTICAL,
        MENUTYPE_HORIZONTAL,
        MENUTYPE_GRID
    }
    
    @Override
    public String toString() {
        for(int i = 0; i < choices.get(0).size(); i++) {
            System.out.println(choices.get(0).get(i));
        }
        return "";
    }
}
