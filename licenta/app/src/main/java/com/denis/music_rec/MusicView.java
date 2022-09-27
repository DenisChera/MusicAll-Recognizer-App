package com.denis.music_rec;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.denis.music_rec.data.model.Music;
import com.denis.music_rec.databinding.ViewMusicBinding;
public class MusicView extends LinearLayout {

    private String title;
    private String artist;
    private String genre;
    private String releaseDate;
    private ViewMusicBinding binding;
    private static String TAG = MusicView.class.getSimpleName();


    public MusicView(Context context, Music music) {
        super(context);
        this.title = music.getSongTitle();
        this.artist = music.getSongArtist();
        this.genre = music.getSongGenre();
        this.releaseDate = music.getSongReleaseDate();
        loadView();
    }

    public void loadView() {
        binding = ViewMusicBinding.inflate(LayoutInflater.from(getContext()), this, true);
        binding.title.setText(title);
        binding.artist.setText("Artist: " + artist);

        if((genre != null) && !genre.isEmpty()) {
            binding.genre.setVisibility(VISIBLE);
            binding.genre.setText(genre);
        } else {
            binding.genre.setVisibility(GONE);
        }

        if((releaseDate != null) && !releaseDate.isEmpty()) {
            binding.releaseDate.setVisibility(VISIBLE);
            binding.releaseDate.setText(releaseDate);
        } else {
            binding.releaseDate.setVisibility(GONE);
        }
    }
}
