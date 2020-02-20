package org.menu.audio;
 
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
  
import java.util.Random;
import org.menu.settings.GameSettings;

 
public class MusicHelper {
 
    AssetManager assetManager;
     Random rand=new Random();
   long sfxPlayTime=0;
   long sfxCooloffPeriod=1000*60;
 
    public MusicHelper(AssetManager assetManager )
    {
        this.assetManager=assetManager;
         
        
    }
  
    
 AudioNode songMainMenu;
 boolean  songMainMenuFlag=false;
 
AudioNode[] sfxs=new AudioNode[10];
   
  public void initMainMenuAudio() {
    //
     songMainMenu = new AudioNode(assetManager, "Sounds/Menu/Music/mainTheme.ogg", DataType.Buffer);
     songMainMenu.setPositional(false);
     songMainMenu.setLooping(true);
     songMainMenu.setVolume(1);
       
  }
    
 public void playMainMenu()
  {
     if(songMainMenu!=null && songMainMenuFlag) 
        stopMainMenu();
         
     //
     playAudioNode( songMainMenu );
     songMainMenuFlag=true;
      
  }
    
 public void stopMainMenu()
  {
     songMainMenu.stop();
     songMainMenuFlag=false;
  }

 
 public boolean isMainMenuPlaying()
  {
      return songMainMenuFlag;
   }
 
 public void resetVolume( )
  {
      songMainMenu.setVolume((float)GameSettings.optionMusicVolume/(float)100); 
  }
  
  
 public void playAudioNode(AudioNode audioNode)
  {
     if(audioNode==null)
          return;
  
    audioNode.setVolume((float)GameSettings.optionMusicVolume/(float)100);
    try
       {
        audioNode.play();
       }
    catch(Exception ex)
       {
         ex.printStackTrace();
       } 
  }
  
 public void playAudioNodeOnce(AudioNode audioNode)
  {
     
     if(audioNode==null)
          return;
 
      audioNode.play();
    
  }
 
}
