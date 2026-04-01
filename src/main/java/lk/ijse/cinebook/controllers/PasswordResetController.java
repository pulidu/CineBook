package lk.ijse.cinebook.controllers;

import lk.ijse.cinebook.dto.request.ForgotPasswordRequest;
import lk.ijse.cinebook.dto.request.ResetPasswordRequest;
import lk.ijse.cinebook.service.impl.PasswordResetServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetServiceImpl passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordResetService.createPasswordResetToken(request.getEmail());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset email sent successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset successfully");
        return ResponseEntity.ok(response);
    }
}