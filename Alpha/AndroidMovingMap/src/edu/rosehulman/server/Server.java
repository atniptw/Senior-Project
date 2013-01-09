package edu.rosehulman.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
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


import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

public class Server {
	private static String connect_to = "192.168.1.106";	

	public static Map<Integer,POI> POIelements;
	public static String message;
	public static String file;
	
	public Server()
	{
		if (Server.POIelements == null)
		{
			Server.POIelements = new HashMap<Integer,POI>();
		}
	}

	public static boolean updatePOIFromString(String data)
	{
		try {
			JSONObject text = new JSONObject(data);
			JSONObject POIelements = text.getJSONObject("POI");
			Log.d("size of POIelements:", "" + POIelements.length());
	
			JSONArray names = POIelements.names();
			Log.d("POIelements names", "" + names);
			
			for (int i = 0; i < POIelements.length(); i++)
			{
				String UIDstring = names.getString(i);
	
//				Log.d("POIelement name", "" + UIDstring);
				int UIDint = Integer.parseInt(UIDstring);
	
				JSONObject POIelement = POIelements.getJSONObject(UIDstring);
//				Log.d("element " + UIDstring, "" + POIelement);
			
				POI point = new POI(POIelement);
				Server.POIelements.remove(UIDint);
				Server.POIelements.put(UIDint, point);

			}
			return true;
		} catch (JSONException e) {
			Log.d("Server updatePOI", "malformed or bad data:" + data);
			return false;
		}
	}
	
	
	public class GetFiles extends AsyncTask<String, Void, String> {
		protected void onPostExecute(String files) {
			message = "File(s) fetched:\n" + files;
		}

		protected String doInBackground(String... urls) {
			String files = "";
			InputStream in = null;

			try {            
				for (String url : urls)
				{
					files += "<" + url + "> ";
					Log.d("filename", "about to fetch file " + url);

					URL u = new URL(url);
		            HttpURLConnection c = (HttpURLConnection) u.openConnection();
		            c.setRequestMethod("GET");
		            c.setDoOutput(true);

		            c.connect();

					in = c.getInputStream();
					
					File folder = new File(Environment.getExternalStorageDirectory() + "/OSMDroid/tiles/Mapnik/0/0");
					if (!folder.exists()) {
					    folder.mkdirs();
					}
					String PATH_op = Environment.getExternalStorageDirectory() + "/OSMDroid/tiles/Mapnik/0/0/0.png.tile";

					Log.d("path", "path: " + PATH_op);

		            FileOutputStream f = new FileOutputStream(new File(PATH_op));

		            long size = 0;
		            byte[] buffer = new byte[1024];
		            int len1 = 0;
		            while ( (len1 = in.read(buffer)) > 0 ) {
		                f.write(buffer,0, len1);
		                size += len1;
		            }
					Log.d("filesize", "file size: " + size);

		            f.close();
					in.close();
				}
			}
			catch (Exception e1) {}
			finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception e2) {}
				}
			}
			return files;
		}
	}

	
	public class GetUrlDataTask extends AsyncTask<String, Void, String> {
		protected void onPostExecute(String data) {
			message = "Message:\n" + data;

			Server.updatePOIFromString(data);
		}

		protected String doInBackground(String... urls) {
			String page = "";
			BufferedReader in = null;

			try {
				HttpClient client = new DefaultHttpClient();
				client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
						"android");
				HttpGet request = new HttpGet();
				request.setHeader("Content-Type", "text/plain; charset=utf-8");
				request.setURI(new URI(urls[0]));

				Log.d("response code", "about to get it");
				HttpResponse response = client.execute(request);
				Log.d("response code", "got response");

				in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();

				page = sb.toString();

			}
			catch (Exception e1) {}
			finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception e2) {}
				}
			}
			return page;
		}
	}

	
	public class ListenPOISocket extends Thread {
		private Socket socket;
	   	private ObjectInputStream in;
	  	private ObjectOutputStream out;
	  	public Handler updatePOIHandler;
	  	public volatile boolean stopThread = false;
	  	
	  	void sendMessage(String msg)
	  	{
	  		try{
	  			out.writeObject(msg);
	  			Log.d("POI socket", "sending: |" + msg + "|");
	  		}
	  		catch(Exception e){
	  			e.printStackTrace();
	  		}
	  	}

	  	public void run() {
	  		try{
	  			//open a socket connecting us to the server
	  			socket = new Socket(connect_to, 5047);
	  			Log.d("POI socket", "Connected!");

	  			//create in and out streams
	  			out = new ObjectOutputStream(socket.getOutputStream());
	  			in = new ObjectInputStream(socket.getInputStream());
	
	  			//send (and receive from server)
  				try{
  					sendMessage("hello\n");
  					message = (String)in.readObject();
  					if (!message.equals("ACKhello\n"))
  						Log.d("POI socket", "server did not ackHello instead recieved|" + message + "|");
  					else
  						Log.d("POI socket", "server replied ackHello");

  					while (!this.stopThread)
  					{
  	  					message = (String)in.readObject();
  						Log.d("POI socket",  "server says: omitted"); //" + message);
  						if (Server.updatePOIFromString(message))
  						{
  							Log.d("POI socket", "dispatching updateDisplay handler");
  							updatePOIHandler.sendMessage(updatePOIHandler.obtainMessage());
  						}
  						Thread.sleep(50);
  					}
				}
  				catch(ClassNotFoundException classNot){
  					System.err.println("data received in unknown format");
  				}
	  		}
	  		catch(Exception e){
	  			e.printStackTrace();
	  		}
	  		finally{
	  			try{
	  				in.close();
	  				out.close();
	  				socket.close();
	  			}
	  			catch(Exception e){
	  				e.printStackTrace();
	  			}
	  		}
		}
	};
}
