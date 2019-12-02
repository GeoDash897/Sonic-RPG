/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author GeoSonicDash
 */
public class Music {
    private static Clip testAreaTheme;
    public static void standard() {
        //loadTestAreaTheme();
    }
    public static void loadTestAreaTheme() {
        try {
            File musicPath = new File("src\\game\\resources\\music files\\Test.wav");//creates File object which has path of musicLocation
            AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);//creating an object that is responsible 
            //for bringing the music from the music file to our Java application (gets that from the musicPath object)
            testAreaTheme = AudioSystem.getClip();//use clip class to play music, gets the audiostream from the AudioInput object
            testAreaTheme.open(audioInput);//opens music
            /*FloatControl gainControl = (FloatControl) era1Theme.getControl(FloatControl.Type.MASTER_GAIN); //allows for changing the volume of the music
            gainControl.setValue(60.0f); // Reduce volume by 30 decibels.﻿*/
            Thread.sleep(300);//stops thread (that is created by the clip class)
            //from killing its self (and the music) as it immediately starts        
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void playTestAreaTheme(int start, int setPosition) {
        if(start == 0) {
            testAreaTheme.stop();
        }
        else if(start == 1) {
            if(setPosition == 0) {
                testAreaTheme.setMicrosecondPosition(0);
            }
            testAreaTheme.start();
            testAreaTheme.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
}
