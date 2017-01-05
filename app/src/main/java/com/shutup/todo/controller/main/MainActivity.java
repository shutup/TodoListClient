package com.shutup.todo.controller.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shutup.todo.R;
import com.shutup.todo.common.Constants;
import com.shutup.todo.controller.base.BaseActivity;
import com.shutup.todo.model.normal_use.MenuItem;
import com.shutup.todo.model.persist.Todo;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
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
    private List<Todo> mTodos;
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

        initRecyclerView();
        initRealm();
        loadLocalData();
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
        mMenuItems.add(new MenuItem("全部"));
        mMenuItems.add(new MenuItem("已完成"));
        mMenuItems.add(new MenuItem("未完成"));
        mDrawerMenuAdapter = new DrawerMenuAdapter(this, mMenuItems);
        mDrawerMenuList.setAdapter(mDrawerMenuAdapter);
        mDrawerMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private void initRecyclerView() {
        mTodos = new ArrayList<>();
        mTodoListAdapter = new TodoListAdapter(this, mTodos);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mTodoListAdapter);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void initRealm() {
        mRealm = Realm.getDefaultInstance();
    }

    private void loadLocalData() {
        RealmResults<Todo> todos = mRealm.where(Todo.class).findAllSortedAsync("createdAt");
        todos.addChangeListener(new RealmChangeListener<RealmResults<Todo>>() {
            @Override
            public void onChange(RealmResults<Todo> elements) {
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
        for (Todo n : mTodoListAdapter.getTodos()) {
            if (n.isChecked()) {
                selectedList.add(n);
            }
        }
        mTodoListAdapter.getTodos().removeAll(selectedList);
        mTodoListAdapter.notifyDataSetChanged();
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
}
