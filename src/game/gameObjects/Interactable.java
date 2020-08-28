/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.gameObjects;

import game.player.mario.MarioOWA;
import game.player.sonic.SonicOWA;

/**
 *
 * @author GeoSonicDash
 */
public interface Interactable {
    void interactWithSonic(SonicOWA owaS);
    void interactWithMario(MarioOWA owaM);
}
