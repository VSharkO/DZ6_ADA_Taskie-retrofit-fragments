package ada.osc.taskie.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.R;
import ada.osc.taskie.networking.ApiService;
import ada.osc.taskie.networking.RetrofitUtil;
import ada.osc.taskie.persistance.TaskRepository;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.util.SharedPrefsUtil;
import ada.osc.taskie.view.fragments.AllTasksFragment;
import ada.osc.taskie.view.fragments.FavoriteTasksFragment;
import ada.osc.taskie.view.fragments.TasksPagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TasksActivity extends AppCompatActivity {

    private static final String TAG = TasksActivity.class.getSimpleName();
    @BindView(R.id.fragmentContainer) ViewPager viewPager;
    private static final int REQUEST_NEW_TASK = 10;
    MyViewModel model;
    private TasksPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        ButterKnife.bind(this);

        model = ViewModelProviders.of(this).get(MyViewModel.class);
        adapter = new TasksPagerAdapter(getSupportFragmentManager());
        List<Fragment> pages = new ArrayList<>();
        pages.add(new AllTasksFragment());
        pages.add(new FavoriteTasksFragment());
        adapter.setItems(pages);
        viewPager.setAdapter(adapter);

    }

    @OnClick(R.id.fab_tasks_addNew)
    public void startNewTaskActivity() {
        Intent newTask = new Intent();
        newTask.setClass(this, NewTaskActivity.class);
        startActivityForResult(newTask, REQUEST_NEW_TASK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            Task newTask = (Task) data.getSerializableExtra("newTask");
            model.setNewTask(newTask);
        }
    }
}
