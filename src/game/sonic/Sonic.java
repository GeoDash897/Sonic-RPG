/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.sonic;
import game.Game;
import game.overworld.OverWorld;
import game.items.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
/*
    Author: GeoDash897  Date:10/5/19    Updated:10/5/19
*/
public class Sonic extends OverWorld {//This is the main Sonic class; 
    //this will run all the other classes related to Sonic (Animation, Inventory,etc)
    private static int level;
    private static int exp;
    private static int coins;
    private static int health;
    private static int maxHealth;
    private static int rings;
    private static int maxRings;
    private static int attack;
    private static int defense;
    private static int speed;
    private static int area = 1;
    private static int layer = 2;
    private static int owPowerUp = 0;
    private static boolean cutscene = false;
    private static boolean bMenu = false;
    private Inventory inventory;
    private OverWorldAction owa;
    private Animation animation;
    public void setup(Graphics2D g2) {
        owa = new OverWorldAction();
        inventory = new Inventory();
        inventory.addItem(new Potion());
        inventory.drawItem(0, g2);
        animation =  new Animation();
        if(!cutscene) {
            animation.standard(g2,owa.getXCenterSonic(),owa.getYCenterSonic()); 
            owa.standard(g2);
        }
        if(Game.getDebug()) {
            g2.setColor(Color.RED);
            g2.drawString("Sonic's area: "+area+", Sonic's layer: "+layer, 75,500);
        }
    }
    public int getAreaNumber() {
        return area;
    }
    public int getLayer() {
        return layer;
    }
    public int getOWPowerUp() {
        return owPowerUp;
    }
    public void changeOWPowerUp(int change) {
        owPowerUp = change;
    }
    public void increaseRings(int amount) {
        rings += amount;
    }
    @Override
    public void getPressInput(KeyEvent e) {
        if(!cutscene) {
            owa = new OverWorldAction();
            owa.keyPressed(e); 
        }  
    }
    public void getReleasedInput(KeyEvent e) {
        if(!cutscene) {
            owa = new OverWorldAction();
            owa.keyReleased(e); 
        }     
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == e.VK_RIGHT ) {
            getReleasedInput(e);
        }
        if (e.getKeyCode() == e.VK_LEFT ) {           
            getReleasedInput(e);
        }
        if (e.getKeyCode() == e.VK_DOWN) {           
            getReleasedInput(e);
        }
        if (e.getKeyCode() == e.VK_Z ) {           
            getReleasedInput(e);
        }
        if (e.getKeyCode() == e.VK_X) {           
            getReleasedInput(e);
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_RIGHT ) {
            getPressInput(e);
        }
        if (e.getKeyCode() == e.VK_LEFT ) {           
            getPressInput(e);
        }          
        if (e.getKeyCode() == e.VK_UP ) {
            getPressInput(e);
        }
        if (e.getKeyCode() == e.VK_DOWN) {
            getPressInput(e);
        }
        if (e.getKeyCode() == e.VK_Z ) {
            getPressInput(e);
        }
        if (e.getKeyCode() == e.VK_X ) {
            getPressInput(e);  
        }
        if (e.getKeyCode() == e.VK_ENTER) {
            
        } 
        if (e.getKeyCode() == e.VK_ESCAPE) {
            getPressInput(e);
        }
    }//end keypressed
}
