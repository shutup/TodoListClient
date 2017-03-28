package com.shutup.todo.controller.main;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shutup.todo.BuildConfig;
import com.shutup.todo.R;
import com.shutup.todo.common.Constants;
import com.shutup.todo.common.RecyclerTouchListener;
import com.shutup.todo.controller.base.BaseActivity;
import com.shutup.todo.controller.sync.MyIntentService;
import com.shutup.todo.model.normal_use.MenuItem;
import com.shutup.todo.model.persist.Todo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MainActivity extends BaseActivity implements Constants {

    @InjectView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeRefresh;
    @InjectView(R.id.drawer_menu_list)
    ListView mDrawerMenuList;
    @InjectView(R.id.drawer_view)
    LinearLayout mDrawerView;
    @InjectView(R.id.drawer)
    DrawerLayout mDrawer;
    @InjectView(R.id.addTodoFAB)
    FloatingActionButton mAddTodoFAB;
    @InjectView(R.id.main_content)
    CoordinatorLayout mMainContent;
    @InjectView(R.id.select_all_btn)
    Button mSelectAllBtn;
    @InjectView(R.id.delete_selected_btn)
    Button mDeleteSelectedBtn;
    @InjectView(R.id.bottom_bar)
    LinearLayout mBottomBar;
    private String TAG = this.getClass().getSimpleName();
    private List<MenuItem> mMenuItems;
    private DrawerMenuAdapter mDrawerMenuAdapter;
    private TodoListAdapter mTodoListAdapter;
    private RealmList<Todo> mTodos;
    private Realm mRealm;
    private static int currentType = ACTIVITY_NORMAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        initToolBar();

        initSwipeRefreshEvent();

        initDrawerLayout();
        initDrawerMenu();

        initRealm();
        initRecyclerView();
        syncServerData();
        loadLocalData();
        syncLocalData();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if (BuildConfig.DEBUG) Log.d("MainActivity", "onStart");
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
        if (BuildConfig.DEBUG) Log.d("MainActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        syncLocalData();
        super.onDestroy();
    }

    private void syncServerData() {
        Intent intent = new Intent(MainActivity.this, MyIntentService.class);
        intent.setAction(MyIntentService.ACTION_FETCH_ALL);
        startService(intent);
    }

    private void syncLocalData() {
        Intent intent = new Intent(MainActivity.this, MyIntentService.class);
        intent.setAction(MyIntentService.ACTION_DELETE_ALL);
        startService(intent);
        intent.setAction(MyIntentService.ACTION_CREATE_ALL);
        startService(intent);
        intent.setAction(MyIntentService.ACTION_SYNC_ALL);
        startService(intent);
    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        mToolbarTitle.setText(R.string.app_name);
    }

    private void initSwipeRefreshEvent() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadLocalData();
            }
        });
    }

    private void initDrawerLayout() {
        //set drawer width
        Display display = getWindowManager().getDefaultDisplay();
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) mDrawerView.getLayoutParams();
        params.width = (int) (0.45 * display.getWidth());
        mDrawerView.setLayoutParams(params);
        //
        // init drawer toggle
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();

        mDrawer.addDrawerListener(mDrawerToggle);
    }

    private void initDrawerMenu() {
        mMenuItems = new ArrayList<>();
        mMenuItems.add(new MenuItem("账户",new Intent(this,UserActivity.class)));
        mMenuItems.add(new MenuItem("设置",new Intent(this,SettingActivity.class)));
        mDrawerMenuAdapter = new DrawerMenuAdapter(this, mMenuItems);
        mDrawerMenuList.setAdapter(mDrawerMenuAdapter);
        mDrawerMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(mMenuItems.get(i).getIntent());
            }
        });
    }

    private void initRecyclerView() {
        mTodos = new RealmList<>();
        mTodoListAdapter = new TodoListAdapter(this, mTodos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mTodoListAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (currentType == ACTIVITY_NORMAL) {
                    EventBus.getDefault().postSticky(mTodos.get(position));
                    Intent intent = new Intent(MainActivity.this, AddTodoActivity.class);
                    intent.putExtra(ACTIVITY_STATUS,ACTIVITY_EDIT);
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void initRealm() {
        mRealm = Realm.getDefaultInstance();
    }

    private void loadLocalData() {
        RealmResults<Todo> todos = mRealm.where(Todo.class).equalTo("isDelete",false).findAllSortedAsync("createdAt");
        todos.addChangeListener(new RealmChangeListener<RealmResults<Todo>>() {
            @Override
            public void onChange(RealmResults<Todo> elements) {
                if (BuildConfig.DEBUG) Log.d("MainActivity", "clear");
                mTodos.clear();
                mTodos.addAll(elements);
                mTodoListAdapter.setTodos(mTodos);
                mTodoListAdapter.notifyDataSetChanged();
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    @OnClick(R.id.addTodoFAB)
    public void onClick() {
        Intent intent = new Intent(this, AddTodoActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        } else if (item.getItemId() == R.id.menu_edit) {
            if (item.getTitle().toString().contentEquals(getString(R.string.menu_edit))) {
                currentType = ACTIVITY_EDIT;
                item.setTitle(R.string.menu_cancel);
                mBottomBar.setVisibility(View.VISIBLE);
                mTodoListAdapter.setType(ACTIVITY_EDIT);
                mTodoListAdapter.notifyDataSetChanged();
            } else if (item.getTitle().toString().contentEquals(getString(R.string.menu_cancel))) {
                mTodos = mTodoListAdapter.getTodos();
                for (Todo n : mTodos
                        ) {
                    n.setChecked(false);
                }
                mSelectAllBtn.setText(getString(R.string.btn_select_all_title));
                currentType = ACTIVITY_NORMAL;
                item.setTitle(R.string.menu_edit);
                mBottomBar.setVisibility(View.GONE);
                mTodoListAdapter.setType(ACTIVITY_NORMAL);
                mTodoListAdapter.notifyDataSetChanged();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.select_all_btn, R.id.delete_selected_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.select_all_btn:
                selectAll((Button) view);
                break;
            case R.id.delete_selected_btn:
                deleteSelected();
                break;
        }
    }

    private void deleteSelected() {
        ArrayList<Todo> selectedList = new ArrayList<>();
        RealmQuery<Todo> query = mRealm.where(Todo.class);

        for (Todo n : mTodoListAdapter.getTodos()) {
            if (n.isChecked()) {
                selectedList.add(n);
                if (BuildConfig.DEBUG) Log.d("MainActivity", "checked");
            }
        }
        if (selectedList.size()==0) {
            return;
        }
        for (int i = 0; i < selectedList.size(); i++) {
            Todo todo = selectedList.get(i);
            query = mRealm.where(Todo.class);
            query.equalTo("id",selectedList.get(i).getId());
            if (hasNet()) {
                //有网
                if (todo.isCreated()) {
                    //已同步
                    Intent intent = new Intent(MainActivity.this,MyIntentService.class);
                    intent.setAction(MyIntentService.ACTION_DELETE);
                    intent.putExtra(MyIntentService.EXTRA_PARAM1,selectedList.get(i).getSid());
                    startService(intent);
                }else {
                    //未同步
                    final RealmResults<Todo> todos = query.findAll();
                    if (BuildConfig.DEBUG) Log.d("MainActivity", "del un sync todo size:" + todos.size());

                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            todos.deleteAllFromRealm();
                            mTodoListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }else {
                //无网
                if (todo.isCreated()) {
                    //已同步
//                    mark as delete
                    final RealmResults<Todo> todos = query.findAll();
                    if (BuildConfig.DEBUG) Log.d("MainActivity", "todos.size():" + todos.size());
                    for (final Todo t:todos
                         ) {
                        mRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                if (BuildConfig.DEBUG) Log.d("MainActivity", "delete:" + t.getId());
                                t.setDelete(true);
                                realm.insertOrUpdate(t);
                            }
                        });
                    }
                }else {
                    final RealmResults<Todo> todos = query.findAll();
                    if (BuildConfig.DEBUG) Log.d("MainActivity", "todos.size():" + todos.size());

                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            if (BuildConfig.DEBUG) Log.d("MainActivity", "todos.size():" + todos.size());
                            todos.deleteAllFromRealm();
                            mTodoListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }
    }

    private boolean hasNet() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    private void selectAll(Button btn) {
        if (btn.getText().toString().contentEquals(getString(R.string.btn_select_all_title))) {
            btn.setText(R.string.btn_reverse_select_title);
            for (Todo t : mTodoListAdapter.getTodos()) {
                t.setChecked(true);
            }
        } else if (btn.getText().toString().contentEquals(getString(R.string.btn_reverse_select_title))) {
            for (Todo t : mTodoListAdapter.getTodos()) {
                t.setChecked(false);
            }
            btn.setText(R.string.btn_select_all_title);
        }
        mTodoListAdapter.notifyDataSetChanged();
    }

    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void onActionRecv(Message msg) {
        if (msg.what==1) {
            mTodoListAdapter.notifyDataSetChanged();
        }
    }
}
