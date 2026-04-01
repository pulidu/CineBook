package lk.ijse.cinebook.service;

public interface PasswordResetService {
    void createPasswordResetToken(String email);
    void resetPassword(String token, String newPassword);
}
