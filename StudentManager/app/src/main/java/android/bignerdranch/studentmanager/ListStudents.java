package android.bignerdranch.studentmanager;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.bignerdranch.studentmanager.adapter.StudentAdapter;
import android.bignerdranch.studentmanager.database.DbClassManagerHelper;
import android.bignerdranch.studentmanager.database.DbStudentHelper;
import android.bignerdranch.studentmanager.database.DbStudentHelper;
import android.bignerdranch.studentmanager.model.Student;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListStudents extends AppCompatActivity implements SearchView.OnQueryTextListener {

private ListView lvListStudents;
private List<Student> mListStudents;
private StudentAdapter mAdapter;
private TextView tvStudentCount;
public static String mSearchText = null;
String classID;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_students);

        classID = getIntent().getStringExtra(DbStudentHelper.COLUMN_CLASS_ID);
        tvStudentCount =(TextView) findViewById(R.id.tvStudentCount);
        lvListStudents = (ListView) findViewById(R.id.lvListStudents);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mListStudents = new ArrayList<>();
        mAdapter = new StudentAdapter(this, R.layout.student_row, mListStudents);
        lvListStudents.setAdapter(mAdapter);
        refreshListStudentsData(classID);
        addEventListener();
        }

@Override
public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Insert name");
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
        }

public void refreshListStudentsData(String classID) {
        DbStudentHelper dbStudentHelper = new DbStudentHelper(this, null);
        mListStudents.clear();
        mListStudents.addAll(dbStudentHelper.getListInClass(classID));
        tvStudentCount.setText(Integer.toString(mListStudents.size()));
        mAdapter.notifyDataSetChanged();
        }

public void addStudent(View v) {
        Intent i = new Intent(this,EditStudent.class);
        i.putExtra(DbClassManagerHelper.COLUMN_CLASS_ID, classID);
        startActivity(i);
        }

private void addEventListener() {
//        lvListStudents.setOnTouchListener(swipeDetector);
        lvListStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {

@Override
public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Student student = mListStudents.get(position);
        Intent i = new Intent(ListStudents.this, EditStudent.class);
        i.putExtra(DbStudentHelper.COLUMN_ID, student.getId());
        i.putExtra(DbStudentHelper.COLUMN_CLASS_ID, student.getClassID());
        startActivity(i);

        }
        });
        lvListStudents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
@Override
public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (position >= 0) {
        deleteStudent(mListStudents.get(position).getId());
        }
        return true;
        }
        });
        }


private void deleteStudent(final String id) {

        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(R.string.delete).setMessage(getString(R.string.delete_message) + " - " + id + " ?")
        .setIcon(android.R.drawable.ic_delete)
        .setNegativeButton(R.string.no, null)
        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialogInterface, int i) {
        DbStudentHelper dbStudentHelper = new DbStudentHelper(ListStudents.this, null);
        if (dbStudentHelper.delete(id) > 0) {
        Toast.makeText(ListStudents.this, getString(R.string.deleted), Toast.LENGTH_LONG).show();
        refreshListStudentsData(classID);
        } else {
        Toast.makeText(ListStudents.this, getString(R.string.error), Toast.LENGTH_LONG).show();
        }
        }
        });
        b.create().show();
        }

@Override
public boolean onQueryTextSubmit(String query) {
        return false;
        }

@Override
public boolean onQueryTextChange(String newText) {
        mSearchText = newText;
        DbStudentHelper dbHelper = new DbStudentHelper(this, null);
        mListStudents.clear();
        mListStudents.addAll(dbHelper.searchStudent(newText,classID));
        mAdapter.notifyDataSetChanged();
        return true;
        }
        public void back(View v) {
                Intent i = new Intent(this, ListClassManagers.class);
                startActivity(i);
        }

        }