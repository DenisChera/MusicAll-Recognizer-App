package com.denis.music_rec;

import com.denis.music_rec.data.model.Music;

public class Common {
    private static Common s_instance = null;

    public static Common getInstance() {
        if (s_instance == null) {
            s_instance = new Common();

        }
        synchronized (s_instance) {
            return s_instance;
        }
    }
    public Music selectedInfo;

    public Common()
    {

        selectedInfo = new Music();
    }
}
