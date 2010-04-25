package org.telekommunisten;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceChangeListener;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity {

    private SharedPreferences prefs;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        

        EditTextPreference path = (EditTextPreference) findPreference("dialstation_user_path");
        EditTextPreference user = (EditTextPreference) findPreference("dialstation_user");
        EditTextPreference pass = (EditTextPreference) findPreference("dialstation_user_password");
        path.setSummary(prefs.getString("dialstation_user_path", "0"));
        user.setSummary(prefs.getString("dialstation_user", "0"));

        user.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object user) {
            	preference.setSummary((String) user);
            	prefs.edit().putString("dialstation_user", (String) user).commit();
            	return validateAccess(""+user, prefs.getString("dialstation_user_password", null));
            }
        });
        pass.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object pass) {
                preference.setSummary((String) pass);
                prefs.edit().putString("dialstation_user_pasword", (String) pass).commit();
                return validateAccess(prefs.getString("dialstation_user", null), ""+pass);
            }
        });

    }
    public boolean validateAccess(String user, String pass){

    	// NOT VALID - no values within the prefmanager settings
    	if (user == null || pass == null) {
    		Toast.makeText(this, "NOT VALID - no values within the prefmanager settings", Toast.LENGTH_LONG).show();
    		return false;
    	}
    	
    	DefaultHttpClient httpClient = new DefaultHttpClient();
    	
    	httpClient.getCredentialsProvider().setCredentials(
				new AuthScope(null, -1),
				new UsernamePasswordCredentials(user,pass));
		String whoami = null;
		try {
			whoami = new BufferedReader(
					new InputStreamReader(
							httpClient.execute(
									new HttpGet(DialstationProvider.DialstationUrl+"/whoami")
									).getEntity().getContent())).readLine();
			
			
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// NOT VALID - dialstation-server-capability answers with a access violation
		
		if (whoami.contains("Access denied")) {
			Toast.makeText(this, "NOT VALID - dialstation-server-capability answers with a access violation", Toast.LENGTH_LONG).show();
			return false;
		}
		Toast.makeText(this, "drinne!  :-)", Toast.LENGTH_LONG).show()
		;
		prefs.edit().putString("dialstation_user_path", whoami).commit();
		return true;
    }

}
