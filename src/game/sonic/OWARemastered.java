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
        direction = 0;
        
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
            ySpeed = 0;
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
        g2.setColor(Color.GREEN);
        g2.fill(bottomLeft);
        g2.setColor(Color.CYAN);
        g2.fill(bottomRight);
        Ground highLeft = getHighestSensorTile(g2,bottomLeft);
        Ground highRight = getHighestSensorTile(g2,bottomRight);
        Ground highest = Ground.compareDCTile(xBottomLeft, xBottomRight, highLeft, highRight);
        g2.setColor(Color.BLACK);
        g2.drawString("highLeft: "+highLeft, 500, 100);
        g2.drawString("highRight: "+highRight, 500, 125);
        //g2.drawString("highest: "+highest, 500, 150);
        if(highest != null) {
            if(highest == highLeft) {
                int heightBottomIndex = (int) Math.abs(((xBottomLeft - highest.getXRef())/4));
                int pixelY = (int) highest.getPixelBox(heightBottomIndex).getY();
                g2.drawString("pixelYL: "+pixelY, 500, 175);
                ySpriteCenterSonic = pixelY - 76;
            }
            else if(highest == highRight) {
                int heightBottomIndex = (int) Math.abs(((xBottomRight - highest.getXRef())/4));
                int pixelY = (int) highest.getPixelBox(heightBottomIndex).getY();
                g2.drawString("pixelYR: "+pixelY, 500, 200);
                ySpriteCenterSonic = pixelY - 76;                
            }
            else if(highest == highLeft && highest == highRight) {
                int heightBottomLeftIndex = (int) Math.abs(((xBottomLeft - highest.getXRef())/4));
                int heightBottomRightIndex = (int) Math.abs(((xBottomRight - highest.getXRef())/4));
                int pixelyL = (int) highest.getPixelBox(heightBottomLeftIndex).getY();
                int pixelyR = (int) highest.getPixelBox(heightBottomRightIndex).getY();
                if(pixelyL < pixelyR) {
                    ySpriteCenterSonic = pixelyL - 76;
                }
                else if(pixelyR < pixelyL) {
                    ySpriteCenterSonic = pixelyR - 76;
                }
            }       
        }
    }
    
    private Ground getHighestSensorTile(Graphics2D g2, Rectangle sensor) {
        int xBottomSensor = (int) sensor.getX();
        int yBottomSensor = (int) (sensor.getY()+sensor.getHeight());
        
        if(sensor == bottomLeft) {
            g2.setColor(Color.MAGENTA);
            g2.fillRect(xBottomSensor,ySpriteCenterSonic+4,1,76);
            g2.fillRect(xBottomSensor,ySpriteCenterSonic+95,1,1);
        }
        if(sensor == bottomRight) {
            g2.setColor(Color.RED);
            g2.fillRect(xBottomSensor,ySpriteCenterSonic+4,1,76);
            g2.fillRect(xBottomSensor,ySpriteCenterSonic+95,1,1);
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
        //Variables that have to do with Sonic's animations:
        g2.setColor(Color.GREEN);
        g2.drawString("angle: "+angle, 400, 75);
    }
}