package inc.mesa.mesanews.client.model.request;

public class SignUpRequest {

    private String name;
    private String email;
    private String password;

    public SignUpRequest(final String name, final String email, final String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static class Builder {

        private String name;
        private String email;
        private String password;

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder email(final String email) {
            this.email = email;
            return this;
        }

        public Builder password(final String password) {
            this.password = password;
            return this;
        }

        public SignUpRequest build() {
            return new SignUpRequest(name, email, password);
        }
    }
}
