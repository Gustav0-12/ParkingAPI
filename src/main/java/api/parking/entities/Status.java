package api.parking.entities;

public enum Status {
    AVAILABLE("available"),
    OCCUPIED("occupied");

    String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
