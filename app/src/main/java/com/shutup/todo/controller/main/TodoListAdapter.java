package com.shutup.todo.controller.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shutup.todo.R;
import com.shutup.todo.common.Constants;
import com.shutup.todo.common.DateUtils;
import com.shutup.todo.model.persist.Todo;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by shutup on 2017/1/5.
 */

public class TodoListAdapter extends RecyclerView.Adapter<MyViewHolder> implements Constants{
    private Context mContext;
    private List<Todo> mTodos;
    private int type;

    public TodoListAdapter(Context context, List<Todo> todos) {
        mContext = context;
        mTodos = todos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final int pos = position;
        Todo todo = mTodos.get(position);
        holder.mTodoContent.setText(todo.getTodo());
        holder.mTodoCreateTime.setText(DateUtils.formatDate(todo.getCreatedAt()));
        if (type == ACTIVITY_EDIT) {
            holder.mCheckboxLayout.setVisibility(View.VISIBLE);
            holder.mCheckbox.setChecked(todo.isChecked());
            holder.mCheckbox.setTag(todo);
            holder.mCheckbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;

                    Todo todo1 = (Todo) cb.getTag();
                    todo1.setChecked(cb.isChecked());

                    mTodos.get(pos).setChecked(cb.isChecked());
                }
            });
        }else {
            holder.mCheckboxLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return mTodos.size();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<Todo> getTodos() {
        return mTodos;
    }

    public void setTodos(List<Todo> todos) {
        mTodos = todos;
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    @InjectView(R.id.todoContent)
    TextView mTodoContent;
    @InjectView(R.id.todoCreateTime)
    TextView mTodoCreateTime;
    @InjectView(R.id.checkbox)
    CheckBox mCheckbox;
    @InjectView(R.id.checkbox_layout)
    LinearLayout mCheckboxLayout;

    MyViewHolder(View view) {
        super(view);
        ButterKnife.inject(this, view);
    }
}