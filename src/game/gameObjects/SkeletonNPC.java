/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gameObjects;

import game.LoadAnimations;
import game.animation.Animation;
import game.animation.Animation.AnimationName;
import static game.animation.Animation.AnimationName.*;
import game.overworld.Room;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Map;

/**Controls SkeletonNPC implementation- controls specific dialogs and animations for SkeletonNPC.
 *
 * @author GeoSonicDash
 */
public class SkeletonNPC extends NPC {
    private int animationFrame;
    private int animationTimer;
    private String loadedDialog;
    private NPCSkeletonType npcType;
    private static Map<AnimationName, Animation> animations;
    private Animation currentAnimation;
    public SkeletonNPC(Room objectRoom, NPCSkeletonType npcType, int layer, int xRef, int yRef) {
        super(objectRoom);
        this.animationFrame = 1;
        this.animationTimer = 1;
        this.npcType = npcType;
        create(layer, xRef, yRef);
    }
    
    /**
     * This method is used to create SkeletonNPC objects ONLY.
     * @param layer layer of the SkeletonNPC object, used to organize object in objectRoom's picture ArrayList (occurs in SaveLoadObjects class).
     * @param xRef the x position of the SkeletonNPC Object.
     * @param yRef the y position of the SkeletonNPC Object.
     */
    private void create(int layer, int xRef, int yRef) {
        int length = 0;
        int width = 0;
        Rectangle bottomLeft = null;
        Rectangle bottomRight = null;
        Image picture = null;
        switch(npcType) {
            case NPC_SKELETON:
                length = 24;
                width = 48;
                bottomLeft = new Rectangle(xRef-32,yRef,4,width*2);
                bottomRight = new Rectangle(xRef+32,yRef,4,width*2);
                if(animations == null) {
                    animations = LoadAnimations.getAnimationMap(String.valueOf(npcType));    
                }                
                currentAnimation = animations.get(ANIMATION_NPCSKELETON_STAND);
                loadedDialog = "MEET";                               
                break;
            default:
                break;
        }
        Rectangle intersectBox = new Rectangle(xRef-(length*2), yRef-(width*2), length*4, width*4);
        //Sets of the variables for the correct classes.
        super.createNPC(String.valueOf(npcType), "MEET", false, true);
        super.createSolidObject(false);
        super.createObject(layer, xRef, yRef, length, width, bottomLeft, bottomRight, intersectBox, false, true, picture);
    }   
    
    @Override
    public void action() {       
        switch(npcType) {
            case NPC_SKELETON:
                //If the current dialog has been completed, load the next conversation
                if(super.justFinishedDialog() && loadedDialog.equals("MEET")) {
                    super.loadDialogChain(String.valueOf(npcType),"RETURN");
                    loadedDialog = "RETURN";
                }
                else if(super.justFinishedDialog() && loadedDialog.equals("RETURN")) {
                    super.loadDialogChain(String.valueOf(npcType),"GREET");
                    loadedDialog = "GREET";
                }
                break;
            default:
                break;
        }
        changeAnimation();
        super.action();
    }   
    
    private void changeAnimation() {
        if(currentAnimation.getNumberOfFrames() != 1) {
            animationTimer++;    
        }       
        if(animationTimer%currentAnimation.getAnimationTimerFrameSet() == 0) {
            animationFrame++;
        }
        if(animationTimer >= (currentAnimation.getAnimationTimerFrameSet()*currentAnimation.getNumberOfFrames())) {
            animationFrame = currentAnimation.getResetAnimationFrame();
            animationTimer = currentAnimation.getResetAnimationTimer();
        }
        /*The animation frame is substract by one to prevent animation frame from going outOfBounds (since the first frame of animation
        is technically at [0]*/
        if(super.getDirection() == Direction.DIRECTION_LEFT) {
          super.setPicture(currentAnimation.getLeftAnimationArray()[animationFrame-1]);  
        }
        else if(super.getDirection() == Direction.DIRECTION_RIGHT) {
            super.setPicture(currentAnimation.getAnimationArray()[animationFrame-1]);
        }
        
    }
    
    @Override
    public String toString() {
        return ""+npcType+", "+super.getLayer()+", "+super.getXRef()+", "+super.getYRef()+", "+super.getDirection();
    }
    
    public enum NPCSkeletonType {
        NPC_SKELETON
    }    
}
