package edu.rosehulman.osmdroidpoi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.tileprovider.MapTile;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {
	DemoItemizedOverlay overlay;
	private List<Overlay> mapOverlays;
	private MapView mapView;

	private ServerStuff server = new ServerStuff();
	private ServerStuff.ListenPOISocket POIThread = null;
	
	private XYTileSource tileSource;
	private String mapSourcePrefix = "http://137.112.156.177/~king/testMessage/";
	private ArrayList<String> mapSourceNames = new ArrayList<String>(Arrays.asList("map2/", "map1/"));
	private int mapSourceIndex = 0;

   	private Handler invalidateDisplay = new Handler() {
	    @Override
		public void handleMessage(Message msg) {
	    	updatePOIandScreen();
	    }
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        
        mapView = new MapView(this, 256);
        mapView.setClickable(true);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(true);

        tileSource = new XYTileSource("local" + mapSourceIndex, null, 0, 4, 256, ".png",
        		mapSourcePrefix + mapSourceNames.get(mapSourceIndex));
        mapView.setTileSource(tileSource);

        GeoPoint myLoc = new GeoPoint(39.87, -87.25);
//        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.getController().setZoom(3);
        mapView.getController().setCenter(myLoc);
        
        overlay = new DemoItemizedOverlay(getResources().getDrawable(R.drawable.ic_launcher), new DefaultResourceProxyImpl(getApplicationContext()), this);
        mapOverlays = mapView.getOverlays();

    	
        OverlayItem overlayItem = new OverlayItem("hi", "lol", myLoc);
        overlay.addOverlay(overlayItem);
        
        mapOverlays.add(overlay);

        setContentView(mapView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private void updatePOIandScreen()
    {
    	if (!mapOverlays.remove(overlay))
    	{
    		Log.d("menu display", "unable to remove old overlay");
    	}
    	else
    	{
    		Log.d("menu display", "removed old overlay");        		
    	}

    	overlay.clear();

    	for (POI point : ServerStuff.POIelements.values())
    	{
    		OverlayItem overlayItem = new OverlayItem("" + point.getUID(), "lol", point.getGeoPoint());
    		overlay.addOverlay(overlayItem);
    	}

    	Log.d("menu display", "adding " + overlay.size() + " POI");
    	mapOverlays.add(overlay);
    	this.mapView.invalidate();
    }
    
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
        case R.id.menu_settings:
        	return true;

        case R.id.menu_cycle_map_type:
        	mapSourceIndex = (mapSourceIndex + 1) % mapSourceNames.size();
        	tileSource = new XYTileSource("local" + mapSourceIndex, null, 0, 4, 256, ".png",
            		mapSourcePrefix + mapSourceNames.get(mapSourceIndex));
        	Log.d("menu cycle", "pathBase: " + tileSource.getTileURLString(new MapTile(0,0,0)));
        	mapView.setTileSource(tileSource);
            mapView.invalidate();

        case R.id.menu_start_stop_sync:
        	if (this.POIThread == null)
        	{
        		this.POIThread = this.server.new ListenPOISocket();
        		this.POIThread.updatePOIHandler = this.invalidateDisplay;
        		this.POIThread.start();
        	} else
        	{
            	this.POIThread.stopThread = true;
            	this.POIThread = null;
        	}
//        	this.invalidateOptionsMenu();
        	return true;
 
        case R.id.menu_display:
        	this.updatePOIandScreen();
        	return true;

        case R.id.menu_quit:
        	if (this.POIThread != null)
        	{
        		this.POIThread.stopThread = true;
        		Log.d("test", "setting closeThread");
        		try {
        			this.POIThread.join();
        			Log.d("test", "joined");
        		} catch (InterruptedException e) {}
        	}
        	android.os.Process.killProcess(android.os.Process.myPid());
//        	this.mapView.removeAllViews();
//        	this.finish();
        	return true;
        	
        default:
        	return super.onOptionsItemSelected(item);
    	}
    }
}
