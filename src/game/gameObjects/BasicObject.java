/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gameObjects;

import game.overworld.Ground;
import game.overworld.Picture;
import game.overworld.Room;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

/**
 *
 * @author GeoSonicDash
 */
public class BasicObject implements Picture  {
    private int xRef;
    private int yRef;
    private double xSpeed;
    private double ySpeed;
    private int length;
    private int width;
    private int layer;
    private boolean ground;
    private boolean gravity;
    private boolean bLCollide;
    private boolean bRCollide;
    private Rectangle bottomLeft;
    private Rectangle bottomRight;
    
    private static double GRAVITY = 0.21875;
    
    private Image picture;
    private Room objectRoom;
    public BasicObject(Room objectRoom) {
        this.objectRoom = objectRoom;
    }
    public void createObject(int layer, int xRef, int yRef, int length, int width, Rectangle bottomLeft, 
            Rectangle bottomRight, boolean ground, boolean gravity, Image picture) {
        this.layer = layer;
        this.xRef = xRef;
        this.yRef = yRef;
        this.xSpeed = 0;
        this.ySpeed = 0;
        this.length = length;
        this.width = width;
        this.bottomLeft = bottomLeft;
        this.bottomRight = bottomRight;
        this.ground = ground;
        this.gravity = gravity;
        this.bLCollide = false;
        this.bRCollide = false;
        this.picture = picture;
    }
    
    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(picture, xRef, yRef, length*4, width*4, null);
        g2.setColor(Color.RED);
        g2.fill(bottomLeft);
        g2.setColor(Color.MAGENTA);
        g2.fill(bottomRight);       
    }
    
    public void action() {
        bottomLeft = new Rectangle((int)(bottomLeft.getX()+xSpeed), (int)(bottomLeft.getY()+ySpeed), (int)bottomLeft.getWidth(), (int)bottomLeft.getHeight());
        bottomRight = new Rectangle((int)(bottomRight.getX()+xSpeed), (int)(bottomRight.getY()+ySpeed), (int)bottomRight.getWidth(), (int)bottomRight.getHeight());       
        if(gravity) {
            if(!ground) {
                ySpeed += GRAVITY;
                int xBottomLeft = (int) bottomLeft.getX();
                int xBottomRight = (int) bottomRight.getX();  
                int yBottomSensor = (int) (bottomLeft.getY()+bottomLeft.getHeight());
                int heightBottomLeftIndex = 0;
                int heightBottomRightIndex = 0;
                /*int pixelyL = yBottomSensor+80;
                int pixelyR = yBottomSensor+80;*/
                Rectangle groundCheckL;
                Rectangle groundCheckR;
                Ground highLeft = getCorrectTile(yBottomSensor,bottomLeft);
                Ground highRight = getCorrectTile(yBottomSensor,bottomRight);
                if(highLeft != null) {
                    heightBottomLeftIndex = (int) Math.abs(((xBottomLeft - highLeft.getXRef())/4));   
                    //pixelyL = (int) highLeft.getPixelBox(heightBottomLeftIndex).getY();
                    groundCheckL = highLeft.getPixelBox(heightBottomLeftIndex);
                    if(bottomLeft.intersects(groundCheckL)) {
                        bLCollide = true;
                    }            
                }
                if(highRight != null) {
                    heightBottomRightIndex = (int) Math.abs(((xBottomRight - highRight.getXRef())/4));
                    //pixelyR = (int) highRight.getPixelBox(heightBottomRightIndex).getY();
                    groundCheckR = highRight.getPixelBox(heightBottomRightIndex);
                    if(bottomRight.intersects(groundCheckR)) {
                        bRCollide = true;
                    }            
                }
                if(bLCollide || bRCollide) {
                    ground = true;
                    ySpeed = 0;
                }
            }            
        }
        xRef+= (int) xSpeed;
        yRef+= (int) ySpeed;
    }

    private Ground getCorrectTile(int yBottomSensor, Rectangle sensor) {
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
    
    public Rectangle getSensor() {
        return bottomRight;
    }
    
    @Override
    public int getLayer() {
        return layer;
    }
}
