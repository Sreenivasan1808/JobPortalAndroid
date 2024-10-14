package com.example.jobportal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private Context context;
    private List<Job> jobList;
    private DBHelper.ROLES role;
    private int userId;

    public JobAdapter(Context context, List<Job> jobList, DBHelper.ROLES role, int userId) {
        this.context = context;
        this.jobList = jobList;
        this.role = role;
        this.userId = userId;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_card, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.jobTitle.setText(job.getTitle());
        holder.jobSalary.setText(job.getSalary());
        holder.jobLocation.setText(job.getLocation());
        holder.jobOrganization.setText(job.getOrganization());

        holder.itemView.setOnClickListener(V -> {
            Intent i = new Intent(context, ViewJobDetail.class);
            i.putExtra("jobId", job.getId());
            i.putExtra("role", role.ordinal());
            i.putExtra("uid", userId);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {

        TextView jobTitle, jobSalary, jobLocation, jobOrganization;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            jobOrganization = itemView.findViewById(R.id.jobOrganization);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            jobSalary = itemView.findViewById(R.id.jobSalary);
            jobLocation = itemView.findViewById(R.id.jobLocation);
        }
    }
}
