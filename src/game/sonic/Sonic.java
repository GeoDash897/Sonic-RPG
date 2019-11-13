/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.sonic;
import game.overworld.OverWorld;
import game.items.*;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
/*
    Author: GeoDash897  Date:10/5/19    Updated:10/5/19
*/
public class Sonic extends OverWorld {//This is the main Sonic class; 
    //this will run all the other classes related to Sonic (Animation, Inventory,etc)
    private int level;
    private int exp;
    private int coins;
    private int health;
    private int maxHealth;
    private int rings;
    private int maxRings;
    private int attack;
    private int defense;
    private int speed;
    private static boolean cutscene = false;
    private static boolean bMenu = false;
    private Inventory inventory;
    private OverWorldAction owa;
    private Animation animation;
    public void setup(Graphics2D g2) {
        owa = new OverWorldAction();
        g2.drawString("Sonic is running",100,125);
        inventory = new Inventory();
        inventory.addItem(new Potion());
        inventory.drawItem(0, g2);
        animation =  new Animation();
        if(!cutscene) {
            animation.standard(g2,owa.getXCenterSonic(),owa.getYCenterSonic()); 
            owa.standard(g2);
        }
        g2.drawString(animation.toString(),100,150);
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
              
        }
        if (e.getKeyCode() == e.VK_ENTER) {
            
        }                 
    }//end keypressed
}
