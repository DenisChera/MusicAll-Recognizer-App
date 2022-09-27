package com.denis.music_rec.ui.history;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.denis.music_rec.Common;
import com.denis.music_rec.ui.detail.DetailActivity;
import com.denis.music_rec.R;
import com.denis.music_rec.data.db.DBManager;
import com.denis.music_rec.data.model.Colors;
import com.denis.music_rec.data.model.Music;
import com.denis.music_rec.databinding.FragmentHistoryBinding;
import com.denis.music_rec.ui.BaseFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import petrov.kristiyan.colorpicker.ColorPicker;

public class HistoryFragment extends BaseFragment {
    private FragmentHistoryBinding binding;
    private Animation fabOpen, fabClose, rotateFwd, rotateBwd;
    boolean isOpen = false;
    ArrayList<Music> songs = new ArrayList<>();
    ArrayList<Integer> colorsList = new ArrayList<>();

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        getBaseActivity().setSupportActionBar(binding.toolbar);
        if (getBaseActivity().getSupportActionBar() != null) {
            getBaseActivity().getSupportActionBar().setDisplayShowTitleEnabled(false);
            getBaseActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.toolbar.setTitle(R.string.label_history);
        DBManager.setContext(getBaseActivity());

        fabOpen = AnimationUtils.loadAnimation(getContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(getContext(), R.anim.fab_close);
        rotateFwd = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_forward);
        rotateBwd = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_backward);

        binding.deleteAllBtn.setOnClickListener(v -> {
            confirmDialog();
        });
        binding.changeTheme.setOnClickListener(v -> {
            openColorPicker();
        });

        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_history, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort: {
                sortAZ();
                return true;
            }

            case R.id.sortReverse: {
                sortZA();
                return true;
            }
            case R.id.sortDef: {
                sortDefault();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void sortDefault() {
        songs = DBManager.getManager().getMusicList();
        colorsList = DBManager.getManager().getColor();
        executeData();
    }


    private void sortAZ() {
        songs = DBManager.getManager().getMusicList();
        if(songs.isEmpty()==true){
            Toast.makeText(getContext(), "The history is empty!", Toast.LENGTH_SHORT).show();
        }
        else {
            Collections.sort(songs, (obj1, obj2) -> obj2.getSongTitle().compareToIgnoreCase(obj1.getSongTitle()));
        }
        executeData();
    }

    private void sortZA() {
        songs = DBManager.getManager().getMusicList();
        if(songs.isEmpty()==true)
            {
                Toast.makeText(getContext(), "The history is empty!", Toast.LENGTH_SHORT).show();
            }
        else {
            Collections.sort(songs, new Comparator<Music>() {
                public int compare(Music obj1, Music obj2) {
                    return obj1.getSongTitle().compareToIgnoreCase(obj2.getSongTitle());
                }
            });
        }
        executeData();
    }

    @Override
    public void onResume() {
        super.onResume();
        sortDefault();
    }

    private void executeData() {
        if (colorsList.isEmpty() == false) {
            binding.historyContainer.setBackgroundColor(colorsList.get(colorsList.size() - 1));
        }
        binding.fab.setOnClickListener(v -> animateFab());
        if (songs.size() == 0) {
            binding.emptyImage.setVisibility(View.VISIBLE);
            binding.noDataText.setVisibility(View.VISIBLE);
        } else {
            binding.emptyImage.setVisibility(View.GONE);
            binding.noDataText.setVisibility(View.GONE);
        }
        binding.lnContainer.removeAllViews();
        for (int i = songs.size() - 1; i >= 0; i--) {
            Music info = songs.get(i);
            final View v2 = LayoutInflater.from(getContext()).inflate(R.layout.music_item, null);
            TextView txtTitle = (TextView) v2.findViewById(R.id.txtTitle);
            txtTitle.setText(info.getSongTitle());
            TextView txtArtist = (TextView) v2.findViewById(R.id.txtArtist);
            txtArtist.setText("Artist: " + info.getSongArtist());
            TextView txtRelease = (TextView) v2.findViewById(R.id.txtRelease);
            txtRelease.setText(info.getSongReleaseDate());

            TextView txtGenre = (TextView) v2.findViewById(R.id.txtGenre);
            txtGenre.setText(info.getSongGenre());

            MaterialCardView lnItem = (MaterialCardView) v2.findViewById(R.id.lnItem);
            lnItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Common.getInstance().selectedInfo = info;
                    getContext().startActivity(new Intent(getContext(), DetailActivity.class));
                }
            });
            binding.lnContainer.addView(v2);
        }
    }

    void confirmDialog() {
        AlertDialog.Builder b = new AlertDialog.Builder(getContext());
        b.setTitle("Do you want to remove all history data?");
        b.setMessage("Are you sure you want to clear the history?");
        b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DBManager.getManager().deleteAllData();
                binding.lnContainer.removeAllViews();
                binding.emptyImage.setVisibility(View.VISIBLE);
                binding.noDataText.setVisibility(View.VISIBLE);
            }
        });
        b.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        b.create().show();
    }

    void openColorPicker() {
        ArrayList<Integer> arrayList2 = DBManager.getManager().getColor();
        final ColorPicker cp = new ColorPicker(getBaseActivity());
        ArrayList<String> c = new ArrayList<>();
        c.add("#000000");
        c.add("#DBC434");
        c.add("#34DB37");
        c.add("#34DBDB");
        c.add("#6C34DB");
        c.add("#DB343C");
        c.add("#34DBB0");
        c.add("#E9EAEA");
        c.add("#FFBF16");
        c.add("#BCFEF5");

        cp.setColors(c)
                .setColumns(5)
                .setRoundColorButton(true)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int col) {
                        binding.historyContainer.setBackgroundColor(col);
                        if (arrayList2.isEmpty() == false) {
                            DBManager.getManager().deleteAllColors();
                        }
                        DBManager.getManager().insertColors(new Colors(col));

                        //mainToolbar.setBackgroundColor(color);
                        //change_theme.setBackgroundColor(color);
                        //home_container.setBackgroundColor(color);
//                        recognize_theme.setBackgroundColor(color);
//                        login_theme.setBackgroundColor(color);
//                        register_theme.setBackgroundColor(color);
//                        setup_theme.setBackgroundColor(color);
//                        change_pw_theme.setBackgroundColor(color);
                    }

                    @Override
                    public void onCancel() {
                    }
                }).show();
    }

    private void animateFab() {
        if (isOpen) {
            binding.fab.startAnimation(rotateFwd);
            binding.deleteAllBtn.startAnimation(fabClose);
            binding.changeTheme.startAnimation(fabClose);
            binding.deleteAllBtn.setClickable(false);
            binding.changeTheme.setClickable(false);
            isOpen = false;
        } else {
            binding.fab.startAnimation(rotateBwd);
            binding.deleteAllBtn.startAnimation(fabOpen);
            binding.changeTheme.startAnimation(fabOpen);
            binding.deleteAllBtn.setClickable(true);
            binding.changeTheme.setClickable(true);
            isOpen = true;
        }
    }
}