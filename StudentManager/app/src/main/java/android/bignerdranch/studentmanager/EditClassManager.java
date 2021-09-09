package android.bignerdranch.studentmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.bignerdranch.studentmanager.database.DbClassManagerHelper;
import android.bignerdranch.studentmanager.database.DbStudentHelper;
import android.bignerdranch.studentmanager.model.ClassManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class EditClassManager extends AppCompatActivity {
    private TextView tvTitle;
    private EditText etID, etName,etStart,etEnd,etRoom;
    private String mId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_class_manager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvTitle = (TextView) findViewById(R.id.tvTitleClass);
        etID = (EditText) findViewById(R.id.etIDClass);
        etName =(EditText) findViewById(R.id.etNameClass);
        etStart = (EditText) findViewById(R.id.etStart);
        etEnd = (EditText) findViewById(R.id.etEnd);
        etRoom = (EditText) findViewById(R.id.etClassRoom);
        mId = getIntent().getStringExtra(DbClassManagerHelper.COLUMN_CLASS_ID);
        if (mId == null) {
            // ADD MODE
            etID.requestFocus();
        } else {
            // EDIT MODE
            DbClassManagerHelper dbHelper = new DbClassManagerHelper(this, null);
            ClassManager classManager = dbHelper.get(mId);
            if (classManager != null) {
                tvTitle.setText(R.string.update_class);
                etID.setText(classManager.getClassID());
                etID.setEnabled(false);
                etName.setText(classManager.getClassName());
                etName.requestFocus();
                etStart.setText(classManager.getStart());
                etEnd.setText(classManager.getEnd());
                etRoom.setText(classManager.getClassRoom());
            }
        }

        etStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditClassManager.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        etStart.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        etEnd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditClassManager.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        etEnd.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mId == null)
            return super.onCreateOptionsMenu(menu);
        else {
            getMenuInflater().inflate(R.menu.menu_edit, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            DbClassManagerHelper dbHelper = new DbClassManagerHelper(this, null);
            if (dbHelper.delete(mId) > 0) {
                Toast.makeText(this, getString(R.string.deleted), Toast.LENGTH_LONG).show();
                navigateToClassManagersList();
            } else {
                Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clear(View v) {
        etName.setText("");
        if (mId == null) {
            // ADD MODE
            etID.setText("");
        } else {
            // EDIT MODE
            etName.requestFocus();
        }
        etID.requestFocus();

    }

    public void save(View v) {
        if (mId == null) {
            // ADD MODE
            addClassManager();
        } else {
            // EDIT MODE
            updateClassManager();
        }
    }

    private void addClassManager() {
        String mId = etID.getText().toString().trim();
        if (mId.length() == 0) {
            etID.setError("?");
            etID.requestFocus();
            return;
        }
        String name = etName.getText().toString().trim();
        if (name.length() == 0) {
            etName.setError("?");
            etName.requestFocus();
            return;
        }
        ClassManager classManager = new ClassManager();
        classManager.setClassID(mId);
        classManager.setClassName(name);
        classManager.setStart(etStart.getText().toString());
        classManager.setEnd(etEnd.getText().toString());
        classManager.setClassRoom(etRoom.getText().toString());
        DbClassManagerHelper dbHelper = new DbClassManagerHelper(this, null);
        if (dbHelper.add(classManager) > 0) {
            showToastMessage(getString(R.string.saved));
            navigateToClassManagersList();
            this.finish();
        } else {
            if (dbHelper.get(mId) != null)
                showToastMessage(getString(R.string.class_exits));
            else
                showToastMessage(getString(R.string.error));
        }
    }


    private void updateClassManager() {
        String mId = etID.getText().toString().trim();
        if (mId.length() == 0) {
            etID.setError("?");
            etID.requestFocus();
            return;
        }
        String name = etName.getText().toString().trim();
        if (name.length() == 0) {
            etName.setError("?");
            etName.requestFocus();
            return;
        }
        ClassManager classManager = new ClassManager();
        classManager.setClassID(mId);
        classManager.setClassName(name);
        classManager.setStart(etStart.getText().toString());
        classManager.setEnd(etEnd.getText().toString());
        classManager.setClassRoom(etRoom.getText().toString());
        DbClassManagerHelper dbHelper = new DbClassManagerHelper(this, null);
        if (dbHelper.update(classManager) > 0) {
            showToastMessage(getString(R.string.saved));
            navigateToClassManagersList();
            this.finish();
        } else {
                showToastMessage(getString(R.string.error));
        }
    }
    private void showToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void navigateToClassManagersList() {
        Intent i = new Intent(this, ListClassManagers.class);
        startActivity(i);
    }
    public void navigateToEmail(View v) {
        Intent i = new Intent(EditClassManager.this, SendMailActivity.class);
        i.putExtra(DbStudentHelper.COLUMN_CLASS_ID, mId);
        startActivity(i);
    }
    public void back(View v) {
       navigateToClassManagersList();
    }
}