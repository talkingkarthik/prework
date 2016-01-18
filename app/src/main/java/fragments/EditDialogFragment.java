package fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.kartikkulkarni.prework.R;

import java.util.ArrayList;
import java.util.List;

import models.ToDoItem;


public class EditDialogFragment extends DialogFragment {

    private int position;
    private EditText mEditText;
    private EditText mEditTitle;
    private TimePicker mTime;
    private Button saveBtn;
    private CheckBox highPri;
    private BaseAdapter la;

    private ArrayList<ToDoItem> list;

    public static EditDialogFragment newInstance(ArrayList<ToDoItem> list, int position, BaseAdapter la) {
        EditDialogFragment frag = new EditDialogFragment();
        frag.setList(list);
        Bundle args = new Bundle();
        args.putInt("position", position);
        frag.setArguments(args);
        frag.setLA(la);
        return frag;
    }

    public void setLA(BaseAdapter la) {
        this.la = la;
    }

    public void setList(ArrayList<ToDoItem> list) {
        this.list = list;
    }

    public List getList(List list) {
        return this.list;
    }

    public void EditNameDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditTitle = (EditText) view.findViewById(R.id.name);
        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        mTime = (TimePicker) view.findViewById(R.id.timePicker);
        saveBtn = (Button) view.findViewById(R.id.save_btn);
        highPri = (CheckBox) view.findViewById(R.id.highpri);

        // Fetch arguments from bundle and set title
        position = getArguments().getInt("position");

        getDialog().setTitle("Add/Edit ToDO");
        mEditTitle.setText(list.get(position).getTitle());
        mEditText.setText(list.get(position).getText());
        //mTime.setHour(((Date)Date.parse(list.get(position).getTime())).getHours());
        //mTime.setMinute((Date.parse(list.get(position).getTime())).getMinutes() );
        highPri.setChecked(list.get(position).isHighpri());

        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).setText(mEditText.getText().toString());
                list.get(position).setTitle(mEditTitle.getText().toString());
                list.get(position).setHighpri(highPri.isChecked());
                la.notifyDataSetChanged();
                dismiss();
            }
        });
    }
}