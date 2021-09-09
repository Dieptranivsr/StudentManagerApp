package android.bignerdranch.studentmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.bignerdranch.studentmanager.adapter.ClassManagerAdapter;
import android.bignerdranch.studentmanager.database.DbClassManagerHelper;
import android.bignerdranch.studentmanager.model.ClassManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListClassManagers extends Fragment implements SearchView.OnQueryTextListener {
    private ListView lvListClassManagers;
    private List<ClassManager> mListClassManagers;
    private ClassManagerAdapter mAdapter;
    public static String mSearchText = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_list_class_managers, container, false);
//        setContentView(R.layout.activity_list_class_managers);
        ImageButton button = (ImageButton) v.findViewById(R.id.btn_addi);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),EditClassManager.class);
                startActivity(i);
            }
        });
        lvListClassManagers =(ListView) v.findViewById(R.id.lvListCLassManagers);
        mListClassManagers = new ArrayList<>();
        mAdapter = new ClassManagerAdapter(this.getActivity(), R.layout.class_manager_row, mListClassManagers);
        lvListClassManagers.setAdapter(mAdapter);
        refreshListClassManagersData();
        addEventListener();
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("Insert name");
        searchView.setOnQueryTextListener(this);

        super.onCreateOptionsMenu(menu, inflater);
    }

    public void refreshListClassManagersData() {
        DbClassManagerHelper dbClassManagerHelper = new DbClassManagerHelper(getActivity(), null);
        mListClassManagers.clear();
        mListClassManagers.addAll(dbClassManagerHelper.getList());
        mAdapter.notifyDataSetChanged();
    }

//    public void addClassManager() {
//        Intent i = new Intent(getActivity(),EditClassManager.class);
//        startActivity(i);
//    }

    private void addEventListener() {
        lvListClassManagers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ClassManager classManager = mListClassManagers.get(position);
                Intent i = new Intent(getActivity(), ListStudents.class);
                i.putExtra(DbClassManagerHelper.COLUMN_CLASS_ID, classManager.getClassID());
                startActivity(i);

            }
        });
        lvListClassManagers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position >= 0) {
                    ClassManager classManager = mListClassManagers.get(position);
                    Intent i = new Intent(getActivity(), EditClassManager.class);
                    i.putExtra(DbClassManagerHelper.COLUMN_CLASS_ID, classManager.getClassID());
                    startActivity(i);
                }
                return true;
            }
        });
    }


    private void deleteClassManager(final String id) {

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle(R.string.delete).setMessage(getString(R.string.delete_message) + " - " + id + " ?")
                .setIcon(android.R.drawable.ic_delete)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DbClassManagerHelper dbClassManagerHelper = new DbClassManagerHelper(getActivity(), null);
                        if (dbClassManagerHelper.delete(id) > 0) {
                            Toast.makeText(getActivity(), getString(R.string.deleted), Toast.LENGTH_LONG).show();
                            refreshListClassManagersData();
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
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
        DbClassManagerHelper dbHelper = new DbClassManagerHelper(getActivity(), null);
        mListClassManagers.clear();
        mListClassManagers.addAll(dbHelper.search(newText));
        mAdapter.notifyDataSetChanged();
        return true;
    }
}