package edu.evgen.client;

public enum MessageMarkers {
    SETID("@SETID@"),
    SESSIONS("@SESSIONS@"),
    DISCONNECT("@DISCONNECT@"),
    EXCHANGEREPLY("@EXCHANGEREPLY@"),
    EXCHANGEREQUEST("@EXCHANGEREQUEST@");

    private final String marker;

    MessageMarkers(String marker) {
        this.marker = marker;
    }

    public String getMarker() {
        return marker;
    }
    // на 4 сетевое
    // на 5 сетевое и сложное
}
