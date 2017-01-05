package com.shutup.todo.controller.main;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shutup.todo.BuildConfig;
import com.shutup.todo.R;
import com.shutup.todo.common.RetrofitSingleton;
import com.shutup.todo.common.TodoListApi;
import com.shutup.todo.controller.base.BaseActivity;
import com.shutup.todo.model.persist.Todo;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;

public class AddTodoActivity extends BaseActivity {

    private TodoListApi mTodoListApi;
    @InjectView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.todoContent)
    EditText mTodoContent;
    @InjectView(R.id.addTodoFAB)
    FloatingActionButton mAddTodoFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        ButterKnife.inject(this);

        initToolBar();
        initApiInstance();
        initEditor();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_todo_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }else if (item.getItemId() == R.id.menu_ok) {
            saveToLocal();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initEditor() {
    }

    private void initApiInstance() {
        mTodoListApi = RetrofitSingleton.getApiInstance(TodoListApi.class);
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        mToolbarTitle.setText("添加");
    }

    @OnClick(R.id.addTodoFAB)
    public void onClick() {
        if (checkContentNotEmpty()) {
            String content = mTodoContent.getEditableText().toString().trim();
            saveToLocal(content);
            return;
//            Call<ResponseBody> call = mTodoListApi.createTodo("",new AddTodoRequest(content));
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    Gson gson = new Gson();
//                    if (response.isSuccessful()) {
//                        AddTodoResponse addTodoResponse = null;
//                        try {
//                            addTodoResponse = gson.fromJson(response.body().string(),AddTodoResponse.class);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        Realm realm = Realm.getDefaultInstance();
//                        realm.beginTransaction();
//                        realm.copyToRealm(addTodoResponse);
//                        realm.commitTransaction();
//                        onBackPressed();
//                    }else {
//                        RestInfo info = null;
//                        try {
//                            info = gson.fromJson(response.errorBody().string(),RestInfo.class);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        if (info!= null) {
//                            Toast.makeText(AddTodoActivity.this, info.getMsg(), Toast.LENGTH_SHORT).show();
//                        }else {
//                            Toast.makeText(AddTodoActivity.this, "请求异常", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                }
//            });
        } else {
            Toast.makeText(this, "请先输入内容！", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveToLocal() {
        if (checkContentNotEmpty()) {
            String content = mTodoContent.getEditableText().toString().trim();
            saveToLocal(content);
            finish();
        } else {
            Toast.makeText(this, "请先输入内容！", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveToLocal(final String content) {
        
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Todo todo = new Todo(content);
                Number currentIdNum = realm.where(Todo.class).max("id");
                long nextId;
                if(currentIdNum == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }
                todo.setId(nextId);
                realm.insertOrUpdate(todo);
                if (BuildConfig.DEBUG) Log.d("AddTodoActivity", "currentIdNum:" + currentIdNum);
            }
        });
    }

    private boolean checkContentNotEmpty() {
        return !mTodoContent.getEditableText().toString().trim().equals("");
    }
}
