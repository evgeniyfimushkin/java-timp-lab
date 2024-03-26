package edu.evgen.client;

public enum MessageMarkers {
    SETID("@SETID@"),
    SESSIONS("@SESSIONS@"),
    EXCHANGEREPLY("@EXCHANGEREPLY@"),
    EXCHANGEREQUEST("@EXCHANGEREQUEST@");

    private final String marker;

    MessageMarkers(String marker) {
        this.marker = marker;
    }

    public String getMarker() {
        return marker;
    }
}
