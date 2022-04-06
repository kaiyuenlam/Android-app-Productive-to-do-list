package com.example.group10.fragments.notesFragments;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Scroller;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import com.example.group10.R;
import org.jetbrains.annotations.NotNull;

public class NotesFragmentViewMode extends Fragment {

    TextView textView_title, textView_content;
    SharedViewModel viewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes_viewmode, container, false);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        textView_title = view.findViewById(R.id.textview_title);
        textView_content = view.findViewById(R.id.textview_note);

        textView_title.setText(viewModel.getTitle().getValue());
        textView_content.setText(viewModel.getContent().getValue());

        textView_content.setMovementMethod(new ScrollingMovementMethod());
    }
}