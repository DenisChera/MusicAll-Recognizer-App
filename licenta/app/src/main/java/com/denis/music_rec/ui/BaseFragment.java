package com.denis.music_rec.ui;

import androidx.fragment.app.Fragment;

import com.denis.music_rec.MainActivity;

public class BaseFragment extends Fragment {

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }
}
