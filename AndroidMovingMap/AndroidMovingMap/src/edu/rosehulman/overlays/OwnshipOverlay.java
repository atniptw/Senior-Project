package edu.rosehulman.overlays;

import java.util.List;

import org.osmdroid.views.overlay.ItemizedOverlay;

import edu.rosehulman.server.POI;

/**
 * Specialized overlay to represent the position of other
 * devices on the network.
 * 
 * @author kimsj
 *
 */
public class OwnshipOverlay implements IItemizedOverlay {
	
	private static final String name = "Device";

	public String getName() {
		return name;
	}

	public List<POI> getOverlays() {
		// TODO Auto-generated method stub
		return null;
	}

	public ItemizedOverlay<POI> getHandle() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addOverlay(POI poi) {
		// TODO Auto-generated method stub

	}

}
