package ir.thisisjag.jagshome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class AddActivity extends AppCompatActivity {
int id = 0,sid=0,j;
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
                String message=tName.getText().toString();
                if(tID.getText().toString().length()>0 && tsID.getText().toString().length()>0) {
                    id = Integer.parseInt(tID.getText().toString());
                    sid = Integer.parseInt(tsID.getText().toString());
                }
                if((message.length() > 0) && (id != 0) && (sid != 0) ) {



                    DBManager dbManager = new DBManager(getApplicationContext());
                    dbManager.open();
                    Cursor cursor3;
                    cursor3 = dbManager.fetch();
                    if(cursor3.getCount()>0){
                        cursor3.moveToLast();
                        j=cursor3.getCount()+1;
                        cursor3.close();
Log.d("last id is : ",String.valueOf(j));
                        DBManager.insert(j,id,sid,1,message);
                    }else{
                        j++;
                    DBManager.insert(j,id,sid,1,message);}
                    Intent intent = new Intent();
                    intent.putExtra("IDd",id);
                    intent.putExtra("ID",j);
                    intent.putExtra("SID",sid);
                    intent.putExtra("initialState",1);
                    intent.putExtra("MESSAGE", message);
                    setResult(1, intent);
                    Log.d("table id = ",message + ","+ id + ","+ sid );
                    finish();//finishing activity
                }else
                    Snackbar.make(arg0, "Fill All Fields", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent=new Intent();
                intent.putExtra("MESSAGE","Add Button cancelled");
                setResult(1,intent);
                finish();//finishing activity
            }
        });

    }

}
