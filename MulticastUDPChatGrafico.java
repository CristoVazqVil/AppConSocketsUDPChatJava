import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristopher Vázquez Villa
 */
public class MulticastUDPChatGrafico extends javax.swing.JFrame {

    private String nombre;
    private MulticastSocket socket;
    private InetAddress grupo;

    @SuppressWarnings("deprecation")
    public MulticastUDPChatGrafico() {
        initComponents();
        this.setSize(720, 480);
        nombre = JOptionPane.showInputDialog(null, "Ingresa tu nombre:");
    
        try {
            socket = new MulticastSocket(8080);
            grupo = InetAddress.getByName("224.0.0.0");
            socket.joinGroup(grupo);
    
            Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        byte[] buffer = new byte[1024];
                        DatagramPacket mensajeEntrada = new DatagramPacket(buffer, buffer.length);
                        socket.receive(mensajeEntrada);
    
                        String mensajeRecibido = new String(mensajeEntrada.getData(), 0, mensajeEntrada.getLength());
                        String remitente = mensajeRecibido.split(":")[0];
    
                        if (!remitente.equals(nombre)) {
                            chatArea.append(mensajeRecibido + "\n");
                        }
                        
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();

            chatArea.setEditable(false);
    
            enviarBoton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        String mensaje = mensajeField.getText();
                        byte[] m = (nombre + ": " + mensaje).getBytes();
                        DatagramPacket mensajeSalida = new DatagramPacket(m, m.length, grupo, 8080);
                        socket.send(mensajeSalida);
                        mensajeField.setText("");
                        
                        if (mensaje.trim().equalsIgnoreCase("Adios")) {
                            enviarMensaje(nombre + " ha abandonado el chat.");
                            System.exit(0); 
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void enviarMensaje(String mensaje) {
        try {
            byte[] m = mensaje.getBytes();
            DatagramPacket mensajeSalida = new DatagramPacket(m, m.length, grupo, 8080);
            socket.send(mensajeSalida);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
                         
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatArea = new javax.swing.JTextArea();
        mensajeField = new javax.swing.JTextField();
        enviarBoton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(720, 480));
        setPreferredSize(new java.awt.Dimension(720, 480));

        chatArea.setColumns(20);
        chatArea.setRows(5);
        chatArea.setName("chatArea");
        jScrollPane1.setViewportView(chatArea);

        mensajeField.setName("mensaje");

        enviarBoton.setFont(new java.awt.Font("Minecraft", 1, 18)); // NOI18N
        enviarBoton.setText("Enviar");
        enviarBoton.setName("enviar");

        jLabel1.setFont(new java.awt.Font("Minecraft", 1, 24)); // NOI18N
        jLabel1.setText("Super Chat Multicast ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(35, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(mensajeField, javax.swing.GroupLayout.PREFERRED_SIZE, 513, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(enviarBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(228, 228, 228))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 669, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(enviarBoton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(mensajeField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }                                                        

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
            java.util.logging.Logger.getLogger(MulticastUDPChatGrafico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MulticastUDPChatGrafico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MulticastUDPChatGrafico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MulticastUDPChatGrafico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MulticastUDPChatGrafico().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private javax.swing.JTextArea chatArea;
    private javax.swing.JButton enviarBoton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField mensajeField;
    // End of variables declaration                   
}
