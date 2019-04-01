package com.the_spartan.silentbreaker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.CountDownTimer;
import android.support.v7.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by the_spartan on 2/13/18.
 */

public class PhoneStateReceiver extends BroadcastReceiver {
    public static int count = 0;
    private static int defaultMode;
    private static String lastState = TelephonyManager.EXTRA_STATE_IDLE;
    public static String firstNumber;
    public static String secondNumer;
    public static String thirdNumber;
    public static String fourthNumber;
    public static String fifthNumber;
    private static int firstNumberCount = 0;
    private static int secondNumberCount = 0;
    private static int thirdNumberCount = 0;
    private static int fourthNumberCount = 0;
    private static int fifthNumberCount = 0;
    AudioManager audioManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        Log.v("STATE", state);

        SharedPreferences preferences = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(context);
        boolean isSilentBreakerOn = preferences.getBoolean("silent_breaker_switch", true);
        if (isSilentBreakerOn) {
            Log.v("SWITCH", "ON");
            String numberOfCallsString = preferences.getString("number_of_calls_editText", "0");
            int numberOfCalls = Integer.parseInt(numberOfCallsString);

            String intervalString = preferences.getString("time_interval_editText", "0");
            int interval = Integer.parseInt(intervalString);

            String specialNumber = preferences.getString("special_number_editText", "-1");

            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            if (!state.equals(lastState)) {
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    defaultMode = audioManager.getRingerMode();
                    lastState = state;
                    if (intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).contains(specialNumber)) {
                        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    } else if (firstNumber == null) {
                        firstNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    //    count++;
                        firstNumberCount++;
                        Log.v("CHECK", "FIRSTNUMBERSET");
                        Log.v("CHECK", firstNumber);
                        Log.v("CHECK", "COUNT " + count);
                        new CountDownTimer(interval * 60 * 1000, 1000) {

                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                firstNumber = null;
                            //    count = 0;
                                firstNumberCount = 0;
                            }
                        }.start();
                    }else if (!intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).equals(firstNumber) && secondNumer == null) {
                        secondNumer = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                        //    count++;
                        secondNumberCount++;
                        Log.v("CHECK", "SECONDNUMBERSET");
                        Log.v("CHECK", firstNumber);
                        Log.v("CHECK", "COUNT " + count);
                        new CountDownTimer(interval * 60 * 1000, 1000) {

                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                secondNumer = null;
                                //    count = 0;
                                secondNumberCount = 0;
                            }
                        }.start();
                    }else if (!intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).equals(firstNumber)
                            && !intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).equals(secondNumer)
                            && thirdNumber == null) {
                        thirdNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                        //    count++;
                        thirdNumberCount++;
                        Log.v("CHECK", "THIRDNUMBERSET");
                        Log.v("CHECK", firstNumber);
                        Log.v("CHECK", "COUNT " + count);
                        new CountDownTimer(interval * 60 * 1000, 1000) {

                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                thirdNumber = null;
                                //    count = 0;
                                thirdNumberCount = 0;
                            }
                        }.start();
                    }else if (!intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).equals(firstNumber)
                            && !intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).equals(secondNumer)
                            && !intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).equals(thirdNumber)
                            && fourthNumber == null) {
                        fourthNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                        //    count++;
                        fourthNumberCount++;
                        Log.v("CHECK", "FOURTHNUMBERSET");
                        Log.v("CHECK", firstNumber);
                        Log.v("CHECK", "COUNT " + count);
                        new CountDownTimer(interval * 60 * 1000, 1000) {

                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                fourthNumber = null;
                                //    count = 0;
                                fourthNumberCount = 0;
                            }
                        }.start();
                    }else if (!intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).equals(firstNumber)
                            && !intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).equals(secondNumer)
                            && !intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).equals(thirdNumber)
                            && !intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).equals(fourthNumber)
                            && fifthNumber == null) {
                        fifthNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                        //    count++;
                        fifthNumberCount++;
                        Log.v("CHECK", "FIRSTNUMBERSET");
                        Log.v("CHECK", firstNumber);
                        Log.v("CHECK", "COUNT " + count);
                        new CountDownTimer(interval * 60 * 1000, 1000) {

                            @Override
                            public void onTick(long l) {

                            }

                            @Override
                            public void onFinish() {
                                fifthNumber = null;
                                //    count = 0;
                                fifthNumberCount = 0;
                            }
                        }.start();
                    } else if (intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).equals(firstNumber)) {
                        firstNumberCount++;
                        Log.v("CHECK", "COUNT " + count);
                        if (firstNumberCount >= numberOfCalls) {
                            Log.v("CHECK", "BreakingSILENT");
                            defaultMode = audioManager.getRingerMode();
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        }
                    }else if (intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).equals(secondNumer)) {
                        secondNumberCount++;
                        Log.v("CHECK", "COUNT " + count);
                        if (secondNumberCount >= numberOfCalls) {
                            Log.v("CHECK", "BreakingSILENT");
                            defaultMode = audioManager.getRingerMode();
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        }
                    }else if (intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).equals(thirdNumber)) {
                        thirdNumberCount++;
                        Log.v("CHECK", "COUNT " + count);
                        if (thirdNumberCount >= numberOfCalls) {
                            Log.v("CHECK", "BreakingSILENT");
                            defaultMode = audioManager.getRingerMode();
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        }
                    }else if (intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).equals(fourthNumber)) {
                        fourthNumberCount++;
                        Log.v("CHECK", "COUNT " + count);
                        if (fourthNumberCount >= numberOfCalls) {
                            Log.v("CHECK", "BreakingSILENT");
                            defaultMode = audioManager.getRingerMode();
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        }
                    }else if (intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER).equals(fifthNumber)) {
                        fifthNumberCount++;
                        Log.v("CHECK", "COUNT " + count);
                        if (fifthNumberCount >= numberOfCalls) {
                            Log.v("CHECK", "BreakingSILENT");
                            defaultMode = audioManager.getRingerMode();
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        }
                    }
                } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                    if (lastState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                        defaultMode = audioManager.getRingerMode();
                    }
                    lastState = state;
                } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                    lastState = state;
                    audioManager.setRingerMode(defaultMode);
                }
            }
            System.out.println("LAST STATE = " + defaultMode);
        }

    }
}
