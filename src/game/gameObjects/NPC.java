/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gameObjects;

import game.overworld.Room;
import game.sonic.OWARemastered;

/**
 *
 * @author GeoSonicDash
 */
public class NPC extends Sign {
    private int middleOfNPC;
    private boolean movable;
    private boolean turnable;
    NPC(Room objectRoom) {
        super(objectRoom);
    }      
    public void createNPC(String refName, String conversation, boolean movable, boolean turnable) {
        this.movable = movable;
        this.turnable = turnable;
        this.middleOfNPC = -999999;//Note!, if you want to access anything from Sign or BasicObject
        //you have to wait for the constructor statements in child NPC classes to run first (ex: SkeletonNPC)
        super.createDialogChain(refName, conversation);        
    }
    @Override
    public void action() {
        super.action();
        if(middleOfNPC == -999999) {
            this.middleOfNPC = super.getXRef()+(super.getLength()/2);
        }
    }
    
    @Override
    public void interactWithSonic(OWARemastered owaR) {
        super.interactWithSonic(owaR);
        if(turnable && Math.abs(owaR.getXCenterSonic()-middleOfNPC) <= 100) {
            if(super.getDirection() == Direction.DIRECTION_LEFT && owaR.getXCenterSonic() >= middleOfNPC) {
                super.setDirection(Direction.DIRECTION_RIGHT);
            }
            else if(super.getDirection() == Direction.DIRECTION_RIGHT && owaR.getXCenterSonic() < middleOfNPC) {
                super.setDirection(Direction.DIRECTION_LEFT);
            }            
        }       
    }
}
