package org.telekommunisten;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListPdn extends ListActivity {
	    
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        final Cursor cursor = getContentResolver().query(Uri.parse("content://com.dialstation"), new String[] {"pdns"},null,null,null);
	        
	        setListAdapter(new BaseAdapter() {
				
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					if (convertView == null) {
						convertView = new TextView(ListPdn.this);						
					} 
					cursor.moveToPosition(position);
					((TextView) convertView).setText(cursor.getString(0));
					return convertView;
				}
				
				@Override
				public long getItemId(int position) {
					// TODO Auto-generated method stub
					return 0;
				}
				
				@Override
				public Object getItem(int position) {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public int getCount() {
					// TODO Auto-generated method stub
					return cursor.getCount();
				}
			});
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
	
	