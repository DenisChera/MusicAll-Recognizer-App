package com.denis.music_rec.ui.notfound;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denis.music_rec.R;
import com.denis.music_rec.data.db.DBManager;
import com.denis.music_rec.data.model.Music;
import com.denis.music_rec.databinding.FragmentMainBinding;
import com.denis.music_rec.databinding.FragmentNotFoundBinding;
import com.denis.music_rec.ui.BaseFragment;

import java.util.ArrayList;

public class NotFoundFragment extends BaseFragment {
    private TextToSpeech tts;
    private FragmentNotFoundBinding binding = null;
    public static NotFoundFragment newInstance() {
        return new NotFoundFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentNotFoundBinding.inflate(inflater, container, false);
        binding.tryAgain.setOnClickListener(view -> {
            getActivity().finish();
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayList<Integer> arrayList = DBManager.getManager().getColor();
        if(arrayList.isEmpty()==false){
            binding.notFound.setBackgroundColor(arrayList.get(arrayList.size()-1));
        }
    }

}