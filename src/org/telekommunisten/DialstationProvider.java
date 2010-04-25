package org.telekommunisten;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.AbstractCursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import novoda.rest.RESTProvider;
import novoda.rest.cursors.json.JsonCursor;



public class DialstationProvider extends RESTProvider {
	public static String DialstationUrl = "http://10.0.2.2:3000";
	private static String username = "t";
	private static String password = "123";
	private static Boolean readytogo = false;
	 // availible via DialstationUrl/whoami.json 
	private String tag = "dialstation";
	@Override
	
	public boolean onCreate() {
		
		return super.onCreate();
	}
	
	@Override
	public HttpUriRequest queryRequest(Uri resourcename, String[] projection, String arg2,
			String[] arg3, String arg4) {

		
		Log.d(tag , "sind wir drin? !!!!!!!!!!!!!!!!!!!!!");
		
		SharedPreferences pm = PreferenceManager.getDefaultSharedPreferences(getContext());

		httpClient.getCredentialsProvider().setCredentials(
				new AuthScope(null, -1),
				new UsernamePasswordCredentials(pm.getString("dialstation_user", ""), pm.getString("dialstation_user_password", "")));
		Log.d(tag,"USER+PASS:"+pm.getString("dialstation_user", "")+pm.getString("dialstation_user_password", ""));
		
		HttpGet get = new HttpGet(pm.getString("dialstation_user_path", "")+"/pdns.json");
		
		
		try {
			Log.d(TAG, "sind wir draussen? !!!!!!!!!!!!!!!!!!!!!" +  new BufferedReader(new InputStreamReader(httpClient.execute(get).getEntity().getContent())).readLine());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return get;
	}
	@Override
	public HttpUriRequest deleteRequest(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseHandler<? extends Integer> getDeleteHandler(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseHandler<? extends Uri> getInsertHandler(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseHandler<? extends AbstractCursor> getQueryHandler(Uri arg0) {
		// TODO Auto-generated method stub
		
		return new JsonCursor();
	}

	@Override
	public ResponseHandler<? extends Integer> getUpdateHandler(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpUriRequest insertRequest(Uri arg0, ContentValues arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	

	@Override
	public HttpUriRequest updateRequest(Uri arg0, ContentValues arg1,
			String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
