package adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.kartikkulkarni.prework.R;

import java.util.ArrayList;

import database.CouchbaseDAO;
import fragments.EditDialogFragment;
import models.ToDoItem;

public class ToDoItemsAdapter extends BaseAdapter implements ListAdapter {

    final String TAG = "CouchbaseEvents";
    private FragmentManager fm;
    private ArrayList<ToDoItem> list;
    private Context context;
    private CouchbaseDAO dao = null;

    public ToDoItemsAdapter(ArrayList<ToDoItem> list, Context context, FragmentManager f) {
        this.list = list;
        this.context = context;
        this.fm = f;
        this.dao= CouchbaseDAO.getInstance(context);
    }

    public void showEditDialog(int pos) {
        EditDialogFragment editDialogFragment = EditDialogFragment.newInstance(list, pos, this);
        getItem(pos);
        editDialogFragment.show(fm, "fragment_edit_name");
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
        TextView listPriText = (TextView) view.findViewById(R.id.list_pri_string);
        TextView listDueText = (TextView) view.findViewById(R.id.list_due_string);

        listItemText.setText(list.get(position).getTitle());
        listPriText.setText((list.get(position).isHighpri()) ? "HIGH" : "LOW");
        listPriText.setTextColor((list.get(position).isHighpri()) ? Color.RED : Color.BLUE);
        listDueText.setText(list.get(position).getTimestamp().toString());

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button) view.findViewById(R.id.delete_btn);
        Button addBtn = (Button) view.findViewById(R.id.add_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                if (!list.get(position).isNewly()) {
                    dao.deleteDocument(list.get(position).getDocid());
                }
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(position);
            }
        });
        return view;
    }
}
