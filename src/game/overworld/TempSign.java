/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import game.LoadDialogs;
import game.gui.Dialog;
import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author GeoSonicDash
 */
public class TempSign implements Picture {
    private int xRef;
    private int yRef;
    private SignType signType;
    private int layer;
    private int dialogIndex;
    private boolean isVisible;
    private LoadDialogs load;
    private ArrayList<Dialog> dialogChain;
    public TempSign(SignType signType, int layer, int xRef, int yRef) {
        this.signType = signType;
        this.layer = layer;
        this.xRef = xRef;
        this.yRef = yRef;  
        this.dialogIndex = -1;
        this.isVisible = false;
        create();
    }

    private void create() {
        String refName = String.valueOf(signType);
        load = new LoadDialogs(refName);
        dialogChain = load.getDialogChain();
    }
    @Override
    public void draw(Graphics2D g2) {
        if(isVisible) {
            dialogChain.get(dialogIndex).draw(g2);
        }
    }
    
    public void interactWithSonic() {

    }
    
    @Override
    public int getLayer() {
        return layer;
    }
        
    public enum SignType {
        SIGN_TEMP
    }
}
