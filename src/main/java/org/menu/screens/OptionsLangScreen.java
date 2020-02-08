package main.java.org.menu.screens;


import main.java.org.menu.settings.Vars;
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
 import com.jme3.texture.Texture;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.GuiGlobals;
 import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Label;
 import com.simsilica.lemur.Panel;
 import com.simsilica.lemur.VAlignment;
 import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.event.MouseListener;
import java.util.ArrayList;
 
import java.util.ResourceBundle;
import main.java.org.Main;
import main.java.org.menu.audio.MenuAudioEffectsHelper;
import main.java.org.menu.audio.MusicHelper;
import main.java.org.menu.settings.GameSettings;
import main.java.org.menu.settings.SaveHelper;

 
public class OptionsLangScreen  extends BaseAppState implements ActionListener, MouseListener {

    
  private final ColorRGBA backgroundColor = ColorRGBA.Blue;
  private Trigger up_trigger = new KeyTrigger(KeyInput.KEY_UP);
  private Trigger down_trigger = new KeyTrigger(KeyInput.KEY_DOWN);
  private Trigger left_trigger = new KeyTrigger(KeyInput.KEY_LEFT);
  private Trigger right_trigger = new KeyTrigger(KeyInput.KEY_RIGHT);
  private Trigger space_trigger = new KeyTrigger(KeyInput.KEY_SPACE);
  private Trigger enter_trigger = new KeyTrigger(KeyInput.KEY_RETURN);
  private Trigger escape_trigger = new KeyTrigger(KeyInput.KEY_ESCAPE);
  String lastPressedAction="";
       
     private ViewPort viewPort;
    private Node rootNode;
    private Node guiNode;
    private AssetManager assetManager;
    private Main app;
    private InputManager inputManager;
    private Node localRootNode = new Node("Settings Screen RootNode");
    private Node localGuiNode = new Node("Settings Screen GuiNode");
    Panel bgImage;

    Label optionWindowLangLabel;
    Label optionSaveLabel;
    Label optionCancelLabel;

    Texture  flagTextureOn ;
    Texture  flagTextureOff ;

    int OPTION_LANG =0;
    int OPTION_CONFIG_SAVE=1;
    int OPTION_CONFIG_CANCEL=2;
 
    boolean resChoiceMode=false;
    Panel[] resPanels ;
     //
    ColorRGBA origColor;
  
    Node buttonNode=new Node();
    ArrayList<Button> buttonTab=new ArrayList<Button> ();//Array containing all buttons
    ArrayList<Panel> langOptTab=new ArrayList<Panel> ();//Array containing all  select  buttons 
    int prevLanguage=0;     
      //Audio
    MenuAudioEffectsHelper menuAudioEffectsHelper ;
    MusicHelper musicHelper;
     //
     JoystickEventListener joystickEventListener;
     int selectedOption=OPTION_LANG;
   
    public void init(AppStateManager stateManager, Application app,MusicHelper musicHelper,MenuAudioEffectsHelper menuAudioEffectsHelper  ) {
         
        
        this.app = (Main) app;
        this.rootNode = this.app.getRootNode();
        this.viewPort = this.app.getViewPort();
        this.guiNode = this.app.getGuiNode();
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.musicHelper=musicHelper;
        this.menuAudioEffectsHelper=menuAudioEffectsHelper;
      
         //
        resPanels=new Panel[OptionsMainScreen.langFlags.size()];
          
        ////////////////////////IMAGES///////
        //BACKGROUND
        bgImage=declareImage(this.viewPort.getCamera().getWidth(), this.viewPort.getCamera().getHeight(), 0 , this.viewPort.getCamera().getHeight(),Vars.ASSET_IMAGE_BG);
        
        //Calculations
        int buttonMargin=this.viewPort.getCamera().getHeight()/19;
        int buttonHeight=this.viewPort.getCamera().getHeight()/25;
        int buttonWidth=(int)(this.viewPort.getCamera().getWidth()/2-buttonMargin*3 );
        int column2PosX=this.viewPort.getCamera().getWidth()/2+buttonMargin/2;
        int buttonLeftMargin=this.viewPort.getCamera().getHeight()/10;
        int buttonTopMargin=buttonLeftMargin;
        int fontSize=this.viewPort.getCamera().getHeight()/24;
        
       if(Main.i18n.equals(Main.I18N_RUSSIA))
           fontSize=this.viewPort.getCamera().getHeight()/20;
       else if(Main.i18n.equals(Main.I18N_KOREA))
           fontSize=this.viewPort.getCamera().getHeight()/20;
         else if(Main.i18n.equals(Main.I18N_CHINA))
           fontSize=this.viewPort.getCamera().getHeight()/20;
         else if(Main.i18n.equals(Main.I18N_JAPAN))
           fontSize=this.viewPort.getCamera().getHeight()/20;
      
         //Radio button tex
          flagTextureOn= assetManager.loadTexture(Vars.ASSET_IMAGE_RADIO_ON);
          flagTextureOff=assetManager.loadTexture(Vars.ASSET_IMAGE_RADIO_OFF);
        
         //
         optionWindowLangLabel=declareButton(fontSize,   buttonWidth,   buttonHeight,"OptionLanguage",buttonLeftMargin, this.viewPort.getCamera().getHeight()-buttonTopMargin );
        
        
         //Add flags with radio buttons
         float posY=optionWindowLangLabel.getLocalTranslation().y ;
         for( int a=0;a<OptionsMainScreen.langFlags.size();a++ ) 
           {
            //
            Panel optionWindowModeWindLowButton = new Panel();
             optionWindowModeWindLowButton.setPreferredSize( new   Vector3f(buttonHeight  , buttonHeight ,0));
             optionWindowModeWindLowButton.setLocalTranslation( column2PosX, posY , 0);
             QuadBackgroundComponent wBFLL  = new QuadBackgroundComponent( GameSettings.language==a? flagTextureOn: flagTextureOff);
             optionWindowModeWindLowButton.setBackground(wBFLL);           
             buttonNode.attachChild(optionWindowModeWindLowButton);
             resPanels[a]=optionWindowModeWindLowButton;
             langOptTab.add(optionWindowModeWindLowButton);
             //   
             Texture  optionLanguageTex=assetManager.loadTexture(OptionsMainScreen.langFlags.get(a));
            Panel optionLanguageButton = new Panel();
            optionLanguageButton.setPreferredSize( new   Vector3f(buttonHeight*2, buttonHeight,0));
            optionLanguageButton.setLocalTranslation( optionWindowModeWindLowButton.getLocalTranslation().x+buttonHeight+buttonMargin,posY , 0);
            QuadBackgroundComponent wBFLIa = new QuadBackgroundComponent(optionLanguageTex ); 
            optionLanguageButton.setBackground(wBFLIa);           
            buttonNode.attachChild(optionLanguageButton);
            posY=posY -buttonMargin;
           
         }
         //
      
           
         //////////////Save
         //Save
         optionSaveLabel=declareButton(fontSize,   buttonWidth,   buttonHeight,"OptionSave",buttonLeftMargin,buttonTopMargin );
         optionCancelLabel=declareButton(fontSize,   buttonWidth,   buttonHeight,"OptionCancel",column2PosX,(int)optionSaveLabel.getLocalTranslation().y );
        
        //
         //Add buttons to gui
         localGuiNode.attachChild(buttonNode);
       
        
           //Current option
          optionWindowLangLabel.setColor(ColorRGBA.Red);
          selectedOption=OPTION_LANG;
      
      }
    
       
/**
 * Update controls that might be modified with cancel 
 */
 public void setup()
    {
        for( int a=0;a<OptionsMainScreen.langFlags.size();a++ ) 
           {
              QuadBackgroundComponent wBFLL  = new QuadBackgroundComponent(flagTextureOff);
              resPanels[a].setBackground(wBFLL);   
            } 
           
         QuadBackgroundComponent wBFLL  = new QuadBackgroundComponent(  flagTextureOn );
         resPanels[GameSettings.language].setBackground(wBFLL);   
         prevLanguage=GameSettings.language;
          //
         selectedOption=OPTION_LANG;
         GuiGlobals.getInstance().requestFocus(optionWindowLangLabel);
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
    * @return declared Button
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
 void handleRight()
   {
    if( selectedOption==OPTION_LANG)
        {
          resChoiceMode=true;
          menuAudioEffectsHelper.playOptionSwitch();  
         }
         
       
   }
     void handleLeft()
   {
      if( selectedOption==OPTION_LANG)
        {
            resChoiceMode=false;
            menuAudioEffectsHelper.playOptionSwitch();  
            optionWindowLangLabel.setColor(ColorRGBA.Red);
        }
       
   }
    @Override
    protected void initialize(Application app) {
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void cleanup(Application app) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

      @Override
    protected void onEnable() {
        rootNode.attachChild(localRootNode);
        guiNode.attachChild(localGuiNode);
        //
        attachInput();
     }

    @Override
    protected void onDisable() {
         rootNode.detachChild(localRootNode);
         guiNode.detachChild(localGuiNode);
         //
         dettachInput();
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
      
      if(!isPressed)
      return;
     
     
      if (name.equals("UP"))  
         {
              handleUp();
         }
      else  if (name.equals("DOWN"))  
         {
         handleDown();
                  
         }
    else  if (name.equals("LEFT"))  
         {
             handleLeft();
         }
     else  if (name.equals("RIGHT"))  
         {
             handleRight();
         }
       else  if(name.equals("ENTER") || name.equals("SPACE") )
         {
            handleEnter();  
         }
       else  if(name.equals("ESC") )
           {
               selectedOption=OPTION_CONFIG_CANCEL;
               handleEnter();  
           }
    }
    
     
    void attachInput()
    {
      inputManager.addMapping("UP", up_trigger);
     inputManager.addListener(this, new String[]{"UP"});
     inputManager.addMapping("DOWN", down_trigger);
     inputManager.addListener(this, new String[]{"DOWN"});
      inputManager.addMapping("LEFT", left_trigger);
     inputManager.addListener(this, new String[]{"LEFT"});
     inputManager.addMapping("RIGHT",right_trigger);
     inputManager.addListener(this, new String[]{"RIGHT"});
     inputManager.addMapping("SPACE", space_trigger);
     inputManager.addListener(this, new String[]{"SPACE"});
     inputManager.addMapping("ENTER", enter_trigger);
     inputManager.addListener(this, new String[]{"ENTER"});
       inputManager.addMapping("ESC", escape_trigger);
     inputManager.addListener(this, new String[]{"ESC"});
     //
      for(int a=0;a<langOptTab.size();a++)
         langOptTab.get(a).addMouseListener(this);
          // this way.
     for(int a=0;a<buttonTab.size();a++)
       buttonTab.get(a).addMouseListener(this);
     
      joystickEventListener =new   JoystickEventListener();
       inputManager.addRawInputListener( joystickEventListener );   
    }
      void dettachInput()
    {
         inputManager.removeListener(this);
       
         inputManager.deleteMapping("UP");
         inputManager.deleteMapping("DOWN");
         inputManager.deleteMapping("LEFT");
         inputManager.deleteMapping("RIGHT");
         inputManager.deleteMapping("SPACE");
         inputManager.deleteMapping("ENTER");
         inputManager.deleteMapping("ESC");
       
          for(int a=0;a<langOptTab.size();a++)
            langOptTab.get(a).removeMouseListener(this); 
         for(int a=0;a<buttonTab.size();a++)
            buttonTab.get(a).removeMouseListener(this); 
         
         inputManager.removeRawInputListener( joystickEventListener );  
    }
    
  void handleEnter()
   {
       
         //
        if( selectedOption==OPTION_CONFIG_SAVE)
          {
             //Select langauge
             Main.selectLanguage(GameSettings.language);
         
             //
            SaveHelper.save();
            app.moveFromOptionsLangToOptions();
            
          }
        else    if( selectedOption==OPTION_CONFIG_CANCEL)
          {
            resetOptions();
            app.moveFromOptionsLangToOptions();
          }
        
    
        
        
   }
   
   /**
    * Brings back options after cancellation
    */
 private void   resetOptions()
   {
       
      GameSettings.language=prevLanguage;      
      
   }
   
 void handleUp( )
   {
        
     if(!resChoiceMode)
         {
             
            menuAudioEffectsHelper.playOptionSwitch();
            rollUp();
           
         }
      else
        { 
           for( int a=0;a<OptionsMainScreen.langFlags.size();a++ ) 
           {
              QuadBackgroundComponent wBFLL  = new QuadBackgroundComponent(flagTextureOff);
              resPanels[a].setBackground(wBFLL);   
            
           } 
            //
            GameSettings.language--;
            if(GameSettings.language==-1)
              GameSettings.language=OptionsMainScreen.langFlags.size()-1;
            
            QuadBackgroundComponent wBFLL  = new QuadBackgroundComponent(  flagTextureOn );
              resPanels[GameSettings.language].setBackground(wBFLL);   
            
            
            } 
        
        
        
     
   }
  void handleDown()
   {
        
        
        if(!resChoiceMode)
         {
           rollDown();
         }
        else
        {
           for( int a=0;a<OptionsMainScreen.langFlags.size();a++ ) 
           {
              QuadBackgroundComponent wBFLL  = new QuadBackgroundComponent(   flagTextureOff);
              resPanels[a].setBackground(wBFLL);   
            
           } 
            //
            GameSettings.language++;
            if(GameSettings.language>OptionsMainScreen.langFlags.size()-1)
              GameSettings.language=0;
            
            QuadBackgroundComponent wBFLL  = new QuadBackgroundComponent(  flagTextureOn );
             resPanels[GameSettings.language].setBackground(wBFLL);   
         
        }
   } 

    @Override
    public void mouseButtonEvent(MouseButtonEvent mbe, Spatial sptl, Spatial sptl1) {
         if(sptl==null || mbe.isPressed())
         return;
         
         if(buttonTab.contains(sptl)) 
         {
            selectedOption=buttonTab.indexOf(sptl);
            //action
           handleEnter();
         }
       else
          {   
            if(langOptTab.contains(sptl)) 
              {
               for( int a=0;a<OptionsMainScreen.langFlags.size();a++ ) 
                {
                   QuadBackgroundComponent wBFLL  = new QuadBackgroundComponent(flagTextureOff);
                   resPanels[a].setBackground(wBFLL);   
                 } 
                 //
                GameSettings.language=langOptTab.indexOf(sptl);
                QuadBackgroundComponent wBFLL  = new QuadBackgroundComponent(  flagTextureOn );
                resPanels[GameSettings.language].setBackground(wBFLL);   

              }
          }
        menuAudioEffectsHelper.playOptionSwitch();
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
     *  Easier to watch for all button and axis events with a raw input listener.
     */   
    protected class JoystickEventListener implements RawInputListener {

        public void onJoyAxisEvent(JoyAxisEvent evt) {
        
              
          if(evt.getValue()==1 && evt.getAxis().getName().equals(JoystickAxis.POV_X))
             {
                 handleRight();
             }
            else   if(evt.getValue()==-1 && evt.getAxis().getName().equals(JoystickAxis.POV_X))
              {
                 handleLeft();
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
                        resetOptions();
                         app.moveFromOptionsLangToOptions();
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
