package com.letter.logtool;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class SerialConnect implements SerialPortEventListener {

    private boolean connected = false;
    private Enumeration<CommPortIdentifier> portList;
    private SerialPort serialPort;
    private OutputStream outputStream;
    private InputStream inputStream;

    private TextChangedListener serialReceiveListener;

    @SuppressWarnings("unchecked")
    public SerialConnect () {
        portList = CommPortIdentifier.getPortIdentifiers();
    }

    public List<String> getPortList () {
        List<String> arrayList= new ArrayList<String>();
        CommPortIdentifier portId;
        while (portList.hasMoreElements()) {
            portId = portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                arrayList.add(portId.getName());
            }
        }
        return arrayList;
    }

    public boolean connectSerialPort (String string, int baudRate) {
        try {
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(string);
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL
                    && string.equals(portId.getName())) {
                serialPort = (SerialPort) portId.open(string, 2000);
                serialPort.addEventListener(this);
                serialPort.notifyOnDataAvailable(true);
                serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                outputStream = serialPort.getOutputStream();
                inputStream = serialPort.getInputStream();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        connected = true;
        return true;
    }

    private void serialRead () {
        byte[] readBuff = new byte[1024];
        try {
            int length;
            while ((length = inputStream.read(readBuff)) > 0) {
                serialReceiveListener.textPerformed(new String(readBuff, 0, length));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void serialWrite (String string) {
        try {
            outputStream.write(string.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        switch (serialPortEvent.getEventType()) {
            case SerialPortEvent.BI:	                    //通讯中断
            case SerialPortEvent.OE:	                    //溢位错误
            case SerialPortEvent.FE:	                    //帧错误
            case SerialPortEvent.PE:	                    //奇偶校验错误
            case SerialPortEvent.CD:	                    //载波检测
            case SerialPortEvent.CTS:	                    //清除发送
            case SerialPortEvent.DSR:	                    //数据设备准备好
            case SerialPortEvent.RI:	                    //响铃侦测
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:	    //输出缓冲区已清空
                break;
            case SerialPortEvent.DATA_AVAILABLE:	        //有数据到达
                serialRead();
                break;
            default:
                break;
        }
    }

    public void setSerialReceiveListener(TextChangedListener serialReceiveListener) {
        this.serialReceiveListener = serialReceiveListener;
    }

    public void closeSerialPort() {
        if (serialPort != null) {
            serialPort.notifyOnDataAvailable(false);
            serialPort.removeEventListener();
            if (inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;
                }
                catch (IOException e) {}
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                    outputStream = null;
                }
                catch (IOException e) {}
            }
            serialPort.close();
            serialPort = null;
        }
        connected = false;
    }

    public boolean isConnected() {
        return connected;
    }

    public static void main (String[] args) {
        SerialConnect serialConnect = new SerialConnect();
        serialConnect.getPortList();
        serialConnect.connectSerialPort("COM1", 115200);
        serialConnect.setSerialReceiveListener(string -> System.out.println(string));
        serialConnect.closeSerialPort();
    }
}
