/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.sonic;

import game.overworld.OverWorld;
import game.overworld.Picture;
import game.overworld.Room;
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
    private static int animationNumber = 0;
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
            System.out.println("Code Ran Here");
            currentRoom.addPicture(this);
            addToPictureAL = true;
        }
        xDrawSonic = xCenterSonic - 144;
        yDrawSonic = yCenterSonic - 144;
        switch (animationNumber) {
            case 0:              
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
            case 1:               
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
            case 2:               
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
            case 3:               
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
            case 4:               
                animationTimerFrameSet = 25;
                numberOfFrames = 3;
                resetAnimationFrame = 1;
                resetAnimationTimer = 1;
                sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\LSonic Trip A_"+animationFrame+".png");
                break;
            case 5:               
                animationTimerFrameSet = 25;
                numberOfFrames = 3;
                resetAnimationFrame = 1;
                resetAnimationTimer = 1;
                sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Trip A_"+animationFrame+".png");
                break;
            case 7:              
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
            case 8:
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
            case 9:
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
            case 10:
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
            case 11:
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
            case 13:                
                animationTimerFrameSet = 40;
                numberOfFrames = 4;
                resetAnimationFrame = 1;
                resetAnimationTimer = 1;
                sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\LSonic Push_"+animationFrame+".png");
                break;
            case 14:               
                animationTimerFrameSet = 40;
                numberOfFrames = 4;
                resetAnimationFrame = 1;
                resetAnimationTimer = 1;
                sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Push_"+animationFrame+".png");
                break;
            case 15:               
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
            case 16:               
                animationTimerFrameSet = 30;
                numberOfFrames = 30;
                resetAnimationFrame = 1;
                resetAnimationTimer = 1;
                sonicPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic Idle_"+animationFrame+".png");
                break;
            default:
                break;
        }
        if(animationNumber != 0) {
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
    public void setAnimationNumber(int newAnimation) {
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
    public int getAnimationNumber() {
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
}
