package inc.mesa.mesanews.auth;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthManager  {

    private static final String SHARED_PREFS_KEY = "shared_prefs";
    private static final String AUTH_TOKEN = "auth_token";

    private SharedPreferences prefs;

    public AuthManager(final Context context) {
        prefs = context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE);
    }

    public String getAuthToken() {
        return prefs.getString(AUTH_TOKEN, null);
    }

    public void setAuthToken(final String token) {
        prefs.edit().putString(AUTH_TOKEN, token).apply();
    }
}
