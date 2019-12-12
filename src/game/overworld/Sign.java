/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import static game.Game.dialog;
import game.sonic.OverWorldAction;
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
public class Sign extends OverWorld implements DefaultObject {
    private int group;
    private int id;
    private int xRef;
    private int yRef;
    private int length;
    private int width;
    private int middle;
    private int layer;
    private int limit;
    private int currentSection;
    private Rectangle hitBox;
    private Image signPicture;
    private Image xKey;
    private String description;
    private boolean stopAdd;
    public Sign(int id, int layer, int xRef, int yRef) {
        group = 0;
        this.id = id;
        this.layer = layer;
        this.xRef = xRef;
        this.yRef = yRef;
        this.currentSection = -3;
        this.stopAdd = false;        
    }
    @Override
    public void create() {
        if(id == 0) {
            //insert picture here later
            length = 16;
            width = 16;
            middle = xRef+((length*4)/2)-40;
            description = "Hello Player! Welcome to the area known as the Test Zone. This is the area where the programmer tests crap-"
                + "pretty self explainatory huh? I'm kind of surprised that this thing works but I need to figure out a way to display"
                + " text in an easier format as well as figure out cutscenes I guess? I don't know man, I'm just a sign afterall, so I"
                + "My next plans are to optimize this code.ewjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjioeeerwrerer"
                + "qweeeeeeeeeeeeeeeeeeeeeqqqqqeqecccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc"
                + "Big Beans! higoewhsdiomv hr80fw9yenh gowe iphg98we nshoigw omg niow oi";
            if(description.length() % 80 == 0) {
                limit = (description.length()/80);    
            }
            else {
                limit = (description.length()/80) + 1;    
            }          
        }      
    }

    @Override
    public void draw(Graphics2D g2) {
        if(id == 0) {
            g2.setColor(Color.RED);
            g2.fillRect(xRef, yRef, length*4, width*4);
        }
    }

    @Override
    public void action() {
        hitBox = new Rectangle(xRef, yRef, length*4, width*4);
    }

    @Override
    public void interactWithSonic(Rectangle sensor) {
    }
    @Override
    public int getGroup() {
        return group;
    }
    @Override
    public int getID() {
        return id;
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
    public int getCurrentSection() {
        return currentSection;
    }
    public void increaseCurrentSection() {
        if(currentSection+3 < limit) {
            currentSection+=3;
        }
        else {
            OverWorldAction temp = new OverWorldAction();
            temp.setObserve(false);
            currentSection = -3;
        }
    }
    public void drawXKey(Graphics2D g2, Rectangle sensor) {
        if(hitBox.intersects(sensor)) {
            g2.setColor(Color.WHITE);
            g2.fillRect(middle, yRef-100, 64, 64);    
        }
    }
    public void drawDescription(Graphics2D g2) {       
        Dialog.drawDialog(g2, currentSection, description);             
        /*for(int i = currentSection; i < splitDescription.size();i++) {
            String firstLetter = String.valueOf(splitDescription.get(i).charAt(0));
            if(!firstLetter.equals(" ")) {
                g2.drawString(splitDescription.get(i), 200, 50+(i*30));    
            }
            else if(firstLetter.equals(" ")) {
                g2.drawString(splitDescription.get(i), 185, 50+(i*30));  
            } 
        }*/
    }
}
