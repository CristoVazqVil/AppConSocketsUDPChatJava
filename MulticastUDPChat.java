import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.Scanner;

//Cristopher Vázquez Villa

public class MulticastUDPChat {

    @SuppressWarnings("deprecation")
    public static void main(String args[]) {
        try {
            int puerto = 8080;
            InetAddress grupo = InetAddress.getByName("224.0.0.0");
            MulticastSocket socket = new MulticastSocket(puerto);

            socket.joinGroup(grupo);

            Scanner scan = new Scanner(System.in);
            System.out.println("Ingresa tu nombre:");
            String nombre = scan.nextLine();
            System.out.println("Puedes escribir mensajes ahora:");

            Thread thread = new Thread(() -> {
                try {
                    while (true) {
                        byte[] buffer = new byte[1024];
                        DatagramPacket mensajeEntrada = new DatagramPacket(buffer, buffer.length);
                        socket.receive(mensajeEntrada);

                        String mensajeRecibido = new String(mensajeEntrada.getData(), 0, mensajeEntrada.getLength());
                        if (!mensajeRecibido.startsWith(nombre)) {
                            System.out.println(mensajeRecibido);
                        }
                    }
                } catch (IOException e) {

                }
            });
            thread.start();

            while (true) {
                String mensaje = scan.nextLine();
                if (mensaje.equalsIgnoreCase("Adios")) {
                    String mensajeDesconexion = nombre + " ha abandonado el chat.";
                    byte[] m = mensajeDesconexion.getBytes();
                    DatagramPacket mensajeSalida = new DatagramPacket(m, m.length, grupo, puerto);
                    socket.send(mensajeSalida);
                    break;
                }
                String mensajeConNombre = nombre + ": " + mensaje;
                byte[] m = mensajeConNombre.getBytes();
                DatagramPacket mensajeSalida = new DatagramPacket(m, m.length, grupo, puerto);
                socket.send(mensajeSalida);
            }

            socket.leaveGroup(grupo);
            socket.close();
            scan.close();

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            
        }
    }
}
