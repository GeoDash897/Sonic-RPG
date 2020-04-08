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
    private static boolean collideWithSlope;
    
    private static int direction;
    
    private static Rectangle bottomLeft;
    private static Rectangle bottomRight;
    private static Rectangle middleLeft;
    private static Rectangle middleRight;
    private static Rectangle topLeft;
    private static Rectangle topRight;
    
    private static OverWorld overWorld;
    private static Room currentRoom;
    
    public OWARemastered() {
        xDrawCenterSonic = 600;
        ySpriteCenterSonic = 500;
        xSpeed = 0;
        ySpeed = 0;
        groundSpeed = 0;
        slope = 0;
        angle = 0; 
        grounded = false;
        collideWithSlope = false;
        
    }
    
    public void mainMethod(Graphics2D g2, Room cR){
        /*try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }*/
        currentRoom = cR;
        yDrawCenterSonic = ySpriteCenterSonic + 20;
        bottomLeft = new Rectangle(xDrawCenterSonic-36,ySpriteCenterSonic+4,4,76);    
        bottomRight = new Rectangle(xDrawCenterSonic+36,ySpriteCenterSonic+4,4,76);       
        if(PlayerInput.getRightPress()) {
            xSpeed = 3;
        }
        else if(PlayerInput.getLeftPress()) {
            xSpeed = -3;
        }
        else if(PlayerInput.getUpPress()) {
            ySpeed = -3;
        }
        else if(PlayerInput.getDownPress()) {
            ySpeed = 3;
        }
        else {
            xSpeed = 0;
            ySpeed = 5;
        }
        xDrawCenterSonic += (int) xSpeed;
        ySpriteCenterSonic += (int) ySpeed;
        collisionCheck(g2);
        if(Game.getDebug()) {
            drawDebug(g2);
        }
        
    }
    private void collisionCheck(Graphics2D g2) {
        if(grounded || (ySpeed >= 0 || (Math.abs(xSpeed) > Math.abs(ySpeed)))) {
            bottomCollision(g2);    
        }
        
    }
    
    private void bottomCollision(Graphics2D g2) {
        int xBottomLeft = (int) bottomLeft.getX();
        int xBottomRight = (int) bottomRight.getX();  
        int yBottomSensor = (int) (ySpriteCenterSonic+bottomRight.getHeight()+4);
        int heightBottomLeftIndex = 0;
        int heightBottomRightIndex = 0;
        int pixelyL = Integer.MAX_VALUE;
        int pixelyR = Integer.MAX_VALUE;
        g2.setColor(Color.GREEN);
        g2.fill(bottomLeft);
        g2.setColor(Color.CYAN);
        g2.fill(bottomRight);
        Ground highLeft = getHighestSensorTile(yBottomSensor, g2,bottomLeft);
        Ground highRight = getHighestSensorTile(yBottomSensor, g2,bottomRight);
        //Ground highest = Ground.compareDCTile(xBottomLeft, xBottomRight, highLeft, highRight);
        g2.setColor(Color.BLACK);
        g2.drawString("highLeft: "+highLeft, 500, 100);
        g2.drawString("highRight: "+highRight, 500, 125);
        if(highLeft != null) {
            heightBottomLeftIndex = (int) Math.abs(((xBottomLeft - highLeft.getXRef())/4));   
            pixelyL = (int) highLeft.getPixelBox(heightBottomLeftIndex).getY();
        }
        if(highRight != null) {
            heightBottomRightIndex = (int) Math.abs(((xBottomRight - highRight.getXRef())/4));
            pixelyR = (int) highRight.getPixelBox(heightBottomRightIndex).getY();
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
        //g2.drawString("highest: "+highest, 500, 150);
        /*if(highest != null) {
            if(highest == highLeft) {
                int heightBottomIndex = (int) Math.abs(((xBottomLeft - highest.getXRef())/4));
                int pixelY = (int) highest.getPixelBox(heightBottomIndex).getY();
                g2.drawString("pixelYL: "+pixelY, 500, 175);
                setSonicGroundStat(pixelY, highest);
            }
            else if(highest == highRight) {
                int heightBottomIndex = (int) Math.abs(((xBottomRight - highest.getXRef())/4));
                int pixelY = (int) highest.getPixelBox(heightBottomIndex).getY();
                setSonicGroundStat(pixelY, highest);         
            }
            else if(highest == highLeft && highest == highRight) {
                int heightBottomLeftIndex = (int) Math.abs(((xBottomLeft - highest.getXRef())/4));
                int heightBottomRightIndex = (int) Math.abs(((xBottomRight - highest.getXRef())/4));
                int pixelyL = (int) highest.getPixelBox(heightBottomLeftIndex).getY();
                int pixelyR = (int) highest.getPixelBox(heightBottomRightIndex).getY();
                if(pixelyL < pixelyR) {
                    setSonicGroundStat(pixelyL, highest);
                }
                else if(pixelyR < pixelyL) {
                    setSonicGroundStat(pixelyR, highest);
                } 
            }       
        }*/
    }   
    private void setSonicGroundStat(int heightIndex, int pixelHeight, int yBottomSensor, Ground highest) {       
        Rectangle collideCheck = highest.getPixelBox(heightIndex);
        if(yBottomSensor >= (int) collideCheck.getY()) {
            ySpriteCenterSonic = pixelHeight - 76; 
            if(highest.getAngle() != 0) {
                collideWithSlope = true;
            }
            else if(highest.getAngle() == 0) {
                collideWithSlope = false;
            }
            angle = highest.getAngle();    
        }
    }
    private Ground getHighestSensorTile(int yBottomSensor,Graphics2D g2, Rectangle sensor) {
        int xBottomSensor = (int) sensor.getX();     
        if(sensor == bottomLeft) {
            g2.setColor(Color.MAGENTA);
            g2.fillRect(xBottomSensor,ySpriteCenterSonic+4,1,76);
            g2.fillRect(xBottomSensor,ySpriteCenterSonic+143,1,1);
        }
        if(sensor == bottomRight) {
            g2.setColor(Color.RED);
            g2.fillRect(xBottomSensor,ySpriteCenterSonic+4,1,76);
            g2.fillRect(xBottomSensor,ySpriteCenterSonic+143,1,1);
        }
        int xBottomIndex = xBottomSensor/64;
        int yBottomIndex = yBottomSensor/64;
        
        Ground intersect = currentRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex);
        if(intersect != null) {
            Ground intersectUp = currentRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex-1);
            Ground higher = Ground.compareSCTile(xBottomSensor, intersect, intersectUp);
            return higher;
        }
        else {
            Ground intersectDown = currentRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex+1);
            Ground higher = Ground.compareSCTile(xBottomSensor, intersect, intersectDown);
            return higher;
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
        g2.drawString("collideWithSlope: "+collideWithSlope,75,275);
        //Variables that have to do with Sonic's animations:
        g2.setColor(Color.GREEN);
        g2.drawString("angle: "+angle, 400, 75);
    }
}