/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.sonic;

import game.Game;
import game.PlayerInput;
import game.overworld.Ground;
import game.overworld.OverWorld;
import game.overworld.Room;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class OWARemastered {

    private static int xDrawCenterSonic;
    private static int yDrawCenterSonic;
    private static int ySpriteCenterSonic;
    private static double xSpeed;
    private static double ySpeed;
    private static double groundSpeed;
    private static double slope;
    private static double angle;
    private static boolean grounded;
    private static boolean collideWithSlopeL;
    private static boolean collideWithSlopeR;
    private static boolean bLCollide;
    private static boolean bRCollide;
    private static boolean tLCollide;
    private static boolean tRCollide;
    private static int bLDistanceFromRect;
    private static int bRDistanceFromRect;
    
    private static int direction;
    
    private static double GRAVITY = 0.21875;
    
    private static Rectangle bottomLeft;
    private static Rectangle bottomRight;
    private static Rectangle middleLeft;
    private static Rectangle middleRight;
    private static Rectangle topLeft;
    private static Rectangle topRight;
    
    private static Animation animation;
    private static Room currentRoom;
    
    private static SonicState state;
    
    public OWARemastered() {
        xDrawCenterSonic = 600;
        ySpriteCenterSonic = 500;
        xSpeed = 0;
        ySpeed = 0;
        groundSpeed = 0;
        slope = 0;
        angle = 0; 
        grounded = false;
        collideWithSlopeL = false;
        collideWithSlopeR = false;
        bLCollide = false;
        bRCollide = false;
        tLCollide = false;
        tRCollide = false;
        bLDistanceFromRect = 0;
        bRDistanceFromRect = 0;
    }
    
    public void mainMethod(Graphics2D g2, Room cR, Animation ani){
        /*try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }*/
        currentRoom = cR;
        animation = ani;
        yDrawCenterSonic = ySpriteCenterSonic + 16;
        state = SonicState.STATE_STAND;
        if(bLCollide && bRCollide) {
            grounded = true;
        }
        else if(bLCollide || bRCollide) {
            grounded = true;
        }
        else if(!bLCollide && !bRCollide) {
            grounded = false;
        }
        if(tLCollide || tRCollide) {
            ySpeed = 1;
        }
        if(!collideWithSlopeL && !collideWithSlopeR) {
            bottomLeft = new Rectangle(xDrawCenterSonic-36,ySpriteCenterSonic,4,80);    
            bottomRight = new Rectangle(xDrawCenterSonic+36,ySpriteCenterSonic,4,80);       
        }
        else if(collideWithSlopeL && !collideWithSlopeR) {
            bottomLeft = new Rectangle(xDrawCenterSonic-36,ySpriteCenterSonic,4,144);    
            bottomRight = new Rectangle(xDrawCenterSonic+36,ySpriteCenterSonic,4,80);       
        }
        else if(!collideWithSlopeL && collideWithSlopeR) {
            bottomLeft = new Rectangle(xDrawCenterSonic-36,ySpriteCenterSonic,4,80);    
            bottomRight = new Rectangle(xDrawCenterSonic+36,ySpriteCenterSonic,4,144);            
        }
        else if(collideWithSlopeL && collideWithSlopeR) {
            bottomLeft = new Rectangle(xDrawCenterSonic-36,ySpriteCenterSonic,4,144);    
            bottomRight = new Rectangle(xDrawCenterSonic+36,ySpriteCenterSonic,4,144);            
        }
        topLeft = new Rectangle(xDrawCenterSonic-36,ySpriteCenterSonic-84,4,80);
        topRight = new Rectangle(xDrawCenterSonic+36,ySpriteCenterSonic-84,4,80);
        if(PlayerInput.getRightPress()) {
            xSpeed = 3;
        }
        else if(PlayerInput.getLeftPress()) {
            xSpeed = -3;
        }
        else {
            xSpeed = 0;
        }
        if(PlayerInput.getUpPress()) {
            ySpeed = -3;
        }
        else if(PlayerInput.getDownPress()) {
            ySpeed = 3;
        }
        //Gravity code goes here
        if(!grounded) {           
            if (ySpeed < 0 && ySpeed > -4)//air drag calculation
            {
                if (Math.abs(xSpeed) >= 0.125) {
                    xSpeed = xSpeed * 0.96875;
                }
            }   
            ySpeed += GRAVITY;//Causes Sonic to move down
            if(ySpeed > 16) {//limits how fast Sonic is falling (so he won't clip through tiles)
                ySpeed = 16;
            }
        }
        else if(grounded) {
            ySpeed = 0;
        }
        
        
        xDrawCenterSonic += (int) xSpeed;
        ySpriteCenterSonic += (int) ySpeed;
        
        collisionCheck(g2);
        //changeAnimation();
        if(Game.getDebug()) {
            drawDebug(g2);
        }
        
    }
    private void collisionCheck(Graphics2D g2) {
        if(grounded || ySpeed >= 0) {
            bottomCollision(g2);    
        }
        else if(!grounded && ySpeed < 0) {
            topCollision(g2);
        }
    }
    
    private void bottomCollision(Graphics2D g2) {
        int xBottomLeft = (int) bottomLeft.getX();
        int xBottomRight = (int) bottomRight.getX();  
        int yBottomSensor = (int) (ySpriteCenterSonic+80);
        int heightBottomLeftIndex = 0;
        int heightBottomRightIndex = 0;
        int pixelyL = 90000;
        int pixelyR = 90000;
        Rectangle groundCheckL;
        Rectangle groundCheckR;
        g2.setColor(Color.GREEN);
        g2.fill(bottomLeft);
        g2.setColor(Color.CYAN);
        g2.fill(bottomRight);
        Ground highLeft = getCorrectTile(yBottomSensor, g2,bottomLeft);
        Ground highRight = getCorrectTile(yBottomSensor, g2,bottomRight);
        g2.setColor(Color.BLACK);
        g2.drawString("highLeft: "+highLeft, 500, 100);
        g2.drawString("highRight: "+highRight, 500, 125);
        bLCollide = false;
        bRCollide = false;
        tLCollide = false;
        tRCollide = false;
        if(highLeft != null) {
            heightBottomLeftIndex = (int) Math.abs(((xBottomLeft - highLeft.getXRef())/4));   
            pixelyL = (int) highLeft.getPixelBox(heightBottomLeftIndex).getY();
            groundCheckL = highLeft.getPixelBox(heightBottomLeftIndex);
            bLDistanceFromRect = Math.abs(yBottomSensor - (int) groundCheckL.getY());
            if(bottomLeft.intersects(groundCheckL)) {
                bLCollide = true;
                if(highLeft.getAngle() != 0) {
                collideWithSlopeL = true;
                }
                else if(highLeft.getAngle() == 0) {
                    collideWithSlopeL = false;
                }
            }            
        }
        if(highRight != null) {
            heightBottomRightIndex = (int) Math.abs(((xBottomRight - highRight.getXRef())/4));
            pixelyR = (int) highRight.getPixelBox(heightBottomRightIndex).getY();
            groundCheckR = highRight.getPixelBox(heightBottomRightIndex);
            bRDistanceFromRect = Math.abs(yBottomSensor - (int) groundCheckR.getY());
            if(bottomRight.intersects(groundCheckR)) {
                bRCollide = true;
                if(highRight.getAngle() != 0) {
                collideWithSlopeR = true;
            }
                else if(highRight.getAngle() == 0) {
                    collideWithSlopeR = false;
                }
            }            
        }       
        g2.drawString("pixelyL: "+pixelyL, 500, 150);
        g2.drawString("pixelyR: "+pixelyR, 500, 175);
        if(pixelyL < pixelyR) {
            setSonicGroundStat(heightBottomLeftIndex,pixelyL, yBottomSensor, highLeft);
        }
        else if(pixelyR < pixelyL) {
            setSonicGroundStat(heightBottomRightIndex,pixelyR, yBottomSensor, highRight);
        } 
        else if(pixelyR == pixelyL && pixelyR != Integer.MAX_VALUE) {
            setSonicGroundStat(heightBottomRightIndex,pixelyR, yBottomSensor, highRight);
        }
        
        //Get's Sonic's ledges
        if(grounded && xSpeed == 0 && angle == 0 && bLCollide && !bRCollide && bRDistanceFromRect >= 48) {
            if(highLeft != null) {
                if(xDrawCenterSonic >= highLeft.getXRef()+highLeft.getLength()+4) {
                    state = SonicState.STATE_RIGHTLEDGE;    
                }
            }
            
        }
        else if(grounded && xSpeed == 0 && angle == 0 && !bLCollide && bRCollide && bLDistanceFromRect >= 48) {
            if(highRight != null) {
                if(xDrawCenterSonic <= highRight.getXRef()-4) {
                    state = SonicState.STATE_LEFTLEDGE;    
                }
            }
        }
    } 
    
    private Ground getCorrectTile(int yBottomSensor,Graphics2D g2, Rectangle sensor) {
        int xBottomSensor = (int) sensor.getX();     
        if(sensor == bottomLeft) {
            g2.setColor(Color.MAGENTA);
            g2.fillRect(xBottomSensor,ySpriteCenterSonic,1,80);
            g2.fillRect(xBottomSensor,ySpriteCenterSonic+143,1,1);
        }
        if(sensor == bottomRight) {
            g2.setColor(Color.RED);
            g2.fillRect(xBottomSensor,ySpriteCenterSonic,1,80);
            g2.fillRect(xBottomSensor,ySpriteCenterSonic+143,1,1);
        }
        int xBottomIndex = xBottomSensor/64;
        int yBottomIndex = yBottomSensor/64;
        
        Ground intersect = currentRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex);
        if(intersect != null) {
            if(sensor == bottomLeft || sensor == bottomRight) {
                Ground intersectUp = currentRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex-1);
                Ground higher = Ground.compareSCTile(xBottomSensor, intersect, intersectUp);
                return higher;    
            }
            else if(sensor == topLeft || sensor == topRight) {
                Ground intersectDown = currentRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex+1);
                Ground higher = Ground.compareSCTile(xBottomSensor, intersect, intersectDown);
                return higher;    
            }
        }
        else {
            if(sensor == bottomLeft || sensor == bottomRight) {
                Ground intersectDown = currentRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex+1);
                Ground higher = Ground.compareSCTile(xBottomSensor, intersect, intersectDown);
                return higher;    
            }
            else if(sensor == topLeft || sensor == topRight) {
                Ground intersectUp = currentRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex-1);
                Ground higher = Ground.compareSCTile(xBottomSensor, intersect, intersectUp);
                return higher;    
            }
        }
        return null;
    }
    
    private void setSonicGroundStat(int heightIndex, int pixelHeight, int yBottomSensor, Ground highest) {     
        if(highest != null) {
            Rectangle collideCheck = highest.getPixelBox(heightIndex);
            if(yBottomSensor >= (int) collideCheck.getY()) {
                ySpriteCenterSonic = pixelHeight - 76; 
                angle = highest.getAngle();    
            }
        }        
    }
    
    private void topCollision(Graphics2D g2) {
        int xTopLeft = (int) topLeft.getX();
        int xTopRight = (int) topRight.getX();  
        int yBottomSensor = ySpriteCenterSonic;
        int heightBottomLeftIndex = 0;
        int heightBottomRightIndex = 0;
        int pixelyL = -90000;
        int pixelyR = -90000;
        Rectangle groundCheckL;
        Rectangle groundCheckR;
        Ground lowLeft = getCorrectTile(yBottomSensor, g2,topLeft);
        Ground lowRight = getCorrectTile(yBottomSensor, g2,topRight);
        g2.setColor(Color.BLACK);
        g2.drawString("lowLeft: "+lowLeft, 500, 100);
        g2.drawString("lowLeft: "+lowRight, 500, 125);
        g2.setColor(Color.BLUE);
        g2.fill(topLeft);
        g2.setColor(Color.YELLOW);
        g2.fill(topRight);
        if(lowLeft != null) {
            heightBottomLeftIndex = (int) Math.abs(((xTopLeft - lowLeft.getXRef())/4));   
            pixelyL = (int) (lowLeft.getPixelBox(heightBottomLeftIndex).getY()+lowLeft.getPixelBox(heightBottomLeftIndex).getHeight());
            groundCheckL = lowLeft.getPixelBox(heightBottomLeftIndex);
            if(topLeft.intersects(groundCheckL)) {
                tLCollide = true;
            }
        }  
        if(lowRight != null) {
            heightBottomRightIndex = (int) Math.abs(((xTopRight - lowRight.getXRef())/4));   
            pixelyR = (int) (lowRight.getPixelBox(heightBottomRightIndex).getY()+lowRight.getPixelBox(heightBottomRightIndex).getHeight());
            groundCheckL = lowRight.getPixelBox(heightBottomRightIndex);
            if(topRight.intersects(groundCheckL)) {
                tRCollide = true;
            }
        }
        if(pixelyL > pixelyR) {
            ySpriteCenterSonic = pixelyL + 64;
        }
        else if(pixelyR > pixelyL) {
            ySpriteCenterSonic = pixelyR + 64;
        } 
        else if(pixelyR == pixelyL && pixelyR != -90000) {
            ySpriteCenterSonic = pixelyL + 64;
        }
    }
    
    private void sideCollision() {
        
    }
            
    private void changeAnimation() {
        if(null != state) switch (state) {
            case STATE_STAND:
                if(animation.getAnimationNumber() != Animation.SonicAnimation.ANIMATION_SONIC_STAND) {
                    animation.setSonicAnimation(Animation.SonicAnimation.ANIMATION_SONIC_STAND);
                }   break;
            case STATE_LEFTLEDGE:
                if(animation.getAnimationNumber() != Animation.SonicAnimation.ANIMATION_SONIC_TRIPA_LEFT) {
                    animation.setSonicAnimation(Animation.SonicAnimation.ANIMATION_SONIC_TRIPA_LEFT);
                }   break;
            case STATE_RIGHTLEDGE:
                if(animation.getAnimationNumber() != Animation.SonicAnimation.ANIMATION_SONIC_TRIPA_RIGHT) {
                    animation.setSonicAnimation(Animation.SonicAnimation.ANIMATION_SONIC_TRIPA_RIGHT);
                }   break;
            default:
                break;
        }
    }
    public int getXCenterSonic() {
        return xDrawCenterSonic;
    }
    
    public int getYCenterSonic() {
        return yDrawCenterSonic;
    }

    private void drawDebug(Graphics2D g2) {
        g2.setColor(Color.MAGENTA);
        g2.drawString("DEBUG MENU",600,25);        
        //Variables that have to do with Sonic's x and y position, checking ground, x and y speed, etc:
        g2.drawString("xDrawCenterSonic: "+xDrawCenterSonic,75,75);
        g2.drawString("yDrawCenterSonic: "+yDrawCenterSonic,75,100);
        g2.drawString("ySpriteCenterSonic: "+ySpriteCenterSonic,75,125);
        g2.drawString("grounded: "+grounded,75,150);
        g2.drawString("groundSpeed: "+groundSpeed,75,175);
        g2.drawString("xSpeed: "+xSpeed,75,200);
        g2.drawString("ySpeed: "+ySpeed,75,225);       
        g2.drawString("slope: "+slope,75,250);
        g2.drawString("collideWithSlopeL: "+collideWithSlopeL,75,275);
        g2.drawString("collideWithSlopeR: "+collideWithSlopeR,75,300);
        g2.drawString("bLCollide: "+bLCollide,75,325);
        g2.drawString("bRCollide: "+bRCollide,75,350);
        g2.drawString("bLDistanceFromRect: "+bLDistanceFromRect,75,375);
        g2.drawString("bRDistanceFromRect: "+bRDistanceFromRect,75,400);
        //Variables that have to do with Sonic's animations:
        g2.setColor(Color.GREEN);
        g2.drawString("angle: "+angle, 400, 75);
        g2.setColor(Color.ORANGE);
        g2.drawString("Sonic's State: "+state, 75, 500);
    }
    public enum SonicState {
        STATE_STAND,
        STATE_LEFTLEDGE,
        STATE_RIGHTLEDGE,
    } 
}