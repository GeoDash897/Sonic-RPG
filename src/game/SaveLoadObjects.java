/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import game.gameObjects.Monitor;
import game.gameObjects.Monitor.MonitorType;
import game.gameObjects.Sign;
import game.gameObjects.Sign.SignType;
import game.gameObjects.SkeletonNPC;
import game.gameObjects.SkeletonNPC.NPCSkeletonType;
import game.gameObjects.Spring;
import game.gameObjects.Spring.SpringType;
import game.gameObjects.Warp;
import game.gameObjects.Warp.WarpType;
import game.overworld.Room;
import game.overworld.Room.RoomType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 *This class controls creating DefaultObjects (Monitors, Springs, Signs, etc)
 * from lines of text, and saving rooms.
 * @author GeoSonicDash
 */
public class SaveLoadObjects {
    public static void createGameObjectArrayList(String area, Room room) {
        try {
            File file = new File("src/game/"+area+".txt");
            if(!file.exists()) {
                file.createNewFile();
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String currentLine = br.readLine();
            while(currentLine != null) {
                String[] temp = currentLine.split(" ");
                if(temp[0].equals(String.valueOf(room.getRoomType()))) {
                    createObject(temp, room);    
                }
                currentLine = br.readLine();
            }
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private static void createObject(String[] lineSplit, Room room) {
        String objectType = lineSplit[1].substring(0, lineSplit[1].length()-1);
        switch(objectType) {
            case "Sign":
                Sign sign = new Sign(room, SignType.valueOf(lineSplit[2]), Integer.valueOf(lineSplit[3]), 
                        Integer.valueOf(lineSplit[4]), Integer.valueOf(lineSplit[5]));
                room.addGameObject(sign);
                room.addGUI(sign);
                room.addPicture(sign);
                break;
            case "Monitor":
                Monitor monitor = new Monitor(room, MonitorType.valueOf(lineSplit[2]), Integer.valueOf(lineSplit[3]), 
                        Integer.valueOf(lineSplit[4]), Integer.valueOf(lineSplit[5]));
                room.addGameObject(monitor);
                room.addPicture(monitor);
                break;
            case "SkeletonNPC":
                SkeletonNPC sNPC = new SkeletonNPC(room, NPCSkeletonType.valueOf(lineSplit[2]), Integer.valueOf(lineSplit[3]), 
                        Integer.valueOf(lineSplit[4]), Integer.valueOf(lineSplit[5]));
                room.addGameObject(sNPC);
                room.addGUI(sNPC);
                room.addPicture(sNPC);
                break;
            case "Spring":
                Spring spring = new Spring(room, SpringType.valueOf(lineSplit[2]), Integer.valueOf(lineSplit[3]), 
                        Integer.valueOf(lineSplit[4]), Integer.valueOf(lineSplit[5]));
                room.addGameObject(spring);
                room.addPicture(spring);
                break;
            case "Warp":
                Warp warp = new Warp(room, WarpType.valueOf(lineSplit[2]), RoomType.valueOf(lineSplit[3]), 
                        Integer.valueOf(lineSplit[4]), Integer.valueOf(lineSplit[5]),Integer.valueOf(lineSplit[6]), Integer.valueOf(lineSplit[7])
                ,Integer.valueOf(lineSplit[8]));
                room.addGameObject(warp);
                room.addPicture(warp);
                break;
            default:
                break;
        }
            
    }
}
