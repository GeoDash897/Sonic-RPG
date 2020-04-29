/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import static game.GameLoop.dialog;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author GeoSonicDash
 */
public class Dialog {
    private final static int CHAR_LIMIT = 80;    
    public static void drawDialog(Graphics2D g2, int currentSection, String description) {
        String[] splitDescription = initDescriptionArray(description);
        if(currentSection >= 0) {
            g2.setFont(dialog);
            g2.setColor(Color.BLUE);
            g2.fillRect(0,0,2000,150);
            g2.setColor(Color.WHITE);
            int temp1 = currentSection+1;
            int temp2 = currentSection+2;
            boolean line1, line2, line3;
            if(currentSection < splitDescription.length) {
                line1 = checkFirstLetter(splitDescription,currentSection);
                if(!line1) {
                    g2.drawString(splitDescription[currentSection], 200, 50);    
                }
                else if(line1) {
                    g2.drawString(splitDescription[currentSection], 185, 50);  
                } 
                if(temp1 < splitDescription.length) {
                    line2 = checkFirstLetter(splitDescription,temp1);
                    if(!line2) {
                        g2.drawString(splitDescription[temp1], 200, 80);    
                    }
                    else if(line2) {
                        g2.drawString(splitDescription[temp1], 185, 80);  
                    } 
                    if(temp2 < splitDescription.length) {
                        line3 = checkFirstLetter(splitDescription,temp2);
                        if(!line3) {
                            g2.drawString(splitDescription[temp2], 200, 110);    
                        }
                        else if(line3) {
                            g2.drawString(splitDescription[temp2], 185, 110);  
                        }   
                    }
                }
            }    
        }
    }
    public static String[] initDescriptionArray(String description) {
        ArrayList<String> list = new ArrayList<String>();
        int fullLine = description.length() / 80;
        int leftOver = description.length() % 80;
        boolean stopAdd = false;
        
        if(!stopAdd) {
            for(int i = 0; i < fullLine;i++) {
                list.add(description.substring(80*i,80*(i+1)));
            }
            
            if(leftOver > 0) {
                list.add(description.substring(fullLine*80, description.length()));                 
            }  
        }
        stopAdd = true;
        
        return list.toArray(new String[list.size()]);
    }
    public static boolean checkFirstLetter(String [] splitDescription, int temp) {
        boolean checkForSpace = false;
        String firstLetter = String.valueOf(splitDescription[temp].charAt(0));
        if(!firstLetter.equals(" ")) {
                checkForSpace = false;  
            }
            else if(firstLetter.equals(" ")) {
                checkForSpace = true;
        }
        return checkForSpace;
    }
}
