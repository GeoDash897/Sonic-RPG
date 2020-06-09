/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.animation;

import java.awt.Image;
import java.awt.Toolkit;

/**
 *
 * @author GeoSonicDash
 */
public class Animation {    
    private int animationTimerFrameSet;
    private int numberOfFrames;
    private int resetAnimationFrame;
    private int resetAnimationTimer;
    private AnimationName animationName;
    private Image[] animation;
    private Image[] leftAnimation;
    public Animation(AnimationName animationName, String filePath, int animationTimerFrameSet, int numberOfFrames, int resetAnimationFrame,
            int resetAnimationTimer) {        
        this.animationTimerFrameSet = animationTimerFrameSet;
        this.numberOfFrames = numberOfFrames;
        this.resetAnimationFrame = resetAnimationFrame;
        this.resetAnimationTimer = resetAnimationTimer;
        this.animationName = animationName;
        this.animation = new Image[numberOfFrames];
        this.leftAnimation = new Image[numberOfFrames];
        createAnimationArray(filePath);
    }
    
    private void createAnimationArray(String filePath) {
        for(int i = 0; i < numberOfFrames; i++) {
            animation[i] = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\"+filePath+"_"+(i+1)+".png");
            leftAnimation[i] = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\L"+filePath+"_"+(i+1)+".png");
        }
    }   
    
    public Image[] getAnimationArray() {
        return animation;
    }
    
    public Image[] getLeftAnimationArray() {
        return leftAnimation;
    }
    
    public int getAnimationTimerFrameSet() {
        return animationTimerFrameSet;
    }
    
    public int getNumberOfFrames() {
        return numberOfFrames;
    }
    
    public int getResetAnimationFrame() {
        return resetAnimationFrame;
    }
    
    public int getResetAnimationTimer() {
        return resetAnimationTimer;
    }
    
    public AnimationName getAnimationName() {
        return animationName;
    }
    
    public enum AnimationName {
        ANIMATION_SONIC_STAND,
        ANIMATION_SONIC_WAIT,
        ANIMATION_SONIC_WALK,
        ANIMATION_SONIC_RUN,
        ANIMATION_SONIC_TRIPA,
        ANIMATION_SONIC_JUMP,
        ANIMATION_SONIC_DUCK,
        ANIMATION_SONIC_SKID,
        ANIMATION_SONIC_SPINDASH,
        ANIMATION_SONIC_SLEEP,
        ANIMATION_SONIC_BORED,
        ANIMATION_SONIC_PUSH,
        ANIMATION_SONIC_SPRING,
        ANIMATION_SONIC_BATTLE_IDLE,
        ANIMATION_NPCSKELETON_STAND,
        ANIMATION_MONITORRING_STATIC,
        ANIMATION_MONITORSPEED_STATIC,
        ANIMATION_SPRINGYELLOWUP_NORMAL,
        ANIMATION_SPRINGYELLOWUP_EXTEND,
        ANIMATION_SPRINGREDUP_NORMAL,
        ANIMATION_SPRINGREDUP_EXTEND,
        ANIMATION_SPRINGYELLOWLEFT_NORMAL,
        ANIMATION_SPRINGYELLOWLEFT_EXTEND,
        ANIMATION_SPRINGREDLEFT_NORMAL,
        ANIMATION_SPRINGREDLEFT_EXTEND,
        ANIMATION_SPRINGYELLOWRIGHT_NORMAL,
        ANIMATION_SPRINGYELLOWRIGHT_EXTEND,
        ANIMATION_SPRINGREDRIGHT_NORMAL,
        ANIMATION_SPRINGREDRIGHT_EXTEND
    }
}
