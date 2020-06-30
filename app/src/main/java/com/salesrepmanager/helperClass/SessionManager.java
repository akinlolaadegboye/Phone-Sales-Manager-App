package com.salesrepmanager.helperClass;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SessionManager {
	private static String TAG = SessionManager.class.getSimpleName();
	private SharedPreferences pref;
	private static Editor editor;
	private Context _context;
	private int PRIVATE_MODE = 0;
	private static final String PREF_NAME = "SalesManager";
	private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	public static void setLogin(boolean isLoggedIn) {
		editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
		editor.commit();
		Log.d(TAG, "User login session modified!");
	}
	public boolean isLoggedIn(){
		return pref.getBoolean(KEY_IS_LOGGED_IN, false);
	}
}
