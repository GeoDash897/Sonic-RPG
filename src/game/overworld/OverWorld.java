/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;
import game.Game;
import game.Music;
import game.sonic.*;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
/*
    Author: GeoDash897  Date:10/5/19    Updated:10/5/19
*/
//memes
public class OverWorld extends Game {
    private static ArrayList<Ground> groundTiles = new ArrayList<Ground>();
    private static ArrayList<DefaultObject> objects = new ArrayList<DefaultObject>();
    private static int generateEverything = 0;
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
            create.draw(g2);
            create.action();
        }
        Sonic sonic = new Sonic();
        sonic.setup(g2);
    }
    public void generate(Graphics2D g2) {
        if(groundTiles.size() < 33) {//limits how many tiles are created (don't want to constantly create tile objects = lag
            //Sending X,Y and angles of tiles I want to create to method
            createTile(0,799,444+150,16,16,45,1);
            createTile(0,856,386+150,16,16,45,1);
            createTile(0,912,327+150,16,16,45,1);
            createTile(0,975,326+150,16,16,0,1);
            createTile(0,1032,327+150,16,16,45,0);
            createTile(0,1100,383+150,16,16,0,1);
            createTile(0,0,664,1400,32,0,1);   
            createTile(0,0,472,16,48,0,1);
            createTile(0,1525,600,16,16,0,1);
            createTile(0,100,300,16,16,0,1);
        }
        if(objects.size() < 3) {
            createMonitor(0,1,400,300);
            createMonitor(0,1,600,300);
            createSign(0,1,150,600);
            createNPC(0,1,400,300);
        }      
        generateEverything = 1;
    }
    public void createTile(int id, int xRef, int yRef, int length, int width, int angleOfTile, int direction) {//Actually creates the Tile Objects and adds it to the arrayList
        groundTiles.add(new Ground(id,xRef,yRef,length,width, angleOfTile, direction));                
    }
    public void createMonitor(int id, int layer, int xRef, int yRef) {
        objects.add(new Monitor(id, layer,xRef, yRef));
    }
    public void createSign(int id, int layer, int xRef, int yRef) {
        objects.add(new Sign(id, layer, xRef, yRef));
    }
    public void createNPC(int id, int layer, int xRef,int yRef) {
        objects.add(new NPC(id,layer,xRef,yRef));
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
    public int getIDInArray(int index) {
        return objects.get(index).getID();
    }
    public void removeObject(int index) {
        objects.remove(index);
    }
}
