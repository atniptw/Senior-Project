package edu.rosehulman.overlays;

import java.io.Serializable;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class AddPOITypeDialogFragment extends DialogFragment {

	private AddPOITypeDialogFragment() {}
	
	private AMMOverlayManager mOverlayManager;
	private Context mContext;
	
	public static AddPOITypeDialogFragment newInstance(AMMOverlayManager overlayManager, Context context) {
		AddPOITypeDialogFragment result = new AddPOITypeDialogFragment();
		Bundle args = new Bundle();
		args.putSerializable("overlayManager", (Serializable)overlayManager);
		args.putSerializable("context", (Serializable)context);
		result.setArguments(args);
		return result;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mOverlayManager = (AMMOverlayManager)getArguments().getSerializable("overlayManager");
		mContext = (Context)getArguments().getSerializable("context");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = new LinearLayout(mContext);
		LinearLayout ll = (LinearLayout) v;
		
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		final EditText editText = new EditText(mContext);
		editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		ll.addView(editText);
		
		Button button = new Button(mContext);
		button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		button.setText("Okay");
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final String name = editText.getText().toString();
				mOverlayManager.addCustomOverlay(name);
				dismiss();
			}
		});
		ll.addView(button);
		
		return v;
	}
}
