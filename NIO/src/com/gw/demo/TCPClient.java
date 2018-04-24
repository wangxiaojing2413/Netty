package com.gw.demo;  
  
import java.io.IOException;  
import java.net.InetSocketAddress;  
import java.nio.ByteBuffer;  
import java.nio.channels.SelectionKey;  
import java.nio.channels.Selector;  
import java.nio.channels.SocketChannel;  
  
public class TCPClient {  
    //�ŵ�ѡ����  
    private Selector selector;  
  
    // �������ͨ�ŵ��ŵ�  
    SocketChannel socketChannel;  
  
    // Ҫ���ӵķ�����Ip��ַ  
    private String hostIp;  
  
    // Ҫ���ӵ�Զ�̷������ڼ����Ķ˿�  
    private int hostListenningPort;  
  
    /** 
     * ���캯�� 
     *  
     * @param HostIp 
     * @param HostListenningPort 
     * @throws IOException 
     */  
    public TCPClient(String HostIp, int HostListenningPort) throws IOException {  
        this.hostIp = HostIp;  
        this.hostListenningPort = HostListenningPort;  
  
        initialize();  
    }  
  
    /** 
     * ��ʼ�� 
     *  
     * @throws IOException 
     */  
    private void initialize() throws IOException {  
        // �򿪼����ŵ�������Ϊ������ģʽ  
        socketChannel = SocketChannel.open(new InetSocketAddress(hostIp, hostListenningPort));  
        socketChannel.configureBlocking(false);  
  
        // �򿪲�ע��ѡ�������ŵ�  
        selector = Selector.open();  
        socketChannel.register(selector, SelectionKey.OP_READ);  
  
        // ������ȡ�߳�  
        new TCPClientReadThread(selector);  
    }  
  
    /** 
     * �����ַ����������� 
     *  
     * @param message 
     * @throws IOException 
     */  
    public void sendMsg(String message) throws IOException {  
        ByteBuffer writeBuffer = ByteBuffer.wrap(message.getBytes("UTF-16"));  
          
        int r = socketChannel.write(writeBuffer);  
        System.out.println("write return:" + r);  
        //socketChannel.  
    }  
  
    public static void main(String[] args) throws IOException {  
        TCPClient client = new TCPClient("localhost", 1978);  
        for(int i=0; i<10; i++){  
            client.sendMsg("Nio" + i);  
            /*try { 
                Thread.sleep(20); 
            } catch (InterruptedException e) { 
                // TODO Auto-generated catch block 
                e.printStackTrace(); 
            }*/  
        }  
          
    }  
}  
