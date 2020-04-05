package inc.mesa.mesanews.client.model.request;

public class LogInRequest {

    private String email;
    private String password;

    public LogInRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }

    public static class Builder {

        private String email;
        private String password;

        public Builder email(final String email) {
            this.email = email;
            return this;
        }

        public Builder password(final String password) {
            this.password = password;
            return this;
        }

        public LogInRequest build() {
            return new LogInRequest(email, password);
        }
    }
}
