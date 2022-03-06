package br.com.agmg.parkingcontrol.util;

public class ErrorCode {
    
    private String reason;

    public ErrorCode(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
