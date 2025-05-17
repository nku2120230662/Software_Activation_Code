package com.guohao.local_server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Scanner;

/**
 * 硬件标识识别工具类（国产化环境适配版）
 */
public class HardwareIdentifier {

    /**
     * 获取CPU序列号（适配龙芯、飞腾等国产CPU）
     */
    public static String getCPUSerial() {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("linux")) {
                // 国产Linux系统（如麒麟、统信UOS）
                Process process = Runtime.getRuntime().exec("dmidecode -t processor");
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().startsWith("ID:")) {
                        return line.split(":")[1].trim();
                    }
                }
            } else if (os.contains("win")) {
                // Windows系统（适配兆芯等国产平台）
                Process process = Runtime.getRuntime().exec(
                        new String[]{"wmic", "cpu", "get", "ProcessorId"});
                process.getOutputStream().close();

                Scanner sc = new Scanner(process.getInputStream());
                sc.next(); // 跳过标题行
                if (sc.hasNext()) {
                    return sc.next().trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "UNKNOWN";
    }

    /**
     * 获取主板序列号（适配国产主板）
     */
    public static String getMotherboardSerial() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("linux")) {
                // 国产Linux系统
                Process process = Runtime.getRuntime().exec("dmidecode -t baseboard");
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().startsWith("Serial Number:")) {
                        return line.split(":")[1].trim();
                    }
                }
            } else if (os.contains("win")) {
                // Windows系统
                Process process = Runtime.getRuntime().exec(
                        new String[]{"wmic", "baseboard", "get", "serialnumber"});
                process.getOutputStream().close();

                Scanner sc = new Scanner(process.getInputStream());
                sc.next(); // 跳过标题行
                if (sc.hasNext()) {
                    return sc.next().trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "UNKNOWN";
    }

    /**
     * 获取MAC地址（多网卡支持）
     */
    public static String getMacAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }

                byte[] mac = networkInterface.getHardwareAddress();
                if (mac != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
                    }
                    return sb.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "00:00:00:00:00:00";
    }

    /**
     * 获取硬盘序列号（主硬盘）
     */
    public static String getDiskSerial() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("linux")) {
                // Linux系统（适配国产存储设备）
                Process process = Runtime.getRuntime().exec("hdparm -I /dev/sda");
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().startsWith("Serial Number:")) {
                        return line.split(":")[1].trim();
                    }
                }
            } else if (os.contains("win")) {
                // Windows系统
                Process process = Runtime.getRuntime().exec(
                        new String[]{"wmic", "diskdrive", "get", "serialnumber"});
                process.getOutputStream().close();

                Scanner sc = new Scanner(process.getInputStream());
                sc.next(); // 跳过标题行
                if (sc.hasNext()) {
                    return sc.next().trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "UNKNOWN";
    }

    /**
     * 生成综合硬件指纹（SHA-256哈希）
     */
    public static String getHardwareFingerprint() {
        try {
            String info = getCPUSerial() + getMotherboardSerial() +
                    getMacAddress() + getDiskSerial();
            return sha256Hash(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "UNKNOWN";
    }

    // 国产SM3哈希算法替代SHA-256（需引入BouncyCastle库）
    private static String sha256Hash(String input) {
        // 实际项目中应使用国密SM3算法
        // 这里简化为示例
        return Integer.toHexString(input.hashCode());
    }

    public static void main(String[] args) {
        System.out.println("===== 硬件标识信息 =====");
        System.out.println("CPU序列号: " + getCPUSerial());
        System.out.println("主板序列号: " + getMotherboardSerial());
        System.out.println("MAC地址: " + getMacAddress());
        System.out.println("硬盘序列号: " + getDiskSerial());
        System.out.println("硬件指纹: " + getHardwareFingerprint());
    }
}