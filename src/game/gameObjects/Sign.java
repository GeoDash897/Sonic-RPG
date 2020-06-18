/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gameObjects;

import game.LoadDialogs;
import game.gui.Dialog;
import game.gui.GUI;
import game.input.PlayerInput;
import game.overworld.Room;
import game.sonic.OWARemastered;
import game.sonic.OWARemastered.DuckState;
import game.sonic.OWARemastered.SpindashState;
import game.sonic.Sonic;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**Controls Sign implementation- controls viewing readable objects.
 *
 * @author GeoSonicDash
 */
public class Sign extends SolidObject implements GUI {
    private SignType signType;
    private int dialogIndex;
    private boolean isVisible;
    private boolean justFinishedDialog;
    private LoadDialogs load;
    private ArrayList<Dialog> dialogChain;
    public Sign(Room objectRoom, SignType signType, int layer, int xRef, int yRef) {
        super(objectRoom);
        this.signType = signType; 
        this.justFinishedDialog = false;
        this.dialogIndex = -1;
        this.isVisible = false;
        create(layer, xRef, yRef);
    }
    public Sign(Room objectRoom) {
        super(objectRoom);
    }
    
    /**
     * This method is used to create Sign objects ONLY.
     * @param layer layer of the Sign object, used to organize object in objectRoom's picture ArrayList (occurs in SaveLoadObjects class).
     * @param xRef the x position of the Sign Object.
     * @param yRef the y position of the Sign Object.
     */
    private void create(int layer, int xRef, int yRef) {
        String refName = String.valueOf(signType);
        load = new LoadDialogs(refName);
        dialogChain = load.getDialogChain("READ");
        int length = 0;
        int width = 0;
        Rectangle bottomLeft = null;
        Rectangle bottomRight = null;
        Image picture = null;
        if(signType == SignType.SIGN_TEMP) {
            length = 28;
            width = 32;
            bottomLeft = new Rectangle(xRef-32, yRef, 4, width*2);
            bottomRight = new Rectangle(xRef+32, yRef, 4, width*2);            
            picture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Speed Monitor_2.png");            
        }
        Rectangle intersectBox = new Rectangle(xRef-(length*2), yRef-(width*2), length*4, width*4);
        super.createObject(layer, xRef, yRef, length, width, bottomLeft, bottomRight, intersectBox, false, true, picture);
    }
    /**
     * This method creates the dialogChain for objects that extend the Sign class (for example: NPC class).
     * @param refName The name of the text file in {@code games.resources.dialogs} package that dialog is written in.
     * @param conversation The specific conversation in the text file that is being loaded.
     */
    public void createDialogChain(String refName, String conversation) {
        this.signType = SignType.SIGN_NPC;
        this.dialogIndex = -1;
        this.isVisible = false;
        load = new LoadDialogs(refName);
        loadDialogChain(refName, conversation);
    }
    
    public void loadDialogChain(String refName, String conversation) {
        load.clearSpeechesDialogs();//This empties the previous dialogChain's dialog and speech arrayLists
        dialogChain = load.getDialogChain(conversation);
        justFinishedDialog = false;
        /*The previous dialog (the one stored prior to clearSpeechesDialogs() method ran)
        would have been long done by now, so set boolean to false*/
    }        
    
    @Override
    public void standardGUI() {
        
    }
        
    @Override
    public void drawGUI(Graphics2D g2) {
        if(isVisible) {
            dialogChain.get(dialogIndex).draw(g2);
        }
        g2.setColor(Color.WHITE);
        g2.drawString("dialogIndex :"+dialogIndex, 300, 400);
    }
    
    @Override
    public void action() {
        super.action();
        if(justFinishedDialog) {            
            /*If the dialog has ended and no new one needs to be loaded, set justFinishedDialog to false
            since at this point the dialog exchange would have been over for some time- allows the dialog to restart again after it is over
            */
            justFinishedDialog = false;
        }
    }
         
    @Override
    public void interactWithSonic(OWARemastered owaR) {
        super.interactWithSonic(owaR);
        /*I moved navigating through the dialogs to here instead of action since it would make no sense
        (OWARemastered ins't disabled when dialog is displayed, meaning that this method still runs, making
        there be no reason to have it in action anymore. Also, it is much better to understand)*/
        
        /*First if statement displays the sign's text and disables Sonic's normal movement, the others control
        navigating through the sign's text*/
        if(Sonic.getOWARAllowInput() && owaR.getXSpeed() == 0 && owaR.getDuckState() == DuckState.STATE_NODUCK
                && owaR.getSpindashState() == SpindashState.STATE_NOSPINDASH && owaR.getIntersectBox().intersects(super.getIntersectBox())
                && PlayerInput.checkIsPressedOnce(KeyEvent.VK_X) && dialogIndex == -1 && !justFinishedDialog) {
                isVisible = true;
                Sonic.setOWARAllowInput(false);
                dialogIndex++; 
        }
        else if(dialogIndex >= 0 && dialogIndex < dialogChain.size()-1 && PlayerInput.checkIsPressedOnce(KeyEvent.VK_X)) {               
            dialogIndex++; 
        }
        else if(dialogIndex == dialogChain.size()-1 && PlayerInput.checkIsPressedOnce(KeyEvent.VK_X)) {
            resetConversation();
        }
    }
   
    public void resetConversation() {
        isVisible = false;
        justFinishedDialog = true;//Conversation just ended at this point
        Sonic.setOWARAllowInput(true);
        dialogIndex = -1;
    }
    
    public boolean justFinishedDialog() {
        return justFinishedDialog;
    }
    @Override
    public String toString() {
        return ""+signType+", "+super.getLayer()+", "+super.getXRef()+", "+super.getYRef();
    }
    
    @Override
    public boolean isVisible() {
        return isVisible;
    }
        
    public enum SignType {
        SIGN_TEMP,
        SIGN_NPC
    }
}
