package org.chat.machine.client.settings;

import org.chat.machine.client.Main;

import javax.swing.*;

public class PrivacyPolicy {
    private JPanel jp;
    private JLabel 隐私政策Label;
    private JScrollPane TextJsp;
    private JTextArea text;

    public PrivacyPolicy() {
        if (Main.resource.look.text.PrivacyPolicy != null) {
            text.setText(Main.resource.look.text.PrivacyPolicy);
        } else {
            text.setText("资源未加载");
        }
    }

    public static void main() {
        JFrame frame = new JFrame("聊天器 - 隐私政策");
        frame.setContentPane(new PrivacyPolicy().jp);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
