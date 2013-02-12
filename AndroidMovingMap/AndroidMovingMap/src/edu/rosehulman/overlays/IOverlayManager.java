package edu.rosehulman.overlays;

import java.io.Serializable;
import java.util.List;

import edu.rosehulman.server.POI;

public interface IOverlayManager extends Serializable {
	public void addOverlay(POI poi);
	public List<AMMItemizedOverlay> getOverlays();
}
