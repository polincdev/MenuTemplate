package org;
 
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
 
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
  import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.style.Attributes;
import com.simsilica.lemur.style.BaseStyles;
 import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
 import java.util.Locale;
 
 import javax.imageio.ImageIO;
import org.menu.audio.MenuAudioEffectsHelper;
import org.menu.audio.MusicHelper;
import org.menu.screens.LoadingPreMenuScreen;
import org.menu.screens.LoadingPreGameScreen;
import org.menu.screens.GameScreen;
import org.menu.screens.MenuMainScreen;
import org.menu.screens.OptionsMainScreen;
import org.menu.screens.OptionsLangScreen;
import org.menu.screens.TutorialScreen;
import org.menu.settings.GameSettings;
import org.menu.settings.SaveHelper;
import org.menu.settings.SaveObject;
import org.smp.player.SimpleMediaPlayer;
 
  
 
public class Main extends SimpleApplication  {
  
 
  public static String I18N_JAPAN="Bundle_ja";
  public static String I18N_POLAND="Bundle_pl";
  public static String I18N_ENGLAND="Bundle_en";
   public static String I18N_FRANCE="Bundle_fr";
   public static String I18N_SPAIN="Bundle_es";
   public static String I18N_GERMANY="Bundle_de";
   public static String I18N_ITALY="Bundle_it";
   public static String I18N_RUSSIA="Bundle_ru";
   public static String I18N_CHINA="Bundle_cn";
   public static String I18N_KOREA="Bundle_kr";
 
   //
  public static String i18n=I18N_ENGLAND;
  //
  public static SaveObject saveObject=new SaveObject();

  
  //Screens
  private OptionsMainScreen optionsAppState;
  private OptionsLangScreen optionsLangAppState;
  private LoadingPreGameScreen loadingGameAppState ;
  private LoadingPreMenuScreen   loadingPreMenuScreen ;
  private MenuMainScreen mainScreenState;
  private TutorialScreen tutorialScreen;
  private GameScreen gameScreen;
  //Audio
  MenuAudioEffectsHelper menuAudioEffectsHelper ;
  MusicHelper musicHelper;
  /** Start the jMonkeyEngine application */
  public static void main(String[] args) {
     
        //Disables debugging
       // Logger.getLogger("com.jme3").setLevel(Level.SEVERE);
  
        //load saved data
         SaveHelper.load();
         
        //Choose language
        selectLanguage(GameSettings.language);
        
        // 
        Main app = new Main();
        //Settings
        AppSettings settings = new AppSettings(true);
        settings.setTitle("MyGame");
        settings.setUseJoysticks(true);
        app.setShowSettings(true);
        BufferedImage[] icons=null;
      try {
          icons = new BufferedImage[]{
               ImageIO.read(app.getClass().getResourceAsStream("Icons/icon128x128.png")),
               ImageIO.read(app.getClass().getResourceAsStream("Icons/icon64x64.png")),
               ImageIO.read(app.getClass().getResourceAsStream("Icons/icon32x32.png"))
          };
      } catch (IOException ex) {
          ex.printStackTrace();
      }
         settings.setIcons(icons);
        
        app.setSettings(settings);
         //
        app.start();
        //
          
    //Print available langs  
   System.out.println(Arrays.toString(Locale.getISOLanguages()));
  }
  
    Main main;
  
  @Override
  public void simpleInitApp() {
     setDisplayFps(false);         // hide FPS
     setDisplayStatView(false);    // hide debug statistics
 
     //
    main=this;
   
    //////////////////GUI////////////
    GuiGlobals.initialize(this);
    BaseStyles.loadStyleResources("Interface/Styles/custom-styles.groovy"); 


    ////////////////////LANG//////////
    initLanguage(Main.i18n);


    //////////////////////AUDIO//////
    menuAudioEffectsHelper=new MenuAudioEffectsHelper(assetManager);
    musicHelper=new MusicHelper(assetManager);
    //init
    musicHelper.initMainMenuAudio();
    menuAudioEffectsHelper.initAudio();
    
    ////////////////SCREENS/STATES///////////   
    //Step 1 - declare
    loadingPreMenuScreen=new LoadingPreMenuScreen();
    mainScreenState = new MenuMainScreen();
    optionsAppState= new OptionsMainScreen(); 
    optionsLangAppState=new OptionsLangScreen();
    tutorialScreen=new TutorialScreen();
    loadingGameAppState= new  LoadingPreGameScreen();
    gameScreen=new GameScreen();
    //Step 2 - init
    loadingPreMenuScreen.init(stateManager, this );
    mainScreenState.init(stateManager, this,musicHelper,menuAudioEffectsHelper);
    optionsAppState.init(stateManager, this,musicHelper,menuAudioEffectsHelper);
    optionsLangAppState.init(stateManager, this,musicHelper,menuAudioEffectsHelper);
    tutorialScreen.init(stateManager, this,musicHelper,menuAudioEffectsHelper);
    loadingGameAppState.init(stateManager, this  );
    gameScreen.init(stateManager,this, menuAudioEffectsHelper);
            
    //Step 3 - add and enable/disable
    //INTRO READY - uncomment and change next line - loadingPreMenuScreen.setEnabled to false
     //buildIntro();
     
    //Adding states. The first one to appear gets the TRUE 
    loadingPreMenuScreen.setEnabled(true);
    stateManager.attach(loadingPreMenuScreen);
    //
    mainScreenState.setEnabled(false);
    stateManager.attach(mainScreenState);
    //
    optionsAppState.setEnabled(false);
    stateManager.attach(optionsAppState);  
    //
     optionsLangAppState.setEnabled(false);
    stateManager.attach(optionsLangAppState); 
     //
    mainScreenState.setEnabled(false);
    stateManager.attach(mainScreenState);
     //
    tutorialScreen.setEnabled(false);
    stateManager.attach(tutorialScreen);
    //
    loadingGameAppState.setEnabled(false);
    stateManager.attach(loadingGameAppState);
    //
    gameScreen.setEnabled(false);
    stateManager.attach(gameScreen);
   
     //Disable escape - otherwise intro is broken
    getInputManager().deleteMapping( SimpleApplication.INPUT_MAPPING_EXIT );
     
  }
    
  public void  moveFromMainToTutorial()
    {
        
        mainScreenState.setEnabled(false);
       tutorialScreen.setEnabled(true);
     
    }
  
   
  
  public void  moveFromMainToOptions()
    {
        //Refresh controls
        optionsAppState.setup(); 
        mainScreenState.setEnabled(false);
        optionsAppState.setEnabled(true);
     
    }
 public void moveFromOptionsToMain()
     {
     optionsAppState.setEnabled(false);
     mainScreenState.setEnabled(true );
      }
 
  public void moveFromGameToMenu()
     {
     mainScreenState.enableMusic();
     gameScreen.setEnabled(false);
     mainScreenState.setEnabled(true );
      }
  public void moveFromTutorialToMenu()
     {
      tutorialScreen.setEnabled(false);
      mainScreenState.setEnabled(true );
     }
 
public void moveFromOptionsToOptionsLang( )
    {
      optionsLangAppState.setup();
      optionsAppState.setEnabled(false);
      optionsLangAppState.setEnabled(true );
    }
  
 public void moveFromOptionsLangToOptions()
 {
      optionsAppState.setup();
      optionsLangAppState.setEnabled(false);
      optionsAppState.setEnabled(true );
         
 }
 
    
   
 public void  moveFromLoadGameToGame()
    {
        // enqueue(new Callable(){
      //  public Void call(){
            loadingGameAppState.setEnabled(false);
            gameScreen.setEnabled(true);
       //   }
      //  });
     //
    }
 
 public void  moveFromMenuToGame()
    { 
        mainScreenState.disableMusic();
        mainScreenState.setEnabled(false );
        gameScreen.setEnabled(true);
        return  ;
     }
 
 public void  moveFromMenuToLoadGame()
    { 
       
        mainScreenState.disableMusic();
        mainScreenState.setEnabled(false );
        loadingGameAppState.loadData();
        loadingGameAppState.setEnabled(true);
        return  ;
     }
 public void  moveFromLoadAppToMain()
    { 
        loadingPreMenuScreen .setEnabled(false);
        mainScreenState.setEnabled(true);
        return  ;
     }
  


  
public static void selectLanguage(int language)
{
    
    
    //jezlei autommatyczne
  if(language==GameSettings.LANG_AUTO)
    {
     //
      Locale locale = Locale.getDefault();
      String lang = locale.getDisplayLanguage();
      String country = locale.getLanguage().toLowerCase();
      
      System.out.println("SYSTEM LANG="+lang+" "+country+" "+locale.getDisplayLanguage()+" "+locale.getDisplayName());
      //Uncomment to force language
      //country="pl";
      if(country.equals("en"))
           i18n=I18N_ENGLAND;
        else if(country.equals("us"))
           i18n=I18N_ENGLAND;
        else if(country.equals("pl"))
           i18n=I18N_POLAND;
       else if(country.equals("fr"))
           i18n=I18N_FRANCE;
       else if(country.equals("es"))
           i18n=I18N_SPAIN;
       else if(country.equals("de"))
           i18n=I18N_GERMANY;
       else if(country.equals("it"))
           i18n=I18N_ITALY;
       else if(country.equals("ru"))
           i18n=I18N_RUSSIA;
       else if(country.equals("cn"))
           i18n=I18N_CHINA;
       else if(country.startsWith("zh"))
           i18n=I18N_CHINA;
       else if(country.equals("kr"))
           i18n=I18N_KOREA;
      else if(country.equals("ja"))
           i18n=I18N_JAPAN;
     }
  
    if(language==GameSettings.LANG_EN)
           i18n=I18N_ENGLAND;
      else  if(language==GameSettings.LANG_PL)
           i18n=I18N_POLAND;
       else  if(language==GameSettings.LANG_FR)
           i18n=I18N_FRANCE;
       else  if(language==GameSettings.LANG_ES)
           i18n=I18N_SPAIN;
       else  if(language==GameSettings.LANG_DE)
           i18n=I18N_GERMANY;
       else  if(language==GameSettings.LANG_IT)
           i18n=I18N_ITALY;
       else  if(language==GameSettings.LANG_RU)
           i18n=I18N_RUSSIA;
       else if(language==GameSettings.LANG_CN)
           i18n=I18N_CHINA;
       else if(language==GameSettings.LANG_KR)
           i18n=I18N_KOREA;
       else if(language==GameSettings.LANG_JP)
           i18n=I18N_JAPAN;
}


  public void initLanguage(String lang)
     {   
      if(lang.equals(Main.I18N_RUSSIA))
            {
            //styling
            Attributes attrs =   GuiGlobals.getInstance().getStyles().getSelector("label", null);
            attrs.set("font", assetManager.loadFont("Interface/Fonts/ru-export.fnt"));
            attrs.set("fontSize", 20);
             //styling
            Attributes attrs2 =   GuiGlobals.getInstance().getStyles().getSelector("button", null);
            attrs2.set("font", assetManager.loadFont("Interface/Fonts/ru-export.fnt"));
            attrs2.set("fontSize", 30);
            
            }
      else   if(lang.equals(Main.I18N_KOREA))
            {
            //styling
            Attributes attrs =   GuiGlobals.getInstance().getStyles().getSelector("label", null);
            attrs.set("font", assetManager.loadFont("Interface/Fonts/kr-export_hangul.fnt"));
            attrs.set("fontSize", 20);
             //styling
            Attributes attrs2 =   GuiGlobals.getInstance().getStyles().getSelector("button", null);
            attrs2.set("font", assetManager.loadFont("Interface/Fonts/kr-export_hangul.fnt"));
            attrs2.set("fontSize", 30);
            
            }
       else   if(lang.equals(Main.I18N_JAPAN))
            {
            //styling
            Attributes attrs =   GuiGlobals.getInstance().getStyles().getSelector("label", null);
            attrs.set("font", assetManager.loadFont("Interface/Fonts/jp-export.fnt"));
            attrs.set("fontSize", 20);
             //styling
            Attributes attrs2 =   GuiGlobals.getInstance().getStyles().getSelector("button", null);
            attrs2.set("font", assetManager.loadFont("Interface/Fonts/jp-export.fnt"));
            attrs2.set("fontSize", 30);
            
            }
         else   if(lang.equals(Main.I18N_CHINA))
            {
            //styling
            Attributes attrs =   GuiGlobals.getInstance().getStyles().getSelector("label", null);
            attrs.set("font", assetManager.loadFont("Interface/Fonts/cn-export_hanja.fnt"));
            attrs.set("fontSize", 20);
             //styling
            Attributes attrs2 =   GuiGlobals.getInstance().getStyles().getSelector("button", null);
            attrs2.set("font", assetManager.loadFont("Interface/Fonts/cn-export_hanja.fnt"));
            attrs2.set("fontSize", 30);
            
            }
         else
            {
            //styling
            Attributes attrs =   GuiGlobals.getInstance().getStyles().getSelector("label", null);
            attrs.set("font", assetManager.loadFont("Interface/Fonts/eng_pol-export.fnt"));
            attrs.set("fontSize", 20);
             //styling
            Attributes attrs2 =   GuiGlobals.getInstance().getStyles().getSelector("button", null);
            attrs2.set("font", assetManager.loadFont("Interface/Fonts/eng_pol-export.fnt"));
            attrs2.set("fontSize", 30);
          
            }
}
  
  ///////////////////////////////INTRO READY//////////
  
  SimpleMediaPlayer mediaPlayer;
  BaseAppState introState;
  ActionListener breakListener;
  
  @Override
    public void simpleUpdate(float tpf) {
        
        //INTRO READY
        //!!!!!!!!!!IMPORTANT
        if(mediaPlayer!=null)
          mediaPlayer.update(tpf);
    }
 
  public void  moveFromIntroToLoadPreMenu()
    {
        introState.setEnabled(false);
        loadingPreMenuScreen.setEnabled(true);
       
    }
 
 void buildIntro()
 {
  
      //Init player
         mediaPlayer=new SimpleMediaPlayer(this);
        //Config
       //Node to add the geometry to. It gets self attached and dettached on enable/disable
        Node guiNode=getGuiNode();
         //Original movie dimentions - relevant only for keeping aspect ratio
        int movieWidth=960;
        int movieHeight=540;
        //True if aspect ratio should be kept. False is the movie should be stretched to the screen 
        boolean keepAspect=true;
        //Unique name
        String screenName="Intro";
         //Image to display when player is idle. Null to use screenColor
         String idleImageAssetPath="Textures/Media/idleImageAssetPath.jpg";
         //Image to display when player is loading. Null to use screenColor
         String loadingImageAssetPath="Textures/Media/loadingImageAssetPath.jpg";
         //Image to display when player is paused. Null to last frame
         String pausedImageAssetPath="Textures/Media/pausedImageAssetPath.jpg";
         //Color to use if above pictures are not provided.
         ColorRGBA screenColor=ColorRGBA.Black;
         //Video to play. Must not be null
         String videoAssetPath="Media/320_180.mjpg";
         //Audio to play. Null if no audio
         String audioAssetPath="Media/audio.ogg";
         //Source FPS. Should be consistent with original FPS. In most cases 25 or 30
         int framesPerSec=30;
         //Playback mode. Play once or loop
         int playBackMode=SimpleMediaPlayer.PB_MODE_ONCE;
         //Transparency of the screen. 1 for intro, material and menu geometries. Below 1 for HUD geometries
         float alpha=1f;
         //Generate state 
         introState=mediaPlayer.genState(guiNode, movieWidth, movieHeight, keepAspect,screenName, idleImageAssetPath, loadingImageAssetPath, pausedImageAssetPath,screenColor,videoAssetPath,audioAssetPath, framesPerSec, playBackMode,alpha );
         //Add intro state. Auto load and play video on enabled
         stateManager.attach(introState);
          introState.setEnabled(true);
          //Listener to chain next (menu)state. On end switches states
          mediaPlayer.setListener(new SimpleMediaPlayer.VideoScreenListener() {
              @Override
              public void onPreLoad(String screenName) {
                   }

              @Override
              public void onLoaded(String screenName) {
                }

              @Override
              public void onPrePlay(String screenName) {
               }

              @Override
              public void onLoopEnd(String screenName) {
                }

              @Override
              public void onEnd(String screenName) {
                     //
                     inputManager.deleteMapping("StopBySpace");
                     inputManager.deleteMapping("StopByEnter");
                     inputManager.addListener(breakListener );
                     //
                     moveFromIntroToLoadPreMenu();
                  
                }
          });
          
         //Key listener to stop the intro in the course of playback. Calls onEnd and switches to menu
           breakListener=new ActionListener(){
              @Override
              public void onAction(String name, boolean isPressed, float tpf) {
                  mediaPlayer.stopMedia();
                 
               }
          };
         
        inputManager.addMapping("StopBySpace", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("StopByEnter", new KeyTrigger(KeyInput.KEY_RETURN));
        inputManager.addListener(breakListener, new String[]{"StopBySpace"});
        inputManager.addListener(breakListener, new String[]{"StopByEnter"});
 }
  
}
 