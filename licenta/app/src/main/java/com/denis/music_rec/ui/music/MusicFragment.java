package com.denis.music_rec.ui.music;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.denis.music_rec.databinding.FragmentMusicBinding;
import com.denis.music_rec.ui.BaseFragment;
import com.denis.music_rec.ui.main.MainFragment;
import com.denis.music_rec.ui.main.MainViewModel;

public class MusicFragment extends BaseFragment {

    private FragmentMusicBinding binding;

    private MainViewModel mainVM;

    public static MusicFragment newInstance() {
        return new MusicFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMusicBinding.inflate(inflater, container, false);

        getBaseActivity().setSupportActionBar(binding.toolbar);
        if (getBaseActivity().getSupportActionBar() != null) {
            getBaseActivity().getSupportActionBar().setDisplayShowTitleEnabled(false);
            getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mainVM = MainFragment.getViewModel();

        binding.title.setText(mainVM.getMusic().getValue().getSongTitle());
        binding.artist.setText(mainVM.getMusic().getValue().getSongArtist());
        binding.genre.setText(mainVM.getMusic().getValue().getSongGenre());
        binding.releaseDate.setText(mainVM.getMusic().getValue().getSongReleaseDate());
    }

}