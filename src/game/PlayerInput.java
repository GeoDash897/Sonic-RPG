/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author GeoSonicDash
 */
public class PlayerInput implements KeyListener {
    private static boolean leftPress = false;
    private static boolean rightPress = false;
    private static boolean downPress = false;
    private static boolean zPress = false;
    private static boolean xPress = false;
    private static int zPressTimer = 0;
    private static int xPressTimer = 0;
    private static boolean allowInput = true;
    private static boolean cutscene = false;
    public void standard(Graphics2D g2) {
        g2.setColor(Color.cyan);
        g2.drawString("leftPress: "+leftPress,1000,500);
        g2.drawString("rightPress: "+rightPress,1000,525);
        g2.drawString("downPress: "+downPress,1000,550);
        g2.drawString("zPress: "+zPress,1000,575);
        g2.drawString("zPressTimer: "+zPressTimer,1000,600);
        g2.drawString("xPress: "+xPress,1000,625);
        g2.drawString("xPressTimer: "+xPressTimer,1000,650);
        if(zPress) {
            zPressTimer++;
        }
        if(xPress) {
          xPressTimer++;  
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        
    }
    public static boolean getLeftPress() {
        return leftPress;
    }
    public static boolean getRightPress() {
        return rightPress;
    }
    public static boolean getUpPress() {
        return false;
    }
    public static boolean getDownPress() {
        return downPress;
    }
    public static boolean getZPress() {
        return zPress;
    }
    public static boolean getXPress() {
        return xPress;
    }
    public static boolean checkForZPressOnce() {
        boolean check = false;
        if(zPress && zPressTimer <= 1) {
            check = true;
        }
        return check;
    }
    public static boolean checkForXPressOnce() {
        boolean check = false;
        if(xPress && xPressTimer <= 1) {
            check = true;
        }
        return check;
    }
    public static void setLeftPress(boolean set) {
        leftPress = set;
    }
    public static void setRightPress(boolean set) {
        rightPress = set;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if(allowInput && e.getKeyCode() == e.VK_RIGHT) {
            rightPress = true;
            leftPress = false;
        }
        if(allowInput && e.getKeyCode() == e.VK_LEFT) {  
            leftPress = true;
            rightPress = false;
        }          
        if(allowInput && e.getKeyCode() == e.VK_UP) {
            
        }
        if(allowInput && e.getKeyCode() == e.VK_DOWN) {
            downPress = true;
        }
        if(allowInput && e.getKeyCode() == e.VK_Z) {
            zPress = true;
        }
        if(allowInput && e.getKeyCode() == e.VK_X) {
            xPress = true;
        }
        if(allowInput && e.getKeyCode() == e.VK_ESCAPE) {
            Game.setDebug();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(allowInput && e.getKeyCode() == e.VK_RIGHT) {
            rightPress = false;
        }
        if(allowInput && e.getKeyCode() == e.VK_LEFT) {
            leftPress = false;
        }
        if(allowInput && e.getKeyCode() == e.VK_DOWN) {
            downPress = false;
        }
        if(allowInput && e.getKeyCode() == e.VK_Z) {              
            zPress = false;  
            zPressTimer = 0;
        }
        if(allowInput && e.getKeyCode() == e.VK_X) {            
            xPress = false;
            xPressTimer = 0;
        }
    }
    
}
