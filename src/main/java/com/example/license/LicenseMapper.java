package com.example.license;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LicenseMapper {
    void saveLicense(@Param("licenseKey") String licenseKey);
    boolean verifyLicense(@Param("licenseKey") String licenseKey);
}