package inc.mesa.mesanews.login;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.google.common.hash.Hashing;

import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import inc.mesa.mesanews.auth.AuthManager;
import inc.mesa.mesanews.client.NewsClient;
import inc.mesa.mesanews.client.RetrofitManager;
import inc.mesa.mesanews.client.model.request.LogInRequest;
import inc.mesa.mesanews.client.model.request.SignUpRequest;
import inc.mesa.mesanews.client.model.response.AuthResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

public class LogInPresenter implements LogInContract.Presenter {

    private static final String TAG = "LogInPresenter";

    private AuthManager authManager;
    private NewsClient client;

    private LogInContract.View view;

    public LogInPresenter(final AuthManager authManager,
                          final RetrofitManager retrofitManager,
                          final LogInContract.View view) {
        this.view = view;
        this.authManager = authManager;

        this.client = retrofitManager.getClientInstance(null).create(NewsClient.class);
    }

    @Override
    public void logIn(final String email, final String password) {
        LogInRequest request = new LogInRequest.Builder()
                .email(email)
                .password(password)
                .build();

        Call<AuthResponse> call = client.signIn(request);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(final Call<AuthResponse> call,
                                   final Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse auth = response.body();
                    if (auth != null) {
                        String token = auth.getToken();
                        authManager.setAuthToken(token);
                        view.showHomeScreen();
                    }
                }
            }

            @Override
            public void onFailure(final Call<AuthResponse> call,
                                  final Throwable t) {
                Log.e(TAG, t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void logInWithFacebook(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(final JSONObject object, final GraphResponse response) {
                try {
                    if (response.getError() != null) {
                        Log.e(TAG, "onCompleted: " + response.getError().getErrorMessage());
                        return;
                    }

                    String name = object.getString("name");
                    String email = object.getString("email");

                    authenticate(accessToken.getUserId(), name, email);
                } catch (Exception e) {
                    Log.e(TAG, "onCompleted: " + e.getLocalizedMessage());
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email");
        request.setParameters(parameters);

        request.executeAsync();
    }

    private void authenticate(final String userId, final String name, final String email) {
        String hashedUserId = Hashing.sha256()
                .hashString(userId, StandardCharsets.UTF_8).toString();

        LogInRequest request = new LogInRequest.Builder()
                .email(email)
                .password(hashedUserId)
                .build();

        Call<AuthResponse> signInCall = client.signIn(request);
        signInCall.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(final Call<AuthResponse> call, final Response<AuthResponse> response) {
                if (response.code() == HTTP_UNAUTHORIZED) {
                    signUp(name, email, hashedUserId);
                    return;
                }

                AuthResponse auth = response.body();
                if (auth != null) {
                    String token = auth.getToken();
                    authManager.setAuthToken(token);
                    view.showHomeScreen();
                }
            }

            @Override
            public void onFailure(final Call<AuthResponse> call, final Throwable t) {
            }
        });
    }

    private void signUp(final String name, final String email, final String password) {
        SignUpRequest request = new SignUpRequest.Builder()
                .name(name)
                .email(email)
                .password(password)
                .build();

        Call<AuthResponse> signUpCall = client.signUp(request);
        signUpCall.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(final Call<AuthResponse> call, final Response<AuthResponse> response) {
                AuthResponse auth = response.body();
                if (auth != null) {
                    String token = auth.getToken();
                    authManager.setAuthToken(token);
                    view.showHomeScreen();
                }
            }

            @Override
            public void onFailure(final Call<AuthResponse> call, final Throwable t) {
                Log.e(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }
}
