package edu.rosehulman.androidmovingmap;

import edu.rosehulman.overlays.AMMItemizedOverlay;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

public class OverlayTypeView extends LinearLayout implements Checkable {

	private ImageView mImageView;
	private CheckedTextView mTextView;
	private boolean isChecked;
	private AMMItemizedOverlay mOverlay;

	public OverlayTypeView(Context mContext, int mChoiceMode) {
		super(mContext);

		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.overlay_list_item, this);

		mImageView = (ImageView) findViewById(R.id.overlay_icon);
		mTextView = (CheckedTextView) findViewById(R.id.overlay_text);

		if (mChoiceMode == ListView.CHOICE_MODE_NONE) {
			mTextView.setCheckMarkDrawable(null);
		}
	}

	public OverlayTypeView(Context mContext, int mChoiceMode,
			AMMItemizedOverlay overlay) {
		super(mContext);

		LayoutInflater inflater = LayoutInflater.from(mContext);
		inflater.inflate(R.layout.overlay_list_item, this);

		mImageView = (ImageView) findViewById(R.id.overlay_icon);
		mTextView = (CheckedTextView) findViewById(R.id.overlay_text);

		setOverlay(overlay);

		if (mChoiceMode == ListView.CHOICE_MODE_NONE) {
			mTextView.setCheckMarkDrawable(null);
		}
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		isChecked = checked;
		if (mOverlay != null) {
			mOverlay.setActive(isChecked);
		}
		mTextView.setChecked(isChecked);
	}

	public void toggle() {
		isChecked = !isChecked;
		if (mOverlay != null) {
			mOverlay.setActive(isChecked);
		}
		mTextView.setChecked(isChecked);
	}

	public void setImage(Drawable icon) {
		mImageView.setImageDrawable(icon);
	}

	public void setText(String text) {
		mTextView.setText(text);
	}

	public void setOverlay(AMMItemizedOverlay overlay) {
		mOverlay = overlay;
		if (mOverlay.isActive()) {
			isChecked = true;
		} else {
			isChecked = false;
		}
		mTextView.setChecked(isChecked);
	}

	protected void onFinishInflate() {
		super.onFinishInflate();

	}
}