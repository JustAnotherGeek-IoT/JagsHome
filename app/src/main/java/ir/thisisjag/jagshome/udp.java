package ir.thisisjag.jagshome;


import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import android.util.Log;

class PrimeThread extends Thread{

    public void run(InetAddress strIP, byte[] str, int port){
        DatagramSocket socket = null;
        try
        {
            socket = new DatagramSocket();
            Log.d("IP Address1", strIP.toString());

            DatagramPacket packet;

            //send socket
            packet=new DatagramPacket(str,str.length,strIP,port);
            socket.send(packet);
        }
        catch(SocketException e)
        {
            e.printStackTrace();
            String error = e.toString();
            Log.e("Error by Sender", error);
        }
        catch(UnknownHostException e)
        {
            e.printStackTrace();
            String error = e.toString();
            Log.e("Error by Sender", error);
        }
        catch(IOException e)
        {
            e.printStackTrace();
            String error = e.toString();
            Log.e("Error by Sender", error);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            String error = e.toString();
            Log.e("Error by Sender", error);
        }
        finally{
            if(socket != null){

                socket.disconnect();
                socket.close();

            }
        }
    }
}