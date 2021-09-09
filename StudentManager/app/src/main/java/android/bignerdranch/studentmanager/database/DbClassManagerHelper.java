package android.bignerdranch.studentmanager.database;

import android.bignerdranch.studentmanager.model.ClassManager;
import android.bignerdranch.studentmanager.model.Student;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DbClassManagerHelper extends SQLiteOpenHelper implements DbHelper{

    public static final String DATABASE_NAME = "class_manager";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "class_manager";
    public static final String COLUMN_CLASS_ID = "classID";
    public static final String COLUMN_CLASS_NAME = "className";
    public static final String COLUMN_START_TIME = "startTime";
    public static final String COLUMN_END_TIME = "endTime";
    public static final String COLUMN_CLASS_ROOM = "classRoom";
    public static final String COLUMN_TOTAL_STUDENT = "totalStudent";

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME
            + " (" + COLUMN_CLASS_ID + " text primary key not null, " + COLUMN_CLASS_NAME + " text not null, "
            + COLUMN_START_TIME + " text not null, " + COLUMN_END_TIME + " text not null, " + COLUMN_CLASS_ROOM + " text not null, "
            + COLUMN_TOTAL_STUDENT + " integer);";

    private static final String TAG = "DbzClassManagerHelper";

    public DbClassManagerHelper(Context context, SQLiteDatabase.CursorFactory factory) {
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
    public List<ClassManager> getList() {
        List<ClassManager> classManagers = new ArrayList<ClassManager>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME, new String[]{COLUMN_CLASS_ID, COLUMN_CLASS_NAME, COLUMN_START_TIME,
                        COLUMN_END_TIME, COLUMN_CLASS_ROOM, COLUMN_TOTAL_STUDENT},
                null, null, null, null, COLUMN_CLASS_NAME);
        if (c.moveToFirst()) {
            do {
                String classID = c.getString(c.getColumnIndex(COLUMN_CLASS_ID));
                String className = c.getString(c.getColumnIndex(COLUMN_CLASS_NAME));
                String startTime = c.getString(c.getColumnIndex(COLUMN_START_TIME));
                String endTime = c.getString(c.getColumnIndex(COLUMN_END_TIME));
                String classRoom = c.getString(c.getColumnIndex(COLUMN_CLASS_ROOM));
                int totalStudent = c.getInt(c.getColumnIndex(COLUMN_TOTAL_STUDENT));
                ClassManager classManager = new ClassManager(classID,className,startTime,endTime,classRoom,totalStudent);
                classManagers.add(classManager);
            } while (c.moveToNext());
            c.close();
        }

        db.close();
        return classManagers;
    }
    @Override
    public ClassManager get(String id) {
        ClassManager classManager;
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_CLASS_ID+ " = " + "'" + id + "'";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            String className = c.getString(c.getColumnIndex(COLUMN_CLASS_NAME));
            String startTime = c.getString(c.getColumnIndex(COLUMN_START_TIME));
            String endTime = c.getString(c.getColumnIndex(COLUMN_END_TIME));
            String classRoom = c.getString(c.getColumnIndex(COLUMN_CLASS_ROOM));
            int totalStudent = c.getInt(c.getColumnIndex(COLUMN_TOTAL_STUDENT));
            classManager = new ClassManager(id,className,startTime,endTime,classRoom,totalStudent);
        } else
            classManager = null;
        c.close();
        db.close();
        return classManager;
    }
    @Override
    public long add(Object object) {
        ClassManager classManager = (ClassManager) object;
        long ret = -1;
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_ID, classManager.getClassID());
        values.put(COLUMN_CLASS_NAME, classManager.getClassName());
        values.put(COLUMN_START_TIME, classManager.getStart());
        values.put(COLUMN_END_TIME, classManager.getEnd());
        values.put(COLUMN_CLASS_ROOM,classManager.getClassRoom());
        values.put(COLUMN_TOTAL_STUDENT,classManager.getTotalStudent());
        SQLiteDatabase db = this.getWritableDatabase();
        ret = db.insert(TABLE_NAME, null, values);
        db.close();
        return ret;
    }
    @Override
    public int update(Object object) {
        ClassManager classManager = (ClassManager) object;
        int ret = -1;
        ContentValues values = new ContentValues();
        values.put(COLUMN_CLASS_NAME, classManager.getClassName());
        values.put(COLUMN_START_TIME, classManager.getStart());
        values.put(COLUMN_END_TIME, classManager.getEnd());
        values.put(COLUMN_CLASS_ROOM,classManager.getClassRoom());
        values.put(COLUMN_TOTAL_STUDENT,classManager.getTotalStudent());
        SQLiteDatabase db = this.getWritableDatabase();
        ret = db.update(TABLE_NAME, values, COLUMN_CLASS_ID + "=?", new String[]{classManager.getClassID()});
        db.close();
        return ret;
    }
    @Override
    public int delete(String id) {
        int ret = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ret = db.delete(TABLE_NAME, COLUMN_CLASS_ID + "=?", new String[]{id});
        db.close();
        return ret;
    }
    @Override
    public List<ClassManager> search(String searchText) {
        List<ClassManager> classManagers = new ArrayList<ClassManager>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE "
                + COLUMN_CLASS_NAME + " LIKE " + "'%" + searchText + "%'";
        Cursor c = db.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                String classID = c.getString(c.getColumnIndex(COLUMN_CLASS_ID));
                String className = c.getString(c.getColumnIndex(COLUMN_CLASS_NAME));
                String startTime = c.getString(c.getColumnIndex(COLUMN_START_TIME));
                String endTime = c.getString(c.getColumnIndex(COLUMN_END_TIME));
                String classRoom = c.getString(c.getColumnIndex(COLUMN_CLASS_ROOM));
                int totalStudent = c.getInt(c.getColumnIndex(COLUMN_TOTAL_STUDENT));
                ClassManager classManager = new ClassManager(classID,className,startTime,endTime,classRoom,totalStudent);
                classManagers.add(classManager);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return classManagers;
    }
}

