package com.denis.music_rec.ui.main;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.acrcloud.rec.ACRCloudResult;
import com.acrcloud.rec.IACRCloudListener;
import com.acrcloud.rec.IACRCloudRadioMetadataListener;
import com.denis.music_rec.Dialog;
import com.denis.music_rec.HistoryActivity;
import com.denis.music_rec.MusicView;
import com.denis.music_rec.NotFoundActivity;
import com.denis.music_rec.R;
import com.denis.music_rec.data.db.DBManager;
import com.denis.music_rec.data.model.Music;
import com.denis.music_rec.databinding.FragmentMainBinding;
import com.denis.music_rec.ui.BaseFragment;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Locale;


public class MainFragment extends BaseFragment implements IACRCloudListener {

    private FragmentMainBinding binding = null;
    private static MainViewModel vm;
    private final String TAG = MainFragment.class.getSimpleName();
    private static String[] PERM = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.RECORD_AUDIO
    };
    private TextToSpeech tts;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        getBaseActivity().setSupportActionBar(binding.toolbar);
        DBManager.setContext(MainFragment.this.getBaseActivity());
        binding.toolbar.setTitle(R.string.app_name);
        setHasOptionsMenu(true);
        verifyPermissions();
        binding.listen.setOnClickListener(view -> {
            binding.listen.playAnimation();
            binding.message.setText(R.string.msg_listening);
            vm.startProcessing();
        });

        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        ArrayList<Integer> arrayList = DBManager.getManager().getColor();
        if(arrayList.isEmpty()==false){
            binding.main.setBackgroundColor(arrayList.get(arrayList.size()-1));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        vm = new ViewModelProvider(this).get(MainViewModel.class);
        vm.init(this, this.getActivity());

        tts = new TextToSpeech(getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(Locale.US);


            }
        });

        vm.getMusic().observe(getViewLifecycleOwner(), song -> {
            if (song != null) {
                if (this.getActivity() != null) {
                    MusicView mv = new MusicView(getBaseActivity(), vm.getMusic().getValue());
                    Dialog.showSong(getBaseActivity(), null, mv, null, null, null, null, dialogInterface -> {
                        if (mv.getParent() != null) {
                            tts.setSpeechRate((float) 0.5);
                            tts.speak("The recognized song is " + song.getSongArtist() + song.getSongTitle(), TextToSpeech.QUEUE_FLUSH, null, null);
                            ((ViewGroup) mv.getParent()).removeView(mv);
                        }

                    });
                }
            } else {
                tts.setSpeechRate((float) 0.5);
                tts.speak("No result found. Please, try again!", TextToSpeech.QUEUE_FLUSH, null, null);
                startActivity(NotFoundActivity.createIntent(this.getActivity()));
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().onBackPressed();
                return true;
            }
            case R.id.history: {
                startActivity(HistoryActivity.createIntent(this.getActivity()));

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public static MainViewModel getViewModel() {
        return vm;
    }

    @Override
    public void onVolumeChanged(double v) {

    }

    @Override
    public void onResult(ACRCloudResult results) {
        vm.reset();
        String res = results.getResult();
        try {
            vm.findMusic(res);
        } catch (JSONException e) {
            Toast.makeText(getBaseActivity(), R.string.msg_error, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        binding.message.setText(R.string.msg_start);
        binding.listen.setProgress(0f);
        binding.listen.pauseAnimation();
    }


    public void verifyPermissions() {
        for (int i = 0; i < PERM.length; i++) {
            int permission = ActivityCompat.checkSelfPermission(getActivity(), PERM[i]);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), PERM,
                        1);
                break;
            }
        }
    }

    @Override
    public void onDestroy() {

        if (vm.getClient() != null) {
            vm.getClient().release();
            vm.setInitState(false);
            vm.setClient(null);
        }

        super.onDestroy();
    }

}