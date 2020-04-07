package inc.mesa.mesanews.login;

import com.facebook.AccessToken;

public interface LogInContract {

    interface View {
        void showHomeScreen();

        // void showInvalidCredentials();
    }

    interface Presenter {
        void logIn(final String email, final String password);

        void logInWithFacebook(final AccessToken accessToken);
    }
}
