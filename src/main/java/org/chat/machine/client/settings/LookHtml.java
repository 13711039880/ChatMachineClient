package org.chat.machine.client.settings;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LookHtml {
    private static String html;
    private static String title;
    private JPanel jp;
    private JLabel TitleLabel;
    private JLabel text;
    private JScrollPane jsp;
    private JButton 源Button;

    public LookHtml() {
        jsp.getVerticalScrollBar().setUnitIncrement(30);
        text.setText("<html>" + html + "<html>");
        TitleLabel.setText(title);

        ActionListener 源Button2ActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text.setText("<html>" + html + "<html>");
                源Button.removeActionListener(this);
            }
        };

        ActionListener 源Button1ActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text.setText(html);
                源Button.removeActionListener(this);

                源Button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        text.setText("<html>" + html + "<html>");
                        源Button.removeActionListener(this);
                        源Button.addActionListener(源Button2ActionListener);
                    }
                });
            }
        };

        源Button.addActionListener(源Button1ActionListener);
    }

    public static void main(String html, String title) {
        LookHtml.html = html;
        LookHtml.title = title;

        JFrame frame = new JFrame(title);
        frame.setContentPane(new LookHtml().jp);
        frame.setSize(700, 450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
