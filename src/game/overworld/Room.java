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
 * This class controls creating and managing rooms- creating all of the 
 * Ground tiles of the room, controls creating the DefaultObjects 
 * needed to be created (sends to SaveLoadObjects class), and runs the room
 * (runs the action methods of the DefaultObjects as well as drawing methods for them).
 * @author GeoSonicDash
 */
public class Room {
    private RoomType roomType;
    private ArrayList<Ground> groundTiles;
    private ArrayList<Map<Integer, Ground>> groundGrid;
    private ArrayList<DefaultObject> objects;
    private ArrayList<Picture> pictures;
    private SaveLoadObjects slo;
    private OverWorld overworld;
    
    /**
     * Creates room Object (it's the constructor of the Room class).
     * @param overworld passed from OverWorld class, used here to send
     * instance of overworld to the DefaultObjects.
     * @param roomType the type of room being created- influences the 
     * DefaultObjects and the Ground tiles that are going to be created in the room.
     */
    public Room(OverWorld overworld, RoomType roomType) {
        this.overworld = overworld;
        this.roomType = roomType;   
        this.groundGrid = new ArrayList<Map<Integer, Ground>>();
        this.groundTiles = new ArrayList<Ground>();
        this.objects = new ArrayList<DefaultObject>();
        this.pictures = new ArrayList<Picture>();
        this.slo = new SaveLoadObjects();
        createRoom();
    }
    
    /**
     * Creates the Ground tiles and DefaultObjects in the room.
     */
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
            //Objects that are supposed to be in room are loaded in SaveLoadObjects and are returned in an ArrayList 
            objects = slo.getObject(overworld, roomType);
        }
    }
    
    /**
     * Runs draw and action methods of DefaultObjects.
     * @param g2 Needed to send to Draw class to draw all the picture objects.
     */
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
    
    /**
     * Creates the Ground Tile within its spot in the groundGrid (in the correct spot
     * on the screen).
     * @param groundType the type of Ground tile being created.
     * @param layer the layer of the Ground tile(important for collision and drawing purposes).
     * @param xRef the x position of the Ground tile (relative to top-left corner).
     * @param yRef the y position of the Ground tile (relative to top-left corner).
     * @param direction the direction of the tile.
     */
    public void createTile(Ground.GroundType groundType, int layer, int xRef, int yRef,int direction) {
        int xIndex = xRef/64;
        int yIndex = yRef/64;
        Ground ground = new Ground(groundType,layer,xRef,yRef,direction);
        /*If the ground Tile's x position is greater than the groundGrid.size()-1,
        meaning that the tile exists outside of the HashMaps that are created already on screen,
        add a new HashMap*/
        if(xIndex > groundGrid.size()-1) {
            groundGrid.add(new HashMap<Integer, Ground>());//X Plane (tiles going across on screen)
        }
        /*Note!- an index has to already exist before a tile is created in that index. For example:
        Let's say I wanted to create a ground Tile at 64,64. I have to make sure a HashMap exists at 64 before
        I create the tile (it will try to add the tile to a HashMap that doesn't exist*/
        groundGrid.get(xIndex).put(yIndex, ground);//Y Plane (tiles going down on screen)
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
    
    /**
     * The type of rooms that are in the game:
     * <ul>
     * <li> {@code ROOM_SONIC_HOUSE} - where the player starts at the beginning of the game. This is where
     * Sonic lives- a one room house.
     * </ul>
     */
    public enum RoomType {
        ROOM_SONIC_HOUSE,
        ROOM_SONIC_TEST
    };
}
