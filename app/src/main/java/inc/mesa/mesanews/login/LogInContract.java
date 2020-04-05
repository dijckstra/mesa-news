package inc.mesa.mesanews.login;

public interface LogInContract {

    interface View {
        void showHomeScreen();

        // void showInvalidCredentials();
    }

    interface Presenter {
        void logIn(final String email, final String password);
    }
}
