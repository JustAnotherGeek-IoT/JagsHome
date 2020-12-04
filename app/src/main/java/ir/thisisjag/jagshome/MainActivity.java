package ir.thisisjag.jagshome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.DhcpInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    int REQUEST_ACCESS_NETWORK_STATE;
    static int gate;
    static int idForBg;
    static String edt_id;
    static String edt_subid;
    static String edt_name;

    public MainActivity() throws IOException {
    }

    public static boolean isEdt_flag() {
        return edt_flag;
    }

    public static void setEdt_flag(boolean edt_flag) {
        MainActivity.edt_flag = edt_flag;
    }

    static boolean edt_flag;

    public static String getEdt_id() {
        return edt_id;
    }

    public static String getEdt_subid() {
        return edt_subid;
    }

    public static String getEdt_name() {
        return edt_name;
    }

    static InetAddress receiverAddress;
    //private static DBHelper dbManager;
    static int stss;
    private TableLayout layout;
    //private int u;
    static final Button[] btnShow = new Button[128];
    private final int[] state = new int[128];
    private TextView bt_edt = null;
    private TextView bt_dlt = null;
    private PrimeThread pt = new PrimeThread();
    private final Rec rec = new Rec();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        assert manager != null;
        DhcpInfo info = manager.getDhcpInfo();
        gate = info.gateway;
        int[] gate1 = new int[]{(gate & 0xFF), ((gate >> 8) & 0xFF), ((gate >> 16) & 0xFF), ((gate >> 24) & 0xFF)};
        Log.d("IP in udp activity", gate1[0] + "." + gate1[1] + "." + gate1[2] + "." + gate1[3]);
        byte[] ipAddr = new byte[]{(byte) gate1[0], (byte) gate1[1], (byte) gate1[2], (byte) 255};
        try {
            receiverAddress = InetAddress.getByAddress(ipAddr);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        rec.start();
        //rec.run();




        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_ACCESS_NETWORK_STATE);
        }
        DBHelper dbHelper1 = new DBHelper(MainActivity.this);
        if (dbHelper1.getNum() > 0) {
            String queryString = "SELECT * FROM " + DBHelper.TABLE_NAME;
            SQLiteDatabase db = dbHelper1.getReadableDatabase();
            Cursor cursor = db.rawQuery(queryString, null);
            Relays relays = null;
            if (cursor.moveToFirst()) {
                do {
                    relays = new Relays(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(4), cursor.getInt(3));
                    btnCreate(relays.get_id(), relays.getId(), relays.getName(), relays.getSubId(), relays.isState(), false);
                    setBg(cursor.getInt(3), cursor.getInt(0));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        //rec.run(receiverAddress,55666);

//        udp.PrimeThread pt = new udp.PrimeThread(0);
//        pt.start();

//        if (pt.isReceive()) {
//            setBg(pt.getStatus(), 1);
//            pt.setReceive(false);
//        }
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                startActivityForResult(intent, 1);// Activity is started with requestCode 2
                edt_flag = false;
            }
        });
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            String message = data.getStringExtra("MESSAGE");
            int idd = data.getIntExtra("IDd", -1);
            int sidd = data.getIntExtra("SID", -1);
            int sts = data.getIntExtra("initialState", -1);
            int u = data.getIntExtra("ID", -1);
//            Log.d("j = ", String.valueOf(j));
            assert message != null;
            if (message.equals("Add Button cancelled")) {
                Snackbar.make(this.findViewById(R.id.fab), "Add Button cancelled", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                btnCreate(u, idd, message, sidd, sts, false);
                Log.d("table id result = ", message + "," + idd + "," + sidd + "," + sts);

            }
        }
    }

    private void btnCreate(int j, int idd, String message, int sid, final int sts, boolean f) {
        layout = findViewById(R.id.active);
        // Create Button Dynamically
        if (!edt_flag) {
            btnShow[j] = new Button(this);
            btnShow[j].setId((j));
            layout.addView(btnShow[j]);

        }

        btnShow[j].setText(message);
        btnShow[j].setTextColor(Color.WHITE);
        btnShow[j].setTextSize(24);
        btnShow[j].setBackground(this.getDrawable(R.drawable.btn));
        setBg(state[j],j);
        Log.d("Button id = ", String.valueOf(j));

        btnShow[j].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnClick(v);
            }
        });
        btnShow[j].setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View t) {
                final int i = t.getId();
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert layoutInflater != null;
                View popupView = layoutInflater.inflate(R.layout.fragment_second, null);
                bt_edt = popupView.findViewById(R.id.edt);
                bt_dlt = popupView.findViewById(R.id.dlt);
                final PopupWindow popupWindow = new PopupWindow(popupView, 450, 200);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAtLocation(popupView, 1, 0, 0);
                bt_edt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = btnShow[i].getText().toString();
                        Log.d("Editing button(MA) = ", name);
                        DBHelper dbHelper1 = new DBHelper(MainActivity.this);
                        String queryString = "SELECT * FROM " + DBHelper.TABLE_NAME + " WHERE " + DBHelper.NAME + "='" + btnShow[i].getText().toString() + "'";
                        SQLiteDatabase db = dbHelper1.getReadableDatabase();
                        Cursor cursor = db.rawQuery(queryString, null);
                        cursor.moveToFirst();
                        edt_id = String.valueOf(cursor.getInt(1));
                        edt_subid = String.valueOf(cursor.getInt(2));
                        edt_name = name;
                        edt_flag = true;
                        cursor.close();
                        db.close();
                        Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                        startActivityForResult(intent, 1);// Activity is started with requestCode 2
                        popupWindow.dismiss();
                    }
                });

                bt_dlt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = btnShow[i].getText().toString();
                        DBHelper dbHelper1 = new DBHelper(MainActivity.this);
                        dbHelper1.delOne(name);
                        layout.removeView(btnShow[i]);
                        popupWindow.dismiss();
                    }
                });
                return true;
            }
        });
    }


    static void setBg(int st, int i) {
        if (st == 1) {
            btnShow[i].setBackground(App.getContext().getDrawable(R.drawable.btnon));
        }
        if (st == 0) {
            btnShow[i].setBackground(App.getContext().getDrawable(R.drawable.btn));
        }
    }

    public void btnClick(View v) {
        int i = v.getId();
        idForBg = i;

        DBHelper dbHelper1 = new DBHelper(MainActivity.this);
        if (dbHelper1.getNum() > 0) {
            String queryString = "SELECT * FROM " + DBHelper.TABLE_NAME + " WHERE " + DBHelper.NAME + "='" + btnShow[i].getText().toString() + "'";
            SQLiteDatabase db = dbHelper1.getReadableDatabase();
            Cursor cursor = db.rawQuery(queryString, null);
            cursor.moveToFirst();
            state[i] = cursor.getInt(3);
            if (state[i] == 0)
                state[i] = 1;
            else state[i] = 0;
            dbHelper1.update(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), state[i], cursor.getString(4));

            if (state[i] == 1) {
                byte[] sendBuffer = new byte[]{(byte) 0xf2, (byte) 0xc0, 0x13, (byte) 0xff,
                        (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                        (byte) 0xff, (byte) 0xff, 0x00, 0x12, (byte) 0xc0, (byte) 0xa8,
                        (byte) 0x89, 0x69, 0x04, 0x11, (byte) 0x82, (byte) cursor.getInt(1), (byte) cursor.getInt(2), 0x44, 0x46};
                //setBg(1, i);
                //pt.start();
                pt.run(receiverAddress, sendBuffer, 55666);

            } else {
                //setBg(0, i);
                byte[] sendBuffer = new byte[]{(byte) 0xf2, (byte) 0xc0, 0x13, (byte) 0xff,
                        (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
                        (byte) 0xff, (byte) 0xff, 0x00, 0x12, (byte) 0xc0, (byte) 0xa8,
                        (byte) 0x89, 0x69, 0x04, 0x11, (byte) 0x81, (byte) cursor.getInt(1), (byte) cursor.getInt(2), 0x44, 0x46};
                //pt.start();
                pt.run(receiverAddress, sendBuffer, 55666);

            }
            Relays relays = new Relays(cursor.getInt(0), cursor.getInt(1), cursor.getInt(2), cursor.getString(4), cursor.getInt(3));
            Log.d("Received Relays MA = ",relays.toString());

            cursor.close();
            db.close();

        }
    }


}
