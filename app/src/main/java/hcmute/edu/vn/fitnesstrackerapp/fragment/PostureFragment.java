package hcmute.edu.vn.fitnesstrackerapp.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import hcmute.edu.vn.fitnesstrackerapp.R;
import hcmute.edu.vn.fitnesstrackerapp.activity.RecognizePostureActivity;

public class PostureFragment extends Fragment {

    private Button startRecognize;
    public PostureFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_posture, container, false);
        startRecognize = view.findViewById(R.id.startRecognize);
        startRecognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecognizePostureActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}