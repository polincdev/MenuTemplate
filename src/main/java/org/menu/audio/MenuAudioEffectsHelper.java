package main.java.org.menu.audio;

 

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
 
import main.java.org.menu.settings.GameSettings;
 
 
public class MenuAudioEffectsHelper {
 
    AssetManager assetManager;
  
    
    public MenuAudioEffectsHelper(AssetManager assetManager )
    {
        this.assetManager=assetManager;
        
       
    }
  
    
 AudioNode effectSilentClick;
 AudioNode effectAccept;
 AudioNode effectOptionSwitch;
 AudioNode effectEnabled;
 AudioNode effectDisabled;
 
    
   
    public void initAudio() {
    //
    effectSilentClick = new AudioNode(assetManager, "Sounds/Menu/Effect/silentClick.ogg", DataType.Buffer);
    effectAccept = new AudioNode(assetManager, "Sounds/Menu/Effect/accepted.ogg", DataType.Buffer);
    effectOptionSwitch = new AudioNode(assetManager, "Sounds/Menu/Effect/optionSwitch.ogg", DataType.Buffer);
    effectEnabled = new AudioNode(assetManager, "Sounds/Menu/Effect/enabled.ogg", DataType.Buffer);
    effectDisabled = new AudioNode(assetManager, "Sounds/Menu/Effect/disabled.ogg", DataType.Buffer);
        
     effectSilentClick.setPositional(false);
     effectSilentClick.setLooping(false);
     effectSilentClick.setVolume(1);
     
     effectAccept.setPositional(false);
     effectAccept.setLooping(false);
     effectAccept.setVolume(1);
     
     effectOptionSwitch.setPositional(false);
     effectOptionSwitch.setLooping(false);
     effectOptionSwitch.setVolume(1);
     
     effectEnabled.setPositional(false);
     effectEnabled.setLooping(false);
     effectEnabled.setVolume(1);
     
     effectDisabled.setPositional(false);
     effectDisabled.setLooping(false);
     effectDisabled.setVolume(1);
  }
  
 public void playSilendClick()
  {
     playAudioNode( effectSilentClick );
  }
 public void playAccept()
  {
     playAudioNode( effectAccept );
  }
 public void playOptionSwitch()
  {
   playAudioNode( effectOptionSwitch );
  }
 public void playEnabled()
  {
     playAudioNode(effectEnabled );
  }
 public void playDisabled()
  {
   playAudioNode(effectDisabled );
  }
 
  
 public void playAudioNode(AudioNode audioNode)
  {
    audioNode.setVolume((float)GameSettings.optionAudioVolume/(float)100);
    audioNode.playInstance();
  } 
}
