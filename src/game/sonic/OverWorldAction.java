/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.sonic;

import game.overworld.Tile;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author GeoSonicDash
 */
public class OverWorldAction extends Sonic {
    private static int xDrawCenterSonic = 1000; //center for drawing the picture
    private static int yDrawCenterSonic = 300; //center for drawing the picture
    private static int ySpriteCenterSonic; // Center of the actual sprite (basis for positions of collision boxes//The X Position for the center of the actual sprite is the same as xDrawCenterSonic   
    private static double groundSpeed = 0;
    private static double angle = 0;
    private static double xSpeed = 0;
    private static double ySpeed = 0; 
    private static int slope = 0;
    private static int direction = 1;
    private static int waitTimer = 0;
    private static int leftPress = 0;
    private static int rightPress = 0;
    private static final double ACCELERATION = 0.046875;
    private static final double DECELERATION = 0.5;
    private static final double FRICTION = 0.046875;
    private static final double TOP = 6;
    private static final double JUMP = 6.5;
    private static final double SLOPE = 0.125;
    private static final double SLOPEROLLUP = 0.078125;
    private static final double SLOPEROLLDOWN = 0.3125;
    private static final double FALL = 2.5;
    private static int collideWithSlope = 0;
    private static int bLCollide = 0;
    private static int bRCollide = 0;
    private static int mLCollide = 0;
    private static int mRCollide = 0;
    private static Rectangle topLeft;
    private static Rectangle bottomLeft;
    private static Rectangle topRight;
    private static Rectangle bottomRight;
    private static Rectangle middleLeft;
    private static Rectangle middleRight;
    private Animation animation;
    private Inventory inventory;
    public void standard(Graphics2D g2) {  
        /*try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(OverWorldAction.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        ySpriteCenterSonic = yDrawCenterSonic - 20;    
        if(collideWithSlope == 0) {
            bottomLeft = new Rectangle(xDrawCenterSonic-28,ySpriteCenterSonic,4,84);    
            bottomRight = new Rectangle(xDrawCenterSonic+28,ySpriteCenterSonic,4,84);
            middleLeft = new Rectangle(xDrawCenterSonic-36,ySpriteCenterSonic+32,36,4);    
            middleRight = new Rectangle(xDrawCenterSonic,ySpriteCenterSonic+32,44,4); 
        }
        else if(collideWithSlope == 1) {
            bottomLeft = new Rectangle(xDrawCenterSonic-28,ySpriteCenterSonic,4,148);    
            bottomRight = new Rectangle(xDrawCenterSonic+28,ySpriteCenterSonic,4,148);
            middleLeft = new Rectangle(xDrawCenterSonic-36,ySpriteCenterSonic+32,36,4);    
            middleRight = new Rectangle(xDrawCenterSonic,ySpriteCenterSonic+32,44,4); 
        }
        topLeft = new Rectangle(xDrawCenterSonic-28,ySpriteCenterSonic-80,4,80);       
        topRight = new Rectangle(xDrawCenterSonic+28,ySpriteCenterSonic-80,4,80);                  
        if(rightPress == 1) {
            rightPress();
        }
        else if(leftPress == 1) {
            leftPress();
        }
        else if(leftPress == 0 && rightPress == 0 && angle == 0) {
            groundSpeed -= Math.min(Math.abs(groundSpeed), FRICTION) * Math.signum(groundSpeed);
        }
        xSpeed = groundSpeed*Math.cos(angle);
        ySpeed = groundSpeed*-Math.sin(angle);
        if(rightPress == 0 && leftPress == 0) {
            waitTimer++;
            if(waitTimer >= 998 && waitTimer < 1000) {
                animation = new Animation();
                animation.setAnimationNumber(1);
            }
            else if(waitTimer >= 1000) {
                waitTimer = 1000;
            }
        }
        else {
            waitTimer = 0;
        }
        //Displaying variables here
        g2.drawString("rightPress: "+rightPress,100,175);
        g2.drawString("leftPress: "+leftPress,100,200);
        g2.drawString("bLCollide: "+bLCollide,100,225);
        g2.drawString("bRCollide: "+bRCollide,100,250);
        g2.drawString("mLCollide: "+mLCollide,100,275);
        g2.drawString("mRCollide: "+mRCollide,100,300);
        g2.drawString("direction: "+direction,100,325); //0 for left, 1 for right
        g2.drawString("waitTimer: "+waitTimer,100,350);
        g2.drawString("groundSpeed: "+groundSpeed,100,375); 
        g2.drawString("xSpeed: "+xSpeed,100,425);
        g2.drawString("ySpeed: "+ySpeed,100,450);
        g2.drawString("angle: "+angle,100,475); 
        g2.drawString("collideWithSlope: "+collideWithSlope,200,175);
        g2.drawString("yDrawCenterSonic: "+ySpriteCenterSonic,200,200);
        collisionCheck(g2);
        drawCollisionBoxes(g2);              
        xDrawCenterSonic+= (int) xSpeed;
        yDrawCenterSonic+= (int) ySpeed;
    }
    public void drawCollisionBoxes(Graphics2D g2) {
        //Draws rectangles themselves
        g2.setColor(Color.blue);
        g2.fillRect(xDrawCenterSonic-28,ySpriteCenterSonic-80,4,80);//topLeft       
        g2.setColor(Color.green);
        g2.fillRect(xDrawCenterSonic+28,ySpriteCenterSonic-80,4,80);//topRight
        if(collideWithSlope == 0) {
            g2.setColor(Color.red);
            g2.fillRect(xDrawCenterSonic-28,ySpriteCenterSonic,4,84);//bottomLeft    
            g2.setColor(Color.yellow);
            g2.fillRect(xDrawCenterSonic+28,ySpriteCenterSonic,4,84);//bottomRight
            g2.setColor(Color.orange);
            g2.fillRect(xDrawCenterSonic-36,ySpriteCenterSonic+32,36,4);//middleLeft
            g2.setColor(Color.cyan);
            g2.fillRect(xDrawCenterSonic,ySpriteCenterSonic+32,40,4);//middleRight
        }
        else if(collideWithSlope == 1) {
            g2.setColor(Color.red);
            g2.fillRect(xDrawCenterSonic-28,ySpriteCenterSonic,4,148);//bottomLeft    
            g2.setColor(Color.yellow);
            g2.fillRect(xDrawCenterSonic+28,ySpriteCenterSonic,4,148);//bottomRight
            g2.setColor(Color.orange);
            g2.fillRect(xDrawCenterSonic-36,ySpriteCenterSonic,40,4);//middleLeft
            g2.setColor(Color.cyan);
            g2.fillRect(xDrawCenterSonic,ySpriteCenterSonic,40,4);//middleRight
        }  
        //Draw collision rectangles
        g2.setColor(Color.blue);
        g2.drawString(""+topLeft.toString(),1000,100);
        g2.setColor(Color.red);
        g2.drawString(""+bottomLeft.toString(),1000,125);
        g2.setColor(Color.green);
        g2.drawString(""+topRight.toString(),1000,150);
        g2.setColor(Color.yellow);
        g2.drawString(""+bottomRight.toString(),1000,175);
        g2.setColor(Color.orange);
        g2.drawString(""+middleLeft.toString(),1000,200);
        g2.setColor(Color.cyan);
        g2.drawString(""+middleRight.toString(),1000,225);
        g2.setColor(Color.black); 
        g2.fillRect(xDrawCenterSonic, yDrawCenterSonic, 4, 4);
        g2.setColor(Color.red);
        g2.fillRect(xDrawCenterSonic,ySpriteCenterSonic,4,4);
    }
    public void collisionCheck(Graphics2D g2) {
        int xBottomLeft = (int) bottomLeft.getX();
        int yBottomLeft = (int) bottomLeft.getY()+ (int)bottomLeft.getHeight();
        int xBottomRight = (int) bottomRight.getX();
        int yBottomRight = (int) bottomRight.getY()+ (int)bottomRight.getHeight();
        int xMiddleLeft = (int) middleLeft.getX();
        int xMiddleRight = (int) middleRight.getX()+36;
        int pixelyL = ySpriteCenterSonic+200;
        int pixelyR = ySpriteCenterSonic+200;
        int heightBottomLeftIndex = 0;
        int heightBottomRightIndex = 0;
        Rectangle flatBoxRect = new Rectangle(0,0,0,0);
        double angleL = 0;
        double angleR = 0;
        int tileDirection = 0;
        for(Tile checkBoundary: environmentTiles) {
            g2.setColor(Color.red);
            g2.fillRect((int)checkBoundary.getXRef(),(int)checkBoundary.getYRef(),64,4);
            if(ySpeed >= 0) {
                if(checkBoundary.getAngle() != 0) {               
                    if(xBottomRight >= checkBoundary.getXRef() && xBottomRight < checkBoundary.getXRef()+64 && 
                    yBottomRight >= checkBoundary.getYRef() && ySpriteCenterSonic < checkBoundary.getYRef()){//Checks if Sonic is with 64x64 tile 
                        //(before calculations                 
                        collideWithSlope = 1;
                        heightBottomRightIndex = (int) Math.abs(((xBottomRight - checkBoundary.getXRef())/4));//gets specific height of pixel (depends
                        //on sensor's x position relative to the tile's xRef (abs to avoid negatives)                   
                        g2.drawString("heightBottomRightIndex: "+heightBottomRightIndex, 600, 225);
                        g2.drawString("Height value of pixel (in 16x16 tile)"+checkBoundary.heightValues.get(heightBottomRightIndex), 600, 250);
                        pixelyR = (checkBoundary.getYRef()+64-(checkBoundary.heightValues.get(heightBottomRightIndex)*4));//gets pixel's height relative
                        //to screen
                        angleR = checkBoundary.getAngle();  
                        tileDirection = checkBoundary.getDirection();
                        bRCollide = 1;                       
                        break;                         
                    }
                    else {
                        bRCollide = 0;
                    }        
                }                                                         
                else if(checkBoundary.getAngle() == 0) {
                    if(xBottomRight > checkBoundary.getXRef() && xBottomRight < checkBoundary.getXRef()+64 && 
                        yBottomRight > checkBoundary.getYRef() && yBottomRight < checkBoundary.getYRef()+100) {
                        //the -4 is the offset (since the block is 16x16 the Sensor is not technically within the tile so the -4 extends it to 16x17 
                        //instead)
                        g2.drawString("Height value of pixel (in 16x16 tile)"+checkBoundary.heightValues.get(0), 600, 250);
                        flatBoxRect = checkBoundary.getPixelBox(0);
                        bRCollide = 1;
                        pixelyR = (checkBoundary.getYRef());
                        collideWithSlope = 0;
                        break;
                    }               
                }
                else {
                    bRCollide = 0;
                }
            }
        }
        for(Tile checkBoundary: environmentTiles) {
            if(ySpeed >= 0) {
                if(checkBoundary.getAngle() != 0) {              
                    if(xBottomLeft > checkBoundary.getXRef() && xBottomLeft < checkBoundary.getXRef()+64 && 
                           yBottomLeft > checkBoundary.getYRef()-4 && ySpriteCenterSonic < checkBoundary.getYRef()) {//Checks if Sonic is with 64x64 tile 
                        //(before calculations                   
                        collideWithSlope = 1;
                        heightBottomLeftIndex = (int) Math.abs(((xBottomLeft - checkBoundary.getXRef())/4));//gets specific height of pixel (depends
                        //on sensor's x position relative to the tile's xRef (abs to avoid negatives)
                        pixelyL = (checkBoundary.getYRef()+64-(checkBoundary.heightValues.get(heightBottomLeftIndex)*4));//gets pixel's height relative                   
                        //to screen    
                        angleL = checkBoundary.getAngle();      
                        tileDirection = checkBoundary.getDirection();
                        bLCollide = 1;                       
                        break;
                    } 
                    else {
                        bLCollide = 0;
                    }
                }
                else if(checkBoundary.getAngle() == 0) {
                    if(xBottomLeft > checkBoundary.getXRef() && xBottomLeft < checkBoundary.getXRef()+64 && 
                        yBottomLeft > checkBoundary.getYRef() && yBottomLeft < checkBoundary.getYRef()+100) {
                        //the -4 is the offset (since the block is 16x16 the Sensor is not technically within the tile so the -4 extends it to 16x17 
                        //instead) 
                        bLCollide = 1;
                        flatBoxRect = checkBoundary.getPixelBox(0);
                        pixelyL = (checkBoundary.getYRef());
                        collideWithSlope = 0;                   
                        break;
                    }
                    else {
                        bLCollide = 0;
                    }   
                }
            }
        }
        if(pixelyR > pixelyL && pixelyL != 0 && pixelyR != 0) {
            yDrawCenterSonic = pixelyL - 60;//Changes Sonic's yPosition   
            angle = angleL;
        } 
        else if(pixelyL > pixelyR && pixelyL != 0 && pixelyR != 0) {
            yDrawCenterSonic = pixelyR - 60;//Changes Sonic's yPosition   
            angle = angleR;
        }  
        if(pixelyL == pixelyR) {
            angle = 0;
        }
        if(tileDirection == 0) {
            groundSpeed -= SLOPE*(-Math.sin(angle));    
        }
        else if(tileDirection == 1) {
            groundSpeed -= SLOPE*(Math.sin(angle));    
        }        
        g2.drawString("pixelyL:"+pixelyL,500,100);
        g2.drawString("pixelyR:"+pixelyR,600,100);
        for(Tile checkBoundary: environmentTiles) {
            if(xMiddleRight > checkBoundary.getXRef()-64 && xMiddleRight < checkBoundary.getXRef()+64) {//Checks if Sonic is with 64x64 tile 
                //(before calculations
                Rectangle collisionPixel = checkBoundary.getPixelBox(0);
                int x = (int) collisionPixel.getX() - 8;
                if(middleRight.intersects(collisionPixel) && middleRight.getX() < x && leftPress == 0) {
                    groundSpeed = 0;
                    mRCollide = 1;
                    break;
                }
            }
            else {
                mRCollide = 0;
            }
        }
        for(Tile checkBoundary: environmentTiles) {
            if(xMiddleLeft > checkBoundary.getXRef() && xMiddleLeft < checkBoundary.getXRef()+72) {//Checks if Sonic is with 64x64 tile 
                //(before calculations
                Rectangle collisionPixel = checkBoundary.getPixelBox(0);
                int x = (int) collisionPixel.getX() + 70;
                if(middleLeft.intersects(collisionPixel) && middleLeft.getX() < x && rightPress == 0) {
                    groundSpeed = 0;
                    mLCollide = 1;
                    break;
                }
            }
            else {
                mLCollide = 0;
            }
        }      
        if(xDrawCenterSonic > (int) flatBoxRect.getX()+68 && bRCollide == 0 && bLCollide == 1 && leftPress == 0 && rightPress == 0 && groundSpeed == 0 && angle == 0) {
            animation = new Animation();
            animation.setAnimationNumber(5);
        }
        if(xDrawCenterSonic < (int) flatBoxRect.getX()-4 && bRCollide == 1 && bLCollide == 0 && leftPress == 0 && rightPress == 0 && groundSpeed == 0 && angle ==  0) {
            animation = new Animation();
            animation.setAnimationNumber(4);
        }
    }
    public void rightPress() {
        direction = 1;
        if(mRCollide == 0) {
            animation = new Animation();
            animation.setAnimationNumber(3);
        }
        else {
            animation = new Animation();
            animation.setAnimationNumber(14);
        }  
        if (groundSpeed < 0) {        
            groundSpeed += DECELERATION;
            if (groundSpeed >= 0) {
                groundSpeed = 0.5;   
            }                
        }
        else if (groundSpeed < TOP) {        
            groundSpeed += ACCELERATION;
            if (groundSpeed >= TOP) {
                groundSpeed = TOP;    
            }               
        }
    }
    public void leftPress() {
        direction = 0;
        if(mLCollide == 0) {
            animation = new Animation();
            animation.setAnimationNumber(3); 
        }
        else {
            animation = new Animation();
            animation.setAnimationNumber(13);
        }
        if(mLCollide == 0) {
            
        }
        if(groundSpeed > 0) {
            groundSpeed -= DECELERATION;           
            if(groundSpeed <= 0) {
               groundSpeed = -0.5; 
            }           
        }
        else if(groundSpeed > -TOP) {   
            groundSpeed -= ACCELERATION;
            if(groundSpeed <= -TOP) {
               groundSpeed = -TOP; 
            }            
        }        
    }
    public int getXCenterSonic() {
        return xDrawCenterSonic;
    }
    public int getYCenterSonic() {
        return yDrawCenterSonic;
    }
    public int getDirection() {
        return direction;
    }
    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == e.VK_RIGHT) {
            animation = new Animation();
            animation.setAnimationNumber(0);
            rightPress = 0;
        }
        if(e.getKeyCode() == e.VK_LEFT) {
            animation = new Animation();
            animation.setAnimationNumber(0);
            leftPress = 0;
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == e.VK_RIGHT ) {
            rightPress = 1;
            leftPress = 0;
        }
        if (e.getKeyCode() == e.VK_LEFT ) {  
            leftPress = 1;
            rightPress = 0;
        }          
        if (e.getKeyCode() == e.VK_UP ) {
            yDrawCenterSonic-=4;
        }
        if (e.getKeyCode() == e.VK_DOWN) {
            yDrawCenterSonic+=4;
        }
        if (e.getKeyCode() == e.VK_Z ) {
            yDrawCenterSonic = 300;
            xDrawCenterSonic = 1000;
        }
        if (e.getKeyCode() == e.VK_X ) {
              
        }
        if (e.getKeyCode() == e.VK_ENTER) {
            
        }                 
    }//end keypressed


    
}
