/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.sonic;

import game.items.Inventory;
import game.overworld.OverWorld;
import game.overworld.Room;
import game.playerMenu.OWMenuManager;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author GeoSonicDash
 */
public class PlayerManager {
    private int partySize;
    private ArrayList<Inventory> inventory;
    private OWMenuManager owMManager;
    private OverWorld overWorld;
    private Room currentRoom;
    private ArrayList<PlayerCharacter> players;
    public PlayerManager(OverWorld overWorld) {
        this.overWorld = overWorld;
        this.currentRoom = overWorld.getCurrentRoom();
        this.players = new ArrayList<PlayerCharacter>(2);
        players.add(new PlayerCharacter(this, OWPosition.OWPOSITION_FRONT, 300, 600));
        players.add(new PlayerCharacter(this, OWPosition.OWPOSITION_BACK, 156, 600));
        partySize = players.size();
        this.inventory = new ArrayList<Inventory>(2);
        inventory.add(new Inventory(30));
        inventory.add(new Inventory(5));
        this.owMManager = new OWMenuManager(this);
        currentRoom.addGUI(owMManager);
    }
    
    public void standard() {
        for(PlayerCharacter temp : players) {
            temp.standard();
        }
    }
    
    public void draw(Graphics2D g2) {
        for(PlayerCharacter temp : players) {
            temp.draw(g2);
        }
    }
    
    public Room getCurrentRoom() {
        return currentRoom;
    }
    
    public Inventory getInventory(int position) {
        return inventory.get(position);
    }
    
    public OWMenuManager getOWMenuManager() {
        return owMManager;
    }
    
    public ArrayList<PlayerCharacter> getCharacterList() {
        return players;
    }
    
    public PlayerCharacter getCharacter(int position) {
        return players.get(position);
    }
    
    public int getPartySize() {
        return partySize;
    }
    
    /**Adds PlayerCharacter's picture and GUI to the new {@code Room} that PlayerCharacter is in.
     * 
     */
    public void addToNewRoom() {
        if(currentRoom != overWorld.getCurrentRoom()) {
            currentRoom = overWorld.getCurrentRoom();
        }
        currentRoom.addGUI(owMManager);
        for(PlayerCharacter temp : players) {
            temp.getAnimationControl().addToRoomPictureAL();
        }
    }
    
    public enum PlayerName {
        PLAYERNAME_SONIC,
        PLAYERNAME_TAILS
    };
    
    public enum OWPosition {
        OWPOSITION_FRONT,
        OWPOSITION_BACK
    };
}
