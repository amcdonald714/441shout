package in.kubryant.shout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;


public class SettingsActivity extends ActionBarActivity {
    private static final String PREFERENCE_GENERAL = "preference_category_general";
    private static final String PREFERENCE_SYNC = "preference_category_sync";
    private static final String PREFERENCE_NOTIFICATION = "preference_category_notification";
    private static final String SHARED_PREF = "shared_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment fragment = new ApplicationPreferencesFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }

    public static class ApplicationPreferencesFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref_headers);

            this.findPreference(PREFERENCE_GENERAL)
                    .setOnPreferenceClickListener(new PreferenceClickListener(PREFERENCE_GENERAL));
//            this.findPreference(PREFERENCE_SYNC)
//                    .setOnPreferenceClickListener(new PreferenceClickListener(PREFERENCE_SYNC));
//            this.findPreference(PREFERENCE_NOTIFICATION)
//                    .setOnPreferenceClickListener(new PreferenceClickListener(PREFERENCE_NOTIFICATION));
        }

        private class PreferenceClickListener implements Preference.OnPreferenceClickListener {
            private String category;

            public PreferenceClickListener(String category) {
                this.category = category;
            }

            @Override
            public boolean onPreferenceClick(Preference preference) {
                Fragment fragment;

                switch(category) {
                    case PREFERENCE_GENERAL:
                        fragment = new GeneralPreferencesFragment();
                        break;
                    case PREFERENCE_SYNC:
                        fragment = new ListenPreferencesFragment();
                        break;
                    case PREFERENCE_NOTIFICATION:
                        fragment = new NotificationPreferencesFragment();
                        break;
                    default:
                        throw new AssertionError();
                }

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(android.R.id.content, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                return true;
            }
        }
    }

    public static class GeneralPreferencesFragment extends PreferenceFragment
            implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
        }

        @Override
        public void onResume() {
            super.onResume();
            ((SettingsActivity) getActivity()).getSupportActionBar().setTitle(R.string.pref_header_general);
            ((SettingsActivity) getActivity()).getSupportActionBar().setElevation(0);
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
            super.onPause();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            ((SettingsActivity) getActivity()).getSupportActionBar().setTitle("Setting");
            ((SettingsActivity) getActivity()).getSupportActionBar().setElevation(0);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        }
    }

    public static class ListenPreferencesFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_sync);
        }

        @Override
        public void onResume() {
            super.onResume();
            ((SettingsActivity) getActivity()).getSupportActionBar().setTitle(R.string.pref_header_sync);
            ((SettingsActivity) getActivity()).getSupportActionBar().setElevation(0);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            ((SettingsActivity) getActivity()).getSupportActionBar().setTitle("Setting");
            ((SettingsActivity) getActivity()).getSupportActionBar().setElevation(0);
        }
    }

    public static class NotificationPreferencesFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
        }

        @Override
        public void onResume() {
            super.onResume();
            ((SettingsActivity) getActivity()).getSupportActionBar().setTitle(R.string.pref_header_notifications);
            ((SettingsActivity) getActivity()).getSupportActionBar().setElevation(0);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            ((SettingsActivity) getActivity()).getSupportActionBar().setTitle("Setting");
            ((SettingsActivity) getActivity()).getSupportActionBar().setElevation(0);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            finish();
        }
        return true;
    }
}
