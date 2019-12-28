/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;
import game.Game;
import game.overworld.Ground.GroundType;
import static game.overworld.Ground.GroundType.GRD_SONICHOUSE_WOODPLANK;
import static game.overworld.Ground.GroundType.GRD_SONICHOUSE_WOODSLOPE;
import game.overworld.NPC.NPCType;
import game.overworld.Monitor.MonitorType;
import game.overworld.Sign.SignType;
import game.overworld.Spring.SpringType;
import game.sonic.*;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
/*
    Author: GeoDash897  Date:10/5/19    Updated:10/5/19
*/
//memes
public class OverWorld extends Game {
    private static ArrayList<Ground> groundTiles = new ArrayList<Ground>();
    private static ArrayList<DefaultObject> objects = new ArrayList<DefaultObject>();
    private static ArrayList<Thread> loadObjectThreads = new ArrayList<Thread>();
    private static int generateEverything = 0;
    private static int currentArea = 0;
    public void getObject() {
        File file = new File("src/game/TempSave.txt");
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String currentLine = br.readLine();
            while(currentLine != null) {
                Thread lo = new Thread(new LoadObject(currentLine));
                loadObjectThreads.add(lo);                
                lo.start();          
                currentLine = br.readLine();
            }
            br.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void saveCurrentRoom(int direction) {
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
                if(!cLArray[0].equals(""+currentArea)) {
                    sortObject.add(String.join(" ", cLArray));
                }
                currentLine = br.readLine();
            }        
            br.close();
            for(DefaultObject temp : objects) {
                sortObject.add(currentArea+" "+temp.toString());
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
            loadObjectThreads.removeAll(loadObjectThreads);
            if(objects.isEmpty()) {
                if(direction == 0) {
                    currentArea--;
                }
                else {
                    currentArea++;    
                }               
                generateEverything = 0;
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void standard(Graphics2D g2) {
        if(generateEverything == 0) {
            //Music.playTestAreaTheme(1, 0);    
        }      
        if(generateEverything == 0) {
            generate(g2);
        }
        Collections.sort(objects,DefaultObject.defaultObjectCompareLayer);
        for(Ground create : groundTiles) {
            create.create(); //Creates the Rectangle hitboxes 
            create.draw(g2);    
        }
        for(DefaultObject create : objects) {
            create.create();           
            create.action();
            create.draw(g2);
        }
        g2.drawString(""+currentArea,300,200);
        g2.drawString("size of object array: "+objects.size(),300,325);
        Sonic sonic = new Sonic();
        sonic.setup(g2);
    }
    
    public void generate(Graphics2D g2) {
        if(groundTiles.size() < 33) {//limits how many tiles are created (don't want to constantly create tile objects = lag
            //Sending X,Y and angles of tiles I want to create to method
            createTile(GRD_SONICHOUSE_WOODSLOPE,799,444+150,1);
            createTile(GRD_SONICHOUSE_WOODSLOPE,856,386+150,1);
            createTile(GRD_SONICHOUSE_WOODSLOPE,912,327+150,1);
            createTile(GRD_SONICHOUSE_WOODPLANK,975,326+150,1);
            createTile(GRD_SONICHOUSE_WOODSLOPE,1032,327+150,0);
            createTile(GRD_SONICHOUSE_WOODPLANK,1100,383+150,1);  
            //createTile(GRD_SONICHOUSE_WOODPLANK,0,472,1);
            //createTile(GRD_SONICHOUSE_WOODPLANK,0,536,1);
            createTile(GRD_SONICHOUSE_WOODPLANK,0,600,1);
            createTile(GRD_SONICHOUSE_WOODPLANK,1525,600,1);
            createTile(GRD_SONICHOUSE_WOODPLANK,100,300,1);
            for(int i = 0; i < 25; i ++) {
                createTile(GRD_SONICHOUSE_WOODPLANK,0+(i*64),664,1);     
            }
            
        }
        if(objects.isEmpty()) {
            getObject();
        }
        generateEverything = 1;
    }
    public void createTile(GroundType groundType, int xRef, int yRef,int direction) {//Actually creates the Tile Objects and adds it to the arrayList
        groundTiles.add(new Ground(groundType,xRef,yRef,direction));                
    }
    public void createMonitor(MonitorType monitorType, int layer, int xRef, int yRef) {
        objects.add(new Monitor(monitorType, layer,xRef, yRef));
    }
    public void createSign(SignType signType, int layer, int xRef, int yRef) {
        objects.add(new Sign(signType, layer, xRef, yRef));
    }
    public void createNPC(NPCType npcType, int layer, int xRef,int yRef) {
        objects.add(new NPC(npcType,layer,xRef,yRef));
    }
    public void createSpring(SpringType springType, int layer, int xRef,int yRef) {
        objects.add(new Spring(springType,layer,xRef,yRef));
    }
    public ArrayList<Ground> getGroundArrayList() {
        return groundTiles;
    }
    public ArrayList<DefaultObject> getDefaultObjectArrayList() {
        return objects;
    }
    public int getGroupInArray(int index) {
        return objects.get(index).getGroup();
    }
    public void removeObject(int index) {
        objects.remove(index);
    }
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == e.VK_ENTER) {
            saveCurrentRoom(1);
        }
        if(e.getKeyCode() == e.VK_C) {
            saveCurrentRoom(0);
        }
    }
    class LoadObject implements Runnable {
        private String currentLine;
        private boolean isDone;
        public LoadObject(String currentLine) {
            this.currentLine = currentLine;
            this.isDone = false;
        }
        public synchronized void run() {
            if(!isDone) {
                String [] line = currentLine.split(" ");
                if(line[0].equals(String.valueOf(currentArea))) {
                    if(line[1].equals("Monitor:")) {
                        createMonitor(MonitorType.valueOf(line[2]),Integer.valueOf(line[3]),Integer.valueOf(line[4]),Integer.valueOf(line[5]));
                    }
                    else if(line[1].equals("NPC:")) {
                        createNPC(NPCType.valueOf(line[2]),Integer.valueOf(line[3]),Integer.valueOf(line[4]),Integer.valueOf(line[5]));
                    }
                    else if(line[1].equals("Sign:")) {
                        createSign(SignType.valueOf(line[2]),Integer.valueOf(line[3]),Integer.valueOf(line[4]),Integer.valueOf(line[5]));
                    } 
                    else if(line[1].equals("Spring:")) {
                        createSpring(SpringType.valueOf(line[2]),Integer.valueOf(line[3]),Integer.valueOf(line[4]),Integer.valueOf(line[5]));
                    } 
                }    
            }           
            isDone = true;
            System.out.println(""+Thread.currentThread().getName()+ "isDone: "+ isDone);
        }
        
    }
}
