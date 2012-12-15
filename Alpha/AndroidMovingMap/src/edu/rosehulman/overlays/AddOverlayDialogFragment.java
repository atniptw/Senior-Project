package edu.rosehulman.overlays;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class AddOverlayDialogFragment extends DialogFragment {
	
	private AMMOverlayManager mOverlayManager;
	private ListView mOverlayListView;
	
	public AddOverlayDialogFragment(AMMOverlayManager manager, Context context) {
		this.mOverlayManager = manager;
		mOverlayListView = new ListView(context);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Dialog dialog = new Dialog(getActivity());
		
		dialog.setContentView(mOverlayListView);
		BaseAdapter adapter = new OverlayListAdapter(getActivity(), mOverlayManager);
		
		mOverlayListView.setAdapter(adapter);

		return dialog;
	}
}
