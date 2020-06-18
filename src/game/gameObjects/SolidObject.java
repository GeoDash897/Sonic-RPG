/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gameObjects;

import game.overworld.Ground;
import game.overworld.Room;
import game.sonic.OWARemastered;
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
    public void interactWithSonic(OWARemastered owaR) {
        if(isSolid) {
            collisionCheck(owaR);
        }
    }
    
    public void collisionCheck(OWARemastered owaR) {
        Rectangle middleLeft = owaR.getMiddleLeft();
        Rectangle middleRight = owaR.getMiddleRight();
        int xMiddleLeft = (int) middleLeft.getX();
        int xMiddleRight = (int) (middleRight.getX()+middleRight.getWidth());
        if(xMiddleLeft < (int) (super.getIntersectBox().getX()+super.getIntersectBox().getWidth())+4 && middleLeft.intersects(super.getIntersectBox())
                && owaR.getXSpeed() < 0) {      
            owaR.setCollideLeftStats(super.getIntersectBox());
        }
        else if(xMiddleRight > (int) super.getIntersectBox().getX() && middleRight.intersects(super.getIntersectBox())
                && middleRight.intersects(super.getIntersectBox()) && owaR.getXSpeed() > 0) {      
            owaR.setCollideRightStats(super.getIntersectBox());
        }
        else {
            bottomCollision(owaR);
        }
    }
    public void middleCollision(OWARemastered owaR) {
        Rectangle middleLeft = owaR.getMiddleLeft();
        Rectangle middleRight = owaR.getMiddleRight();
        int xMiddleLeft = (int) middleLeft.getX();
        int xMiddleRight = (int) (middleRight.getX()+middleRight.getWidth());
        if(xMiddleLeft < (int) (super.getIntersectBox().getX()+super.getIntersectBox().getWidth())+4 && middleLeft.intersects(super.getIntersectBox())
                && owaR.getXSpeed() < 0) {      
            owaR.setCollideLeftStats(super.getIntersectBox());
        }
        else if(xMiddleRight > (int) super.getIntersectBox().getX() && middleRight.intersects(super.getIntersectBox())
                && middleRight.intersects(super.getIntersectBox()) && owaR.getXSpeed() > 0) {      
            owaR.setCollideRightStats(super.getIntersectBox());
        }
    }
       
    public void bottomCollision(OWARemastered owaR) {
        if(owaR.getBottomLeft().intersects(super.getIntersectBox())) {
            owaR.setSonicBottomGameStat(owaR.getBottomLeft(), super.getIntersectBox());
            int bLDistanceFromRect = getDistanceFromGround(owaR, owaR.getBottomLeft());
            owaR.setBLDistanceFromRect(bLDistanceFromRect);
        }
        if(owaR.getBottomRight().intersects(super.getIntersectBox())) {
            owaR.setSonicBottomGameStat(owaR.getBottomRight(), super.getIntersectBox());
            int bRDistanceFromRect = getDistanceFromGround(owaR, owaR.getBottomRight());
            owaR.setBLDistanceFromRect(bRDistanceFromRect);
        }
        owaR.getLedgeGameObject(super.getIntersectBox());
    } 
    
    /**Gets the correct distance from the Ground tile-used in triggering Sonic's ledge animation.
     * 
     * @param owaR object of {@code OWARemastered} class.
     * @param sensor one of Sonic's bottom sensors (either bottomLeft or bottomRight) that is being checked.
     * It determines which distanceFromRect value is being found.
     * @return the distance from the sensor being checked to the nearest Ground tile (maximum distance being 64).
     */
    private int getDistanceFromGround(OWARemastered owaR, Rectangle sensor) {
        int xBottomSensor = 0;
        int yBottomSensor = (int) (sensor.getY()+80);
        Ground tile = null;       
        if(sensor == owaR.getBottomLeft()) {
            xBottomSensor = (int) owaR.getBottomLeft().getX();
            tile = super.getCorrectTile(yBottomSensor, owaR.getBottomLeft());
        }
        else if(sensor == owaR.getBottomRight()) {
            xBottomSensor = (int) owaR.getBottomRight().getX();
            tile = super.getCorrectTile(yBottomSensor, owaR.getBottomRight());
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
