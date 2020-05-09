/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.sonic;

import game.GameLoop;
import game.input.PlayerInput;
import game.overworld.Ground;
import game.overworld.Room;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class OWARemastered {

    private static int xDrawCenterSonic;
    private static int yDrawCenterSonic;
    private static int ySpriteCenterSonic;
    private static int yLastGround;
    private static double xSpeed;
    private static double ySpeed;
    private static double groundSpeed;
    private static double slope;
    private static double angle;
    private static double friction;
    private static double spindashRev;
    private static boolean grounded;
    private static boolean collideWithSlopeL;
    private static boolean collideWithSlopeR;
    private static boolean bLCollide;
    private static boolean bRCollide;
    private static boolean tLCollide;
    private static boolean tRCollide;
    private static boolean mLCollide;
    private static boolean mRCollide;
    private static int bLDistanceFromRect;
    private static int bRDistanceFromRect;
    private static int waitTimer;
    
    private static double ACCELERATION = 0.046875;
    private static double AIR = 0.09375;
    private static double DECELERATION = 0.15;
    private static double ROLLDECELERATION = 0.025;
    private static double FRICTION = 0.046875;
    private static double ROLLFRICTION = 0.0234375;
    private static double GRAVITY = 0.21875;
    private static double JUMP = 6.5;
    private static double SLOPE = 0.125;
    private static double SLOPEROLLUP = 0.078125;
    private static double SLOPEROLLDOWN = 0.3125;
    private static double TOPSPEED = 6;   
    
    private static Ground middleLeftIntersect;
    private static Ground middleRightIntersect;
    
    private static Rectangle bottomLeft;
    private static Rectangle bottomRight;
    private static Rectangle middleLeft;
    private static Rectangle middleRight;
    private static Rectangle topLeft;
    private static Rectangle topRight;
    
    private static Animation animation;
    private static Room currentRoom;
    private static Sonic sonic;
    
    private static DuckState duckState;
    private static JumpState jumpState;
    private static LedgeState ledgeState;
    private static SpindashState spindashState;
    
    public OWARemastered() {
        xDrawCenterSonic = 100;
        ySpriteCenterSonic = 625;
        yLastGround = 0;
        xSpeed = 0;
        ySpeed = 0;
        groundSpeed = 0;
        slope = SLOPE;
        angle = 0; 
        friction = FRICTION;
        grounded = false;
        collideWithSlopeL = false;
        collideWithSlopeR = false;
        bLCollide = false;
        bRCollide = false;
        tLCollide = false;
        tRCollide = false;
        bLDistanceFromRect = 0;
        bRDistanceFromRect = 0;
        waitTimer = 0;
        ledgeState = LedgeState.STATE_NOLEDGE;
        jumpState = JumpState.STATE_NOJUMP;
        spindashState = SpindashState.STATE_NOSPINDASH;
        middleLeftIntersect = null;
    }
    
    public void mainMethod(Sonic son, Room cR, Animation ani){
        currentRoom = cR;
        animation = ani;
        sonic = son;
        yDrawCenterSonic = ySpriteCenterSonic + 16;
        ledgeState = LedgeState.STATE_NOLEDGE;
        if(bLCollide && bRCollide) {
            grounded = true;
        }
        else if(bLCollide || bRCollide) {
            grounded = true;
        }
        else if(!bLCollide && !bRCollide) {
            grounded = false;
        }
        if(!collideWithSlopeL && !collideWithSlopeR) {
            bottomLeft = new Rectangle(xDrawCenterSonic-36,ySpriteCenterSonic,4,80);    
            bottomRight = new Rectangle(xDrawCenterSonic+36,ySpriteCenterSonic,4,80); 
            middleLeft = new Rectangle(xDrawCenterSonic-40,ySpriteCenterSonic+28,40,4);
            middleRight = new Rectangle(xDrawCenterSonic+4,ySpriteCenterSonic+28,40,4);            
        }
        else if(collideWithSlopeL && !collideWithSlopeR) {
            bottomLeft = new Rectangle(xDrawCenterSonic-36,ySpriteCenterSonic,4,144);    
            bottomRight = new Rectangle(xDrawCenterSonic+36,ySpriteCenterSonic,4,80);  
            middleLeft = new Rectangle(xDrawCenterSonic-40,ySpriteCenterSonic-4,40,4);
            middleRight = new Rectangle(xDrawCenterSonic+4,ySpriteCenterSonic-4,40,4);
        }
        else if(!collideWithSlopeL && collideWithSlopeR) {
            bottomLeft = new Rectangle(xDrawCenterSonic-36,ySpriteCenterSonic,4,80);    
            bottomRight = new Rectangle(xDrawCenterSonic+36,ySpriteCenterSonic,4,144);
            middleLeft = new Rectangle(xDrawCenterSonic-40,ySpriteCenterSonic-4,40,4);
            middleRight = new Rectangle(xDrawCenterSonic+4,ySpriteCenterSonic-4,40,4);            
        }
        else if(collideWithSlopeL && collideWithSlopeR) {
            bottomLeft = new Rectangle(xDrawCenterSonic-36,ySpriteCenterSonic,4,144);    
            bottomRight = new Rectangle(xDrawCenterSonic+36,ySpriteCenterSonic,4,144);  
            middleLeft = new Rectangle(xDrawCenterSonic-40,ySpriteCenterSonic-4,40,4);
            middleRight = new Rectangle(xDrawCenterSonic+4,ySpriteCenterSonic-4,40,4);
        }
        if(!grounded) {/*Added this to reset the height of Sonic sensors if he jumps off a slope (ground == true wouldn't trigger 
            early) making Sonic stop earlier then he should*/
            bottomLeft = new Rectangle(xDrawCenterSonic-36,ySpriteCenterSonic,4,80);    
            bottomRight = new Rectangle(xDrawCenterSonic+36,ySpriteCenterSonic,4,80); 
            middleLeft = new Rectangle(xDrawCenterSonic-40,ySpriteCenterSonic-4,40,4);
            middleRight = new Rectangle(xDrawCenterSonic+4,ySpriteCenterSonic-4,40,4);            
        }
        topLeft = new Rectangle(xDrawCenterSonic-36,ySpriteCenterSonic-84,4,80);
        topRight = new Rectangle(xDrawCenterSonic+36,ySpriteCenterSonic-84,4,80);
        if(PlayerInput.getLeftPress()) {
            leftPress();
        }
        else if(PlayerInput.getRightPress()) {
            rightPress();
        }
        if(PlayerInput.getUpPress()) {
            ySpeed = -3;
        }
        else if(PlayerInput.getDownPress()) {
            downPress();
        }
        performSpindash();
        if(PlayerInput.getZPress()) {//Jump has to be calculated before gravity is added to ySpeed
            zPress();
        }
        else if(!PlayerInput.getZPress() && PlayerInput.checkZReleased()) {
            zReleased();
        }           
        /*This resets the duckState if Sonic jumps (this is why is placed here right after the code for jumping),
        and resets spindashRev to 0 (so Sonic won't boost again on ground when he ducks) 
        */
        if(jumpState != JumpState.STATE_NOJUMP) {
            duckState = DuckState.STATE_NODUCK;
            spindashRev = 0;
        }
        //resets the duckState if groundSpeed is less than abs(0.5) (prevents turning bug from Genesis games)
        if(!PlayerInput.getDownPress() && Math.abs(groundSpeed) < 0.5 && angle == 0) {
            duckState = DuckState.STATE_NODUCK;
        }
        //resets the duckState if the down key is released and Sonic is ducking
        if(PlayerInput.checkDownReleased() && duckState == DuckState.STATE_DUCK && spindashState == SpindashState.STATE_NOSPINDASH) {
            duckState = DuckState.STATE_NODUCK;
        }
        //Sets correct value for friction
        if(duckState == DuckState.STATE_ROLL) {
            friction = ROLLFRICTION;           
            if(Math.signum(groundSpeed) == Math.signum(Math.sin(angle))) {//Checks if Sonic is rolling uphill
                slope = SLOPEROLLUP;
            }
            else if(Math.signum(groundSpeed) != Math.signum(Math.sin(angle))) {//Checks if Sonic is rolling downhill
                slope = SLOPEROLLDOWN;
            }            
        }
        else {
            friction = FRICTION;
            slope = SLOPE;
        }
        //Gravity code goes here
        if(!grounded) {    
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
        /*Be careful when creating tiles, especially ones that allow Sonic's bottom and middle sensors to intersect 
        the same tile*/
        sideCheck();
        if(tLCollide || tRCollide) {
            ySpeed = 1;
        }
        if(mLCollide || mRCollide) {
            xSpeed = 0;
            groundSpeed = 0;
            spindashRev = 0;
        }  
        if(grounded) {
            /*I'm getting the correct groundSpeed when sonic first is grounded (if grounded == true and 
            jump == STATE_JUMP_DOWN, this means that sonic just landed after a jump*/
            if(jumpState == JumpState.STATE_JUMP_DOWN && angle < 45) {
                groundSpeed = xSpeed;
            }
            else if(jumpState == JumpState.STATE_JUMP_DOWN && angle == 45) {
                if(Math.abs(xSpeed) > ySpeed) {
                    groundSpeed = xSpeed;    
                }
                else {
                    groundSpeed = ySpeed*0.5*-Math.signum(Math.sin(angle));
                }          
            }
            jumpState = JumpState.STATE_NOJUMP;
            /*Remember- the SLOPE variable controls how Sonic interacts with slopes (slides on them)
            and changes depending if Sonic is rolling/running/etc
            BUG- If a sensor interacts with a slope and another one interacts on another one (this occurs
            when Sonic is standing on one tile with two opposing slopes on either side of him,), Sonic will
            be pushed in the wrong direction by a tile*/            
            groundSpeed -= slope*Math.sin(angle);
            if(!PlayerInput.getLeftPress() && !PlayerInput.getRightPress() && grounded) {
                groundSpeed -= Math.min(Math.abs(groundSpeed), friction) * Math.signum(groundSpeed);    
            }  
            else if(duckState == DuckState.STATE_ROLL) {
                /*If you are rolling and pressing in the direction of your motion- friction is still in effect
                and Sonic should still slow down*/              
                if(groundSpeed < 0 && PlayerInput.getLeftPress()) {
                    groundSpeed -= Math.min(Math.abs(groundSpeed), friction) * Math.signum(groundSpeed);   
                }
                else if(groundSpeed > 0 && PlayerInput.getRightPress()) {
                    groundSpeed -= Math.min(Math.abs(groundSpeed), friction) * Math.signum(groundSpeed);   
                }
            }
            /*NOTE! If you want to change groundSpeed, you have to put the code before here! (since this is 
            where groundSpeed effects xSpeed*/
            xSpeed = groundSpeed*Math.cos(angle);
            ySpeed = groundSpeed*-Math.sin(angle);
        }             
        xDrawCenterSonic += (int) xSpeed;
        ySpriteCenterSonic += (int) ySpeed;
        bottomTopCheck();//This needs to go after gravity is calculated (since it affects ySpeed)!
        changeAnimation();    
    }
    
    private void sideCheck() {
        mLCollide = false;
        mRCollide = false;
        if(grounded) {
            if(groundSpeed > 0) {
               sideCollision(middleRight);
            }
            else if(groundSpeed < 0) {
               sideCollision(middleLeft);
            }            
        }
        else if(!grounded) {
            if(xSpeed < 0) {
                sideCollision(middleLeft);
            }
            else if(xSpeed > 0) {
                sideCollision(middleRight);
            }
        }
    }
    private void bottomTopCheck() {
        bLCollide = false;
        bRCollide = false;
        tLCollide = false;
        tRCollide = false;
        if(grounded) {
            bottomCollision();             
        }
        else if(!grounded) {
            if(ySpeed > 0 || Math.abs(xSpeed) > Math.abs(ySpeed)) {
                bottomCollision();     
            }
            else if(ySpeed < 0 || Math.abs(xSpeed) > Math.abs(ySpeed)) {
                topCollision();
            }
        }
    }
    
    private void bottomCollision() {
        int xBottomLeft = (int) bottomLeft.getX();
        int xBottomRight = (int) bottomRight.getX();  
        int yBottomSensor = (int) (ySpriteCenterSonic+80);
        int heightBottomLeftIndex = 0;
        int heightBottomRightIndex = 0;
        int pixelyL = ySpriteCenterSonic+80;
        int pixelyR = ySpriteCenterSonic+80;
        bLDistanceFromRect = 64;
        bRDistanceFromRect = 64;
        Rectangle groundCheckL;
        Rectangle groundCheckR;
        Ground highLeft = getCorrectTile(yBottomSensor,bottomLeft);
        Ground highRight = getCorrectTile(yBottomSensor,bottomRight);
        /*g2.setColor(Color.BLACK);
        g2.drawString("highLeft: "+highLeft, 500, 100);
        g2.drawString("highRight: "+highRight, 500, 125);*/
        if(highLeft != null && middleLeftIntersect != highLeft) {
            heightBottomLeftIndex = (int) Math.abs(((xBottomLeft - highLeft.getXRef())/4));   
            pixelyL = (int) highLeft.getPixelBox(heightBottomLeftIndex).getY();
            groundCheckL = highLeft.getPixelBox(heightBottomLeftIndex);
            bLDistanceFromRect = Math.abs(yBottomSensor - (int) groundCheckL.getY());
            if(bottomLeft.intersects(groundCheckL)) {
                bLCollide = true;
                if(highLeft.getAngle() != 0) {
                    collideWithSlopeL = true;
                }
                else if(highLeft.getAngle() == 0) {
                    collideWithSlopeL = false;
                }
            }            
        }
        if(highRight != null && middleRightIntersect != highRight) {
            heightBottomRightIndex = (int) Math.abs(((xBottomRight - highRight.getXRef())/4));
            pixelyR = (int) highRight.getPixelBox(heightBottomRightIndex).getY();
            groundCheckR = highRight.getPixelBox(heightBottomRightIndex);
            bRDistanceFromRect = Math.abs(yBottomSensor - (int) groundCheckR.getY());
            if(bottomRight.intersects(groundCheckR)) {
                bRCollide = true;
                if(highRight.getAngle() != 0) {
                    collideWithSlopeR = true;
                }
                else if(highRight.getAngle() == 0) {
                    collideWithSlopeR = false;
                }
            }            
        }       
        /*g2.drawString("pixelyL: "+pixelyL, 500, 150);
        g2.drawString("pixelyR: "+pixelyR, 500, 175);*/
        if(pixelyL < pixelyR) {
            setSonicGroundStat(heightBottomLeftIndex,pixelyL, yBottomSensor, highLeft);
        }
        else if(pixelyR < pixelyL) {
            setSonicGroundStat(heightBottomRightIndex,pixelyR, yBottomSensor, highRight);
        } 
        else if(pixelyR == pixelyL && pixelyR != (ySpriteCenterSonic+80)) {
            /*Make sure that when pixelyR and pixelyL are equal that they are not equal to default value
            This will cause Sonic to be set to the default position! (ySpriteCenterSonic+80)) */
            /*Added this here to prevent Sonic from sliding down a slope if his front sensor (the one in
            front of him relative to his movement) collides with a slope*/
            if(groundSpeed > 0) {
                setSonicGroundStat(heightBottomRightIndex,pixelyR, yBottomSensor, highLeft);    
            }
            else if(groundSpeed < 0) {
                setSonicGroundStat(heightBottomRightIndex,pixelyR, yBottomSensor, highRight);    
            }
            else {//This is if Sonic jumps while standing still
                setSonicGroundStat(heightBottomRightIndex,pixelyR, yBottomSensor, highRight);
            }           
        }
        
        //Get's Sonic's ledges
        if(grounded && xSpeed == 0 && angle == 0 && bLCollide && !bRCollide && bRDistanceFromRect >= 48) {
            if(highLeft != null) {
                if(xDrawCenterSonic >= highLeft.getXRef()+64+4 && animation.getDirection() == 1) {
                    ledgeState = LedgeState.STATE_RIGHTLEDGE;    
                }
            }
            
        }
        else if(grounded && xSpeed == 0 && angle == 0 && !bLCollide && bRCollide && bLDistanceFromRect >= 48) {
            if(highRight != null) {
                if(xDrawCenterSonic <= highRight.getXRef()-4 && animation.getDirection() == 0) {
                    ledgeState = LedgeState.STATE_LEFTLEDGE;    
                }
            }
        }          
    } 
    
    private Ground getCorrectTile(int yBottomSensor, Rectangle sensor) {
        int xBottomSensor = (int) sensor.getX();     
        /*if(sensor == bottomLeft) {
            g2.setColor(Color.MAGENTA);
            g2.fillRect(xBottomSensor,ySpriteCenterSonic,1,80);
            g2.fillRect(xBottomSensor,ySpriteCenterSonic+143,1,1);
        }
        if(sensor == bottomRight) {
            g2.setColor(Color.RED);
            g2.fillRect(xBottomSensor,ySpriteCenterSonic,1,80);
            g2.fillRect(xBottomSensor,ySpriteCenterSonic+143,1,1);
        }*/
        int xBottomIndex = xBottomSensor/64;
        int yBottomIndex = yBottomSensor/64;       
        Ground intersect = currentRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex);
        if(intersect != null) {
            if(sensor == bottomLeft || sensor == bottomRight) {
                Ground intersectUp = currentRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex-1);
                Ground higher = Ground.compareSCTile(xBottomSensor, intersect, intersectUp, sonic);
                return higher;    
            }
            else if(sensor == topLeft || sensor == topRight) {
                Ground intersectDown = currentRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex+1);
                Ground higher = Ground.compareSCTile(xBottomSensor, intersect, intersectDown, sonic);
                return higher;    
            }
        }
        else {
            if(sensor == bottomLeft || sensor == bottomRight) {
                Ground intersectDown = currentRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex+1);
                Ground higher = Ground.compareSCTile(xBottomSensor, intersect, intersectDown, sonic);
                return higher;    
            }
            else if(sensor == topLeft || sensor == topRight) {
                Ground intersectUp = currentRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex-1);
                Ground higher = Ground.compareSCTile(xBottomSensor, intersect, intersectUp, sonic);
                return higher;    
            }
        }
        return null;
    }
    
    private void setSonicGroundStat(int heightIndex, int pixelHeight, int yBottomSensor, Ground highest) {     
        if(highest != null) {
            Rectangle collideCheck = highest.getPixelBox(heightIndex);
            if(highest.getAngle() != 0 && yBottomSensor >= (int) collideCheck.getY()-32) {
                yLastGround = pixelHeight;
                ySpriteCenterSonic = pixelHeight - 76; 
                angle = highest.getAngle();    
            }
            else if(highest.getAngle() == 0 && yBottomSensor >= (int) collideCheck.getY()) {
                yLastGround = pixelHeight;
                ySpriteCenterSonic = pixelHeight - 76; 
                angle = highest.getAngle();    
            }
        }        
    }
    private void topCollision() {
        int xTopLeft = (int) topLeft.getX();
        int xTopRight = (int) topRight.getX();  
        int yBottomSensor = ySpriteCenterSonic;
        int heightBottomLeftIndex = 0;
        int heightBottomRightIndex = 0;
        int pixelyL = -90000;
        int pixelyR = -90000;
        Rectangle groundCheckL;
        Rectangle groundCheckR;
        Ground lowLeft = getCorrectTile(yBottomSensor,topLeft);
        Ground lowRight = getCorrectTile(yBottomSensor,topRight);
        /*g2.setColor(Color.BLACK);
        g2.drawString("lowLeft: "+lowLeft, 500, 100);
        g2.drawString("lowRight: "+lowRight, 500, 125);
        g2.setColor(Color.BLUE);
        g2.fill(topLeft);
        g2.setColor(Color.YELLOW);
        g2.fill(topRight);*/
        if(lowLeft != null) {
            heightBottomLeftIndex = (int) Math.abs(((xTopLeft - lowLeft.getXRef())/4));   
            pixelyL = (int) (lowLeft.getPixelBox(heightBottomLeftIndex).getY()+lowLeft.getPixelBox(heightBottomLeftIndex).getHeight());
            groundCheckL = lowLeft.getPixelBox(heightBottomLeftIndex);
            if(topLeft.intersects(groundCheckL)) {
                tLCollide = true;
            }
        }  
        if(lowRight != null) {
            heightBottomRightIndex = (int) Math.abs(((xTopRight - lowRight.getXRef())/4));   
            pixelyR = (int) (lowRight.getPixelBox(heightBottomRightIndex).getY()+lowRight.getPixelBox(heightBottomRightIndex).getHeight());
            groundCheckR = lowRight.getPixelBox(heightBottomRightIndex);
            if(topRight.intersects(groundCheckR)) {
                tRCollide = true;               
            }
        }
        if(pixelyL > pixelyR) {            
            ySpriteCenterSonic = pixelyL + 64;
            jumpState = JumpState.STATE_JUMP_DOWN;
        }
        else if(pixelyR > pixelyL) {
            ySpriteCenterSonic = pixelyR + 64;
            jumpState = JumpState.STATE_JUMP_DOWN;
        } 
        else if(pixelyR == pixelyL && pixelyR != -90000) {
            ySpriteCenterSonic = pixelyL + 64;
            jumpState = JumpState.STATE_JUMP_DOWN;
        }
    }
    
    private void sideCollision(Rectangle sideSensor) {
        int xMiddleSensor = 0;
        if(sideSensor == middleLeft) {
            xMiddleSensor = (int) sideSensor.getX();
            /*g2.setColor(Color.MAGENTA);
            g2.fill(middleLeft); */               
        }
        else if(sideSensor == middleRight) {
            xMiddleSensor = (int) (sideSensor.getX()+sideSensor.getWidth());
            /*g2.setColor(Color.YELLOW);
            g2.fill(middleRight);*/
        }
        int yMiddleSensor = (int) sideSensor.getY();
        int xBottomIndex = xMiddleSensor/64;
        int yBottomIndex = yMiddleSensor/64;
        Ground intersect = currentRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex);        
        //g2.drawString("intersect :"+intersect, 500, 200);
        if(intersect != null && sonic.getLayer() == intersect.getLayer()) {
            if(sideSensor == middleLeft) {               
                Rectangle collideCheck = intersect.getPixelBox(intersect.getPixelBoxes().size()-1);
                if(xMiddleSensor < (int) (collideCheck.getX()+collideCheck.getWidth())+4 && middleLeft.intersects(collideCheck)) {      
                    middleLeftIntersect = intersect;
                    groundSpeed = 0;
                    xSpeed = 0;
                    mLCollide = true;    
                    xDrawCenterSonic = (int) (collideCheck.getX()+collideCheck.getWidth()+38);
                }    
            }
            else if(sideSensor == middleRight) {                      
                Rectangle collideCheck = intersect.getPixelBox(0);
                if(xMiddleSensor > (int) collideCheck.getX() && middleRight.intersects(collideCheck)) {
                    middleRightIntersect = intersect;
                    groundSpeed = 0;
                    xSpeed = 0;
                    mRCollide = true;   
                    xDrawCenterSonic = (int) (collideCheck.getX()-40);
                }    
            }
        }
    }
    
    private void performSpindash() {
        //If Sonic is ducking and the Z Key is pressed, start spindashing (need state in order to change animation)
        if(grounded && duckState == DuckState.STATE_DUCK && PlayerInput.checkForZPressOnce() && spindashState == SpindashState.STATE_NOSPINDASH) {
            spindashState = SpindashState.STATE_SPINDASH;           
        }
        if(spindashRev > 0.01) {
            spindashRev -= ((spindashRev / 0.125) / 256); //"div" is division ignoring any remainder    
        }           
        else {
            spindashRev = 0;    
        } 
        if(spindashState == SpindashState.STATE_SPINDASH && PlayerInput.checkDownReleased()) {    
            //I changed the initial groundSpeed from 8 to 10 since the spindash was too weak
            if(animation.getDirection() == 0) {
                groundSpeed = -10 - ((int)(spindashRev) / 2); //Negative since Sonic would be facing left
            }
            else if(animation.getDirection() == 1) {
                groundSpeed = 10 + ((int)(spindashRev) / 2); //Positive since Sonic would be facing right    
            }                     
            spindashState = SpindashState.STATE_NOSPINDASH;
            duckState = DuckState.STATE_ROLL;                                                    
        }
    }
    
    private void leftPress() {
        if(!grounded) {
            animation.setDirection(0);
            if(xSpeed > -5) {               
                xSpeed -= AIR;    
            }
            else {
                xSpeed = -5;
            }
        }
        else if(grounded) {
            if(groundSpeed < 0) {//If Sonic's groundSpeed is less than 0, set his direction to 0 (left), this makes it so the player has to stop 
            //completely (skid) before changing direction (can't change direction immediately)
                animation.setDirection(0);
            }    
            /*Added to fix bug where if Sonic was against a wall, tapped the another direction and then pushed toward the wall,
            he would be pushing the wrong way (this is because Sonic can't change his direction since his groundSpeed would be 0)
            */                      
            if(grounded && groundSpeed == 0 && mLCollide && animation.getDirection() == 1) {
                animation.setDirection(1);
            }
            if(groundSpeed > 0) {
                if(duckState == DuckState.STATE_NODUCK) {
                    groundSpeed -= DECELERATION;    
                }
                else if(duckState == DuckState.STATE_ROLL) {
                    groundSpeed -= (ROLLDECELERATION+friction);
                }
                if(groundSpeed <= 0) {
                    groundSpeed = -0.5;
                }
            }
            else if(groundSpeed > -TOPSPEED && duckState == DuckState.STATE_NODUCK) {
                groundSpeed -= ACCELERATION;
                if(groundSpeed <= -TOPSPEED) {
                    groundSpeed = -TOPSPEED;
                }
            }             
        }       
    }
    
    private void rightPress() {
        if(!grounded) {
            animation.setDirection(1);
            if(xSpeed < 5) {               
                xSpeed += AIR;    
            }  
            else {
                xSpeed = 5;
            }
        }
        else if(grounded) {
            if(groundSpeed > 0) {//If Sonic's groundSpeed is less than 1, set his direction to 1 (right), this makes it so the player has to stop 
            //(completely skid) before changing direction (can't change direction immediately)
                animation.setDirection(1);
            }
            /*Added to fix bug where if Sonic was against a wall, tapped the another direction and then pushed toward the wall,
            he would be pushing the wrong way (this is because Sonic can't change his direction since his groundSpeed would be 0)
            */
            if(grounded && groundSpeed == 0 && mRCollide && animation.getDirection() == 0) {
                animation.setDirection(1);
            }
            if(groundSpeed < 0) {
                if(duckState == DuckState.STATE_NODUCK) {
                    groundSpeed += DECELERATION;    
                }
                else if(duckState == DuckState.STATE_ROLL) {
                    groundSpeed += (ROLLDECELERATION+friction);
                }
                if(groundSpeed >= 0) {
                    groundSpeed = 0.5;
                }
            }
            else if(groundSpeed < TOPSPEED && duckState == DuckState.STATE_NODUCK) {
                groundSpeed += ACCELERATION;
                if(groundSpeed >= TOPSPEED) {
                    groundSpeed = TOPSPEED;
                }
            }    
        }       
    }    
    
    private void downPress() {
        if(grounded) {
            if(angle == 0) {
                if(Math.abs(groundSpeed) < 1) {
                    duckState = DuckState.STATE_DUCK;
                }
                else if(Math.abs(groundSpeed) >= 1) {
                    duckState = DuckState.STATE_ROLL;
                }    
            }
            else {
                duckState = DuckState.STATE_ROLL;
            }
            
        }
    }
    
    private void zPress() {       
        if(grounded && spindashState == SpindashState.STATE_NOSPINDASH && jumpState == JumpState.STATE_NOJUMP) {
            jumpState = JumpState.STATE_JUMP_UP;
        }
        if(jumpState == JumpState.STATE_JUMP_UP && ySpriteCenterSonic < yLastGround-390) {
            if(ySpeed < -4) {
                ySpeed = -4;
            }
            jumpState = JumpState.STATE_JUMP_DOWN;
        }
        else if(jumpState == JumpState.STATE_JUMP_UP && ySpriteCenterSonic > yLastGround-400) {
            if(angle == 0) {           
                ySpeed = -JUMP;    
            }      
            else {
                //I have to fix when Sonic jumps on a slope
                ySpeed = -JUMP;    
            }
            grounded = false;
        }
        if(spindashState == SpindashState.STATE_SPINDASH && PlayerInput.checkForZPressOnce()) {
            spindashRev+=2;
        }
    }
    
    private void zReleased() {
        if(ySpeed < -4) {
            ySpeed = -4;
        }
        if(jumpState == JumpState.STATE_JUMP_UP) {
            jumpState = JumpState.STATE_JUMP_DOWN;
        }
    }
    private void changeAnimation() {
        /*Watch out for state conflicts- if one is conflicting with another one, it causes Sonic to freeze on his animation/
        or perform his animation wrong*/
        if(jumpState == JumpState.STATE_NOJUMP && ledgeState == LedgeState.STATE_NOLEDGE && duckState == DuckState.STATE_NODUCK) {
            if(grounded && groundSpeed == 0 && angle == 0 && !PlayerInput.getLeftPress() && !PlayerInput.getRightPress()) {
                waitTimer++;
                if(waitTimer < 988) {
                    if(animation.getAnimationNumber() != Animation.SonicAnimation.ANIMATION_SONIC_STAND) {
                        animation.setSonicAnimation(Animation.SonicAnimation.ANIMATION_SONIC_STAND);               
                    }                   
                }
                else if(waitTimer >= 998 && waitTimer < 3000) {
                    if(animation.getAnimationNumber() != Animation.SonicAnimation.ANIMATION_SONIC_WAIT) {
                        animation.setSonicAnimation(Animation.SonicAnimation.ANIMATION_SONIC_WAIT);               
                    }
                }
                else if(waitTimer >= 3000 && waitTimer < 3001) {
                    if(animation.getAnimationNumber() != Animation.SonicAnimation.ANIMATION_SONIC_BORED) {
                        animation.setSonicAnimation(Animation.SonicAnimation.ANIMATION_SONIC_BORED);               
                    }
                }
                else if(waitTimer >= 3000) {
                    waitTimer = 3000;
                }
            }
            else {//This resets waitTimer if the player starts to walk/run
                waitTimer = 0;
            }         
            if(Math.abs(groundSpeed) > 0.5 && Math.abs(groundSpeed) < 6 && (!mLCollide || !mRCollide)) {
                if(animation.getAnimationNumber() != Animation.SonicAnimation.ANIMATION_SONIC_WALK) {
                    animation.setSonicAnimation(Animation.SonicAnimation.ANIMATION_SONIC_WALK);    
                }
            }
            else if(Math.abs(groundSpeed) >= 6) {//Controls when Sonic's running animation plays
                if(animation.getAnimationNumber() != Animation.SonicAnimation.ANIMATION_SONIC_RUN) {
                    animation.setSonicAnimation(Animation.SonicAnimation.ANIMATION_SONIC_RUN);    
                }
            }
            if(PlayerInput.getLeftPress() && mLCollide && animation.getAnimationNumber() != Animation.SonicAnimation.ANIMATION_SONIC_PUSH) {
                animation.setSonicAnimation(Animation.SonicAnimation.ANIMATION_SONIC_PUSH);
            }
            else if(PlayerInput.getRightPress() && mRCollide && animation.getAnimationNumber() != Animation.SonicAnimation.ANIMATION_SONIC_PUSH) {
                animation.setSonicAnimation(Animation.SonicAnimation.ANIMATION_SONIC_PUSH);
            }
        }
        else {//This resets the waitTimer if the player is ducking/rolling/on ledge/spindashing/etc
            waitTimer = 0;
        }
        if(ledgeState == LedgeState.STATE_LEFTLEDGE && duckState == DuckState.STATE_NODUCK) {
            if(animation.getAnimationNumber() != Animation.SonicAnimation.ANIMATION_SONIC_TRIPA) {
                animation.setSonicAnimation(Animation.SonicAnimation.ANIMATION_SONIC_TRIPA);    
            }
        }
        else if(ledgeState == LedgeState.STATE_RIGHTLEDGE && duckState == DuckState.STATE_NODUCK) {
            if(animation.getAnimationNumber() != Animation.SonicAnimation.ANIMATION_SONIC_TRIPA) {
                animation.setSonicAnimation(Animation.SonicAnimation.ANIMATION_SONIC_TRIPA);    
            }
        }
        if(jumpState == JumpState.STATE_JUMP_UP || jumpState == JumpState.STATE_JUMP_DOWN) {
            if(animation.getAnimationNumber() != Animation.SonicAnimation.ANIMATION_SONIC_JUMP) {
                animation.setSonicAnimation(Animation.SonicAnimation.ANIMATION_SONIC_JUMP);   
            }
        }
        if(duckState == DuckState.STATE_DUCK && spindashState == SpindashState.STATE_NOSPINDASH) {
            if(animation.getAnimationNumber() != Animation.SonicAnimation.ANIMATION_SONIC_DUCK) {
                animation.setSonicAnimation(Animation.SonicAnimation.ANIMATION_SONIC_DUCK);   
            }
        }
        else if(duckState == DuckState.STATE_ROLL) {
            if(animation.getAnimationNumber() != Animation.SonicAnimation.ANIMATION_SONIC_JUMP) {
                animation.setSonicAnimation(Animation.SonicAnimation.ANIMATION_SONIC_JUMP);   
            }
        }
        if(duckState == DuckState.STATE_DUCK && spindashState == SpindashState.STATE_SPINDASH) {
            if(animation.getAnimationNumber() != Animation.SonicAnimation.ANIMATION_SONIC_SPINDASH) {
                animation.setSonicAnimation(Animation.SonicAnimation.ANIMATION_SONIC_SPINDASH);   
            }
        }
    }
    public int getXCenterSonic() {
        return xDrawCenterSonic;
    }
    
    public int getYCenterSonic() {
        return yDrawCenterSonic;
    }

    public void drawDebug(Graphics2D g2) {
        if(GameLoop.getDebug()) {
            g2.setColor(Color.MAGENTA);
            g2.drawString("DEBUG MENU",600,25);        
            //Variables that have to do with Sonic's x and y position, checking ground, x and y speed, etc:
            g2.drawString("xDrawCenterSonic: "+xDrawCenterSonic,75,75);
            g2.drawString("yDrawCenterSonic: "+yDrawCenterSonic,75,100);
            g2.drawString("ySpriteCenterSonic: "+ySpriteCenterSonic,75,125);
            g2.drawString("grounded: "+grounded,75,150);
            g2.drawString("groundSpeed: "+groundSpeed,75,175);
            g2.drawString("xSpeed: "+xSpeed,75,200);
            g2.drawString("ySpeed: "+ySpeed,75,225);       
            g2.drawString("slope: "+slope,75,250);
            g2.drawString("collideWithSlopeL: "+collideWithSlopeL,75,275);
            g2.drawString("collideWithSlopeR: "+collideWithSlopeR,75,300);
            g2.drawString("bLCollide: "+bLCollide,75,325);
            g2.drawString("bRCollide: "+bRCollide,75,350);
            g2.drawString("tLCollide: "+tLCollide,75,375);
            g2.drawString("tRCollide: "+tRCollide,75,400);
            g2.drawString("mLCollide: "+mLCollide,75,425);
            g2.drawString("mRCollide: "+mRCollide,75,450);
            g2.drawString("bLDistanceFromRect: "+bLDistanceFromRect,75,475);
            g2.drawString("bRDistanceFromRect: "+bRDistanceFromRect,75,500);
            g2.drawString("yLastGround: "+yLastGround,75,525);
            g2.drawString("friction: "+friction,75,550);
            g2.drawString("spindashRev: "+spindashRev,75,575);
            //Variables that have to do with Sonic's animations:
            g2.setColor(Color.GREEN);
            g2.drawString("angle: "+angle, 400, 75);
            //g2.drawString("direction: "+animation.getDirection(), 400, 100);
            g2.drawString("waitTimer: "+waitTimer, 400, 125);
            g2.setColor(Color.ORANGE);
            g2.drawString("Sonic's Ledge State: "+ledgeState, 75, 700);
            g2.drawString("Sonic's Jump State: "+jumpState, 75, 725);
            g2.drawString("Sonic's Duck State: "+duckState, 75, 750);
            g2.drawString("Sonic's Spindash State: "+spindashState, 75, 775);   
            g2.setColor(Color.BLUE);    
            g2.fill(topLeft);
            g2.setColor(Color.YELLOW);    
            g2.fill(topRight);
            g2.setColor(Color.MAGENTA);    
            g2.fill(middleLeft);
            g2.setColor(Color.RED);    
            g2.fill(middleRight);
            g2.setColor(Color.GREEN);    
            g2.fill(bottomLeft);
            g2.setColor(Color.CYAN);    
            g2.fill(bottomRight);
        }          
    }
    
    public enum LedgeState {
        STATE_NOLEDGE,
        STATE_LEFTLEDGE,
        STATE_RIGHTLEDGE,
    } 
    
    public enum JumpState {
        STATE_NOJUMP,
        STATE_JUMP_UP,
        STATE_JUMP_DOWN,
    }
    
    public enum DuckState {
        STATE_NODUCK,
        STATE_DUCK,
        STATE_ROLL
    }
    
    public enum SpindashState {
        STATE_NOSPINDASH,
        STATE_SPINDASH
    }
}