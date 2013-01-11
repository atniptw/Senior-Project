package edu.rosehulman.androidmovingmap.test;

import android.test.ActivityInstrumentationTestCase2;
import edu.rosehulman.androidmovingmap.MainActivity;

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private MainActivity mActivity;

	public MainActivityTest() {
		super("edu.rosehulman.androidmovingmap", MainActivity.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		/*
		 * prepare to send key events to the app under test by turning off touch
		 * mode. Must be done before the first call to getActivity()
		 */

		setActivityInitialTouchMode(false);

		/*
		 * Start the app under test by starting its main activity. The test
		 * runner already knows which activity this is from the call to the
		 * super constructor, as mentioned previously. The tests can now use
		 * instrumentation to directly access the main activity through
		 * mActivity.
		 */
		mActivity = getActivity();
	}

}
