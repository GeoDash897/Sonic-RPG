/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.player;
import game.player.PlayerManager.OWPosition;
import game.player.mario.MarioOWA;
import game.player.randomBattle.PlayerBattleCharacter;
import game.player.sonic.SonicOWA;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

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
    private boolean cutscene = false;
    private boolean bMenu = false;
    
    private static PlayerManager manager;   
    private MarioOWA marioOWA;
    private SonicOWA sonicOWA;   
    private AnimationControl animation;
    private PlayerBattleCharacter playerBC;
    
    private PlayerName playerName;
    private OWPosition owPosition;
    
    public PlayerCharacter(PlayerManager man, PlayerName playerName, OWPosition owPosition, int x, int y) {
        manager = man;
        this.playerName = playerName;
        this.owPosition = owPosition;
        this.animation =  new AnimationControl(manager, this);      
        if(playerName == PlayerName.PLAYERNAME_SONIC) {
            this.sonicOWA = new SonicOWA(man, owPosition, animation, x, y);           
        }
        else if(playerName == PlayerName.PLAYERNAME_MARIO) {
            this.marioOWA = new MarioOWA(man, owPosition, animation, x, y);              
        }    
        this.playerBC = new PlayerBattleCharacter(this);
    }
    public void OWAStandard() {          
        if(!cutscene) {     
            if(playerName == PlayerName.PLAYERNAME_SONIC) {
                lastXCenterPlayer = sonicOWA.getXDrawCenterPlayer();
                lastYCenterPlayer = sonicOWA.getYDrawCenterSonic();
                sonicOWA.mainMethod(manager.getCurrentRoom());
            }
            else if(playerName == PlayerName.PLAYERNAME_MARIO) {
                lastXCenterPlayer = marioOWA.getXDrawCenterPlayer();
                lastYCenterPlayer = marioOWA.getYDrawCenterSonic();
                marioOWA.mainMethod(manager.getCurrentRoom());
            }
            animation.standard(lastXCenterPlayer,lastYCenterPlayer);
        }
    }
       
    public void draw(Graphics2D g2) { 
        //sonicOWA.drawDebug(g2);
        marioOWA.drawDebug(g2);
    }
    
    public void drawStatusStats(Graphics2D g2, int xMenu, int yMenu) {
        String name = playerName.toString();
        g2.drawString(name.substring(name.indexOf("_")+1), xMenu+450, yMenu+50);       
        g2.drawString("Level: "+level, xMenu+30, yMenu+200);
        g2.drawString("EXP: "+exp, xMenu+30, yMenu+300);
        g2.drawString("Money: "+coins, xMenu+30, yMenu+400);
        g2.drawString("ExpToLU: 0", xMenu+600, yMenu+200);
        g2.drawString("Position: "+owPosition, xMenu+600, yMenu+350);
        if(playerName == PlayerName.PLAYERNAME_SONIC) {
            g2.drawString("Status: ", xMenu+410, yMenu+100);
            Image temp = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Victory Pose B.gif");
            g2.drawImage(temp,xMenu+200,yMenu+20,576,576,null); 
        }
        else if(playerName == PlayerName.PLAYERNAME_MARIO) {
            g2.drawString("Status: ", xMenu+390, yMenu+100);
            Image temp = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Mario Battle Select.gif");
            g2.drawImage(temp,xMenu+150,yMenu+20,576,576,null); 
        }
    }
    
    public AnimationControl getAnimationControl() {
        return animation;
    }
    
    public PlayerBattleCharacter getPlayerBattleCharacter() {
        return playerBC;
    }
    
    public boolean getOWARAllowInput() {
        if(manager.getCharacter(0).getPlayerName() == PlayerName.PLAYERNAME_SONIC) {
            return sonicOWA.isInputAllowed();
        }
        else {
            return marioOWA.isInputAllowed();
        }       
    }
    
    public void setOWARAllowInput(boolean temp) {
        if(manager.getCharacter(0).getPlayerName() == PlayerName.PLAYERNAME_SONIC) {
            sonicOWA.setAllowInput(temp);
        }
        else {
            marioOWA.setAllowInput(temp);
        }
    }             
    
    public PlayerName getPlayerName() {
        return playerName;
    }
   
    public enum PlayerName {
        PLAYERNAME_SONIC,
        PLAYERNAME_MARIO
    };
}
