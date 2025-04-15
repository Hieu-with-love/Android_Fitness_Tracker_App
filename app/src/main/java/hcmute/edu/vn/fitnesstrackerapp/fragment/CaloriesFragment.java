package hcmute.edu.vn.fitnesstrackerapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import hcmute.edu.vn.fitnesstrackerapp.R;


public class CaloriesFragment extends Fragment {
    private EditText edtHeight, edtWeight, edtAge;
    private RadioGroup rgGender;
    private RadioGroup rgActivityCapital;
    private Button btnCalculateCalories;
    private TextView tvBMR, tvTDEE, tvBreakfast, tvLunch, tvDinner, tvSnacks;

    public CaloriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calories, container, false);
        edtHeight = view.findViewById(R.id.etHeight);
        edtWeight = view.findViewById(R.id.etWeight);
        edtAge = view.findViewById(R.id.etAge);
        rgGender = view.findViewById(R.id.rgGender);
        rgActivityCapital = view.findViewById(R.id.rgActivity);
        btnCalculateCalories = view.findViewById(R.id.btnCalculate);
        tvBMR = view.findViewById(R.id.tvBMR);
        tvTDEE = view.findViewById(R.id.tvTDEE);
        tvBreakfast = view.findViewById(R.id.tvBreakfast);
        tvLunch = view.findViewById(R.id.tvLunch);
        tvDinner = view.findViewById(R.id.tvDinner);
        tvSnacks = view.findViewById(R.id.tvSnacks);

        btnCalculateCalories.setOnClickListener(v -> calcCalories());

        return view;
    }

    private void calcCalories() {
        // Check input
        if (edtHeight.getText().toString().isEmpty()||
            edtWeight.getText().toString().isEmpty()||
            edtAge.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get data from user input
        double height = Double.parseDouble(edtHeight.getText().toString());
        double weight = Double.parseDouble(edtWeight.getText().toString());
        int age = Integer.parseInt(edtAge.getText().toString());

        // Check gender
        int selectedGender = rgGender.getCheckedRadioButtonId();
        if (selectedGender == -1){
            Toast.makeText(getContext(), "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
            return;
        }
        // check activity level
        int selectedActivityLevel = rgActivityCapital.getCheckedRadioButtonId();
        if (selectedActivityLevel == -1){
            Toast.makeText(getContext(), "Vui lòng chọn mức độ hoạt động", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate BMR by Mifflin St Jeor
        double bmr;
        boolean isMale = selectedGender == R.id.rbMale;
        
        if (isMale) {
            // BMR for men: 10 × weight(kg) + 6.25 × height(cm) - 5 × age(y) + 5;
            bmr = 10 * weight + 6.25 * height - 5 * age + 5;
        } else {
            // BMR for women: 10 × weight(kg) + 6.25 × height(cm) - 5 × age(y) - 161;
            bmr = 10 * weight + 6.25 * height - 5 * age - 161;
        }

        // Calculate TDEE based on activity level
        double tdee;
        double activityMultiplier;
        
        if (selectedActivityLevel == R.id.rbSedentary) {
            activityMultiplier = 1.2; // Sedentary
        } else if (selectedActivityLevel == R.id.rbLightlyActive) {
            activityMultiplier = 1.375; // Lightly active
        } else if (selectedActivityLevel == R.id.rbModeratelyActive) {
            activityMultiplier = 1.55; // Moderately active
        } else if (selectedActivityLevel == R.id.rbVeryActive) {
            activityMultiplier = 1.725; // Very active
        } else {
            activityMultiplier = 1.9; // Extra active
        }
        
        tdee = bmr * activityMultiplier;
        
        // Calculate meal calorie distribution
        double breakfast = tdee * 0.25; // 25% of TDEE
        double lunch = tdee * 0.35; // 35% of TDEE
        double dinner = tdee * 0.30; // 30% of TDEE
        double snacks = tdee * 0.10; // 10% of TDEE
        
        // Round results to avoid showing too many decimal places
        bmr = Math.round(bmr);
        tdee = Math.round(tdee);
        breakfast = Math.round(breakfast);
        lunch = Math.round(lunch);
        dinner = Math.round(dinner);
        snacks = Math.round(snacks);
        
        // Display results
        tvBMR.setText("BMR: " + (int)bmr + " calo");
        tvTDEE.setText("TDEE: " + (int)tdee + " calo");
        tvBreakfast.setText("Bữa sáng: " + (int)breakfast + " calo (25%)");
        tvLunch.setText("Bữa trưa: " + (int)lunch + " calo (35%)");
        tvDinner.setText("Bữa tối: " + (int)dinner + " calo (30%)");
        tvSnacks.setText("Đồ ăn vặt: " + (int)snacks + " calo (10%)");
    }
}