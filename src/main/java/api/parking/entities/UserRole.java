package api.parking.entities;

public enum UserRole {
    ADMIN("admin"),
    COMMON("common");

    String userRole;

    UserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserRole() {
        return userRole;
    }
}
