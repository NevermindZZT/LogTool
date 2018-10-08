package com.letter.logtool;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import java.util.List;

public class LogTool {
    public static final int UI_THEME = 0;
    public static final String lookAndFeel = UIManager.getSystemLookAndFeelClassName();

    public static void main (String[] args) {
        try {
            if (UI_THEME == 0) {
                UIManager.setLookAndFeel(lookAndFeel);
            } else if (UI_THEME == 1) {
                BeautyEyeLNFHelper.launchBeautyEyeLNF();
                UIManager.put("RootPane.setupButtonVisible", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogToolFrame logToolFrame = new LogToolFrame();
        SerialConnect serialConnect = new SerialConnect();
        LogFile logFile = new LogFile(System.getProperty("user.dir") + "/logTool.log");

        logFile.setLogLevel(LogFile.LOG_LEVEL_VERBOSE);
        logFile.setLevelMode(LogFile.LEVEL_MODE_LOW);

        logToolFrame.setConnectButtonListener((port, baudRate) -> {
            if (serialConnect.isConnected()) {
                serialConnect.closeSerialPort();
                logToolFrame.setConnectButtonText("连接");
            } else {
                if (serialConnect.connectSerialPort(port, baudRate)) {
                    logToolFrame.setConnectButtonText("断开");
                } else {
                    JOptionPane.showMessageDialog(null, "串口连接失败");
                }
            }
        });
        logToolFrame.setDebugLevelComboBoxListener(string -> {
            for (int i = 0; i < LogToolFrame.DEBUG_LEVEL.length; i++) {
                if (string.equals(LogToolFrame.DEBUG_LEVEL[i])) {
                    logFile.setLogLevel(5 - i);
                }
            }
            logFile.resetReader();
            String line = null;
            while ((line = logFile.readLine()) != null) {
                if (logFile.toDebugLine(line) != null) {
                    logToolFrame.addTextToLogTextArea(line + "\n");
                }
            }
        });
        logToolFrame.setLevelModeComboBoxListener(string -> {
            for (int i = 0; i < LogToolFrame.LEVEL_MODE.length; i++) {
                if (string.equals(LogToolFrame.LEVEL_MODE[i])) {
                    logFile.setLevelMode(i);
                }
            }
            logFile.resetReader();
            String line = null;
            while ((line = logFile.readLine()) != null) {
                if (logFile.toDebugLine(line) != null) {
                    logToolFrame.addTextToLogTextArea(line + "\n");
                }
            }
        });
        logToolFrame.setSearchTextListener((string -> {
            logFile.setSearchPatten(string);
            logFile.resetReader();
            String line = null;
            while ((line = logFile.readLine()) != null) {
                if (logFile.toDebugLine(line) != null) {
                    logToolFrame.addTextToLogTextArea(line + "\n");
                }
            }
        }));
        logToolFrame.setSendTextListener(string -> serialConnect.serialWrite(string + "\n"));

        List<String> serialList = serialConnect.getPortList();
        for (String serialPort: serialList) {
            logToolFrame.addSerialPort(serialPort);
        }

        serialConnect.setSerialReceiveListener(string -> {
            logToolFrame.addTextToInputTextArea(string);
            logFile.writeFile(string);
            if (string.contains("\n")) {
                String line;
                while ((line = logFile.readLine()) != null) {
                    if (logFile.toDebugLine(line) != null) {
                        logToolFrame.addTextToLogTextArea(line + "\n");
                    }
                }
            }
        });

    }
}
