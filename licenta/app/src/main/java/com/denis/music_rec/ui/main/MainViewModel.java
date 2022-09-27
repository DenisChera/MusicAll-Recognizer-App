package com.denis.music_rec.ui.main;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.acrcloud.rec.ACRCloudClient;
import com.acrcloud.rec.ACRCloudConfig;
import com.acrcloud.rec.IACRCloudListener;
import com.acrcloud.rec.IACRCloudPartnerDeviceInfo;
import com.acrcloud.rec.utils.ACRCloudLogger;
import com.denis.music_rec.Common;
import com.denis.music_rec.data.db.DBManager;
import com.denis.music_rec.data.model.Music;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    private boolean processing = false;
    private boolean initState = false;
    private ACRCloudConfig config = null;
    private ACRCloudClient client = null;
    private MutableLiveData<Music> music = new MutableLiveData<Music>();

    public void setMusic(MutableLiveData<Music> music) {
        this.music = music;
    }
    public MutableLiveData<Music> getMusic() {
        return music;
    }
    public ACRCloudClient getClient() {
        return client;
    }
    public void init(IACRCloudListener arcloudListener, Context context) {
        this.config = new ACRCloudConfig();

        this.config.acrcloudListener = arcloudListener;
        this.config.context = context;
        this.config.host = "identify-eu-west-1.acrcloud.com";
        this.config.accessKey = "7f13e78bee8495263afbde3262ecd566";
        this.config.accessSecret = "u0DUADBVTghv2CWufzEjDMGFLphHuN1mjWnc33rt";
        this.config.recorderConfig.rate = 8000;
        this.config.recorderConfig.channels = 1;
        this.client = new ACRCloudClient();
        ACRCloudLogger.setLog(true);

        this.initState = this.client.initWithConfig(this.config);
    }

    public void setClient(ACRCloudClient client) {
        this.client = client;
    }

    public void setInitState(boolean initState) {
        this.initState = initState;
    }

    public void startProcessing() {
        if (!this.initState) {
            return;
        }

        if (!processing) {
            processing = true;
            if (this.client == null || !this.client.startRecognize()) {
                processing = false;
            }
        }
    }

    public void cancel() {
        if (processing && this.client != null) {
            this.client.cancel();
        }

        this.reset();
    }

    public void reset() {
        processing = false;
    }

    public void findMusic(String musicSignature) throws JSONException {
        //String output = "";
        JSONObject resultJson = new JSONObject(musicSignature);
        JSONObject status = resultJson.getJSONObject("status");
        int code = status.getInt("code");
        Music m = new Music();
        if (code == 0) {
            JSONObject mData = resultJson.getJSONObject("metadata");
            if (mData.has("music")) {
                JSONArray muzic = mData.getJSONArray("music");
                for (int i = 0; i < 1; i++) {
                    JSONObject sng = (JSONObject) muzic.get(i);
                    String songTitle = sng.getString("title");
                    JSONArray artists = sng.getJSONArray("artists");
                    JSONObject artist = (JSONObject) artists.get(0);
                    String artistName = artist.getString("name");
                    String releaseDate = sng.optString("release_date", "");
                    JSONArray genres = sng.optJSONArray("genres");
                    if (genres != null) {
                        JSONObject gnr = genres.getJSONObject(0);
                        String genre = gnr.getString("name");
                        m.setSongGenre(genre);
                    }

                    //output = output + (i + 1) + ".  Title: " + songTitle + "    Artist: " + artistName + "\n";

                    m.setSongTitle(songTitle);
                    m.setSongArtist(artistName);
                    m.setSongReleaseDate(releaseDate);

                    if (!songTitle.equals("")) {
                        ArrayList<Music> arrayList = DBManager.getManager().getMusicList();
                        boolean exist = false;
                        for (int index = 0; index < arrayList.size(); index++) {
                            if ((arrayList.get(index).getSongTitle().equals(songTitle)) && (arrayList.get(index).getSongArtist().equals(artistName))) {
                                DBManager.getManager().deleteDuplicate(arrayList.get(index).getId().toString());
                                DBManager.getManager().insertMusicInfo(m);
                                exist = true;
                                break;
                            }
                        }
                        if (!exist) {
                            DBManager.getManager().insertMusicInfo(m);
                        }
                    }
                }
            }
            music.setValue(m);
        } else {
            music.setValue(null);
        }
    }
}