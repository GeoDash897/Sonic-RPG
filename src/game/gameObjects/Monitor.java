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
import game.player.BasicOWA.JumpState;
import game.player.mario.MarioOWA;
import game.player.mario.MarioOWA.HammerState;
import game.player.sonic.SonicOWA;
import game.player.sonic.SonicOWA.RollState;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Map;

/**Controls implementation of Monitor gameObject.
 *
 * @author GeoSonicDash
 */
public class Monitor extends SolidObject {
    private int animationFrame;
    private int animationTimer;
    private MonitorType monitorType;
    private Map<AnimationName, Animation> animations;
    private Animation currentAnimation;
    public Monitor(Room objectRoom, MonitorType monitorType, int layer, int xRef, int yRef) {
        super(objectRoom);
        this.animationFrame = 1;
        this.animationTimer = 1;
        this.monitorType = monitorType;
        createMonitor(layer, xRef, yRef);
    }

    private void createMonitor(int layer, int xRef, int yRef) {
        int length = 0;
        int width = 0;
        Rectangle bottomLeft = null;
        Rectangle bottomRight = null;        
        Image picture = null;
        if(animations == null) {
            animations = LoadAnimations.getAnimationMap("MONITOR");                   
        }
        switch(monitorType) {
            case MONITOR_RING:
                length = 28;
                width = 32;
                bottomLeft = new Rectangle(xRef-32, yRef, 4, width*2);
                bottomRight = new Rectangle(xRef+32, yRef, 4, width*2);                                
                currentAnimation = animations.get(ANIMATION_MONITORRING_STATIC);
                break;
            case MONITOR_SPEED:
                length = 28;
                width = 32;
                bottomLeft = new Rectangle(xRef-32, yRef, 4, width*2);
                bottomRight = new Rectangle(xRef+32, yRef, 4, width*2);  
                currentAnimation = animations.get(ANIMATION_MONITORSPEED_STATIC);
            default:
                break;
        }
        Rectangle intersectBox = new Rectangle(xRef-(length*2), yRef-(width*2), length*4, width*4);
        super.createSolidObject(true);
        super.createObject(layer, xRef, yRef, length, width, bottomLeft, bottomRight, intersectBox, false, true, picture);
    }
    
    @Override
    public void action() {
        super.action();
        changeAnimation();
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
        /*The animation frame is substracted by one to prevent animation frame from going outOfBounds (since the first frame of animation
        is technically at [0]*/
        super.setPicture(currentAnimation.getAnimationArray()[animationFrame-1]);//
    }
    
    @Override
    public void interactWithMario(MarioOWA owaM) {
        if(owaM.getJumpState() == JumpState.STATE_NOJUMP) {
            super.interactWithMario(owaM);    
        }
        if(owaM.getJumpState() == JumpState.STATE_JUMP_DOWN) {
            if(owaM.getIntersectBox().intersects(super.getIntersectBox())) {
                owaM.setYSpeed(-owaM.getYSpeed());
                super.getObjectRoom().removeGameObject(this);
                super.getObjectRoom().removePicture(this);
            }
        }
        else if(owaM.getHammerState() == HammerState.STATE_HAMMER) {
            if(owaM.checkValidHammerHit(super.getIntersectBox())) {
                super.getObjectRoom().removeGameObject(this);
                super.getObjectRoom().removePicture(this);
            }
        }
    }
    
    @Override
    public void interactWithSonic(SonicOWA owaS) {
        if(owaS.getRollState()== RollState.STATE_NOROLL && owaS.getJumpState() == JumpState.STATE_NOJUMP) {
            super.interactWithSonic(owaS);    
        }
        if(owaS.getRollState() == RollState.STATE_ROLL || owaS.getJumpState() == JumpState.STATE_JUMP_DOWN) {
            if(owaS.getIntersectBox().intersects(super.getIntersectBox())) {
                owaS.setYSpeed(-owaS.getYSpeed());
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
