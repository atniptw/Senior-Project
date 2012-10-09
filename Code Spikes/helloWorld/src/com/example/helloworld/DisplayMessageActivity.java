package com.example.helloworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class DisplayMessageActivity extends Activity {
	public final static String BASE_URL = "http://137.112.156.211";
	public static TextView tView;

	private class GetUrlDataTask extends AsyncTask<String, Void, String> {
		protected void onPostExecute(String data) {
			tView.setText("Message:\n" + data);
		}

		protected String doInBackground(String... urls) {
			String page = "u: " + urls[0] + "\n";
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
				Log.d("response code", "got request");

				in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();

				page += "data: " + sb.toString() + "\n";

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

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_message);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		// Get the message from the Intent
		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

		// Create the text view
		this.tView = new TextView(this);
		this.tView.setTextSize(20);
		this.tView.setText("Waiting for data!");

		new GetUrlDataTask().execute(BASE_URL + "/~king/testMessage/" + message);

		// Set the text view as the activity layout
		setContentView(this.tView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_display_message, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
