/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gameObjects;

import game.overworld.Room;
import game.overworld.Room.RoomType;
import game.sonic.OWARemastered;
import java.awt.Image;
import java.awt.Rectangle;

/**
 *
 * @author GeoSonicDash
 */
public class Warp extends BasicObject {
    private int xWarp;
    private int yWarp;
    private WarpType warpType;
    private RoomType pointRoom;
    public Warp(Room objectRoom, WarpType warpType, RoomType pointRoom, int layer, int xRef, int yRef, int xWarp, int yWarp) {
        super(objectRoom);
        this.xWarp = xWarp;
        this.yWarp = yWarp;
        this.warpType = warpType;
        this.pointRoom = pointRoom;
        create(layer, xRef, yRef);
    }
    
    private void create(int layer, int xRef, int yRef) {
        int length = 0;
        int width = 0;
        Rectangle bottomLeft = null;
        Rectangle bottomRight = null; 
        Rectangle intersectBox = null;
        Image picture = null;
        switch(warpType) {
            case WARP_VERTICAL:
                intersectBox = new Rectangle(xRef-32, yRef-400, 64, 800);  
                break;
            default:
                break;
        }
        super.createObject(layer, xRef, yRef, length, width, bottomLeft, bottomRight, intersectBox, false, false, picture);
    }
    
    @Override
    public void interactWithSonic(OWARemastered owaR) {
        if(owaR.getIntersectBox().intersects(super.getIntersectBox())) {
            super.getObjectRoom().getOverWorld().setCurrentRoomType(pointRoom);
            owaR.setXDrawCenterSonic(xWarp);
            if(yWarp != -1) {
                owaR.setYSpriteCenterSonic(yWarp);   
            }            
        }
    }
    
    @Override
    public String toString() {
        return ""+warpType+", "+super.getLayer()+", "+super.getXRef()+", "+super.getYRef()+", "+xWarp+", "+yWarp;
    }
    public enum WarpType {
        WARP_VERTICAL
    }
}
