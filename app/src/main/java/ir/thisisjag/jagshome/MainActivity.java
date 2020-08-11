package ir.thisisjag.jagshome;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    int REQUEST_ACCESS_NETWORK_STATE ;
    static int gate;
    static int idForBg;
    private static DBManager dbManager;
    static int stss;
    private TableLayout layout;
    //private int u;
    static final Button[] btnShow = new Button[128];
    private final boolean[] state = new boolean[128];
    private TextView saveUserDataButton = null;
    private TextView cancelUserDataButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WifiManager manager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        assert manager != null;
        DhcpInfo info = manager.getDhcpInfo();
        gate = info.gateway;
        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor1;
        cursor1 = dbManager.fetch();
        Log.d("index count onCreate ", String.valueOf(cursor1.getCount()));
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, REQUEST_ACCESS_NETWORK_STATE);
        } else {
            //TODO
        }
        if (cursor1.getCount() > 0) {
            //cursor1.moveToFirst();
            cursor1.move(-1);
            for (int h = 1; h <= cursor1.getCount(); h++) {
                if (cursor1.moveToNext()) {
                    btnCreate(h, cursor1.getInt(2), cursor1.getString(5), cursor1.getInt(3), cursor1.getInt(4), false);
                    Log.d("id = ", cursor1.getInt(1) + " Device ID = " + cursor1.getInt(2) + " Sub ID = " + cursor1.getInt(3) + " Status = " + cursor1.getInt(4) + " Name  = " + cursor1.getString(5));
                }
            }
        }
//        udp.PrimeThread pt = new udp.PrimeThread(0);
//        pt.start();

        if (udp.isReceive()) {
            setBg(udp.getStatus(), 1);
            udp.setReceive(false);
        }
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                startActivityForResult(intent, 1);// Activity is started with requestCode 2
            }
        });
    }


//    private void btnLoad() {
//        Cursor cursor1;
//        cursor1 = dbManager.fetch();
//        Log.d("index count onLoad ", String.valueOf(cursor1.getCount()));
//            //cursor1.moveToFirst();
//            cursor1.move(-1);
//            for (int h = 1; h <= cursor1.getCount(); h++) {
//                if (cursor1.moveToNext()) {
//                    btnCreate(h,cursor1.getInt(2), cursor1.getString(5),  cursor1.getInt(3), cursor1.getInt(4), true);
//                    Log.d("id = ", cursor1.getInt(1)+ " Device ID = " + cursor1.getInt(2) + " Sub ID = " + cursor1.getInt(3) + " Status = " + cursor1.getInt(4) + " Name  = "+ cursor1.getString(5));
//                }
//            }
//    }

    static int getGate() {
        return gate;
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
        btnShow[j] = new Button(this);
        btnShow[j].setId((j));

        if (!f) {
            btnShow[j].setText(message);
            btnShow[j].setTextColor(Color.WHITE);
            btnShow[j].setTextSize(24);
            btnShow[j].setBackground(this.getDrawable(R.drawable.btn));
            layout.addView(btnShow[j]);
        }
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
                //saveUserDataButton = popupView.findViewById(R.id.edt);
                cancelUserDataButton = popupView.findViewById(R.id.dlt);
                final PopupWindow popupWindow = new PopupWindow(popupView, 450, 200);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAtLocation(popupView, 1, 0, 0);
//                saveUserDataButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(getApplicationContext(), AddActivity.class);
//                        startActivityForResult(intent, 2);// Activity is started with requestCode 2
//                        popupWindow.dismiss();
//                    }
//                });

                cancelUserDataButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor cursor2;
                        cursor2 = dbManager.fetch();
                        Log.d("Remove View ", String.valueOf(i));
                        for (int x = 0; x <= cursor2.getCount(); x++) {
                            layout.removeView(btnShow[x]);
                            dbManager.delete(x);
                        }
                        popupWindow.dismiss();
                    }
                });
                return false;
            }
        });
    }


    static void setBg(int st, int i) {
        if (st == 2) {
            btnShow[i].setBackground(App.getContext().getDrawable(R.drawable.btnon));
        }
        if (st == 1) {
            btnShow[i].setBackground(App.getContext().getDrawable(R.drawable.btn));
        }
    }

    public void btnClick(View v) {
        int i = v.getId();
//
        idForBg=i;
        Cursor cursor;
        cursor = dbManager.fetch();
//
        //
        if (cursor != null) {
            //cursor.move(2);
            cursor.moveToPosition(i - 1);
            Log.d("id = ", cursor.getInt(1) + " Device ID = " + cursor.getInt(2) + " Sub ID = " + cursor.getInt(3) + " Status = " + cursor.getInt(4) + " Name  = " + cursor.getString(5));
            udp.setId(cursor.getInt(1));
            udp.setSubid((byte) cursor.getInt(3));
            cursor.close();
        }
        state[i] = !state[i];
        if (state[i]) {
            //setBg(2,i);
            udp.setStatus(0x82);
            udp.setSend(true);
            udp.PrimeThread pt = new udp.PrimeThread(0);
            pt.start();



            //Toast.makeText(this, "new born button!", Toast.LENGTH_LONG).show();
        } else {
            //setBg(1,i);
            udp.setStatus(0x81);
            udp.setSend(true);
            udp.PrimeThread pt = new udp.PrimeThread(1);
            pt.start();


            //Toast.makeText(this, "new born button!", Toast.LENGTH_LONG).show();
        }


    }


}
