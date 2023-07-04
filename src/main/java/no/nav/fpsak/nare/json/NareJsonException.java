package no.nav.fpsak.nare.json;

public class NareJsonException extends RuntimeException {

    public NareJsonException(String message) {
        super(message);
    }

    public NareJsonException(String message, Throwable cause) {
        super(message, cause);
    }

}
