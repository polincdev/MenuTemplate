package org.menu.screens;

import org.menu.settings.Vars;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
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
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.QuadBackgroundComponent;
 import com.simsilica.lemur.event.MouseListener;
import java.util.ArrayList;
import java.util.ResourceBundle;
 import org.Main;
import org.menu.audio.MenuAudioEffectsHelper;
import org.menu.audio.MusicHelper;
 
public class MenuMainScreen extends BaseAppState implements ActionListener, MouseListener {
 
    private ViewPort viewPort;
    private Node rootNode;
    private Node guiNode;
    private AssetManager assetManager;
    private Main app;
    private InputManager inputManager;
    private final Node localRootNode = new Node("Settings Screen RootNode");
    private final Node localGuiNode = new Node("Settings Screen GuiNode");
    private final ColorRGBA backgroundColor = ColorRGBA.Black;
     
    Panel bgImage;
    Panel logoImage;
    Panel graphImage;
   
    private Trigger up_trigger = new KeyTrigger(KeyInput.KEY_UP);
    private Trigger down_trigger = new KeyTrigger(KeyInput.KEY_DOWN);
    private Trigger space_trigger = new KeyTrigger(KeyInput.KEY_SPACE);
    private Trigger enter_trigger = new KeyTrigger(KeyInput.KEY_RETURN);
    private Trigger escape_trigger = new KeyTrigger(KeyInput.KEY_ESCAPE);
   
    int buttonBetMargin=0;
    int buttonLeftMargin=0;  
    int logoLeftMargin=0;  
    int logoTopMargin=0;  
    int graphRightMargin=0;  
    int graphBottomMargin=0;  
  
    // 
    Button  playButt;
    Button settButt;
    Button quitButt;
    Button tutButt;
       
    Node buttonNode=new Node();
    
    //Main options
    int OPTION_PLAY=0;
    int OPTION_SETTINGS=1;
    int OPTION_TUTORIAL=2;
    int OPTION_QUIT=3;
    int selectedOption=OPTION_PLAY; //For scrolling. Current button with focus
    ArrayList<Button> buttonTab=new ArrayList<Button> ();//Array containing all buttons
    
    MenuAudioEffectsHelper menuAudioEffectsHelper ;
    MusicHelper musicHelper;
    JoystickEventListener joystickEventListener;
  public MenuMainScreen( )
       {
           
         
       }
       
 
    public void init(AppStateManager stateManager, Application app,MusicHelper musicHelper,MenuAudioEffectsHelper menuAudioEffectsHelper ) {
          
        this.app = (Main) app;
        this.rootNode = this.app.getRootNode();
        this.viewPort = this.app.getViewPort();
        this.guiNode = this.app.getGuiNode();
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.musicHelper=musicHelper;
        this.menuAudioEffectsHelper=menuAudioEffectsHelper;
        
         //Calculations
        int buttonHeight=this.viewPort.getCamera().getHeight()/15;
        int buttonWidth=(int)(this.viewPort.getCamera().getWidth()/2);
        int fontSize=this.viewPort.getCamera().getHeight()/12;
        if(Main.i18n.equals(Main.I18N_RUSSIA))
           fontSize=this.viewPort.getCamera().getHeight()/14;
       else if(Main.i18n.equals(Main.I18N_KOREA))
           fontSize=this.viewPort.getCamera().getHeight()/14;
       else if(Main.i18n.equals(Main.I18N_CHINA))
           fontSize=this.viewPort.getCamera().getHeight()/14;
       else if(Main.i18n.equals(Main.I18N_JAPAN))
           fontSize=this.viewPort.getCamera().getHeight()/14;
        
        buttonBetMargin=this.viewPort.getCamera().getHeight()/25;
        buttonLeftMargin=this.viewPort.getCamera().getHeight()/10;
        logoLeftMargin=buttonLeftMargin;  
        logoTopMargin=logoLeftMargin;  
        graphRightMargin=buttonLeftMargin;  
        graphBottomMargin=graphRightMargin;  
 
        ////////////////////////IMAGES///////
       //BACKGROUND
        bgImage=declareImage(this.viewPort.getCamera().getWidth(), this.viewPort.getCamera().getHeight(), 0 , this.viewPort.getCamera().getHeight(),Vars.ASSET_IMAGE_BG);
        //LOGO
        logoImage=declareImage((int)(this.viewPort.getCamera().getHeight()/1.5f),  (this.viewPort.getCamera().getHeight()/10), logoLeftMargin,  this.viewPort.getCamera().getHeight()-logoTopMargin,Vars.ASSET_IMAGE_LOGO);
       //GRAPHIC
        graphImage=declareImage((int)(this.viewPort.getCamera().getHeight()/2.5f),  (this.viewPort.getCamera().getHeight()/2), (int)(this.viewPort.getCamera().getWidth()-this.viewPort.getCamera().getHeight()/2-graphRightMargin),(int)(this.viewPort.getCamera().getHeight()/2+graphBottomMargin),Vars.ASSET_IMAGE_GRAPHIC);
       
            
        /////////////////////////////BUTTONS//////////////////////  
        //Play
        playButt=declareButton(fontSize,   buttonWidth,   buttonHeight,"MainMenuButtonPlay", buttonLeftMargin,  this.viewPort.getCamera().getHeight()/2+buttonHeight/2);
        //Settings
        settButt=declareButton(fontSize,   buttonWidth,   buttonHeight,"MainMenuButtonSettings", buttonLeftMargin,(int)(playButt.getLocalTranslation().y-buttonHeight-buttonBetMargin) );
        //Tutorial
        tutButt=declareButton(fontSize,   buttonWidth,   buttonHeight,"MainMenuButtonTutorial", buttonLeftMargin,(int)(settButt.getLocalTranslation().y-buttonHeight-buttonBetMargin) );
        //Quit
        quitButt=declareButton(fontSize,   buttonWidth,   buttonHeight,"MainMenuButtonQuit", buttonLeftMargin,(int)(tutButt.getLocalTranslation().y-buttonHeight-buttonBetMargin) );
        
        //Add buttons to gui
         localGuiNode.attachChild(buttonNode);
        
        //
        viewPort.setBackgroundColor(backgroundColor);
 
        //Select the first one
        GuiGlobals.getInstance().requestFocus(playButt);
       
    }
    
    /**
     * 
     * @param sizeX
     * @param sizeY
     * @param posX
     * @param posY
     * @param assetPath
     * @return 
     */
   private  Panel declareImage(int sizeX, int sizeY, int posX, int posY, String assetPath)
    {
        Panel image = new Panel();
        image.setPreferredSize(new Vector3f(  sizeX, sizeY, 0));
        localGuiNode.attachChild(image);
        image.setLocalTranslation(posX,  posY, 0);
        QuadBackgroundComponent pBLBG = new QuadBackgroundComponent( assetManager.loadTexture(assetPath));
        image.setBackground(pBLBG);  
        
        return image;
    }
   /**
    * 
    * @param fontSize
    * @param buttonWidth
    * @param buttonHeight
    * @param i18nLabel
    * @param xPos
    * @param yPos
    * @return 
    */
   Button declareButton(int fontSize, int buttonWidth, int buttonHeight,String i18nLabel, int xPos, int yPos)
   {
       Button butt=new Button(ResourceBundle.getBundle(Main.i18n).getString(i18nLabel));///
        butt.setFontSize(fontSize);
        butt.setPreferredSize( new   Vector3f(buttonWidth, buttonHeight,0));
        butt.setLocalTranslation(xPos ,yPos, 0);
        //playButt.setColor(new ColorRGBA(1f,0.1f,0.1f,1f));
        butt.setShadowColor(ColorRGBA.Black);
        butt.setFocusColor(ColorRGBA.Red);
        butt.setFocusShadowColor(ColorRGBA.Red);
        butt.setHighlightColor(ColorRGBA.Red);
        butt.setHighlightShadowColor(ColorRGBA.Red); 
        butt.setTextHAlignment(HAlignment.Left);
        butt.setTextVAlignment(VAlignment.Bottom);
        butt.setEnabled(true);
        buttonNode.attachChild(butt); 
        //Put button in array
        buttonTab.add(butt);   
        //
        return butt;
   }
   /**
    * Service keys and muouse only in this state. Disable once the state is switched
    */
    void attachInput()
    {
      inputManager.addMapping("UP", up_trigger);
     inputManager.addListener(this, new String[]{"UP"});
     inputManager.addMapping("DOWN", down_trigger);
     inputManager.addListener(this, new String[]{"DOWN"});
     inputManager.addMapping("SPACE", space_trigger);
     inputManager.addListener(this, new String[]{"SPACE"});
     inputManager.addMapping("ENTER", enter_trigger);
     inputManager.addListener(this, new String[]{"ENTER"});
     inputManager.addMapping("ESC", escape_trigger);
     inputManager.addListener(this, new String[]{"ESC"});
   
     for(int a=0;a<buttonTab.size();a++)
         buttonTab.get(a).addMouseListener(this);
       
     //
      joystickEventListener =new   JoystickEventListener();
      inputManager.addRawInputListener( joystickEventListener );   
   
    }
    
 
  /**
   * Disable inputs when the state is disabled
   */  
  void dettachInput()
    {
         inputManager.removeListener(this);
         inputManager.deleteMapping("UP");
         inputManager.deleteMapping("DOWN");
         inputManager.deleteMapping("SPACE");
         inputManager.deleteMapping("ENTER");
         inputManager.deleteMapping("ESC");
         for(int a=0;a<buttonTab.size();a++)
            buttonTab.get(a).removeMouseListener(this); 
         inputManager.removeRawInputListener( joystickEventListener );
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
 
 /**
  * Handle enter action
  */
 private void handleEnter()
     {
        //sound
          menuAudioEffectsHelper.playEnabled();
         
         //Move to pregame load or directly to game screen
         if(selectedOption==OPTION_PLAY)
           {
                app.moveFromMenuToLoadGame();
           }
          //Move to tutorial  
       else  if(selectedOption==OPTION_TUTORIAL)
           {
               app.moveFromMainToTutorial();
            }  
        //Move to settings  
       else  if(selectedOption==OPTION_SETTINGS)
           {
               app.moveFromMainToOptions();
            }
        //Quit 
         else  if(selectedOption==OPTION_QUIT)
           {
                app.stop() ;
           }
     }
     
    
    
    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
  
   
     if(!isPressed)
        return;
      
     //Do nothing on Escape. Escape via quit
    if (name.equals("ESC"))  
         {
             return;
         }
    
      if (name.equals("UP"))  
         {
            //sound
            menuAudioEffectsHelper.playOptionSwitch();
            rollUp();   
          }
      else  if (name.equals("DOWN"))  
         {
           //sound
          menuAudioEffectsHelper.playOptionSwitch();
          rollDown();  
          }
        else  if(name.equals("ENTER") || name.equals("SPACE") )
         {
            handleEnter();
             
         }
    }

    @Override
    protected void initialize(Application app) {
     }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
       rootNode.attachChild(localRootNode);
        guiNode.attachChild(localGuiNode);
        attachInput();
        
      
     }

    @Override
    protected void onDisable() {
         rootNode.detachChild(localRootNode);
         guiNode.detachChild(localGuiNode);
         dettachInput();
    }

    @Override
    public void mouseButtonEvent( MouseButtonEvent mbe, Spatial sptl, Spatial sptl1) {
    
      //Search for clicked
        if(buttonTab.contains(sptl))
           {
            selectedOption=buttonTab.indexOf(sptl);
           }
      //sound
      menuAudioEffectsHelper.playOptionSwitch();
      //action
      handleEnter();
    }

    @Override
    public void mouseEntered(MouseMotionEvent mme, Spatial sptl, Spatial sptl1) {
      //
      menuAudioEffectsHelper.playOptionSwitch();
    }

    @Override
    public void mouseExited(MouseMotionEvent mme, Spatial sptl, Spatial sptl1) {
     }

    @Override
    public void mouseMoved(MouseMotionEvent mme, Spatial sptl, Spatial sptl1) {
     }
 
   
   
 
   //Enable music on first frame. Putting it is simpleInit starts the music before anything is visible
   boolean isMusicInited=false;
   @Override
   public void postRender()
    {
        //Trigger only once
        if(!isMusicInited)
        {
            musicHelper.playMainMenu();
            isMusicInited=true;
        }
            
    }
 public void enableMusic()
   {
       isMusicInited=false;
   }
 public void disableMusic()
   {
    musicHelper.stopMainMenu();
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
               rollDown();  
               }
            else  if(evt.getValue()==1 && evt.getAxis().getName().equals(JoystickAxis.POV_Y))
             { 
               rollUp(); 
             }
             else  if(evt.getValue()==-1 && evt.getAxis().getName().equals(JoystickAxis.POV_Y))
             {
               rollDown();  
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
           
        }

        public void beginInput() {}
        public void endInput() {}
        public void onMouseMotionEvent(MouseMotionEvent evt) {}
        public void onMouseButtonEvent(MouseButtonEvent evt) {}
        public void onKeyEvent(KeyInputEvent evt) {}
        public void onTouchEvent(TouchEvent evt) {}        
    }
 
 
}