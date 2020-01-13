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
    private SaveLoadObjects slo = new SaveLoadObjects();
    public Room(RoomType roomType) {
        this.roomType = roomType;
        groundTiles = new ArrayList<Ground>();
        objects = new ArrayList<DefaultObject>();
        createRoom();
    }
    private void createRoom() {
        if(roomType == RoomType.ROOM_SONIC_HOUSE) {
            for(int i = 0; i < 11; i ++) {
                createTile(GRD_SONICHOUSE_WOODPLANK,0,-36+(i*64),1);
            }
            for(int i = 0; i < 24; i ++) {
                createTile(GRD_SONICHOUSE_WOODPLANK,64+(i*64),0,1);
            }
            createTile(GRD_SONICHOUSE_BIGWOODPLANK,0,664,1);           
            createTile(GRD_SONICHOUSE_SONICBED,64,525,1);
        }
        else if(roomType == RoomType.ROOM_SONIC_TEST) {
            for(int i = 0; i < 20; i ++ ) {
                createTile(GRD_SONICHOUSE_BIGWOODPLANK,0,664,1);
            }
        }
        if(objects.isEmpty()) {
            objects = slo.getObject(roomType);
        }
    }
    public void runRoom(Graphics2D g2) {
        for(int i = 0; i < objects.size(); i++) {
            g2.drawString(objects.get(i).toString(),500, 100+(25*i));
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
        g2.drawString("size of groundTiles array: "+groundTiles.size(),300,300);
        g2.drawString("size of object array: "+objects.size(),300,325);
    }
    public void saveRoom() {
        slo.saveCurrentRoom(roomType, groundTiles, objects);
    }
    public void createTile(Ground.GroundType groundType, int xRef, int yRef,int direction) {//Actually creates the Tile Objects and adds it to the arrayList
        groundTiles.add(new Ground(groundType,xRef,yRef,direction));                
    }
    public ArrayList<Ground> getGroundArrayList() {
        return groundTiles;
    }
    public ArrayList<DefaultObject> getDefaultObjectArrayList() {
        return objects;
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
    public RoomType getRoomType() {
        return roomType;
    }
    public enum RoomType {
        ROOM_SONIC_HOUSE,
        ROOM_SONIC_TEST
    };
}
