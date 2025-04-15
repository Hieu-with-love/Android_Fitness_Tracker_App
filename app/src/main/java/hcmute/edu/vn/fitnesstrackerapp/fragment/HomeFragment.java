package hcmute.edu.vn.fitnesstrackerapp.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.fitnesstrackerapp.R;
import hcmute.edu.vn.fitnesstrackerapp.adapter.TeamMemberAdapter;
import hcmute.edu.vn.fitnesstrackerapp.model.TeamMember;

/**
 * Home fragment showing app introduction, instructor and team members
 */
public class HomeFragment extends Fragment {

    private RecyclerView teamMembersRecyclerView;
    private TeamMemberAdapter teamMemberAdapter;
    private List<TeamMember> teamMembers;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        
        // Initialize RecyclerView
        teamMembersRecyclerView = view.findViewById(R.id.rv_team_members);
        teamMembersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Prepare team members data
        prepareTeamMembersData();
        
        // Set up adapter
        teamMemberAdapter = new TeamMemberAdapter(teamMembers);
        teamMembersRecyclerView.setAdapter(teamMemberAdapter);
        
        return view;
    }
    
    private void prepareTeamMembersData() {
        teamMembers = new ArrayList<>();
        
        // Add team members (replace with actual team members)
        teamMembers.add(new TeamMember("Student Name 1", "MSSV: 20110123", "Team Leader"));
        teamMembers.add(new TeamMember("Student Name 2", "MSSV: 20110124", "UI/UX Designer"));
        teamMembers.add(new TeamMember("Student Name 3", "MSSV: 20110125", "Backend Developer"));
        teamMembers.add(new TeamMember("Student Name 4", "MSSV: 20110126", "Database Administrator"));
        teamMembers.add(new TeamMember("Student Name 5", "MSSV: 20110127", "QA Tester"));
    }
}