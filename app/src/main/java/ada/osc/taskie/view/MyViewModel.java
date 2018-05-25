package ada.osc.taskie.view;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskList;
import ada.osc.taskie.networking.ApiService;
import ada.osc.taskie.networking.RetrofitUtil;
import ada.osc.taskie.util.SharedPrefsUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyViewModel extends AndroidViewModel {

        private MutableLiveData<List<Task>> tasks;
        private MutableLiveData<List<Task>> favoriteTasks;
        private List<Task> tasksLocal;
        private List<Task> favoriteTaskLocal;

    public MyViewModel(@NonNull Application application) {
        super(application);
        if(tasksLocal == null)
        getTasks();
        if(getFavoriteTasksLocal()==null)
        getFavoriteTasks();
    }

    public LiveData<List<Task>> getTasks() {
            if (tasks == null) {
                tasks = new MutableLiveData<>();
                loadTasks();
            }
            return tasks;
        }

    public LiveData<List<Task>> getFavoriteTasks() {
        if (favoriteTasks == null) {
            favoriteTasks = new MutableLiveData<>();
            loadFavoriteTasks();
        }
        return favoriteTasks;
    }

        private void loadTasks() {

            Retrofit retrofit = RetrofitUtil.createRetrofit();
            ApiService apiService = retrofit.create(ApiService.class);

            Call<TaskList> taskListCall = apiService
                    .getTasks(SharedPrefsUtil.getPreferencesField(getApplication()
                            , SharedPrefsUtil.TOKEN));

            taskListCall.enqueue(new Callback<TaskList>() {
                @Override
                public void onResponse(Call<TaskList> call, Response<TaskList> response) {
                    if (response.isSuccessful()) {
                        List<Task> tasksFromServer = new ArrayList<>(response.body().mTaskList);
                        tasks.setValue(tasksFromServer);
                        tasksLocal = tasksFromServer;
                    }
                }

                @Override
                public void onFailure(Call<TaskList> call, Throwable t) {

                }
            });
        }

        private void loadFavoriteTasks() {

            Retrofit retrofit = RetrofitUtil.createRetrofit();
            ApiService apiService = retrofit.create(ApiService.class);

            Call<TaskList> taskListCall = apiService
                    .getFavoriteTasks(SharedPrefsUtil.getPreferencesField(getApplication()
                            , SharedPrefsUtil.TOKEN));

            taskListCall.enqueue(new Callback<TaskList>() {
                @Override
                public void onResponse(Call<TaskList> call, Response<TaskList> response) {
                    if (response.isSuccessful()) {
                        List<Task> favoriteTasksFromServer = new ArrayList<>(response.body().mTaskList);
                        favoriteTasks.setValue(favoriteTasksFromServer);
                        favoriteTaskLocal = favoriteTasksFromServer;
                    }
                }

                @Override
                public void onFailure(Call<TaskList> call, Throwable t) {

                }
            });
        }

    public void setFavoriteOnServer(Task setFavoriteTask) {

        setFavoriteLocal(setFavoriteTask);

        Retrofit retrofit = RetrofitUtil.createRetrofit();
        ApiService apiService = retrofit.create(ApiService.class);
        Call postFavoriteTaskCall = apiService
                .postFavoriteTask(SharedPrefsUtil.getPreferencesField(getApplication()
                        , SharedPrefsUtil.TOKEN), setFavoriteTask.getmId());

        postFavoriteTaskCall.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    Log.i("state", "Success");

                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.i("state", "Failed");
            }
        });
    }

    private void setFavoriteLocal(Task setFavoriteTask) {
        favoriteTaskLocal.add(setFavoriteTask);
        tasksLocal.remove(setFavoriteTask);
        tasks.setValue(tasksLocal);
        favoriteTasks.setValue(favoriteTaskLocal);
    }

    public List<Task> getTasksLocal() {
        return tasksLocal;
    }

    public List<Task> getFavoriteTasksLocal() {
        return favoriteTaskLocal;
    }

    public void setNewTask(Task taskToSave) {
        tasksLocal.add(taskToSave);
        tasks.setValue(getTasksLocal());
    }
}
