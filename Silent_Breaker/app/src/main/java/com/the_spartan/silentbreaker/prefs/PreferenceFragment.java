package com.the_spartan.silentbreaker.prefs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;
import com.the_spartan.silentbreaker.MainActivity;
import com.the_spartan.silentbreaker.R;

import java.util.Objects;

import static com.the_spartan.silentbreaker.R.id.mode_switch;

/**
 * Created by the_spartan on 2/13/18.
 */

public class PreferenceFragment extends PreferenceFragmentCompat {

    private NotificationManager notificationManager;
    private SwitchPreference switchPreference;
    private final String switchPreferenceKey = "silent_breaker_switch";
    private final String permissionsPreferenceKey = "permissions";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("CHECK", "onCreate");

    }

    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

//        if (view != null){
//            findPreference("silent_breaker_switch").performClick();
//            switchPreference.setChecked(false);
//        }

        return view;
    }

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

//        Preference numberOfCallsPreference = findPreference("number_of_calls_editText");
//        numberOfCallsPreference.setSummary(preferences.getString("number_of_calls_editText", "3"));

        Log.v("CHECK", "onCreatePREFERENCE");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            notificationManager = (NotificationManager) Objects.requireNonNull(getContext()).getSystemService(Context.NOTIFICATION_SERVICE);

            if (isPermissionGranted()){
                findPreference(switchPreferenceKey).setEnabled(true);
                findPreference(permissionsPreferenceKey).setVisible(false);
            }
        }


        findPreference(switchPreferenceKey).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                if ((boolean)newValue){
                    Toast.makeText(getContext(), "Silent Breaker Mode ON", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getContext(), "Silent Breaker Mode OFF", Toast.LENGTH_SHORT)
                            .show();
                }

                return true;
            }
        });

        findPreference(permissionsPreferenceKey).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions();
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED && notificationManager.isNotificationPolicyAccessGranted()){
                        findPreference(switchPreferenceKey).setEnabled(true);
                    }
                }

                return true;
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission granted, need to work here for breaking silent mode

                    Toast.makeText(getContext(), "Granted", Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied, show another warning dialog, for now...

                    Toast.makeText(getContext(), "Denied", Toast.LENGTH_SHORT).show();
                }

                return;
        }
    }

    private boolean isPermissionGranted(){
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                && notificationManager.isNotificationPolicyAccessGranted()){
            return true;
        } else {
            return false;
        }
    }

    public void requestPermissions(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (notificationManager == null)
                notificationManager = (NotificationManager) Objects.requireNonNull(getContext()).getSystemService(Context.NOTIFICATION_SERVICE);

            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(Objects.requireNonNull(getActivity()), Manifest.permission.READ_PHONE_STATE)){
                    // some codes to write here to show an explanation to the user why this permission is necessary
                } else {

                    Log.d("PreferenceFragment", "requesting for permissions");
                    ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                }
            } else {
                // permission is already granted, nothing to do

                Log.d("PreferenceFragment", "permissions are already given");
            }

            if (notificationManager != null && !notificationManager.isNotificationPolicyAccessGranted()) {
                Log.d("PreferenceFragment", "requesting for do not disturb");
                requestForDoNotDisturb();
            }
        }
    }

    private void requestForDoNotDisturb(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext())
                .setTitle("Do Not Disturb")
                .setMessage("Please give this app the permission to change ringer mode even on do not disturb mode to work properly.")
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                        startActivityForResult(intent, 0);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog.show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (notificationManager.isNotificationPolicyAccessGranted() &&
                        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
                    //warn the user here...
                    findPreference(switchPreferenceKey).setEnabled(true);
                    findPreference(permissionsPreferenceKey).setVisible(false);
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setTitle("Warning!")
                            .setMessage("Proper permissions are not given!\nApp won't work.To give permissions, tap on permissions.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }
        }
    }


}
