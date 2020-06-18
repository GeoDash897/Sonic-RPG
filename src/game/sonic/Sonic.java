/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.sonic;
import game.overworld.OverWorld;
import game.overworld.Room;
import java.awt.Graphics2D;

/**This is the main Sonic class. It runs all the other classes related to Sonic (Animation, Inventory, etc)
 * 
 * @author GeoSonicDash
 */
public class Sonic {
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
    private static int owPowerUp = 0;
    private static boolean cutscene = false;
    private static boolean bMenu = false;
    private static OverWorld overWorld;
    private static OWARemastered owaR;
    private static AnimationControl animation;
    private static Room currentRoom;
    private static PlayerMenu playerMenu;
    public Sonic(OverWorld overworld) {
        overWorld = overworld;
        currentRoom = overWorld.getCurrentRoom();                   
        animation =  new AnimationControl(this);    
        playerMenu = new PlayerMenu(this);
        currentRoom.addGUI(playerMenu);
        owaR = new OWARemastered(this, animation);   
    }
    public void standard() {          
        if(!cutscene) {     
            lastXCenterSonic = owaR.getXCenterSonic();
            lastYCenterSonic = owaR.getYCenterSonic();
            owaR.mainMethod(currentRoom);   
            animation.standard(lastXCenterSonic,lastYCenterSonic);
        }
    }
    
    public void draw(Graphics2D g2) {
        if(owaR != null) {
            owaR.drawDebug(g2);    
        }        
    }
    
    public void drawStatusStats(Graphics2D g2, int xMenu, int yMenu) {
        g2.drawString("Status: ", xMenu+410, yMenu+50);
        g2.drawString("Level: "+level, xMenu+30, yMenu+150);
        g2.drawString("EXP: "+exp, xMenu+30, yMenu+250);
        g2.drawString("Money: "+coins, xMenu+30, yMenu+350);
        g2.drawString("ExpToLU: 0", xMenu+600, yMenu+150);
    }
    
    public OverWorld getOverWorld() {
        return overWorld;
    }
    
    public static boolean getOWARAllowInput() {
        return owaR.getAllowInput();
    }
    
    public static void setOWARAllowInput(boolean temp) {
        owaR.setAllowInput(temp);
    }   
      
    public Room getCurrentRoom() {
        return currentRoom;
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
    
    /**Adds Sonic's picture and GUI to the new {@code Room} that Sonic is in.
     * 
     */
    public void addToNewRoom() {
        if(currentRoom != overWorld.getCurrentRoom()) {
            currentRoom = overWorld.getCurrentRoom();
        }
        currentRoom.addGUI(playerMenu);
        animation.addToRoomPictureAL();
    }
}
