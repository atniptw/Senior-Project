package edu.rosehulman.overlays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.drawable.Drawable;

/**
 * Singleton class that associates an overlay type
 * to its appropriate icon.
 * 
 * @author kimsj
 *
 */
public class OverlayIconRegistry {
	
	private static OverlayIconRegistry mInstance;
	
	private Map<String, Drawable> mIconMap;
	
	private OverlayIconRegistry() {
		mIconMap = new HashMap<String, Drawable>();
	}
	
	public static OverlayIconRegistry getInstance() {
		if (mInstance == null) {
			mInstance = new OverlayIconRegistry();
		}
		return mInstance;
	}

	public Drawable getIcon(String name) {
		Drawable result = mIconMap.get(name);
		if (result == null) {
			throw new NoAssociatedIconException("Requested overlay type has no associated icon");
		}
		
		return result;
	}
	
	public void registerIcon(String name, Drawable icon) {
		mIconMap.put(name, icon);
	}
	
	public void unregisterIcon(String name) {
		mIconMap.remove(name);
	}
	
	public List<String> getRegisteredOverlays() {
		return new ArrayList<String>(mIconMap.keySet());
	}
	
	public class NoAssociatedIconException extends RuntimeException {

		public NoAssociatedIconException(String string) {
			super(string);
		}
		
	}
}
