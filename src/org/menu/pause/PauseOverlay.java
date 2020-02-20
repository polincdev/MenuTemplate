 
package org.menu.pause;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.JoystickAxis;
import com.jme3.input.JoystickButton;
import com.jme3.input.KeyInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.BorderLayout;
import com.simsilica.lemur.event.MouseListener;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.Main;
import org.menu.audio.MenuAudioEffectsHelper;


 
public class PauseOverlay  implements ActionListener, MouseListener{
    
       
    private ViewPort viewPort;
    private Node guiNode;
    private Node pauseNode=new Node();
    private AssetManager assetManager;
    private Main app;
    private InputManager inputManager;
    private BlurFilter boxBlurFilter;
    private FilterPostProcessor fpp;
    private PauseListener pauseListener;
     boolean enabled=false;
    MenuAudioEffectsHelper menuAudioEffectsHelper ;
    ArrayList<Button> buttonTab=new ArrayList<Button> ();//Array containing all buttons
  JoystickEventListener joystickEventListener;
  private final ColorRGBA backgroundColor = ColorRGBA.Blue;
  private Trigger up_trigger = new KeyTrigger(KeyInput.KEY_UP);
  private Trigger down_trigger = new KeyTrigger(KeyInput.KEY_DOWN);
  private Trigger space_trigger = new KeyTrigger(KeyInput.KEY_SPACE);
  private Trigger enter_trigger = new KeyTrigger(KeyInput.KEY_RETURN);
  private Trigger escape_trigger = new KeyTrigger(KeyInput.KEY_ESCAPE);
  
  
    int OPTION_RETURN=0;
    int OPTION_QUIT=1;
 
     
    int selectedOption=OPTION_RETURN;
  
    public void init(AppStateManager stateManager, Application app,Node guiNode, FilterPostProcessor fpp, PauseListener pauseListener,MenuAudioEffectsHelper menuAudioEffectsHelper) {
          
        this.app = (Main) app;
        this.viewPort = this.app.getViewPort();
        this.guiNode = this.app.getGuiNode();
        this.assetManager = this.app.getAssetManager();
        this.inputManager=this.app.getInputManager();
        this.pauseListener=pauseListener;
        this.menuAudioEffectsHelper=menuAudioEffectsHelper;
        //Filter
        boxBlurFilter=new BlurFilter();
        boxBlurFilter.setEnabled(false);
        fpp.addFilter(boxBlurFilter);
        //  
        buildPauseMenu();
      }
    
   Button  quitButt;
   Button  retButt;
  
   void buildPauseMenu()
    {
        
        int fontSize=this.viewPort.getCamera().getHeight()/10;
        if(Main.i18n.equals(Main.I18N_RUSSIA))
           fontSize=this.viewPort.getCamera().getHeight()/10;
        else if(Main.i18n.equals(Main.I18N_KOREA))
           fontSize=this.viewPort.getCamera().getHeight()/10;
        else if(Main.i18n.equals(Main.I18N_CHINA))
           fontSize=this.viewPort.getCamera().getHeight()/11;
       else if(Main.i18n.equals(Main.I18N_JAPAN))
           fontSize=this.viewPort.getCamera().getHeight()/10;
        
        Container c = new Container(new BorderLayout());
        c.setPreferredSize(new Vector3f(  app.getCamera().getWidth()/3 ,app.getCamera().getHeight()/3 , 0));
        c.setLocalTranslation(app.getCamera().getWidth()/2-c.getPreferredSize().x/2,  app.getCamera().getHeight()/2+c.getPreferredSize().y/2, 0);
        //
        retButt=new Button(ResourceBundle.getBundle(Main.i18n).getString("GameMenuButtonBack"));
        retButt.setFontSize(fontSize);
        //retButt.setColor(new ColorRGBA(1,1,1,1f));
        retButt.setFocusColor(ColorRGBA.Red);
        retButt.setFocusShadowColor(ColorRGBA.Red);
        retButt.setHighlightColor(ColorRGBA.Red);
        retButt.setHighlightShadowColor(ColorRGBA.Red);
        retButt.setTextHAlignment(HAlignment.Center);
        retButt.setTextVAlignment(VAlignment.Center);
        c.addChild(retButt, BorderLayout.Position.North);
         retButt.setEnabled(true);
      //Put button in array
        buttonTab.add(retButt);   
        //
        quitButt=new Button(ResourceBundle.getBundle(Main.i18n).getString("GameMenuButtonQuit"));
        quitButt.setFontSize(fontSize);
        //quitButt.setColor(new ColorRGBA(1,1,1,1f));
        quitButt.setFocusColor(ColorRGBA.Red);
        quitButt.setFocusShadowColor(ColorRGBA.Red);
        quitButt.setHighlightColor(ColorRGBA.Red);
        quitButt.setHighlightShadowColor(ColorRGBA.Red);
        quitButt.setTextHAlignment(HAlignment.Center);
        quitButt.setTextVAlignment(VAlignment.Center);
        c.addChild(quitButt, BorderLayout.Position.South);
        //
        pauseNode.attachChild(c);
        //Put button in array
        buttonTab.add(quitButt);   
    
    }
   
  public boolean isEnabled()
  {
      return enabled;
  }
  
   public void enableMenu()
   {
        guiNode.attachChild(pauseNode);
        boxBlurFilter.setEnabled(true);
        pauseListener.onPause();
        enabled=true;
        attachInput();
   }
   
   public void disableMenu()
   {
      
      boxBlurFilter.setEnabled(false);
      guiNode.detachChild(pauseNode);
      pauseListener.onUnpause();
      enabled=false;
      dettachInput();
   }
 

    @Override
    public void mouseButtonEvent(MouseButtonEvent mbe, Spatial sptl, Spatial sptl1)
          {
                
            if(sptl==null || mbe.isPressed())
            return;
  
            if(buttonTab.contains(sptl)) 
            {
               selectedOption=buttonTab.indexOf(sptl);
               //action
              handleEnter();
            }
          
         }
    @Override
    public void mouseEntered(MouseMotionEvent mme, Spatial sptl, Spatial sptl1) {
     }

    @Override
    public void mouseExited(MouseMotionEvent mme, Spatial sptl, Spatial sptl1) {
     }

    @Override
    public void mouseMoved(MouseMotionEvent mme, Spatial sptl, Spatial sptl1) {
     }
   
    
   
 public interface PauseListener 
   {
       public void onReturn();
       public void onQuit();
       public void onPause();
         public void onUnpause();
   }
 
    
  void handleEnter()
   {
      
         //
        if( selectedOption==OPTION_QUIT)
          {
             menuAudioEffectsHelper.playOptionSwitch();
             pauseListener.onQuit();
           }
        else    if( selectedOption==OPTION_RETURN)
          {
           menuAudioEffectsHelper.playOptionSwitch();
           pauseListener.onReturn();
          }
        
     
   }
    void attachInput()
    {
        
    
      inputManager.addMapping("MENUUP", up_trigger);
     inputManager.addListener(this, new String[]{"MENUUP"});
     inputManager.addMapping("MENUDOWN", down_trigger);
     inputManager.addListener(this, new String[]{"MENUDOWN"});
     inputManager.addMapping("MENUSPACE", space_trigger);
     inputManager.addListener(this, new String[]{"MENUSPACE"});
     inputManager.addMapping("MENUENTER", enter_trigger);
     inputManager.addListener(this, new String[]{"MENUENTER"});
       inputManager.addMapping("MENUESC", escape_trigger);
     inputManager.addListener(this, new String[]{"MENUESC"});
   
          // this way.
     for(int a=0;a<buttonTab.size();a++)
       buttonTab.get(a).addMouseListener(this);
     
       joystickEventListener =new  JoystickEventListener();
       inputManager.addRawInputListener( joystickEventListener );   
    }
      void dettachInput()
    {
         inputManager.removeListener(this);
       
         inputManager.deleteMapping("MENUUP");
         inputManager.deleteMapping("MENUDOWN");
         inputManager.deleteMapping("MENUSPACE");
         inputManager.deleteMapping("MENUENTER");
         inputManager.deleteMapping("MENUESC");
       
      
         for(int a=0;a<buttonTab.size();a++)
            buttonTab.get(a).removeMouseListener(this); 
         
         inputManager.removeRawInputListener( joystickEventListener );  
    }
   
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
      
      if(!isPressed)
      return;
     
      
      if (name.equals("MENUUP"))  
         {
              handleUp();
         }
      else  if (name.equals("MENUDOWN"))  
         {
         handleDown();
                  
         }
   
       else  if(name.equals("MENUENTER") || name.equals("MENUSPACE") )
         {
            handleEnter();  
         }
       else  if(name.equals("MENUESC") )
           {
               selectedOption=OPTION_RETURN;
               handleEnter();  
           }
    }
       
             
  /**
     *  Easier to watch for all button and axis events with a raw input listener.
     */   
    protected class JoystickEventListener implements RawInputListener {

        public void onJoyAxisEvent(JoyAxisEvent evt) {
        
              
          if(evt.getValue()==1 && evt.getAxis().getName().equals(JoystickAxis.POV_X))
             {
                 
             }
            else   if(evt.getValue()==-1 && evt.getAxis().getName().equals(JoystickAxis.POV_X))
              {
                
               }
            else  if(evt.getValue()==1 && evt.getAxis().getName().equals(JoystickAxis.POV_Y))
             { 
               handleUp();
             }
             else  if(evt.getValue()==-1 && evt.getAxis().getName().equals(JoystickAxis.POV_Y))
             {
               handleDown();  
             }
          else
             {
             }
           
        }

        public void onJoyButtonEvent(JoyButtonEvent evt) {
            if(evt.isPressed())
            if(evt.getButton().getLogicalId().equals(JoystickButton.BUTTON_0) ||
                evt.getButton().getLogicalId().equals(JoystickButton.BUTTON_1) ||
                    evt.getButton().getLogicalId().equals(JoystickButton.BUTTON_2) ||
                     evt.getButton().getLogicalId().equals(JoystickButton.BUTTON_3) ||
                     evt.getButton().getLogicalId().equals(JoystickButton.BUTTON_7) )
              {
                  handleEnter();
              }
           //exit
             else   if(  evt.getButton().getLogicalId().equals(JoystickButton.BUTTON_6)  || evt.getButton().getLogicalId().equals(JoystickButton.BUTTON_8) )  
                    {
                        
                         selectedOption=OPTION_RETURN;
                         handleEnter();  
                     }
        }

        public void beginInput() {}
        public void endInput() {}
        public void onMouseMotionEvent(MouseMotionEvent evt) {}
        public void onMouseButtonEvent(MouseButtonEvent evt) {}
        public void onKeyEvent(KeyInputEvent evt) {}
        public void onTouchEvent(TouchEvent evt) {}        
    }
   
    
    /**
  * Move option up
  */ 
 private void rollUp()
    {
      selectedOption--;
      if(selectedOption<0) 
        selectedOption=buttonTab.size()-1;    
     //Select option
     GuiGlobals.getInstance().requestFocus( buttonTab.get(selectedOption));  
    }
 
 /**
  * Move option down
  */ 
 private void rollDown()
    {
      selectedOption++;
      if(selectedOption>=buttonTab.size()) 
        selectedOption=0;
     //Select option
     GuiGlobals.getInstance().requestFocus( buttonTab.get(selectedOption));
    }
 
 
 void handleUp( )
   {
      menuAudioEffectsHelper.playOptionSwitch();
      rollUp();
      
   }
  void handleDown()
   {
      menuAudioEffectsHelper.playOptionSwitch();
      rollDown();
    } 
}
