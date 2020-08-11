package ir.thisisjag.jagshome;

import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static android.content.Context.WIFI_SERVICE;

class udp {
    private static DatagramSocket datagramSocket;
    private static DatagramSocket datagramSocket1;
    private static DatagramPacket packet;
    private static int[] gate;
    private static int type;
    private static int status;
    private static int id;
    private static byte subid;
    private static boolean send;
    private static boolean receive;

    public static boolean isReceive() {
        return receive;
    }

    public static void setReceive(boolean receive) {
        udp.receive = receive;
    }

    public static void setSend(boolean send) {
        udp.send = send;
    }

    public int getType() {
        return type;
    }

    public void setType(int type1) {
        type = type1;
    }

    public static int getStatus() {
        return status;
    }

    public static void setStatus(int status1) {
        status = status1;
    }

    public int getId() {
        return id;
    }

    public static void setId(int id1) {
        id = id1;
    }

    public byte getSubid() {
        return subid;
    }

    public static void setSubid(byte subid1) {
        subid = subid1;
    }

    private static InetAddress receiverAddress;

    public static class PrimeThread extends Thread {
        long minPrime;



        PrimeThread(long minPrime) {
            this.minPrime = minPrime;
       }

        public void run() {
            try {
                datagramSocket = new DatagramSocket(55666);
                //datagramSocket1 = new DatagramSocket(55666);
            } catch (SocketException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            int x = MainActivity.getGate();
            gate =new int[] {(x & 0xFF ),((x >> 8 ) & 0xFF),((x >> 16 ) & 0xFF),((x >> 24 ) & 0xFF)};
            Log.d("IP in udp activity", gate[0] +"."+ gate[1] +"."+ gate[2] +"."+ gate[3]);
            byte[] ipAddr = new byte[] {(byte) gate[0], (byte) gate[1], (byte) gate[2], (byte) 255 };
            try {
                receiverAddress = InetAddress.getByAddress(ipAddr);
                //datagramSocket.bind(receiverAddress);

            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            //if(send) {
                byte[] sendbuffer = new byte[]{(byte) 0xf2, (byte) 0xc0, 0x13, (byte) 0xff,
                        (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                        (byte) 0xff, (byte) 0xff, 0x00, 0x12, (byte) 0xc0, (byte) 0xa8,
                        (byte) 0x89, 0x69, 0x04, 0x11, (byte) status, (byte) id, subid, 0x0c,
                        (byte) 0xd2};
                packet = new DatagramPacket(
                        sendbuffer, sendbuffer.length, receiverAddress, 55666);

                try {
                    for (int i = 1; i < 3; i++) {
                        datagramSocket.send(packet);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                }
                send=false;
            //}
            while (!datagramSocket.isClosed()) {
                try {
                    byte[] buffer1 = new byte[64];
                    DatagramPacket packet1 = new DatagramPacket(buffer1, buffer1.length);
                    datagramSocket.receive(packet1);
                    if(buffer1[2]==0x2e){
                        Log.d("R type = ", String.valueOf(buffer1[45])); //type
                        type =buffer1[45];
                        Log.d("R status = ", String.valueOf(buffer1[46])); //status
                        status = buffer1[46];
                        //MainActivity.stss = status;
                        Log.d("R id = ", String.valueOf(buffer1[47])); //id
                        id = buffer1[47];
                        Log.d("R subid = ", String.valueOf(buffer1[48])); //subid
                        subid = buffer1[48];
                        MainActivity.setBg(status,MainActivity.idForBg);
                    }


                } catch (IOException e) {
                    Log.e("client has IOException", "error: ", e);

                }

            }

        }

    }
}
