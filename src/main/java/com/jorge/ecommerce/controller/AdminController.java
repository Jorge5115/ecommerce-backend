package com.jorge.ecommerce.controller;

import com.jorge.ecommerce.dto.DashboardStatsDTO;
import com.jorge.ecommerce.dto.TopProductDTO;
import com.jorge.ecommerce.dto.UserDTO;
import com.jorge.ecommerce.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        return ResponseEntity.ok(adminService.getDashboardStats());
    }

    @GetMapping("/top-products")
    public ResponseEntity<List<TopProductDTO>> getTopProducts(
            @RequestParam(defaultValue = "5") int limit
    ) {
        return ResponseEntity.ok(adminService.getTopProducts(limit));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/{id}/toggle-role")
    public ResponseEntity<UserDTO> toggleUserRole(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.toggleUserRole(id));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
