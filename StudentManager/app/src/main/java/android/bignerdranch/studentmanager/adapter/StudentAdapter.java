package android.bignerdranch.studentmanager.adapter;

import android.bignerdranch.studentmanager.ListStudents;
import android.bignerdranch.studentmanager.R;
import android.bignerdranch.studentmanager.model.Student;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {

    private Context mContext;
    private int mResourceID;
    private List<Student> mListStudents;

    public StudentAdapter(Context context, int resource, List<Student> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResourceID = resource;
        this.mListStudents = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        viewHolder holder;
        if(convertView == null || convertView.getTag() == null){
            convertView = View.inflate(mContext,mResourceID,null);

            holder = new viewHolder();
            holder.tvOrderStudent = (TextView) convertView.findViewById(R.id.tvOrderStudent);
            holder.tvIdStudent = (TextView) convertView.findViewById(R.id.tvIdStudent);
            holder.tvNameStudent = (TextView) convertView.findViewById(R.id.tvNameStudent);
            holder.tvBirthdayStudent = (TextView) convertView.findViewById(R.id.tvBirthdayStudent);
            holder.ivGenderStudent = (ImageView) convertView.findViewById(R.id.ivGenderStudent);
            convertView.setTag(holder);
        }else
            holder = (viewHolder) convertView.getTag();

        Student student = mListStudents.get(position);

        holder.tvOrderStudent.setText(String.valueOf(position + 1));
        holder.tvIdStudent.setText(student.getId());
        // search text effect
//        String name = student.getName();
//        if(ListStudents.mSearchText != null){
//            // equals ignore Case
//            int startIndex = name.toLowerCase().indexOf(ListStudents.mSearchText.toLowerCase());
//            if(startIndex >= 0){
//                int endIndex = ListStudents.mSearchText.length();
//                SpannableString textSpan = new SpannableString(name);
//                textSpan.setSpan(new BackgroundColorSpan(Color.RED),startIndex,endIndex,SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
//                holder.tvNameStudent.setText(textSpan);
//            }
//        }else
            holder.tvNameStudent.setText(student.getName());

        holder.tvBirthdayStudent.setText(student.getBirthday());
        if(student.getGender() == 0)
            holder.ivGenderStudent.setImageResource(R.drawable.girlicon);
        else
            holder.ivGenderStudent.setImageResource(R.drawable.boyicon);

        return convertView;
    }

    private class viewHolder{
        TextView tvOrderStudent, tvIdStudent, tvNameStudent, tvBirthdayStudent;
        ImageView ivGenderStudent;
    }
}

