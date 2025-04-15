package hcmute.edu.vn.fitnesstrackerapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.fitnesstrackerapp.R;
import hcmute.edu.vn.fitnesstrackerapp.model.TeamMember;

public class TeamMemberAdapter extends RecyclerView.Adapter<TeamMemberAdapter.TeamMemberViewHolder> {
    
    private List<TeamMember> teamMembers;
    
    public TeamMemberAdapter(List<TeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }
    
    @NonNull
    @Override
    public TeamMemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_team_member, parent, false);
        return new TeamMemberViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull TeamMemberViewHolder holder, int position) {
        TeamMember member = teamMembers.get(position);
        holder.nameTextView.setText(member.getName());
        holder.studentIdTextView.setText(member.getStudentId());
        holder.roleTextView.setText(member.getRole());
    }
    
    @Override
    public int getItemCount() {
        return teamMembers.size();
    }
    
    public static class TeamMemberViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        TextView nameTextView;
        TextView studentIdTextView;
        TextView roleTextView;
        
        public TeamMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.iv_member_avatar);
            nameTextView = itemView.findViewById(R.id.tv_member_name);
            studentIdTextView = itemView.findViewById(R.id.tv_member_student_id);
            roleTextView = itemView.findViewById(R.id.tv_member_role);
        }
    }
}