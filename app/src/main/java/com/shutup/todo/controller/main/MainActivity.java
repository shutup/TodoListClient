package com.shutup.todo.controller.main;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shutup.todo.R;
import com.shutup.todo.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity {

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
    private String TAG = this.getClass().getSimpleName();
    private List<MenuItem> mMenuItems;
    private DrawerMenuAdapter mDrawerMenuAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        initToolBar();

        initSwipeRefreshEvent();

        initDrawerLayout();
        initDrawerMenu();
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
        mDrawerMenuAdapter = new DrawerMenuAdapter(this,mMenuItems);
        mDrawerMenuList.setAdapter(mDrawerMenuAdapter);
        mDrawerMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
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
//                if (!isRequest) {
//                    if (BuildConfig.DEBUG) Log.d(TAG, "beginRequest");
//                } else {
//                    if (BuildConfig.DEBUG) Log.d(TAG, "isRequesting");
//                }
            }
        });
    }
}
