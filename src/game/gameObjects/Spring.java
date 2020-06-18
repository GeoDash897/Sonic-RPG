/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gameObjects;

import game.LoadAnimations;
import game.animation.Animation;
import game.animation.Animation.AnimationName;
import game.overworld.Room;
import game.sonic.OWARemastered;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Map;

/**Controls Spring implementation- controls spring animations and behaviors when 
 *interacting with Sonic (launching him)
 * @author GeoSonicDash
 */
public class Spring extends SolidObject {
    private boolean extend;
    private int animationTimer;
    private double xLaunchSpeed;
    private double yLaunchSpeed;
    private String springName;
    private Map<AnimationName, Animation> animations;
    private Animation currentAnimation;
    private SpringType springType;
    public Spring(Room objectRoom, SpringType springType, int layer, int xRef, int yRef) {
        super(objectRoom);
        this.springType = springType;
        create(layer, xRef, yRef);
    }        
    
    /**This method is used to create Spring objects ONLY.
     * @param layer layer of the Spring object, used to organize object in objectRoom's picture ArrayList (occurs in SaveLoadObjects class).
     * @param xRef the x position of the Spring Object.
     * @param yRef the y position of the Spring Object.
     */
    private void create(int layer, int xRef, int yRef) {
        int length = 0;
        int width = 0;
        boolean gravity = false;
        boolean ground = false;
        Rectangle bottomLeft = null;
        Rectangle bottomRight = null;   
        Rectangle intersectBox = null;
        Image picture = null;
        if(animations == null) {
            animations = LoadAnimations.getAnimationMap(String.valueOf("SPRING"));                   
        }
        switch(springType) {
            case SPRING_YELLOWUP:
                length = 72;
                width = 72;
                gravity = true;
                ground = false;
                //Spring's actual length is 32 pixels (128 pixels in game) and width is 16 (64 in game)
                bottomLeft = new Rectangle(xRef-32, yRef, 4, 64);
                bottomRight = new Rectangle(xRef+32, yRef, 4, 64);
                intersectBox = new Rectangle(xRef-60,yRef-4, 128, 68);
                yLaunchSpeed = -13;
                xLaunchSpeed = 0;           
                break;
            case SPRING_REDUP:
                length = 72;
                width = 72;
                gravity = true;
                ground = false;
                //Spring's actual length is 32 pixels (128 pixels in game) and width is 16 (64 in game)
                bottomLeft = new Rectangle(xRef-32, yRef, 4, 64);
                bottomRight = new Rectangle(xRef+32, yRef, 4, 64);
                intersectBox = new Rectangle(xRef-60,yRef-4, 128, 68);
                yLaunchSpeed = -16;
                xLaunchSpeed = 0;           
                break;
            case SPRING_YELLOWLEFT:
                length = 72;
                width = 72;
                gravity = false;
                ground = false;
                //Spring's actual length is 16 pixels (64 pixels in game) and width is 32 (128 in game)
                intersectBox = new Rectangle(xRef-28,yRef-60, 64, 128);
                yLaunchSpeed = 0;
                xLaunchSpeed = -8; 
                break;
            case SPRING_REDLEFT:
                length = 72;
                width = 72;
                gravity = false;
                ground = false;
                //Spring's actual length is 16 pixels (64 pixels in game) and width is 32 (128 in game)
                intersectBox = new Rectangle(xRef-28,yRef-60, 64, 128);
                yLaunchSpeed = 0;
                xLaunchSpeed = -16; 
                break;
            case SPRING_YELLOWRIGHT:
                length = 72;
                width = 72;
                gravity = false;
                ground = false;
                //Spring's actual length is 16 pixels (64 pixels in game) and width is 32 (128 in game)
                intersectBox = new Rectangle(xRef-36,yRef-60, 64, 128);
                yLaunchSpeed = 0;
                xLaunchSpeed = 8; 
                break;
            default:
                break;
        }
        
        String[] temp = String.valueOf(springType).split("_");
        springName = String.join("", temp);
        currentAnimation = animations.get(AnimationName.valueOf("ANIMATION_"+springName+"_NORMAL"));
        super.createSolidObject(true);
        super.createObject(layer, xRef, yRef, length, width, bottomLeft, bottomRight, intersectBox, ground, gravity, picture);
    }
    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
    }
    @Override
    public void action() {
        super.action();
        changeAnimation();        
    }
    
    private void changeAnimation() {//Edit this to apply to all Springs
        if(!extend && currentAnimation != animations.get(AnimationName.valueOf("ANIMATION_"+springName+"_NORMAL"))) {
            currentAnimation = animations.get(AnimationName.valueOf("ANIMATION_"+springName+"_NORMAL"));
        }
        else if(extend) {
            /*Change the spring's animation to extend for a certain amount of time, then return it to its
            normal animation (non-extended version)*/
            if(animationTimer < 75) {
                animationTimer++;
                currentAnimation = animations.get(AnimationName.valueOf("ANIMATION_"+springName+"_EXTEND"));
            }
            else {
                animationTimer = 0;
                extend = false;
            }
        }
        super.setPicture(currentAnimation.getAnimationArray()[0]);
    }
    @Override
    public void interactWithSonic(OWARemastered owaR) {
        if(springType == SpringType.SPRING_REDUP || springType == SpringType.SPRING_YELLOWUP) {
            super.middleCollision(owaR); 
            if(owaR.getXCenterSonic() > (int) super.getIntersectBox().getX() && owaR.getXCenterSonic() < (int) (super.getIntersectBox().getX()+
                super.getIntersectBox().getWidth())) {
                if(owaR.getYCenterSonic() < (int)super.getIntersectBox().getY() && owaR.getIntersectBox().intersects(super.getIntersectBox())) {
                    performSpring(owaR);
                }
            }
        }
        else {
            if(owaR.getIntersectBox().intersects(super.getIntersectBox())) {
                performSpring(owaR);
            }
        }        
               
    }
    
    /**Controls launching Sonic at the right speed after he intersects with a spring.
     * 
     * @param owaR instance of {@code OWARemastered}- used to obtain Sonic's overworld variables.
     */
    private void performSpring(OWARemastered owaR) {
        if(yLaunchSpeed != 0) {
            owaR.setYSpeed(yLaunchSpeed);
            owaR.setXSpeed(xLaunchSpeed);
            owaR.setGroundSpeed(0);
            owaR.setSpringState(OWARemastered.SpringState.STATE_SPRING);
            owaR.setJumpState(OWARemastered.JumpState.STATE_NOJUMP);           
        }
        else {
            if(xLaunchSpeed < 0) {
                owaR.getAnimationControl().setDirection(0);
            }
            else {
                owaR.getAnimationControl().setDirection(1);
            }           
            owaR.setGroundSpeed(xLaunchSpeed);
            owaR.setSpringLock(true);
        }
        
        extend = true;
    }
    
    @Override
    public String toString() {
        return ""+springType+", "+super.getLayer()+", "+super.getXRef()+", "+super.getYRef();
    }
    
    public enum SpringType {
        SPRING_YELLOWUP,
        SPRING_REDUP,
        SPRING_YELLOWLEFT,
        SPRING_REDLEFT,
        SPRING_YELLOWRIGHT
    }
}
