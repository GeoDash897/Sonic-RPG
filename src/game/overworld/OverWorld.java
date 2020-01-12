/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;
import game.Game;
import game.overworld.Ground.GroundType;
import static game.overworld.Ground.GroundType.GRD_SONICHOUSE_BIGWOODPLANK;
import static game.overworld.Ground.GroundType.GRD_SONICHOUSE_SONICBED;
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
    Author: GeoDash897  Date:10/5/19    Updated:12/31/19
*/
//memes
public class OverWorld extends Game {
    private static ArrayList<Ground> groundTiles = new ArrayList<Ground>();
    private static ArrayList<DefaultObject> objects = new ArrayList<DefaultObject>();
    private static ArrayList<Thread> loadObjectThreads = new ArrayList<Thread>();
    private static boolean generateEverything = false;
    private static int currentRoom = 0;
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
                lo.start();                    
                loadObjectThreads.add(lo);
                currentLine = br.readLine();
            }
            br.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveCurrentRoom(int direction) {
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
                if(!cLArray[0].equals(""+currentRoom)) {
                    sortObject.add(String.join(" ", cLArray));
                }
                currentLine = br.readLine();
            }        
            br.close();
            for(DefaultObject temp : objects) {
                sortObject.add(currentRoom+" "+temp.toString());
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
            groundTiles.removeAll(groundTiles);
            loadObjectThreads.removeAll(loadObjectThreads);
            if(objects.isEmpty()) {
                if(direction == 0) {
                    currentRoom--;
                }
                else {
                    currentRoom++;    
                }               
                generateEverything = false;
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
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
            g2.drawString(""+currentRoom,300,200);
            g2.drawString("size of object array: "+objects.size(),300,325);
            Sonic sonic = new Sonic();
            sonic.setup(g2);    
        }      
    }
    
    public void generate(Graphics2D g2) {    
        if(objects.isEmpty()) {
            getObject();
        }
        if(currentRoom == 0) {
            for(int i = 0; i < 11; i ++) {
                createTile(GRD_SONICHOUSE_WOODPLANK,0,-36+(i*64),1);
            }
            for(int i = 0; i < 24; i ++) {
                createTile(GRD_SONICHOUSE_WOODPLANK,64+(i*64),0,1);
            }
            createTile(GRD_SONICHOUSE_BIGWOODPLANK,0,664,1);           
            createTile(GRD_SONICHOUSE_SONICBED,64,525,1);

        }         
        System.out.println("Everything has been generated");
        generateEverything = true;
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
    public static void addObject(DefaultObject object) {
        objects.add(object);
    }
    public static void removeObject(int index) {
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
        private String name;
        public LoadObject(String currentLine) {
            this.currentLine = currentLine;
            this.isDone = false;
            this.name = "Memes";
        }
        @Override
        public synchronized void run() {
            if(!isDone) {
                String [] line = currentLine.split(" ");
                if(line[0].equals(String.valueOf(currentRoom))) {
                    if(line[1].equals("Monitor:")) {
                        createMonitor(MonitorType.valueOf(line[2]),Integer.valueOf(line[3]),Integer.valueOf(line[4]),Integer.valueOf(line[5]));
                        name = "Monitor In Room Thread";
                    }
                    else if(line[1].equals("NPC:")) {
                        createNPC(NPCType.valueOf(line[2]),Integer.valueOf(line[3]),Integer.valueOf(line[4]),Integer.valueOf(line[5]));
                        name = "NPC In Room Thread";
                    }
                    else if(line[1].equals("Sign:")) {
                        createSign(SignType.valueOf(line[2]),Integer.valueOf(line[3]),Integer.valueOf(line[4]),Integer.valueOf(line[5]));
                        name = "Sign In Room Thread";
                    } 
                    else if(line[1].equals("Spring:")) {
                        createSpring(SpringType.valueOf(line[2]),Integer.valueOf(line[3]),Integer.valueOf(line[4]),Integer.valueOf(line[5]));
                        name = "Spring In Room Thread";
                    } 
                }
                else if(!line[0].equals(String.valueOf(currentRoom))) {
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
