/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;
import game.player.PlayerManager;
import game.overworld.Room.RoomType;
import java.awt.Graphics2D;
import java.util.ArrayList;
import static game.overworld.Room.RoomType.MEDIVAL_SONIC_HOUSE;
import static game.overworld.Room.RoomType.MEDIVAL_SONIC_TEST;
/**This class controls what Rooms are in the rooms ArrayList (depends on the area Sonic 
 * is in).
 * @author GeoSonicDash
 */
public class OverWorld {    
    private static ArrayList<Room> rooms; 
    private static boolean generateEverything;
    private static AreaName currentArea;
    private static RoomType currentRoomName;
    private static Room currentRoom;
    private static PlayerManager manager;
    public OverWorld() {
        rooms = new ArrayList<Room>();                
        generateEverything = false;
        currentArea = AreaName.AREA_MEDIVAL;
        currentRoomName = MEDIVAL_SONIC_HOUSE;
        generateRoomAL();/*This needs to be run first in order to have an AL of rooms that Sonic
        can access (needed to obtain currentRoom- used to add Sonic's picture to pictureAL)*/
        if(manager == null) {
            manager = new PlayerManager(this); 
        } 
    }
    /**Part of main game loop- generates the correct room ArrayList (if in new area), or runs the logic
     * of the current room that Sonic is in.
     */
    public void standard() {
        if(generateEverything == false) {
            //Music.playTestAreaTheme(1, 0);
            generateRoomAL();//This generates correctRoomAL if the area changed           
        }      
        else if(generateEverything == true) {
            if(currentRoom == null) {
                currentRoom = getCurrentRoom();
            }
            manager.standard(); 
            currentRoom.runRoom();                                                            
        }      
    }  
    
    /**Part of main game loop- runs the drawing methods for current {@code Room} and Sonic
     * @param g2 {@code Graphics2D} needed for drawing.
     */
    public void draw(Graphics2D g2) {
        currentRoom.drawRoom(g2);
        manager.draw(g2);
        //g2.drawString(""+currentRoomName,300,200);               
    }
    
    public void generateRoomAL() {      
        int roomLimit = 0;
        if(currentArea == AreaName.AREA_MEDIVAL) {
            roomLimit = 2;
            rooms.add(new Room(this, AreaName.AREA_MEDIVAL, MEDIVAL_SONIC_HOUSE));
            rooms.add(new Room(this, AreaName.AREA_MEDIVAL, MEDIVAL_SONIC_TEST));
        }            
        if(rooms.size() == roomLimit) {
            System.out.println("Everything has been generated");
            generateEverything = true;    
        }        
    }
    public ArrayList<Room> getRoomsArrayList() {
        return rooms;
    }
    
    public PlayerManager getManager() {
        return manager;
    }
    
    public Room getCurrentRoom() {
        for(int i = 0; i < rooms.size(); i ++) {
            if(rooms.get(i).getRoomType() == currentRoomName) {
                return rooms.get(i);
            }
        }
        return null;
    }
    
    /**Changes from current {@code Room} to a new {@code Room}.
     * <p>Process of transitioning (at the moment):</p> 
     * 1. All of the gameObjects in the current {@code Room} are saved to text file.
     * <p>2. {@code currentRoomName} and {@code currentRoom} are set based on {@code newRoom} </p> 
     * <p>3. Current room variables in {@code sonic} package are updated to match changes. </p> 
     * @param newRoom the {@code RoomType} of the room being transitioned to.
     */
    public void setCurrentRoomType(RoomType newRoom) {
        getCurrentRoom().saveRoom();
        currentRoomName = newRoom;
        currentRoom = getCurrentRoom();
        manager.addToNewRoom();
    }
    
    public enum AreaName {
        AREA_MEDIVAL  
    };
}
