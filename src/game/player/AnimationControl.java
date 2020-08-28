/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.player;

import static game.Launcher.debugStat;
import game.LoadAnimations;
import game.animation.Animation;
import game.animation.Animation.AnimationName;
import game.overworld.Picture;
import game.player.PlayerCharacter.PlayerName;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Map;

/**Controls the animations of Sonic- stores it and changes it to the correct animation.
 * 
 * @author GeoSonicDash
 */
public class AnimationControl implements Picture {//This will control Sonic's animations
    private int xDrawCharacter;
    private int yDrawCharacter;
    private int layer;
    private int animationTimer;
    private int animationFrame;
    private int characterWidth;
    private int direction;
    private boolean freezeCurrentAnimation;
    private Image characterPicture;
    private PlayerCharacter character;
    private Map<AnimationName, Animation> animations;
    private Animation currentAnimation;
    private static PlayerManager manager;
    public AnimationControl(PlayerManager man, PlayerCharacter character) {
        manager = man;
        this.animationTimer = 1;
        this.animationFrame = 1;       
        this.layer = 1;
        this.direction = 1;
        this.freezeCurrentAnimation = false;
        this.character = character;
        if(character.getPlayerName() == PlayerName.PLAYERNAME_SONIC) {
            characterWidth = 288;
            animations = LoadAnimations.getAnimationMap("SONIC");
            currentAnimation = animations.get(AnimationName.ANIMATION_SONIC_STAND);
        }    
        else if(character.getPlayerName() == PlayerName.PLAYERNAME_MARIO) {
            characterWidth = 360;
            animations = LoadAnimations.getAnimationMap("MARIO");
            currentAnimation = animations.get(AnimationName.ANIMATION_MARIO_STAND);
        }       
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
     * @param xCenterPlayer x of Sonic (based on the center).
     * @param yCenterPlayer y of Sonic (based on the center).
     */
    public void standard(int xCenterPlayer, int yCenterPlayer) {
        xDrawCharacter = xCenterPlayer - (characterWidth/2);
        yDrawCharacter = yCenterPlayer - (characterWidth/2);    
        if(!freezeCurrentAnimation) {
            animationTimer++;
            if(animationTimer%currentAnimation.getAnimationTimerFrameSet() == 0) {
                animationFrame++;
            }
            if(animationTimer >= (currentAnimation.getAnimationTimerFrameSet()*currentAnimation.getNumberOfFrames())) {
                animationFrame = currentAnimation.getResetAnimationFrame();
                animationTimer = currentAnimation.getResetAnimationTimer();
            }
        }       
        if(direction == 0) {
            characterPicture = currentAnimation.getLeftAnimationArray()[animationFrame-1];
        }
        else {
            characterPicture = currentAnimation.getAnimationArray()[animationFrame-1];
        }        
    }
    
    public void setAnimation(AnimationName newAnimation) {        
        currentAnimation = animations.get(newAnimation);
        animationTimer = 1;
        animationFrame = 1;
    }
    @Override
    public void draw(Graphics2D g2) { 
        g2.setFont(debugStat);
        g2.setColor(Color.ORANGE);
        g2.drawString(toString(), 200, 300);
        g2.drawImage(characterPicture,xDrawCharacter,yDrawCharacter,characterWidth,characterWidth, null);
        g2.setColor(Color.MAGENTA);
        g2.fillRect(xDrawCharacter+(characterWidth/2),yDrawCharacter+(characterWidth/2),4,4);
        if(character.getPlayerName() == PlayerName.PLAYERNAME_SONIC) {
            g2.setColor(Color.GREEN);
            g2.fillRect(xDrawCharacter+(characterWidth/2),yDrawCharacter+(characterWidth/2)-20,4,4); 
        }           
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
        setAnimation(currentAnimation.getAnimationName());
        direction = change;      
    }
    
    public void setAnimationFrame(int temp) {
        animationFrame = temp;
    }
    
    public boolean getFreezeCurrentAnimation() {
        return freezeCurrentAnimation;
    }
    
    public void setFreezeCurrentAnimation(boolean temp) {
        freezeCurrentAnimation = temp;
    }
    
    @Override
    public String toString() {
        return character.getPlayerName()+" is playing animationNumber "+currentAnimation.getAnimationName()+"; Current Frame: "+animationFrame+", Current AnimationTimer: "+
                animationTimer+ ", direction: "+direction+", layer: "+layer;
    }  
}
