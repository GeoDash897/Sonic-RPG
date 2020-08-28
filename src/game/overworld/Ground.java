/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import game.player.mario.MarioOWA;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.ArrayList;

/**Represents a 64 x 64 tile in {@code Room} that acts as solid ground,
 * has no special properties like gameObjects do.
 *
 * @author GeoSonicDash
 */
public class Ground implements Picture {
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
    
    /**Creates the {@code Ground} tile, including its arrayList of height values and pixelBoxes arrayList.
     * 
     */
    private void create() {
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
    
    /**Creates the rectangles of the {@code Ground} tile based on the heightValues arrayList.
     * 
     */
    private void createPixelBoxesArrayList() {
        for(int i = 0; i < heightValues.size(); i++ ) {                
            pixelBoxes.add(new Rectangle((xRef+(i*4)),(yRef+64-(heightValues.get(i)*4)),4,(heightValues.get(i)*4)));
        }
    }
    private void setSonicBedHeightValues(int part) {
        switch (part) {
            case 01:
            case 11:
            case 21:
            case 31:
                setBlock();
                break;
            case 00:
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
                break;
            case 10:
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
                break;
            case 20:
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
                break;
            case 30:
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
                break;
            default:
                break;
        }
    }
    @Override
    public void draw(Graphics2D g2) {  
        /*g2.setColor(Color.CYAN);
        for(Rectangle temp : pixelBoxes) {
            g2.draw(temp);
        }*/
        g2.setColor(boundary);
        g2.fillRect(xRef, yRef, 64, 1);
        g2.fillRect(xRef, yRef+63, 64, 1);
        g2.fillRect(xRef, yRef, 1, 64);
        g2.fillRect(xRef+63, yRef, 1, 64);
        g2.drawImage(groundPicture, xRef, yRef, length*4, width*4, null);
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
    
    /**Returns specific {@code Rectangle} from {@code pixelBoxes} arrayList.
     * 
     * @param selection specific index of the {@code pixelBoxes} arrayList.
     * @return the {@code Rectangle} of index in {@code pixelBoxes} arrayList.
     */
    public Rectangle getPixelBox(int selection) {
        /*If index selection in heightValues arrayList is 0 that means that there is 
        no Rectangle at that index in pixelBoxes arrayList.
        If that is the case, find the next lowest rectangle in pixelBoxes arrayList.*/
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
    
    /**Compares two {@code Ground} tiles in the same column based on their heights.
     * 
     * @param xBottomSensor the x position of the game element's sensor
     * that is checking for the correct Ground tile (either the left one or the right one) that is interacting with it.
     * @param tile1 the first tile that is being checked.
     * @param tile2 the second tile that is being checked.
     * @param layer the layer of the game element that is checking for Ground tiles (can be Sonic or a gameObject like Monitors).
     * @return the correct {@code Ground} tile that is interacting with game element's sensor (one that closest to game element).
     */
    public static Ground compareSCTile(int xBottomSensor, Ground tile1, Ground tile2, int layer) {
        Rectangle rectTile1 = null;
        Rectangle rectTile2 = null;
        /*If tile1 is not null and is in the same layer as game element, get the 
        correct Rectangle from Rectangle arrayList of tile*/
        /*Correct index of arrayList is based on the difference between xBottomSensor and 
        the xRef of tile (the upper left corner of the tile) divided by 4*/
        if(tile1 != null && tile1.getLayer() == layer) {
            int heightIndex = (int) Math.abs((xBottomSensor-tile1.getXRef())/4);
            rectTile1 = tile1.getPixelBox(heightIndex);    
        }
        if(tile2 != null && tile2.getLayer() == layer) {
            int heightIndex = (int) Math.abs((xBottomSensor-tile2.getXRef())/4);
            rectTile2 = tile2.getPixelBox(heightIndex);    
        }
        /*System.out.println("rectTile1: "+rectTile1);
        System.out.println("rectTile2: "+rectTile2);*/
        
        //Compare the heights of the tiles, return the tile that is lower on the screen.
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
        //Return nothing if there is no tiles to compare.
        return null;       
    }
    
    /**Compares two {@code Ground} tiles in different columns based on their heights.
     * 
     * @param xBottomLeft the x position of the game element's left sensor.
     * @param xBottomRight the x position of the game element's right sensor.
     * @param tile1 first tile that is being checked.
     * @param tile2 the second tile that is being checked.
     * @return the correct {@code Ground} tile that is interacting with game element's sensor (one that closest to game element).
     */
    public static Ground compareDCTile(int xBottomLeft, int xBottomRight, Ground tile1, Ground tile2) {
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
         
        //Compare the heights of the tiles, return the tile that is lower on the screen.
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
    
    public void hammerCollide(MarioOWA owaM) {
        for(Rectangle pixelBox : pixelBoxes) {
            if(owaM.getHammerHitbox().intersects(pixelBox)) {
                owaM.setHammerState(MarioOWA.HammerState.STATE_PAUSEHAMMER);
            }
        }
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
