/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.player.mario;

import game.GameLoop;
import game.animation.Animation;
import game.overworld.Room;
import game.player.AnimationControl;
import game.player.BasicOWA;
import game.player.PlayerManager;
import game.player.PlayerManager.OWPosition;
import game.input.PlayerInput;
import game.overworld.Ground;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

/**
 *
 * @author GeoSonicDash
 */
public class MarioOWA extends BasicOWA {
    
    private int xHammerHitbox;
    private int yHammerHitbox;
    private int sideHammerHitbox;
    private int waitTimer;
    private int hammerTimer;
    
    private Rectangle hammerHitbox;
    
    private HammerState hammerState;
    
    public MarioOWA(PlayerManager man, PlayerManager.OWPosition owPosition, AnimationControl aniC, int x, int y) {
        super(man, owPosition, aniC, x, y);
        this.waitTimer = 0;
        this.hammerState = HammerState.STATE_NOHAMMER;
        this.xHammerHitbox = -128;
        this.yHammerHitbox = -128;
        this.sideHammerHitbox = 48;
    }
    
    public void mainMethod(Room cR) {
        super.resetDrawPosLedgeState();
        super.updateRoomPartner(cR);
        super.resetLeftRightMiddleIntersect();
        super.resetDrawPosLedgeState();
        super.updateSensors();
        hammerHitbox = new Rectangle(xHammerHitbox, yHammerHitbox, sideHammerHitbox, sideHammerHitbox);
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
            performHammer();
        }
        if(super.isInputAllowed() && PlayerInput.checkIsPressed(KeyEvent.VK_Z)) {//Jump has to be calculated before gravity is added to ySpeed
            zPress();
        }
        else if(super.isInputAllowed() && PlayerInput.checkIsJustReleased(KeyEvent.VK_Z)) {
            zReleased();
        }  
        /*This resets the duckState if the player jumps (this is why is placed here right after the code for jumping)
        */
        if(super.getJumpState() != JumpState.STATE_NOJUMP) {     
            super.setDuckState(DuckState.STATE_NODUCK);
        }
        //resets the duckState if the down key is released and Mario is ducking
        if(super.isInputAllowed() && PlayerInput.checkIsJustReleased(KeyEvent.VK_DOWN) && super.getDuckState() == DuckState.STATE_DUCK) {
            super.setDuckState(DuckState.STATE_NODUCK);           
        }
        super.springLock();
        super.setNormalFrictionSlope();             
        if(!super.isGrounded()) {
            super.controlGravity();
        }
        super.sideCheck();
        super.resetSpeedTopCollideCheck();
        if(super.getMLCollide() || super.getMRCollide()) {
            super.setXSpeed(0);
            super.setGroundSpeed(0);
        }
        if(super.isGrounded()) {  
            if(super.isInputAllowed() && super.getXSpeed() == 0 && super.getGroundSpeed() == 0 && super.getDuckState() == DuckState.STATE_NODUCK 
                    && PlayerInput.checkIsPressed(KeyEvent.VK_ENTER)) {
                super.getManager().getOWMenuManager().setVisible(true);
            }
            /*Remember- the SLOPE variable controls how Mario interacts with slopes (slides on them)
            and changes depending if Mario is running/etc
            BUG- If a sensor interacts with a slope and another one interacts on another one (this occurs
            when Mario is standing on one tile with two opposing slopes on either side of him,), Mario will
            be pushed in the wrong direction by a tile*/     
            super.getGSpeedAfterJumpLand();
            double groundSp = super.getGroundSpeed();
            if(!PlayerInput.checkIsPressed(KeyEvent.VK_LEFT) && !PlayerInput.checkIsPressed(KeyEvent.VK_RIGHT) && super.isGrounded()) {                
                super.setGroundSpeed(groundSp-(Math.min(Math.abs(groundSp), super.getFriction()) * Math.signum(groundSp)));    
            }  
            super.updateGroundSpeed();
            super.updateXSpeed();
            super.updateYSpeed();
        }
        super.bottomTopCheck();
        //This has to go at the end since the collision booleans are reset in the bottom and side collision methods
        for(int i = 0; i < super.getCurrentRoom().getGameObjectArrayList().size(); i++) {
            super.getCurrentRoom().getGameObjectArrayList().get(i).interactWithMario(this);
        }
        super.updatePlayerPosition();
        changeAnimation();
    }
    
    public void changeAnimation() {
        /*Watch out for state conflicts- if one is conflicting with another one, it causes Mario to freeze on his animation/
        or perform his animation wrong*/
        if(super.getJumpState() == BasicOWA.JumpState.STATE_NOJUMP && super.getSpringState() == BasicOWA.SpringState.STATE_NOSPRING 
                && super.getLedgeState() == BasicOWA.LedgeState.STATE_NOLEDGE && super.getDuckState() == BasicOWA.DuckState.STATE_NODUCK && hammerState == HammerState.STATE_NOHAMMER) {
            if(super.isInputAllowed() && super.isGrounded() && super.getGroundSpeed() == 0 && super.getAngle() == 0 && !PlayerInput.checkIsPressed(KeyEvent.VK_LEFT) && !PlayerInput.checkIsPressed(KeyEvent.VK_RIGHT)) {
                idleAnimation();
            }
            else {//This resets waitTimer if the player starts to walk/run
                waitTimer = 0;
            }         
            if(Math.abs(super.getGroundSpeed()) > 0.5 && Math.abs(super.getGroundSpeed()) < 6 && (!super.getMLCollide() || !super.getMRCollide())) {
                if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_MARIO_WALK) {
                    super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_MARIO_WALK);    
                }
            }
            else if(Math.abs(super.getGroundSpeed()) >= 6) {//Controls when Sonic's running animation plays
                if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_MARIO_RUN) {
                    super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_MARIO_RUN);    
                }
            }
            if(PlayerInput.checkIsPressed(KeyEvent.VK_LEFT) && super.getMLCollide() && super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_MARIO_PUSH) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_MARIO_PUSH);
            }
            else if(PlayerInput.checkIsPressed(KeyEvent.VK_RIGHT) && super.getMRCollide() && super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_MARIO_PUSH) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_MARIO_PUSH);
            }
        }
        else {//This resets the waitTimer if the player is ducking/rolling/on ledge/etc
            waitTimer = 0;
        }
        if(super.getLedgeState() == BasicOWA.LedgeState.STATE_LEFTLEDGE && hammerState == HammerState.STATE_NOHAMMER && super.getDuckState() == BasicOWA.DuckState.STATE_NODUCK) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_MARIO_PANIC) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_MARIO_PANIC);    
            }
        }
        else if(super.getLedgeState() == BasicOWA.LedgeState.STATE_RIGHTLEDGE && hammerState == HammerState.STATE_NOHAMMER && super.getDuckState() == BasicOWA.DuckState.STATE_NODUCK) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_MARIO_PANIC) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_MARIO_PANIC);    
            }
        }
        if(super.getJumpState() == BasicOWA.JumpState.STATE_JUMP_UP) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_MARIO_JUMPUP) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_MARIO_JUMPUP);   
            }
        }
        else if(super.getJumpState() == BasicOWA.JumpState.STATE_JUMP_DOWN) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_MARIO_JUMPDOWN) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_MARIO_JUMPDOWN);   
            }
        }
        if(super.getDuckState() == BasicOWA.DuckState.STATE_DUCK) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_MARIO_DUCK) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_MARIO_DUCK);   
            }
        }
        if(super.getSpringState() == BasicOWA.SpringState.STATE_SPRING && !super.isGrounded()) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_MARIO_JUMPUP) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_MARIO_JUMPUP);   
            }
        }
        if(hammerState == HammerState.STATE_HAMMER) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_MARIO_HAMMER) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_MARIO_HAMMER);   
            } 
        }
    }
    
    private void idleAnimation() {
        waitTimer++;
        if(waitTimer < 988) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_MARIO_STAND) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_MARIO_STAND);               
            }                   
        }
        else if(waitTimer >= 998 && waitTimer < 3000) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_MARIO_THINK) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_MARIO_THINK);               
            }
        }
        else if(waitTimer >= 3000 && waitTimer < 3001) {
            if(super.getAnimationControl().getAnimationNumber() != Animation.AnimationName.ANIMATION_MARIO_THINK) {
                super.getAnimationControl().setAnimation(Animation.AnimationName.ANIMATION_MARIO_THINK);               
            }
        }
        else if(waitTimer >= 3000) {
            waitTimer = 3000;
        }
    }   
    
    private void performHammer() {
        int tempMulti = super.getAnimationControl().getDirection();
        if(super.isGrounded() && super.getAngle() == 0 && super.getDuckState() == DuckState.STATE_NODUCK 
        && hammerState == HammerState.STATE_NOHAMMER && PlayerInput.checkIsPressedOnce(KeyEvent.VK_X)) {
            //Make sure the hammer's hitBox moves in the right direction (left of Mario or right of him)
            super.setGroundSpeed(0);
            super.setXSpeed(0);
            if(super.getAnimationControl().getDirection() == -1) {
                xHammerHitbox = super.getXDrawCenterPlayer() + 64;
            }
            else {
                xHammerHitbox = super.getXDrawCenterPlayer() - 124;
            }
            hammerState = HammerState.STATE_HAMMER;           
            yHammerHitbox = super.getYSpriteCenterPlayer()-32;
        }
        else if(hammerState != HammerState.STATE_NOHAMMER) {
            hammerTimer++;
        }
        if(hammerState == HammerState.STATE_HAMMER && hammerTimer < 49) {           
            //Moves the hammer's hitbox along with the hammer's arc
            if(hammerTimer%1 == 0 && hammerTimer < 15) {
                xHammerHitbox += (tempMulti * 4);
                yHammerHitbox -= 8;
            }
            else if(hammerTimer%1 == 0 && hammerTimer >= 15 && hammerTimer < 30) {
                xHammerHitbox += (tempMulti * 4);
            }
            else if(hammerTimer%1 == 0 && hammerTimer >= 30 && hammerTimer < 49) {
                xHammerHitbox += (tempMulti * 4);
                yHammerHitbox += 8;
            }     
            int xBottomIndex = 0;
            if(super.getAnimationControl().getDirection() == -1) {
                xBottomIndex = xHammerHitbox/64;
            }
            else {
                xBottomIndex = (xHammerHitbox+sideHammerHitbox)/64;
            }
            int yBottomIndex = (yHammerHitbox+sideHammerHitbox)/64;
            Ground intersect = super.getCurrentRoom().getGroundGridArrayList().get(xBottomIndex).get(yBottomIndex);
            if(intersect != null) {
                //Checks if the hammer is colliding with a Ground tile, if it is set state to STATE_PAUSEHAMMER
                intersect.hammerCollide(this);
            }            
        }
        else if(hammerState == HammerState.STATE_PAUSEHAMMER) {
            //Freezes the animation frame         
            super.getAnimationControl().setFreezeCurrentAnimation(true);    
            if(hammerTimer < 50) {
                hammerTimer = 50; 
            }              
        }
        if(hammerTimer >= 125) {
            /*After the hammer is finished with its swing (or was stopped) and a bit of time is passed, reset hammer vars,
            allowing Mario to swing hammer again*/
            super.getAnimationControl().setFreezeCurrentAnimation(false);
            xHammerHitbox = -128;
            yHammerHitbox = -128;
            hammerTimer = 0;
            hammerState = HammerState.STATE_NOHAMMER;
        }
    }
    
    public boolean checkValidHammerHit(Rectangle intersect) {
        return hammerTimer == 48 && hammerHitbox.intersects(intersect);
    }
    
    public void leftPress() {
        //Note!- Mario can turn instantly in air, but NOT on the ground
        if(!super.isGrounded()) {
            super.leftPressNotGround();
        }
        else if(super.isGrounded() && hammerState == HammerState.STATE_NOHAMMER) {
            /*Added to fix bug where if Mario was against a wall, tapped the another direction and then pushed toward the wall,
            he would be pushing the wrong way (this is because Sonic can't change his direction since his groundSpeed would be 0)*/     
            if(super.getAnimationControl().getDirection() == 1 && super.getMLCollide() && super.getGroundSpeed() == 0) {
                super.getAnimationControl().setDirection(-1);
            }
            else if(super.getAnimationControl().getDirection() == 1 && !super.getMLCollide() && super.getGroundSpeed() < 0) {//If Mario's groundSpeed is less than 0, set his direction to 0 (left), this makes it so the player has to stop 
            //completely (skid) before changing direction (can't change direction immediately)
                super.getAnimationControl().setDirection(-1);
            }   
            //Controls appropiate acceleration/decaceleration depending on Mario's direction.
            double groundSp = super.getGroundSpeed();
            if(super.getGroundSpeed() > 0) {
                if(super.getDuckState() == DuckState.STATE_NODUCK) {
                    super.setGroundSpeed(groundSp-super.getDeceleration());
                }
                if(super.getGroundSpeed() <= 0) {
                    super.setGroundSpeed(-0.5);
                }
            }
            else if(super.getGroundSpeed() > -super.getTopSpeed() && super.getDuckState() == DuckState.STATE_NODUCK) {
                super.setGroundSpeed(groundSp-super.getAcceleration());
                if(super.getGroundSpeed() <= -super.getTopSpeed()) {
                    super.setGroundSpeed(-super.getTopSpeed());
                }
            }
        }
    }
    
    public void rightPress() {
        //Note!- Mario can turn instantly in air, but NOT on the ground
        if(!super.isGrounded()) {
            super.rightPressNotGround();
        }
        else if(super.isGrounded() && hammerState == HammerState.STATE_NOHAMMER) {
            /*Added to fix bug where if Sonic was against a wall, tapped the another direction and then pushed toward the wall,
            he would be pushing the wrong way (this is because Mario can't change his direction since his groundSpeed would be 0)
            */
            if(super.getAnimationControl().getDirection() == -1 && super.getMRCollide() && super.getGroundSpeed() == 0) {
                super.getAnimationControl().setDirection(1);
            }
            else if(super.getAnimationControl().getDirection() == -1 && !super.getMRCollide() && super.getGroundSpeed() > 0) {//If Mario's groundSpeed is less than 1, set his direction to 1 (right), this makes it so the player has to stop 
            //(completely skid) before changing direction (can't change direction immediately)
                super.getAnimationControl().setDirection(1);
            }
            //Controls appropiate acceleration/decaceleration depending on Mario's direction.
            double groundSp = super.getGroundSpeed();
            if(super.getGroundSpeed() < 0) {
                if(super.getDuckState() == DuckState.STATE_NODUCK) {
                    super.setGroundSpeed(groundSp+super.getDeceleration());   
                }
                if(super.getGroundSpeed() >= 0) {
                    super.setGroundSpeed(0.5);
                }
            }
            else if(super.getGroundSpeed() < super.getTopSpeed() && super.getDuckState() == DuckState.STATE_NODUCK) {
                super.setGroundSpeed(groundSp+super.getAcceleration());
                if(super.getGroundSpeed() >= super.getTopSpeed()) {
                    super.setGroundSpeed(super.getTopSpeed());
                }
            }    
        }
    }
    
    public void downPress() {
        if(super.isGrounded()) {
            if(super.getAngle() == 0 && Math.abs(super.getGroundSpeed()) < 1) {
                super.setDuckState(DuckState.STATE_DUCK);
            }
        }
    }
    
    @Override
    public void drawDebug(Graphics2D g2) {
        g2.drawString("Mario's hammerState: "+hammerState, 75, 675);
        g2.drawString("Mario's hammerTimer: "+hammerTimer, 600, 325);
        g2.fill(hammerHitbox);
        if(GameLoop.getDebug() && super.getOWPosition() == OWPosition.OWPOSITION_FRONT) {
           super.drawDebug(g2);         
        }      
    }
    
    public void zPress() {
        if(hammerState == HammerState.STATE_NOHAMMER) {
            super.zPressJump(300); 
        }               
    }
    
    public void zReleased() {
        super.zReleasedJump();
    }  
    
    public HammerState getHammerState() {
        return hammerState;
    }
    
    public void setHammerState(HammerState temp) {
        hammerState = temp;
    }
    
    public Rectangle getHammerHitbox() {
        return hammerHitbox;
    }
    
    public enum HammerState { 
        STATE_NOHAMMER,
        STATE_PAUSEHAMMER,
        STATE_HAMMER
    }
}
