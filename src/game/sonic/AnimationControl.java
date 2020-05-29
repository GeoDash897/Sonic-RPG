/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.sonic;

import static game.Launcher.debugStat;
import game.LoadAnimations;
import game.animation.Animation;
import game.animation.Animation.AnimationName;
import game.overworld.Picture;
import game.overworld.Room;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Map;

/*
    Author: GeoDash897  Date:10/5/19    Updated:10/5/19
*/
public class AnimationControl implements Picture {//This will control Sonic's animations
    private static int xDrawSonic;
    private static int yDrawSonic;
    private static int layer;
    private static int animationTimer;
    private static int animationFrame;
    private static int sonicWidth;
    private static boolean addToPictureAL;
    private static int direction;
    private static Image sonicPicture;
    private static Map<AnimationName, Animation> animations;
    private static Animation currentAnimation;
    public AnimationControl() {
        animationTimer = 1;
        animationFrame = 1;
        sonicWidth = 288;
        addToPictureAL = false;
        layer = 1;
        direction = 1; 
        animations = LoadAnimations.getAnimationMap("SONIC");
        currentAnimation = animations.get(AnimationName.ANIMATION_SONIC_STAND);
    }
    
    public void standard(Room currentRoom, int xCenterSonic, int yCenterSonic) {
        if(!addToPictureAL) {
            currentRoom.addPicture(this);
            addToPictureAL = true;
        }
        xDrawSonic = xCenterSonic - (sonicWidth/2);
        yDrawSonic = yCenterSonic - (sonicWidth/2); 
        
        animationTimer++;
        if(animationTimer%currentAnimation.getAnimationTimerFrameSet() == 0) {
            animationFrame++;
        }
        if(animationTimer >= (currentAnimation.getAnimationTimerFrameSet()*currentAnimation.getNumberOfFrames())) {
            animationFrame = currentAnimation.getResetAnimationFrame();
            animationTimer = currentAnimation.getResetAnimationTimer();
        }
        if(direction == 0) {
            sonicPicture = currentAnimation.getLeftAnimationArray()[animationFrame-1];
        }
        else {
            sonicPicture = currentAnimation.getAnimationArray()[animationFrame-1];
        }        
    }
    
    public void setSonicAnimation(AnimationName newAnimation) {
        currentAnimation = animations.get(newAnimation);
        animationTimer = 1;
        animationFrame = 1;
        //System.out.println("Changed animation to "+newAnimation);
    }
    @Override
    public void draw(Graphics2D g2) { 
        g2.setFont(debugStat);
        g2.setColor(Color.ORANGE);
        g2.drawString(toString(), 200, 300);
        g2.drawImage(sonicPicture,xDrawSonic,yDrawSonic,sonicWidth,sonicWidth, null);
        g2.setColor(Color.MAGENTA);
        g2.fillRect(xDrawSonic+(sonicWidth/2),yDrawSonic+(sonicWidth/2),4,4);
        g2.setColor(Color.GREEN);
        //g2.drawString("animationTimer:"+animationTimer, 1000, 400);
        g2.fillRect(xDrawSonic+(sonicWidth/2),yDrawSonic+(sonicWidth/2)-20,4,4);             
    }
    public void setaddToPictureAL(boolean set) {
        addToPictureAL = set;
    }
    public AnimationName getAnimationNumber() {
        return currentAnimation.getAnimationName();
    }
    
    @Override
    public int getLayer() {
        return layer;
    }    
    public int getDirection() {
        return direction;
    }
    public void setDirection(int change) {
        direction = change;
    }
    @Override
    public String toString() {
        return "Sonic is playing animationNumber "+currentAnimation.getAnimationName()+"; Current Frame: "+animationFrame+", Current AnimationTimer: "+
                animationTimer+ ", direction: "+direction+", layer: "+layer;
    }  
}
