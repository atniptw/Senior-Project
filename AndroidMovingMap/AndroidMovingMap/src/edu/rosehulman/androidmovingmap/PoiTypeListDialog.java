package edu.rosehulman.androidmovingmap;

import java.io.Serializable;
import java.util.List;

import edu.rosehulman.overlays.OverlayIconRegistry;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class PoiTypeListDialog extends DialogFragment {

	private Context mContext;
	private List<String> mOverlayNames;
	private ListView mOverlayListView;
	
	public static PoiTypeListDialog newInstance(Context context) {
		PoiTypeListDialog result = new PoiTypeListDialog();
		Bundle args = new Bundle();
		args.putSerializable("context", (Serializable)context);
		result.setArguments(args);
		return result;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = (Context)getArguments().getSerializable("context");
		mOverlayNames = OverlayIconRegistry.getInstance().getRegisteredOverlays();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_poi_types, container);
		
		mOverlayListView = (ListView)view.findViewById(R.id.listView);
		
		BaseAdapter adapter = new OverlayListAdapter(mContext, mOverlayNames);
		
		mOverlayListView.setAdapter(adapter);
		
		return view;
	}
	
}
