package edu.rosehulman.osmdroidtest;

import java.util.List;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {
	
	DemoItemizedOverlay overlay;
	private GeoPoint myLoc = new GeoPoint(39.51, -87.31);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        MapView mapView = new MapView(this, 256);
        mapView = new MapView(this, 256);
        mapView.setClickable(true);
        mapView.setMultiTouchControls(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.getController().setZoom(12);
        mapView.getController().setCenter(myLoc);
        
        overlay = new DemoItemizedOverlay(getResources().getDrawable(R.drawable.ic_launcher), new DefaultResourceProxyImpl(getApplicationContext()), this);
        List<Overlay> mapOverlays = mapView.getOverlays();
        
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
}
