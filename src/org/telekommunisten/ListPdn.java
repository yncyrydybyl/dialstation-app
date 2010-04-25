package org.telekommunisten;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
	    
	    private Cursor cursor;
		private String force = "";

		/** Called when the activity is first created. */
	
		@Override
		public void onListItemClick(ListView l, View v, int pos,long id)
		{
			String pdntext = (String) l.getItemAtPosition(pos);
			Log.d("dialstation",pdntext);
		}
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	    	
	        super.onCreate(savedInstanceState);
	         
	        
	        
	    }

	    @Override
	    protected void onStart() {
	    	// TODO Auto-generated method stub
	    	super.onStart();
	    	if (PreferenceManager.getDefaultSharedPreferences(this).getString("dialstation_user_path", null) == null)
	    	{
	    		startActivity(new Intent(this,SettingsActivity.class));
	    		
	    		Toast.makeText(this, "enter credentials"+force, Toast.LENGTH_LONG).show();
	    		force += "!!!";
	    		if (force.contains("!!!!!!!!!!!!")) force = " AND DESTROY YOUR PHONE!";
	    	}
	    	else {
	    		Log.d("ds","really provider called");
		    	cursor = getContentResolver().query(Uri.parse("content://com.dialstation"), new String[] {"pdns"},null,null,null);
		    	Log.d("ds","cols: "+cursor.getColumnCount());

		    	setListAdapter(new BaseAdapter() {
					
					@Override
					public View getView(int position, View convertView, ViewGroup parent) {
						if (convertView == null) {
							convertView = new TextView(ListPdn.this);						
						} 
						
						cursor.moveToPosition(position);
						((TextView) convertView).setText(cursor.getString(1)+"\n"+cursor.getString(cursor.getColumnIndex("destination")));
						return convertView;
					}
					
					@Override
					public long getItemId(int position) {
						cursor.moveToPosition(position);
						return Long.valueOf(cursor.getString(cursor.getColumnIndex("id")));
	
						// TODO Auto-generated method stub
					}
					
					@Override
					public Object getItem(int position) {
						// TODO Auto-generated method stub
						cursor.moveToPosition(position);
						return cursor.getString(1)+"\n"+cursor.getString(cursor.getColumnIndex("destination"));
					}
					
					@Override
					public int getCount() {
						// TODO Auto-generated method stub
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

	        case R.id.search:
	            onSearchRequested();
	            break;

	        case R.id.settings:
	            startActivity(new Intent(this, SettingsActivity.class));
	            break;
	        case R.id.feedback:
	            LogCollector.feedback(this, "flo@andlabs.de", "blah blah blah");
	            break;
	        }
	        return super.onOptionsItemSelected(item);
	    }

	}
	
	
