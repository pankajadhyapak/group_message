package students;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.w3effects.groupmess.groupmess.R;

/**
 * Created by pankaj on 04/10/14.
 */
public class StudentPreferenceActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Add General preferences
        addPreferencesFromResource(R.xml.student_pref);
        //addPreferencesFromResource(R.xml.pref_notification);
        String[] semesters = new String[6];
        semesters[0] = "First";
        semesters[1] = "Second";
        semesters[2] = "Third";
        semesters[3] = "Fourth";
        semesters[4] = "Fifth";
        semesters[5] = "Sixth";
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String currentSemester = prefs.getString("currentSemester", "0");
        Preference connectionPref = findPreference("currentSemester");
        connectionPref.setSummary(semesters[Integer.valueOf(currentSemester)] + " Semester ");

    }
    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals("currentSemester")) {

            Preference connectionPref = findPreference(key);
            ParseUser currentUser = ParseUser.getCurrentUser();
            if(currentUser != null){
                currentUser.put("semester",Integer.valueOf(sharedPreferences.getString(key, "")));
                ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                installation.put("semester", Integer.valueOf(sharedPreferences.getString(key, "")));
                installation.saveInBackground();
                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){

                            Toast.makeText(StudentPreferenceActivity.this,"Settings Updated Successfully",Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(StudentPreferenceActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            String[] semesters = new String[6];
            semesters[0] = "First";
            semesters[1] = "Second";
            semesters[2] = "Third";
            semesters[3] = "Fourth";
            semesters[4] = "Fifth";
            semesters[5] = "Sixth";
            Log.e("sdsds", sharedPreferences.getString(key +"Semester", ""));

            // Set summary to be the user-description for the selected value
            connectionPref.setSummary(semesters[Integer.valueOf(sharedPreferences.getString(key, ""))] + " Semester ");
        }
    }
}
