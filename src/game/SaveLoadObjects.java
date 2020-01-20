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
 *
 * @author GeoSonicDash
 */
public class SaveLoadObjects {
    private ArrayList<DefaultObject> objects = new ArrayList<DefaultObject>();
    public ArrayList<DefaultObject> getObject(RoomType roomType) {
        File file = new File("src/game/TempSave.txt");
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String currentLine = br.readLine();
            while(currentLine != null) {
                Thread lo = new Thread(new SaveLoadObjects.LoadObject(currentLine, roomType));
                lo.start();                    
                //loadObjectThreads.add(lo);
                currentLine = br.readLine();
            }
            br.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return objects;
    }
    public ArrayList<DefaultObject> saveCurrentRoom(RoomType roomType,ArrayList<Ground> groundTiles, ArrayList<DefaultObject> roomObjects) {
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
                String [] cLArray = currentLine.split(" ");
                if(!cLArray[0].equals(""+roomType)) {
                    sortObject.add(String.join(" ", cLArray));
                }
                currentLine = br.readLine();
            }        
            br.close();
            for(DefaultObject temp : roomObjects) {
                sortObject.add(roomType+" "+temp.toString());
            }
            Collections.sort(sortObject);
            for(String temp : sortObject) {
                bw.write(""+temp);
                bw.newLine();
            }        
            bw.flush();
            bw.close();
            file.delete();
            saveRoom.renameTo(new File("src/game/TempSave.txt"));
            objects.removeAll(objects);
            roomObjects.removeAll(roomObjects);
            groundTiles.removeAll(groundTiles);             
        }
        catch(IOException e) {
            e.printStackTrace();
        }        
        return roomObjects;
    }
    public void createMonitor(Monitor.MonitorType monitorType, int layer, int xRef, int yRef) {
        objects.add(new Monitor(monitorType, layer,xRef, yRef));
    }
    public void createSign(Sign.SignType signType, int layer, int xRef, int yRef) {
        objects.add(new Sign(signType, layer, xRef, yRef));
    }
    public void createNPC(NPC.NPCType npcType, int layer, int xRef,int yRef) {
        objects.add(new NPC(npcType,layer,xRef,yRef));
    }
    public void createSpring(Spring.SpringType springType, int layer, int xRef,int yRef) {
        objects.add(new Spring(springType,layer,xRef,yRef));
    }
    public void createWarp(Warp.WarpType warpType, int layer, int xRef, int yRef) {
        objects.add(new Warp(warpType, layer, xRef, yRef));
    }
    class LoadObject implements Runnable {
        private String currentLine;
        private volatile boolean isDone;
        private String name;
        private RoomType roomType;
        public LoadObject(String currentLine, RoomType roomType) {
            this.currentLine = currentLine;
            this.isDone = false;
            this.name = "Memes";
            this.roomType = roomType;
        }
        @Override
        public synchronized void run() {
            if(!isDone) {
                String [] line = currentLine.split(" ");
                if(line[0].equals(String.valueOf(roomType))) {
                    if(line[1].equals("Monitor:")) {
                        createMonitor(Monitor.MonitorType.valueOf(line[2]),Integer.valueOf(line[3]),Integer.valueOf(line[4]),Integer.valueOf(line[5]));
                        name = "Monitor In Room Thread";
                    }
                    else if(line[1].equals("NPC:")) {
                        createNPC(NPC.NPCType.valueOf(line[2]),Integer.valueOf(line[3]),Integer.valueOf(line[4]),Integer.valueOf(line[5]));
                        name = "NPC In Room Thread";
                    }
                    else if(line[1].equals("Sign:")) {
                        createSign(Sign.SignType.valueOf(line[2]),Integer.valueOf(line[3]),Integer.valueOf(line[4]),Integer.valueOf(line[5]));
                        name = "Sign In Room Thread";
                    } 
                    else if(line[1].equals("Spring:")) {
                        createSpring(Spring.SpringType.valueOf(line[2]),Integer.valueOf(line[3]),Integer.valueOf(line[4]),Integer.valueOf(line[5]));
                        name = "Spring In Room Thread";
                    }
                    else if(line[1].equals("Warp:")) {
                        createWarp(Warp.WarpType.valueOf(line[2]),Integer.valueOf(line[3]),Integer.valueOf(line[4]),Integer.valueOf(line[5]));
                        name = "Warp In Room Thread";
                    }
                }
                else if(!line[0].equals(String.valueOf(roomType))) {
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
            isDone = true;
            System.out.println(""+toString()+ " isDone: "+ isDone);
        }
        @Override
        public String toString() {
            return name;
        }
    }
}
