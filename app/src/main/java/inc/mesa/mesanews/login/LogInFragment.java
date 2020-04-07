package inc.mesa.mesanews.login;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import inc.mesa.mesanews.R;
import inc.mesa.mesanews.dep.DependencyProvider;

public class LogInFragment extends Fragment implements LogInContract.View, View.OnClickListener {

    private static final String TAG = "LogInFragment";

    private EditText etEmail;
    private EditText etPassword;
    private Button btLogIn;

    private LoginButton btLoginFacebook;
    private CallbackManager callbackManager;

    private LogInContract.Presenter presenter;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_log_in, container, false);

        // Create the presenter
        presenter = new LogInPresenter(DependencyProvider.getAuthManager(),
                                       DependencyProvider.getRetrofitManager(),
                                       this);

        etEmail = root.findViewById(R.id.et_email);
        etPassword = root.findViewById(R.id.et_password);

        btLogIn = root.findViewById(R.id.bt_log_in);
        btLogIn.setOnClickListener(this);

        btLoginFacebook = root.findViewById(R.id.bt_login_facebook);
        btLoginFacebook.setPermissions("email");
        btLoginFacebook.setFragment(this);

        callbackManager = CallbackManager.Factory.create();
        btLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                presenter.logInWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");
            }

            @Override
            public void onError(final FacebookException error) {
                Log.d(TAG, "onError");
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showHomeScreen() {
        Navigation.findNavController(btLogIn)
                .navigate(R.id.action_display_home);
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.bt_log_in) {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            presenter.logIn(email, password);
        }
    }
}
