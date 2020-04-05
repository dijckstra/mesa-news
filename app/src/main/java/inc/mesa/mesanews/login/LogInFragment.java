package inc.mesa.mesanews.login;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.navigation.Navigation;
import inc.mesa.mesanews.R;
import inc.mesa.mesanews.auth.AuthManager;

public class LogInFragment extends Fragment implements LogInContract.View, View.OnClickListener {

    private static final String TAG = "LogInFragment";

    private EditText etEmail;
    private EditText etPassword;
    private Button btLogIn;

    private LogInContract.Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_log_in, container, false);

        // Create the presenter
        presenter = new LogInPresenter(AuthManager.getInstance(getContext()),
                                       this);

        etEmail = root.findViewById(R.id.et_email);
        etPassword = root.findViewById(R.id.et_password);

        btLogIn = root.findViewById(R.id.bt_log_in);
        btLogIn.setOnClickListener(this);

        return root;
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
