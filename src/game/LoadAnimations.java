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

/**This class controls loading animations and their variables from gameInfo text files.
 *
 * @author GeoSonicDash
 */
public class LoadAnimations {
    /**
     * 
     * @param refName the name of the game element that the animation Map is being created 
     * for.
     * @return Map of animations for the specific game element.
     */
    public static Map<AnimationName, Animation> getAnimationMap(String refName) {
        String section = "";
        Map<AnimationName, Animation> temp = new HashMap<AnimationName, Animation>();
        try {
            File file = new File("src/game/resources/gameInfo/"+refName+".txt");
            if(!file.exists()) {
                file.createNewFile();
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String currentLine = br.readLine();
            while(currentLine != null) {
                /*This checks to see if the BufferedReader is in the Animation section of the game element's text
                file (where the information for the animations are located)*/
                if(section.equals("ANIMATION")) {
                    String[] line = currentLine.split(" ");
                    Animation currentAnimation = getAnimation(currentLine);
                    temp.put(AnimationName.valueOf(line[0]), currentAnimation);
                }
                else if(currentLine.equals("ANIMATION")) {
                    section = currentLine;
                }
                currentLine = br.readLine();
            }
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        return temp;
    }
    
    /**Creates {@code Animation} from specific line of text in game element's text file.
     * 
     * @param currentLine the current line being read by the BufferedReader
     * @return {@code Animation} created from the current line being read.
     */
    private static Animation getAnimation(String currentLine) {
        /*This cuts out and stores the name of the Image being used for the specific Animation
        Example: Sonic Stand_1, the String filePath would be Sonic Stand*/
        String cutToFirstQuote = currentLine.substring(currentLine.indexOf("\"")+1);
        String filePath = cutToFirstQuote.substring(0, cutToFirstQuote.indexOf("\""));
        String cutOutFilePath = currentLine.replaceFirst("\""+filePath+"\" ", "");
        String[] lineSplit = cutOutFilePath.split(" ");
        Animation animationTemp = new Animation(AnimationName.valueOf(lineSplit[0]), filePath, Integer.valueOf(lineSplit[1]), Integer.valueOf(lineSplit[2]),
        Integer.valueOf(lineSplit[3]), Integer.valueOf(lineSplit[4]));
        return animationTemp;
    }
}
