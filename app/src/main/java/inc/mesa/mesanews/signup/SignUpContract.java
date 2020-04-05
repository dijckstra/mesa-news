package inc.mesa.mesanews.signup;

public interface SignUpContract {

    interface View {
        void showHomeScreen();
    }

    interface Presenter {
        void signUp(final String name, final String email, final String password, final String passwordConfirmation);
    }
}
