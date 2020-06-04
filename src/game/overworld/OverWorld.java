/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;
import game.overworld.Room.RoomType;
import static game.overworld.Room.RoomType.ROOM_SONIC_HOUSE;
import static game.overworld.Room.RoomType.ROOM_SONIC_TEST;
import game.sonic.*;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
/*
    Author: GeoDash897  Date:10/5/19    Updated:12/31/19
*/
//memes
public class OverWorld {    
    private static ArrayList<Room> rooms; 
    private static boolean generateEverything;
    private static RoomType currentRoomName;
    private static Room currentRoom;
    private Sonic sonic;
    public OverWorld() {
        rooms = new ArrayList<Room>();
        sonic = new Sonic();         
        generateEverything = false;
        currentRoomName = ROOM_SONIC_HOUSE;
    }
    public void standard() {
        if(generateEverything == false) {
            //Music.playTestAreaTheme(1, 0);
            generate();
        }      
        else if(generateEverything == true) {
            if(currentRoom != getCurrentRoom()) {
                currentRoom = getCurrentRoom();
            }
            currentRoom.runRoom();          
            sonic.setup(this);             
                           
        }      
    }   
    
    public void draw(Graphics2D g2) {
        getCurrentRoom().drawRoom(g2);
        sonic.draw(g2);
        g2.drawString(""+currentRoomName,300,200);               
    }
    
    public void generate() {      
        rooms.add(new Room(this, ROOM_SONIC_HOUSE));
        System.out.println("Everything has been generated");
        if(rooms.size() == 1) {
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
        sonic.addToPictureALAgain();
    }
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == e.VK_ENTER) {
            setCurrentRoomType(ROOM_SONIC_TEST);
        }
        if(e.getKeyCode() == e.VK_C) { 
            setCurrentRoomType(ROOM_SONIC_HOUSE);
        }
    }
}
