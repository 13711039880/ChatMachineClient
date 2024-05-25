package org.chat.machine.client.RoomManagement;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoomManagement {
    private JPanel jp;
    private JLabel 房间Label;
    private JButton 创建房间Button;
    private JButton 删除房间Button;

    public RoomManagement() {
        创建房间Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddRoom.main();
            }
        });
    }

    public static void main() {
        JFrame frame = new JFrame("聊天器 - 房间");
        frame.setContentPane(new RoomManagement().jp);
        frame.setSize(400, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
