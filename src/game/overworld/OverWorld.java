/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;
import game.Game;
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
public class OverWorld extends Game {
    private static ArrayList<Room> rooms = new ArrayList<Room>();
    private static boolean generateEverything = false;
    private static RoomType currentRoom = ROOM_SONIC_HOUSE;
    private Sonic sonic;
    public void standard(Graphics2D g2) {
        if(generateEverything == false) {
            //Music.playTestAreaTheme(1, 0);    
        }      
        if(generateEverything == false) {
            generate(g2);
        }
        else if(generateEverything == true) {
            getCurrentRoom().runRoom(g2);
            g2.drawString(""+currentRoom,300,200);
            sonic = new Sonic();
            sonic.setup(g2,this);
        }      
    }   
    public void generate(Graphics2D g2) {      
        rooms.add(new Room(this, ROOM_SONIC_HOUSE));
        rooms.add(new Room(this,ROOM_SONIC_TEST));
        System.out.println("Everything has been generated");
        generateEverything = true;
    }
    public ArrayList<Room> getRoomsArrayList() {
        return rooms;
    }
    public Room getCurrentRoom() {
        for(int i = 0; i < rooms.size(); i ++) {
            if(rooms.get(i).getRoomType() == currentRoom) {
                return rooms.get(i);
            }
        }
        return null;
    }
    public void setCurrentRoomType(RoomType newRoom) {
        getCurrentRoom().saveRoom();
        currentRoom = newRoom;
        sonic = new Sonic();
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
