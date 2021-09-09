package android.bignerdranch.studentmanager.adapter;
import android.bignerdranch.studentmanager.R;
import android.bignerdranch.studentmanager.model.ClassManager;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.bignerdranch.studentmanager.ListClassManagers;


import androidx.annotation.NonNull;

import java.util.List;

public class ClassManagerAdapter extends ArrayAdapter<ClassManager> {
    private Context mContext;
    private int mResourceID;
    private List<ClassManager> mListClassManagers;
    public ClassManagerAdapter(@NonNull Context context, int resource, @NonNull List<ClassManager> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResourceID = resource;
        this.mListClassManagers = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ClassManagerAdapter.viewHolder holder;
        if(convertView == null || convertView.getTag() == null){
            convertView = View.inflate(mContext,mResourceID,null);

            holder = new ClassManagerAdapter.viewHolder();
            holder.tvOrderClass = (TextView) convertView.findViewById(R.id.tvOrderClass);
            holder.tvIdClass = (TextView) convertView.findViewById(R.id.tvIdClass);
            holder.tvNameClass = (TextView) convertView.findViewById(R.id.tvNameClass);
            holder.tvTimeClass = (TextView) convertView.findViewById(R.id.tvTimeClass);
            holder.tvRoomClass = (TextView) convertView.findViewById(R.id.tvRoomClass);

            convertView.setTag(holder);
        }else
            holder = (ClassManagerAdapter.viewHolder) convertView.getTag();

        ClassManager classManager = mListClassManagers.get(position);

        holder.tvOrderClass.setText(String.valueOf(position + 1));
        holder.tvIdClass.setText(classManager.getClassID());

        // search text effect
        String name = classManager.getClassName();
        if(ListClassManagers.mSearchText != null){
            // equals ignore Case
            int startIndex = name.toLowerCase().indexOf(ListClassManagers.mSearchText.toLowerCase());
            if(startIndex >= 0){
                int endIndex = ListClassManagers.mSearchText.length();
                SpannableString textSpan = new SpannableString(name);
                textSpan.setSpan(new BackgroundColorSpan(Color.RED),startIndex,endIndex,SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.tvNameClass.setText(textSpan);
            }
        }else
            holder.tvNameClass.setText(classManager.getClassName());

        holder.tvTimeClass.setText(classManager.getStart()+ '-' + classManager.getEnd());
        holder.tvRoomClass.setText(classManager.getClassRoom());
        return convertView;
    }

    private class viewHolder{
        TextView tvOrderClass, tvIdClass, tvNameClass, tvTimeClass, tvRoomClass;
    }
}
