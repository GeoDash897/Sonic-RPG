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
    private int group = 0;
    private int id;
    private int xRef;
    private int yRef;
    private int length;
    private int width;
    private int currentSection;
    private Rectangle hitBox;
    private Image signPicture;
    private Image xKey;
    private String description;
    private boolean observable;
    private boolean stopAdd;
    private ArrayList<String> splitDescription;
    public Sign(int id, int xRef, int yRef) {
        this.id = id;
        this.xRef = xRef;
        this.yRef = yRef;
        this.currentSection = -3;
        this.observable = true;
        this.stopAdd = false;        
        this.splitDescription = new ArrayList<String>();
    }
    @Override
    public void create() {
        if(id == 0) {
            //insert picture here later
            length = 16;
            width = 16;
            description = "Hello Player! Welcome to the area known as the Test Zone. This is the area where the programmer tests crap-"
                + "pretty self explainatory huh? I'm kind of surprised that this thing works but I need to figure out a way to display"
                + " text in an easier format as well as figure out cutscenes I guess? I don't know man, I'm just a sign afterall, so I"
                + "My next plans are to optimize this code.ewjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjioeeerwrerer"
                + "qweeeeeeeeeeeeeeeeeeeeeqqqqqeqecccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc";
            setUpSplitDescriptionArray();
        }
        xKey = Toolkit.getDefaultToolkit().getImage("src\\game\\resources\\insert something here pls.png");
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getXRef() {
        return xRef;
    }

    @Override
    public int getYRef() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public int getWidth() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Rectangle getHitBox() {
        return hitBox;
    }
    public int getCurrentSection() {
        return currentSection;
    }
    public void increaseCurrentSection() {
        if(currentSection+3 < splitDescription.size()) {
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
            g2.fillRect(xRef, yRef-100, 64, 64);    
        }
    }
    public void setUpSplitDescriptionArray() {
        int lengthOfDescription = description.length();
        int fullLine = lengthOfDescription/80;
        int leftOver = lengthOfDescription - (fullLine*80);
        if(!stopAdd) {
            for(int i = 0; i < fullLine;i++) {
                String temp = description.substring(80*i,80*(i+1));
                splitDescription.add(temp);
            }
            if(leftOver > 0) {
                splitDescription.add(description.substring(fullLine*80,lengthOfDescription));                 
            }  
        }
        stopAdd = true;
    }
    public boolean checkFirstLetter(int temp) {
        boolean checkForSpace = false;
        String firstLetter = String.valueOf(splitDescription.get(temp).charAt(0));
        if(!firstLetter.equals(" ")) {
                checkForSpace = false;  
            }
            else if(firstLetter.equals(" ")) {
                checkForSpace = true;
        }
        return checkForSpace;
    }
    public void drawDescription(Graphics2D g2) {       
        if(currentSection >= 0) {
            g2.setFont(dialog);
            g2.setColor(Color.BLUE);
            g2.fillRect(0,0,2000,150);
            g2.setColor(Color.WHITE);
            int temp1 = currentSection+1;
            int temp2 = currentSection+2;
            boolean line1, line2, line3;
            if(currentSection < splitDescription.size()) {
                line1 = checkFirstLetter(currentSection);
                if(!line1) {
                    g2.drawString(splitDescription.get(currentSection), 200, 50);    
                }
                else if(line1) {
                    g2.drawString(splitDescription.get(currentSection), 185, 50);  
                } 
                if(temp1 < splitDescription.size()) {
                    line2 = checkFirstLetter(temp1);
                    if(!line2) {
                        g2.drawString(splitDescription.get(temp1), 200, 80);    
                    }
                    else if(line2) {
                        g2.drawString(splitDescription.get(temp1), 185, 80);  
                    } 
                    if(temp2 < splitDescription.size()) {
                        line3 = checkFirstLetter(temp2);
                        if(!line3) {
                            g2.drawString(splitDescription.get(temp2), 200, 110);    
                        }
                        else if(line3) {
                            g2.drawString(splitDescription.get(temp2), 185, 110);  
                        }   
                    }
                }
            }    
        }
             
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
