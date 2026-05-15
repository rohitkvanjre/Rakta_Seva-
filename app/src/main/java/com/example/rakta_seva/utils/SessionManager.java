package com.example.rakta_seva.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "rakta_seva_session";
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public static final String KEY_IS_LOGGED_IN = "is_logged_in";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_PHONE = "user_phone";
    public static final String KEY_USER_BLOOD_GROUP = "user_blood_group";
    public static final String KEY_USER_AGE = "user_age";
    public static final String KEY_USER_GENDER = "user_gender";
    public static final String KEY_USER_EMAIL = "user_email";
    public static final String KEY_USER_LOCATION = "user_location";
    public static final String KEY_LAST_DONATION = "last_donation";
    public static final String KEY_IS_VERIFIED = "is_verified";
    public static final String KEY_IS_AVAILABLE = "is_available";
    public static final String KEY_USER_TYPE = "user_type";

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean loggedIn) {
        editor.putBoolean(KEY_IS_LOGGED_IN, loggedIn);
        editor.apply();
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, "");
    }

    public void setUserName(String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    public String getUserPhone() {
        return prefs.getString(KEY_USER_PHONE, "");
    }

    public void setUserPhone(String phone) {
        editor.putString(KEY_USER_PHONE, phone);
        editor.apply();
    }

    public String getUserBloodGroup() {
        return prefs.getString(KEY_USER_BLOOD_GROUP, "");
    }

    public void setUserBloodGroup(String bloodGroup) {
        editor.putString(KEY_USER_BLOOD_GROUP, bloodGroup);
        editor.apply();
    }

    public int getUserAge() {
        return prefs.getInt(KEY_USER_AGE, 0);
    }

    public void setUserAge(int age) {
        editor.putInt(KEY_USER_AGE, age);
        editor.apply();
    }

    public String getUserGender() {
        return prefs.getString(KEY_USER_GENDER, "");
    }

    public void setUserGender(String gender) {
        editor.putString(KEY_USER_GENDER, gender);
        editor.apply();
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, "");
    }

    public void setUserEmail(String email) {
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    public String getUserLocation() {
        return prefs.getString(KEY_USER_LOCATION, "");
    }

    public void setUserLocation(String location) {
        editor.putString(KEY_USER_LOCATION, location);
        editor.apply();
    }

    public String getLastDonation() {
        return prefs.getString(KEY_LAST_DONATION, "");
    }

    public void setLastDonation(String date) {
        editor.putString(KEY_LAST_DONATION, date);
        editor.apply();
    }

    public boolean isVerified() {
        return prefs.getBoolean(KEY_IS_VERIFIED, false);
    }

    public void setVerified(boolean verified) {
        editor.putBoolean(KEY_IS_VERIFIED, verified);
        editor.apply();
    }

    public boolean isAvailable() {
        return prefs.getBoolean(KEY_IS_AVAILABLE, true);
    }

    public void setAvailable(boolean available) {
        editor.putBoolean(KEY_IS_AVAILABLE, available);
        editor.apply();
    }

    public String getUserType() {
        return prefs.getString(KEY_USER_TYPE, "both");
    }

    public void setUserType(String userType) {
        editor.putString(KEY_USER_TYPE, userType);
        editor.apply();
    }

    public void saveLoginSession(String name, String phone, String bloodGroup, int age,
                                  String gender, String email, String location,
                                  String lastDonation, boolean verified, boolean available) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_PHONE, phone);
        editor.putString(KEY_USER_BLOOD_GROUP, bloodGroup);
        editor.putInt(KEY_USER_AGE, age);
        editor.putString(KEY_USER_GENDER, gender);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_LOCATION, location);
        editor.putString(KEY_LAST_DONATION, lastDonation);
        editor.putBoolean(KEY_IS_VERIFIED, verified);
        editor.putBoolean(KEY_IS_AVAILABLE, available);
        editor.apply();
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}
