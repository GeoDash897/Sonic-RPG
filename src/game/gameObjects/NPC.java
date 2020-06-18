/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gameObjects;

import game.overworld.Room;
import game.sonic.OWARemastered;

/**Controls NPC implementation- controls NPC behavior (turning, moving, etc).
 *
 * @author GeoSonicDash
 */
public class NPC extends Sign {
    private boolean movable;
    private boolean turnable;
    NPC(Room objectRoom) {
        super(objectRoom);
    } 
    
    /**Sets the variables of specific NPC object (method is called by another class that 
     * extends this one), including its specific text.
     * @param refName the name used to find specific text file with dialog.
     * @param conversation the name of the specific conversation trying to be loaded for 
     * this NPC object.
     * @param movable determines if NPC can move (walk).
     * @param turnable determines if NPC can turn to face Sonic when the player passes NPC.
     */
    public void createNPC(String refName, String conversation, boolean movable, boolean turnable) {
        this.movable = movable;
        this.turnable = turnable;
        //Note!, if you want to access anything from Sign or BasicObject
        //you have to wait for the constructor statements in child NPC classes to run first (ex: SkeletonNPC)
        super.createDialogChain(refName, conversation);        
    }
    @Override
    public void action() {
        super.action();
    }
    
    @Override
    public void interactWithSonic(OWARemastered owaR) {
        super.interactWithSonic(owaR);
        //Controls turning of NPC
        if(turnable && Math.abs(owaR.getXCenterSonic()-super.getXRef()) <= 100) {
            if(super.getDirection() == Direction.DIRECTION_LEFT && owaR.getXCenterSonic() >= super.getXRef()) {
                super.setDirection(Direction.DIRECTION_RIGHT);
            }
            else if(super.getDirection() == Direction.DIRECTION_RIGHT && owaR.getXCenterSonic() < super.getXRef()) {
                super.setDirection(Direction.DIRECTION_LEFT);
            }            
        }       
    }
}
