package com.example.first_responder_app.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.first_responder_app.FirestoreDatabase;
import com.example.first_responder_app.R;
import com.example.first_responder_app.dataModels.FireDepartmentDataModel;
import com.example.first_responder_app.databinding.CreateDepartmentFragmentBinding;
import com.example.first_responder_app.viewModels.CreateDepartmentViewModel;

public class CreateDepartmentFragment extends Fragment {

    private CreateDepartmentViewModel mViewModel;

    public static CreateDepartmentFragment newInstance() {
        return new CreateDepartmentFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CreateDepartmentFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.create_department_fragment, container, false);

        binding.createDepartmentCreateButton.setOnClickListener(v -> {
            String location = binding.createDepartmentLocationEditText.getText().toString();
            String name = binding.createDepartmentNameEditText.getText().toString();

            FireDepartmentDataModel fireDepartmentDataModel = new FireDepartmentDataModel(location, name);

            if(!location.equals("") && !name.equals("")) {
                FirestoreDatabase.getInstance().getDb().collection(FirestoreDatabase.FIRE_DEPARTMENT_COLLECTION_DIR).add(fireDepartmentDataModel)
                        .addOnSuccessListener(documentReference -> {
                            String fireDeptId = documentReference.getId();

                            new AlertDialog.Builder(getContext())
                                    .setTitle("Remember this id!")
                                    .setMessage("The only way for you or anyone else to join this department is with this id. You cannot get this id again until someone joins the department." +
                                            "\n\nID: " + fireDeptId)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setNegativeButton(android.R.string.ok, (dialog, which) -> {
                                        NavDirections action = CreateDepartmentFragmentDirections.actionCreateDepartmentFragmentToLoginFragment();
                                        Navigation.findNavController(binding.getRoot()).navigate(action);
                                    })
                                    .setPositiveButton("Create account for department", (dialog, which) -> {
                                        // TODO: go to make user
                                        Log.d("", "onCreateView: nothing");
//                                        NavDirections action = CreateDepartmentFragmentDirections.actionCreateDepartmentFragmentToLoginFragment();
//                                        Navigation.findNavController(binding.getRoot()).navigate(action);
                                    }).show();
                        })
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to make fire department", Toast.LENGTH_SHORT).show());
            }
            else {
                Toast.makeText(getContext(), "Invalid entries", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CreateDepartmentViewModel.class);
        // TODO: Use the ViewModel
    }

}