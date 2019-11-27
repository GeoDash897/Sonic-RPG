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
public class Ground {
    private int id;
    private int xRef;
    private int yRef;
    private int length;
    private int width;
    private int angle;
    private int direction;

    private ArrayList<Integer> heightValues = new ArrayList<Integer>();
    private ArrayList<Rectangle> pixelBoxes = new ArrayList<Rectangle>();
    public Ground(int id,int xRef, int yRef, int length, int width, int angle, int direction) {
        this.id = 0;
        this.xRef = xRef;
        this.yRef = yRef;
        this.length = length;
        this.width = width;
        this.angle = angle;
        this.direction = direction;
    }
    public void create() {
        if(angle == 0) {               
            heightValues.add(width);
            pixelBoxes.add(new Rectangle(xRef,yRef,length*4,width*4));   
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
    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        if(angle == 0) {
            g2.fillRect(xRef,yRef,length*4,width*4);     
        }
        else {
            for(int i = 0; i < 16; i ++) {
                g2.setColor(Color.black);
                g2.fillRect((xRef+(i*4)),(yRef+64-(heightValues.get(i)*4)),4,(heightValues.get(i)*4));
            } 
        }
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
    public Rectangle getPixelBox(int selection) {
        return pixelBoxes.get(selection);
    }
    public int getHeightValueInArrayList(int selection) {
        return heightValues.get(selection);
    }
    public ArrayList<Rectangle> getPixelBoxes() {
        return pixelBoxes;
    }
}
