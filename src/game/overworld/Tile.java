/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 *
 * @author GeoSonicDash
 */
public class Tile {
    private int xRef;
    private int yRef;
    private int angle;
    private int direction;
    private int length;
    private int width;
    public ArrayList<Integer> heightValues = new ArrayList<Integer>();
    private ArrayList<Rectangle> pixelBoxes = new ArrayList<Rectangle>();
    public Tile(int xRef, int yRef, int angle, int direction) {
        this.xRef = xRef;
        this.yRef = yRef;
        this.angle = angle;
        this.direction = direction;
        this.length = 16;
        this.width = 16;
    }
    public Tile(int xRef, int yRef, int angle, int direction, int length, int width) {
        this.xRef = xRef;
        this.yRef = yRef;
        this.angle = angle;
        this.direction = direction;
        this.length = length;
        this.width = width;
    }

    public void create() {
        if(angle == 0) {               
            heightValues.add(width);
            pixelBoxes.add(new Rectangle(xRef,yRef,length*4,width*4));   
        }
        else if(angle == 15) {
            for(int i = 0; i < 16; i++ ) {
                if(i < 5) {
                    heightValues.add(i);    
                }
                else { 
                    heightValues.add(5);
                }
                pixelBoxes.add(new Rectangle((xRef+(i*4)),(yRef+64-(heightValues.get(i)*4)),4,(heightValues.get(i)*4)));
            }
        }
        else if(angle == 45) {
            if(direction == 0) {
                for(int i = 0; i < 16; i++ ) {                
                    heightValues.add(16-i);
                    pixelBoxes.add(new Rectangle((xRef+(i*4)),(yRef+64-(heightValues.get(i)*4)),4,(heightValues.get(i)*4)));
                }  
            }
            else if(direction == 1) {                   
              for(int i = 0; i < 16; i++ ) {                
                heightValues.add(i);
                pixelBoxes.add(new Rectangle((xRef+(i*4)),(yRef+64-(heightValues.get(i)*4)),4,(heightValues.get(i)*4)));
                }  
            }                      
        }
    }
    public void drawRectangle(Graphics2D g2) {
        g2.setColor(Color.black);
        g2.fillRect(xRef, yRef, length*4, width*4);
    }
    public void drawSlope(Graphics2D g2) {
        for(int i = 0; i < 16; i ++) {
            g2.setColor(Color.black);
            g2.fillRect((xRef+(i*4)),(yRef+64-(heightValues.get(i)*4)),4,(heightValues.get(i)*4));
        }        
    }
    public Rectangle getPixelBox(int selection) {
        return pixelBoxes.get(selection);
    }
    public int getXRef() {
        return xRef;
    }
    public int getYRef() {
        return yRef;
    }
    public int getAngle() {
        return angle;
    }
    public int getDirection() {
        return direction;
    }
    public int getLength() {
        return length;
    }
    @Override
    public String toString() {
        return "Stats of tile Sonic is colliding with- xRef: "+xRef+", yRef: "+yRef+", angle: "+angle+", direction: "+direction;
    }
}
