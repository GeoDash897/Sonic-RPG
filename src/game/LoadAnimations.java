/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import game.animation.Animation;
import game.animation.Animation.AnimationName;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author GeoSonicDash
 */
public class LoadAnimations {
    public static Map<AnimationName, Animation> getAnimationMap(String refName) {
        Map<AnimationName, Animation> temp = new HashMap<AnimationName, Animation>();
        try {
            File file = new File("src/game/resources/animations/"+refName+".txt");
            if(!file.exists()) {
                file.createNewFile();
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String currentLine = br.readLine();
            while(currentLine != null) {
                String[] line = currentLine.split(" ");
                Animation currentAnimation = getAnimation(currentLine);
                temp.put(AnimationName.valueOf(line[0]), currentAnimation);
                currentLine = br.readLine();
            }
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        return temp;
    }
    
    private static Animation getAnimation(String currentLine) {
        String cutToFirstQuote = currentLine.substring(currentLine.indexOf("\"")+1);
        String filePath = cutToFirstQuote.substring(0, cutToFirstQuote.indexOf("\""));
        String cutOutFilePath = currentLine.replaceFirst("\""+filePath+"\" ", "");
        String[] lineSplit = cutOutFilePath.split(" ");
        Animation animationTemp = new Animation(AnimationName.valueOf(lineSplit[0]), filePath, Integer.valueOf(lineSplit[1]), Integer.valueOf(lineSplit[2]),
        Integer.valueOf(lineSplit[3]), Integer.valueOf(lineSplit[4]));
        return animationTemp;
    }
}
