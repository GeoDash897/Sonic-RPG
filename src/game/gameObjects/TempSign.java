/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gameObjects;

import game.LoadDialogs;
import game.gui.Dialog;
import game.input.PlayerInput;
import game.overworld.Room;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 *
 * @author GeoSonicDash
 */
public class TempSign extends BasicObject {
    private SignType signType;
    private int dialogIndex;
    private boolean isVisible;
    private LoadDialogs load;
    private ArrayList<Dialog> dialogChain;
    public TempSign(Room objectRoom, SignType signType, int layer, int xRef, int yRef) {
        super(objectRoom);
        this.signType = signType; 
        this.dialogIndex = -1;
        this.isVisible = false;
        create(layer, xRef, yRef);
    }
    private void create(int layer, int xRef, int yRef) {
        String refName = String.valueOf(signType);
        load = new LoadDialogs(refName);
        dialogChain = load.getDialogChain();
        int length = 0;
        int width = 0;
        Rectangle bottomLeft = null;
        Rectangle bottomRight = null;
        Image picture = null;
        if(signType == SignType.SIGN_TEMP) {
            length = 28;
            width = 32;
            bottomLeft = new Rectangle(xRef+((length*4)/2)-32, yRef+((width*4)/2), 4, ((width*4)/2)+8);
            bottomRight = new Rectangle(xRef+((length*4)/2)+32, yRef+((width*4)/2), 4, ((width*4)/2)+8);
            picture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Speed Monitor 1.png");            
        }
        super.createObject(layer, xRef, yRef, length, width, bottomLeft, bottomRight, false, true, picture);
    }
       
    @Override
    public void draw(Graphics2D g2) {
        if(isVisible) {
            dialogChain.get(dialogIndex).draw(g2);
        }
        super.draw(g2);
    }
    
    public void interactWithSonic(Rectangle sensor) {
        if(sensor.intersects(super.getSensor()) && PlayerInput.checkIsPressedOnce(KeyEvent.VK_X)) {
            if(dialogIndex == -1) {
                isVisible = true;
            }
            dialogIndex++;
            if(dialogIndex == dialogChain.size()) {
                isVisible = false;
                dialogIndex = -1;
            }
        }
    }
        
    public enum SignType {
        SIGN_TEMP
    }
}
