package edu.rosehulman.overlays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import edu.rosehulman.androidmovingmap.R;
import edu.rosehulman.server.POI;

public class AMMOverlayManager implements IOverlayManager {

	private Map<String, IItemizedOverlay> overlays;
	private OwnshipOverlay ownshipOverlays;
	private Context context;

	private void initializeOverlayTypes() {
		overlays = new HashMap<String, IItemizedOverlay>();

		Resources resources = this.context.getResources();

		overlays.put(
				"Fire",
				new AMMItemizedOverlay(resources
						.getDrawable(R.drawable.fire), "Fire",
						this.context));
		overlays.put(
				"Downed Power Line",
				new AMMItemizedOverlay(resources
						.getDrawable(R.drawable.downed_power_line),
						"Downed Power Line", this.context));
		overlays.put(
				"Fallen Tree",
				new AMMItemizedOverlay(resources
						.getDrawable(R.drawable.fallen_tree), "Fallen Tree",
						this.context));
	}

	public AMMOverlayManager(Context context) {
		this.context = context;
		initializeOverlayTypes();
	}

	public List<IItemizedOverlay> getOverlays() {
		List<IItemizedOverlay> result = new ArrayList<IItemizedOverlay>(
				overlays.values());

		return result;
	}

	public List<ItemizedOverlay> getHandles() {
		List<IItemizedOverlay> list = getOverlays();
		List<ItemizedOverlay> result = new ArrayList<ItemizedOverlay>();

		for (IItemizedOverlay overlay : list) {
			if (overlay.isActive()) {
				result.add(overlay.getHandle());
			}
		}

		return result;
	}

	public void addCustomOverlay(final String name) {
		overlays.put(name, new AMMItemizedOverlay(this.context.getResources()
				.getDrawable(R.drawable.ic_launcher), name, this.context));
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
