package com.example.studentmanager.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studentmanager.R;
import com.example.studentmanager.models.ScoreRecord;
import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {

    private List<ScoreRecord> scoreList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ScoreRecord record);
    }

    public ScoreAdapter(List<ScoreRecord> scoreList, OnItemClickListener listener) {
        this.scoreList = scoreList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ScoreRecord record = scoreList.get(position);
        holder.tvName.setText(record.getCourseName());
        holder.tvCredit.setText("学分: " + record.getCredit());
        holder.tvScore.setText("成绩: " + record.getScore());
        holder.itemView.setOnClickListener(v -> listener.onItemClick(record));
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvCredit, tvScore;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_item_score_name);
            tvCredit = itemView.findViewById(R.id.tv_item_score_credit);
            tvScore = itemView.findViewById(R.id.tv_item_score_value);
        }
    }
}
