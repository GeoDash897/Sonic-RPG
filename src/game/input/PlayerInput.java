/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.input;

import game.GameLoop;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**This class controls player input (input from the keyboard)
 *
 * @author GeoSonicDash
 */
public class PlayerInput implements KeyListener {
    private static HashMap<Integer, Button> keys;
    
    public PlayerInput(){
        keys = new HashMap<Integer, Button>();
        addKeys();
    }
    
    private void addKeys() {
        keys.put(KeyEvent.VK_DOWN, new Button(KeyEvent.VK_DOWN));
        keys.put(KeyEvent.VK_UP, new Button(KeyEvent.VK_UP));
        keys.put(KeyEvent.VK_LEFT, new Button(KeyEvent.VK_LEFT));
        keys.put(KeyEvent.VK_RIGHT, new Button(KeyEvent.VK_RIGHT));
        keys.put(KeyEvent.VK_Z, new Button(KeyEvent.VK_Z));
        keys.put(KeyEvent.VK_X, new Button(KeyEvent.VK_X));
        keys.put(KeyEvent.VK_ESCAPE, new Button(KeyEvent.VK_ESCAPE));
        keys.put(KeyEvent.VK_ENTER, new Button(KeyEvent.VK_ENTER));
    }
    
    /**
     * Updates the variables of Buttons (keyboard input) in keys HashMap.
     */
    public void standard() {
        for (Map.Entry<Integer, Button> entry : keys.entrySet()) {
            Button button = entry.getValue();
            button.standard();
        }
    }
    
    /**
     * Draws the variables of the current key(s) that are pressed.
     */
    public void draw(Graphics2D g2) {
        if(GameLoop.getDebug()) {
            for (Map.Entry<Integer, Button> entry : keys.entrySet()) {
                Button button = entry.getValue();
                if(button.getButtonPressed()) {
                    button.draw(g2);    
                }           
            }    
        }        
    }
    
    public static boolean checkIsPressed(int numOfKey) {
        return keys.get(numOfKey).isPressed();
    }
    
    public static boolean checkIsPressedOnce(int numOfKey) {
        return keys.get(numOfKey).isPressedOnce();
    }
    
    public static boolean checkIsJustReleased(int numOfKey) {
        return keys.get(numOfKey).isJustReleased();
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == e.VK_DOWN && !keys.get(KeyEvent.VK_DOWN).isPressed()) {
            keys.get(KeyEvent.VK_DOWN).setPressed(true);
        }        
        if(e.getKeyCode() == e.VK_UP && !keys.get(KeyEvent.VK_UP).isPressed()) {
            keys.get(KeyEvent.VK_UP).setPressed(true);
        }
        if(e.getKeyCode() == e.VK_LEFT && !keys.get(KeyEvent.VK_LEFT).isPressed()) {
            keys.get(KeyEvent.VK_LEFT).setPressed(true);
        }
        if(e.getKeyCode() == e.VK_RIGHT && !keys.get(KeyEvent.VK_RIGHT).isPressed()) {
            keys.get(KeyEvent.VK_RIGHT).setPressed(true);
        }
        if(e.getKeyCode() == e.VK_Z && !keys.get(KeyEvent.VK_Z).isPressed()) {
            keys.get(KeyEvent.VK_Z).setPressed(true);
        }
        if(e.getKeyCode() == e.VK_X && !keys.get(KeyEvent.VK_X).isPressed()) {
            keys.get(KeyEvent.VK_X).setPressed(true);
        }
        if(e.getKeyCode() == e.VK_ESCAPE && !keys.get(KeyEvent.VK_ESCAPE).isPressed()) {
            keys.get(KeyEvent.VK_ESCAPE).setPressed(true);
        }
        if(e.getKeyCode() == e.VK_ENTER && !keys.get(KeyEvent.VK_ENTER).isPressed()) {
            keys.get(KeyEvent.VK_ENTER).setPressed(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == e.VK_DOWN && keys.get(KeyEvent.VK_DOWN).isPressed()) {
            keys.get(KeyEvent.VK_DOWN).setPressed(false);
        }
        if(e.getKeyCode() == e.VK_UP && keys.get(KeyEvent.VK_UP).isPressed()) {
            keys.get(KeyEvent.VK_UP).setPressed(false);
        }
        if(e.getKeyCode() == e.VK_LEFT && keys.get(KeyEvent.VK_LEFT).isPressed()) {
            keys.get(KeyEvent.VK_LEFT).setPressed(false);
        }
        if(e.getKeyCode() == e.VK_RIGHT && keys.get(KeyEvent.VK_RIGHT).isPressed()) {
            keys.get(KeyEvent.VK_RIGHT).setPressed(false);
        }
        if(e.getKeyCode() == e.VK_Z && keys.get(KeyEvent.VK_Z).isPressed()) {
            keys.get(KeyEvent.VK_Z).setPressed(false);
        }
        if(e.getKeyCode() == e.VK_X && keys.get(KeyEvent.VK_X).isPressed()) {
            keys.get(KeyEvent.VK_X).setPressed(false);
        }
        if(e.getKeyCode() == e.VK_ESCAPE && keys.get(KeyEvent.VK_ESCAPE).isPressed()) {
            keys.get(KeyEvent.VK_ESCAPE).setPressed(false);
        }
        if(e.getKeyCode() == e.VK_ENTER && keys.get(KeyEvent.VK_ENTER).isPressed()) {
            keys.get(KeyEvent.VK_ENTER).setPressed(false);
        }
    }
}
