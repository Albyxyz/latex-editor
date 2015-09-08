package me.albertonicoletti.latex;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;

/**
 * Settings fragment.
 *
 * @author Alberto Nicoletti    albyx.n@gmail.com    https://github.com/albyxyz
 */
public class SettingsFragment extends PreferenceFragment implements
                                            SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String IMAGES_FOLDER = "images_folder";
    public static final String OUTPUT_FOLDER = "output_folder";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * On resume it update every settings.
     */
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
            Preference preference = getPreferenceScreen().getPreference(i);
            if (preference instanceof PreferenceGroup) {
                PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
                for (int j = 0; j < preferenceGroup.getPreferenceCount(); ++j) {
                    updatePreference(preferenceGroup.getPreference(j));
                }
            } else {
                updatePreference(preference);
            }
        }

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    /**
     * Updates a given preference.
     * @param p Preference to update
     */
    private void updatePreference(Preference p) {
        if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
            p.setSummary(listPref.getEntry());
        }
        if (p instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            if (p.getTitle().toString().contains("password"))
            {
                p.setSummary("******");
            } else {
                p.setSummary(editTextPref.getText());
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key){
            case IMAGES_FOLDER:
                EditTextPreference imagesFolder = (EditTextPreference) findPreference(IMAGES_FOLDER);
                imagesFolder.setSummary(ensureFolderSlash(imagesFolder.getText()));
                break;
            case OUTPUT_FOLDER:
                EditTextPreference outputFolder = (EditTextPreference) findPreference(OUTPUT_FOLDER);
                outputFolder.setSummary(ensureFolderSlash(outputFolder.getText()));
                break;
        }
    }

    /**
     * Checks if the folder path finishes with /.
     * If not, it will add a final /.
     * @param folder Folder path
     * @return Correct folder path
     */
    private String ensureFolderSlash(String folder){
        if(!folder.endsWith("/")){
            folder += "/";
        }
        return folder;
    }

}