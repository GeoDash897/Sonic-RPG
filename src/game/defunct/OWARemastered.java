/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.defunct;

import game.GameLoop;
import game.Launcher;
import game.animation.Animation;
import game.input.PlayerInput;
import game.overworld.Ground;
import game.overworld.Room;
import game.player.AnimationControl;
import game.player.PlayerCharacter;
import game.player.PlayerManager;
import game.player.PlayerManager.OWPosition;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

/**Controls Sonic's movement, physics, and actions in the overworld.
 * 
 * @author GeoSonicDash
 */
public class OWARemastered {

    private int xDrawCenterSonic;
    private int yDrawCenterSonic;
    private int ySpriteCenterSonic;
    private int yLastGround;
    private int springTimer;
    private boolean springLock;
    private double xSpeed;
    private double ySpeed;
    private double groundSpeed;
    private double slope;
    private double angle;
    private double friction;
    private double spindashRev;
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
    private int waitTimer;
    
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
    private PlayerManager manager;
    
    private DuckState duckState;
    private JumpState jumpState;
    private LedgeState ledgeState;
    private SpindashState spindashState;
    private SpringState springState;
    private FrontPosition frontPosition;
    
    public OWARemastered(PlayerManager man, OWPosition owPosition, AnimationControl aniC, int x, int y) {
        xDrawCenterSonic = x;
        ySpriteCenterSonic = y;
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
        allowInput = true;
        springTimer = 0;
        bLDistanceFromRect = 0;
        bRDistanceFromRect = 0;
        waitTimer = 0;
        ledgeState = LedgeState.STATE_NOLEDGE;
        jumpState = JumpState.STATE_NOJUMP;
        spindashState = SpindashState.STATE_NOSPINDASH;
        springState = SpringState.STATE_NOSPRING;
        middleLeftIntersect = null;
        middleRightIntersect = null;
        this.owPosition = owPosition;
        this.frontPosition = FrontPosition.STATE_MIDDLE;
        partner = null;
        manager = man;
        animation = aniC;
    }
    
    public void mainMethod(Room cR){
        //Get current Room that Sonic is currently in
        currentRoom = cR;
        if(manager.getCharacterList().size() > 1 && owPosition == OWPosition.OWPOSITION_FRONT) {
            partner = manager.getCharacter(1);
        }
        else if(manager.getCharacterList().size() > 1 && owPosition == OWPosition.OWPOSITION_BACK) {
            partner = manager.getCharacter(0);
        }
        yDrawCenterSonic = ySpriteCenterSonic + 16;
        ledgeState = LedgeState.STATE_NOLEDGE;
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
        /*Creates the correct sensors of Sonic depending on his state(being on the ground,
        being in the air, colliding with a slope, etc*/
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
        //Gets the correct size of interactBox
        if(jumpState != JumpState.STATE_NOJUMP || duckState != DuckState.STATE_NODUCK) {
            intersectBox = new Rectangle(xDrawCenterSonic-30, ySpriteCenterSonic-20, 60, 80);
        }
        else {
            intersectBox = new Rectangle(xDrawCenterSonic-29, ySpriteCenterSonic-70, 60, 140);
        }
        if(owPosition == OWPosition.OWPOSITION_FRONT && allowInput && PlayerInput.checkIsPressed(KeyEvent.VK_LEFT)) {
            leftPress();
        }
        else if(owPosition == OWPosition.OWPOSITION_FRONT && allowInput && PlayerInput.checkIsPressed(KeyEvent.VK_RIGHT)) {
            rightPress();
        }
        if(owPosition == OWPosition.OWPOSITION_FRONT && allowInput && PlayerInput.checkIsPressed(KeyEvent.VK_UP)) {
            ySpeed = -3;
        }
        else if(allowInput && PlayerInput.checkIsPressed(KeyEvent.VK_DOWN)) {
            downPress();
        }
        if(allowInput) {
           performSpindash(); 
        }        
        if(allowInput && PlayerInput.checkIsPressed(KeyEvent.VK_Z)) {//Jump has to be calculated before gravity is added to ySpeed
            zPress();
        }
        else if(allowInput && PlayerInput.checkIsJustReleased(KeyEvent.VK_Z)) {
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
        if(allowInput && !PlayerInput.checkIsPressed(KeyEvent.VK_DOWN) && Math.abs(groundSpeed) < 0.5 && angle == 0) {
            duckState = DuckState.STATE_NODUCK;
        }
        //resets the duckState if the down key is released and Sonic is ducking
        if(allowInput && PlayerInput.checkIsJustReleased(KeyEvent.VK_DOWN) && duckState == DuckState.STATE_DUCK && spindashState == SpindashState.STATE_NOSPINDASH) {
            duckState = DuckState.STATE_NODUCK;
            
        }
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
        //Sets correct value for friction (for rolling and not rolling)
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
            if(allowInput && xSpeed == 0 && groundSpeed == 0 && duckState == DuckState.STATE_NODUCK && PlayerInput.checkIsPressed(KeyEvent.VK_ENTER)) {
                manager.getOWMenuManager().setVisible(true);
            }
            if(springState == SpringState.STATE_SPRING) {
                springState = SpringState.STATE_NOSPRING;
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_STAND);
            }
            /*I'm getting the correct groundSpeed when sonic first is grounded (if grounded == true and 
            jump == STATE_JUMP_DOWN, this means that sonic just landed after a jump*/
            if(jumpState == JumpState.STATE_JUMP_DOWN && angle < 45) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_STAND);
                groundSpeed = xSpeed;
            }
            else if(jumpState == JumpState.STATE_JUMP_DOWN && angle == 45) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_STAND);
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
            if(!PlayerInput.checkIsPressed(KeyEvent.VK_LEFT) && !PlayerInput.checkIsPressed(KeyEvent.VK_RIGHT) && grounded) {
                groundSpeed -= Math.min(Math.abs(groundSpeed), friction) * Math.signum(groundSpeed);    
            }  
            else if(duckState == DuckState.STATE_ROLL) {
                /*If you are rolling and pressing in the direction of your motion- friction is still in effect
                and Sonic should still slow down*/              
                if(groundSpeed < 0 && PlayerInput.checkIsPressed(KeyEvent.VK_LEFT)) {
                    groundSpeed -= Math.min(Math.abs(groundSpeed), friction) * Math.signum(groundSpeed);   
                }
                else if(groundSpeed > 0 && PlayerInput.checkIsPressed(KeyEvent.VK_RIGHT)) {
                    groundSpeed -= Math.min(Math.abs(groundSpeed), friction) * Math.signum(groundSpeed);   
                }
            }
            /*NOTE! If you want to change groundSpeed, you have to put the code before here! (since this is 
            where groundSpeed effects xSpeed*/
            xSpeed = groundSpeed*Math.cos(angle);
            ySpeed = groundSpeed*-Math.sin(angle);
        }
        if(owPosition == OWPosition.OWPOSITION_BACK) {
            if(partner.getAnimationControl().getDirection() == 0 
                    && xDrawCenterSonic > partner.getOWARemastered().getXCenterPlayer()+100) {
                if(animation.getDirection() == 1) {
                    animation.setDirection(0);
                }                
                frontPosition = FrontPosition.STATE_LEFT;
                xSpeed = partner.getOWARemastered().getXSpeed();
                getCorrectPartnerGroundSpeed(); 
            } 
            else if(partner.getAnimationControl().getDirection() == 1 
                    && xDrawCenterSonic < partner.getOWARemastered().getXCenterPlayer()-100) {
                if(animation.getDirection() == 0) {
                    animation.setDirection(1);
                }
                frontPosition = FrontPosition.STATE_RIGHT;
                xSpeed = partner.getOWARemastered().getXSpeed();
                getCorrectPartnerGroundSpeed(); 
            }
            else {
                frontPosition = FrontPosition.STATE_MIDDLE;
                groundSpeed = 0;
                xSpeed = 0;
            }
        }
        bottomTopCheck();//This needs to go after gravity is calculated (since it affects ySpeed)!
        
        //This has to go at the end since the collision booleans are reset in the bottom and side collision methods
        for(int i = 0; i < currentRoom.getGameObjectArrayList().size(); i++) {
            //currentRoom.getGameObjectArrayList().get(i).interactWithPlayer(this);
        }    
        if(partner != null && owPosition == OWPosition.OWPOSITION_FRONT) {
            if(xSpeed < 0 && xDrawCenterSonic < partner.getOWARemastered().getXCenterPlayer()-144) {
                groundSpeed = 0;
                xSpeed = 0;
            }
            else if(xSpeed > 0 && xDrawCenterSonic > partner.getOWARemastered().getXCenterPlayer()+144) {
                groundSpeed = 0;
                xSpeed = 0;
            }
        }
        xDrawCenterSonic += (int) xSpeed; 
        ySpriteCenterSonic += (int) ySpeed;
        if(owPosition == OWPosition.OWPOSITION_FRONT) {
            changeFrontAnimation(); 
        }
        else {
            changeBackAnimation();
        }     
    }
    
    private void getCorrectPartnerGroundSpeed() {
        if(partner.getOWARemastered().isGrounded()) {
            groundSpeed = partner.getOWARemastered().getGroundSpeed();
        }
        else {
            groundSpeed = partner.getOWARemastered().getXSpeed()/(Math.cos(angle));
        }
    }
    
    /**Controls when sideCollision methods are run.
     * 
     */
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
    
    /**Controls when bottomCollision and topCollision methods are run.
     * 
     */
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
        getLedgeGround(highLeft, highRight);        
    } 
    /**Sets Sonic's ledge state depending on his position compared to the {@code Ground} tiles he 
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
    
    /**Sets Sonic's ledge state depending on his position compared to the Rectangle {@code intersectBox}
     * of the gameObject that he is interacting with.
     * 
     * @param intersectBox the intersectBox of the gameObject that Sonic is interacting with.
     */
    public void getLedgeGameObject(Rectangle intersectBox){
        if(grounded && xSpeed == 0 && angle == 0 && bLCollide && !bRCollide && bRDistanceFromRect >= 48) {
            if(xDrawCenterSonic >= (int) (intersectBox.getX()+intersectBox.getWidth()+4) && animation.getDirection() == 1) {
                ledgeState = LedgeState.STATE_RIGHTLEDGE;    
            }
            
        }
        else if(grounded && xSpeed == 0 && angle == 0 && !bLCollide && bRCollide && bLDistanceFromRect >= 48) {
            if(xDrawCenterSonic <= intersectBox.getX()-4 && animation.getDirection() == 0) {
                ledgeState = LedgeState.STATE_LEFTLEDGE;    
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
    
    /**Sets Sonic's y position variables and collision variables based on the gameObject he is
     * interacting with.
     * @param sensor the {@code Rectangle} sensor that is currently being checked.
     * @param collideCheck the intersectBox of the gameObject that Sonic is currently interacting with.
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
            ySpriteCenterSonic = (int) (collideCheck.getY() - 76);  
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
            if(jumpState == JumpState.STATE_JUMP_UP) {
                jumpState = JumpState.STATE_JUMP_DOWN;    
            }            
        }
        else if(pixelyR > pixelyL) {
            ySpriteCenterSonic = pixelyR + 64;
            if(jumpState == JumpState.STATE_JUMP_UP) {
                jumpState = JumpState.STATE_JUMP_DOWN;    
            }
        } 
        else if(pixelyR == pixelyL && pixelyR != -90000) {
            ySpriteCenterSonic = pixelyL + 64;
            if(jumpState == JumpState.STATE_JUMP_UP) {
                jumpState = JumpState.STATE_JUMP_DOWN;    
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
        xDrawCenterSonic = (int) (collideCheck.getX()+collideCheck.getWidth()+38);
    }
    
    public void setCollideRightStats(Rectangle collideCheck) {
        groundSpeed = 0;
        xSpeed = 0;
        mRCollide = true;   
        xDrawCenterSonic = (int) (collideCheck.getX()-40);
    }
    
    private void performSpindash() {
        //If Sonic is ducking and the Z Key is pressed, start spindashing (need state in order to change animation)
        if(grounded && duckState == DuckState.STATE_DUCK && PlayerInput.checkIsPressedOnce(KeyEvent.VK_Z) && spindashState == SpindashState.STATE_NOSPINDASH) {
            spindashState = SpindashState.STATE_SPINDASH;           
        }
        if(spindashRev > 0.01) {
            spindashRev -= ((spindashRev / 0.125) / 256); //"div" is division ignoring any remainder    
        }           
        else {
            spindashRev = 0;    
        } 
        if(spindashState == SpindashState.STATE_SPINDASH && PlayerInput.checkIsJustReleased(KeyEvent.VK_DOWN)) {    
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
        //Note!- Sonic can turn instantly in air, but NOT on the ground
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
        else if(grounded) {
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
        //Note!- Sonic can turn instantly in air, but NOT on the ground
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
        else if(grounded) {
            /*Added to fix bug where if Sonic was against a wall, tapped the another direction and then pushed toward the wall,
            he would be pushing the wrong way (this is because Sonic can't change his direction since his groundSpeed would be 0)
            */
            if(animation.getDirection() == 0 && mRCollide && groundSpeed == 0) {
                animation.setDirection(1);
            }
            else if(animation.getDirection() == 0 && !mRCollide && groundSpeed > 0) {//If Sonic's groundSpeed is less than 1, set his direction to 1 (right), this makes it so the player has to stop 
            //(completely skid) before changing direction (can't change direction immediately)
                animation.setDirection(1);
            }
            //Controls appropiate acceleration/decaceleration depending on Sonic's direction.
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
        //Change Sonic's duck state depending on groundSpeed (if he is ducking or rolling) 
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
        if(grounded && spindashState == SpindashState.STATE_NOSPINDASH && jumpState == JumpState.STATE_NOJUMP && springState == SpringState.STATE_NOSPRING) {
            jumpState = JumpState.STATE_JUMP_UP;
        }
        /*If Sonic is jumping up and is below the maximum jump height, continue jumping, if he is, stop moving 
        up (even if z is still being pressed)*/
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
                //I have to fix when Sonic jumps on a slope (or not)- depends on what I want to do
                ySpeed = -JUMP;    
            }
            grounded = false;
        }
        if(spindashState == SpindashState.STATE_SPINDASH && PlayerInput.checkIsPressedOnce(KeyEvent.VK_Z)) {
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
    private void changeFrontAnimation() {
        /*Watch out for state conflicts- if one is conflicting with another one, it causes Sonic to freeze on his animation/
        or perform his animation wrong*/
        if(jumpState == JumpState.STATE_NOJUMP && springState == SpringState.STATE_NOSPRING && ledgeState == LedgeState.STATE_NOLEDGE && duckState == DuckState.STATE_NODUCK) {
            if(allowInput && grounded && groundSpeed == 0 && angle == 0 && !PlayerInput.checkIsPressed(KeyEvent.VK_LEFT) && !PlayerInput.checkIsPressed(KeyEvent.VK_RIGHT)) {
                idleAnimation();
            }
            else {//This resets waitTimer if the player starts to walk/run
                waitTimer = 0;
            }         
            if(Math.abs(groundSpeed) > 0.5 && Math.abs(groundSpeed) < 6 && (!mLCollide || !mRCollide)) {
                if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_WALK) {
                    animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_WALK);    
                }
            }
            else if(Math.abs(groundSpeed) >= 6) {//Controls when Sonic's running animation plays
                if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_RUN) {
                    animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_RUN);    
                }
            }
            if(PlayerInput.checkIsPressed(KeyEvent.VK_LEFT) && mLCollide && animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_PUSH) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_PUSH);
            }
            else if(PlayerInput.checkIsPressed(KeyEvent.VK_RIGHT) && mRCollide && animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_PUSH) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_PUSH);
            }
        }
        else {//This resets the waitTimer if the player is ducking/rolling/on ledge/spindashing/etc
            waitTimer = 0;
        }
        if(ledgeState == LedgeState.STATE_LEFTLEDGE && duckState == DuckState.STATE_NODUCK) {
            if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_TRIPA) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_TRIPA);    
            }
        }
        else if(ledgeState == LedgeState.STATE_RIGHTLEDGE && duckState == DuckState.STATE_NODUCK) {
            if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_TRIPA) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_TRIPA);    
            }
        }
        if(jumpState == JumpState.STATE_JUMP_UP || jumpState == JumpState.STATE_JUMP_DOWN) {
            if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_JUMP) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_JUMP);   
            }
        }
        if(duckState == DuckState.STATE_DUCK && spindashState == SpindashState.STATE_NOSPINDASH) {
            if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_DUCK) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_DUCK);   
            }
        }
        else if(duckState == DuckState.STATE_ROLL) {
            if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_JUMP) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_JUMP);   
            }
        }
        if(duckState == DuckState.STATE_DUCK && spindashState == SpindashState.STATE_SPINDASH) {
            if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_SPINDASH) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_SPINDASH);   
            }
        }
        if(springState == SpringState.STATE_SPRING && !grounded) {
            if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_SPRING) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_SPRING);   
            }
        }
    }
    
    private void changeBackAnimation() {
        if(jumpState == JumpState.STATE_NOJUMP && springState == SpringState.STATE_NOSPRING && ledgeState == LedgeState.STATE_NOLEDGE && duckState == DuckState.STATE_NODUCK) {
            if(allowInput && grounded && groundSpeed == 0 && angle == 0 && !PlayerInput.checkIsPressed(KeyEvent.VK_LEFT) && !PlayerInput.checkIsPressed(KeyEvent.VK_RIGHT)) {
               idleAnimation();
            }
            else if(frontPosition == FrontPosition.STATE_MIDDLE) {
                idleAnimation();
            }
            else {//This resets waitTimer if the player starts to walk/run
                waitTimer = 0;
            }         
            if(frontPosition != FrontPosition.STATE_MIDDLE && Math.abs(groundSpeed) > 0.5 && Math.abs(groundSpeed) < 6 && (!mLCollide || !mRCollide)) {
                if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_WALK) {
                    animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_WALK);    
                }
            }
            else if(frontPosition != FrontPosition.STATE_MIDDLE && Math.abs(groundSpeed) >= 6) {//Controls when Sonic's running animation plays
                if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_RUN) {
                    animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_RUN);    
                }
            }
            if(PlayerInput.checkIsPressed(KeyEvent.VK_LEFT) && mLCollide && animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_PUSH) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_PUSH);
            }
            else if(PlayerInput.checkIsPressed(KeyEvent.VK_RIGHT) && mRCollide && animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_PUSH) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_PUSH);
            }
        }
        else {//This resets the waitTimer if the player is ducking/rolling/on ledge/spindashing/etc
            waitTimer = 0;
        }
        if(ledgeState == LedgeState.STATE_LEFTLEDGE && duckState == DuckState.STATE_NODUCK) {
            if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_TRIPA) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_TRIPA);    
            }
        }
        else if(ledgeState == LedgeState.STATE_RIGHTLEDGE && duckState == DuckState.STATE_NODUCK) {
            if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_TRIPA) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_TRIPA);    
            }
        }
        if(jumpState == JumpState.STATE_JUMP_UP || jumpState == JumpState.STATE_JUMP_DOWN) {
            if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_JUMP) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_JUMP);   
            }
        }
        if(duckState == DuckState.STATE_DUCK && spindashState == SpindashState.STATE_NOSPINDASH) {
            if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_DUCK) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_DUCK);   
            }
        }
        else if(duckState == DuckState.STATE_ROLL) {
            if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_JUMP) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_JUMP);   
            }
        }
        if(duckState == DuckState.STATE_DUCK && spindashState == SpindashState.STATE_SPINDASH) {
            if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_SPINDASH) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_SPINDASH);   
            }
        }
        if(springState == SpringState.STATE_SPRING && !grounded) {
            if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_SPRING) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_SPRING);   
            }
        }
    }
    
    private void idleAnimation() {
        waitTimer++;
        if(waitTimer < 988) {
            if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_STAND) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_STAND);               
            }                   
        }
        else if(waitTimer >= 998 && waitTimer < 3000) {
            if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_WAIT) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_WAIT);               
            }
        }
        else if(waitTimer >= 3000 && waitTimer < 3001) {
            if(animation.getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_BORED) {
                animation.setAnimation(Animation.AnimationName.ANIMATION_SONIC_BORED);               
            }
        }
        else if(waitTimer >= 3000) {
            waitTimer = 3000;
        }
    }
    
    public int getXCenterPlayer() {
        return xDrawCenterSonic;
    }
    
    public int getYCenterPlayer() {
        return yDrawCenterSonic;
    }

    public void setSpringLock(boolean temp) {
        springLock = temp;
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
    
    public Rectangle getIntersectBox() {
        return intersectBox;
    }
    
    public JumpState getJumpState() {
        return jumpState;
    }
    
    public DuckState getDuckState() {
        return duckState;
    }
    
    public SpindashState getSpindashState() {
        return spindashState;
    }
    
    public double getGroundSpeed() {
        return groundSpeed;
    }
    
    public void setGroundSpeed(double speed) {
        groundSpeed = speed;
    }
    
    public void setJumpState(JumpState state) {
        jumpState = state;
    }
    
    public void setSpringState(SpringState spring) {
        springState = spring;
    }
    
    public boolean isGrounded() {
        return grounded;
    }
    
    public PlayerCharacter getPartner() {
        return partner;
    }
    
    public AnimationControl getAnimationControl() {
        return animation;
    }
    public boolean getAllowInput() {
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
        
    public void setXDrawCenterSonic(int temp) {
        xDrawCenterSonic = temp;
    }
    
    public void setYSpriteCenterSonic(int temp) {
        ySpriteCenterSonic = temp;
    }   
    
    public OWPosition getOWPosition() {
        return owPosition;
    }
    
    public void drawDebug(Graphics2D g2) {
        if(owPosition == OWPosition.OWPOSITION_FRONT) {
            //g2.fillRect(xDrawCenterSonic, ySpriteCenterSonic-100, 50, 150);
        }
        if(GameLoop.getDebug() && owPosition == OWPosition.OWPOSITION_FRONT) {
            g2.setColor(Color.MAGENTA);
            g2.setFont(Launcher.debugStat);
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
            g2.drawString("direction: "+animation.getDirection(), 400, 100);
            g2.drawString("waitTimer: "+waitTimer, 400, 125);
            g2.setColor(Color.ORANGE);
            g2.drawString("Sonic's Ledge State: "+ledgeState, 75, 600);
            g2.drawString("Sonic's Jump State: "+jumpState, 75, 625);
            g2.drawString("Sonic's Duck State: "+duckState, 75, 650);
            g2.drawString("Sonic's Spindash State: "+spindashState, 75, 675);   
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
            g2.fill(intersectBox);*/
            g2.setColor(Color.CYAN);
            g2.fillRect(xDrawCenterSonic, ySpriteCenterSonic, 1, 1);
        }          
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
        STATE_DUCK,
        STATE_ROLL
    };
    
    public enum SpindashState {
        STATE_NOSPINDASH,
        STATE_SPINDASH
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