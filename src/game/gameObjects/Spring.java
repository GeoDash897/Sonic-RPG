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
import game.sonic.OWARemastered;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.Map;

/**
 *
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
    
    private void create(int layer, int xRef, int yRef) {
        int length = 0;
        int width = 0;
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
                //Spring's actual length is 32 pixels (128 pixels in game) and width is 16 (64 in game)
                bottomLeft = new Rectangle(xRef-32, yRef, 4, 64);
                bottomRight = new Rectangle(xRef+32, yRef, 4, 64);
                intersectBox = new Rectangle(xRef-60,yRef-4, 128, 68);
                yLaunchSpeed = -16;
                xLaunchSpeed = 0;           
                break;
            default:
                break;
        }
        
        String[] temp = String.valueOf(springType).split("_");
        springName = String.join("", temp);
        currentAnimation = animations.get(AnimationName.valueOf("ANIMATION_"+springName+"_NORMAL"));
        super.createSolidObject(true);
        super.createObject(layer, xRef, yRef, length, width, bottomLeft, bottomRight, intersectBox, false, true, picture);
    }
    @Override
    public void draw(Graphics2D g2) {
        super.draw(g2);
        g2.drawString("animationTimer " +animationTimer, 300, 350);
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
        super.middleCollision(owaR);
        if(owaR.getXCenterSonic() > (int) super.getIntersectBox().getX() && owaR.getXCenterSonic() < (int) (super.getIntersectBox().getX()+
                super.getIntersectBox().getWidth())) {
            if(owaR.getYCenterSonic() < (int)super.getIntersectBox().getY() && owaR.getIntersectBox().intersects(super.getIntersectBox())) {
                owaR.setYSpeed(yLaunchSpeed);
                owaR.setXSpeed(xLaunchSpeed);
                owaR.setGroundSpeed(0);
                owaR.setJumpState(OWARemastered.JumpState.STATE_NOJUMP);
                owaR.setSpringState(OWARemastered.SpringState.STATE_SPRING);
                extend = true;
            }
        }       
    }
    
    @Override
    public String toString() {
        return ""+springType+", "+super.getLayer()+", "+super.getXRef()+", "+super.getYRef();
    }
    
    public enum SpringType {
        SPRING_YELLOWUP,
        SPRING_REDUP
    }
}
