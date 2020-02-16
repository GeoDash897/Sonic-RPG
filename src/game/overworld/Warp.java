/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author GeoSonicDash
 */
public class Warp implements DefaultObject, Picture {
    String group;
    private WarpType warpType;
    private int layer;
    private int xRef;
    private int yRef;
    private Rectangle hitBox;
    private OverWorld overworld;
    private Room currentRoom;
    public Warp(OverWorld overworld, WarpType warpType, int layer, int xRef, int yRef) {
        this.overworld = overworld;
        this.warpType = warpType;
        this.layer = layer;
        this.xRef = xRef;
        this.yRef = yRef;
        create();
    }
    
    @Override
    public void create() {
        currentRoom = overworld.getCurrentRoom();
        if(warpType == WarpType.WARP_NEXTROOM_FULL) {
            hitBox = new Rectangle(xRef,yRef,25,1080);
        }
    }
    @Override
    public void draw(Graphics2D g2) {
        g2.setColor(Color.red);
        g2.fillRect(xRef,yRef,25,1080);
    }

    @Override
    public void action() {
        
    }

    @Override
    public void interactWithSonic(Rectangle sensor) {
        int positionOfCurrentRoom = overworld.getRoomsArrayList().indexOf(currentRoom);
        if(sensor.intersects(hitBox)) {
            if(warpType == WarpType.WARP_NEXTROOM_FULL) {
                if(overworld.getRoomsArrayList().get(positionOfCurrentRoom+1) != null) {
                    overworld.setCurrentRoomType(overworld.getRoomsArrayList().get(positionOfCurrentRoom+1).getRoomType());
                }
            }    
        }        
    }

    @Override
    public String getGroup() {
        group = String.valueOf(warpType).substring(0,String.valueOf(warpType).indexOf("_"));
        return group;
    }

    @Override
    public int getXRef() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getYRef() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getLength() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public int getWidth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    @Override
    public int getLayer() {
        return layer;
    }
    @Override
    public Rectangle getHitBox() {
        return hitBox;
    }
    @Override
    public boolean isSameLayer(int otherLayer) {
        return otherLayer == this.layer;
    }
    @Override
    public String toString() {
        return "Warp: "+warpType+" "+layer+ " "+xRef+" "+yRef;
    }
    public enum WarpType {
        WARP_NEXTROOM_FULL
    };
}
