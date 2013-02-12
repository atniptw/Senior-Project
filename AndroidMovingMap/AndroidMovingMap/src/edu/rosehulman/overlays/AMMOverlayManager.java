package edu.rosehulman.overlays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Context;
import android.content.res.Resources;
import edu.rosehulman.androidmovingmap.R;
import edu.rosehulman.server.POI;

public class AMMOverlayManager implements IOverlayManager {

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
	
	public List<AMMItemizedOverlay> getOverlays() {
		List<AMMItemizedOverlay> result = new ArrayList<AMMItemizedOverlay>(overlayTypes.values());
		result.addAll(customOverlays.values());
		
		return result;
	}
	
	public List<ItemizedOverlay> getHandles() {
		List<AMMItemizedOverlay> list = getOverlays();
		List<ItemizedOverlay> result = new ArrayList<ItemizedOverlay>();
		
		for (AMMItemizedOverlay overlay : list) {
			result.add(overlay.getHandle());
		}
		
		return result;
	}
	
	public void addCustomOverlay(final String name) {
		customOverlays.put(name, new AMMItemizedOverlay(this.context.getResources().getDrawable(R.drawable.ic_launcher), name, this.context));
	}

	public void addOverlay(POI poi) {
		// TODO Auto-generated method stub
		overlayTypes.get(poi.getType()).addOverlay(poi);
	}
	
	public int getNumberOfOverlays() {
		int result = 0;
		for (ItemizedOverlay<OverlayItem> overlay : getHandles()) {
			result += overlay.size();
		}
		
		return result;
	}
	
}
