package edu.rosehulman.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.rosehulman.server.POI;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

@SuppressLint("UseSparseArrays")
public class Server {
	private static Server instance = null;
	
	private final static String connect_to = "queen.wlan.rose-hulman.edu";
	private final static int connect_port = 5047;

	public Map<Integer,POI> POIelements = new HashMap<Integer,POI>();
  	public static Handler updatePOIHandler;

	private Server() { ; }

	public static Server getInstance()
	{
		if (Server.instance == null)
		{
			Server.instance = new Server();
		}
		return Server.instance;
	}
	
	public boolean updatePOIFromString(String data)
	{
		try {
			JSONObject text = new JSONObject(data);
			JSONObject POIelements = text.getJSONObject("POI");
	
			JSONArray names = POIelements.names();
			Log.d("POIelements", "size: " + POIelements.length() + " | names: " + names);
			
			for (int i = 0; i < POIelements.length(); i++)
			{
				String UIDstring = names.getString(i);
	
//				Log.d("POIelement", "name: " + UIDstring);
				int UIDint = Integer.parseInt(UIDstring);
	
				JSONObject POIelement = POIelements.getJSONObject(UIDstring);
//				Log.d("POIelement", "element: " + POIelement);
			
				POI point = new POI(POIelement);
				this.POIelements.remove(UIDint);
				this.POIelements.put(UIDint, point);
			}
			return true;
		} catch (JSONException e) {
			Log.d("Server updatePOI", "malformed or bad data:" + data);
			return false;
		}
	}
	
  	public void sendMessage(String msg) throws IOException
  	{
  		try
  		{
	  		while (ListenPOISocket.getInstance().acked != true)
	  		{
				Thread.sleep(50); // TODO FIXME locks up main thread???
	  		}	  			
  		} catch (InterruptedException e) {
		}
  		ListenPOISocket.getInstance().sendMessage(msg);
  	}

  	public void stopServer()
  	{
	  	if (ListenPOISocket.instance != null)
		{
	  		ListenPOISocket.instance.stopThread = true;
	  		ListenPOISocket.instance = null;
		}
  	}

  	public void startServer()
  	{
  		ListenPOISocket.getInstance();
  	}

  	public boolean serverRunning()
  	{
  		return ListenPOISocket.instance != null;
  	}
  	
  	protected static class ListenPOISocket extends Thread {
		private static ListenPOISocket instance = null;
		private Socket socket;
	   	private DataInputStream in;
        private DataOutputStream out;

        public volatile boolean acked = false;

        public volatile boolean stopThread = false;
	  	public volatile boolean pullFrom = false;
	  	public volatile boolean pushTo = false;
	  	
	  	
	  	private ListenPOISocket()
	  	{
	  	}

	  	public static ListenPOISocket getInstance()
	  	{
	  		if (ListenPOISocket.instance == null)
	  		{
		  		ListenPOISocket.instance = new ListenPOISocket();
		  		ListenPOISocket.instance.start();
	  		}
	  		return ListenPOISocket.instance;
	  	}

	  	public void sendMessage(String msg) throws IOException
	  	{  		
	  		Log.d("POI", "sending message: '" + msg + "'");
  			out.writeBytes(msg);
  			out.flush();
  			Log.d("POI", "sent");
	  	}

	  	private String getMessage() throws IOException
	  	{
			String msg = in.readLine();
			return msg;
	  	}

	  	public void run() {
	  		try{
	  			//open a socket connecting us to the server
	  			Log.d("POI", "Opening Socket");	  			
	  			socket = new Socket(connect_to, connect_port);
	  			Log.d("POI", "Socket Connected!");

	  			//create in and out streams
	            out = new DataOutputStream(socket.getOutputStream());
	            in = new DataInputStream(socket.getInputStream());

	  			sendMessage("hello");
				String message = getMessage();
				if (!message.equals("ACKhello"))
				{
					Log.d("POI", "socket server did not ackHello instead received |" + message + "|");
					return;
				}
				else
				{
					Log.d("POI", "socket server replied ackHello");
				}
		  		this.acked = true;
				
				while ( !this.stopThread )
				{
					Thread.sleep(50);

					Log.d("POI", "waiting on message");
					message = getMessage();
					Log.d("POI", "got message");
					if (message == null)
					{
						break;
					}
				    Log.d("POI",  "socket server says: omitted"); //" + message);
					if (Server.getInstance().updatePOIFromString(message))
					{
						Log.d("POI", "socket dispatching updateDisplay handler");
						Server.updatePOIHandler.sendMessage(Server.updatePOIHandler.obtainMessage());
					}
				};
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	  		finally
			{
				try {
					Log.d("POI", "in finally");
					if (socket != null)
						socket.close();
					if (in != null)
						in.close();
					if (out != null)
						out.close();
				} catch (IOException e) {}
			}
		}
	};
}
