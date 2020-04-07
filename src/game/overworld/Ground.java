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
public class Ground extends OverWorld implements Picture {
    private GroundType groundType;
    private int xIndex;
    private int yIndex;
    private int xRef;
    private int yRef;
    private int length;
    private int width;
    private int layer;
    private int angle;
    private int direction;
    private boolean loadFile;
    private Color boundary;
    private Image groundPicture;
    private ArrayList<Integer> heightValues;
    private ArrayList<Rectangle> pixelBoxes;
    public Ground(GroundType groundType ,int layer, int xRef, int yRef, int direction) {
        this.groundType = groundType;
        this.layer = layer;
        this.xRef = xRef;
        this.yRef = yRef;
        this.direction = direction;
        this.heightValues = new ArrayList<Integer>();
        this.pixelBoxes = new ArrayList<Rectangle>();
        create();
    }
    public void create() {
        /*NOTE!- When creating the height values of tiles, try to not have any height values of 0 for the pixelBoxes (it makes comparing tiles a lot
        more complicated*/
        //0 = left, 1 = right
            int r = (int)(Math.random() * ((255 - 0) + 1)) + 0;
            int g = (int)(Math.random() * ((255 - 0) + 1)) + 0;
            int b = (int)(Math.random() * ((255 - 0) + 1)) + 0;
            xIndex = xRef/64;
            yIndex = yRef/64;
            boundary = new Color(r, g, b);
            if(groundType == GroundType.GRD_SONICHOUSE_WOODPLANK) {
                length = 16;
                width = 16;
                angle = 0;
                setBlock();
                groundPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Wood Tile Middle_1.png");
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
            else if(groundType == GroundType.GRD_SONICHOUSE_BIGWOODPLANK) {
                length = 480;
                width = 16;
                angle = 0;
                setBlock();
            }
            else if(groundType == GroundType.GRD_SONICHOUSE_SONICBED) {
                length = 64;
                width = 34;
                angle = 0;
                groundPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic's Bed.png");
                setSonicBedHeightValues();
            }
            if(angle == 45) {
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
    private void setBlock() {
        for(int i = 0; i < length; i ++) {
            heightValues.add(width);
            pixelBoxes.add(new Rectangle((xRef+(i*4)),(yRef+(width*4)-(heightValues.get(i)*4)),4,(heightValues.get(i)*4)));
        }
    }
    private void setSonicBedHeightValues() {
        heightValues.add(32);
        heightValues.add(32);
        heightValues.add(34);
        heightValues.add(34);
        heightValues.add(32);
        heightValues.add(32);
        heightValues.add(24);
        heightValues.add(24);
        heightValues.add(24);
        heightValues.add(24);
        heightValues.add(24);
        heightValues.add(24);
        heightValues.add(22);
        heightValues.add(22);
        heightValues.add(20);
        heightValues.add(20);
        heightValues.add(22);
        heightValues.add(22);
        heightValues.add(20);
        heightValues.add(20);
        for(int i = 0; i < 38; i ++) {
            heightValues.add(22);    
        }
        heightValues.add(20);
        heightValues.add(20);
        heightValues.add(18);
        heightValues.add(18);
        heightValues.add(16);       
        heightValues.add(16);
        for(int i = 0; i < heightValues.size(); i++) {
            pixelBoxes.add(new Rectangle((xRef+(i*4)),(yRef+(width*4)-(heightValues.get(i)*4)),4,(heightValues.get(i)*4)));
        }
    }
    @Override
    public void draw(Graphics2D g2) {  
        g2.setColor(Color.CYAN);
        for(Rectangle temp : pixelBoxes) {
            g2.draw(temp);
        }
        g2.setColor(boundary);
        g2.fillRect(xRef, yRef, 64, 1);
        g2.fillRect(xRef, yRef+63, 64, 1);
        g2.fillRect(xRef, yRef, 1, 64);
        g2.fillRect(xRef+63, yRef, 1, 64);
        //g2.drawImage(groundPicture, xRef, yRef, length*4, width*4, this);
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
    public int getWidth() {
        return width;
    }
    @Override
    public int getLayer() {
        return layer;
    }
    public boolean isSameLayer(int otherLayer) {
        return otherLayer == this.layer;
    }
    public ArrayList<Integer> getHeightValues() {
        return heightValues;
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
    //Compares tiles in same column
    public static Ground compareSCTile(int xBottomSensor, Ground tile1, Ground tile2) {
        //Change it so it initially sets each rectangle to null, this changes if this or other is not null (and gets rect)
        Rectangle rectTile1 = null;
        Rectangle rectTile2 = null;
        if(tile1 != null) {
            int heightIndex = (int) Math.abs((xBottomSensor-tile1.getXRef())/4);
            if(heightIndex == 0) {
                int max = Integer.MIN_VALUE;
                int positionMax = -1;
                for(int i = 0; i < tile1.getHeightValues().size(); i++) {
                    if(tile1.getHeightValues().get(i) >= max) {
                        max = tile1.getHeightValues().get(i);
                        positionMax = i;
                    }
                }               
                rectTile1 = tile1.getPixelBox(positionMax);    
            }
            else {
                rectTile1 = tile1.getPixelBox(heightIndex);    
            }
        }
        if(tile2 != null) {
            int heightIndex = (int) Math.abs((xBottomSensor-tile2.getXRef())/4);
           if(heightIndex == 0) {
                int max = Integer.MIN_VALUE;
                int positionMax = -1;
                for(int i = 0; i < tile2.getHeightValues().size(); i++) {
                    if(tile2.getHeightValues().get(i) >= max) {
                        max = tile2.getHeightValues().get(i);
                        positionMax = i;
                    }
                }               
                rectTile2 = tile2.getPixelBox(positionMax);    
            }
            else {
                rectTile2 = tile2.getPixelBox(heightIndex);    
            }
        }
        /*System.out.println("rectTile1: "+rectTile1);
        System.out.println("rectTile2: "+rectTile2);*/
        /*If the rectangle is null, that means that: 
        1. the specific index of a tile (that exists) doesn't have a rectangle
        2. the tile does not exist
        */
        if(rectTile1 != null && rectTile2 != null) {           
            if((int) rectTile1.getY() < (int) rectTile2.getY()) {
                return tile1;
            }
            else if((int) rectTile1.getY() > (int) rectTile2.getY()) {
                return tile2;
            }
        }
        else if(rectTile1 != null && rectTile2 == null) {
            return tile1;
        }
        else if(rectTile1 == null && rectTile2 != null) {
            return tile2;
        }
        return null;       
    }
    public static Ground compareDCTile(int xBottomLeft, int xBottomRight, Ground tile1, Ground tile2) {
        //Change it so it initially sets each rectangle to null, this changes if this or other is not null (and gets rect)
        Rectangle rectTile1 = null;
        Rectangle rectTile2 = null;
        if(tile1 != null) {
            int heightIndex = (int) Math.abs((xBottomLeft-tile1.getXRef())/4);
            if(heightIndex == 0) {
                rectTile1 = tile1.getPixelBox(15);    
            }
            else {
                rectTile1 = tile1.getPixelBox(heightIndex);    
            }
        }
         if(tile2 != null) {
            int heightIndex = (int) Math.abs((xBottomRight-tile2.getXRef())/4);
            if(heightIndex == 0) {
                rectTile2 = tile2.getPixelBox(15);    
            }
            else {
                rectTile2 = tile2.getPixelBox(heightIndex);    
            }
        }
        /*If the rectangle is null, that means that: 
        1. the specific index of a tile (that exists) doesn't have a rectangle
        2. the tile does not exist
        */
        if(rectTile1 != null && rectTile2 != null) {
            if((int) rectTile1.getY() < (int) rectTile2.getY()) {
                return tile1;
            }
            else if((int) rectTile1.getY() > (int) rectTile2.getY()) {
                return tile2;
            }
            else if((int) rectTile1.getY() == (int) rectTile2.getY()) {
                return tile1;
            }
        }
        else if(rectTile1 != null && rectTile2 == null) {
            return tile1;
        }
        else if(rectTile1 == null && rectTile2 != null) {
            return tile2;
        }
        return null;    
    }
    @Override
    public String toString() {
        return "xIndex: "+xIndex+", yIndex: "+yIndex+", GroundType:"+groundType.toString()+", Layer: "+layer;
    }
    public enum GroundType {
        GRD_SONICHOUSE_WOODPLANK,
        GRD_SONICHOUSE_WOODSLOPE,
        GRD_SONICHOUSE_BIGWOODPLANK,
        GRD_SONICHOUSE_SONICBED,
    }
}
