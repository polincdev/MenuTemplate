package org.menu.pause;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

import java.io.IOException;
 
public class BlurFilter extends Filter {
 
private Material material;
private float strength;
private Vector2f resolution=new Vector2f(0,0);
public BlurFilter() {
super("BoxBlurFilter");
}

@Override
protected void initFilter(AssetManager assetManager, RenderManager arg1, ViewPort arg2, int w, int h) {
resolution.set(w,h);
material = new Material(assetManager, "MatDefs/Blur/Blur.j3md");
material.setVector2("Resolution", resolution);

material.setFloat("Strength", 1f);
 setEnabled(false);
 
this.material=material;
}

@Override
protected Material getMaterial() {
return material;
}
 

public void setStrength(float newStrength) {
   strength=checkFloatArgument(newStrength, 0.0f, 1f );
   //
  material.setFloat("Strength", strength);
}
 
 
  private   float checkFloatArgument(float value, float min, float max ) {
    if (value < min  ) 
             return min;
       else if(value > max  )
           return max;
    else
           return value;
    }
@Override
public void write(JmeExporter ex) throws  IOException {
super.write(ex);
OutputCapsule oc = ex.getCapsule(this);
 
}

@Override
public void read(JmeImporter im) throws IOException {
super.read(im);
InputCapsule ic = im.getCapsule(this);
 
}
}