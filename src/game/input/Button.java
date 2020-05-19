/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.input;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author GeoSonicDash
 */
public class Button {
    private boolean buttonPressed;
    private int buttonPressTimer;
    private int buttonReleaseTimer;
    private int keyInt;
    public Button(int keyInt) {
        this.keyInt = keyInt;
        this.buttonReleaseTimer = 5;
    }
    public void standard() { 
        if(buttonPressed) {
            if(buttonReleaseTimer > 0) {
                buttonReleaseTimer = 0;
            }
            if(buttonPressTimer < 5) {
                buttonPressTimer++;    
            }            
        }
        else if(!buttonPressed) {
            if(buttonPressTimer > 0){
                buttonPressTimer = 0;
            } 
            if(buttonReleaseTimer < 5) {
                buttonReleaseTimer++;    
            }            
        }              
        
    }
       
    public void draw(Graphics2D g2) {
        g2.setColor(Color.cyan);
        g2.drawString("keyInt: "+keyInt,1000,475);
        g2.drawString("buttonPressed: "+buttonPressed,1000,500);
        g2.drawString("buttonPressTimer: "+buttonPressTimer,1000,525);
        g2.drawString("buttonReleaseTimer: "+buttonReleaseTimer,1000,550);           
    }
    
    public boolean getButtonPressed() {
        return buttonPressed;
    }
    
    public boolean isPressed() {
        return buttonPressed;
    }
    
    public void setPressed(boolean temp) {
        buttonPressed = temp;
    }
       
    public boolean isPressedOnce() {
        boolean check = false;
        if(buttonPressed && buttonPressTimer == 1) {
            check = true;
        }
        return check;
    }
    
    public boolean isJustReleased() {
        boolean check = false;
        if(!buttonPressed && buttonReleaseTimer == 1) {
            check = true;
        }
        return check;
    }
}
