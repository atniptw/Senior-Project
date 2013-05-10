package edu.rosehulman.overlays;

import java.util.List;

import org.osmdroid.views.overlay.ItemizedOverlay;

import edu.rosehulman.server.POI;

public interface IItemizedOverlay {
	public String getName();
	public List<POI> getOverlays();
	public ItemizedOverlay<POI> getHandle();
	public void addOverlay(POI poi);
	public boolean isActive();
}
