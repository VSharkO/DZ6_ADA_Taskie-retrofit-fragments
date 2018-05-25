package ada.osc.taskie.view.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ada.osc.taskie.R;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.util.AppStatus;
import ada.osc.taskie.view.MyViewModel;
import ada.osc.taskie.view.TaskAdapter;
import ada.osc.taskie.view.TaskClickListener;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteTasksFragment extends Fragment {

    @BindView(R.id.tasks)
    RecyclerView tasks;

    private TaskAdapter taskAdapter;
    public MyViewModel model;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        tasks.setLayoutManager(new LinearLayoutManager(getActivity()));
        tasks.setItemAnimator(new DefaultItemAnimator());

        model = ViewModelProviders.of(getActivity()).get(MyViewModel.class);
        model.getFavoriteTasks().observe(this, tasks -> {
            // update UI
            updateTasksDisplay(tasks);
        });
        taskAdapter = new TaskAdapter(new TaskClickListener() {
            @Override
            public void onClick(Task task) {

            }

            @Override
            public void onLongClick(Task task) {
                if (AppStatus.getInstance(getActivity().getApplicationContext()).isOnline()) {
                    model.deleteTask(task);
                    updateTasksDisplay(model.getFavoriteTaskLocal());
                } else {

                    Toast.makeText(getActivity().getApplicationContext(),
                            "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFavoriteClick(Task task) {

            }
        },true);

        tasks.setAdapter(taskAdapter);

    }

    private void updateTasksDisplay(List<Task> taskList) {
        taskAdapter.updateTasks(taskList);
        for (Task t : taskList) {
            Log.d("taskovi", t.getTitle());
        }
    }

}
