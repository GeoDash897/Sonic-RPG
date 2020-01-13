/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;
import game.Game;
import game.overworld.Room.RoomType;
import static game.overworld.Room.RoomType.ROOM_SONIC_HOUSE;
import game.sonic.*;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
/*
    Author: GeoDash897  Date:10/5/19    Updated:12/31/19
*/
//memes
public class OverWorld extends Game {
    private static ArrayList<Ground> groundTiles = new ArrayList<Ground>();
    private static ArrayList<DefaultObject> objects = new ArrayList<DefaultObject>();
    private static ArrayList<Thread> loadObjectThreads = new ArrayList<Thread>();
    private static ArrayList<Room> rooms = new ArrayList<Room>();
    private static boolean generateEverything = false;
    private static RoomType currentRoom = ROOM_SONIC_HOUSE;
    public void standard(Graphics2D g2) {
        for(int i = 0; i < objects.size(); i++) {
            g2.drawString(objects.get(i).toString(),500, 100+(25*i));
        }
        if(generateEverything == false) {
            //Music.playTestAreaTheme(1, 0);    
        }      
        if(generateEverything == false) {
            generate(g2);
        }
        else if(generateEverything == true) {
            getCurrentRoom().runRoom(g2);
            g2.drawString(""+currentRoom,300,200);
            Sonic sonic = new Sonic();
            sonic.setup(g2);
        }      
    }   
    public void generate(Graphics2D g2) {      
        rooms.add(new Room(ROOM_SONIC_HOUSE));
        System.out.println("Everything has been generated");
        generateEverything = true;
    }
    public Room getCurrentRoom() {
        for(int i = 0; i < rooms.size(); i ++) {
            if(rooms.get(i).getRoomType() == currentRoom) {
                return rooms.get(i);
            }
        }
        return null;
    }
    public static void setCurrentRoomType(RoomType newRoom) {
        currentRoom = newRoom;
    }
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == e.VK_ENTER) {
            getCurrentRoom().saveRoom();
        }
        if(e.getKeyCode() == e.VK_C) {
            
        }
    }
}
