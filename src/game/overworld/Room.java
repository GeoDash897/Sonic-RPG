/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import game.SaveLoadObjects;
import game.gameObjects.BasicObject;
import static game.overworld.Ground.GroundType.*;
import game.gui.GUI;
import game.overworld.OverWorld.AreaName;
import game.randombattle.RandomBattle;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class controls creating and managing a {@code Room}- a single over world screen
 * in the game.
 * It stores and maintains the {@code Ground} tiles and gameObjects of the room, as well as
 * runs the logic of the room (runs the action methods of the gameObjects as well as drawing methods for them).
 * @author GeoSonicDash
 */
public class Room {
    private RoomType roomType;
    private AreaName area;
    
    private ArrayList<Map<Integer, Ground>> groundGrid;
    private ArrayList<Picture> pictures;
    private ArrayList<GUI> guis;
    private ArrayList<BasicObject> gameObjects;
    
    private SaveLoadObjects slo;
    private OverWorld overworld;   
    private RandomBattle randomBattle;
    
    private static Draw draw;
    
    /**
     * Creates room Object (it's the constructor of the Room class).
     * @param overworld passed from OverWorld class, used here to send
     * instance of overworld to the DefaultObjects.
     * @param area the name of the area that the {@code Room} belongs to.
     * @param roomType the type of room being created- influences the 
     * DefaultObjects and the Ground tiles that are going to be created in the room.
     */
    public Room(OverWorld overworld, AreaName area, RoomType roomType) {
        this.overworld = overworld;
        this.roomType = roomType;   
        this.area = area;
        this.groundGrid = new ArrayList<Map<Integer, Ground>>();
        this.pictures = new ArrayList<Picture>();
        this.guis = new ArrayList<GUI>();
        this.gameObjects = new ArrayList<BasicObject>();
        this.slo = new SaveLoadObjects();  
        this.randomBattle = null;
        if(draw == null) {
            draw = new Draw();              
        }       
        createRoom();
    }
    
    /**
     * Creates the Ground tiles and DefaultObjects in the room.
     */
    private void createRoom() {
        pictures.add(new Background(roomType.toString()));
        if(roomType == RoomType.MEDIVAL_SONIC_HOUSE) {
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
            createTile(GRD_SONICHOUSE_SONICBED_00,1,64,576,1);
            createTile(GRD_SONICHOUSE_SONICBED_01,1,64,640,1); 
            createTile(GRD_SONICHOUSE_SONICBED_10,1,128,576,1);  
            createTile(GRD_SONICHOUSE_SONICBED_11,1,128,640,1);
            createTile(GRD_SONICHOUSE_SONICBED_20,1,192,576,1);  
            createTile(GRD_SONICHOUSE_SONICBED_21,1,192,640,1);
            createTile(GRD_SONICHOUSE_SONICBED_30,1,256,576,1);
            createTile(GRD_SONICHOUSE_SONICBED_31,1,256,640,1);
        }
        else if(roomType == RoomType.MEDIVAL_SONIC_TEST) {
            for(int i = 0; i < 24; i ++) {
                createTile(GRD_SONICHOUSE_WOODPLANK,1,0+(i*64),704,1);
            }
            for(int i = 0; i < 11; i ++) {
                createTile(GRD_SONICHOUSE_WOODPLANK,1,1472,0+(i*64),1);
            }
            createTile(GRD_SONICHOUSE_WOODSLOPE,1,384,640,1);           
            createTile(GRD_SONICHOUSE_WOODPLANK,1,448,640,1);
            createTile(GRD_SONICHOUSE_WOODSLOPE,1,448,576,1);  
            createTile(GRD_SONICHOUSE_WOODPLANK,1,512,576,1); 
            createTile(GRD_SONICHOUSE_WOODSLOPE,1,512,512,1);
            createTile(GRD_SONICHOUSE_WOODPLANK,1,576,512,1);
            createTile(GRD_SONICHOUSE_WOODPLANK,1,640,512,1);
            createTile(GRD_SONICHOUSE_WOODPLANK,1,704,512,1);
            createTile(GRD_SONICHOUSE_WOODSLOPE,1,768,512,0);    
            createTile(GRD_SONICHOUSE_WOODPLANK,1,768,576,1);
            createTile(GRD_SONICHOUSE_WOODSLOPE,1,832,576,0);
            createTile(GRD_SONICHOUSE_WOODPLANK,1,832,640,1);
            createTile(GRD_SONICHOUSE_WOODSLOPE,1,896,640,0);
        }
        SaveLoadObjects.createGameObjectArrayList("Area1", this);
    }
    
    /**
     * Runs the action methods of DefaultObjects.
     */
    public void runRoom() {
        if(randomBattle == null) {
            randomBattle = new RandomBattle(this);
            randomBattle.firstTimeSetUp();
        }
        /*for(BasicObject temp : gameObjects) {
            temp.action();
        }
        for(GUI temp : guis) {
            if(temp.isVisible()) {
               temp.standardGUI();
            }
        }*/
        randomBattle.battleStandard();
    }
    
    /**
     * Draws all of the game elements in the current Room (Ground tiles, gameObjects, Sonic)
     * @param g2 {@code Graphics2D} object needed for drawing.
     */
    public void drawRoom(Graphics2D g2) {
        /*for(int i = 0; i < gameObjects.size(); i++) {
            g2.drawString(gameObjects.get(i).toString(),500, 100+(25*i));
        }
        g2.setColor(Color.MAGENTA);
        for(int i = 0; i < 24; i++) {
            for(int j = 0; j < 24; j++) {
                g2.drawRect(0+(i*64), 0+(j*64), 64, 64);
            }
        }             
        Draw.drawInLayers(g2, pictures, guis);*/
        randomBattle.draw(g2);
    }
    
    public void saveRoom() {
        
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
        Let's say I wanted to create a ground Tile at 64,64. I have to make sure a HashMap exists at 64 (or index 1 of ArrayList) before
        I create the tile (it will try to add the tile to a HashMap that doesn't exist*/
        groundGrid.get(xIndex).put(yIndex, ground);//Y Plane (tiles going down on screen)
        pictures.add(ground);
    }
    
    public ArrayList<Map<Integer, Ground>> getGroundGridArrayList() {
        return groundGrid;
    } 
    
    public ArrayList<BasicObject> getGameObjectArrayList() {
        return gameObjects;
    }
    
    public ArrayList<Picture> getPictureArrayList() {
        return pictures;
    }
    
    public void addGameObject(BasicObject add) {
        gameObjects.add(add);
    }
    
    public void removeGameObject(BasicObject index) {
        gameObjects.remove(index);
    }
    
    public void addPicture(Picture add) {
        /*This checks to make sure sonic's picture isn't added more than once into the room's guiAL,
        if the sonic's picture isn't in it (meaning the player didn't enter it yet), add it
        I don't want to clear it out since it would be redunant (why clear out all of the pictures, just
        to add them again if the player returned to a previous room).
        This is also a good failsafe if something is accidently added again*/
        boolean check = false;
        for(int i = 0; i < pictures.size(); i++) {
            if(add == pictures.get(i)) {
                check = true;
            }
        }
        if(!check) {
            pictures.add(add);
        }       
    }
    
    public void removePicture(Picture index) {
        pictures.remove(index);
    }
    
    public void addGUI(GUI add) {
        /*This checks to make sure playerMenu isn't added more than once into the room's guiAL,
        if the playerMenu isn't in it (meaning the player didn't enter it yet), add it
        I don't want to clear it out since it would be redunant (why clear out all of the GUIS, just
        to add them again if the player returned to a previous room).
        This is also a good failsafe if something is accidently added again*/
        boolean check = false;
        for(int i = 0; i < guis.size(); i++) {
            if(add == guis.get(i)) {
                check = true;
            }
        }
        if(!check) {
            guis.add(add);
        }       
    }
    
    public RandomBattle getRandomBattle() {
        return randomBattle;
    }
    
    public AreaName getAreaName() {
        return area;
    }
    
    public RoomType getRoomType() {
        return roomType;
    }
    
    public OverWorld getOverWorld() {
        return overworld;
    }
    
    /**
     * The type of rooms that are in the game:
     * <ul>
     * <li> {@code MEDIVAL_SONIC_HOUSE} - where the player starts at the beginning of the game. This is where
     * Sonic lives- a one room house.
     * </li>
     * </ul>
     */
    public enum RoomType {
        MEDIVAL_SONIC_HOUSE,
        MEDIVAL_SONIC_TEST
    };
}
