/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.sonic;

import game.overworld.OverWorld;
import game.overworld.Picture;
import game.overworld.Room;
import static game.sonic.Animation.SonicAnimation.ANIMATION_SONIC_STAND;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;

/*
    Author: GeoDash897  Date:10/5/19    Updated:10/5/19
*/
public class Animation extends Sonic implements Picture {//This will control Sonic's animations
    private static int xDrawSonic;
    private static int yDrawSonic;
    private static int animationTimer = 1;
    private static SonicAnimation animationNumber = SonicAnimation.ANIMATION_SONIC_STAND;
    private static int animationReset = 0;
    private static int animationFrame = 1;
    private static int sonicWidth = 288;
    private static int animationTimerFrameSet = 0;
    private static int numberOfFrames = 0;
    private static int resetAnimationFrame = 1;
    private static int resetAnimationTimer = 0;
    private static boolean addToPictureAL = false;
    private static int direction = 1;
    private static Image sonicPicture;
    public void standard(Graphics2D g2, Room currentRoom, int xCenterSonic, int yCenterSonic) {
        if(!addToPictureAL) {
            currentRoom.addPicture(this);
            addToPictureAL = true;
        }
        xDrawSonic = xCenterSonic - 144;
        yDrawSonic = yCenterSonic - 144;
        switch (animationNumber) {
            case ANIMATION_SONIC_STAND:              
                animationTimerFrameSet = 10;
                numberOfFrames = 1;
                resetAnimationFrame = 1;
                resetAnimationTimer = 1;
                if(direction == 0) {
                    sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\LSonic Wait_"+animationFrame+".png");
                }
                else if(direction == 1) {
                    sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Wait_"+animationFrame+".png");
                }   
                break;
            case ANIMATION_SONIC_WAIT:               
                animationTimerFrameSet = 30;
                numberOfFrames = 6;
                resetAnimationFrame = 5;
                resetAnimationTimer = 121;
                if(direction == 0) {
                    sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\LSonic Wait_"+animationFrame+".png");
                }
                else if(direction == 1) {
                    sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Wait_"+animationFrame+".png");
                }   
                break;
            case ANIMATION_SONIC_WALK:               
                animationTimerFrameSet = 30;
                numberOfFrames = 8;
                resetAnimationFrame = 1;
                resetAnimationTimer = 1;
                if(direction == 0) {
                    sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\LSonic Walk_"+animationFrame+".png");    
                }
                else if (direction == 1) {
                   sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Walk_"+animationFrame+".png"); 
                }               
                break;
            case ANIMATION_SONIC_RUN:               
                animationTimerFrameSet = 15;
                numberOfFrames = 4;
                resetAnimationFrame = 1;
                resetAnimationTimer = 1;
                if(direction == 0) {
                    sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\LSonic Run_"+animationFrame+".png");
                }
                else if(direction == 1) {
                    sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Run_"+animationFrame+".png");
                }   
                break;
            case ANIMATION_SONIC_TRIPA_LEFT:               
                animationTimerFrameSet = 25;
                numberOfFrames = 3;
                resetAnimationFrame = 1;
                resetAnimationTimer = 1;
                sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\LSonic Trip A_"+animationFrame+".png");
                break;
            case ANIMATION_SONIC_TRIPA_RIGHT:               
                animationTimerFrameSet = 25;
                numberOfFrames = 3;
                resetAnimationFrame = 1;
                resetAnimationTimer = 1;
                sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Trip A_"+animationFrame+".png");
                break;
            case ANIMATION_SONIC_JUMP:              
                animationTimerFrameSet = 10;
                numberOfFrames = 8;
                resetAnimationFrame = 1;
                resetAnimationTimer = 1;
                if(direction == 0) {
                    sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\LSonic Jump_"+animationFrame+".png");
                }
                else if(direction == 1) {
                    sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Jump_"+animationFrame+".png");
                }
                break;
            case ANIMATION_SONIC_DUCK:
                animationTimerFrameSet = 50;
                numberOfFrames = 2;
                resetAnimationFrame = 2;
                resetAnimationTimer = 200;
                if(direction == 0) {
                    sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\LSonic Duck_"+animationFrame+".png");
                }
                else if(direction == 1) {
                    sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Duck_"+animationFrame+".png");
                }
                break;
            case ANIMATION_SONIC_SKID:
                animationTimerFrameSet = 10;
                numberOfFrames = 1;
                resetAnimationFrame = 1;
                resetAnimationTimer = 1;
                if(direction == 0) {
                    sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\LSonic Skid_"+animationFrame+".png");
                }
                else if(direction == 1) {
                    sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Skid_"+animationFrame+".png");
                }
                break;
            case ANIMATION_SONIC_SPINDASH:
                animationTimerFrameSet = 20;
                numberOfFrames = 6;
                resetAnimationFrame = 1;
                resetAnimationTimer = 1;
                if(direction == 0) {
                    sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\LSonic Spindash_"+animationFrame+".png");
                }
                else if(direction == 1) {
                    sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Spindash_"+animationFrame+".png");
                }
                break;
            case ANIMATION_SONIC_SLEEP:
                animationTimerFrameSet = 50;
                numberOfFrames = 4;
                resetAnimationFrame = 4;
                resetAnimationTimer = 200;
                if(direction == 0) {
                    sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\LSonic Sleep_"+animationFrame+".png");
                }
                else if(direction == 1) {
                    sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Sleep_"+animationFrame+".png");
                }
                break;
            case ANIMATION_SONIC_PUSH_LEFT:                
                animationTimerFrameSet = 40;
                numberOfFrames = 4;
                resetAnimationFrame = 1;
                resetAnimationTimer = 1;
                sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\LSonic Push_"+animationFrame+".png");
                break;
            case ANIMATION_SONIC_PUSH_RIGHT:               
                animationTimerFrameSet = 40;
                numberOfFrames = 4;
                resetAnimationFrame = 1;
                resetAnimationTimer = 1;
                sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Push_"+animationFrame+".png");
                break;
            case ANIMATION_SONIC_SPRING:               
                animationTimerFrameSet = 10;
                numberOfFrames = 1;
                resetAnimationFrame = 1;
                resetAnimationTimer = 1;
                if(direction == 0) {
                    sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\LSonic Spring_"+animationFrame+".png");
                }
                else if(direction == 1) {
                    sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Spring_"+animationFrame+".png");
                }                
                break;
            case ANIMATION_SONIC_BATTLE_IDLE:               
                animationTimerFrameSet = 30;
                numberOfFrames = 30;
                resetAnimationFrame = 1;
                resetAnimationTimer = 1;
                sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Idle_"+animationFrame+".png");
                break;
            default:
                break;
        }
        if(animationNumber != ANIMATION_SONIC_STAND) {
            animationTimer++;            
        }
        if(animationTimer%animationTimerFrameSet == 0) {
            animationFrame++;
        }
        if(animationTimer >= (animationTimerFrameSet*numberOfFrames)) {
            animationFrame = resetAnimationFrame;
            animationTimer = resetAnimationTimer;
        }                            
    }
    public void setSonicAnimation(SonicAnimation newAnimation) {
        animationNumber = newAnimation;
        animationReset = 0;
        if(animationReset == 0) {
            animationTimer = 1;
            animationFrame = 1;
            animationReset = 1;
        }
        System.out.println("Changed animation to "+newAnimation);
    }
    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(sonicPicture,xDrawSonic,yDrawSonic,sonicWidth,sonicWidth,this);
        g2.setColor(Color.black);
    }
    public void setaddToPictureAL(boolean set) {
        addToPictureAL = set;
    }
    public SonicAnimation getAnimationNumber() {
        return animationNumber;
    }
    public int getDirection() {
        return direction;
    }
    public void setDirection(int change) {
        direction = change;
    }
    @Override
    public String toString() {
        return "Sonic is playing animationNumber "+animationNumber+"; Current Frame: "+animationFrame+", Current AnimationTimer: "+
                animationTimer;
    }  
    public enum SonicAnimation {
        ANIMATION_SONIC_STAND,
        ANIMATION_SONIC_WAIT,
        ANIMATION_SONIC_WALK,
        ANIMATION_SONIC_RUN,
        ANIMATION_SONIC_TRIPA_LEFT,
        ANIMATION_SONIC_TRIPA_RIGHT,
        ANIMATION_SONIC_JUMP,
        ANIMATION_SONIC_DUCK,
        ANIMATION_SONIC_SKID,
        ANIMATION_SONIC_SPINDASH,
        ANIMATION_SONIC_SLEEP,
        ANIMATION_SONIC_PUSH_LEFT,
        ANIMATION_SONIC_PUSH_RIGHT,
        ANIMATION_SONIC_SPRING,
        ANIMATION_SONIC_BATTLE_IDLE
    }
}
