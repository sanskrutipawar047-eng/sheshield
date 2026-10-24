package edunet.sheshield_app.dto;

/** The 5 danger signals the user toggles on the Simulate page. */
public record DangerSignalDto(boolean scream, boolean jerk, boolean isolated,
                              boolean lateNight, boolean heartRate) { }
