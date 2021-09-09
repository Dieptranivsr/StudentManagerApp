package android.bignerdranch.studentmanager.database;

import android.bignerdranch.studentmanager.model.Student;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DbStudentHelper extends SQLiteOpenHelper implements DbHelper{

    public static final String DATABASE_NAME = "student";
    public static final int DATABASE_VERSION = 7;
    public static final String TABLE_NAME = "students";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BIRTHDAY = "birthday";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_CLASS_ID = "classID";
    public static final String COLUMN_PHONE_NUMBER = "phoneNumber";
    public static final String COLUMN_EMAIL = "email";

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME
            + " (" + COLUMN_ID + " text not null, " + COLUMN_NAME + " text not null, "
            + COLUMN_BIRTHDAY + " text not null, " + COLUMN_GENDER + " integer, "
            + COLUMN_PHONE_NUMBER + " text not null, "
            + COLUMN_EMAIL + " text not null, "
            + COLUMN_CLASS_ID + " text not null,"
            + " PRIMARY KEY ( " + COLUMN_ID + " , " + COLUMN_CLASS_ID + " ) " + " ); ";

    private static final String TAG = "DbzStudentHelper";

    public DbStudentHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    @Override
    public List<Student> getList() {
        List<Student> students = new ArrayList<Student>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_BIRTHDAY, COLUMN_GENDER,COLUMN_PHONE_NUMBER,COLUMN_EMAIL,COLUMN_CLASS_ID},
                null, null, null, null, COLUMN_NAME);

        if (c.moveToFirst()) {
            do {
                String id = c.getString(c.getColumnIndex(COLUMN_ID));
                String name = c.getString(c.getColumnIndex(COLUMN_NAME));
                String birthday = c.getString(c.getColumnIndex(COLUMN_BIRTHDAY));
                int gender = c.getInt(c.getColumnIndex(COLUMN_GENDER));
                String phoneNumber = c.getString(c.getColumnIndex(COLUMN_PHONE_NUMBER));
                String email = c.getString(c.getColumnIndex(COLUMN_EMAIL));
                String classID = c.getString(c.getColumnIndex(COLUMN_CLASS_ID));
                Student student = new Student(id, name, birthday, gender,phoneNumber,email,classID);
                students.add(student);
            } while (c.moveToNext());
            c.close();
        }

        db.close();
        return students;
    }

    public List<Student> getListInClass(String classIDInput) {
        List<Student> students = new ArrayList<Student>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_CLASS_ID + " = " + "'"+classIDInput+"'" ;
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                String id = c.getString(c.getColumnIndex(COLUMN_ID));
                String name = c.getString(c.getColumnIndex(COLUMN_NAME));
                String birthday = c.getString(c.getColumnIndex(COLUMN_BIRTHDAY));
                int gender = c.getInt(c.getColumnIndex(COLUMN_GENDER));
                String phoneNumber = c.getString(c.getColumnIndex(COLUMN_PHONE_NUMBER));
                String email = c.getString(c.getColumnIndex(COLUMN_EMAIL));
                String classID = c.getString(c.getColumnIndex(COLUMN_CLASS_ID));
                Student student = new Student(id, name, birthday, gender,phoneNumber,email,classID);
                students.add(student);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return students;
    }

    @Override
    public Student get(String id) {
        Student student;
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_ID + " = " + "'" + id + "'";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            String name = c.getString(c.getColumnIndex(COLUMN_NAME));
            String birthday = c.getString(c.getColumnIndex(COLUMN_BIRTHDAY));
            int gender = c.getInt(c.getColumnIndex(COLUMN_GENDER));
            String phoneNumber = c.getString(c.getColumnIndex(COLUMN_PHONE_NUMBER));
            String email = c.getString(c.getColumnIndex(COLUMN_EMAIL));
            String classID = c.getString(c.getColumnIndex(COLUMN_CLASS_ID));
            student = new Student(id, name, birthday, gender,phoneNumber,email,classID);
        } else
            student = null;
        c.close();
        db.close();
        return student;
    }

    @Override
    public long add(Object object) {
        Student student = (Student) object;
        long ret = -1;
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, student.getId());
        values.put(COLUMN_NAME, student.getName());
        values.put(COLUMN_BIRTHDAY, student.getBirthday());
        values.put(COLUMN_GENDER, student.getGender());
        values.put(COLUMN_PHONE_NUMBER, student.getPhoneNumber());
        values.put(COLUMN_EMAIL, student.getEmail());
        values.put(COLUMN_CLASS_ID,student.getClassID());
        SQLiteDatabase db = this.getWritableDatabase();
        ret = db.insert(TABLE_NAME, null, values);
        db.close();
        return ret;
    }


    @Override
    public int update(Object object) {
        Student student = (Student) object;
        int ret = -1;
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, student.getName());
        values.put(COLUMN_BIRTHDAY, student.getBirthday());
        values.put(COLUMN_GENDER, student.getGender());
        values.put(COLUMN_PHONE_NUMBER, student.getPhoneNumber());
        values.put(COLUMN_EMAIL, student.getEmail());
        values.put(COLUMN_CLASS_ID,student.getClassID());
        SQLiteDatabase db = this.getWritableDatabase();
        ret = db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{student.getId()});
        db.close();
        return ret;
    }
    @Override
    public int delete(String id) {
        int ret = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ret = db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{id});
        db.close();
        return ret;
    }
    @Override
    public List<Student> search(String searchText) {
        List<Student> students = new ArrayList<Student>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_NAME + " LIKE " + "'%" + searchText + "%'";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                String id = c.getString(c.getColumnIndex(COLUMN_ID));
                String name = c.getString(c.getColumnIndex(COLUMN_NAME));
                String birthday = c.getString(c.getColumnIndex(COLUMN_BIRTHDAY));
                int gender = c.getInt(c.getColumnIndex(COLUMN_GENDER));
                String phoneNumber = c.getString(c.getColumnIndex(COLUMN_PHONE_NUMBER));
                String email = c.getString(c.getColumnIndex(COLUMN_EMAIL));
                String classID = c.getString(c.getColumnIndex(COLUMN_CLASS_ID));
                Student student = new Student(id, name, birthday, gender,phoneNumber,email,classID);
                students.add(student);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return students;
    }
    public List<Student> searchStudent(String searchText, String classIDInput) {
        List<Student> students = new ArrayList<Student>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_CLASS_ID + " = " + "'"+classIDInput+"'"+ " AND " + COLUMN_NAME + " LIKE " + "'%" + searchText + "%'";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                String id = c.getString(c.getColumnIndex(COLUMN_ID));
                String name = c.getString(c.getColumnIndex(COLUMN_NAME));
                String birthday = c.getString(c.getColumnIndex(COLUMN_BIRTHDAY));
                int gender = c.getInt(c.getColumnIndex(COLUMN_GENDER));
                String phoneNumber = c.getString(c.getColumnIndex(COLUMN_PHONE_NUMBER));
                String email = c.getString(c.getColumnIndex(COLUMN_EMAIL));
                String classID = c.getString(c.getColumnIndex(COLUMN_CLASS_ID));
                Student student = new Student(id, name, birthday, gender,phoneNumber,email,classID);
                students.add(student);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return students;
    }

    public List<String> getListEmailsInClass(String classIDInput) {
        List<String> emails = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_CLASS_ID + " = " + "'"+classIDInput+"'" ;
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                String email = c.getString(c.getColumnIndex(COLUMN_EMAIL));
                emails.add(email);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return emails;
    }
}
