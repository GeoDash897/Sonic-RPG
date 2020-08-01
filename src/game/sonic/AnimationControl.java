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
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Map;

/**Controls the animations of Sonic- stores it and changes it to the correct animation.
 * 
 * @author GeoSonicDash
 */
public class AnimationControl implements Picture {//This will control Sonic's animations
    private int xDrawSonic;
    private int yDrawSonic;
    private int layer;
    private int animationTimer;
    private int animationFrame;
    private int sonicWidth;
    private int direction;
    private Image sonicPicture;
    private Map<AnimationName, Animation> animations;
    private Animation currentAnimation;
    private static PlayerManager manager;
    public AnimationControl(PlayerManager man) {
        animationTimer = 1;
        animationFrame = 1;
        sonicWidth = 288;
        layer = 1;
        direction = 1; 
        animations = LoadAnimations.getAnimationMap("SONIC");
        currentAnimation = animations.get(AnimationName.ANIMATION_SONIC_STAND);
        manager = man;
        addFirstTime();
    }
    /**This adds Sonic's picture to the room that he spawns into.
     * 
     */
    private void addFirstTime() {
        manager.getCurrentRoom().addPicture(this);
    }
    
    /**Sets the x and y position for drawing Sonic's {@code Image},
     * increments the time of the current {@code Animation} that
     * is playing, and sets the correct {@code Image} that should be displayed
     * for the current {@code Animation}.
     * 
     * @param xCenterSonic x of Sonic (based on the center).
     * @param yCenterSonic y of Sonic (based on the center).
     */
    public void standard(int xCenterSonic, int yCenterSonic) {
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
    
    public void addToRoomPictureAL() {
        manager.getCurrentRoom().addPicture(this);
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
        setSonicAnimation(currentAnimation.getAnimationName());
        direction = change;      
    }
    @Override
    public String toString() {
        return "Sonic is playing animationNumber "+currentAnimation.getAnimationName()+"; Current Frame: "+animationFrame+", Current AnimationTimer: "+
                animationTimer+ ", direction: "+direction+", layer: "+layer;
    }  
}
