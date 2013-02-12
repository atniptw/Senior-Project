package edu.rosehulman.maps;

import org.metalev.multitouch.controller.MultiTouchController.MultiTouchObjectCanvas;
import org.metalev.multitouch.controller.MultiTouchController.PointInfo;
import org.metalev.multitouch.controller.MultiTouchController.PositionAndScale;

import android.util.Log;


public class AMMGestureListener implements MultiTouchObjectCanvas<Object>{

	OSMMapView mapView;
	float pofpoie;
	
	public AMMGestureListener(OSMMapView mapView) {
		this.mapView = mapView;
	}
	
	public Object getDraggableObjectAtPoint(PointInfo arg0) {
		return mapView;
	}

	public void getPositionAndScale(Object obj, PositionAndScale objPosAndScaleOut) {
		objPosAndScaleOut.set(0, 0, true, pofpoie, false, 0, 0, true, 0);
	}

	public void selectObject(Object arg0, PointInfo arg1) {
		// TODO Auto-generated method stub
		
	}

	public boolean setPositionAndScale(Object obj, PositionAndScale aNewObjPosAndScale,
			PointInfo aTouchPoint) {
		float multiTouchScale = aNewObjPosAndScale.getScale();
		pofpoie = multiTouchScale;
//		Log.d("transform", "Scale = " + multiTouchScale);
//		mapView.transform.setScale(multiTouchScale);
		return true;
	}

	
	

}
