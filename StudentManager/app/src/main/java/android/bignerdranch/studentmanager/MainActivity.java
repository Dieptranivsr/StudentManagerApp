package android.bignerdranch.studentmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {
    EditText edituser,editpass;
    ImageView imgadmin,imguser;
    Button bntlogin;
    CheckBox check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edituser = (EditText) findViewById(R.id.edituser);
        editpass = (EditText) findViewById(R.id.editpass);
        imgadmin = (ImageView) findViewById(R.id.imgviewanh);
        imguser = (ImageView) findViewById(R.id.imgviewanh);
        check = (CheckBox) findViewById(R.id.check) ;
        bntlogin = (Button) findViewById(R.id.btnLogin);
//        Intent intent = new Intent(this,ListClassManagers.class);
        Intent intent = new Intent(this,NavActivity.class);
        bntlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u= edituser.getText().toString();
                String p= editpass.getText().toString();
                boolean ck = check.isChecked();
                if (u.equalsIgnoreCase("admin") && p.equalsIgnoreCase("admin")) {
                    saveLogin(u, p);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
    public void saveLogin(String user, String pass) {
        SharedPreferences sharedPre = getSharedPreferences("fileLogin", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPre.edit();
        String u = edituser.getText().toString();
        String p = editpass.getText().toString();
        boolean ck = check.isChecked();
        if (!ck) {
            edit.clear();
        } else {
            edit.putString("user", user);
            edit.putString("pass", pass);
            edit.putBoolean("Savetatus", ck);
        }
        edit.commit();
    }
}