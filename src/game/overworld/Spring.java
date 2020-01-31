/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

/**
 *
 * @author GeoSonicDash
 */
public class Spring extends OverWorld implements DefaultObject {
    private String group;
    private SpringType springType;
    private int xRef;
    private int yRef;
    private int length;
    private int width;
    private int layer;
    private int springAnimationTimer;
    private boolean ground;
    private Rectangle hitBox;
    private Image springPicture;
    private OverWorld overworld;
    public Spring(OverWorld overworld, SpringType springType, int layer, int xRef, int yRef) {
        this.overworld = overworld;
        this.springType = springType;
        this.layer = layer;
        this.xRef = xRef;
        this.yRef = yRef;
        this.ground = false;
        this.springAnimationTimer = 0;
        create();
    }
    @Override
    public void create() {
        if(springType == SpringType.SPRING_YELLOWUP) {
            springPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Yellow Spring Up_1.png");
        }
        else if(springType == SpringType.SPRING_REDUP) {
            springPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Red Spring Up_1.png");
        }
        length = 72;
        width = 72;
    }

    @Override
    public void draw(Graphics2D g2) {
        g2.drawImage(springPicture, xRef, yRef, length*4, width*4,this);
        //g2.fillRect(xRef+85, yRef+141, 128, 64);
        g2.drawString(""+springAnimationTimer,1000,300);
    }

    @Override
    public void action() {
        hitBox = new Rectangle(xRef+85, yRef+141, 128, 64);
        for(Ground var : overworld.getCurrentRoom().getGroundArrayList()) {
            for(Rectangle temp : var.getPixelBoxes()) {
                if(hitBox.intersects(temp)) {       
                    if(yRef+205 > var.getYRef()) {
                        yRef = var.getYRef()-205;
                    }
                    ground = true;
                }    
            }          
        }
        if(!ground) {
            yRef+=16;
        }
        if(springAnimationTimer >= 1 && springAnimationTimer < 25) {
            springAnimationTimer++;
            if(springType == SpringType.SPRING_YELLOWUP) {
                springPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Yellow Spring Up_2.png");
            }
            else if(springType == SpringType.SPRING_REDUP) {
                springPicture = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\Red Spring Up_2.png");
            }
        }
        else if(springAnimationTimer == 25) {
            springAnimationTimer = 0;
        }
    }

    @Override
    public void interactWithSonic(Rectangle sensor) {
        springAnimationTimer = 1;
    }
    public int getSpringJumpValue() {
        int temp = 0;
        if(springType == SpringType.SPRING_YELLOWUP) {
            temp = -12;
        }
        else if(springType == SpringType.SPRING_REDUP) {
            temp = -16;
        }
        return temp;
    }
    @Override
    public String getGroup() {
        group = String.valueOf(springType).substring(0,String.valueOf(springType).indexOf("_"));
        return group;
    }

    @Override
    public int getXRef() {
        return xRef;
    }

    @Override
    public int getYRef() {
        return yRef;
    }

    @Override
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

    @Override
    public Rectangle getHitBox() {
        return hitBox;
    }
    public String toString() {
        return "Spring: "+springType+" "+layer+" "+xRef+" "+yRef;
    }
    public enum SpringType {
        SPRING_YELLOWUP,
        SPRING_REDUP
    };
}
