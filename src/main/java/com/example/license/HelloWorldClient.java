package com.example.license;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import com.google.gson.Gson;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.google.gson.reflect.TypeToken;

public class HelloWorldClient {
    private static final String LICENSE_SERVICE_URL = "http://127.0.0.1:8080/api/verify_license";

    public static void main(String[] args) {
        // 创建主窗口
        JFrame frame = new JFrame("激活码输入");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new FlowLayout());

        // 创建输入框和按钮
        JTextField licenseField = new JTextField(20);
        JButton submitButton = new JButton("提交");

        // 添加组件到窗口
        frame.add(licenseField);
        frame.add(submitButton);

        // 按钮点击事件处理
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String licenseKey = licenseField.getText();
                try {
                    URL url = new URL(LICENSE_SERVICE_URL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; utf-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);

                    String jsonInputString = "{\"license\": \"" + licenseKey + "\"}";

                    try (java.io.OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }

                    try (BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                        StringBuilder response = new StringBuilder();
                        String responseLine;
                        while ((responseLine = br.readLine()) != null) {
                            response.append(responseLine.trim());
                        }
                        Gson gson = new Gson();
                        // 使用 TypeToken 来避免类型不安全的转换
                        java.lang.reflect.Type type = new TypeToken<Map<String, Boolean>>() {
                        }.getType();
                        Map<String, Boolean> result = gson.fromJson(response.toString(), type);
                        boolean isValid = result.get("valid");

                        if (isValid) {
                            JOptionPane.showMessageDialog(frame, "激活码验证成功！");
                        } else {
                            JOptionPane.showMessageDialog(frame, "激活码验证失败，请重试。");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "验证过程中出现错误，请重试。");
                }
            }
        });

        // 显示窗口
        frame.setVisible(true);
    }
}