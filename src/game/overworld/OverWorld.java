/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;
import game.gui.Menu.MenuType;
import game.gui.PlayerMenu;
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
    private static PlayerMenu playerMenu = new PlayerMenu(MenuType.MENUTYPE_VERTICAL);
    private static ArrayList<Room> rooms = new ArrayList<Room>();
    private static boolean generateEverything = false;
    private static RoomType currentRoom = ROOM_SONIC_HOUSE;
    private Sonic sonic;
    public void standard() {
        if(generateEverything == false) {
            //Music.playTestAreaTheme(1, 0);
            generate();
        }      
        else if(generateEverything == true) {
            getCurrentRoom().runRoom();
            if(sonic == null) {
                sonic = new Sonic();     
            }            
            sonic.setup(this);
            playerMenu.standard();
        }      
    }   
    
    public void draw(Graphics2D g2) {
        getCurrentRoom().drawRoom(g2);
        if(sonic != null) {
            sonic.draw(g2);
        }
        g2.drawString(""+currentRoom,300,200);
        playerMenu.draw(g2);
    }
    
    public void generate() {      
        rooms.add(new Room(this, ROOM_SONIC_HOUSE));
        rooms.add(new Room(this,ROOM_SONIC_TEST));
        System.out.println("Everything has been generated");
        if(rooms.size() == 2) {
            generateEverything = true;    
        }        
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
        if(sonic == null) {
            sonic = new Sonic();    
        }      
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
