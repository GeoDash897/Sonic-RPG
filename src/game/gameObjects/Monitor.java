/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gameObjects;

import game.overworld.Room;
import game.sonic.OWARemastered;
import game.sonic.OWARemastered.DuckState;
import game.sonic.OWARemastered.JumpState;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

/**
 *
 * @author GeoSonicDash
 */
public class Monitor extends SolidObject {
    private MonitorType monitorType;
    public Monitor(Room objectRoom, MonitorType monitorType, int layer, int xRef, int yRef) {
        super(objectRoom);
        this.monitorType = monitorType;
        createMonitor(layer, xRef, yRef);
    }

    private void createMonitor(int layer, int xRef, int yRef) {
        int length = 0;
        int width = 0;
        Rectangle bottomLeft = null;
        Rectangle bottomRight = null;        
        Image picture = null;
        switch(monitorType) {
            case MONITOR_RING:
                length = 28;
                width = 32;
                bottomLeft = new Rectangle(xRef+((length*4)/2)-32, yRef+((width*4)/2), 4, ((width*4)/2)+8);
                bottomRight = new Rectangle(xRef+((length*4)/2)+32, yRef+((width*4)/2), 4, ((width*4)/2)+8);
                picture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Ring Monitor 2.png");
                break;
            default:
                break;
        }
        Rectangle intersectBox = new Rectangle(xRef, yRef, length*4, width*4);
        super.createSolidObject(true);
        super.createObject(layer, xRef, yRef, length, width, bottomLeft, bottomRight, intersectBox, false, true, picture);
    }
    
    @Override
    public void action() {
        super.action();
    }
    
    @Override
    public void interactWithSonic(OWARemastered owaR) {        
        if(owaR.getDuckState() != DuckState.STATE_ROLL && owaR.getJumpState() == JumpState.STATE_NOJUMP) {
            super.interactWithSonic(owaR);    
        }
        else if(owaR.getDuckState() == DuckState.STATE_ROLL || owaR.getJumpState() != JumpState.STATE_NOJUMP) {
            if(owaR.getMiddleLeft().intersects(super.getIntersectBox())) {
                owaR.setYSpeed(-owaR.getYSpeed());
                super.getObjectRoom().removeGameObject(this);
                super.getObjectRoom().removePicture(this);
            }
        }
    }
    
    @Override
    public String toString() {
        return ""+monitorType+", "+super.getLayer()+", "+super.getXRef()+", "+super.getYRef();
    }
    
    public enum MonitorType {
        MONITOR_RING,
        MONITOR_SPEED
    }
}
