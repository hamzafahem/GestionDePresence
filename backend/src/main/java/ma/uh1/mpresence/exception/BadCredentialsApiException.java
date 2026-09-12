package ma.uh1.mpresence.exception;

public class BadCredentialsApiException extends RuntimeException {
    public BadCredentialsApiException(String message) {
        super(message);
    }
}
