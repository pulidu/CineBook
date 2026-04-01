package lk.ijse.cinebook.controllers;

import lk.ijse.cinebook.dto.response.DashboardResponse;
import lk.ijse.cinebook.entity.Payment;
import lk.ijse.cinebook.service.impl.AdminServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminServiceImpl adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/users")
    public ResponseEntity<List<lk.ijse.cinebook.dto.response.UserResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable Long id, @RequestParam String role) {
        adminService.updateUserRole(id, role);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/payments")
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(adminService.getAllPayments());
    }
}