package org.chat.machine.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chat.machine.client.settings.settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.chat.machine.client.ConciseLoad.LoadMain.pause;

public class ConciseLoad extends JFrame {
    private static final Logger log = LogManager.getLogger(load.class);
    private JPanel jp;
    private JLabel title;
    private JLabel tip1;
    private JLabel tip3;
    private JLabel status;
    private JLabel tip2;
    private JLabel tip4;

    private JFrame InfoFrame;
    private JScrollPane InfoJsp;
    private JTextArea InfoText;

    {
        InfoFrame = new JFrame();
        InfoJsp = new JScrollPane();
        InfoText = new JTextArea();

        InfoText.setEditable(false);
        InfoJsp.setViewportView(InfoText);
        InfoFrame.setTitle("信息");
        InfoFrame.setContentPane(InfoJsp);
        InfoFrame.setSize(500, 300);
        InfoFrame.setLocationRelativeTo(null);
    }

    public ConciseLoad() {
        setTitle("聊天器");
        setContentPane(jp);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        addKeyListener(new listener());

        SetModel();
        new LoadMain().start();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void SetModel() {
        if (Main.set.load.StereoscopicLogo) {
            title.setFont(new Font("微软雅黑", Font.PLAIN, 8));
            title.setText("""
                <html>
                <br> ________  ___  ___  ________  _________  _____ ______   ________  ________  ___  ___  ___  ________   _______   ________  ___       ___  _______   ________   _________  \s
                <br>|\\   ____\\|\\  \\|\\  \\|\\   __  \\|\\___   ___\\\\   _ \\  _   \\|\\   __  \\|\\   ____\\|\\  \\|\\  \\|\\  \\|\\   ___  \\|\\  ___ \\ |\\   ____\\|\\  \\     |\\  \\|\\  ___ \\ |\\   ___  \\|\\___   ___\\\s
                <br>\\ \\  \\___|\\ \\  \\\\\\  \\ \\  \\|\\  \\|___ \\  \\_\\ \\  \\\\\\__\\ \\  \\ \\  \\|\\  \\ \\  \\___|\\ \\  \\\\\\  \\ \\  \\ \\  \\\\ \\  \\ \\   __/|\\ \\  \\___|\\ \\  \\    \\ \\  \\ \\   __/|\\ \\  \\\\ \\  \\|___ \\  \\_|\s
                <br> \\ \\  \\    \\ \\   __  \\ \\   __  \\   \\ \\  \\ \\ \\  \\\\|__| \\  \\ \\   __  \\ \\  \\    \\ \\   __  \\ \\  \\ \\  \\\\ \\  \\ \\  \\_|/_\\ \\  \\    \\ \\  \\    \\ \\  \\ \\  \\_|/_\\ \\  \\\\ \\  \\   \\ \\  \\ \s
                <br>  \\ \\  \\____\\ \\  \\ \\  \\ \\  \\ \\  \\   \\ \\  \\ \\ \\  \\    \\ \\  \\ \\  \\ \\  \\ \\  \\____\\ \\  \\ \\  \\ \\  \\ \\  \\\\ \\  \\ \\  \\_|\\ \\ \\  \\____\\ \\  \\____\\ \\  \\ \\  \\_|\\ \\ \\  \\\\ \\  \\   \\ \\  \\\s
                <br>   \\ \\_______\\ \\__\\ \\__\\ \\__\\ \\__\\   \\ \\__\\ \\ \\__\\    \\ \\__\\ \\__\\ \\__\\ \\_______\\ \\__\\ \\__\\ \\__\\ \\__\\\\ \\__\\ \\_______\\ \\_______\\ \\_______\\ \\__\\ \\_______\\ \\__\\\\ \\__\\   \\ \\__\\
                <br>    \\|_______|\\|__|\\|__|\\|__|\\|__|    \\|__|  \\|__|     \\|__|\\|__|\\|__|\\|_______|\\|__|\\|__|\\|__|\\|__| \\|__|\\|_______|\\|_______|\\|_______|\\|__|\\|_______|\\|__| \\|__|    \\|__|
                <html>
                """);
        } else {
            title.setFont(new Font("微软雅黑", Font.PLAIN, 20));
            title.setText("ChatMachineClient");
        }
    }

    class listener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            if (KeyEvent.getKeyText(e.getKeyCode()).equals("Esc")) {
                System.exit(0);
            }

            if (KeyEvent.getKeyText(e.getKeyCode()).equals("F1")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        pause = true;
                        settings.main();

                        while (true) {
                            if (!settings.open) {
                                break;
                            }
                        }

                        pause = false;
                    }
                }).start();
            }
            
            if (KeyEvent.getKeyText(e.getKeyCode()).equals("F2")) {
                InfoFrame.setVisible(true);
            }

            if (KeyEvent.getKeyText(e.getKeyCode()).equals("Delete")) {
                pause = !pause;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }
    }

    class LoadMain extends Thread {
        public static boolean pause = false;
        public static boolean HaveError = false;

        @Override
        public void run() {
            IfPause();
            if (!Main.set.load.ConciseLoadFast) {
                IfPause();

                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            IfPause();

            log.info("[load] 开始加载");
            timing LoadTime = new timing();
            IfPause();

            log.info("[load][获取资源] 获取资源...");
            timing GetResourceTime = new timing();
            IfPause();
            try {
                GetResource();
            } catch (IOException e) {
                HaveError = true;
                log.error("[load] 获取资源: " + new RuntimeException(e));
            }
            log.info("[load][获取资源] 获取资源完成({}ms)", GetResourceTime.stop());
            IfPause();

            log.info("[load][连接] 正在连接到服务器...");
            timing CheckConnectionTime = new timing();
            IfPause();
            try {
                RequestServer.main();
            } catch (IOException e) {
                HaveError = true;
                log.error("[load] 连接失败: {}(IP: {})", new RuntimeException(e).toString(), Main.config.ip + ":" + Main.config.port);
                JOptionPane.showMessageDialog(null, "连接失败:\n" + new RuntimeException(e) + "\nIP:\n" + Main.config.ip + ":" + Main.config.port);
                System.exit(1);
            }
            log.info("[load][连接] 服务器连接完成({}ms)", CheckConnectionTime.stop());
            IfPause();

            log.info("[load][验证] 正在验证...");
            timing AuthenticationTime = new timing();
            IfPause();
            String VerificationReturn = RequestServer.LoadSpecialPurpose("verify:" + Main.config.key, "验证", InfoText);
            IfPause();
            if (VerificationReturn != null) {
                if (VerificationReturn.equals("true") || VerificationReturn.equals("false")) {
                    if (VerificationReturn.equals("true")) {
                        log.info("[load][验证] 验证通过({}ms)", AuthenticationTime.stop());
                    } else {
                        HaveError = true;
                        log.warn("[load][验证] key错误(" + Main.config.key + ")\n");
                    }
                } else {
                    HaveError = true;
                    log.error("[load][验证] 验证: 返回值错误: {} ({}ms)", VerificationReturn, AuthenticationTime.stop());
                }
            } else {
                HaveError = true;
                log.error("[load][验证] 验证: 返回值错误: {} ({}ms)", " " + VerificationReturn, AuthenticationTime.stop());
            }
            IfPause();

            log.info("[load][获取房间] 获取房间...");
            timing GetRoomTime = new timing();
            IfPause();
            Main.info.room = RequestServer.LoadSpecialPurpose("getroom:", "验证", InfoText);
            IfPause();
            if (Main.info.room == null) {
                HaveError = true;
                log.error("[load][获取房间] 返回值错误: {} ({}ms)", VerificationReturn, GetRoomTime.stop());
            } else {
                log.info("[load][获取房间] 房间: {} ({}ms)", Main.info.room, GetRoomTime.stop());
            }
            IfPause();

            log.info("[load][创建托盘] 创建托盘...");
            timing CreateTrayTime = new timing();
            IfPause();
            tray.main(null);
            IfPause();
            log.info("[load][创建托盘] 创建托盘完成({}ms)", CreateTrayTime.stop());
            IfPause();

            log.info("[load] 加载完成({}ms)", LoadTime.stop());

            if (!HaveError) {
                InfoFrame.dispose();
                dispose();
                login.main();
            } else {
                log.error("[load] 有错误");
                InfoFrame.setVisible(true);
                JOptionPane.showMessageDialog(null, "有错误");
            }
        }

        private void GetResource() throws IOException {
            ZipInputStream zis = new ZipInputStream(new FileInputStream("resource.zip"));
            ZipEntry zipEntry;

            while ((zipEntry = zis.getNextEntry()) != null) {
                String outputFilePath = ".\\temp" + File.separator + zipEntry.getName();
                File destFile = new File(outputFilePath);

                if (!destFile.getParentFile().exists()) {
                    destFile.getParentFile().mkdirs();
                }

                FileOutputStream fos = new FileOutputStream(destFile);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = zis.read(buffer)) >= 0) {
                    fos.write(buffer, 0, len);
                }

                zis.closeEntry();
            }

            Main.resource.icon.user = new ImageIcon(".\\temp\\UserIco.png");
            Main.resource.icon.set = new ImageIcon(".\\temp\\SetIco.png");
            Main.resource.icon.room = new ImageIcon(".\\temp\\RoomIco.png");
            Main.resource.icon.main = new ImageIcon(".\\temp\\main.ico");

            Main.resource.look.text.EULA = Files.readString(Path.of(".\\temp\\EULA.txt"));
            Main.resource.look.text.PrivacyPolicy = Files.readString(Path.of(".\\temp\\PrivacyPolicy.txt"));

            Main.resource.look.html.about = GetHtml(".\\temp\\about.html");
            Main.resource.look.html.SendReplaceHelp = GetHtml(".\\temp\\SendReplaceHelp.html");
            Main.resource.look.html.ReceiveReplaceHelp = GetHtml(".\\temp\\ReceiveReplaceHelp.html");

            Main.resource.music.message = new File(".\\temp\\MessageMusic.wav");
        }

        private static String GetHtml(String file) throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("GBK")));
            StringBuilder sb = new StringBuilder();
            String str;

            while ((str = br.readLine()) != null) {
                sb.append(str);
            }

            return sb.toString();
        }

        private void IfPause() {
            if (pause) {
                status.setText("暂停");

                while (true) {
                    if (!pause) {
                        break;
                    }
                }

                status.setText("");
            }
        }
    }
}
