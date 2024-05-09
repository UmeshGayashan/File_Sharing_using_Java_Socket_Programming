import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class BroadCast {
    private static final int SERVER_PORT = 1234;
    private static final int BROADCAST_PORT = 8888;

    public static void broadcastServerPresence() {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);

            // Get LAN IP address
            String lanIpAddress = getLocalIpAddress();
            InetAddress localhost = InetAddress.getLocalHost();

            // Get the host name
            String hostName = localhost.getHostName();

            while (true) {
                String message = "Server:" + lanIpAddress + ":" + "HostName :" + hostName;
                DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(),
                        InetAddress.getByName("255.255.255.255"), BROADCAST_PORT);
                socket.send(packet);

                Thread.sleep(1000); // Broadcast every second
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
}
