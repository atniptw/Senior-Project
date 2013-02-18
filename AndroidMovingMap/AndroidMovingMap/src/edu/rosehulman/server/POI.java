package edu.rosehulman.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.OverlayItem;

import android.location.Location;
import android.util.Log;

public class POI extends OverlayItem implements Serializable {
    private final int UID;
    private String name;
    private double latitude;
    private double longitude;
    private String POItype;
    private Map<String,String> attributes;

    public POI(JSONObject data) throws JSONException
    {
		super(data.getString("name"), "", new GeoPoint(data.getDouble("latitude"), data.getDouble("longitude")));

    	this.UID = data.getInt("UID"); data.remove("UID");
    	this.name = data.getString("name"); data.remove("name");
    	this.latitude = data.getDouble("latitude"); data.remove("latitude");
    	this.longitude = data.getDouble("longitude"); data.remove("longitude");
    	this.POItype = data.getString("POItype"); data.remove("POItype");
    	this.attributes = new HashMap<String,String>();
    	
        Iterator i = data.keys();
        while (i.hasNext()) {
            try {
                String key = i.next().toString();
                String value = data.getString(key);
                this.attributes.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
   	}

	public POI(int UID, String name, double latitude, double longitude, String POItype, Map<String,String> attributes)
    {
		super(name, "", new GeoPoint(latitude, longitude));
		
    	this.UID = UID;
    	this.name = name;
    	this.latitude = latitude;
    	this.longitude = longitude;
    	this.POItype = POItype;
    	this.attributes = new HashMap<String,String>();
    	this.attributes.putAll(attributes);
    }

	public int getUID()
	{
		return this.UID;
	}
	
	@Override
	public String getUid() 
	{
		return String.valueOf(getUID());
	}
	
	public String getType()
	{
		return this.POItype;
	}
	
	public GeoPoint getGeoPoint()
	{
		return new GeoPoint(this.latitude, this.longitude);
	}
	
	public void setLocation(Location loc){
		this.latitude = loc.getLatitude();
		this.longitude = loc.getLongitude();
	}
	
	public String toJSONString()
	{
/*	    private final int UID;
	    private String name;
	    private double latitude;
	    private double longitude;
	    private int POItype;
	    private Map<String,String> attributes;
*/
		JSONObject data = new JSONObject();
		try {
			data.put("UID", this.UID);
			data.put("name", this.name);
			data.put("latitude", this.latitude);
			data.put("longitude", this.longitude);
			data.put("POItype", this.POItype);

			for (String key : this.attributes.keySet())
			{
				data.put(key, this.attributes.get(key));
			}
		} catch (JSONException e) {
			Log.d("POI", "toJSONString error");
			e.printStackTrace();
		}

/*
		this.attributes = new HashMap<String,String>();

        Iterator i = data.keys();
        while (i.hasNext()) {
            try {
                String key = i.next().toString();
                String value = data.getString(key);
                this.attributes.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
*/
		return data.toString();
	}
	
    public String toString()
    {
    	String data = "POI:\n";
    	data += "\tname: " + this.name + "\n";
    	data += "\tUID: " + this.UID + "\n";
    	data += "\tlatitude: " + this.latitude + "\n";
    	data += "\tlongitude: " + this.longitude + "\n";
    	data += "\ttype: " + this.POItype + "\n";
    	for (String key : attributes.keySet())
    	{
    		data += "\tattributes(" + key + "): " + attributes.get(key) + "\n";
    	}
    	return data;
    }
}
