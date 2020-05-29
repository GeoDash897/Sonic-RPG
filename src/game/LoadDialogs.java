/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import game.gui.Dialog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author GeoSonicDash
 */
public class LoadDialogs {
    private static int  CHAR_LIMIT;
    /**
     *Consists of a list of speeches.
     *<p> A speech consists of all of the dialogue that a character says (a character
     * is "CHARACTER_NAME" before the double colons).
     */
    private ArrayList<String> speeches;
    /**
     *Consists of a list of dialogs.
     *<p> A dialog is a part of a speech of the character that can fit into one blue box on the
     * screen. 
     */
    private ArrayList<Dialog> dialogs;
    private String refName;
    /**
     * All of the text with a text file- includes all of the "CHARACTER_NAME::"s and "BREAK"s.
     */
    private String chunk;
    public LoadDialogs(String refName) {
        this.chunk = "";
        this.refName = refName;
        this.speeches = new ArrayList<String>();
        this.dialogs = new ArrayList<Dialog>();
        CHAR_LIMIT = 160;
    }
    
    public void clearSpeechesDialogs() {
        if(!speeches.isEmpty()) {
            speeches.clear();
        }
        if(!dialogs.isEmpty()) {
            dialogs.clear();
        }
    }
    
    public ArrayList<Dialog> getDialogChain(String conversation) {       
        getDialogChunk(conversation);        
        spiltChunk();
        for(String temp : speeches) {
            createDialogChain(temp);
        }
        return dialogs;
    }
    
    private void getDialogChunk(String conversation) {
        File file = new File("src/game/resources/dialogs/"+refName+".txt");
        try {
            if(!file.exists()) {
                file.createNewFile();
            }  
            BufferedReader br = new BufferedReader(new FileReader(file));
            String currentLine = br.readLine();
            String section = "";
            boolean endLoop = false;
            while(!endLoop && currentLine != null) {
                if(section.equals(conversation) && !currentLine.equals(conversation+"-END")) {
                    chunk+=currentLine;    
                }
                else if(currentLine.equals(conversation)) {
                    section = conversation;
                }
                else if(currentLine.equals(conversation+"-END")) {
                    endLoop = true;
                }
                currentLine = br.readLine();
            }
            br.close();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void spiltChunk() {
        int colonIndex = chunk.indexOf("::");
        int breakIndex = chunk.indexOf("BREAK");
        while(colonIndex != -1) {
            String speech = chunk.substring(0, breakIndex);            
            chunk = chunk.substring(breakIndex+5);
            colonIndex = chunk.indexOf("::");
            breakIndex = chunk.indexOf("BREAK");
            speeches.add(speech);
        }
    }
     
    private void createDialogChain(String speech) {
        String speakerName = speech.substring(0,speech.indexOf("::"));
        String removedName = speech.substring(speech.indexOf("::")+2);
        int fullLine = removedName.length() / CHAR_LIMIT;
        for(int i = 0; i < fullLine; i++) {
            String dialog = removedName.substring(CHAR_LIMIT*i, CHAR_LIMIT*(i+1));
            dialogs.add(new Dialog(speakerName, dialog));
        } 
        String tailEnd = removedName.substring((CHAR_LIMIT*fullLine));
        dialogs.add(new Dialog(speakerName, tailEnd));
    }
}
