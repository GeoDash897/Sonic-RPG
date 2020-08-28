/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.player;

import game.Launcher;
import game.overworld.Ground;
import game.overworld.Room;
import game.player.PlayerCharacter.PlayerName;
import game.player.PlayerManager.OWPosition;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author GeoSonicDash
 */
public class BasicOWA {
    private int xDrawCenterPlayer;
    private int yDrawCenterPlayer;
    private int ySpriteCenterPlayer;
    private int yLastGround;
    private int springTimer;
    private boolean springLock;
    private double xSpeed;
    private double ySpeed;
    private double groundSpeed;
    private double slope;
    private double angle;
    private double friction;
    private boolean grounded;
    private boolean collideWithSlopeL;
    private boolean collideWithSlopeR;
    private boolean bLCollide;
    private boolean bRCollide;
    private boolean tLCollide;
    private boolean tRCollide;
    private boolean mLCollide;
    private boolean mRCollide;
    private static boolean allowInput;
    private int bLDistanceFromRect;
    private int bRDistanceFromRect;   
    
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
    
    private Ground middleLeftIntersect;
    private Ground middleRightIntersect;
    
    private Rectangle bottomLeft;
    private Rectangle bottomRight;
    private Rectangle middleLeft;
    private Rectangle middleRight;
    private Rectangle topLeft;
    private Rectangle topRight;
    private Rectangle intersectBox;
    
    private AnimationControl animation;
    private OWPosition owPosition;
    private PlayerCharacter partner;
    private Room currentRoom;
    private static PlayerManager manager;
    
    private DuckState duckState;
    private JumpState jumpState;
    private LedgeState ledgeState;
    private SpringState springState;
    private FrontPosition frontPosition;
    
    public BasicOWA(PlayerManager man, OWPosition owPosition, AnimationControl aniC, int x, int y) {
        this.xDrawCenterPlayer = x;
        this.ySpriteCenterPlayer = y;
        this.yLastGround = 0;
        this.xSpeed = 0;
        this.ySpeed = 0;
        this.groundSpeed = 0;
        this.slope = SLOPE;
        this.angle = 0; 
        this.friction = FRICTION;
        this.grounded = false;
        this.collideWithSlopeL = false;
        this.collideWithSlopeR = false;
        this.bLCollide = false;
        this.bRCollide = false;
        this.tLCollide = false;
        this.tRCollide = false;
        allowInput = true;
        this.springTimer = 0;
        this.bLDistanceFromRect = 0;
        this.bRDistanceFromRect = 0;
        this.duckState = DuckState.STATE_NODUCK;
        this.ledgeState = LedgeState.STATE_NOLEDGE;
        this.jumpState = JumpState.STATE_NOJUMP;
        this.springState = SpringState.STATE_NOSPRING;
        this.middleLeftIntersect = null;
        this.middleRightIntersect = null;
        this.owPosition = owPosition;
        this.frontPosition = FrontPosition.STATE_MIDDLE;
        this.partner = null;
        manager = man;
        this.animation = aniC;
    }
    
    public void updateRoomPartner(Room cR) {
        //Get current Room that player is currently in
        currentRoom = cR;
        if(manager.getCharacterList().size() > 1 && owPosition == OWPosition.OWPOSITION_FRONT) {
            partner = manager.getCharacter(1);
        }
        else if(manager.getCharacterList().size() > 1 && owPosition == OWPosition.OWPOSITION_BACK) {
            partner = manager.getCharacter(0);
        }
    }
    
    public void resetLeftRightMiddleIntersect() {
        middleLeftIntersect = null;
        middleRightIntersect = null;
    }
    
    public void resetDrawPosLedgeState() {
        if(manager.getCharacter(owPosition.getPosition()).getPlayerName() == PlayerName.PLAYERNAME_SONIC) {
            yDrawCenterPlayer = ySpriteCenterPlayer + 16;
        }
        else if(manager.getCharacter(owPosition.getPosition()).getPlayerName() == PlayerName.PLAYERNAME_MARIO) {
            yDrawCenterPlayer = ySpriteCenterPlayer;
        }
        ledgeState = LedgeState.STATE_NOLEDGE;
    }
    
    public void updateSensors() {
        //Controls when Sonic is considered on the ground or not
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
            bottomLeft = new Rectangle(xDrawCenterPlayer-36,ySpriteCenterPlayer,4,80);    
            bottomRight = new Rectangle(xDrawCenterPlayer+36,ySpriteCenterPlayer,4,80); 
            middleLeft = new Rectangle(xDrawCenterPlayer-40,ySpriteCenterPlayer+28,40,4);
            middleRight = new Rectangle(xDrawCenterPlayer+4,ySpriteCenterPlayer+28,40,4);            
        }
        else if(collideWithSlopeL && !collideWithSlopeR) {
            bottomLeft = new Rectangle(xDrawCenterPlayer-36,ySpriteCenterPlayer,4,144);    
            bottomRight = new Rectangle(xDrawCenterPlayer+36,ySpriteCenterPlayer,4,80);  
            middleLeft = new Rectangle(xDrawCenterPlayer-40,ySpriteCenterPlayer-4,40,4);
            middleRight = new Rectangle(xDrawCenterPlayer+4,ySpriteCenterPlayer-4,40,4);
        }
        else if(!collideWithSlopeL && collideWithSlopeR) {
            bottomLeft = new Rectangle(xDrawCenterPlayer-36,ySpriteCenterPlayer,4,80);    
            bottomRight = new Rectangle(xDrawCenterPlayer+36,ySpriteCenterPlayer,4,144);
            middleLeft = new Rectangle(xDrawCenterPlayer-40,ySpriteCenterPlayer-4,40,4);
            middleRight = new Rectangle(xDrawCenterPlayer+4,ySpriteCenterPlayer-4,40,4);            
        }
        else if(collideWithSlopeL && collideWithSlopeR) {
            bottomLeft = new Rectangle(xDrawCenterPlayer-36,ySpriteCenterPlayer,4,144);    
            bottomRight = new Rectangle(xDrawCenterPlayer+36,ySpriteCenterPlayer,4,144);  
            middleLeft = new Rectangle(xDrawCenterPlayer-40,ySpriteCenterPlayer-4,40,4);
            middleRight = new Rectangle(xDrawCenterPlayer+4,ySpriteCenterPlayer-4,40,4);
        }
        if(!grounded) {/*Added this to reset the height of the player sensors if he jumps off a slope (ground == true wouldn't trigger 
            early) making the player stop earlier then he should*/
            bottomLeft = new Rectangle(xDrawCenterPlayer-36,ySpriteCenterPlayer,4,80);    
            bottomRight = new Rectangle(xDrawCenterPlayer+36,ySpriteCenterPlayer,4,80); 
            middleLeft = new Rectangle(xDrawCenterPlayer-40,ySpriteCenterPlayer-4,40,4);
            middleRight = new Rectangle(xDrawCenterPlayer+4,ySpriteCenterPlayer-4,40,4);            
        }
        topLeft = new Rectangle(xDrawCenterPlayer-36,ySpriteCenterPlayer-84,4,80);
        topRight = new Rectangle(xDrawCenterPlayer+36,ySpriteCenterPlayer-84,4,80);  
        //Gets the correct size of interactBox
        if(jumpState != JumpState.STATE_NOJUMP || duckState != DuckState.STATE_NODUCK) {
            intersectBox = new Rectangle(xDrawCenterPlayer-30, ySpriteCenterPlayer-20, 60, 80);
        }
        else {
            intersectBox = new Rectangle(xDrawCenterPlayer-29, ySpriteCenterPlayer-70, 60, 140);
        }
    }   
    
    public void springLock() {
        //Prevent input for certain amount of time after hitting a sideways spring
        if(springLock) {
            allowInput = false;
            if(springTimer < 50 && !mLCollide && !mRCollide) {
                springTimer++;
            }
            else if(springTimer == 50 || (mLCollide || mRCollide)) {
                springTimer = 0;                  
                springLock = false;
                allowInput = true;
            }
        }
    }
    
    public void setNormalFrictionSlope() {
        friction = FRICTION;
        slope = SLOPE;
    }
    
    public void controlGravity() {
        if(!grounded) {    
            if (ySpeed < 0 && ySpeed > -4)//air drag calculation
            {
                if (Math.abs(xSpeed) >= 0.125) {
                    xSpeed = xSpeed * 0.96875;
                }
            }   
            ySpeed += GRAVITY;//Causes the player to move down
            if(ySpeed > 16) {//limits how fast the player is falling (so he won't clip through tiles)
                ySpeed = 16;
            }
        }  
    }
    
    public void resetSpeedTopCollideCheck() {
        if(tLCollide || tRCollide) {
            ySpeed = 1;
        }
    }
    
    public void getGSpeedAfterJumpLand() {
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
        springState = SpringState.STATE_NOSPRING;
    }
    
    public void updateGroundSpeed() {
        groundSpeed -= slope*Math.sin(angle);
    }
    
    public void updateXSpeed() {
        xSpeed = groundSpeed*Math.cos(angle);
    }
    
    public void updateYSpeed() {
        ySpeed = groundSpeed*-Math.sin(angle);
    }
    
    public void updatePlayerPosition() {
        xDrawCenterPlayer += (int) xSpeed; 
        ySpriteCenterPlayer += (int) ySpeed;
    }
    
    /**Controls when sideCollision methods are run.
     * 
     */
    public void sideCheck() {
        /*Be careful when creating tiles, especially ones that allow Sonic's bottom and middle sensors to intersect 
        the same tile*/
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
        if(intersect != null && animation.getLayer() == intersect.getLayer()) {
            if(sideSensor == middleLeft) {               
                Rectangle collideCheck = intersect.getPixelBox(intersect.getPixelBoxes().size()-1);
                if(xMiddleSensor < (int) (collideCheck.getX()+collideCheck.getWidth())+4 && middleLeft.intersects(collideCheck)) {      
                    middleLeftIntersect = intersect;
                    setCollideLeftStats(collideCheck);
                }    
            }
            else if(sideSensor == middleRight) {                      
                Rectangle collideCheck = intersect.getPixelBox(0);
                if(xMiddleSensor > (int) collideCheck.getX() && middleRight.intersects(collideCheck)) {
                    middleRightIntersect = intersect;
                    setCollideRightStats(collideCheck);
                }    
            }
        }
    }
    
    public void setCollideLeftStats(Rectangle collideCheck) {
        groundSpeed = 0;
        xSpeed = 0;
        mLCollide = true;    
        xDrawCenterPlayer = (int) (collideCheck.getX()+collideCheck.getWidth()+38);
    }
    
    public void setCollideRightStats(Rectangle collideCheck) {
        groundSpeed = 0;
        xSpeed = 0;
        mRCollide = true;   
        xDrawCenterPlayer = (int) (collideCheck.getX()-40);
    }
    
    /**Controls when bottomCollision and topCollision methods are run.
     * 
     */
    public void bottomTopCheck() {
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
        int yBottomSensor = (int) (ySpriteCenterPlayer+80);
        int heightBottomLeftIndex = 0;
        int heightBottomRightIndex = 0;
        int pixelyL = ySpriteCenterPlayer+80;
        int pixelyR = ySpriteCenterPlayer+80;
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
        if(pixelyL < pixelyR) {
            setPlayerGroundStat(heightBottomLeftIndex,pixelyL, yBottomSensor, highLeft);
        }
        else if(pixelyR < pixelyL) {
            setPlayerGroundStat(heightBottomRightIndex,pixelyR, yBottomSensor, highRight);
        } 
        else if(pixelyR == pixelyL && pixelyR != (ySpriteCenterPlayer+80)) {
            /*Make sure that when pixelyR and pixelyL are equal that they are not equal to default value
            This will cause Sonic to be set to the default position! (ySpriteCenterPlayer+80)) */
            /*Added this here to prevent Sonic from sliding down a slope if his front sensor (the one in
            front of him relative to his movement) collides with a slope*/
            if(groundSpeed > 0) {
                setPlayerGroundStat(heightBottomRightIndex,pixelyR, yBottomSensor, highLeft);    
            }
            else if(groundSpeed < 0) {
                setPlayerGroundStat(heightBottomRightIndex,pixelyR, yBottomSensor, highRight);    
            }
            else {//This is if Sonic jumps while standing still
                setPlayerGroundStat(heightBottomRightIndex,pixelyR, yBottomSensor, highRight);
            }           
        }
        
        //Get's player's ledges
        getLedgeGround(highLeft, highRight);        
    } 
    
    /**Sets player's y position variables and collision variables based on the gameObject he is
     * interacting with.
     * @param sensor the {@code Rectangle} sensor that is currently being checked.
     * @param collideCheck the intersectBox of the gameObject that the player is currently interacting with.
     */
    public void setSonicBottomGameStat(Rectangle sensor, Rectangle collideCheck) {
        int yBottomSensor = (int) (sensor.getY()+sensor.getHeight());       
        if(sensor == bottomLeft) {
            bLCollide = true;
        }
        else if(sensor == bottomRight) {
            bRCollide = true;
        }
        if(yBottomSensor >= (int) collideCheck.getY()) {
            yLastGround = (int) collideCheck.getY();
            ySpriteCenterPlayer = (int) (collideCheck.getY() - 76);  
        } 
    }
    
    private void topCollision() {
        int xTopLeft = (int) topLeft.getX();
        int xTopRight = (int) topRight.getX();  
        int yBottomSensor = ySpriteCenterPlayer;
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
            ySpriteCenterPlayer = pixelyL + 64;
            if(jumpState == JumpState.STATE_JUMP_UP) {
                jumpState = JumpState.STATE_JUMP_DOWN;    
            }            
        }
        else if(pixelyR > pixelyL) {
            ySpriteCenterPlayer = pixelyR + 64;
            if(jumpState == JumpState.STATE_JUMP_UP) {
                jumpState = JumpState.STATE_JUMP_DOWN;    
            }
        } 
        else if(pixelyR == pixelyL && pixelyR != -90000) {
            ySpriteCenterPlayer = pixelyL + 64;
            if(jumpState == JumpState.STATE_JUMP_UP) {
                jumpState = JumpState.STATE_JUMP_DOWN;    
            }
        }
    }
    
    /**Sets the player's ledge state depending on his position compared to the {@code Ground} tiles he 
     * is interacting with (the state determines if the Ledge animation is played and what actions
     * he can do)
     * 
     * @param highLeft {@code Ground} tile that {@code bottomLeft} sensor is currently interacting with.
     * @param highRight {@code Ground} tile that {@code bottomLeft} sensor is currently interacting with.
     */
    private void getLedgeGround(Ground highLeft, Ground highRight) {
        //Get's Sonic's ledges
        if(grounded && xSpeed == 0 && angle == 0 && bLCollide && !bRCollide && bRDistanceFromRect >= 48) {
            if(highLeft != null) {
                if(xDrawCenterPlayer >= highLeft.getXRef()+64+4 && animation.getDirection() == 1) {
                    ledgeState = LedgeState.STATE_RIGHTLEDGE;    
                }
            }
            
        }
        else if(grounded && xSpeed == 0 && angle == 0 && !bLCollide && bRCollide && bLDistanceFromRect >= 48) {
            if(highRight != null) {
                if(xDrawCenterPlayer <= highRight.getXRef()-4 && animation.getDirection() == 0) {
                    ledgeState = LedgeState.STATE_LEFTLEDGE;    
                }
            }
        } 
    }
       
    /**Sets the player's ledge state depending on his position compared to the Rectangle {@code intersectBox}
     * of the gameObject that he is interacting with.
     * 
     * @param intersectBox the intersectBox of the gameObject that the player is interacting with.
     */
    public void getLedgeGameObject(Rectangle intersectBox){
        if(grounded && xSpeed == 0 && angle == 0 && bLCollide && !bRCollide && bRDistanceFromRect >= 48) {
            if(xDrawCenterPlayer >= (int) (intersectBox.getX()+intersectBox.getWidth()+4) && animation.getDirection() == 1) {
                ledgeState = LedgeState.STATE_RIGHTLEDGE;    
            }
            
        }
        else if(grounded && xSpeed == 0 && angle == 0 && !bLCollide && bRCollide && bLDistanceFromRect >= 48) {
            if(xDrawCenterPlayer <= intersectBox.getX()-4 && animation.getDirection() == 0) {
                ledgeState = LedgeState.STATE_LEFTLEDGE;    
            }
        }
    }
    
    public void leftPressNotGround() {
        //Note!- The player can turn instantly in air, but NOT on the ground
        if(!grounded) {
            if(animation.getDirection() == 1) {
                animation.setDirection(0);
            }            
            if(xSpeed > -5) {               
                xSpeed -= AIR;    
            }
            else {
                xSpeed = -5;
            }
        }
    }
    
    /**Gets the correct {@code Ground} tile that Sonic's sensor is interacting with/colliding with- determines what variables
     * to use when comparing the heights of each tile that Sonic is interacting with (one for each bottom {@code sensor}).
     * @param yBottomSensor the bottom of the sensor that being checked
     * @param sensor the rectangle of the sensor.
     * @return the correct {@code Ground} tile that {@code sensor} is interacting with.
     */
    private Ground getCorrectTile(int yBottomSensor, Rectangle sensor) {
        int xBottomSensor = (int) sensor.getX();     
        /*if(sensor == bottomLeft) {
            g2.setColor(Color.MAGENTA);
            g2.fillRect(xBottomSensor,ySpriteCenterPlayer,1,80);
            g2.fillRect(xBottomSensor,ySpriteCenterPlayer+143,1,1);
        }
        if(sensor == bottomRight) {
            g2.setColor(Color.RED);
            g2.fillRect(xBottomSensor,ySpriteCenterPlayer,1,80);
            g2.fillRect(xBottomSensor,ySpriteCenterPlayer+143,1,1);
        }*/
        int xBottomIndex = xBottomSensor/64;
        int yBottomIndex = yBottomSensor/64;       
        Ground intersect = currentRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex);
        if(intersect != null) {
            if(sensor == bottomLeft || sensor == bottomRight) {
                Ground intersectUp = currentRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex-1);
                Ground higher = Ground.compareSCTile(xBottomSensor, intersect, intersectUp, animation.getLayer());
                return higher;    
            }
            else if(sensor == topLeft || sensor == topRight) {
                Ground intersectDown = currentRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex+1);
                Ground higher = Ground.compareSCTile(xBottomSensor, intersect, intersectDown, animation.getLayer());
                return higher;    
            }
        }
        else {
            if(sensor == bottomLeft || sensor == bottomRight) {
                Ground intersectDown = currentRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex+1);
                Ground higher = Ground.compareSCTile(xBottomSensor, intersect, intersectDown, animation.getLayer());
                return higher;    
            }
            else if(sensor == topLeft || sensor == topRight) {
                Ground intersectUp = currentRoom.getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex-1);
                Ground higher = Ground.compareSCTile(xBottomSensor, intersect, intersectUp, animation.getLayer());
                return higher;    
            }
        }
        return null;
    }
    
    /**Sets Sonic's y position variables and angle variables based on the correct {@code Ground} tile he
     * is interacting with.
     * @param heightIndex the index of the Rectangle in the {@code pixelBoxes} arrayList that Sonic's 
     * sensor is interacting with (depends on the {@code Ground} tile).
     * @param pixelHeight the correct y position that Sonic needs to be set to (depends on the {@code Ground} tile).
     * @param yBottomSensor the y position at the bottom of Sonic's bottom sensors.
     * @param highest the correct {@code Ground} tile that Sonic is interacting with.
     */
    private void setPlayerGroundStat(int heightIndex, int pixelHeight, int yBottomSensor, Ground highest) {     
        if(highest != null) {
            Rectangle collideCheck = highest.getPixelBox(heightIndex);
            if(highest.getAngle() != 0 && yBottomSensor >= (int) collideCheck.getY()-32) {
                yLastGround = pixelHeight;
                ySpriteCenterPlayer = pixelHeight - 76; 
                angle = highest.getAngle();    
            }
            else if(highest.getAngle() == 0 && yBottomSensor >= (int) collideCheck.getY()) {
                yLastGround = pixelHeight;
                ySpriteCenterPlayer = pixelHeight - 76; 
                angle = highest.getAngle();    
            }
        }        
    }
    
    public void leftPressGround() {
        if(grounded) {
            /*Added to fix bug where if Sonic was against a wall, tapped the another direction and then pushed toward the wall,
            he would be pushing the wrong way (this is because Sonic can't change his direction since his groundSpeed would be 0)*/     
            if(animation.getDirection() == 1 && mLCollide && grounded && groundSpeed == 0) {
                animation.setDirection(0);
            }
            else if(animation.getDirection() == 1 && !mLCollide && groundSpeed < 0) {//If Sonic's groundSpeed is less than 0, set his direction to 0 (left), this makes it so the player has to stop 
            //completely (skid) before changing direction (can't change direction immediately)
                animation.setDirection(0);
            }          
            if(groundSpeed > 0) {
                if(duckState == DuckState.STATE_NODUCK) {
                    groundSpeed -= DECELERATION;    
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
    
    public void rightPressNotGround() {
        //Note!- The player can turn instantly in air, but NOT on the ground
        if(!grounded) {
            if(animation.getDirection() == 0) {
                animation.setDirection(1);
            }
            if(xSpeed < 5) {               
                xSpeed += AIR;    
            }  
            else {
                xSpeed = 5;
            }
        }
    }
    
    public void zPressJump(int heightOfJump) {
        if(grounded && jumpState == JumpState.STATE_NOJUMP && springState == SpringState.STATE_NOSPRING) {
            jumpState = JumpState.STATE_JUMP_UP;
        }
        /*If the player is jumping up and is below the maximum jump height, continue jumping, if he is, stop moving 
        up (even if z is still being pressed)*/
        if(jumpState == JumpState.STATE_JUMP_UP && ySpriteCenterPlayer < yLastGround-heightOfJump) {
            if(ySpeed < -4) {
                ySpeed = -4;
            }
            jumpState = JumpState.STATE_JUMP_DOWN;
        }
        else if(jumpState == JumpState.STATE_JUMP_UP && ySpriteCenterPlayer > yLastGround-(heightOfJump+10)) {
            if(angle == 0) {           
                ySpeed = -JUMP;    
            }      
            else {
                //I have to fix when the Player jumps on a slope (or not)- depends on what I want to do
                ySpeed = -JUMP;    
            }
            grounded = false;
        }
    }
    
    public void zReleasedJump() {
        if(ySpeed < -4) {
            ySpeed = -4;
        }
        if(jumpState == JumpState.STATE_JUMP_UP) {
            jumpState = JumpState.STATE_JUMP_DOWN;
        }
    }       
    
    public int getXDrawCenterPlayer() {
        return xDrawCenterPlayer;
    }
    
    public void setXDrawCenterPlayer(int temp) {
        xDrawCenterPlayer = temp;
    }
    
    public int getYDrawCenterSonic() {
        return yDrawCenterPlayer;
    }
    
    public void setYSpriteCenterPlayer(int temp) {
        ySpriteCenterPlayer = temp;
    }
    
    public int getYSpriteCenterPlayer() {
        return yDrawCenterPlayer;
    }
    
    public void setSpringLock(boolean temp) {
        springLock = temp;
    }
    
    public double getAngle() {
        return angle;
    }
    
    public Rectangle getBottomLeft() {
        return bottomLeft;
    }
    
    public Rectangle getBottomRight() {
        return bottomRight;
    }
    
    public Rectangle getMiddleLeft() {
        return middleLeft;
    }
    
    public Rectangle getMiddleRight() {
        return middleRight;
    }
    
    public void setIntersectBox(Rectangle temp) {
        intersectBox = temp;
    }
    
    public Rectangle getIntersectBox() {
        return intersectBox;
    }
    
    public boolean getMLCollide() {
        return mLCollide;
    }
    
    public boolean getMRCollide() {
        return mRCollide;
    }
    
    public boolean isGrounded() {
        return grounded;
    }
    
    public boolean isInputAllowed() {
        return allowInput;
    }
    
    public void setAllowInput(boolean temp) {
        allowInput = temp;
    }
    
    public void setBLDistanceFromRect(int temp) {
        bLDistanceFromRect = temp;
    }
    
    public void setBRDistanceFromRect(int temp) {
        bRDistanceFromRect = temp;
    }
    
    public double getXSpeed() {
        return xSpeed;
    }
    
    public void setXSpeed(double speed) {
        xSpeed = speed;
    }
    
    public double getYSpeed() {
        return ySpeed;
    }
    
    public void setYSpeed(double temp) {
        ySpeed = temp;
    }
    
    public double getGroundSpeed() {
        return groundSpeed;
    }
    
    public void setGroundSpeed(double speed) {
        groundSpeed = speed;
    }
    
    public void setFriction(double temp) {
        friction = temp;
    }
    
    public double getFriction() {
        return friction;
    }
    
    public void setSlope(double temp) {
        slope = temp;
    }
    
    public double getSlope() {
        return slope;
    }
    
    public double getAcceleration() {
        return ACCELERATION;
    }
    
    public double getDeceleration() {
        return DECELERATION;
    }
    
    public double getTopSpeed() {
        return TOPSPEED;
    }
    
    public PlayerCharacter getPartner() {
        return partner;
    }
    
    public Room getCurrentRoom() {
        return currentRoom;
    }
    
    public AnimationControl getAnimationControl() {
        return animation;
    }
    
    public PlayerManager getManager() {
        return manager;
    }
    
    public void setJumpState(JumpState state) {
        jumpState = state;
    }
    
    public JumpState getJumpState() {
        return jumpState;
    }
    
    public void setDuckState(DuckState temp) {
        duckState = temp;
    }
    
    public DuckState getDuckState() {
        return duckState;
    }
          
    public SpringState getSpringState() {
        return springState;
    }
    
    public LedgeState getLedgeState() {
        return ledgeState;
    }
    
    public void setSpringState(SpringState spring) {
        springState = spring;
    }
    
    public OWPosition getOWPosition() {
        return owPosition;
    }
    
    public void drawDebug(Graphics2D g2) {
        g2.setColor(Color.MAGENTA);
        g2.setFont(Launcher.debugStat);
        g2.drawString("DEBUG MENU",600,25);        
        //Variables that have to do with Sonic's x and y position, checking ground, x and y speed, etc:
        g2.drawString("xDrawCenterSonic: "+xDrawCenterPlayer,75,75);
        g2.drawString("yDrawCenterSonic: "+yDrawCenterPlayer,75,100);
        g2.drawString("ySpriteCenterSonic: "+ySpriteCenterPlayer,75,125);
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
        //Variables that have to do with Sonic's animations:
        g2.setColor(Color.GREEN);
        g2.drawString("angle: "+angle, 400, 75);
        g2.drawString("direction: "+animation.getDirection(), 400, 100);
        g2.setColor(Color.ORANGE);
        g2.drawString("Sonic's Ledge State: "+ledgeState, 75, 600);
        g2.drawString("Sonic's Jump State: "+jumpState, 75, 625);
        g2.drawString("Sonic's Duck State: "+duckState, 75, 650);  
        g2.drawString("Sonic's Spring State: "+springState, 75, 700); 
        g2.drawString("Sonic's FrontPosition State: "+frontPosition, 75, 725);
        /*g2.setColor(Color.BLUE);    
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
        g2.setColor(Color.PINK);
        g2.fill(intersectBox);
        g2.setColor(Color.CYAN);
        g2.fillRect(xDrawCenterPlayer, ySpriteCenterPlayer, 1, 1);*/
    }
       
    public enum LedgeState {
        STATE_NOLEDGE,
        STATE_LEFTLEDGE,
        STATE_RIGHTLEDGE,
    };
    
    public enum JumpState {
        STATE_NOJUMP,
        STATE_JUMP_UP,
        STATE_JUMP_DOWN,
    };
    
    public enum DuckState {
        STATE_NODUCK,
        STATE_DUCK
        //STATE_ROLL
    };
    
    public enum SpringState {
        STATE_NOSPRING,
        STATE_SPRING
    };
    
    public enum FrontPosition {
        STATE_LEFT,
        STATE_RIGHT,
        STATE_MIDDLE
    };
}
