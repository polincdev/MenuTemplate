package org.menu.settings;

 
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
 
 
import java.io.IOException;
/**
 *
 * @author xxx
 */
public class SaveObject implements Savable {
    
         
    public static boolean initedI18N=false; 
    
    public void write(JmeExporter ex) throws IOException {
       
        OutputCapsule capsule = ex.getCapsule(this);
        
         //Is inited  wth i18n keys - must be false otherwise isnt saved
        capsule.write(SaveObject.initedI18N, "initedI18N",false);
       
        //Options
         capsule.write(GameSettings.optionMusicVolume, "optionMusicVolume", 50);
         capsule.write(GameSettings.optionAudioVolume, "optionAudioVolume", 50);
         capsule.write(GameSettings.optionFXLevel, "optionFXLevel", 50 );
         capsule.write(GameSettings.language, "language",  GameSettings.LANG_AUTO);
       
        } 

    public void read(JmeImporter im) throws IOException {
        InputCapsule capsule = im.getCapsule(this); 
        SaveObject.initedI18N  =  capsule.readBoolean(  "initedI18N", false ); 
        GameSettings.optionMusicVolume = capsule.readInt(  "optionMusicVolume", 10 );
        GameSettings.optionAudioVolume = capsule.readInt(  "optionAudioVolume", 50 );
        GameSettings.optionFXLevel = capsule.readInt(  "optionFXLevel", 50  );
         GameSettings.language = capsule.readInt(  "language",  GameSettings.LANG_AUTO );
    
    
    }
}
