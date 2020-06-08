/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gui;

import static game.Launcher.dialogFont;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

/**
 *
 * @author GeoSonicDash
 */
public class Dialog {
    private int length;
    private int width;
    private String speakerName;
    private String dialog;
    private Image speakerPic;
    public Dialog(String speakerName, String dialog) {
        this.speakerName = speakerName;
        this.dialog = dialog;
        getSpeakerPic();
    }
    
    private void getSpeakerPic() {
        switch(speakerName) {
            case "SONIC_NORMAL":
                speakerPic = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Face Normal.png");
                length = 25;
                width = 19;
                break;
            case "TAILS":
                length = 25;
                width = 19;
                break;
            case "SIGN":
                length = 25;
                width = 19;
                break;
            case "SKELETON":
                length = 25;
                width = 19;
                break;
            default:
                break;    
        }
        /*This removes the _EMOTION part of the character name, so the only part that is drawn is the character's
        actual name*/
        if(speakerName.contains("_")) {
            speakerName = speakerName.substring(0,speakerName.indexOf("_"));
        }    
    }
    
    public void draw(Graphics2D g2) {
        g2.setFont(dialogFont);
        g2.setColor(Color.BLUE);
        g2.fillRect(0,0,2000,150);
        g2.setColor(Color.WHITE);
        g2.drawString(speakerName, 25, 135); 
        drawText(g2);
        g2.drawImage(speakerPic, 25, 25, length*4, width*4, null);
    }
    /**
     * This makes sure that the text of the dialog are formatted and displayed correctly.
     * (Occurs when the first character of a line of dialog is a space).
     * @param g2 
     */
    private void drawText(Graphics2D g2) {   
        boolean firstSpace = false;
        boolean secondSpace = false;
        if(dialog.length() == 160) {
            firstSpace = checkFirstLetter(dialog.substring(0, 80));
            secondSpace = checkFirstLetter(dialog.substring(80, 160));
            if(firstSpace) {
                g2.drawString(dialog.substring(1, 80), 50+(length*4)+35, 50); 
            }
            else {
                g2.drawString(dialog.substring(0, 80), 50+(length*4)+35, 50); 
            }
            if(secondSpace) {
                g2.drawString(dialog.substring(81, 160), 50+(length*4)+35, 100); 
            }
            else {
                g2.drawString(dialog.substring(80, 160), 50+(length*4)+35, 100); 
            }  
        }   
        else if (dialog.length() > 80 && dialog.length() < 160) {
            if(firstSpace) {
                g2.drawString(dialog.substring(1, 80), 50+(length*4)+35, 50); 
            }
            else {
                g2.drawString(dialog.substring(0, 80), 50+(length*4)+35, 50); 
            }
            if(secondSpace) {
                g2.drawString(dialog.substring(81), 50+(length*4)+35, 100); 
            }
            else {
                g2.drawString(dialog.substring(80), 50+(length*4)+35, 100); 
            }
        }
        else if(dialog.length() <= 80) {
            if(firstSpace) {
                g2.drawString(dialog.substring(1), 50+(length*4)+35, 50); 
            }
            else {
                g2.drawString(dialog.substring(0), 50+(length*4)+35, 50); 
            }
        }
    }
    
    private boolean checkFirstLetter(String check) {
        boolean checkForSpace = false;
        String firstLetter = String.valueOf(check.charAt(0));
        if(!firstLetter.equals(" ")) {
                checkForSpace = false;  
            }
            else if(firstLetter.equals(" ")) {
                checkForSpace = true;
        }
        return checkForSpace;
    }
    
    @Override
    public String toString() {
        return "speakerName - "+speakerName+", dialog - "+dialog;
    }
}
