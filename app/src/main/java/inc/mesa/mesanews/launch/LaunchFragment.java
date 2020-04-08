package inc.mesa.mesanews.splash;


import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.navigation.Navigation;
import inc.mesa.mesanews.R;

public class SplashFragment extends Fragment implements SplashContract.View, View.OnClickListener {

    private Button logInButton;
    private Button signUpButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_splash, container, false);

        logInButton = root.findViewById(R.id.bt_log_in);
        logInButton.setOnClickListener(this);

        signUpButton = root.findViewById(R.id.bt_sign_up);
        signUpButton.setOnClickListener(this);

        // handle back button to exit the app
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finish();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        return root;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.bt_log_in:
                showLogInScreen();
                break;
            case R.id.bt_sign_up:
                showSignUpScreen();
                break;
        }
    }

    @Override
    public void showLogInScreen() {
        Navigation.findNavController(logInButton)
                .navigate(R.id.action_display_log_in);
    }

    @Override
    public void showSignUpScreen() {
        Navigation.findNavController(signUpButton)
                .navigate(R.id.action_display_sign_up);
    }
}
