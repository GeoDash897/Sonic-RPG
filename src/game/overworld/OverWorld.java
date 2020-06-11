/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;
import game.overworld.Room.RoomType;
import game.sonic.*;
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
    private static Sonic sonic;
    public OverWorld() {
        rooms = new ArrayList<Room>();                
        generateEverything = false;
        currentArea = AreaName.AREA_MEDIVAL;
        currentRoomName = MEDIVAL_SONIC_HOUSE;
        generateRoomAL();/*This needs to be run first in order to have an AL of rooms that Sonic
        can access (needed to obtain currentRoom- used to add Sonic's picture to pictureAL)*/
        if(sonic == null) {
            sonic = new Sonic(this); 
        } 
    }
    /**
     * Part of main game loop- generates the correct room ArrayList (if in new area), or runs the logic
     * of the current room that Sonic is in.
     */
    public void standard() {
        if(generateEverything == false) {
            System.out.println("Code Ran");
            //Music.playTestAreaTheme(1, 0);
            generateRoomAL();//This generates correctRoomAL if the area changed           
        }      
        else if(generateEverything == true) {
            if(currentRoom == null) {
                currentRoom = getCurrentRoom();
            }
            currentRoom.runRoom();          
            sonic.standard();                                        
        }      
    }   
    
    public void draw(Graphics2D g2) {
        getCurrentRoom().drawRoom(g2);
        sonic.draw(g2);
        g2.drawString(""+currentRoomName,300,200);               
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
    public Room getCurrentRoom() {
        for(int i = 0; i < rooms.size(); i ++) {
            if(rooms.get(i).getRoomType() == currentRoomName) {
                return rooms.get(i);
            }
        }
        return null;
    }
    public void setCurrentRoomType(RoomType newRoom) {
        getCurrentRoom().saveRoom();
        currentRoomName = newRoom;
        currentRoom = getCurrentRoom();
        sonic.addToNewRoom();
    }
    
    public enum AreaName {
        AREA_MEDIVAL  
    };
}
