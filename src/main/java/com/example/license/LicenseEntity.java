package com.example.license;

public class LicenseEntity {
    private Long id;
    private String licenseKey;

    public LicenseEntity() {}

    public LicenseEntity(String licenseKey) {
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