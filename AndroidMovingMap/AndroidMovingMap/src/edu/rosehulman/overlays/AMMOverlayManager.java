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

	private Map<String, AMMItemizedOverlay> overlays; 
	private Context context;
	
	private void initializeOverlayTypes() {
		overlays = new HashMap<String, AMMItemizedOverlay>();

		Resources resources = this.context.getResources();
				
		overlays.put( "Type 1",
						  new AMMItemizedOverlay(resources.getDrawable(R.drawable.overlay_type_1), 
								  				"Type 1",
								  				this.context ));
		overlays.put( "Type 2", 
							new AMMItemizedOverlay(resources.getDrawable(R.drawable.overlay_type_2), 
												"Type 2",
												this.context ));
		overlays.put( "Type 3",
							new AMMItemizedOverlay(resources.getDrawable(R.drawable.overlay_type_3), 
												"Type 3",
												this.context ));
	}
	
	public AMMOverlayManager(Context context) {
		this.context = context;
		initializeOverlayTypes();
	}
	
	public List<AMMItemizedOverlay> getOverlays() {
		List<AMMItemizedOverlay> result = new ArrayList<AMMItemizedOverlay>(overlays.values());
		
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
		overlays.put(name, new AMMItemizedOverlay(this.context.getResources().getDrawable(R.drawable.ic_launcher), name, this.context));
	}

	public void addOverlay(POI poi) {
		overlays.get(poi.getType()).addOverlay(poi);
	}
	
	public int getNumberOfOverlays() {
		int result = 0;
		for (ItemizedOverlay<OverlayItem> overlay : getHandles()) {
			result += overlay.size();
		}
		
		return result;
	}
	
}
