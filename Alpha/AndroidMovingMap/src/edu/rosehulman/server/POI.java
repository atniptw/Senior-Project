package edu.rosehulman.server;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.util.GeoPoint;

public class POI {
    private final int UID;
    private String name;
    private double latitude;
    private double longitude;
    private int POItype;
    private Map<String,String> attributes;
    
    public POI(JSONObject data) throws JSONException
    {
    		this(data.getInt("UID"), data.getString("name"),
    			data.getDouble("latitude"), data.getDouble("longitude"),
    			data.getInt("type"));
   	}

	public POI(int UID, String name, double latitude, double longitude, int POItype)
    {
    	this.UID = UID;
    	this.name = name;
    	this.latitude = latitude;
    	this.longitude = longitude;
    	this.POItype = POItype;
    }

	public int getUID()
	{
		return this.UID;
	}
	
	public GeoPoint getGeoPoint()
	{
		return new GeoPoint(this.latitude, this.longitude);
	}
		
    public String toString()
    {
    	String data = "POI:\n";
    	data += "\tname: " + this.name + "\n";
    	data += "\tUID: " + this.UID + "\n";
    	data += "\tlatitude: " + this.latitude + "\n";
    	data += "\tlongitude: " + this.longitude + "\n";
    	data += "\ttype: " + this.POItype + "\n";

    	return data;
    }
}
