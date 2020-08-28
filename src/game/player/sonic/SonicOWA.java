/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.player.sonic;

import game.GameLoop;
import game.animation.Animation;
import game.overworld.Room;
import game.player.AnimationControl;
import game.player.BasicOWA;
import game.player.PlayerManager;
import game.player.PlayerManager.OWPosition;
import game.input.PlayerInput;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

/**
 *
 * @author GeoSonicDash
 */
public class SonicOWA extends BasicOWA {
    
    private int waitTimer;
    private double spindashRev;
    
    private SpindashState spindashState;
    private RollState rollState;
    
    private static double ROLLDECELERATION = 0.025;
    private static double ROLLFRICTION = 0.0234375;
    private static double SLOPEROLLUP = 0.078125;
    private static double SLOPEROLLDOWN = 0.3125;
    
    public SonicOWA(PlayerManager man, PlayerManager.OWPosition owPosition, AnimationControl aniC, int x, int y) {
        super(man, owPosition, aniC, x, y);
        this.waitTimer = 0;
        this.rollState = RollState.STATE_NOROLL;
        this.spindashState = SpindashState.STATE_NOSPINDASH;
    }
    
    public void mainMethod(Room cR) {
        super.resetDrawPosLedgeState();
        super.updateRoomPartner(cR);
        super.resetLeftRightMiddleIntersect();
        super.resetDrawPosLedgeState();
        super.updateSensors();
        if(rollState == RollState.STATE_ROLL) {
            super.setIntersectBox(new Rectangle(super.getXDrawCenterPlayer()-30, super.getYSpriteCenterPlayer()-20, 60, 80));
        }
        if(super.getOWPosition() == OWPosition.OWPOSITION_FRONT && super.isInputAllowed() && PlayerInput.checkIsPressed(KeyEvent.VK_LEFT)) {
            leftPress();
        }
        else if(super.getOWPosition() == OWPosition.OWPOSITION_FRONT && super.isInputAllowed() && PlayerInput.checkIsPressed(KeyEvent.VK_RIGHT)) {
            rightPress();
        }
        if(super.getOWPosition() == OWPosition.OWPOSITION_FRONT && super.isInputAllowed() && PlayerInput.checkIsPressed(KeyEvent.VK_UP)) {
            super.setYSpeed(-3);
        }
        else if(super.isInputAllowed() && PlayerInput.checkIsPressed(KeyEvent.VK_DOWN)) {
            downPress();
        }
        if(super.isInputAllowed()) {
           performSpindash(); 
        }        
        if(super.isInputAllowed() && PlayerInput.checkIsPressed(KeyEvent.VK_Z)) {//Jump has to be calculated before gravity is added to ySpeed
            zPress();
        }
        else if(super.isInputAllowed() && PlayerInput.checkIsJustReleased(KeyEvent.VK_Z)) {
            zReleased();
        }  
        /*This resets the duckState if the player jumps (this is why is placed here right after the code for jumping),
        and resets spindashRev to 0 (so Sonic won't boost again on ground when he ducks), also rollState is reset to stop 
        Sonic from rolling 
        */
        if(super.getJumpState() != JumpState.STATE_NOJUMP) {     
            super.setDuckState(DuckState.STATE_NODUCK);
            rollState = RollState.STATE_NOROLL;
            spindashRev = 0;
        }
        //resets the rollState if groundSpeed is less than abs(0.5) (prevents turning bug from Genesis games)
        if(super.isInputAllowed() && !PlayerInput.checkIsPressed(KeyEvent.VK_DOWN) && Math.abs(super.getGroundSpeed()) < 0.5
                && super.getAngle() == 0) {
            rollState = RollState.STATE_NOROLL;
        }
        //resets the duckState if the down key is released and Sonic is ducking
        if(super.isInputAllowed() && PlayerInput.checkIsJustReleased(KeyEvent.VK_DOWN) && super.getDuckState() == DuckState.STATE_DUCK) {
            super.setDuckState(DuckState.STATE_NODUCK);           
        }
        super.springLock();
        if(rollState == RollState.STATE_ROLL) {
            super.setFriction(ROLLFRICTION);         
            if(Math.signum(super.getGroundSpeed()) == Math.signum(Math.sin(super.getAngle()))) {//Checks if Sonic is rolling uphill
                super.setSlope(SLOPEROLLUP);
            }
            else if(Math.signum(super.getGroundSpeed()) != Math.signum(Math.sin(super.getAngle()))) {//Checks if Sonic is rolling downhill
                super.setSlope(SLOPEROLLDOWN);
            }            
        }
        else {
            super.setNormalFrictionSlope(); 
        }             
        if(!super.isGrounded()) {
            super.controlGravity();
        }
        super.sideCheck();
        super.resetSpeedTopCollideCheck();
        if(super.getMLCollide() || super.getMRCollide()) {
            super.setXSpeed(0);
            super.setGroundSpeed(0);
            rollState = RollState.STATE_NOROLL;
        }
        if(super.isGrounded()) {  
            if(super.isInputAllowed() && super.getXSpeed() == 0 && super.getGroundSpeed() == 0 && super.getDuckState() == DuckState.STATE_NODUCK 
                    && PlayerInput.checkIsPressed(KeyEvent.VK_ENTER)) {
                super.getManager().getOWMenuManager().setVisible(true);
            }
            /*Remember- the SLOPE variable controls how Sonic interacts with slopes (slides on them)
            and changes depending if Sonic is rolling/running/etc
            BUG- If a sensor interacts with a slope and another one interacts on another one (this occurs
            when Sonic is standing on one tile with two opposing slopes on either side of him,), Sonic will
            be pushed in the wrong direction by a tile*/     
            super.getGSpeedAfterJumpLand();
            double groundSp = super.getGroundSpeed();
            if(!PlayerInput.checkIsPressed(KeyEvent.VK_LEFT) && !PlayerInput.checkIsPressed(KeyEvent.VK_RIGHT) && super.isGrounded()) {                
                super.setGroundSpeed(groundSp-(Math.min(Math.abs(groundSp), super.getFriction()) * Math.signum(groundSp)));    
            }  
            else if(super.getDuckState() == DuckState.STATE_NODUCK && rollState == RollState.STATE_ROLL) {
                /*If you are rolling and pressing in the direction of your motion- friction is still in effect
                and Sonic should still slow down*/              
                if(super.getGroundSpeed() < 0 && PlayerInput.checkIsPressed(KeyEvent.VK_LEFT)) {
                    super.setGroundSpeed(groundSp-(Math.min(Math.abs(groundSp), super.getFriction()) * Math.signum(groundSp))); 
                }
                else if(super.getGroundSpeed() > 0 && PlayerInput.checkIsPressed(KeyEvent.VK_RIGHT)) {
                    super.setGroundSpeed(groundSp-(Math.min(Math.abs(groundSp), super.getFriction()) * Math.signum(groundSp)));
                }
            }
            super.updateGroundSpeed();
            super.updateXSpeed();
            super.updateYSpeed();
        }
        super.bottomTopCheck();
        //This has to go at the end since the collision booleans are reset in the bottom and side collision methods
        for(int i = 0; i < super.getCurrentRoom().getGameObjectArrayList().size(); i++) {
            super.getCurrentRoom().getGameObjectArrayList().get(i).interactWithSonic(this);
        }
        super.updatePlayerPosition();
        changeAnimation();
    }
    
    public void changeAnimation() {
        /*Watch out for state conflicts- if one is conflicting with another one, it causes Sonic to freeze on his animation/
        or perform his animation wrong*/
        if(super.getJumpState() == BasicOWA.JumpState.STATE_NOJUMP && super.getSpringState() == BasicOWA.SpringState.STATE_NOSPRING 
                && super.getLedgeState() == BasicOWA.LedgeState.STATE_NOLEDGE && super.getDuckState() == BasicOWA.DuckState.STATE_NODUCK &&
                rollState == RollState.STATE_NOROLL && spindashState == SpindashState.STATE_NOSPINDASH) {
            if(super.isInputAllowed() && super.isGrounded() && super.getGroundSpeed() == 0 && super.getAngle() == 0 && !PlayerInput.checkIsPressed(KeyEvent.VK_LEFT) && !PlayerInput.checkIsPressed(KeyEvent.VK_RIGHT)) {
                idleAnimation();
            }
            else {//This resets waitTimer if the player starts to walk/run
                waitTimer = 0;
            }         
            if(Math.abs(super.getGroundSpeed()) > 0.5 && Math.abs(super.getGroundSpeed()) < 6 && (!super.getMLCollide() || !super.getMRCollide())) {
                if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_WALK) {
                    super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_SONIC_WALK);    
                }
            }
            else if(Math.abs(super.getGroundSpeed()) >= 6) {//Controls when Sonic's running animation plays
                if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_RUN) {
                    super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_SONIC_RUN);    
                }
            }
            if(PlayerInput.checkIsPressed(KeyEvent.VK_LEFT) && super.getMLCollide() && super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_PUSH) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_SONIC_PUSH);
            }
            else if(PlayerInput.checkIsPressed(KeyEvent.VK_RIGHT) && super.getMRCollide() && super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_PUSH) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_SONIC_PUSH);
            }
        }
        else {//This resets the waitTimer if the player is ducking/rolling/on ledge/spindashing/etc
            waitTimer = 0;
        }
        if(super.getLedgeState() == BasicOWA.LedgeState.STATE_LEFTLEDGE && super.getDuckState() == BasicOWA.DuckState.STATE_NODUCK 
                && rollState  == RollState.STATE_NOROLL) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_TRIPA) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_SONIC_TRIPA);    
            }
        }
        else if(super.getLedgeState() == BasicOWA.LedgeState.STATE_RIGHTLEDGE && super.getDuckState() == BasicOWA.DuckState.STATE_NODUCK 
                && rollState  == RollState.STATE_NOROLL) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_TRIPA) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_SONIC_TRIPA);    
            }
        }
        if(super.getJumpState() == BasicOWA.JumpState.STATE_JUMP_UP || super.getJumpState() == BasicOWA.JumpState.STATE_JUMP_DOWN) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_JUMP) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_SONIC_JUMP);   
            }
        }
        if(super.getDuckState() == BasicOWA.DuckState.STATE_DUCK && spindashState == SpindashState.STATE_NOSPINDASH) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_DUCK) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_SONIC_DUCK);   
            }
        }
        else if(super.getDuckState() == BasicOWA.DuckState.STATE_NODUCK && rollState  == RollState.STATE_ROLL) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_JUMP) {               
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_SONIC_JUMP);   
            }
        }
        if(spindashState == SpindashState.STATE_SPINDASH) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_SPINDASH) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_SONIC_SPINDASH);   
            }
        }
        if(super.getSpringState() == BasicOWA.SpringState.STATE_SPRING && !super.isGrounded()) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_SPRING) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_SONIC_SPRING);   
            }
        }
    }
    
    private void idleAnimation() {
        waitTimer++;
        if(waitTimer < 988) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_STAND) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_SONIC_STAND);               
            }                   
        }
        else if(waitTimer >= 998 && waitTimer < 3000) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_WAIT) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_SONIC_WAIT);               
            }
        }
        else if(waitTimer >= 3000 && waitTimer < 3001) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_SONIC_BORED) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_SONIC_BORED);               
            }
        }
        else if(waitTimer >= 3000) {
            waitTimer = 3000;
        }
    }
    
    private void performSpindash() {
        //If Sonic is ducking and the Z Key is pressed, start spindashing (need state in order to change animation)
        if(super.isGrounded() && super.getDuckState() == DuckState.STATE_DUCK && PlayerInput.checkIsPressedOnce(KeyEvent.VK_Z)
            && spindashState == SpindashState.STATE_NOSPINDASH) {
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
            if(super.getAnimationControl().getDirection() == 0) {
                super.setGroundSpeed(-10 - ((int)(spindashRev) / 2));//Negative since Sonic would be facing left
            }
            else if(super.getAnimationControl().getDirection() == 1) {
                super.setGroundSpeed(10 + ((int)(spindashRev) / 2));//Positive since Sonic would be facing right   
            }                     
            spindashState = SpindashState.STATE_NOSPINDASH;
            super.setDuckState(DuckState.STATE_NODUCK);
            rollState = RollState.STATE_ROLL;                                                   
        }
    }
    
    public void leftPress() {
        //Note!- Sonic can turn instantly in air, but NOT on the ground
        if(!super.isGrounded()) {
            super.leftPressNotGround();
        }
        else if(super.isGrounded()) {
            /*Added to fix bug where if Sonic was against a wall, tapped the another direction and then pushed toward the wall,
            he would be pushing the wrong way (this is because Sonic can't change his direction since his groundSpeed would be 0)*/     
            if(super.getAnimationControl().getDirection() == 1 && super.getMLCollide() && super.getGroundSpeed() == 0) {
                super.getAnimationControl().setDirection(0);
            }
            else if(super.getAnimationControl().getDirection() == 1 && !super.getMLCollide() && super.getGroundSpeed() < 0) {//If Sonic's groundSpeed is less than 0, set his direction to 0 (left), this makes it so the player has to stop 
            //completely (skid) before changing direction (can't change direction immediately)
                super.getAnimationControl().setDirection(0);
            }   
            //Controls appropiate acceleration/decaceleration depending on Sonic's direction.
            double groundSp = super.getGroundSpeed();
            if(super.getGroundSpeed() > 0) {
                if(super.getDuckState() == DuckState.STATE_NODUCK) {
                    super.setGroundSpeed(groundSp-super.getDeceleration());
                }
                else if(rollState == RollState.STATE_ROLL) {
                    super.setGroundSpeed(groundSp-(ROLLDECELERATION+super.getFriction()));
                }
                if(super.getGroundSpeed() <= 0) {
                    super.setGroundSpeed(-0.5);
                }
            }
            else if(super.getGroundSpeed() > -super.getTopSpeed() && super.getDuckState() == DuckState.STATE_NODUCK
                    && rollState == RollState.STATE_NOROLL) {
                super.setGroundSpeed(groundSp-super.getAcceleration());
                if(super.getGroundSpeed() <= -super.getTopSpeed()) {
                    super.setGroundSpeed(-super.getTopSpeed());
                }
            }
        }
    }
    
    public void rightPress() {
        //Note!- Sonic can turn instantly in air, but NOT on the ground
        if(!super.isGrounded()) {
            super.rightPressNotGround();
        }
        else if(super.isGrounded()) {
            /*Added to fix bug where if Sonic was against a wall, tapped the another direction and then pushed toward the wall,
            he would be pushing the wrong way (this is because Sonic can't change his direction since his groundSpeed would be 0)
            */
            if(super.getAnimationControl().getDirection() == 0 && super.getMRCollide() && super.getGroundSpeed() == 0) {
                super.getAnimationControl().setDirection(1);
            }
            else if(super.getAnimationControl().getDirection() == 0 && !super.getMRCollide() && super.getGroundSpeed() > 0) {//If Sonic's groundSpeed is less than 1, set his direction to 1 (right), this makes it so the player has to stop 
            //(completely skid) before changing direction (can't change direction immediately)
                super.getAnimationControl().setDirection(1);
            }
            //Controls appropiate acceleration/decaceleration depending on Sonic's direction.
            double groundSp = super.getGroundSpeed();
            if(super.getGroundSpeed() < 0) {
                if(super.getDuckState() == DuckState.STATE_NODUCK) {
                    super.setGroundSpeed(groundSp+super.getDeceleration());   
                }
                else if(rollState == RollState.STATE_ROLL) {
                    super.setGroundSpeed(groundSp+(ROLLDECELERATION+super.getFriction()));
                }
                if(super.getGroundSpeed() >= 0) {
                    super.setGroundSpeed(0.5);
                }
            }
            else if(super.getGroundSpeed() < super.getTopSpeed() && super.getDuckState() == DuckState.STATE_NODUCK
                    && rollState == RollState.STATE_NOROLL) {
                super.setGroundSpeed(groundSp+super.getAcceleration());
                if(super.getGroundSpeed() >= super.getTopSpeed()) {
                    super.setGroundSpeed(super.getTopSpeed());
                }
            }    
        }
    }
    
    public void downPress() {
        if(super.isGrounded()) {
            if(super.getAngle() == 0) {
                if(Math.abs(super.getGroundSpeed()) < 1) {
                    super.setDuckState(DuckState.STATE_DUCK);
                    rollState = RollState.STATE_NOROLL;
                }
                else if(Math.abs(super.getGroundSpeed()) >= 1) {
                    rollState = RollState.STATE_ROLL;
                }    
            }
            else {
                rollState = RollState.STATE_ROLL;
            }
        }
    }
    
    public void zPress() {
        if(spindashState == SpindashState.STATE_NOSPINDASH) {
           super.zPressJump(250); 
        }       
    }
    
    public void zReleased() {
        super.zReleasedJump();
    }
    
    public void setRollState(RollState temp) {
        rollState = temp;
    }
    
    public RollState getRollState() {
        return rollState;
    }
    
    @Override
    public void drawDebug(Graphics2D g2) {
        if(GameLoop.getDebug() && super.getOWPosition() == OWPosition.OWPOSITION_FRONT) {
           super.drawDebug(g2);
           g2.drawString("Sonic's Spindash State: "+spindashState, 75, 675);  
           g2.drawString("rollState :"+rollState, 600, 325);
        }      
    }
       
    public enum SpindashState {
        STATE_NOSPINDASH,
        STATE_SPINDASH
    };
    
    public enum RollState {
        STATE_NOROLL,
        STATE_ROLL
    }
}
