package main.java.org.menu.screens;

import main.java.org.menu.settings.Vars;
import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.ProgressBar;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import java.util.LinkedList;
import main.java.org.Main;
 
public class LoadingPreMenuScreen extends BaseAppState {
    
    boolean active=true;
     private ViewPort viewPort;
    private Node rootNode;
    private Node guiNode;
    private AssetManager assetManager;
    private Main app;
     private Node localRootNode = new Node("Settings Screen RootNode");
    private Node localGuiNode = new Node("Settings Screen GuiNode");
    private final ColorRGBA backgroundColor = ColorRGBA.Blue;
    Panel bgImage;
    
    ProgressBar progressBar;
  
     // Appstate
    LinkedList<String> enqueuedForLoad=new LinkedList<String>();
    LinkedList<Integer> enqueuedTypeForLoad=new LinkedList<Integer>();
    int TYPE_IMAGE=0;
    int TYPE_MODEL=1;
   
    LinkedList<Spatial> loaded=new LinkedList<Spatial>();
    Runnable callback;
    int itemsCount=0;
    float progressByItem=0;
    float currentProgress=0;
    public void init(AppStateManager stateManager, Application app) {
          
        this.app = (Main) app;
        this.rootNode = this.app.getRootNode();
        this.viewPort = this.app.getViewPort();
        this.guiNode = this.app.getGuiNode();
        this.assetManager = this.app.getAssetManager();
   
        //MENU
         bgImage=declareImage(this.viewPort.getCamera().getWidth(), this.viewPort.getCamera().getHeight(), 0 , this.viewPort.getCamera().getHeight(),Vars.ASSET_IMAGE_BG);
        //Calculations
        int buttonHeight=this.viewPort.getCamera().getHeight()/20;
        
       
         //PROGRESS
        progressBar = new ProgressBar ();
        progressBar.setPreferredSize(new Vector3f( this.viewPort.getCamera().getWidth()  , buttonHeight, 0));
        // progressBarLeft.setQueueBucket(RenderQueue.Bucket.Gui); //powoduje najwyszj Z 
        progressBar.setProgressValue(100);
         progressBar.setAlpha(1);
         //rotacja 180
        progressBar.rotate(0.0f, 0f, 180* FastMath.DEG_TO_RAD);
        progressBar.setLocalTranslation(  this.viewPort.getCamera().getWidth()  ,  this.viewPort.getCamera().getHeight()/2 , 0);
        QuadBackgroundComponent gradiendLeft = new QuadBackgroundComponent( assetManager.loadTexture(Vars.ASSET_IMAGE_PROG_BAR));
        progressBar.setBackground(gradiendLeft);
        ((QuadBackgroundComponent) progressBar.getValueIndicator().getBackground()).setColor(ColorRGBA.Black);
         localGuiNode.attachChild(progressBar);
         
        /////////////////LOAD INIT DATA//////////////// 
        loadData();
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

void load(String path, int type){
    enqueuedForLoad.add(path);
    enqueuedTypeForLoad.add(type);

}
   void setCallback(Runnable r){
    callback=r;
    };

public void update(float tpf){
   
   
   
}
 public void postRender()
    {
     
      if(enqueuedForLoad.size()==0)
        {
          app.moveFromLoadAppToMain();
         }  
      else
       {
           currentProgress=currentProgress+progressByItem;
           progressBar.setProgressValue(100-currentProgress);

           String path=enqueuedForLoad.remove(0);
           Integer type=enqueuedTypeForLoad.remove(0);
           if(type==TYPE_IMAGE)
             assetManager.loadTexture(path);
          else  if(type==TYPE_MODEL)
             {
               Spatial sp=assetManager.loadModel(path);
              this.app.getRenderManager().preloadScene(sp);
              }
            
       }
     
    }
 

/**
 * Indicates gui menu data(textures and models) to preload 
 */
 public   void loadData( ){
    //setCallback( );

     //Main 
     load(Vars.ASSET_IMAGE_LOGO , TYPE_IMAGE);
     load(Vars.ASSET_IMAGE_GRAPHIC , TYPE_IMAGE );
     load(Vars.ASSET_IMAGE_RADIO_ON , TYPE_IMAGE );
     load(Vars.ASSET_IMAGE_RADIO_OFF , TYPE_IMAGE );
     load(Vars.ASSET_IMAGE_PROG_LEFT , TYPE_IMAGE );
     load(Vars.ASSET_IMAGE_PROG_RIGHT , TYPE_IMAGE );
      
         
     //Calculate progress steps
     itemsCount=enqueuedForLoad.size() ;
     progressByItem=(float)100/(float)itemsCount;
     currentProgress=0;
    
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
        
      
     }

    @Override
    protected void onDisable() {
         rootNode.detachChild(localRootNode);
         guiNode.detachChild(localGuiNode);
    }
}
