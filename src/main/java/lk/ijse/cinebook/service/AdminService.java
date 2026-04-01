package lk.ijse.cinebook.service;

import lk.ijse.cinebook.dto.response.DashboardResponse;
import lk.ijse.cinebook.dto.response.UserResponse;
import lk.ijse.cinebook.entity.Payment;

import java.util.List;

public interface AdminService {
    DashboardResponse getDashboardStats();
    List<UserResponse> getAllUsers();
    void updateUserRole(Long userId, String roleName);
    List<Payment> getAllPayments();

}
