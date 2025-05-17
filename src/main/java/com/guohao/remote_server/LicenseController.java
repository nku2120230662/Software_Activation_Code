package com.guohao.remote_server;

import org.springframework.web.bind.annotation.*;
import java.util.UUID;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api")
public class LicenseController {
    // 新增数据库访问对象
    @Autowired
    private LicenseMapper licenseMapper; // 需要创建该类

    @GetMapping("/generate_license")
    public Map<String, String> generateLicense() {
        String licenseKey = UUID.randomUUID().toString();
        // 保存到数据库
        licenseMapper.saveLicense(licenseKey);
        return Map.of("license", licenseKey);
    }

    @PostMapping("/verify_license")
    public Map<String, Boolean> verifyLicense(@RequestBody Map<String, String> request) {
        String licenseKey = request.get("license");
        // 从数据库验证
        boolean isValid = licenseMapper.verifyLicense(licenseKey);
        return Map.of("valid", isValid);
    }
}