package com.thoughtworks.readit.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class NetworkService {

	private NetworkService() {}
	
	public static boolean isNetWorkAvailable(final Context contextActivity) {
		ConnectivityManager conMgr = (ConnectivityManager) contextActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnected();
	}
}
