package com.shutup.todo.controller.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.shutup.todo.BuildConfig;
import com.shutup.todo.R;
import com.shutup.todo.common.Constants;
import com.shutup.todo.common.RetrofitSingleton;
import com.shutup.todo.common.TodoListApi;
import com.shutup.todo.controller.base.BaseActivity;
import com.shutup.todo.controller.sync.MyIntentService;
import com.shutup.todo.model.persist.Todo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;

public class AddTodoActivity extends BaseActivity implements Constants{

    private TodoListApi mTodoListApi;
    @InjectView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.todoContent)
    EditText mTodoContent;
    @InjectView(R.id.addTodoFAB)
    FloatingActionButton mAddTodoFAB;
    private int currentType = ACTIVITY_NORMAL;
    private Todo mTodo;
    private boolean isChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) Log.d("AddTodoActivity", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);
        ButterKnife.inject(this);
        initType();
        initToolBar();
        initApiInstance();
        initEditor();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(AddTodoActivity.this, MyIntentService.class);
        if (mTodo == null||!isChanged) {
            return;
        }
        if (currentType == ACTIVITY_NORMAL) {
            intent.setAction(MyIntentService.ACTION_CREATE);
            intent.putExtra(MyIntentService.EXTRA_PARAM1,mTodo.getId()+"");
        }else if (currentType == ACTIVITY_EDIT) {
            intent.setAction(MyIntentService.ACTION_SYNC);
            intent.putExtra(MyIntentService.EXTRA_PARAM1,mTodo.getId()+"");
        }
        startService(intent);
    }

    private void initType() {
        Intent intent = getIntent();
        currentType = intent.getIntExtra(ACTIVITY_STATUS,ACTIVITY_NORMAL);
    }

    @Subscribe(sticky = true)
    public void onTodoRece(Todo todo) {
        EventBus.getDefault().removeStickyEvent(todo);
        mTodo = todo;
        mTodoContent.setText(todo.getTodo());
        mTodoContent.setSelection(todo.getTodo().length());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (currentType == ACTIVITY_NORMAL) {
            inflater.inflate(R.menu.add_update_todo_activity_menu_normal, menu);
        }else if (currentType == ACTIVITY_EDIT){
            inflater.inflate(R.menu.add_update_todo_activity_menu_edit, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            isChanged = false;
            finish(); // close this activity and return to preview activity (if there is any)
        }else if (item.getItemId() == R.id.menu_ok) {
            saveToLocal();
        }else if (item.getItemId() == R.id.menu_save) {
            saveToLocal();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initEditor() {
        mTodoContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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
        if (currentType == ACTIVITY_NORMAL) {
            mToolbarTitle.setText(R.string.insert_todo_str);
        }else if (currentType == ACTIVITY_EDIT) {
            mToolbarTitle.setText(R.string.update_todo_str);
        }
    }

    @OnClick(R.id.addTodoFAB)
    public void onClick() {
        saveToLocal();
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
                if (currentType == ACTIVITY_NORMAL) {
                    Todo todo = new Todo(content);
                    mTodo = todo;
                    todo.setSynced(false);
                    todo.setCreated(false);
                    Number currentIdNum = realm.where(Todo.class).max("id");
                    long nextId;
                    if(currentIdNum == null) {
                        nextId = 1;
                    } else {
                        nextId = currentIdNum.intValue() + 1;
                    }
                    todo.setId(nextId);
                    realm.insertOrUpdate(todo);
                }else if (currentType == ACTIVITY_EDIT){
                    if (isChanged) {
                        mTodo.setTodo(content);
                        mTodo.setSynced(false);
                        realm.insertOrUpdate(mTodo);
                    }
                }
            }
        });
    }

    private boolean checkContentNotEmpty() {
        return !mTodoContent.getEditableText().toString().trim().equals("");
    }
}
