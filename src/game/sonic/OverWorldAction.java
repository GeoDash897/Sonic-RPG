/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.sonic;

import game.overworld.Tile;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

/**
 *
 * @author GeoSonicDash
 */
public class OverWorldAction extends Sonic {
    private static int xDrawCenterSonic = 500; //center for drawing the picture
    private static int yDrawCenterSonic; //center for drawing the picture
    private static int ySpriteCenterSonic = 300; // Center of the actual sprite (basis for positions of collision boxes
    //The X Position for the center of the actual sprite is the same as xDrawCenterSonic   
    private static double groundSpeed = 0;//The actual speed that Sonic is running at (not influenced by accel, slope or anything)
    private static double angle = 0;//The angle of Sonic (influenced by the tile that Sonic is curre
    private static double xSpeed = 0;//How fast Sonic is moving left and right
    private static double ySpeed = 0; //Same but for up and down
    private static int slope = 0;//The current slope factor being used (normal, rolling up a slope, or rolling down it)
    private static boolean ground = true;//Sonic is on ground = true, not = false
    private static int ledge = -1;//Determines if Sonic is near a ledge or not, -1 = not near a ledge, 0 = he is near a ledge on the left
    //1 = he is near a ledge on the right
    private static int jump = 0;//Phases of Sonic's jump: 0 = not jumping,1 = playing is holding jump button (Sonic is moving up)
    //2 = player has released jump button, Sonic has stopped moving up + is moving downward
    private static int duck = 0;//Phases of Sonic's duck: 0 = not ducking, 1 = ducking, 2 = rolling
    private static int spindash = 0;//Controls when the spindash animation is played + when the player can increase spindashCharge (can only
    //increase the double when spindash == 1)
    private static double spindashCharge = 0;//Controls how fast Sonic moves in the xSpeed after being released from the spindash
    private static int waitTimer = 0;//Increases when no keys are being pressed, when it equals a certain number, Sonic's waiting animation plays
    private static int leftPress = 0;//Set to 1 when the left key is pressed
    private static int rightPress = 0;//Likewise for right
    private static int downPress = 0;//Likewise for down
    private static int zPress = 0;//Likewise for Z key
    private static int zPressTimer = 0;//Increases when the Z key is pressed, used to calculate presses of a key
    
    //Physic variables
    private static double AIR = 0.09375;
    private static double GRAVITY = 0.21875;
    private static double ACCELERATION = 0.046875;
    private static double DECELERATION = 0.2;
    private static double FRICTION = 0.046875;
    private static double TOP = 6;
    private static double JUMP = 6.5;
    private static double SLOPE = 0.125;
    private static double SLOPEROLLUP = 0.078125;
    private static double SLOPEROLLDOWN = 0.3125;
    private static double FALL = 2.5;
    
    private static int collideWithSlope = 0;//Checks if Sonic is colliding with a slope, used to change the bottom + middle Sensors to the right
    //positions and sizes
    private static int bLCollide = 0;//If this sensor is colliding with a tile, set it to 1
    private static int bRCollide = 0;//Likewise
    private static int mLCollide = 0;//Likewise
    private static int mRCollide = 0;//Likewise
    
    //The Sensors
    private static Rectangle topLeft;
    private static Rectangle bottomLeft;
    private static Rectangle topRight;
    private static Rectangle bottomRight;
    private static Rectangle middleLeft;
    private static Rectangle middleRight;
    
    //Creates Animation object so Sonic's animations can be changed
    private Animation animation = new Animation();
    private Inventory inventory;
    @Override
    public void standard(Graphics2D g2) {  
        /*try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(OverWorldAction.class.getName()).log(Level.SEVERE, null, ex);
        }*/  
        yDrawCenterSonic = ySpriteCenterSonic + 20;     
        /*Controls how Sonic senses the ground:
        1. bLCollide & bRCollide are true - ground is true (Sonic is on the ground and is not near ledge, so ledge = -1)
        2. bLCollide is true or bRCollide is true - ground is true (Sonic is near or on ledge, so ledge is 0 or 1)
        3.bLCollide and bRCollide are both false - ground is false (Sonic is in the air)
        */
        if(bLCollide == 1 && bRCollide == 1) {
            ground = true;
            ledge = -1;
        }
        else if((bLCollide == 1 && bRCollide == 0) || (bLCollide == 0 && bRCollide == 1)) {
            ground = true;
        }
        else if(bLCollide == 0 && bRCollide == 0) {
            ground = false;
            ledge = -1;
        }
        /*Changes Sonic's sensors depending on what he is doing:
            1.Sonic is not colliding with a slope or Sonic is not on the ground - (default size + positions)
            2.Sonic is colliding with a slope - bottomLeft and bottomRight are extended + middle sensors are moved to ySpriteCenterSonic
        */      
        if(collideWithSlope == 0 || !ground) {
            bottomLeft = new Rectangle(xDrawCenterSonic-28,ySpriteCenterSonic,4,84);    
            bottomRight = new Rectangle(xDrawCenterSonic+28,ySpriteCenterSonic,4,84);
            middleLeft = new Rectangle(xDrawCenterSonic-36,ySpriteCenterSonic+32,36,4);    
            middleRight = new Rectangle(xDrawCenterSonic,ySpriteCenterSonic+32,44,4); 
        }
        else if(collideWithSlope == 1) {
            bottomLeft = new Rectangle(xDrawCenterSonic-28,ySpriteCenterSonic,4,148);    
            bottomRight = new Rectangle(xDrawCenterSonic+28,ySpriteCenterSonic,4,148);
            middleLeft = new Rectangle(xDrawCenterSonic-36,ySpriteCenterSonic,36,4);    
            middleRight = new Rectangle(xDrawCenterSonic,ySpriteCenterSonic,44,4); 
        }
        topLeft = new Rectangle(xDrawCenterSonic-28,ySpriteCenterSonic-80,4,80);//Creates the topLeft and topRight sensors (don't change like the       
        topRight = new Rectangle(xDrawCenterSonic+28,ySpriteCenterSonic-80,4,80);//others do)       
        collisionCheck(g2);//Checks for collisions, gets and uses information from tiles
        //Controls gravity + xSpeed and ySpeed when Sonic is not on ground
        if(!ground) {           
            if (ySpeed < 0 && ySpeed > -4)//air drag calculation
            {
                if (Math.abs(xSpeed) >= 0.125) {
                    xSpeed = xSpeed * 0.96875;
                }
            }   
            ySpeed += GRAVITY;//Causes Sonic to move down
            if(ySpeed > 16) {//limits how fast Sonic is falling (so he won't clip through tiles)
                ySpeed = 16;
            }
        }
        else if(ground) {//gets xSpeed based on groundSpeed and angle (calculated in collisionCheck method), sets jump to 0 (so Sonic can jump again)
            xSpeed = groundSpeed*Math.cos(angle);
            ySpeed = groundSpeed*-Math.sin(angle);
            jump = 0;
        } 
        //After checking all of the collisions and performing gravity, now calculate friction (decreasing groundSpeed if no keys are pressed)
        if((leftPress == 0 && rightPress == 0 && angle == 0 && zPress == 0 && ground) || (Math.abs(groundSpeed) > 0 && duck > 0)) {
            groundSpeed -= Math.min(Math.abs(groundSpeed), FRICTION) * Math.signum(groundSpeed);
        } 
        //Controls key presses (left, right, down, and Z)
        if(rightPress == 1) {
            rightPress();
        }
        if(leftPress == 1) {
            leftPress();
        }
        if(downPress == 1 && spindash == 0) {
            downPress();
        }
        if(zPress == 1) {
            zPress();
        }   
        //If Sonic is rolling and the abs of his groundSpeed (since he has to be on the ground) is < 1 (basically stopped), set it to 0 
        //(Sonic is now standing)
        if(Math.abs(groundSpeed) < 1) {
            if(duck == 2) {
                duck = 0;    
            }
        }
        //Controls spindashing 
        if(downPress == 1 && zPress == 1 && groundSpeed == 0 && ledge == -1 && ground && jump == 0 && spindash == 0) {//Sets spindash state
            spindash = 1;
        }
        if(spindashCharge > 0.01) {//Constantly subtract from spindashCharge (so when Sonic is released from spindash, he can return to zero
            //stopping his movement from the spindash
            spindashCharge -= ((spindashCharge / 0.125) / 256); //"div" is division ignoring any remainder    
        }
        else {
            spindashCharge = 0;
        }           
        if(spindash == 0 && spindashCharge > 0) {//Changes Sonic to rolling after he is released from the spindash and calculates his groundSpeed
            //based on how much the spindash was charged for + his direction
            duck = 2;
            if(animation.getDirection() == 0) {
                groundSpeed = -8 + (Math.floor(spindashCharge) / 2); //this would be negative if the character were facing left, of course    
            }
            else if(animation.getDirection() == 1) {
                groundSpeed = 8 + (Math.floor(spindashCharge) / 2); //this would be negative if the character were facing left, of course    
            }           
        }           
        //Animations:
        changeAnimation();
        
        //Displaying variables here
        g2.drawString("downPress: "+downPress,0,175);
        g2.drawString("rightPress: "+rightPress,100,175);
        g2.drawString("leftPress: "+leftPress,100,200);
        g2.drawString("bLCollide: "+bLCollide,100,225);
        g2.drawString("bRCollide: "+bRCollide,100,250);
        g2.drawString("mLCollide: "+mLCollide,100,275);
        g2.drawString("mRCollide: "+mRCollide,100,300);
        g2.drawString("direction: "+animation.getDirection(),100,325); //0 for left, 1 for right
        g2.drawString("waitTimer: "+waitTimer,100,350);
        g2.drawString("groundSpeed: "+groundSpeed,100,375); 
        g2.drawString("xSpeed: "+xSpeed,100,425);
        g2.drawString("ySpeed: "+ySpeed,100,450);
        g2.drawString("angle: "+angle,100,475); 
        g2.drawString("collideWithSlope: "+collideWithSlope,200,175);
        g2.drawString("yDrawCenterSonic: "+ySpriteCenterSonic,200,200);
        g2.drawString("ground: "+ground,200,225);
        g2.drawString("jump: "+jump,200,250);
        g2.drawString("ledge: "+ledge,200,300);
        g2.drawString("ground: "+ground,200,325);
        g2.drawString("duck: "+duck,200,350);
        g2.drawString("zPressTimer: "+zPressTimer,200,400);
        g2.drawString("spindash: "+spindash,325,175);
        g2.drawString("spindashCharge: "+spindashCharge,325,200);
        drawCollisionBoxes(g2);        
        xDrawCenterSonic+= (int) xSpeed;
        ySpriteCenterSonic+= (int) ySpeed;
    }
    public void drawCollisionBoxes(Graphics2D g2) {
        //Draws rectangles themselves
        g2.setColor(Color.blue);
        g2.fillRect(xDrawCenterSonic-28,ySpriteCenterSonic-80,4,80);//topLeft       
        g2.setColor(Color.green);
        g2.fillRect(xDrawCenterSonic+28,ySpriteCenterSonic-80,4,80);//topRight
        if(collideWithSlope == 0) {
            g2.setColor(Color.red);
            g2.fillRect(xDrawCenterSonic-28,ySpriteCenterSonic,4,84);//bottomLeft    
            g2.setColor(Color.yellow);
            g2.fillRect(xDrawCenterSonic+28,ySpriteCenterSonic,4,84);//bottomRight
            g2.setColor(Color.orange);
            g2.fillRect(xDrawCenterSonic-36,ySpriteCenterSonic+32,36,4);//middleLeft
            g2.setColor(Color.cyan);
            g2.fillRect(xDrawCenterSonic,ySpriteCenterSonic+32,40,4);//middleRight
        }
        else if(collideWithSlope == 1) {
            g2.setColor(Color.red);
            g2.fillRect(xDrawCenterSonic-28,ySpriteCenterSonic,4,148);//bottomLeft    
            g2.setColor(Color.yellow);
            g2.fillRect(xDrawCenterSonic+28,ySpriteCenterSonic,4,148);//bottomRight
            g2.setColor(Color.orange);
            g2.fillRect(xDrawCenterSonic-36,ySpriteCenterSonic,40,4);//middleLeft
            g2.setColor(Color.cyan);
            g2.fillRect(xDrawCenterSonic,ySpriteCenterSonic,40,4);//middleRight
        }  
        //Draw collision rectangles
        g2.setColor(Color.blue);
        g2.drawString(""+topLeft,1000,100);
        g2.setColor(Color.red);
        g2.drawString(""+bottomLeft,1000,125);
        g2.setColor(Color.green);
        g2.drawString(""+topRight,1000,150);
        g2.setColor(Color.yellow);
        g2.drawString(""+bottomRight,1000,175);
        g2.setColor(Color.orange);
        g2.drawString(""+middleLeft,1000,200);
        g2.setColor(Color.cyan);
        g2.drawString(""+middleRight,1000,225);
        g2.setColor(Color.black); 
        g2.fillRect(xDrawCenterSonic, yDrawCenterSonic, 4, 4);
        g2.setColor(Color.red);
        g2.fillRect(xDrawCenterSonic,ySpriteCenterSonic,4,4);
    }
    public void collisionCheck(Graphics2D g2) {
        int xBottomLeft = (int) bottomLeft.getX();//gets the x position of bottomLeft
        int yBottomLeft = (int) bottomLeft.getY()+ (int)bottomLeft.getHeight();//gets y (the bottom of the rectangle)
        int xBottomRight = (int) bottomRight.getX();//gets x of bottomRight
        int yBottomRight = (int) bottomRight.getY()+ (int)bottomRight.getHeight();//gets y (the bottom of the rectangle)
        int pixelyL = ySpriteCenterSonic+200;//the y of the pixel that bottomLeft senses (the y position relative to the entire screen)
        int pixelyR = ySpriteCenterSonic+200;//the y of the pixel that bottomRight senses (the y position relative to the entire screen)
        int heightBottomLeftIndex;//used to get the specific height of the pixel that the left sensor is interesting from the tile (the one that the
        //sensor is currently in) height arrayList
        int heightBottomRightIndex;//used to get the specific height of the pixel that the right sensor is interesting from the tile (the one that the
        //sensor is currently in) height arrayList
        Rectangle flatBoxRect = new Rectangle(0,0,0,0);
        double angleL = 0;
        double angleR = 0;
        int tileDirection = 0;
        for(Tile checkBoundary: environmentTiles) {
            g2.setColor(Color.red);
            g2.fillRect((int)checkBoundary.getXRef(),(int)checkBoundary.getYRef(),checkBoundary.getLength()*4,4);        
                if(checkBoundary.getAngle() != 0) {               
                    if(xBottomRight >= checkBoundary.getXRef() && xBottomRight < checkBoundary.getXRef()+checkBoundary.getLength()*4 && 
                    yBottomRight >= checkBoundary.getYRef() && ySpriteCenterSonic < checkBoundary.getYRef()){//Checks if  
                        //bottomRight sensor is with 64x64 tile (before calculations) and the tile is a slope
                        if(ySpeed >= 0) {//Checks for collision if Sonic is falling/on the ground and not when he is jumping
                            bRCollide = 1;     
                            ySpeed = 0;
                            collideWithSlope = 1;
                            heightBottomRightIndex = (int) Math.abs(((xBottomRight - checkBoundary.getXRef())/4));//gets specific height of pixel (depends
                            //on sensor's x position relative to the tile's xRef (abs to avoid negatives)                   
                            g2.drawString("Height value of pixel (in 16x16 tile)"+checkBoundary.heightValues.get(heightBottomRightIndex), 1000, 575);
                            g2.drawString(checkBoundary.toString(),1000,525);
                            pixelyR = (checkBoundary.getYRef()+64-(checkBoundary.heightValues.get(heightBottomRightIndex)*4));//gets pixel's height relative
                            //to screen
                            angleR = checkBoundary.getAngle();//gets angle of tile that sensors are colliding with
                            tileDirection = checkBoundary.getDirection();//gets the tiles direction that sensors are colliding with
                        break;   
                        }
                    }
                    else {//if Sonic is not within tile, bottomRight is not colliding with anything
                        bRCollide = 0;
                    }        
                }                                                         
                else if(checkBoundary.getAngle() == 0) {
                    if(xBottomRight > checkBoundary.getXRef() && xBottomRight < checkBoundary.getXRef()+checkBoundary.getLength()*4 && 
                        yBottomRight > checkBoundary.getYRef() && yBottomRight < checkBoundary.getYRef()+100) {//checks if bottomRight sensor is 
                        //within tile (the +100 is to extend the tiles range downward since the sensors are lower than the tile's range when Sonic
                        //is going up a slope
                        if(ySpeed >= 0) {//Checks for collision if Sonic is falling/on the ground and not when he is jumping
                            bRCollide = 1;     
                            ySpeed = 0;
                            collideWithSlope = 0;//changes the sensors to appropiate sizes and positions
                            g2.drawString("Height value of pixel (in 16x16 tile)"+checkBoundary.heightValues.get(0), 1000, 575);
                            g2.drawString(checkBoundary.toString(),1000,525);
                            flatBoxRect = checkBoundary.getPixelBox(0);//gets the rectangles hitbox (only one box is in the pixelbox arrayList)
                            //used for ledges
                            pixelyR = (checkBoundary.getYRef());//gets pixel's height relative to the screen                     
                            break;    
                        }                      
                    }
                    else {//if Sonic is not within tile, bottomRight is not colliding with anything
                        bRCollide = 0;
                    }
                }             
        }
        for(Tile checkBoundary : environmentTiles) {         
                if(checkBoundary.getAngle() != 0) {              
                    if(xBottomLeft > checkBoundary.getXRef() && xBottomLeft < checkBoundary.getXRef()+checkBoundary.getLength()*4 && 
                           yBottomLeft > checkBoundary.getYRef() && ySpriteCenterSonic < checkBoundary.getYRef()) {//Checks if  
                        //bottomRight sensor is with 64x64 tile (before calculations) and the tile is a slope
                        if(ySpeed >= 0) {//Checks for collision if Sonic is falling/on the ground and not when he is jumping
                            bLCollide = 1;     
                            ySpeed = 0;
                            collideWithSlope = 1;
                            g2.drawString(checkBoundary.toString(),1000,500);                                     
                            heightBottomLeftIndex = (int) Math.abs(((xBottomLeft - checkBoundary.getXRef())/4));//gets specific height of pixel (depends
                            //on sensor's x position relative to the tile's xRef (abs to avoid negatives)
                            g2.drawString("Height value of pixel (in 16x16 tile)"+checkBoundary.heightValues.get(0), 1000, 550);
                            pixelyL = (checkBoundary.getYRef()+64-(checkBoundary.heightValues.get(heightBottomLeftIndex)*4));//gets pixel's height relative                   
                            //to screen    
                            angleL = checkBoundary.getAngle();//gets angle of tile that sensors are colliding with
                            tileDirection = checkBoundary.getDirection();//gets the tiles direction that sensors are colliding with 
                            break;
                        }
                    } 
                    else {//if Sonic is not within tile, bottomLeft is not colliding with anything
                        bLCollide = 0;
                    }
                }
                else if(checkBoundary.getAngle() == 0) {
                    if(xBottomLeft > checkBoundary.getXRef() && xBottomLeft < checkBoundary.getXRef()+checkBoundary.getLength()*4 && 
                        yBottomLeft > checkBoundary.getYRef() && yBottomLeft < checkBoundary.getYRef()+100) {//checks if bottomRight sensor is 
                        //within tile (the +100 is to extend the tiles range downward since the sensors are lower than the tile's range when Sonic
                        //is going up a slope
                        if(ySpeed >= 0) {//Checks for collision if Sonic is falling/on the ground and not when he is jumping
                            bLCollide = 1;     
                            ySpeed = 0;
                            collideWithSlope = 0; 
                            g2.drawString(checkBoundary.toString(),1000,500);
                            g2.drawString("Height value of pixel (in 16x16 tile)"+checkBoundary.heightValues.get(0), 1000, 550);
                            flatBoxRect = checkBoundary.getPixelBox(0);//gets the rectangles hitbox (only one box is in the pixelbox arrayList)
                            //used for ledges
                            pixelyL = (checkBoundary.getYRef());//gets pixel's height relative to the screen                                       
                        break;
                        }
                    }
                    else {//if Sonic is not within tile, bottomLeft is not colliding with anything
                        bLCollide = 0;
                    }   
                }
        }
        //Changes Sonic's yPosition based on the heights of the pixels that each of the bottom sensors get
        //REMEMBER- ySpriteCenterSonic is 20 pixels higher than yDrawCenterSonic, so everything is based on yDrawCenterSonic
        //So the value would be [insert number here]-20 (to count for that offset)
        if(pixelyR > pixelyL && pixelyL != 0 && pixelyR != 0) {//If pixelyR is greater than pixelyL (meaning that it is lower on the screen)
            //set Sonic's yPosition based on pixelyL + use angleL for angle
            ySpriteCenterSonic = pixelyL - 80;//Changes Sonic's yPosition   
            angle = angleL;
        } 
        else if(pixelyL > pixelyR && pixelyL != 0 && pixelyR != 0) {//If pixelyL is greater than pixelyL (meaning that it is lower on the screen)
            //set Sonic's yPosition based on pixelyR + use angleR for angle
            ySpriteCenterSonic = pixelyR - 80;//Changes Sonic's yPosition   
            angle = angleR;
        }  
        if(pixelyL == pixelyR && ground) {//If they are equal, angle is 0 (since Sonic is on flat ground)
            angle = 0;
        }
        //Controls groundSpeed (which is influenced by angle), used when Sonic is on a slope + when is on a slope and no keys are pressed
        //(used for sliding) + rolling down slopes, is influenced by direction
        if(tileDirection == 0 && angle != 0 && ground) {
            if(duck == 0) {
                groundSpeed -= SLOPE*(-Math.sin(angle));       
            }
            else {
                groundSpeed -= SLOPEROLLDOWN*(-Math.sin(angle));       
            }
        }
        else if(tileDirection == 1 && angle != 0 && ground) {
            if(duck == 0) {
                groundSpeed -= SLOPE*(Math.sin(angle));        
            }
            else {
                groundSpeed -= SLOPEROLLDOWN*(Math.sin(angle));       
            }
               
        }    
        //Sets Sonic's groundSpeed when he lands on the ground after a jump
        if(jump == 2 && angle < 45) {
            groundSpeed = xSpeed;
        }
        else if(jump == 2 && angle == 45) {
            if(Math.abs(xSpeed) > ySpeed) {
                groundSpeed = xSpeed;    
            }
            else {
                groundSpeed = ySpeed*0.5*-Math.signum(Math.sin(angle));
            }          
        }     
        g2.drawString("pixelyL:"+pixelyL,500,100);
        g2.drawString("pixelyR:"+pixelyR,600,100);
        //Controls collisions with the middle sensors
        for(Tile checkBoundary: environmentTiles) {
            if(middleRight.intersects(checkBoundary.getPixelBox(0)) && leftPress == 0) {//If middleRight sensor is intersecting a tile's hitbox
                //set groundSpeed = 0, set mRCollide = 1, and xSpeed = 0 (I need to set both groundSpeed and xSpeed to 0 so Sonic can stop
                //moving when is on the ground or in the air (Sonic doesn't use groundSpeed in the air, only xSpeed)
                groundSpeed = 0;
                xSpeed = 0;
                mRCollide = 1;
                break;
            }
            else {
                mRCollide = 0;
            }
        }
        for(Tile checkBoundary: environmentTiles) {
            if(middleLeft.intersects(checkBoundary.getPixelBox(0)) && rightPress == 0) {//If middleRight sensor is intersecting a tile's hitbox
                //set groundSpeed = 0, set mLCollide = 1, and xSpeed = 0 (I need to set both groundSpeed and xSpeed to 0 so Sonic can stop
                //moving when is on the ground or in the air (Sonic doesn't use groundSpeed in the air, only xSpeed)
                groundSpeed = 0;
                xSpeed = 0;
                mLCollide = 1;
                break;
            }
            else {
                mLCollide = 0;
            }
        }
        //Controls ledges (when Sonic is near a ledge or not)      
        if(xDrawCenterSonic > (int) flatBoxRect.getX()+68 && bRCollide == 0 && bLCollide == 1 && leftPress == 0 && rightPress == 0 && groundSpeed == 0 && angle == 0) {
            duck = 0;
            ledge = 1;
        }
        else if(xDrawCenterSonic < (int) flatBoxRect.getX()-4 && bRCollide == 1 && bLCollide == 0 && leftPress == 0 && rightPress == 0 && groundSpeed == 0 && angle ==  0) {
            duck = 0;
            ledge = 0;
        }
    }
    public void changeAnimation() {
        //Controls waiting animation
        if(rightPress == 0 && leftPress == 0 && groundSpeed == 0 && ground == true && ledge == -1 && duck == 0 && spindash == 0) {
            //If no keys are pressed and Sonic is not moving on ground (groundSpeed == 0), increase waitTimer
            waitTimer++;
            //Depending on how long the keys aren't pressed, a certain animation plays
            if(waitTimer < 988) {
                if(animation.getAnimationNumber() != 0 && duck == 0) {
                    animation.setAnimationNumber(0);               
                }                   
            }
            else if(waitTimer >= 998 && waitTimer < 3000) {
                if(animation.getAnimationNumber() != 1) {
                    animation.setAnimationNumber(1);               
                }
            }
            else if(waitTimer >= 3000 && waitTimer < 3001) {
                if(animation.getAnimationNumber() != 11) {
                    animation.setAnimationNumber(11);               
                }
            }
            else if(waitTimer >= 3000) {
                waitTimer = 3000;
            }
        }
        else {
            waitTimer = 0;
        }
        //Controls when Sonic's walking animation plays
        if((groundSpeed > 0 && groundSpeed < 6 && rightPress == 1 && mRCollide == 0 && jump == 0) || (groundSpeed > 0 && groundSpeed < 6 && rightPress == 0 && leftPress == 0 && mRCollide == 0 && jump == 0 && duck == 0)) {  
            if(animation.getAnimationNumber() != 2) {
                animation.setAnimationNumber(2);    
            }                         
        }
        else if((groundSpeed < 0 && groundSpeed > -6 && leftPress == 1 && mLCollide == 0 && jump == 0) || (groundSpeed < 0 && groundSpeed > -6 && rightPress == 0 && leftPress == 0 && mLCollide == 0 && jump == 0 && duck == 0)) {  
            if(animation.getAnimationNumber() != 2) {
                animation.setAnimationNumber(2);    
            }                         
        }
        else if(Math.abs(groundSpeed) >= 6 && duck == 0) {//Controls when Sonic's running animation plays
            if(animation.getAnimationNumber() != 3) {
                animation.setAnimationNumber(3);    
            }
        }
        else if(ledge == 0) {//If Sonic is on a left ledge, play Left Ledge animation
            if(animation.getAnimationNumber() != 4) {
                animation.setAnimationNumber(4);    
            }
        }
        else if(ledge == 1) {
            if(animation.getAnimationNumber() != 5) {//If Sonic is on a right ledge, play Right Ledge animation
                animation.setAnimationNumber(5);    
            }
        }
        if(jump > 0) {//Plays Sonic's jumping animation when Sonic is jumping
            if(animation.getAnimationNumber() != 7) {
                animation.setAnimationNumber(7);    
            }
        }  
        if(duck == 1 && ledge == -1 && angle == 0 && spindash == 0) {//Plays Sonic's ducking animation if duck == 1
            if(animation.getAnimationNumber() != 8) {
                animation.setAnimationNumber(8);    
            }
        }
        else if(duck == 2) {//If duck is equal to 2 (Sonic is rolling), play jumping animation
            if(animation.getAnimationNumber() != 7) {
                animation.setAnimationNumber(7);    
            }
        }
        else if(spindash == 1) {//If Sonic is charging the spindash, play the spindash animation
            if(animation.getAnimationNumber() != 10) {
                animation.setAnimationNumber(10);
            }
        }
    }
    public void rightPress() {//If Sonic's groundSpeed is greater than 0, set his direction to 0 (left), this makes it so the player has to stop 
            //completely (skid) before changing direction (can't change direction immediately)
        if(groundSpeed > 0) {
            animation.setDirection(1);    
        }      
        if(mRCollide == 1 && ground) {//Displays pushing animayion
            if(animation.getAnimationNumber() != 14) {
                animation.setAnimationNumber(14);    
            }
        }
        if(!ground) {//Used for movement in the air (since groundSpeed does not change Sonic's x movement in the air)
            if(xSpeed < 4) {
                xSpeed += AIR;    
            }            
        }
        else if(ground && duck == 0) {//If Sonic's groundSpeed is negative (moving left), decelerate
            if (groundSpeed < 0) {        
                groundSpeed += DECELERATION;
                if(animation.getAnimationNumber() != 9 && animation.getDirection() == 0) {//Plays skid animation (Sonic has to be facing left though
                    //)This prevents the animation from playing if the player presses right when going down a slope (facing right while going down
                    //left)
                    animation.setAnimationNumber(9);    
                }
                if (groundSpeed >= 0) {
                    groundSpeed = 0.5;   
                }                
            }
            else if (groundSpeed < TOP) {//Cap for Sonic's running speed on flat ground(no influence from power ups, slopes, etc)      
                groundSpeed += ACCELERATION;
                if (groundSpeed >= TOP) {
                    groundSpeed = TOP;    
                }               
            }    
        }       
    }
    public void leftPress() {
        if(groundSpeed < 0) {//If Sonic's groundSpeed is less than 0, set his direction to 0 (left), this makes it so the player has to stop 
            //completely (skid) before changing direction (can't change direction immediately)
            animation.setDirection(0);
        }
        if(mLCollide == 1 && ground) {//Displays pushing animation
            if(animation.getAnimationNumber() != 13) {
                animation.setAnimationNumber(13);    
            }          
        }
        if(!ground) {//Used for movement in the air (since groundSpeed does not change Sonic's x movement in the air)
            if(xSpeed > -4) {
                xSpeed -= AIR;    
            }
        }
        else if(ground && duck == 0) {
            if(groundSpeed > 0) {
                groundSpeed -= DECELERATION;   
                if(animation.getAnimationNumber() != 9 && animation.getDirection() == 1) {//Plays skid animation (Sonic has to be facing right though
                    //)This prevents the animation from playing if the player presses left when going down a slope (facing left while going down
                    //right)         
                    animation.setAnimationNumber(9);    
                }
                if(groundSpeed <= 0) {
                   groundSpeed = -0.5; 
                }           
            }
            else if(groundSpeed > -TOP) {//Cap for Sonic's running speed on flat ground(no influence from power ups, slopes, etc)      
                groundSpeed -= ACCELERATION;
                if(groundSpeed <= -TOP) {
                   groundSpeed = -TOP; 
                }            
            }            
        }       
    }
    public void downPress() {//If abs of Sonic's groundSpeed is almost 0, sonic is ducking (duck = 1), else Sonic is rolling (duck = 2)
        if(Math.abs(groundSpeed) < 1 && ground && ledge == -1) {
            duck = 1;
        }
        else if(Math.abs(groundSpeed) > 1 && ground && ledge == -1) {
            duck = 2;
        }
    }
    public void zPress() {//zPressTimer is used for variables that increase everytime the key is pressed (so it won't increase while the 
        //button is held) - used for mashing the button
        zPressTimer++;
        if(spindash == 0) {
            if(jump == 0 && ground && duck != 1) {//If Sonic is on the ground, is not currently jumping or falling from jump (jumped already)
                //and is not ducking (since that will cause sonic to spindash), jump
                jump = 1;
            }
            if(jump == 1) {//Controls jump
                duck = 0;
                spindashCharge = 0;
                ySpeed = -JUMP;  
                ground = false;   
            }                
        }     
        else if(spindash == 1) {//If spindash == 1 (is charging), increase spindashCharge with everyPress
            if(zPressTimer == 1) {
                spindashCharge++;    
            }           
        }
    }
    public int getXCenterSonic() {
        return xDrawCenterSonic;
    }
    public int getYCenterSonic() {
        return yDrawCenterSonic;
    }
    @Override
    public void keyReleased(KeyEvent e) {
        //Gets key inputs when keys are pressed
        if(e.getKeyCode() == e.VK_RIGHT) {
            rightPress = 0;
        }
        if(e.getKeyCode() == e.VK_LEFT) {
            leftPress = 0;
        }
        if(e.getKeyCode() == e.VK_DOWN) {
            if(duck == 1) {
                duck = 0;
            }
            downPress = 0;
            if(spindash == 1) {
                spindash = 0;
            }
        }
        if(e.getKeyCode() == e.VK_Z) {  
            zPressTimer = 0;
            if(ySpeed < -4) {
                ySpeed = -4;
            }
            if(jump == 1) {
                jump = 2;
            }
        }
        zPress = 0;
    }
    @Override
    public void keyPressed(KeyEvent e) {
        //Gets key inputs from the different keys
        if (e.getKeyCode() == e.VK_RIGHT ) {
            rightPress = 1;
            leftPress = 0;
        }
        if (e.getKeyCode() == e.VK_LEFT ) {  
            leftPress = 1;
            rightPress = 0;
        }          
        if (e.getKeyCode() == e.VK_UP ) {
            
        }
        if (e.getKeyCode() == e.VK_DOWN) {
            downPress = 1;
        }
        if (e.getKeyCode() == e.VK_Z ) {
            zPress = 1;
        }
        if (e.getKeyCode() == e.VK_X ) {

        }
        if (e.getKeyCode() == e.VK_ENTER) {
            
        }                 
    }//end keypressed   
}