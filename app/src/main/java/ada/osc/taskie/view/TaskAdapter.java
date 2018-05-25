package ada.osc.taskie.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ada.osc.taskie.R;
import ada.osc.taskie.model.Task;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

	private List<Task> mTasks;
	private TaskClickListener mListener;
	private boolean isFavoriteFragment=false;
	private int resForTaskLayout;

	public TaskAdapter(TaskClickListener listener, boolean isFavoriteFragment) {
		mListener = listener;
		mTasks = new ArrayList<>();
		if(isFavoriteFragment) {
		    this.isFavoriteFragment=true;
			resForTaskLayout=R.layout.favorite_item_task;
        }else{
			resForTaskLayout=R.layout.item_task;
		}
	}

	public void updateTasks(List<Task> tasks){
		mTasks.clear();
		mTasks.addAll(tasks);
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(parent.getContext())
				.inflate(resForTaskLayout, parent, false);
		return new TaskViewHolder(itemView, mListener);
	}

	@Override
	public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {

		Task current = mTasks.get(position);
		holder.mTitle.setText(current.getTitle());
		holder.mDescription.setText(current.getDescription());

		int color = R.color.taskPriority_Unknown;
//		switch (current.getPriority()){
//			case LOW: color = R.color.taskpriority_low; break;
//			case MEDIUM: color = R.color.taskpriority_medium; break;
//			case HIGH: color = R.color.taskpriority_high; break;
//		}

		holder.mPriority.setImageResource(color);
	}

	@Override
	public int getItemCount() {
		return mTasks.size();
	}

	class TaskViewHolder extends RecyclerView.ViewHolder {

		@BindView(R.id.textview_task_title) TextView mTitle;
		@BindView(R.id.textview_task_description) TextView mDescription;
		@BindView(R.id.imageview_task_priority) ImageView mPriority;
		@BindView(R.id.checkbox_isFavorite) CheckBox mIsFavorite;

		public TaskViewHolder(View itemView, TaskClickListener listener) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}

		@OnClick
		public void onTaskClick(){
			mListener.onClick(mTasks.get(getAdapterPosition()));
		}

		@OnLongClick
		public boolean onTaskLongClick(){
			mListener.onLongClick(mTasks.get(getAdapterPosition()));
			return true;
		}


		@OnClick({R.id.checkbox_isFavorite})
		public void onFavoriteClick(){
		    mListener.onFavoriteClick(mTasks.get(getAdapterPosition()));
			if (mIsFavorite.isChecked()) {
				mIsFavorite.setChecked(false);
			}
			else {
				mIsFavorite.setChecked(true);
			}
		}
	}
}
