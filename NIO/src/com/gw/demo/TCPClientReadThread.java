package com.gw.demo;  
  
import java.io.IOException;  
import java.nio.ByteBuffer;  
import java.nio.channels.SelectionKey;  
import java.nio.channels.Selector;  
import java.nio.channels.SocketChannel;  
import java.nio.charset.Charset;  
  
public class TCPClientReadThread implements Runnable {  
    private Selector selector;  
  
    public TCPClientReadThread(Selector selector) {  
        this.selector = selector;  
  
        new Thread(this).start();  
    }  
  
    @Override  
    public void run() {  
        try {  
            while (selector.select() > 0) {  
                // ����ÿ���п���IO����Channel��Ӧ��SelectionKey  
                for (SelectionKey sk : selector.selectedKeys()) {  
  
                    // �����SelectionKey��Ӧ��Channel���пɶ�������  
                    if (sk.isReadable()) {  
                        // ʹ��NIO��ȡChannel�е�����  
                        SocketChannel sc = (SocketChannel) sk.channel();  
                        ByteBuffer buffer = ByteBuffer.allocate(1024);  
                        sc.read(buffer);  
                        buffer.flip();  
  
                        // ���ֽ�ת��ΪΪUTF-16���ַ���  
                        String receivedString = Charset.forName("UTF-16").newDecoder().decode(buffer).toString();  
  
                        // ����̨��ӡ����  
                        System.out.println("���յ����Է�����" + sc.socket().getRemoteSocketAddress() + "����Ϣ:" + receivedString);  
  
                        // Ϊ��һ�ζ�ȡ��׼��  
                        sk.interestOps(SelectionKey.OP_READ);  
                    }  
  
                    // ɾ�����ڴ����SelectionKey  
                    selector.selectedKeys().remove(sk);  
                }  
            }  
        } catch (IOException ex) {  
            ex.printStackTrace();  
        }  
    }  
}  