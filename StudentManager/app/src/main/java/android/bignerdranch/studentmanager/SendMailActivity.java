package android.bignerdranch.studentmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.bignerdranch.studentmanager.database.DbClassManagerHelper;
import android.bignerdranch.studentmanager.database.DbStudentHelper;
import android.bignerdranch.studentmanager.model.Student;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class SendMailActivity extends AppCompatActivity {
    private String classID;
    private List<String> mListEmails;
    private EditText editTextEmail;
    private EditText editTextSubject;
    private EditText editTextMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);
        editTextSubject = (EditText) findViewById(R.id.editTextSubject);
        editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        classID = getIntent().getStringExtra(DbClassManagerHelper.COLUMN_CLASS_ID);
        mListEmails = new ArrayList<String>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getListStudentsData(classID);
    }

    public void getListStudentsData(String classID) {
        DbStudentHelper dbStudentHelper = new DbStudentHelper(this, null);
        mListEmails.clear();
        mListEmails.addAll(dbStudentHelper.getListEmailsInClass(classID));

    }
    public void sendMail(View v) {
        String[] emails = new String [mListEmails.size()];
        emails = mListEmails.toArray(emails);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                emails);
        emailIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, editTextSubject.getText().toString());
        emailIntent .putExtra(android.content.Intent.EXTRA_TEXT, editTextMessage.getText().toString());
        emailIntent.setType("message/rfc822");

        this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
    public void back (View v) {
        Intent i = new Intent(this, ListClassManagers.class);
        startActivity(i);
    }
}