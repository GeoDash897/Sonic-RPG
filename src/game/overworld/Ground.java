/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import game.sonic.Sonic;
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
    private String groundName;
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
        this.heightValues = new ArrayList<Integer>(16);
        this.pixelBoxes = new ArrayList<Rectangle>(16);
        create();
    }
    public void create() {
        groundName = String.valueOf(groundType);
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
                if(direction == 0) {
                    angle = -45;
                }    
                else if(direction == 1) {
                    angle = 45;
                }           
            }    
            else if(groundName.substring(0,groundName.length()-3).equals("GRD_SONICHOUSE_SONICBED")) { 
                String part = groundName.substring(groundName.length()-2);
                length = 16;
                width = 16;
                angle = 0;
                groundPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Sonic's Bed_"+part+".png");
                setSonicBedHeightValues(Integer.valueOf(part));
            }
            if(Math.abs(angle) == 45) {
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
        }
        createPixelBoxesArrayList();
    }
    private void createPixelBoxesArrayList() {
        for(int i = 0; i < heightValues.size(); i++ ) {                
            pixelBoxes.add(new Rectangle((xRef+(i*4)),(yRef+64-(heightValues.get(i)*4)),4,(heightValues.get(i)*4)));
        }
    }
    private void setSonicBedHeightValues(int part) {
        if(part == 01 || part == 11 || part == 21 || part == 31) {
            setBlock();   
        } 
        else if(part == 00) {
            heightValues.add(16);
            heightValues.add(16);
            heightValues.add(16);
            heightValues.add(16);
            heightValues.add(16);
            heightValues.add(16);
            heightValues.add(8);
            heightValues.add(8);
            heightValues.add(8);
            heightValues.add(8);
            heightValues.add(8);
            heightValues.add(8);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            createPixelBoxesArrayList();
        }
        else if(part == 10) {
            heightValues.add(4);
            heightValues.add(4);
            heightValues.add(4);
            heightValues.add(4); 
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            createPixelBoxesArrayList();
        }
        else if(part == 20) {
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6); 
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            createPixelBoxesArrayList();
        }
        else if(part == 30) {
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6); 
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(6);
            heightValues.add(5);
            heightValues.add(5);
            heightValues.add(4);
            heightValues.add(4);
            heightValues.add(3);
            heightValues.add(3);
            createPixelBoxesArrayList();
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
        
        if(heightValues.get(selection) == 0) {
            int min = Integer.MAX_VALUE;
            int minPosition = 0;
            for(int i = 0; i < heightValues.size(); i++) {
                if(heightValues.get(i) <= min) {
                    min = heightValues.get(i);   
                }
            }
            return pixelBoxes.get(minPosition);
        }
        return pixelBoxes.get(selection);
    }
    public int getHeightValueInArrayList(int selection) {
        return heightValues.get(selection);
    }
    public ArrayList<Rectangle> getPixelBoxes() {
        return pixelBoxes;
    }
    //Compares tiles in same column
    public static Ground compareSCTile(int xBottomSensor, Ground tile1, Ground tile2, Sonic sonic) {
        //Change it so it initially sets each rectangle to null, this changes if this or other is not null (and gets rect)
        Rectangle rectTile1 = null;
        Rectangle rectTile2 = null;
        if(tile1 != null && tile1.getLayer() == sonic.getLayer()) {
            int heightIndex = (int) Math.abs((xBottomSensor-tile1.getXRef())/4);
            rectTile1 = tile1.getPixelBox(heightIndex);    
        }
        if(tile2 != null && tile2.getLayer() == sonic.getLayer()) {
            int heightIndex = (int) Math.abs((xBottomSensor-tile2.getXRef())/4);
            rectTile2 = tile2.getPixelBox(heightIndex);    
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
        GRD_SONICHOUSE_SONICBED_01,
        GRD_SONICHOUSE_SONICBED_11,
        GRD_SONICHOUSE_SONICBED_21,
        GRD_SONICHOUSE_SONICBED_31,
        GRD_SONICHOUSE_SONICBED_00,
        GRD_SONICHOUSE_SONICBED_10,
        GRD_SONICHOUSE_SONICBED_20,
        GRD_SONICHOUSE_SONICBED_30,
    }
}
