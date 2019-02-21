package mobi.lab.veriff.sample.data;

public class TokenResponse {

    private String status;
    private Verification verification;

    public TokenResponse(String status, Verification verification) {
        this.status = status;
        this.verification = verification;
    }

    public String getStatus() {
        return status;
    }

    public Verification getVerification() {
        return verification;
    }

    public static class Verification {
        private String id;
        private String url;
        private String host;
        private String sessionToken;
        private String baseUrl;

        public Verification(String id, String url, String sessionToken, String baseUrl) {
            this.id = id;
            this.url = url;
            this.sessionToken = sessionToken;
            this.baseUrl = baseUrl;
        }

        public Verification() {
        }

        public String getSessionToken() {
            return sessionToken;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public String getHost() {
            return host;
        }
    }
}
