/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import game.SaveLoadObjects;
import static game.overworld.Ground.GroundType.GRD_SONICHOUSE_BIGWOODPLANK;
import static game.overworld.Ground.GroundType.GRD_SONICHOUSE_WOODPLANK;
import static game.overworld.Ground.GroundType.GRD_SONICHOUSE_WOODSLOPE;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author GeoSonicDash
 */
public class Room {
    RoomType roomType;
    private ArrayList<Ground> groundTiles;
    private ArrayList<Map<Integer, Ground>> groundGrid;
    private ArrayList<DefaultObject> objects;
    private ArrayList<Picture> pictures;
    private SaveLoadObjects slo = new SaveLoadObjects();
    private OverWorld overworld;
    public Room(OverWorld overworld, RoomType roomType) {
        this.overworld = overworld;
        this.roomType = roomType;   
        this.groundGrid = new ArrayList<Map<Integer, Ground>>();
        this.groundTiles = new ArrayList<Ground>();
        this.objects = new ArrayList<DefaultObject>();
        this.pictures = new ArrayList<Picture>();
        createRoom();
    }
    private void createRoom() {
        if(roomType == RoomType.ROOM_SONIC_HOUSE) {
            for(int i = 0; i < 24; i ++) {
                createTile(GRD_SONICHOUSE_WOODPLANK,1,0+(i*64),0,1);
            }
            for(int i = 0; i < 11; i ++) {
                createTile(GRD_SONICHOUSE_WOODPLANK,1,0,0+(i*64),1);
            }  
            for(int i = 0; i < 24; i ++) {
                createTile(GRD_SONICHOUSE_WOODPLANK,1,0+(i*64),704,1);
            }    
            for(int i = 0; i < 24; i ++) {
                createTile(GRD_SONICHOUSE_WOODPLANK,1,0+(i*64),768,1);
            }
            //createTile(GRD_SONICHOUSE_SONICBED,1,64,525,1);
            createTile(GRD_SONICHOUSE_WOODSLOPE, 1, 576, 640, 1);
            createTile(GRD_SONICHOUSE_WOODSLOPE, 1, 640, 576, 1);
            createTile(GRD_SONICHOUSE_WOODSLOPE, 1, 704, 512, 1);
            createTile(GRD_SONICHOUSE_WOODPLANK,1,576,704,1);
            createTile(GRD_SONICHOUSE_WOODPLANK,1,640,640,1);
            createTile(GRD_SONICHOUSE_WOODPLANK,1,704,576,1);
            createTile(GRD_SONICHOUSE_WOODPLANK,1,768,512,1);
        }
        else if(roomType == RoomType.ROOM_SONIC_TEST) {
            createTile(GRD_SONICHOUSE_BIGWOODPLANK,1,0,664,1);          
        }
        if(objects.isEmpty()) {
            objects = slo.getObject(overworld, roomType);
        }
    }
    public void runRoom(Graphics2D g2) {
        /*for(int i = 0; i < objects.size(); i++) {
            g2.drawString(objects.get(i).toString(),500, 100+(25*i));
        } */    
        for(DefaultObject obj : objects) {       
            obj.action();
        }          
        Draw.drawInLayers(g2, pictures);
    }
    public void saveRoom() {
        slo.saveCurrentRoom(roomType, groundTiles, objects);
    }
    public void createTile(Ground.GroundType groundType, int layer, int xRef, int yRef,int direction) {//Actually creates the Tile Objects and adds it to the arrayList
        int xIndex = xRef/64;
        int yIndex = yRef/64;
        Ground ground = new Ground(groundType,layer,xRef,yRef,direction);
        if(xIndex > groundGrid.size()-1) {//X Plane
            groundGrid.add(new HashMap<Integer, Ground>());
        }
        groundGrid.get(xIndex).put(yIndex, ground);//Y Plane
        groundTiles.add(ground);  
        pictures.add(ground);
    }
    public ArrayList<Map<Integer, Ground>> getGroundGridArrayList() {
        return groundGrid;
    }
    public ArrayList<Ground> getGroundArrayList() {
        return groundTiles;
    }
    public ArrayList<DefaultObject> getDefaultObjectArrayList() {
        return objects;
    }
    public ArrayList<Picture> getPictureArrayList() {
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
    public void addPicture(Picture add) {
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
