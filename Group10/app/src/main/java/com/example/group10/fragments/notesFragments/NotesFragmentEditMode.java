package com.example.group10.fragments.notesFragments;

import android.os.Bundle;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import com.example.group10.R;
import org.jetbrains.annotations.NotNull;

public class NotesFragmentEditMode extends Fragment {
    SharedViewModel viewModel;
    EditText editText_title, editText_content;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_edit_mode, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        editText_title = view.findViewById(R.id.edit_title);
        editText_content = view.findViewById(R.id.edit_body);

        editText_title.setText(viewModel.getTitle().getValue());
        editText_content.setText(viewModel.getContent().getValue());

    }

    //public method for update change to view model
    public void updateTask() {
        viewModel.getTitle().setValue(editText_title.getText().toString());
        viewModel.getContent().setValue(editText_content.getText().toString());
    }
}