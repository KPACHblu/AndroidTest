package com.example.android.displayingbitmaps.tests;

import com.example.android.displayingbitmaps.ui.ImageGridActivity;

import android.test.ActivityInstrumentationTestCase2;

/**
* Tests for DisplayingBitmaps sample.
*/
public class SampleTests extends ActivityInstrumentationTestCase2<ImageGridActivity> {

    private ImageGridActivity mTestActivity;

    public SampleTests() {
        super(ImageGridActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Starts the activity under test using the default Intent with:
        // action = {@link Intent#ACTION_MAIN}
        // flags = {@link Intent#FLAG_ACTIVITY_NEW_TASK}
        // All other fields are null or empty.
        mTestActivity = getActivity();
    }

    /**
    * Test if the test fixture has been set up correctly.
    */
    public void testPreconditions() {
        //Try to add a message to add context to your assertions. These messages will be shown if
        //a tests fails and make it easy to understand why a test failed
        assertNotNull("mTestActivity is null", mTestActivity);
    }

    /**
    * Add more tests below.
    */

}
