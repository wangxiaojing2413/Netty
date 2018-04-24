package com.gw.demo;  
  
import java.io.IOException;  
import java.net.InetSocketAddress;  
import java.nio.channels.SelectionKey;  
import java.nio.channels.Selector;  
import java.nio.channels.ServerSocketChannel;  
import java.util.Iterator;  
import java.util.Set;  
  
public class TCPServer {  
    // ��������С  
    private static final int BufferSize = 1024;  
  
    // ��ʱʱ�䣬��λ����  
    private static final int TimeOut = 3000;  
  
    // ���ؼ����˿�  
    private static final int ListenPort = 1978;  
  
    public static void main(String[] args) throws IOException {  
        // ����ѡ����  
        Selector selector = Selector.open();  
  
        // �򿪼����ŵ�  
        ServerSocketChannel listenerChannel = ServerSocketChannel.open();  
  
        // �뱾�ض˿ڰ�  
        listenerChannel.socket().bind(new InetSocketAddress(ListenPort));  
  
        // ����Ϊ������ģʽ  
        listenerChannel.configureBlocking(false);  
  
        // ��ѡ�����󶨵������ŵ�,ֻ�з������ŵ��ſ���ע��ѡ����.����ע�������ָ�����ŵ����Խ���Accept����  
        //һ��server socket channel׼���ý����½�������ӳ�Ϊ�����վ�����  
        listenerChannel.register(selector, SelectionKey.OP_ACCEPT);  
  
        // ����һ������Э���ʵ����,�������������  
        TCPProtocol protocol = new TCPProtocolImpl(BufferSize);  
  
        // ����ѭ��,�ȴ�IO  
        while (true) {  
            // �ȴ�ĳ�ŵ�����(��ʱ)  
            int keys = selector.select(TimeOut);  
            //������ʱ�������0��client���Ӻ�һֱ���1  
            //System.out.print(keys);  
            if (keys == 0) {  
                System.out.println("���Եȴ�.");  
                continue;  
            }  
              
            /*if (selector.select(TimeOut) == 0) { 
                System.out.println("���Եȴ�."); 
                continue; 
            }*/  
              
            // ȡ�õ�����.selectedKeys()�а�����ÿ��׼����ĳһI/O�������ŵ���SelectionKey  
            Set<SelectionKey> set = selector.selectedKeys();  
            //���Ϊ1  
            //System.out.println("selectedKeysSize:" + set.size());  
            Iterator<SelectionKey> keyIter  = set.iterator();  
              
            while (keyIter.hasNext()) {  
                SelectionKey key = keyIter.next();  
  
                try {  
                    if (key.isAcceptable()) {  
                        System.out.println("acceptable");  
                        //�÷������ڲ����Ὣinterest��OP_ACCEPT��ΪOP_READ  
                        //�����ִ���������䣬���һֱ��accept״̬����ʼʱ����Ϊ��accept�����޷�������������if���  
                        //consoleһֱ��ӡ��������  
                        protocol.handleAccept(key);  
                    }  
  
                    if (key.isReadable()) {  
                        // �ӿͻ��˶�ȡ����  
                        System.out.println("readable");  
                        protocol.handleRead(key);  
                    }  
  
                    if (key.isValid() && key.isWritable()) {  
                        //�ͻ�������һ�κ�N����������÷���  
                        //System.out.println("writable");//�������  
                        protocol.handleWrite(key);  
                    }  
                } catch (IOException ex) {  
                    // ����IO�쳣����ͻ��˶Ͽ����ӣ�ʱ�Ƴ�������ļ�  
                    keyIter.remove();  
                    continue;  
                }  
  
                // �Ƴ�������ļ�  
                keyIter.remove();  
            }  
        }  
    }  
}  