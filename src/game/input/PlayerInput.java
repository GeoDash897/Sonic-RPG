/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.input;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
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
    }
    
    public void standard() {
        for (Map.Entry<Integer, Button> entry : keys.entrySet()) {
            Button button = entry.getValue();
            button.standard();
        }
    }

    public void draw(Graphics2D g2) {
        for (Map.Entry<Integer, Button> entry : keys.entrySet()) {
            Button button = entry.getValue();
            if(button.getButtonPressed()) {
                button.draw(g2);    
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
    }
}
