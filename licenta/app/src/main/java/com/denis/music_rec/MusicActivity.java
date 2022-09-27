package com.denis.music_rec;

import androidx.annotation.NonNull;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.denis.music_rec.ui.BaseActivity;
import com.denis.music_rec.ui.music.MusicFragment;

public class MusicActivity extends BaseActivity {

    @NonNull
    public static Intent createIntent(@NonNull Context context) {
        Intent i = new Intent().setClass(context, MusicActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle b = new Bundle();
        i.putExtras(b);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MusicFragment.newInstance())
                    .commitNow();
        }
    }
}