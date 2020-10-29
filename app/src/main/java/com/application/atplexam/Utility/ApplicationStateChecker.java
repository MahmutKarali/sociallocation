package com.application.atplexam.Utility;

import android.app.Activity;

public class ApplicationStateChecker {
    private static final String _pause_string = "paused";
    private static final String _resume_string = "resumed";

    private static String _view_lastState;
    private static boolean _from_background = true;

    public static void view_paused(Activity activity) {
        _view_lastState = _pause_string;
    }

    public static void view_stopped(Activity activity) {

        if (_view_lastState.equals(_pause_string)) {
            //if stop called and last event was pause then app is brought to background
            _from_background = true;
        }  //if

    }

    public static void view_resumed(Activity activity) {

        if (_from_background) {
            //Do your stuff here , app is brought to foreground

        }  //if

        _from_background = false;
        _view_lastState = _resume_string;
    }

    public static boolean getAppIsBackground() {
        return _from_background;
    }
}
