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
import com.jme3.math.FastMath;
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
import com.simsilica.lemur.ProgressBar;
 import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.event.MouseListener;
 import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.ResourceBundle;
import main.java.org.Main;
import main.java.org.menu.audio.MenuAudioEffectsHelper;
import main.java.org.menu.audio.MusicHelper;
import main.java.org.menu.settings.GameSettings;
import main.java.org.menu.settings.SaveHelper;

 
public class OptionsMainScreen  extends BaseAppState implements ActionListener, MouseListener  {

  
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
     Label optionMusicLabel;
     Label optionAudioLabel;
     Label optionFXLevelLabel;
     ProgressBar optionMusicProgress;
     ProgressBar optionAudioProgress;
     ProgressBar optionFXLevelProgress;
   
    Node buttonNode=new Node();
    ArrayList<Button> buttonTab=new ArrayList<Button> ();//Array containing all buttons
    ArrayList<Panel> progLeftTab=new ArrayList<Panel> ();//Array containing all left buttons from progressbars
    ArrayList<Panel> progRightTab=new ArrayList<Panel> ();//Array containing all right buttons from progressbars
    Hashtable<Panel, Integer> progOptType=new Hashtable<Panel, Integer>();
 
    Label optionSaveLabel;
    Label optionCancelLabel;
 
    Texture  wonBattleTextureOn ;
    Texture  wonBattleTextureOff ;
 
    Label optionLanguageLabel;
    Texture optionLanguageTex ;
    Panel optionLanguageButton ;

    int OPTION_MUSIC_VOLUME=0;
    int OPTION_AUDIO_VOLUME=1;
    int OPTION_FX_LEVEL=2;
    int OPTION_CONFIG_LANG=3;
    int OPTION_CONFIG_SAVE=4;
    int OPTION_CONFIG_CANCEL=5;
     
    int selectedOption=OPTION_MUSIC_VOLUME;
       
 
    //
   ColorRGBA origColor;

   public  static HashMap<Integer, String> langFlags=new HashMap<Integer,String>();

    MenuAudioEffectsHelper menuAudioEffectsHelper ;
    MusicHelper musicHelper;
     
    int prevOptionFXLevel=80; 
    int prevOptionAudioVolume=100;        
    int prevOptionMusicVolume=50;     
    int prevLanguage=0;     
    //
     JoystickEventListener joystickEventListener;
    
    public void init(AppStateManager stateManager, Application app,MusicHelper musicHelper,MenuAudioEffectsHelper menuAudioEffectsHelper ) {
         
        
        this.app = (Main) app;
        this.rootNode = this.app.getRootNode();
        this.viewPort = this.app.getViewPort();
        this.guiNode = this.app.getGuiNode();
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
         this.musicHelper=musicHelper;
        this.menuAudioEffectsHelper=menuAudioEffectsHelper;
       
        //
       langFlags.clear();
       langFlags.put(GameSettings.LANG_AUTO, "Textures/Flags/flag_auto.png");
       langFlags.put(GameSettings.LANG_EN, "Textures/Flags/flag_en.png");
       langFlags.put(GameSettings.LANG_DE, "Textures/Flags/flag_de.png");
       langFlags.put(GameSettings.LANG_FR, "Textures/Flags/flag_fr.png");
       langFlags.put(GameSettings.LANG_ES, "Textures/Flags/flag_sp.png");
       langFlags.put(GameSettings.LANG_IT, "Textures/Flags/flag_it.png");
       langFlags.put(GameSettings.LANG_CN, "Textures/Flags/flag_cn.png");
       langFlags.put(GameSettings.LANG_KR, "Textures/Flags/flag_kr.png");
       langFlags.put(GameSettings.LANG_RU, "Textures/Flags/flag_ru.png");
       langFlags.put(GameSettings.LANG_PL, "Textures/Flags/flag_pl.png");
       langFlags.put(GameSettings.LANG_JP, "Textures/Flags/flag_jp.png");
       
         //Calculations
        int buttonMargin=this.viewPort.getCamera().getHeight()/12;
        int buttonHeight=this.viewPort.getCamera().getHeight()/25;
        int buttonWidth=(int)(this.viewPort.getCamera().getWidth()/2-buttonMargin*3 );
        int column2PosX=this.viewPort.getCamera().getWidth()/2+buttonMargin/2;
        int buttonLeftMargin=this.viewPort.getCamera().getHeight()/10;
        int buttonTopMargin=buttonLeftMargin;
        int fontSize=this.viewPort.getCamera().getHeight()/18;
       
           //Different size for different languages and fonts
        if(Main.i18n.equals(Main.I18N_RUSSIA))
           fontSize=this.viewPort.getCamera().getHeight()/25;
        else if(Main.i18n.equals(Main.I18N_KOREA))
           fontSize=this.viewPort.getCamera().getHeight()/20;
        else if(Main.i18n.equals(Main.I18N_CHINA))
           fontSize=this.viewPort.getCamera().getHeight()/20;
         else if(Main.i18n.equals(Main.I18N_JAPAN))
           fontSize=this.viewPort.getCamera().getHeight()/20;
      
        //MENU
       //BACKGROUND
        bgImage=declareImage(this.viewPort.getCamera().getWidth(), this.viewPort.getCamera().getHeight(), 0 , this.viewPort.getCamera().getHeight(),Vars.ASSET_IMAGE_BG);
         
         //Music
        optionMusicLabel=declareButton(fontSize,   buttonWidth,   buttonHeight,"OptionMusicVolume", buttonLeftMargin, this.viewPort.getCamera().getHeight()-buttonTopMargin);
        optionMusicProgress=declareProgressbar(OPTION_MUSIC_VOLUME,buttonWidth, buttonHeight,column2PosX,(int)optionMusicLabel.getLocalTranslation().y,Vars.ASSET_IMAGE_PROG_BAR,Vars.ASSET_IMAGE_PROG_LEFT,Vars.ASSET_IMAGE_PROG_RIGHT) ;  
        //Audio
        optionAudioLabel=declareButton(fontSize,   buttonWidth,   buttonHeight,"OptionAudioVolume",buttonLeftMargin,(int)optionMusicLabel.getLocalTranslation().y-buttonMargin);
        optionAudioProgress=declareProgressbar(OPTION_AUDIO_VOLUME,buttonWidth, buttonHeight,column2PosX,(int)optionAudioLabel.getLocalTranslation().y  ,Vars.ASSET_IMAGE_PROG_BAR,Vars.ASSET_IMAGE_PROG_LEFT,Vars.ASSET_IMAGE_PROG_RIGHT) ;  
        //FX
        optionFXLevelLabel=declareButton(fontSize,   buttonWidth,   buttonHeight,"OptionFXLevel",buttonLeftMargin,(int)optionAudioLabel.getLocalTranslation().y-buttonMargin);
        optionFXLevelProgress=declareProgressbar(OPTION_FX_LEVEL,buttonWidth, buttonHeight, column2PosX,(int)optionFXLevelLabel.getLocalTranslation().y  ,Vars.ASSET_IMAGE_PROG_BAR,Vars.ASSET_IMAGE_PROG_LEFT,Vars.ASSET_IMAGE_PROG_RIGHT) ;  
        
         //Language
        optionLanguageLabel=declareButton(fontSize,   buttonWidth,   buttonHeight,"OptionLanguage",buttonLeftMargin,(int)optionFXLevelLabel.getLocalTranslation().y-buttonMargin);
        //lang flag
        optionLanguageTex=assetManager.loadTexture(langFlags.get(GameSettings.language));
        optionLanguageButton = new Panel();
        optionLanguageButton.setPreferredSize( new   Vector3f(buttonHeight*2, buttonHeight,0));
        optionLanguageButton.setLocalTranslation( column2PosX,optionLanguageLabel.getLocalTranslation().y, 0);
        QuadBackgroundComponent wBFLIa = new QuadBackgroundComponent(optionLanguageTex ); 
        optionLanguageButton.setBackground(wBFLIa);           
        buttonNode.attachChild(optionLanguageButton); 
         
        //Save
        optionSaveLabel=declareButton(fontSize,   buttonWidth,   buttonHeight,"OptionSave",buttonLeftMargin,buttonTopMargin );
        optionCancelLabel=declareButton(fontSize,   buttonWidth,   buttonHeight,"OptionCancel",column2PosX,(int)optionSaveLabel.getLocalTranslation().y );
        
        //Add buttons to gui
        localGuiNode.attachChild(buttonNode);
           
        //
        viewPort.setBackgroundColor(backgroundColor);
         
         
         //Select the first one
         GuiGlobals.getInstance().requestFocus(buttonTab.get(selectedOption));
         
           //Currwn toption
         selectedOption=OPTION_MUSIC_VOLUME;
      
      }
    
/**
 * Update controls that might be modified with cancel 
 */
 public void setup()
    {
         optionLanguageTex=assetManager.loadTexture(langFlags.get(GameSettings.language));
         QuadBackgroundComponent wBFLIa = new QuadBackgroundComponent(optionLanguageTex ); 
         optionLanguageButton.setBackground(wBFLIa);    
         
         //Init 
         prevOptionFXLevel=GameSettings.optionFXLevel; 
         prevOptionAudioVolume=GameSettings.optionAudioVolume;     
         prevOptionMusicVolume=GameSettings.optionMusicVolume; 
         prevLanguage=GameSettings.language;
         //
         optionMusicProgress.setProgressValue(100-GameSettings.optionMusicVolume);
         optionAudioProgress.setProgressValue(100-GameSettings.optionAudioVolume);
         optionFXLevelProgress.setProgressValue(100-GameSettings.optionFXLevel);
         //
         selectedOption=OPTION_MUSIC_VOLUME;
         GuiGlobals.getInstance().requestFocus(buttonTab.get(selectedOption));
    }  
 
 /**
  * 
  * @param optionType
  * @param sizeX
  * @param sizeY
  * @param posX
  * @param posY
  * @param assetPathProg
  * @param assetPathCap1
  * @param assetPathCap2
  * @return 
  */
 private ProgressBar declareProgressbar(int optionType,int sizeX, int sizeY, int posX, int posY, String assetPathProg,String assetPathCap1,String assetPathCap2)   
    {
         //
        ProgressBar optionProgress = new ProgressBar ();
        optionProgress.setPreferredSize( new   Vector3f(sizeX, sizeY,0));
        optionProgress.setProgressValue(100);
        optionProgress.setLocalTranslation( posX,posY, 0);
        optionProgress.rotate(0.0f, 0f, 180* FastMath.DEG_TO_RAD);
        optionProgress.move(sizeX,-sizeY, 0);
        QuadBackgroundComponent gradiend = new QuadBackgroundComponent( assetManager.loadTexture(assetPathProg));
        optionProgress.setBackground(gradiend);
        ((QuadBackgroundComponent) optionProgress.getValueIndicator().getBackground()).setColor(ColorRGBA.Black);
         buttonNode.attachChild(optionProgress); 
         
         //caps left
         Panel optionProgCapLeft= new Panel();
         optionProgCapLeft.setPreferredSize(new Vector3f( sizeY,sizeY , 0));
         optionProgCapLeft.setLocalTranslation( optionProgress.getLocalTranslation().x-optionProgress.getPreferredSize().x-optionProgCapLeft.getPreferredSize().x ,  optionProgress.getLocalTranslation().y+sizeY, 10);
         QuadBackgroundComponent hPCL2 = new QuadBackgroundComponent( assetManager.loadTexture(assetPathCap1));
         optionProgCapLeft.setBackground(hPCL2);           
         buttonNode.attachChild(optionProgCapLeft); 
         progLeftTab.add(optionProgCapLeft); 
         //map progressbar to option
         progOptType.put(optionProgCapLeft,optionType);
         
          //caps right
         Panel optionProgCapRight= new Panel();
         optionProgCapRight.setPreferredSize(new Vector3f( sizeY,sizeY , 0));
         optionProgCapRight.setLocalTranslation( optionProgress.getLocalTranslation().x-sizeY+optionProgCapRight.getPreferredSize().x ,  optionProgress.getLocalTranslation().y+sizeY , 10);
         QuadBackgroundComponent hPCR2 = new QuadBackgroundComponent( assetManager.loadTexture(assetPathCap2));
         optionProgCapRight.setBackground(hPCR2);           
         buttonNode.attachChild(optionProgCapRight); 
         progRightTab.add(optionProgCapRight); 
         //map progressbar to option
         progOptType.put(optionProgCapRight,optionType);
         return optionProgress;
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
        butt.setTextVAlignment(VAlignment.Center);
        butt.setEnabled(true);
        buttonNode.attachChild(butt); 
        //Put button in array
        buttonTab.add(butt); 
        //
        return butt;
   }
   
   /**
    * Brings back options after cancellation
    */
 private void   resetOptions()
   {
      GameSettings.optionFXLevel=prevOptionFXLevel; 
      GameSettings.optionAudioVolume=prevOptionAudioVolume;     
      GameSettings.optionMusicVolume=prevOptionMusicVolume;      
      GameSettings.language=prevLanguage;      
      musicHelper.resetVolume();
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
             menuAudioEffectsHelper.playOptionSwitch();
             rollUp();
         }
      else  if (name.equals("DOWN"))  
         {
          menuAudioEffectsHelper.playOptionSwitch();
          rollDown();
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
     // Add a raw listener because it's eisier to get all joystick events
      // this way.
     for(int a=0;a<buttonTab.size();a++)
       buttonTab.get(a).addMouseListener(this);
        
      for(Panel key:progOptType.keySet()  )
          key.addMouseListener(this);
      
      joystickEventListener =new    JoystickEventListener();
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
       
          for(int a=0;a<buttonTab.size();a++)
            buttonTab.get(a).removeMouseListener(this); 
         for(Panel key:progOptType.keySet()  )
            key.removeMouseListener(this);
        inputManager.removeRawInputListener( joystickEventListener );  
         
    }
    
  void handleEnter()
   {
       
         //
        if( selectedOption==OPTION_CONFIG_SAVE)
          {
            //save 
            SaveHelper.save();
            app.moveFromOptionsToMain();
          }
        else    if( selectedOption==OPTION_CONFIG_CANCEL)
          {
            resetOptions();
            app.moveFromOptionsToMain();
          }
      
       else  if( selectedOption==OPTION_CONFIG_LANG )
         {
             
             app.moveFromOptionsToOptionsLang();
          }
          
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
 
  void handleRight()
   {
       if( selectedOption==OPTION_MUSIC_VOLUME)
         {
           if(optionMusicProgress.getProgressValue()>0) 
             {
              optionMusicProgress.setProgressValue(optionMusicProgress.getProgressValue()-10);
              menuAudioEffectsHelper.playOptionSwitch();
             }
           else
              {
                //DISABLE
                 menuAudioEffectsHelper.playDisabled();
              }
             GameSettings.optionMusicVolume=100-(int)optionMusicProgress.getProgressValue();
             musicHelper.resetVolume();
         }
       else   if( selectedOption==OPTION_AUDIO_VOLUME)
         {
             //1//System.out.println("FGGGGG");
           if(optionAudioProgress.getProgressValue()>0) 
             {
              optionAudioProgress.setProgressValue(optionAudioProgress.getProgressValue()-10);
              menuAudioEffectsHelper.playOptionSwitch();
             }
           else
              {
                 //DISABLE
                 menuAudioEffectsHelper.playDisabled();
              }
            GameSettings.optionAudioVolume=100-(int)optionAudioProgress.getProgressValue();
         }
        else if( selectedOption==OPTION_FX_LEVEL)
         {
           if(optionFXLevelProgress.getProgressValue()>0) 
             {
              optionFXLevelProgress.setProgressValue(optionFXLevelProgress.getProgressValue()-10);
                menuAudioEffectsHelper.playOptionSwitch();
             }
           else
              {
                //DISABLE
                 menuAudioEffectsHelper.playDisabled();
              }
             GameSettings.optionFXLevel=100-(int)optionFXLevelProgress.getProgressValue();
          }
       
        else if( selectedOption==OPTION_CONFIG_LANG )
           { 
             handleEnter();
           }
        
        
          else if( selectedOption==OPTION_CONFIG_LANG )
           { 
             handleEnter();
           }
     
       
   }
  
  
  
  
 void handleLeft()
   {
       if( selectedOption==OPTION_MUSIC_VOLUME)
         {
           if(optionMusicProgress.getProgressValue()<100) 
             {
              optionMusicProgress.setProgressValue(optionMusicProgress.getProgressValue()+10);
              menuAudioEffectsHelper.playOptionSwitch();
             }
           else
              {
                  //DISABLE
                   menuAudioEffectsHelper.playDisabled();
              }
           GameSettings.optionMusicVolume=100-(int) optionMusicProgress.getProgressValue();
           musicHelper.resetVolume();
          }
       else if( selectedOption==OPTION_AUDIO_VOLUME)
         {
           if(optionAudioProgress.getProgressValue()<100) 
             {
              optionAudioProgress.setProgressValue(optionAudioProgress.getProgressValue()+10);
             menuAudioEffectsHelper.playOptionSwitch();
             }
           else
              {
                 //DISABLE
                   menuAudioEffectsHelper.playDisabled();
              }
            GameSettings.optionAudioVolume=100-(int)optionAudioProgress.getProgressValue();
           
          }
       else if( selectedOption==OPTION_FX_LEVEL)
         {
           if(optionFXLevelProgress.getProgressValue()<100) 
             {
              optionFXLevelProgress.setProgressValue(optionFXLevelProgress.getProgressValue()+10);
              menuAudioEffectsHelper.playOptionSwitch();
             }
           else
              {
                  //DISABLE
                   menuAudioEffectsHelper.playDisabled();
              }
             GameSettings.optionFXLevel=100-(int)optionFXLevelProgress.getProgressValue();
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
           //    System.out.println("aaaa="+sptl+" "+progOptType);
           selectedOption= progOptType.get(sptl);    
           if(progLeftTab.contains(sptl)) 
              handleLeft();
           else  if(progRightTab.contains(sptl)) 
              handleRight();
          }     
      //sound
      menuAudioEffectsHelper.playOptionSwitch();
      
    }

   
    
    @Override
    public void mouseEntered(MouseMotionEvent mme, Spatial sptl, Spatial sptl1) {
      //
      menuAudioEffectsHelper.playOptionSwitch();
    }


    @Override
    public void mouseExited(MouseMotionEvent mme, Spatial sptl, Spatial sptl1) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseMoved(MouseMotionEvent mme, Spatial sptl, Spatial sptl1) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
           //exit
             else   if(  evt.getButton().getLogicalId().equals(JoystickButton.BUTTON_6)  || evt.getButton().getLogicalId().equals(JoystickButton.BUTTON_8) )  
                    {
                        resetOptions();
                         app.moveFromOptionsToMain();
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
