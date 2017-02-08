package com.shutup.todo.controller.sync;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.shutup.todo.BuildConfig;
import com.shutup.todo.common.RetrofitSingleton;
import com.shutup.todo.common.TodoListApi;
import com.shutup.todo.model.persist.Todo;
import com.shutup.todo.model.request.AddTodoRequest;
import com.shutup.todo.model.request.UpdateTodoRequest;
import com.shutup.todo.model.response.AddTodoResponse;
import com.shutup.todo.model.response.LoginUserResponse;
import com.shutup.todo.model.response.UpdateTodoResponse;

import java.io.IOException;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MyIntentService extends IntentService {

    private TodoListApi mTodoListApi;
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_CREATE = "com.shutup.todo.controller.sync.action.CREATE";
    public static final String ACTION_SYNC = "com.shutup.todo.controller.sync.action.SYNC";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "com.shutup.todo.controller.sync.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.shutup.todo.controller.sync.extra.PARAM2";

    public MyIntentService() {
        super("MyIntentService");
        initApiInstance();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_CREATE.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionCreate(param1, param2);
            } else if (ACTION_SYNC.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionSync(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionCreate(final String param1, String param2) {
        if (BuildConfig.DEBUG) Log.d("MyIntentService Create", param1);
        LoginUserResponse loginUserResponse = Realm.getDefaultInstance().where(LoginUserResponse.class).findFirst();
        RealmResults<Todo> todos = Realm.getDefaultInstance().where(Todo.class).equalTo("id",Long.valueOf(param1)).findAll();
        final Todo todo = todos.first();
        Call<ResponseBody> call = mTodoListApi.createTodo(loginUserResponse.getToken(),new AddTodoRequest(todo));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    try {
                        final AddTodoResponse addTodoResponse = gson.fromJson(response.body().string().toString(), AddTodoResponse.class);

                        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                RealmResults<Todo> todos = realm.where(Todo.class).equalTo("id",Long.valueOf(param1)).findAll();
                                Todo todo = todos.first();
                                todo.setSid(addTodoResponse.getId());
                                todo.setCreated(true);
                                todo.setSynced(true);
                                realm.insertOrUpdate(todo);
                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSync(final String param1, String param2) {
        if (BuildConfig.DEBUG) Log.d("MyIntentService Sync", param1);
        LoginUserResponse loginUserResponse = Realm.getDefaultInstance().where(LoginUserResponse.class).findFirst();
        RealmResults<Todo> todos = Realm.getDefaultInstance().where(Todo.class).equalTo("id",Long.valueOf(param1)).findAll();
        Todo todo = todos.first();
        Call<ResponseBody> call = mTodoListApi.updateTodo(loginUserResponse.getToken(),todo.getSid(),new UpdateTodoRequest(todo));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    try {
                        UpdateTodoResponse updateTodoResponse = gson.fromJson(response.body().string().toString(), UpdateTodoResponse.class);
                        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                RealmResults<Todo> todos = realm.where(Todo.class).equalTo("id",Long.valueOf(param1)).findAll();
                                Todo todo = todos.first();
                                todo.setSynced(true);
                                realm.insertOrUpdate(todo);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void initApiInstance() {
        mTodoListApi = RetrofitSingleton.getApiInstance(TodoListApi.class);
    }
}
