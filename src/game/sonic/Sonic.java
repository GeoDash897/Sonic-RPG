/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.sonic;
import game.overworld.OverWorld;
import game.overworld.Room;
import java.awt.Graphics2D;
/*
    Author: GeoDash897  Date:10/5/19    Updated:10/5/19
*/
public class Sonic {//This is the main Sonic class; 
    //this will run all the other classes related to Sonic (Animation, Inventory,etc)
    private static int lastXCenterSonic;
    private static int lastYCenterSonic;
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
    private static int owPowerUp = 0;
    private static boolean allowInput = true;
    private static boolean cutscene = false;
    private static boolean bMenu = false;
    private static OverWorld overWorld;
    private static OWARemastered owaR;
    private static AnimationControl animation;
    private static Room currentRoom;
    public void setup(OverWorld overworld) {
        if(owaR == null) {
            owaR = new OWARemastered();    
        } 
        if(overWorld == null) {
            overWorld = overworld;    
        }
        if(animation == null) {
            animation =  new AnimationControl();    
        }       
        currentRoom = overworld.getCurrentRoom();
        if(!cutscene && !PlayerMenu.isVisible()) {     
            lastXCenterSonic = owaR.getXCenterSonic();
            lastYCenterSonic = owaR.getYCenterSonic();
            owaR.mainMethod(this, currentRoom, animation);   
            animation.standard(currentRoom,lastXCenterSonic,lastYCenterSonic);
        }
    }
    
    public void draw(Graphics2D g2) {
        if(owaR != null) {
            owaR.drawDebug(g2);    
        }        
    }
    
    public OverWorld getOverWorld() {
        return overWorld;
    }
    
    public void addToPictureALAgain() {
        animation.setaddToPictureAL(false);
    }
    public int getAreaNumber() {
        return area;
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
}
