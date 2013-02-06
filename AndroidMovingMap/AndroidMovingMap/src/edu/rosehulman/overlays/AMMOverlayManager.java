package edu.rosehulman.overlays;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import edu.rosehulman.androidmovingmap.R;

public class AMMOverlayManager implements Serializable {

	// Enumerate Overlay types and their associated data (ie icon)
	// These should have some better names :P
	enum OverlayTypes {
		TYPE_1,
		TYPE_2,
		TYPE_3
	}
	
	private Map<OverlayTypes, AMMItemizedOverlay> overlayTypes;
	// TODO Perhaps all overlays should use the below map style?
	private Map<String, AMMItemizedOverlay> customOverlays; 
	private Context context;
	
	private void initializeOverlayTypes() {
		overlayTypes = new HashMap<OverlayTypes, AMMItemizedOverlay>();
		
		Resources resources = this.context.getResources();
		overlayTypes.put( OverlayTypes.TYPE_1, 
						  new AMMItemizedOverlay(resources.getDrawable(R.drawable.overlay_type_1), 
								  				"Type 1",
								  				this.context ));
		overlayTypes.put( OverlayTypes.TYPE_2, 
							new AMMItemizedOverlay(resources.getDrawable(R.drawable.overlay_type_2), 
												"Type 2",
												this.context ));
		overlayTypes.put( OverlayTypes.TYPE_3,
							new AMMItemizedOverlay(resources.getDrawable(R.drawable.overlay_type_3), 
												"Type 3",
												this.context ));
		
		customOverlays = new HashMap<String, AMMItemizedOverlay>();
	}
	
	public AMMOverlayManager(Context context) {
		this.context = context;
		initializeOverlayTypes();
	}
	
	public List<AMMItemizedOverlay> getOverlayTypes() {
		List<AMMItemizedOverlay> result = new ArrayList<AMMItemizedOverlay>(overlayTypes.values());
		result.addAll(customOverlays.values());
		return result;
	}
	
	public void addCustomOverlay(final String name) {
		customOverlays.put(name, new AMMItemizedOverlay(this.context.getResources().getDrawable(R.drawable.ic_launcher), name, this.context));
	}
	
}
