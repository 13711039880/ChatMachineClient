package org.chat.machine.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.chat.machine.client.Main.*;

public class load extends JFrame {
    protected static final Logger log = LogManager.getLogger(load.class);
    static JLabel status;
    private JPanel jp;
    private JLabel 状态Label;
    private JButton 取消Button;
    private JTextArea ErrorTextArea;
    private JProgressBar ProgressBar;
    private JScrollPane ErrorTextAreaJsp;

    public load() {
        setTitle("聊天器");
        setContentPane(jp);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);

        SetModule();
        new LoadMain(ErrorTextArea, ProgressBar, 取消Button).start();
    }

    public void SetModule() {
        status = 状态Label;

        取消Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    class LoadMain extends Thread {
        protected static final Logger log = LogManager.getLogger(LoadMain.class);
        public static boolean HaveError = false;
        private JTextArea ErrorTextArea;
        private JProgressBar ProgressBar;
        private JButton 取消Button;

        private LoadMain(JTextArea ErrorTextArea, JProgressBar ProgressBar, JButton 取消Button) {
            this.ErrorTextArea = ErrorTextArea;
            this.ProgressBar = ProgressBar;
            this.取消Button = 取消Button;
        }

        @Override
        public void run() {
            log.info("[load] 开始加载");
            timing LoadTime = new timing();

            log.info("[load][获取资源] 获取资源...");
            status.setText("获取资源...");
            timing GetResourceTime = new timing();
            try {
                GetResource();
            } catch (IOException e) {
                HaveError = true;
                log.error("[load] 获取资源: " + new RuntimeException(e));
                ErrorTextArea.append("[错误] 获取资源: " + new RuntimeException(e) + "\n");
            }
            log.info("[load][获取资源] 获取资源完成({}ms)", GetResourceTime.stop());
            SetProgress(ProgressBar, 20);

            log.info("[load][连接] 正在连接到服务器...");
            status.setText("正在连接到服务器...");
            timing CheckConnectionTime = new timing();
            try {
                RequestServer.main();
            } catch (IOException e) {
                HaveError = true;
                log.error("[load][连接] 连接失败: {}(IP: {})", new RuntimeException(e).toString(), config.ip + ":" + config.port);
                ErrorTextArea.append(String.valueOf(new RuntimeException(e)));
                JOptionPane.showMessageDialog(null, "连接失败:\n" + new RuntimeException(e) + "\nIP:\n" + config.ip + ":" + config.port);
                System.exit(1);
            }
            log.info("[load][连接] 服务器连接完成({}ms)", CheckConnectionTime.stop());
            SetProgress(ProgressBar, 40);

            log.info("[load][验证] 正在验证...");
            status.setText("正在验证...");
            timing AuthenticationTime = new timing();
            String VerificationReturn = RequestServer.LoadSpecialPurpose("verify:" + config.key, "验证", ErrorTextArea);
            if (VerificationReturn != null) {
                if (VerificationReturn.equals("true") || VerificationReturn.equals("false")) {
                    if (VerificationReturn.equals("true")) {
                        log.info("[load][验证] 验证通过({}ms)", AuthenticationTime.stop());
                    } else {
                        HaveError = true;
                        log.warn("[load][验证] key错误(" + config.key + ")\n");
                        ErrorTextArea.append("[警告] 验证: key错误(" + config.key + ")\n");
                    }
                } else {
                    HaveError = true;
                    log.error("[load][验证] 验证: 返回值错误: {} ({}ms)", VerificationReturn, AuthenticationTime.stop());
                    ErrorTextArea.append("[错误] 验证: " + new Exception("返回值错误: " + VerificationReturn) + "\n");
                }
            } else {
                HaveError = true;
                log.error("[load][验证] 验证: 返回值错误: {} ({}ms)", " " + VerificationReturn, AuthenticationTime.stop());
                ErrorTextArea.append("[错误] 验证: " + new Exception("返回值错误: " + VerificationReturn) + "\n");
            }
            SetProgress(ProgressBar, 60);

            log.info("[load][获取房间] 获取房间...");
            status.setText("获取房间...");
            timing GetRoomTime = new timing();
            info.room = RequestServer.LoadSpecialPurpose("getroom:", "验证", ErrorTextArea);
            if (info.room == null) {
                HaveError = true;
                log.error("[load][获取房间] 返回值错误: {} ({}ms)", VerificationReturn, GetRoomTime.stop());
                ErrorTextArea.append("[错误] 获取房间: 返回值错误: " + "" + VerificationReturn + "\n");
            } else {
                log.info("[load][获取房间] 房间: {} ({}ms)", info.room, GetRoomTime.stop());
            }
            ProgressBar.setValue(80);

            log.info("[load][创建托盘] 创建托盘...");
            status.setText("创建托盘...");
            timing CreateTrayTime = new timing();
            tray.main(ErrorTextArea);
            log.info("[load][创建托盘] 创建托盘完成({}ms)", CreateTrayTime.stop());
            SetProgress(ProgressBar, 100);

            log.info("[load] 加载完成({}ms)", LoadTime.stop());

            if (!HaveError) {
                dispose();
                login.main();
            } else {
                log.error("[load] 有错误");

                try {
                    取消Button.setText("关闭");
                    status.setText("有错误");
                    sleep(500);
                    status.setForeground(Color.RED);
                    sleep(500);
                    status.setForeground(Color.BLACK);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
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

            resource.icon.user = new ImageIcon(".\\temp\\UserIco.png");
            resource.icon.set = new ImageIcon(".\\temp\\SetIco.png");
            resource.icon.room = new ImageIcon(".\\temp\\RoomIco.png");
            resource.icon.main = new ImageIcon(".\\temp\\main.ico");

            resource.look.text.EULA = Files.readString(Path.of(".\\temp\\EULA.txt"));
            resource.look.text.PrivacyPolicy = Files.readString(Path.of(".\\temp\\PrivacyPolicy.txt"));

            resource.look.html.about = GetHtml(".\\temp\\about.html");
            resource.look.html.SendReplaceHelp = GetHtml(".\\temp\\SendReplaceHelp.html");
            resource.look.html.ReceiveReplaceHelp = GetHtml(".\\temp\\ReceiveReplaceHelp.html");

            resource.music.message = new File(".\\temp\\MessageMusic.wav");
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

        private static void SetProgress(JProgressBar ProgressBar, int to) {
            for (int i = ProgressBar.getValue(); i < to; i++) {
                try {
                    sleep(4);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                ProgressBar.setValue(i);
            }
        }
    }
}
