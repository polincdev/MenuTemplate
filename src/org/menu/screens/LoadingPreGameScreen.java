package org.menu.screens;

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
 
import org.Main;
import org.menu.settings.Vars;
 
public class LoadingPreGameScreen extends BaseAppState {
    
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
   
    Panel leftFigureImage;
    Panel rightFigureImage;
    Panel leftImage;
    Panel rightImage;
    ProgressBar progressBar;
  
     // Appstate
    LinkedList<String> enqueuedForLoad=new LinkedList<String>();
    LinkedList<Integer> enqueuedTypeForLoad=new LinkedList<Integer>();
    int TYPE_IMAGE=0;
    int TYPE_MODEL=1;
   
    Runnable callback;
    int itemsCount=0;
    int progressByItem=0;
    int currentProgress=0;
 
    public void init(AppStateManager stateManager, Application app  ) {
          
        this.app = (Main) app;
        this.rootNode = this.app.getRootNode();
        this.viewPort = this.app.getViewPort();
        this.guiNode = this.app.getGuiNode();
        this.assetManager = this.app.getAssetManager();
      
       //MENU
         bgImage=declareImage(this.viewPort.getCamera().getWidth(), this.viewPort.getCamera().getHeight(), 0 , this.viewPort.getCamera().getHeight(),Vars.ASSET_IMAGE_BG);
        //Calculations
        int buttonHeight=this.viewPort.getCamera().getHeight()/20;
        float figureSize=  this.viewPort.getCamera().getWidth()*0.25f;
         float figureMargin= figureSize*0.25f;
         
       
         float faceSize=  this.viewPort.getCamera().getHeight()*0.4f;
           //LEFT
        leftImage = new Panel();
        //leftImage.setLocalScale ( -1f,1f,1) ;//mirror
        leftImage.setPreferredSize(new Vector3f( (int)(this.viewPort.getCamera().getHeight()/1.5f),  (this.viewPort.getCamera().getHeight()/10) , 0));
        localGuiNode.attachChild(leftImage);
        leftImage.setLocalTranslation(  10,  this.viewPort.getCamera().getHeight() -10  , 0);
        QuadBackgroundComponent lFaIBG = new QuadBackgroundComponent( assetManager.loadTexture(Vars.ASSET_IMAGE_LOGO));
        leftImage.setBackground(lFaIBG);    
          
         //RIGHT
        rightImage = new Panel();
        rightImage.setPreferredSize(new Vector3f( faceSize , (faceSize) , 0));
        localGuiNode.attachChild(rightImage);
        rightImage.setLocalTranslation(  this.viewPort.getCamera().getWidth()-faceSize ,  this.viewPort.getCamera().getHeight()/2   , 0);
        QuadBackgroundComponent lFarIBG = new QuadBackgroundComponent( assetManager.loadTexture(Vars.ASSET_IMAGE_GRAPHIC));
        rightImage.setBackground(lFarIBG);   
           
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
          app.moveFromLoadGameToGame();
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
void clear(){
 enqueuedTypeForLoad.clear();
 enqueuedForLoad.clear();
 currentProgress=0;
}

/**
 * Preloads all models and textures right before the game. Called externally 
 * 
 */
 public   void loadData( ){
             
 //////////LOAD MODELS AND TEXTURES HERE////////  
 //load(.......... , TYPE_IMAGE);
 
 
 //Plus one
 itemsCount=enqueuedForLoad.size() ;
 progressByItem=100/(itemsCount+1);
 currentProgress=0;
 //
   
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
        
        //zeruje progess
         progressBar.setProgressValue(100);
         
        
       
     }

    @Override
    protected void onDisable() {
         rootNode.detachChild(localRootNode);
         guiNode.detachChild(localGuiNode);
    }
}
