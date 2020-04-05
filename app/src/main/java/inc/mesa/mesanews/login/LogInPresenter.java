package inc.mesa.mesanews.login;

import android.util.Log;

import inc.mesa.mesanews.auth.AuthManager;
import inc.mesa.mesanews.client.NewsClient;
import inc.mesa.mesanews.client.RetrofitManager;
import inc.mesa.mesanews.client.model.request.LogInRequest;
import inc.mesa.mesanews.client.model.response.AuthResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInPresenter implements LogInContract.Presenter {

    private static final String TAG = "LogInPresenter";

    private AuthManager authManager;
    private NewsClient client;

    private LogInContract.View view;

    public LogInPresenter(final AuthManager authManager,
                          final LogInContract.View view) {
        this.view = view;
        this.authManager = authManager;

        RetrofitManager manager = RetrofitManager.getInstance();
        this.client = manager.getClientInstance(null).create(NewsClient.class);
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
}
