/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;

/**
 *
 * @author GeoSonicDash
 */
public class Ground extends OverWorld {
    private GroundType groundType;
    private int xRef;
    private int yRef;
    private int length;
    private int width;
    private int angle;
    private int direction;
    private boolean loadFile;
    private Image groundPicture;
    private ArrayList<Integer> heightValues = new ArrayList<Integer>();
    private ArrayList<Rectangle> pixelBoxes = new ArrayList<Rectangle>();
    public Ground(GroundType groundType ,int xRef, int yRef, int direction) {
        this.groundType = groundType;
        this.xRef = xRef;
        this.yRef = yRef;
        this.direction = direction;
        this.loadFile = false;
    }
    public void create() {
        //0 = left, 1 = right
        if(!loadFile) {
            if(groundType == GroundType.GRD_SONICHOUSE_WOODPLANK) {
                length = 16;
                width = 16;
                angle = 0;
                groundPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic House Wood Plank_1.png");
            }
            else if(groundType == GroundType.GRD_SONICHOUSE_WOODSLOPE) {
                length = 16;
                width = 16;
                if(direction == 0) {
                    groundPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\LSonic House Wood Slope_1.png");    
                }
                else if(direction == 1) {
                    groundPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic House Wood Slope_1.png");    
                }
                angle = 45;           
            }    
        }
        loadFile = true;
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
        g2.drawImage(groundPicture, xRef, yRef, length*4, width*4, this);
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
    public enum GroundType {
        GRD_SONICHOUSE_WOODPLANK,
        GRD_SONICHOUSE_WOODSLOPE
    }
}
