/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gameObjects;

import game.overworld.Room;
import game.overworld.Room.RoomType;
import game.player.BasicOWA;
import game.player.PlayerManager;
import game.player.PlayerManager.OWPosition;
import game.player.mario.MarioOWA;
import game.player.sonic.SonicOWA;
import java.awt.Image;
import java.awt.Rectangle;

/**Controls Warp implementation- controls warping Sonic to another room.
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
    
    /**
     * This method is used to create Warp objects ONLY.
     * @param layer layer of the Warp object, used to organize object in objectRoom's picture ArrayList (occurs in SaveLoadObjects class).
     * @param xRef the x position of the Warp Object.
     * @param yRef the y position of the Warp Object.
     */
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
    public void interactWithMario(MarioOWA owaM) {
        interactWithWarp(owaM);
    }
    
    @Override
    public void interactWithSonic(SonicOWA owaS) {
        interactWithWarp(owaS);
    }
    
    public void interactWithWarp(BasicOWA owaB) {
        /*It sets the currentRoom first (allows the player's picture to be added to the 
        the other Room's picture arrayList), and then changes the player's position*/
        if(owaB.getOWPosition() == OWPosition.OWPOSITION_FRONT && owaB.getIntersectBox().intersects(super.getIntersectBox())) {
            super.getObjectRoom().getOverWorld().setCurrentRoomType(pointRoom);
            if(owaB.getPartner() == null) {
                owaB.setXDrawCenterPlayer(xWarp);
                if(yWarp != -1) {
                    owaB.setYSpriteCenterPlayer(yWarp);   
                }
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
