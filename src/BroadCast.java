import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class BroadCast {
    private static final int SERVER_PORT = 1234;
    private static final int BROADCAST_PORT = 8888;

    public static void broadcastServerPresence() {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);

            // Get all IP addresses
            List<String> ipAddresses = getAllLocalIpAddresses();

            InetAddress localhost = InetAddress.getLocalHost();

            while (true) {

                var ipAddress = ipAddresses.get(ipAddresses.size() - 1);

                String message = localhost.getHostName() + ":" + ipAddress;

                String bd = getBroadcastAddress(ipAddress).toString();

                System.out.println(message);

                DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(),
                        InetAddress.getByName(bd.substring(1)), BROADCAST_PORT);
                socket.send(packet);

                Thread.sleep(1000); // Broadcast every second
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getAllLocalIpAddresses() {
        List<String> ipAddresses = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address.getAddress().length == 4) {
                        ipAddresses.add(address.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipAddresses;
    }

    private static InetAddress getBroadcastAddress(String ipAddress) throws UnknownHostException {
        try {
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getByName(ipAddress));
            if (networkInterface != null) {
                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    if (interfaceAddress.getBroadcast() != null) {
                        return interfaceAddress.getBroadcast();
                    }
                }
            }
        } catch (SocketException | NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
