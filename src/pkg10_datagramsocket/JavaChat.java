/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg10_datagramsocket;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import static pkg10_datagramsocket.JavaChat.HOST_MODE;

/**
 *
 * @author Pamungkas
 */
public class JavaChat extends javax.swing.JFrame {
    
    public static final int HOST_MODE = 0;
    public static final int CLIENT_MODE = 1;
    int mode;
    String Name;
    String roomname;
    InetAddress hostip;
    JavaChat chat;
    DatagramSocket socket;
    ArrayList<client> ClientList;
    byte[] b;
    /**
     * Creates new form JavaChat
     */
    public JavaChat() {
//        initComponents();
    }
    
    public JavaChat(String myname, int mod, String ip, String room) {
        
        try {
            initComponents();
            Name = myname;
            mode = mod;
            hostip = InetAddress.getByName(ip);
            roomname = room;
            ClientList = new ArrayList<>();
            txtChatBox.setEditable(false);
            btnSend.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String s = txtChat.getText();
                    if (s.equals("") == false) {
                        if (mode == HOST_MODE) {
                            broadcast(Name + ": " + s);
                        } else {
                            sendToHost(Name + ": " + s);
                        }
                        txtChat.setText("");
                    }
                }
            });

            if (mode == HOST_MODE) {
                socket = new DatagramSocket(37988);
                lbl_ip.setText("My Room IP : " + InetAddress.getLocalHost().getHostAddress());
                lblNama.setText("Nama : "+Name);
            } else {
                socket = new DatagramSocket();
                String reqresp = "!!^^" + Name + "^^!!";
                DatagramPacket pk = new DatagramPacket(reqresp.getBytes(), reqresp.length(), hostip, 37988);
                socket.send(pk);
                b = new byte[300];
                pk = new DatagramPacket(b, 300);
                socket.setSoTimeout(6000);
                socket.receive(pk);
                reqresp = new String(pk.getData());
                if (reqresp.contains("!!^^")) {
                    roomname = reqresp.substring(4, reqresp.indexOf("^^!!"));
                    lbl_ip.setText("Chat Room : " + roomname);
                    lblNama.setText("Nama : "+Name);
                    btnSend.setEnabled(true);
                    txtChat.setEnabled(true);
                } else {
                    JOptionPane.showMessageDialog(chat, "No response from the server");
                    System.exit(0);
                }
            }
            Messenger.start();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
    
    public void broadcast(String str) {
        try {
            DatagramPacket pack = new DatagramPacket(str.getBytes(), str.length());
            for (int i = 0; i < ClientList.size(); i++) {
                pack.setAddress(InetAddress.getByName(ClientList.get(i).ip));
                pack.setPort(ClientList.get(i).port);
                socket.send(pack);
            }
            txtChatBox.setText(txtChatBox.getText() + "\n" + str);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(chat, ex);
        }
    }

    public void sendToHost(String str) {
        DatagramPacket pack = new DatagramPacket(str.getBytes(), str.length(), hostip, 37988);
        try {
            socket.send(pack);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(chat, "Sending to server failed");
        }
    }

    Thread Messenger = new Thread() {
        public void run() {
            try {
                while (true) {
                    b = new byte[300];
                    DatagramPacket pkt = new DatagramPacket(b, 300);
                    socket.setSoTimeout(0);
                    socket.receive(pkt);
                    String s = new String(pkt.getData());
                    if (mode == HOST_MODE) {
                        if (s.contains("!!^^")) {
                            client temp = new client();
                            temp.ip = pkt.getAddress().getHostAddress();
                            temp.port = pkt.getPort();
                            broadcast(s.substring(4, s.indexOf("^^!!")) + " Bergabung.");
                            ClientList.add(temp);
                            s = "!!^^" + roomname + "^^!!";
                            pkt = new DatagramPacket(s.getBytes(), s.length(), InetAddress.getByName(temp.ip), temp.port);
                            socket.send(pkt);
                            btnSend.setEnabled(true);
                            txtChat.setEnabled(true);
                        } else {
                            broadcast(s);
                        }
                    } else {
                        txtChatBox.setText(txtChatBox.getText() + "\n" + s);
                    }
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(chat, ex);
                System.exit(0);
            }
        }
    };

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtChat = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtChatBox = new javax.swing.JTextArea();
        lbl_ip = new javax.swing.JLabel();
        lblNama = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtChatActionPerformed(evt);
            }
        });

        btnSend.setText("Send");

        txtChatBox.setColumns(20);
        txtChatBox.setRows(5);
        jScrollPane1.setViewportView(txtChatBox);

        jScrollPane2.setViewportView(jScrollPane1);

        lbl_ip.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lbl_ip.setText("IP Address : ");

        lblNama.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblNama.setText("Nama : ");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Java Chat");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtChat)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSend, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(130, 130, 130)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblNama)
                                    .addComponent(lbl_ip))))
                        .addGap(0, 154, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(lbl_ip)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNama)
                .addGap(7, 7, 7)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtChat, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(4, 4, 4)))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtChatActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtChatActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JavaChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JavaChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JavaChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JavaChat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JavaChat().setVisible(true);
            }
        });
        
        try {
            String host = "", room = "";
            String name = JOptionPane.showInputDialog("Masukkan namamu");
            if (name == null || name.equals("")) {
                JOptionPane.showMessageDialog(null, "Name tidak boleh kosong");
                return;
            }
            int mode = JOptionPane.showConfirmDialog(null, "Buat Room Chat Sendiri? atau Join Room Chat?\nYa - Buat Room\nTidak - Join Room Chat", "Buat atau Join?", JOptionPane.YES_NO_OPTION);
            if (mode == 1) {
                host = JOptionPane.showInputDialog("Masukan alamat IP Address Room");
                if (host == null || host.equals("")) {
                    JOptionPane.showMessageDialog(null, "IP room tidak ada");
                    return;
                }
            } else {
                room = JOptionPane.showInputDialog("Nama Roommu");
            }
            JavaChat obj = new JavaChat(name, mode, host, room);
            obj.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSend;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblNama;
    private javax.swing.JLabel lbl_ip;
    private javax.swing.JTextField txtChat;
    private javax.swing.JTextArea txtChatBox;
    // End of variables declaration//GEN-END:variables
}
