package com.freeankit.freenotepad.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * @author Ankit Kumar on 27/07/2018
 */
public class DataHolder {
    private static final String PREF_FILE_CONFIG = "PREF_FILE_CONFIG";
    private static final String EXTRA_KEY_USER = "EXTRA_KEY_USER";

    private String uid;
    private boolean isVerified;

    private DataHolder() {
        uid = "";

    }

    private static DataHolder dataHolder = null;

    synchronized public static DataHolder getInstance(Context context) {
        if (dataHolder == null) {

            String configJson = context.getSharedPreferences(PREF_FILE_CONFIG, Context.MODE_PRIVATE)
                    .getString(EXTRA_KEY_USER, null);
            try {
                dataHolder = new Gson().fromJson(configJson, DataHolder.class);
            } catch (JsonSyntaxException e) {
                e.getStackTrace();
            }

            if (dataHolder == null) {
                dataHolder = new DataHolder();
            }
        }
        return dataHolder;
    }

    public void save(Context context) {
        String configJson = new Gson().toJson(this);
        context.getSharedPreferences(PREF_FILE_CONFIG, Context.MODE_PRIVATE)
                .edit().putString(EXTRA_KEY_USER, configJson).apply();
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
