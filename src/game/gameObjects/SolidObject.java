/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gameObjects;

import game.overworld.Ground;
import game.overworld.Room;
import game.player.BasicOWA;
import game.player.mario.MarioOWA;
import game.player.sonic.SonicOWA;
import java.awt.Rectangle;

/**Controls SolidObject implementation- controls collisions with Sonic.
 *
 * @author GeoSonicDash
 */
public class SolidObject extends BasicObject {
    private boolean isSolid;
    public SolidObject(Room objectRoom) {
        super(objectRoom);
    }
    public void createSolidObject(boolean isSolid) {
        this.isSolid = isSolid;
    }

    @Override
    public void interactWithMario(MarioOWA owaM) {
        if(isSolid) {
            collisionCheck(owaM);
        }
    }
    
    @Override
    public void interactWithSonic(SonicOWA owaS) {
        if(isSolid) {
            collisionCheck(owaS);
        }
    }
    
    public void collisionCheck(BasicOWA owaB) {
        Rectangle middleLeft = owaB.getMiddleLeft();
        Rectangle middleRight = owaB.getMiddleRight();
        int xMiddleLeft = (int) middleLeft.getX();
        int xMiddleRight = (int) (middleRight.getX()+middleRight.getWidth());
        if(xMiddleLeft < (int) (super.getIntersectBox().getX()+super.getIntersectBox().getWidth())+4 && middleLeft.intersects(super.getIntersectBox())
                && owaB.getXSpeed() < 0) {      
            owaB.setCollideLeftStats(super.getIntersectBox());
        }
        else if(xMiddleRight > (int) super.getIntersectBox().getX() && middleRight.intersects(super.getIntersectBox())
                && middleRight.intersects(super.getIntersectBox()) && owaB.getXSpeed() > 0) {      
            owaB.setCollideRightStats(super.getIntersectBox());
        }
        else {
            bottomCollision(owaB);
        }
    }
    public void middleCollision(BasicOWA owaB) {
        Rectangle middleLeft = owaB.getMiddleLeft();
        Rectangle middleRight = owaB.getMiddleRight();
        int xMiddleLeft = (int) middleLeft.getX();
        int xMiddleRight = (int) (middleRight.getX()+middleRight.getWidth());
        if(xMiddleLeft < (int) (super.getIntersectBox().getX()+super.getIntersectBox().getWidth())+4 && middleLeft.intersects(super.getIntersectBox())
                && owaB.getXSpeed() < 0) {      
            owaB.setCollideLeftStats(super.getIntersectBox());
        }
        else if(xMiddleRight > (int) super.getIntersectBox().getX() && middleRight.intersects(super.getIntersectBox())
                && middleRight.intersects(super.getIntersectBox()) && owaB.getXSpeed() > 0) {      
            owaB.setCollideRightStats(super.getIntersectBox());
        }
    }
       
    public void bottomCollision(BasicOWA owaB) {
        if(owaB.getBottomLeft().intersects(super.getIntersectBox())) {
            owaB.setSonicBottomGameStat(owaB.getBottomLeft(), super.getIntersectBox());
            int bLDistanceFromRect = getDistanceFromGround(owaB, owaB.getBottomLeft());
            owaB.setBLDistanceFromRect(bLDistanceFromRect);
        }
        if(owaB.getBottomRight().intersects(super.getIntersectBox())) {
            owaB.setSonicBottomGameStat(owaB.getBottomRight(), super.getIntersectBox());
            int bRDistanceFromRect = getDistanceFromGround(owaB, owaB.getBottomRight());
            owaB.setBLDistanceFromRect(bRDistanceFromRect);
        }
        owaB.getLedgeGameObject(super.getIntersectBox());
    } 
    
    /**Gets the correct distance from the Ground tile-used in triggering Sonic's ledge animation.
     * 
     * @param owaR object of {@code BasicOWA} class.
     * @param sensor one of Sonic's bottom sensors (either bottomLeft or bottomRight) that is being checked.
     * It determines which distanceFromRect value is being found.
     * @return the distance from the sensor being checked to the nearest Ground tile (maximum distance being 64).
     */
    private int getDistanceFromGround(BasicOWA owaB, Rectangle sensor) {
        int xBottomSensor = 0;
        int yBottomSensor = (int) (sensor.getY()+80);
        Ground tile = null;       
        if(sensor == owaB.getBottomLeft()) {
            xBottomSensor = (int) owaB.getBottomLeft().getX();
            tile = super.getCorrectTile(yBottomSensor, owaB.getBottomLeft());
        }
        else if(sensor == owaB.getBottomRight()) {
            xBottomSensor = (int) owaB.getBottomRight().getX();
            tile = super.getCorrectTile(yBottomSensor, owaB.getBottomRight());
        }
        if(tile != null) {
            int heightIndex = (int) Math.abs(((xBottomSensor - tile.getXRef())/4));    
            Rectangle groundCheckL = tile.getPixelBox(heightIndex);
            int bLDistanceFromRect = Math.abs(yBottomSensor - (int) groundCheckL.getY());
            return bLDistanceFromRect;
        }        
        return 64;
    }
}
