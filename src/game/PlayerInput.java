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

/**This class controls the input of the player.
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
    
    /**
     * Runs the counters for zPress and xPress (checks how long
     * those keys are pressed for)
     * @param g2 Need Graphics2D object to display variables.
     */
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
    
    /** @return 
     * <ul>
     * <li>{@code false}- the left key is not pressed.
     * <li>{@code true}- the left key is pressed. 
     * </ul>
     */
    public static boolean getLeftPress() {
        return leftPress;
    }
    
    /** @return 
     * <ul>
     * <li>{@code false}- the right key is not pressed.
     * <li>{@code true}- the right key is pressed. 
     * </ul>
     */
    public static boolean getRightPress() {
        return rightPress;
    }
    
    /** @return 
     * <ul>
     * <li>{@code false}- the up key is not pressed.
     * <li>{@code true}- the up key is pressed. 
     * </ul>
     */
    public static boolean getUpPress() {
        return false;
    }
    
    /** @return 
     * <ul>
     * <li>{@code false}- the down key is not pressed.
     * <li>{@code true}- the down key is pressed.
     * </ul>
     */
    public static boolean getDownPress() {
        return downPress;
    }
    
    /** @return 
     * <ul>
     * <li>{@code false}- the Z key is not pressed.
     * <li>{@code true}- the Z key is pressed.
     * </ul>
     */
    public static boolean getZPress() {
        return zPress;
    }
    
    /** @return 
     * <ul>
     * <li>{@code false}- the X key is not pressed.
     * <li>{@code true}- the X key is pressed .
     * </ul>
     */
    public static boolean getXPress() {
        return xPress;
    }
    
    /** @return 
     * Checks if the z Key is pressed only once.
     * <ul>
     * <li>{@code false}- the z key is not pressed/been
     * held for a bit of time.
     * <li>{@code true}- the z key was pressed once 
     * (occurs when the key is first pressed).
     * </ul>
     */
    public static boolean checkForZPressOnce() {
        boolean check = false;
        if(zPress && zPressTimer <= 1) {
            check = true;
        }
        return check;
    }
    
    /** @return 
     * Checks if the x Key is pressed only once.
     * <ul>
     * <li>{@code false}- the x key is not pressed/been
     * held for a bit of time.
     * <li>{@code true}- the x key was pressed once 
     * (occurs when the key is first pressed).
     * </ul>
     */
    public static boolean checkForXPressOnce() {
        boolean check = false;
        if(xPress && xPressTimer <= 1) {
            check = true;
        }
        return check;
    }
    
    /**Sets the value of LeftPress (if key is pressed or not)
     * @param set represents value of key.*/
    public static void setLeftPress(boolean set) {
        leftPress = set;
    }
    /**Sets the value of LeftPress (if key is pressed or not)
     * @param set*/
    public static void setRightPress(boolean set) {
        rightPress = set;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {/*Note!- due to the way keys are processed
        //false = not pressed, true = pressed, it is impossible to check when a 
        key has just been released (whenReleased command in clickteam fusion for example)
        */
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
