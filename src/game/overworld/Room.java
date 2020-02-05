/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import game.SaveLoadObjects;
import static game.overworld.Ground.GroundType.GRD_SONICHOUSE_BIGWOODPLANK;
import static game.overworld.Ground.GroundType.GRD_SONICHOUSE_SONICBED;
import static game.overworld.Ground.GroundType.GRD_SONICHOUSE_WOODPLANK;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author GeoSonicDash
 */
public class Room {
    RoomType roomType;
    private ArrayList<Ground> groundTiles;
    private ArrayList<DefaultObject> objects;
    private ArrayList<DefaultObject> pictures;
    private SaveLoadObjects slo = new SaveLoadObjects();
    private OverWorld overworld;
    public Room(OverWorld overworld, RoomType roomType) {
        this.overworld = overworld;
        this.roomType = roomType;       
        groundTiles = new ArrayList<Ground>();
        objects = new ArrayList<DefaultObject>();
        pictures = new ArrayList<DefaultObject>();
        createRoom();
    }
    private void createRoom() {
        if(roomType == RoomType.ROOM_SONIC_HOUSE) {
            for(int i = 0; i < 11; i ++) {
                createTile(GRD_SONICHOUSE_WOODPLANK,1,0,-36+(i*64),1);
            }
            for(int i = 0; i < 24; i ++) {
                createTile(GRD_SONICHOUSE_WOODPLANK,1,64+(i*64),0,1);
            }
            createTile(GRD_SONICHOUSE_BIGWOODPLANK,1,0,664,1);           
            createTile(GRD_SONICHOUSE_SONICBED,1,64,525,1);
        }
        else if(roomType == RoomType.ROOM_SONIC_TEST) {
            createTile(GRD_SONICHOUSE_BIGWOODPLANK,1,0,664,1);          
        }
        if(objects.isEmpty()) {
            objects = slo.getObject(overworld, roomType);
        }
    }
    public void runRoom(Graphics2D g2) {
        for(int i = 0; i < objects.size(); i++) {
            g2.drawString(objects.get(i).toString(),500, 100+(25*i));
        }
        Collections.sort(pictures,DefaultObject.defaultObjectCompareLayer);
        for(Ground create : groundTiles) {
            create.create(); //Creates the Rectangle hitboxes   
        }
        for(DefaultObject obj : objects) {       
            obj.action();
        }
        for(DefaultObject drawIt : pictures) {
            drawIt.draw(g2);
        }
        g2.drawString("size of groundTiles array: "+groundTiles.size(),300,300);
        g2.drawString("size of object array: "+objects.size(),300,325);
        g2.drawString("size of picture array: "+pictures.size(),300,350);
    }
    public void saveRoom() {
        slo.saveCurrentRoom(roomType, groundTiles, objects);
    }
    public void createTile(Ground.GroundType groundType, int layer, int xRef, int yRef,int direction) {//Actually creates the Tile Objects and adds it to the arrayList
        Ground ground = new Ground(groundType,layer,xRef,yRef,direction);
        groundTiles.add(ground);  
        pictures.add(ground);
    }
    public ArrayList<Ground> getGroundArrayList() {
        return groundTiles;
    }
    public ArrayList<DefaultObject> getDefaultObjectArrayList() {
        return objects;
    }
    public ArrayList<DefaultObject> getPictureArrayList() {
        return pictures;
    }
    public String getGroupInArray(int index) {
        return objects.get(index).getGroup();
    }
    public void addObject(DefaultObject add) {
        objects.add(add);
    }
    public void removeObject(int index) {
        objects.remove(index);
    }
    public void addPicture(DefaultObject add) {
        pictures.add(add);
    }
    public void removePicture(int index) {
        pictures.remove(index);
    }
    public RoomType getRoomType() {
        return roomType;
    }
    public enum RoomType {
        ROOM_SONIC_HOUSE,
        ROOM_SONIC_TEST
    };
}
