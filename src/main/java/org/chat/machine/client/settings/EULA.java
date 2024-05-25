package org.chat.machine.client.settings;

import org.chat.machine.client.Main;

import javax.swing.*;

public class EULA {
    private JPanel jp;
    private JLabel 最终用户许可协议Label;
    private JTextArea text;
    private JScrollPane TextJsp;

    public EULA() {
        if (Main.resource.look.text.EULA != null) {
            text.setText(Main.resource.look.text.EULA);
        } else {
            text.setText("资源未加载");
        }
    }

    public static void main() {
        JFrame frame = new JFrame("聊天器 - 最终用户许可协议");
        frame.setContentPane(new EULA().jp);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
