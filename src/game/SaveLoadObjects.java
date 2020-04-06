/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import game.overworld.DefaultObject;
import game.overworld.Ground;
import game.overworld.Monitor;
import game.overworld.NPC;
import game.overworld.OverWorld;
import game.overworld.Room.RoomType;
import game.overworld.Sign;
import game.overworld.Spring;
import game.overworld.Warp;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 *This class controls creating DefaultObjects (Monitors, Springs, Signs, etc)
 * from lines of text, and saving rooms.
 * @author GeoSonicDash
 */
public class SaveLoadObjects {
    private ArrayList<DefaultObject> objects = new ArrayList<DefaultObject>();
    
    /**
     * 
     * @param overworld instance of overworld passed from OverWorld class (don't have to create
     * new objects for OverWorld).
     * @param roomType room where objects are going to be loaded (passed from Room class).
     * @return {@code objects}- an arrayList of the DefaultObjects that are in Room {@code roomType}.
     */
    public ArrayList<DefaultObject> getObject(OverWorld overworld, RoomType roomType) {
        File file = new File("src/game/TempSave.txt");
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String currentLine = br.readLine();
            /*For each line, a thread is created- the thread reads the line of text
            and uses information from the line to create the DefaultObject*/
            while(currentLine != null) {
                Thread lo = new Thread(new SaveLoadObjects.LoadObject(currentLine, overworld, roomType));
                lo.start();                    
                currentLine = br.readLine();
            }
            br.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return objects;
    }
    
    /**
     * Saves all DefaultObjects that are in Room {@code roomType} to {@code TempSave.txt}.
     * Note!- This only saves the DefaultObjects of a room, not Ground tiles
     * @param roomType room that contains all of the DefaultObjects that are going to be saved
     * @param groundTiles
     * @param roomObjects 
     */
    public void saveCurrentRoom(RoomType roomType,ArrayList<Ground> groundTiles, ArrayList<DefaultObject> roomObjects) {
        File file = new File("src/game/TempSave.txt");
        File saveRoom = new File("src/game/saveRoomTemp.txt");
        try {
            if(!saveRoom.exists()) {
                saveRoom.createNewFile();
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedWriter bw = new BufferedWriter(new FileWriter(saveRoom));
            ArrayList<String> sortObject = new ArrayList<String>();
            String currentLine = br.readLine();
            while(currentLine != null) {
                /*Adds any DefaultObjects that are not in the current room (the
                one that is being saved) sortObject arrayList*/
                String [] cLArray = currentLine.split(" ");
                if(!cLArray[0].equals(""+roomType)) {
                    sortObject.add(String.join(" ", cLArray));
                }
                currentLine = br.readLine();
            }        
            br.close();
            /*Adds any remaining DefaultObjects in the current room to sortObject arrayList
            At this point, order of arrayList- DO not in room, DO in room*/
            for(DefaultObject temp : roomObjects) {
                sortObject.add(roomType+" "+temp.toString());
            }
            /*Sorts it by the String (by the enum- I think) and saves it the TempSave*/
            Collections.sort(sortObject);
            for(String temp : sortObject) {
                bw.write(""+temp);
                bw.newLine();
            }        
            bw.flush();
            bw.close();
            file.delete();
            saveRoom.renameTo(new File("src/game/TempSave.txt"));          
        }
        catch(IOException e) {
            e.printStackTrace();
        }        
    }
    
    /**
     * Creates Monitor object.
     * @param overworld passed from getObject method, needed to create object, used
     * in the methods of monitor.
     * @param monitorType the type of monitor being created (Ring Monitor, Speed Monitor, etc).
     * @param layer the layer of the monitor (important for collision and drawing purposes).
     * @param xRef the x position of the monitor (top left hand corner).
     * @param yRef the y position of the monitor (top left hand corner).
     */
    public void createMonitor(OverWorld overworld, Monitor.MonitorType monitorType, int layer, int xRef, int yRef) {
        Monitor monitor = new Monitor(overworld, monitorType,layer,xRef, yRef);
        objects.add(monitor);
        overworld.getCurrentRoom().addPicture(monitor);
    }
    
    /**
     * Creates Sign object.
     * @param overworld passed from getObject method, needed to create object, used
     * in the methods of sign.
     * @param signType the type of sign being created (determines look and text).
     * @param layer the layer of the sign (important for collision and drawing purposes).
     * @param xRef the x position of the sign (top left hand corner).
     * @param yRef the y position of the sign (top left hand corner).
     */
    public void createSign(OverWorld overworld, Sign.SignType signType, int layer, int xRef, int yRef) {
        Sign sign = new Sign(overworld, signType, layer, xRef, yRef);
        objects.add(sign);
        overworld.getCurrentRoom().addPicture(sign);
    }
    
    /**
     * Creates NPC object.
     * @param overworld passed from getObject method, needed to create object, used
     * in the methods of NPC.
     * @param npcType the type of NPC being created (determines look and dialogue of NPC).
     * @param layer the NPC of the sign (important for collision and drawing purposes).
     * @param xRef the x position of the NPC (top left hand corner).
     * @param yRef the y position of the NPC (top left hand corner).
     */
    public void createNPC(OverWorld overworld, NPC.NPCType npcType, int layer, int xRef,int yRef) {
        NPC npc = new NPC(overworld, npcType,layer,xRef,yRef);
        objects.add(npc);
        overworld.getCurrentRoom().addPicture(npc);
    }
    
    /**
     * Creates Spring object.
     * @param overworld passed from getObject method, needed to create object, used
     * in the methods of Spring.
     * @param springType the type of NPC being created (Yellow Spring, Red Spring, etc).
     * @param layer the Spring of the sign (important for collision and drawing purposes).
     * @param xRef the x position of the Spring (top left hand corner).
     * @param yRef the y position of the Spring (top left hand corner).
     */
    public void createSpring(OverWorld overworld, Spring.SpringType springType, int layer, int xRef,int yRef) {
        Spring spring = new Spring(overworld, springType,layer,xRef,yRef);
        objects.add(spring);
        overworld.getCurrentRoom().addPicture(spring);
    }
    
    /**
     * Creates Warp object - used to transition between different rooms
     * @param overworld passed from getObject method, needed to create object, used
     * in the methods of Warp (needed for switching rooms).
     * @param warpType the type of Warp being created (determines the type of Warp).
     * @param layer the Spring of the Warp (important for collision and drawing purposes).
     * @param xRef the x position of the Spring (top left hand corner).
     * @param yRef the x position of the Spring (top left hand corner).
     */
    public void createWarp(OverWorld overworld, Warp.WarpType warpType, int layer, int xRef, int yRef) {
        Warp warp = new Warp(overworld, warpType,layer, xRef, yRef);
        objects.add(warp);
        overworld.getCurrentRoom().addPicture(warp);
    }
    
    /**
     * Thread used to create DefaultObjects in room (allows objects to be run individually- speeding up time a bit).
     */
    class LoadObject implements Runnable {
        private String currentLine;
        private volatile boolean isDone;
        private String name;
        private RoomType roomType;
        private OverWorld overworld;
        
        public LoadObject(String currentLine, OverWorld overworld, RoomType roomType) {
            this.currentLine = currentLine;
            this.overworld = overworld;
            this.isDone = false;
            this.name = "Memes";
            this.roomType = roomType;
        }
        
        /**
         * Specific thread of LoadObject- takes the currentLine and splits it up, uses information from it to create the DefaultObject.
         */
        @Override
        public synchronized void run() {
            if(!isDone) {
                String [] line = currentLine.split(" ");
                if(line[0].equals(String.valueOf(roomType))) {
                    /*If the first part of the line is == to the roomType (the room where objects are loaded), create the object
                    using information from currentLine*/
                    if(line[1].equals("Monitor:")) {
                        createMonitor(overworld, Monitor.MonitorType.valueOf(line[2]),Integer.valueOf(line[3]),Integer.valueOf(line[4]),Integer.valueOf(line[5]));
                        name = "Monitor In Room Thread";
                    }
                    else if(line[1].equals("NPC:")) {
                        createNPC(overworld, NPC.NPCType.valueOf(line[2]),Integer.valueOf(line[3]),Integer.valueOf(line[4]),Integer.valueOf(line[5]));
                        name = "NPC In Room Thread";
                    }
                    else if(line[1].equals("Sign:")) {
                        createSign(overworld, Sign.SignType.valueOf(line[2]),Integer.valueOf(line[3]),Integer.valueOf(line[4]),Integer.valueOf(line[5]));
                        name = "Sign In Room Thread";
                    } 
                    else if(line[1].equals("Spring:")) {
                        createSpring(overworld, Spring.SpringType.valueOf(line[2]),Integer.valueOf(line[3]),Integer.valueOf(line[4]),Integer.valueOf(line[5]));
                        name = "Spring In Room Thread";
                    }
                    else if(line[1].equals("Warp:")) {
                        createWarp(overworld, Warp.WarpType.valueOf(line[2]),Integer.valueOf(line[3]),Integer.valueOf(line[4]),Integer.valueOf(line[5]));
                        name = "Warp In Room Thread";
                    }
                }
                else if(!line[0].equals(String.valueOf(roomType))) {
                    //If the first part of the line is != to the roomType (the room where objects are loaded), don't create object
                    if(line[1].equals("Monitor:")) {
                        name = "Monitor Not In Room Thread";
                    }
                    else if(line[1].equals("NPC:")) {
                        name = "NPC Not In Room Thread";
                    }
                    else if(line[1].equals("Sign:")) {
                        name = "Sign Not In Room Thread";
                    } 
                    else if(line[1].equals("Spring:")) {
                        name = "Spring Not In Room Thread";
                    } 
                    else if(line[1].equals("Warp:")) {
                        name = "Warp Not In Room Thread";
                    }
                }
            }        
            isDone = true; //Ends the thread
            System.out.println(""+toString()+ " isDone: "+ isDone);
        }
        
        @Override
        public String toString() {
            return name;
        }
    }
}
