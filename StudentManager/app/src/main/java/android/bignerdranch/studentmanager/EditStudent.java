package android.bignerdranch.studentmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.bignerdranch.studentmanager.database.DbStudentHelper;
import android.bignerdranch.studentmanager.database.DbClassManagerHelper;
import android.bignerdranch.studentmanager.model.Student;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditStudent  extends AppCompatActivity {

    private TextView tvTitle;
    private EditText etID, etName, etBirthday, etPhoneNumber, etEmail;
    private RadioButton radMale, radFemale;
    private Date mBirthday = new Date();
    private String mId;
    private String classID;
    TelephonyManager mTelephonyManager;
    private MyPhoneCallListener mListener;
    private static String TAG = "EditStudentActivity";
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            etBirthday.setText(day + "/" + (month + 1) + "/" + year);
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day);
            mBirthday = cal.getTime();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);


        mTelephonyManager = (TelephonyManager)
                getSystemService(TELEPHONY_SERVICE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvTitle = (TextView) findViewById(R.id.tvTitleStudent);
        etID = (EditText) findViewById(R.id.etIDStudent);
        etName = (EditText) findViewById(R.id.etNameStudent);
        etBirthday = (EditText) findViewById(R.id.etBirthdayStudent);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etEmail = (EditText) findViewById(R.id.etEmail);
        radMale = (RadioButton) findViewById(R.id.radMaleStudent);
        radFemale = (RadioButton) findViewById(R.id.radFemaleStudent);
        classID = getIntent().getStringExtra(DbStudentHelper.COLUMN_CLASS_ID);
        mId = getIntent().getStringExtra(DbStudentHelper.COLUMN_ID);
        checkPhone();
        if (mId == null) {
            // ADD MODE
            setDefaultInfo();
        } else {
            // EDIT MODE
            DbStudentHelper dbHelper = new DbStudentHelper(this, null);
            Student student = dbHelper.get(mId);
            if (student != null) {
                tvTitle.setText(R.string.update_student);
                etID.setText(student.getId());
                etID.setEnabled(false);
                etName.setText(student.getName());
                etName.requestFocus();
                etPhoneNumber.setText(student.getPhoneNumber());
                etEmail.setText(student.getEmail());
                etBirthday.setText(student.getBirthday());
                if (student.getGender() == 0)
                    radFemale.setChecked(true);
                else
                    radMale.setChecked(true);
            }
        }

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
            DbStudentHelper dbHelper = new DbStudentHelper(this, null);
            if (dbHelper.delete(mId) > 0) {
                navigateToListStudent();
                Toast.makeText(this, getString(R.string.deleted), Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog(View v) {
        String strDate = etBirthday.getText() + "";
        String strArrtmp[] = strDate.split("/");
        int day = Integer.parseInt(strArrtmp[0]);
        int month = Integer.parseInt(strArrtmp[1]) - 1;
        int year = Integer.parseInt(strArrtmp[2]);
        DatePickerDialog datePicker = new DatePickerDialog(this, callback, year, month, day);
        datePicker.setTitle(R.string.ngaysinh);
        datePicker.show();
    }

    public void clear(View v) {
        if (mId == null) {
            // ADD MODE
            etID.setText("");
            etName.setText("");
            etPhoneNumber.setText("");
            etEmail.setText("");
            setDefaultInfo();
        } else {
            // EDIT MODE
            etName.setText("");
            etPhoneNumber.setText("");
            etEmail.setText("");
            Calendar cal = Calendar.getInstance();
            etBirthday.setText(getDateFormat(cal.getTime()));
            radMale.setChecked(true);
            etName.requestFocus();
        }

    }

    public void save(View v) {
        if (mId == null) {
            // ADD MODE
            addStudent();
        } else {
            // EDIT MODE
            updateStudent();
        }
    }
    public void back (View v) {
        Intent i = new Intent(this, ListStudents.class);
        i.putExtra(DbClassManagerHelper.COLUMN_CLASS_ID, classID);
        startActivity(i);
    }

    private void addStudent() {
        String mId = etID.getText().toString().trim();
        if (mId.length() == 0) {
            etID.setError("?");
            etID.requestFocus();
            return;
        }
        String name = etName.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString();
        String email = etEmail.getText().toString();
        if (name.length() == 0) {
            etName.setError("?");
            etName.requestFocus();
            return;
        }
        Student student = new Student();
        student.setId(mId);
        student.setName(name);
        student.setBirthday(getDateFormat(mBirthday));
        if (radMale.isChecked())
            student.setGender(1);
        else
            student.setGender(0);
        student.setClassID(classID);
        student.setPhoneNumber(phoneNumber);
        student.setEmail(email);

        DbStudentHelper dbHelper = new DbStudentHelper(this, null);
        if (dbHelper.add(student) > 0) {
            showToastMessage(getString(R.string.saved));
            navigateToListStudent();
            this.finish();
        } else {
            if (dbHelper.get(mId) != null)
                showToastMessage(getString(R.string.student_exists));
            else
                showToastMessage(getString(R.string.error));
        }
    }

    private void updateStudent() {
        String name = etName.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String email = etEmail.getText().toString();
        if (name.trim().length() == 0) {
            etName.setError("?");
            etName.requestFocus();
            return;
        }
        Student student = new Student();
        student.setId(mId);
        student.setName(name);
        student.setBirthday(getDateFormat(mBirthday));
        if (radMale.isChecked())
            student.setGender(1);
        else
            student.setGender(0);
        student.setClassID(classID);
        student.setPhoneNumber(phoneNumber);
        student.setEmail(email);

        DbStudentHelper dbHelper = new DbStudentHelper(this, null);
        if (dbHelper.update(student) > 0) {
            showToastMessage(getString(R.string.updated));
            navigateToListStudent();
            this.finish();
        } else {
            showToastMessage(getString(R.string.error));
        }
    }


    private void showToastMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void setDefaultInfo() {
        Calendar cal = Calendar.getInstance();
        etBirthday.setText(getDateFormat(cal.getTime()));
        radMale.setChecked(true);
        etID.requestFocus();
    }

    private String getDateFormat(Date date) {
        SimpleDateFormat dft = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dft.format(date);
    }

    private void navigateToListStudent() {
        Intent i = new Intent(this, ListStudents.class);
        System.out.println(classID);
        i.putExtra(DbClassManagerHelper.COLUMN_CLASS_ID, classID);
        startActivity(i);
    }

    public void call(View v) {
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
// Set the data for the intent as the phone number.
        callIntent.setData(Uri.parse(phoneNumber));
// If package resolves to an app, check for phone permission,
// and send intent.
        if (callIntent.resolveActivity(getPackageManager()) != null) {
            checkForPhonePermission();
            startActivity(callIntent);
        } else {
            Log.e(TAG, "Can't resolve app for ACTION_CALL Intent.");
        }
    }


    public void mess(View v) {

    }

    public void checkPhone() {
        if (isTelephonyEnabled()) {
            checkForPhonePermission();
            mListener = new MyPhoneCallListener();
            mTelephonyManager.listen(mListener,
                    PhoneStateListener.LISTEN_CALL_STATE);

        } else {
            Toast.makeText(this,
                    R.string.telephony_not_enabled,
                    Toast.LENGTH_LONG).show();
// Disable the call button.
            disableCallButton();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isTelephonyEnabled()) {
            mTelephonyManager.listen(mListener,
                    PhoneStateListener.LISTEN_NONE);
        }
    }
    private void disableCallButton() {
        Toast.makeText(this,
                "Phone calling disabled", Toast.LENGTH_LONG).show();
        Button callButton = (Button) findViewById(R.id.btnCall);
        callButton.setVisibility(View.INVISIBLE);
        if (isTelephonyEnabled()) {
           callButton.setVisibility(View.VISIBLE);
        }
    }

    private boolean isTelephonyEnabled() {
        if (mTelephonyManager != null) {
            if (mTelephonyManager.getSimState() ==
                    TelephonyManager.SIM_STATE_READY) {
                return true;
            }
        }
        return false;
    }

    private void checkForPhonePermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
// Permission not yet granted. Use requestPermissions().

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
// Permission already granted.
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (permissions[0].equalsIgnoreCase
                        (Manifest.permission.CALL_PHONE)
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
// Permission was granted.
                } else {
// Permission denied. Stop the app.

                    Toast.makeText(this,
                            getString(R.string.failure_permission),
                            Toast.LENGTH_SHORT).show();
// Disable the call button
                    disableCallButton();
                }
            }
        }
    }







    private class MyPhoneCallListener extends PhoneStateListener {
        private boolean returningFromOffHook = false;

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
// Define a string for the message to use in a toast.
            String message = getString(R.string.phone_status);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
// Incoming call is ringing
                    message = message +
                            getString(R.string.ringing) + incomingNumber;
                    Toast.makeText(EditStudent.this, message,
                            Toast.LENGTH_SHORT).show();
                    Log.i(TAG, message);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
// Phone call is active -- off the hook
                    message = message + getString(R.string.offhook);
                    Toast.makeText(EditStudent.this, message,
                            Toast.LENGTH_SHORT).show();
                    Log.i(TAG, message);
                    returningFromOffHook = true;
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
// Phone is idle before and after phone call.
// If running on version older than 19 (KitKat),
// restart activity when phone call ends.
                    message = message + getString(R.string.idle);
                    Toast.makeText(EditStudent.this, message,
                            Toast.LENGTH_SHORT).show();
                    Log.i(TAG, message);
                    if (returningFromOffHook) {
// No need to do anything if >= version K
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
// Restart the app.
                            Intent intent = getPackageManager()
                                    .getLaunchIntentForPackage(
                                    getPackageName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                    break;
                default:
                    message = message + "Phone off";
                    Toast.makeText(EditStudent.this, message,
                            Toast.LENGTH_SHORT).show();
                    Log.i(TAG, message);
                    break;
            }
        }
    }
}
