// released by t, flo, biafra into the public domain
package org.telekommunisten;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListPdn extends ListActivity {

	private static Cursor cursor;
	private String force = "";
	private String tag = "ListPdn";
	

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);   
		Intent i = getIntent();
		Log.d(tag,"data "+i.getDataString());
		
		if (getIntent().getData() != null) {
		    Cursor c = getContentResolver().query(getIntent().getData(), new String[]{Data._ID, Data.DATA1}, null, null, null);
		    c.moveToFirst();
		    if (!c.getString(1).equals("0"))
		        startActivity(new Intent(Intent.ACTION_CALL,  Uri.parse("tel:+" + c.getString(1))));
		    else
		        ProgressDialog.show(this, "checkking out tarifs..", "and associating psdn number \n \n ToDo :-)");
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int pos, long id) {
		PDN pdn = (PDN) l.getItemAtPosition(pos);  
		startActivity(new Intent(Intent.ACTION_CALL,  Uri.parse("tel:+" + pdn.getPstn() )));
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.d(tag,"onstart");
		super.onStart();
		
		if (PreferenceManager.getDefaultSharedPreferences(this).getString("dialstation_user_path", null) == null)
		{
			startActivity(new Intent(this,SettingsActivity.class));

			Toast.makeText(this, "enter credentials"+force, Toast.LENGTH_LONG).show();
			force += "!!!";
			if (force.contains("!!!!!!!!!!!!")) force = " AND DESTROY YOUR PHONE!";
		}
		else {
			Log.d(tag,"really provider called");
			if (cursor == null) {
				cursor = getContentResolver().query(Uri.parse("content://com.dialstation"), null, null, null, null);
				Log.d(tag,"no cursor found. loading data");
			}
			//Log.d(tag,"cols.....: "+cursor.getColumnCount());
			

			setListAdapter(new BaseAdapter() {

				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					if (convertView == null) {
						convertView = getLayoutInflater().inflate(R.layout.pdn, null);				
					} 

					cursor.moveToPosition(position);
					((TextView) convertView.findViewById(R.id.pstn)).setText(cursor.getString(cursor.getColumnIndex("pstn_number")));
					((TextView) convertView.findViewById(R.id.description)).setText(cursor.getString(cursor.getColumnIndex("description")));
					((TextView) convertView.findViewById(R.id.destination)).setText(cursor.getString(cursor.getColumnIndex("destination")));

					((TextView) convertView.findViewById(R.id.pstn)).setTextColor(Color.GREEN);
					((TextView) convertView.findViewById(R.id.description)).setTextColor(Color.RED);
					//						((TextView) convertView).setTextSize(42);
					//						((TextView) convertView).setTextColor(Color.GREEN);
					return convertView;
				}

				@Override
				public long getItemId(int position) {
					cursor.moveToPosition(position);
					return Long.valueOf(cursor.getString(cursor.getColumnIndex("id")));

				}

				@Override
				public Object getItem(int position) {
					cursor.moveToPosition(position);
					PDN pdn = new PDN(
							cursor.getString(cursor.getColumnIndex("pstn_number")),
							cursor.getString(cursor.getColumnIndex("description"))
					);
					return pdn;
				}

				@Override
				public int getCount() {
					Log.d("ListPdn",cursor.getCount()+"<- cursor.length");
					return cursor.getCount();
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.about:
			startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://dialstation.com/FAQ_en")));
			break;

		case R.id.settings:
			startActivity(new Intent(this, SettingsActivity.class));
			break;
		case R.id.feedback:
			LogCollector.feedback(this, getString(R.string.feedback_email), getString(R.string.feedback_subject));
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}


