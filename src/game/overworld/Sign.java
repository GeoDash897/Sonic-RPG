/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.overworld;

import game.sonic.OverWorldAction;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

/**
 *
 * @author GeoSonicDash
 */
public class Sign extends OverWorld implements DefaultObject {
    private int group;
    private SignType signType;
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
    public Sign(SignType signType, int layer, int xRef, int yRef) {
        group = 0;
        this.signType = signType;
        this.layer = layer;
        this.xRef = xRef;
        this.yRef = yRef;
        this.currentSection = -3;       
    }
    @Override
    public void create() {
        if(signType == SignType.SIGN_STDUKEDESERT) {
            //insert picture here later
            length = 16;
            width = 16;
            middle = xRef+((length*4)/2)-40;
            description = "This is a picture of Sonic and Tails that is worn and torn with age. They are grinning at the camera and look "
                    + "like they are having a fun time. In the background there is a huge desert, "
                    + "with rolling dunes far into the distance. Tall skyscapers and buildings with bright lights and signs hover "
                    + "behind them. People are shuffling in the foreground and the background of the picture as well- some laughing, "
                    + "others talking, and few doing some.....questionable stuff. In the corner of the picture there is a blurred object "
                    + "that looks like an alien arm, but it's too hard to make out. In the other corner, there are the numbers '01 in "
                    + "old, smudged black ink.";
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
        if(signType == SignType.SIGN_STDUKEDESERT) {
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
    public SignType getSignType() {
        return signType;
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
    @Override
    public String toString() {
      return "Sign: "+signType+" "+layer+" "+xRef+" "+yRef;
    }
    public enum SignType {
      SIGN_STDUKEDESERT  
    };
}
