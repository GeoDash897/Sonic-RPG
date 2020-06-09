/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gameObjects;

import game.overworld.Ground;
import game.overworld.Picture;
import static game.gameObjects.BasicObject.Direction;
import game.overworld.Room;
import game.sonic.OWARemastered;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

/**
 *
 * @author GeoSonicDash
 */
public class BasicObject implements Picture, Interactable  {
    private int xRef;
    private int yRef;
    private int xDraw;
    private int yDraw;
    private int bLXOffsetFromCenter;
    private int bLYOffsetFromCenter;
    private int bRXOffsetFromCenter;
    private int bRYOffsetFromCenter;
    private int bIXOffsetFromCenter;
    private int bIYOffsetFromCenter;
    private double xSpeed;
    private double ySpeed;
    private int length;
    private int width;
    private int layer;
    private Direction direction;
    private boolean ground;
    private boolean gravity;
    private boolean bLCollide;
    private boolean bRCollide;
    private Rectangle bottomLeft;
    private Rectangle bottomRight;
    private Rectangle intersectBox;
    
    private static double GRAVITY = 0.21875;
    
    private Image picture;
    private Room objectRoom;
    public BasicObject(Room objectRoom) {
        this.objectRoom = objectRoom;
    }
    public void createObject(int layer, int xRef, int yRef, int length, int width, Rectangle bottomLeft, 
            Rectangle bottomRight, Rectangle intersectBox, boolean ground, boolean gravity, Image picture) {
        this.layer = layer;
        this.xRef = xRef;
        this.yRef = yRef;
        this.xSpeed = 0;
        this.ySpeed = 0;
        this.length = length*4;
        this.width = width*4;
        this.direction = Direction.DIRECTION_RIGHT;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.intersectBox = intersectBox;
        if(bottomLeft != null) {
            this.bLXOffsetFromCenter = (int)(bottomLeft.getX()-xRef);
            this.bLYOffsetFromCenter = (int)(bottomLeft.getY()-yRef);
        }
        if(bottomRight != null) {
            this.bRXOffsetFromCenter = (int) (bottomRight.getX()-xRef);
            this.bRYOffsetFromCenter = (int) (bottomRight.getY()-yRef);
        }
        if(intersectBox != null) {
            this.bIXOffsetFromCenter = (int)(intersectBox.getX()-xRef);
            this.bIYOffsetFromCenter = (int)(intersectBox.getY()-yRef);
        }       
        this.ground = ground;
        this.gravity = gravity;
        this.bLCollide = false;
        this.bRCollide = false;
        this.picture = picture;
    }
    
    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(picture, xDraw, yDraw, length, width, null);        
        g2.setColor(Color.RED);
        if(bottomLeft != null) {
            g2.fill(bottomLeft);
        }        
        g2.setColor(Color.MAGENTA);
        if(bottomRight != null) {
            g2.fill(bottomRight);
        }
        g2.fillRect(xRef, yRef,1,1);
        g2.setColor(Color.YELLOW);
        if(intersectBox != null) {
            g2.draw(intersectBox);
        }
    }
    
    public void action() {
        xDraw = xRef-(length/2);
        yDraw = yRef-(width/2);
        if(bottomLeft != null) {
            bottomLeft = new Rectangle((int)(xRef+bLXOffsetFromCenter), (int)(yRef+bLYOffsetFromCenter), (int)bottomLeft.getWidth(), (int)bottomLeft.getHeight());
        }
        if(bottomRight != null) {
            bottomRight = new Rectangle((int)(xRef+bRXOffsetFromCenter), (int)(yRef+bRYOffsetFromCenter), (int)bottomRight.getWidth(), (int)bottomRight.getHeight()); 
        }
        if(intersectBox != null) {
                    intersectBox = new Rectangle((int)(xRef+bIXOffsetFromCenter), (int)(yRef+bIYOffsetFromCenter), (int)intersectBox.getWidth(), (int)intersectBox.getHeight());
        }                
        if(gravity) {
            if(!ground) {                
                int xBottomLeft = (int) bottomLeft.getX();
                int xBottomRight = (int) bottomRight.getX();  
                int yBottomSensor = (int) (bottomLeft.getY()+bottomLeft.getHeight());
                int heightBottomLeftIndex = 0;
                int heightBottomRightIndex = 0;
                int pixelyL = yBottomSensor;
                int pixelyR = yBottomSensor;
                /*int pixelyL = yBottomSensor+80;
                int pixelyR = yBottomSensor+80;*/
                Rectangle groundCheckL;
                Rectangle groundCheckR;
                Ground highLeft = getCorrectTile(yBottomSensor,bottomLeft);
                Ground highRight = getCorrectTile(yBottomSensor,bottomRight);
                if(highLeft != null) {
                    heightBottomLeftIndex = (int) Math.abs(((xBottomLeft - highLeft.getXRef())/4));   
                    pixelyL = (int) highLeft.getPixelBox(heightBottomLeftIndex).getY();
                    groundCheckL = highLeft.getPixelBox(heightBottomLeftIndex);
                    if(bottomLeft.intersects(groundCheckL)) {
                        bLCollide = true;
                    }            
                }
                if(highRight != null) {
                    heightBottomRightIndex = (int) Math.abs(((xBottomRight - highRight.getXRef())/4));
                    pixelyR = (int) highRight.getPixelBox(heightBottomRightIndex).getY();
                    groundCheckR = highRight.getPixelBox(heightBottomRightIndex);
                    if(bottomRight.intersects(groundCheckR)) {
                        bRCollide = true;
                    }            
                }
                ySpeed += GRAVITY;
                if(bLCollide || bRCollide) {
                    ground = true;
                    ySpeed = 0;
                }                                 
                setCorrectHeight(pixelyL, pixelyR, yBottomSensor);
            }
        }
        xRef+= (int) xSpeed;
        yRef+= (int) ySpeed;
    }
   
    @Override
    public void interactWithSonic(OWARemastered owaR) {
        
    }
    /**
     * This method corrects the yRef of BasicObject- helps to prevent it from being stuck
     * halfway into the ground (prevent it from looking wrong).
     * @param pixelyL the position that the bottomLeft sensor senses (remember its relative to the entire screen).
     * @param pixelyR the position that the bottomRight sensor senses (remember its relative to the entire screen).
     * @param yBottomSensor the y position of the bottom of the sensors.
     */
    private void setCorrectHeight(int pixelyL, int pixelyR, int yBottomSensor) {
        if(pixelyL > pixelyR) {
            yRef = pixelyR-(int)bottomLeft.getHeight();
        }
        else if(pixelyL < pixelyR) {
            yRef = pixelyL-(int)bottomLeft.getHeight();
        }
        else if(pixelyL == pixelyR && pixelyR != yBottomSensor){
            yRef = pixelyR-(int)bottomLeft.getHeight();
        }
    }
    
    public Ground getCorrectTile(int yBottomSensor, Rectangle sensor) {
        int xBottomSensor = (int) sensor.getX();     
        int xBottomIndex = xBottomSensor/64;
        int yBottomIndex = yBottomSensor/64;       
        Ground intersect = objectRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex);
        if(intersect != null) {
                Ground intersectUp = objectRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex-1);
                Ground higher = Ground.compareSCTile(xBottomSensor, intersect, intersectUp, layer);
                return higher;    
        }
        else {
            Ground intersectDown = objectRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex+1);
            Ground higher = Ground.compareSCTile(xBottomSensor, intersect, intersectDown, layer);
            return higher;    
        }
    }
    
    public Rectangle getIntersectBox() {
        return intersectBox;
    }
    
    public int getXRef() {
        return xRef;
    }
    
    public int getYRef() {
        return yRef;
    }
    
    public int getLength() {
        return length;
    }
    
    @Override
    public int getLayer() {
        return layer;
    }
    
    public Room getObjectRoom() {
        return objectRoom;
    }
    
    public Direction getDirection() {
        return direction;
    }
    
    public void setDirection(Direction temp) {
        direction = temp;
    }
    
    public Image getImage() {
        return picture;
    }
    
    public void setPicture(Image temp) {
        picture = temp;
    }
    
    public enum Direction {
        DIRECTION_LEFT,
        DIRECTION_RIGHT
    }
}
