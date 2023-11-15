package com.example.astontest.admin.controller;

import com.example.astontest.admin.service.AdminService;
import com.example.astontest.core.dto.responsedto.ResponseResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/accounts")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/all")
    ResponseResult getAllAccounts() {
        return adminService.getAllAccounts();
    }

    @GetMapping("/{accountName}/transactions")
    ResponseResult getAllTransactionsByAccount(@PathVariable String accountName) {
        return adminService.getAllTransactionByAccount(accountName);
    }
}
