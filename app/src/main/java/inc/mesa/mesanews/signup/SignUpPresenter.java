package inc.mesa.mesanews.signup;

import android.util.Log;

import inc.mesa.mesanews.auth.AuthManager;
import inc.mesa.mesanews.client.NewsClient;
import inc.mesa.mesanews.client.RetrofitManager;
import inc.mesa.mesanews.client.model.request.SignUpRequest;
import inc.mesa.mesanews.client.model.response.AuthResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpPresenter implements SignUpContract.Presenter {

    private static final String TAG = "SignUpPresenter";

    private AuthManager authManager;
    private NewsClient client;

    private SignUpContract.View view;

    public SignUpPresenter(final AuthManager authManager,
                           final SignUpContract.View view) {
        this.view = view;
        this.authManager = authManager;

        RetrofitManager manager = RetrofitManager.getInstance();
        this.client = manager.getClientInstance(null).create(NewsClient.class);

    }

    @Override
    public void signUp(final String name,
                       final String email,
                       final String password,
                       final String passwordConfirmation) {
        SignUpRequest request = new SignUpRequest.Builder()
                .name(name)
                .email(email)
                .password(password)
                .build();

        Call<AuthResponse> call = client.signUp(request);
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
