package edunet.sheshield_app.exception;

import java.time.LocalDateTime;

/** Error message + time, shown on the error page. (Record) */
public record ErrorDetails(String message, LocalDateTime timestamp) { }