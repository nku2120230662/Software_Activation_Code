package com.guohao.model;

public class AuthenticationKey {
    private Long id;
    private String licenseKey;

    public AuthenticationKey() {}

    public AuthenticationKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }
}