package ir.thisisjag.jagshome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class AddActivity extends AppCompatActivity {
    int id = 0, sid = 0, j, s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        TextView bAuto = findViewById(R.id.bAuto);
        TextView bOk = findViewById(R.id.bOk);
        final TextView bCancel = findViewById(R.id.bCancel);
        final EditText tID = findViewById(R.id.tID);
        final EditText tsID = findViewById(R.id.tsID);
        final EditText tName = findViewById(R.id.tName);
        if (MainActivity.isEdt_flag()) {
            tName.setText(MainActivity.getEdt_name());
            tID.setText(MainActivity.getEdt_id());
            tsID.setText(MainActivity.getEdt_subid());
            bOk.setText("Edit");
        }
        //Log.d("Editing button = ",name);

        bAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Snackbar.make(arg0, "See you in next versions!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        bOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String message = tName.getText().toString();
                id = Integer.parseInt(tID.getText().toString());
                sid = Integer.parseInt(tsID.getText().toString());
                try {
                    Relays relays = new Relays(-1, Integer.parseInt(tID.getText().toString()), Integer.parseInt(tsID.getText().toString()), message, 0);
                    Toast.makeText(AddActivity.this, relays.toString(), Toast.LENGTH_SHORT).show();
                    DBHelper dbHelper1 = new DBHelper(getApplicationContext());
                    if (MainActivity.isEdt_flag()) {
                        String queryString = "SELECT * FROM " + DBHelper.TABLE_NAME + " WHERE " + DBHelper.NAME + "='" + MainActivity.getEdt_name() + "'";
                        SQLiteDatabase db = dbHelper1.getReadableDatabase();
                        Cursor cursor = db.rawQuery(queryString, null);
                        cursor.moveToFirst();
                        j = cursor.getInt(0);
                        s = cursor.getInt(3);
                        cursor.close();
                        db.close();
                        dbHelper1.update(j, id, sid, s, message);

                    } else
                        dbHelper1.insert(relays);
                } catch (Exception e) {
                    Snackbar.make(arg0, "Fill All Fields", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                Intent intent = new Intent();
                intent.putExtra("IDd", Integer.parseInt(tID.getText().toString()));
                intent.putExtra("ID", j);
                intent.putExtra("SID", Integer.parseInt(tsID.getText().toString()));
                intent.putExtra("initialState", 1);
                intent.putExtra("MESSAGE", tName.getText().toString());
                setResult(1, intent);
                Log.d("table id = ", message + "," + id + "," + sid);
                finish();//finishing activity
            }

        });
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", "Add Button cancelled");
                setResult(1, intent);
                finish();//finishing activity
            }
        });
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1) {
//            name = data.getStringExtra("NAME");
//            assert name != null;
//            Log.d("Editing button = ",name);
//
//        }
//    }
}
