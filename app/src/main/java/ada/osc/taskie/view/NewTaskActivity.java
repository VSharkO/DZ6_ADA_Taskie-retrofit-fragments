package ada.osc.taskie.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import ada.osc.taskie.R;
import ada.osc.taskie.model.Task;
import ada.osc.taskie.model.TaskPriority;
import ada.osc.taskie.util.AppStatus;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewTaskActivity extends AppCompatActivity {

	@BindView(R.id.edittext_newtask_title)
	EditText mTitleEntry;
	@BindView(R.id.edittext_newtask_description)
	EditText mDescriptionEntry;
	@BindView(R.id.spinner_newtask_priority)
	Spinner mPriorityEntry;
	MyViewModel model;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_task);
		ButterKnife.bind(this);
		setUpSpinnerSource();
		model = ViewModelProviders.of(this).get(MyViewModel.class);
	}

	private void setUpSpinnerSource() {
		mPriorityEntry.setAdapter(
				new ArrayAdapter<TaskPriority>(
						this, android.R.layout.simple_list_item_1, TaskPriority.values()
				)
		);
		mPriorityEntry.setSelection(0);
	}

	@OnClick(R.id.imagebutton_newtask_savetask)
	public void saveTask() {
		String title = mTitleEntry.getText().toString();
		String description = mDescriptionEntry.getText().toString();
		TaskPriority priority = (TaskPriority) mPriorityEntry.getSelectedItem();

		Task newTask = new Task(title, description, priority);
		if (AppStatus.getInstance(getApplicationContext()).isOnline()) {
			model.createNewTaskOnServer(newTask);
		} else {
			Toast.makeText(getApplicationContext(),
					"Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();
		}


		Intent intent = new Intent();
		intent.putExtra("newTask", newTask);
		setResult(RESULT_OK, intent);
		finish();
	}
}