/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.sonic;
import game.sonic.PlayerManager.OWPosition;
import java.awt.Graphics2D;

/**This is the main Player class. It runs all the other classes related to the player (Animation, Inventory, etc)
 * 
 * @author GeoSonicDash
 */
public class PlayerCharacter {
    private int lastXCenterPlayer;
    private int lastYCenterPlayer;
    private int level;
    private int exp;
    private static int coins;
    private int health;
    private int maxHealth;
    private int rings;
    private int maxRings;
    private int attack;
    private int defense;
    private int speed;   
    private int owPowerUp = 0;
    private boolean cutscene = false;
    private boolean bMenu = false;
    private static PlayerManager manager;   
    private OWARemastered owaR;
    private OWPosition temp;
    private AnimationControl animation;
    public PlayerCharacter(PlayerManager man, OWPosition owPosition, int x, int y) {
        manager = man;
        animation =  new AnimationControl(manager);    
        temp = owPosition;
        owaR = new OWARemastered(man, owPosition, animation, x, y);   
    }
    public void standard() {          
        if(!cutscene) {     
            lastXCenterPlayer = owaR.getXCenterPlayer();
            lastYCenterPlayer = owaR.getYCenterPlayer();
            owaR.mainMethod(manager.getCurrentRoom());   
            animation.standard(lastXCenterPlayer,lastYCenterPlayer);
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
        g2.drawString("Position: "+temp, xMenu+600, yMenu+300);
    }
    
    public AnimationControl getAnimationControl() {
        return animation;
    }
    
    public OWARemastered getOWARemastered() {
        return owaR;
    }
    
    public boolean getOWARAllowInput() {
        return owaR.getAllowInput();
    }
    
    public void setOWARAllowInput(boolean temp) {
        owaR.setAllowInput(temp);
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
