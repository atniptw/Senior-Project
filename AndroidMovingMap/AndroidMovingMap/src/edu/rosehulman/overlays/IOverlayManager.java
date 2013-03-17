package edu.rosehulman.overlays;

import java.io.Serializable;
import java.util.List;

import org.osmdroid.views.overlay.ItemizedOverlay;

import edu.rosehulman.server.POI;

public interface IOverlayManager extends Serializable {
	public void addOverlay(POI poi);
	public List<IItemizedOverlay> getOverlays();
	public List<ItemizedOverlay> getHandles();
}
