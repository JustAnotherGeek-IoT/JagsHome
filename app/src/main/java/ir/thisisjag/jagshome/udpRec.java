package ir.thisisjag.jagshome;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import static ir.thisisjag.jagshome.DBHelper.IDd;
import static ir.thisisjag.jagshome.DBHelper.TABLE_NAME;

class Rec extends Thread {
    MainActivity context;
    //DatagramSocket server = null;

    DatagramSocket mSocket;

    public void run() {
        try {
            int port = 55666;
            mSocket = new DatagramSocket(port);
            byte[] buffer = new byte[128];
            // Create a packet to receive data into the buffer
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            // Now loop forever, waiting to receive packets and printing them.
            while (true) {
                // Wait to receive a datagram
                mSocket.receive(packet);
                if (buffer[2] == 0x2e) {
                    Log.d("R type = ", String.valueOf(buffer[45])); //type
                    Log.d("R status = ", String.valueOf(buffer[46])); //status
                    Log.d("R id = ", String.valueOf(buffer[47])); //id
                    Log.d("R subid = ", String.valueOf(buffer[48])); //subid
                    DBHelper dbHelper1 = new DBHelper(App.getContext());
                    String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE " + IDd + "=" + (int) buffer[47] + " AND " + DBHelper.SUBID + "=" + (int) buffer[48];
                    SQLiteDatabase db = dbHelper1.getReadableDatabase();
                    Cursor cursor = db.rawQuery(queryString, null);
                    cursor.moveToFirst();
                    if (buffer[46] == 2) {
                        MainActivity.setBg(1, cursor.getInt(0));

                    } else {
                        MainActivity.setBg(0, cursor.getInt(0));

                    }
                    Relays relays = new Relays(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(4), cursor.getInt(3));
                    Log.d("Received Relays = ", relays.toString());
                    cursor.close();
                    db.close();

                    //MainActivity.setBg();
                }
                // Convert the contents to a string, and display them
                String msg = new String(buffer, 0, packet.getLength());
                Log.d("x : ", packet.getAddress().getHostName() + ": "
                        + msg);
                System.out.println(packet.getAddress().getHostName() + ": "
                        + msg);

                // Reset the length of the packet before reusing it.
                packet.setLength(buffer.length);
            }


        } catch (Exception e) {
            System.err.println(e);
        }

    }

}