package inc.mesa.mesanews.signup;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.navigation.Navigation;
import inc.mesa.mesanews.R;
import inc.mesa.mesanews.auth.AuthManager;

public class SignUpFragment extends Fragment implements SignUpContract.View, View.OnClickListener {

    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btSignUp;

    private SignUpContract.Presenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_sign_up, container, false);

        // Create the presenter
        presenter = new SignUpPresenter(AuthManager.getInstance(getContext()),
                                        this);

        etName = root.findViewById(R.id.et_name);
        etEmail = root.findViewById(R.id.et_email);
        etPassword = root.findViewById(R.id.et_password);
        etConfirmPassword = root.findViewById(R.id.et_confirm_password);

        btSignUp = root.findViewById(R.id.bt_sign_up);
        btSignUp.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(final View v) {
        if (v.getId() == R.id.bt_sign_up) {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String passwordConfirmation = etConfirmPassword.getText().toString().trim();

            presenter.signUp(name, email, password, passwordConfirmation);
        }
    }

    @Override
    public void showHomeScreen() {
        Navigation.findNavController(btSignUp)
                .navigate(R.id.action_display_home);
    }
}
