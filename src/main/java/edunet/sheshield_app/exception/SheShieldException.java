package edunet.sheshield_app.exception;

/** Our own error, thrown when something is wrong (e.g. wrong password). */
public class SheShieldException extends RuntimeException {
    public SheShieldException(String message) {
        super(message);
    }
}