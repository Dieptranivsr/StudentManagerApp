package android.bignerdranch.studentmanager.database;

import android.bignerdranch.studentmanager.model.Student;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

 interface DbHelper<T> {
      List<T> getList();
      T get(String id);
      long add(T object);
      int delete(String id);
      int update(T object);
      List<T> search(String search);
}
