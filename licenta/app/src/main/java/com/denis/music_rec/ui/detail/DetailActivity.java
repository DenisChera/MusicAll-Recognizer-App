package com.denis.music_rec.ui.detail;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.denis.music_rec.Common;
import com.denis.music_rec.R;
import com.denis.music_rec.Util;
import com.denis.music_rec.data.db.DBManager;
import com.denis.music_rec.ui.BaseActivity;
import com.denis.music_rec.ui.history.HistoryFragment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Locale;

import top.defaults.colorpicker.ColorPickerPopup;


public class DetailActivity extends BaseActivity {
    private ImageView closeButton;
    TextView txtTitle, txtArtist, txtRelease, txtLyrics;
    ProgressDialog mProgDlg;
    private static final int PERMISSION_CODE = 200;

    int pageHeight = 2000;
    int pagewidth = 792;
    private HistoryFragment historyFragment;
    private LinearLayout parentLinearLayout;
    private ImageView font_styles;
    private ImageView speech;
    private TextToSpeech tts;
    public String lyrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        DBManager.setContext(DetailActivity.this);
        parentLinearLayout = (LinearLayout) findViewById(R.id.lnItem);
        mProgDlg = new ProgressDialog(DetailActivity.this);
        mProgDlg.setMessage("Wait a little!");
        mProgDlg.setCancelable(false);
        historyFragment = new HistoryFragment();
        speech = findViewById(R.id.speech);

        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtTitle.setText(Common.getInstance().selectedInfo.getSongTitle());
        txtArtist = (TextView) findViewById(R.id.txtArtist);
        txtArtist.setText("Artist: " + Common.getInstance().selectedInfo.getSongArtist());
        txtRelease = (TextView) findViewById(R.id.txtRelease);
        txtRelease.setText(Common.getInstance().selectedInfo.getSongReleaseDate());
        txtLyrics = (TextView) findViewById(R.id.txtLyrics);

        closeButton = findViewById(R.id.close_activity);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tts.isSpeaking())
                    tts.stop();
                finish();

            }
        });
        (findViewById(R.id.downloadPdf)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    Toast.makeText(DetailActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    savePDF();
                } else {
                    requestPermission();
                }

            }
        });

        (findViewById(R.id.deleteItem)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletionDialog(v);

            }
        });

        (findViewById(R.id.font_styles)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFontStyle();

            }
        });


        (findViewById(R.id.color_picker)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ColorPickerPopup.Builder(DetailActivity.this).initialColor(Color.RED)
                        .enableBrightness(true)
                        .enableAlpha(true)
                        .okTitle("Choose")
                        .cancelTitle("Cancel")
                        .showIndicator(true)
                        .showValue(true)
                        .build()
                        .show(v, new ColorPickerPopup.ColorPickerObserver() {
                            @Override
                            public void onColorPicked(int color) {
                                txtLyrics.setTextColor(color);
                            }

                            @Override
                            public void onColor(int color, boolean fromUser) {
                                txtLyrics.setTextColor(color);
                            }
                        });

            }
        });

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(Locale.US);


            }
        });

        (findViewById(R.id.speech)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSpeechSpeed();
            }
        });

        getLyrics();
    }

    private void getLyrics() {
        String url = "http://api.chartlyrics.com/apiv1.asmx/SearchLyricDirect?artist=" + Common.getInstance().selectedInfo.getSongArtist().toLowerCase().trim() + "&song=" + Common.getInstance().selectedInfo.getSongTitle().toLowerCase().trim();
        new TaskGetData().execute(url);
    }

    private void changeFontStyle() {
        final String[] styleList = new String[]{"Normal", "Bold", "Italic", "Bold Italic"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Font Style");
        builder.setItems(styleList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                if (position == 0) {
                    txtLyrics.setTypeface(null, Typeface.NORMAL);
                } else if (position == 1) {
                    txtLyrics.setTypeface(txtLyrics.getTypeface(), Typeface.BOLD);
                } else if (position == 2) {
                    txtLyrics.setTypeface(txtLyrics.getTypeface(), Typeface.ITALIC);
                } else if (position == 3) {
                    txtLyrics.setTypeface(txtLyrics.getTypeface(), Typeface.BOLD_ITALIC);
                }

            }
        });
        builder.show();
    }

    private void changeSpeechSpeed() {
        final String[] styleList = new String[]{"x0.1", "x0.5", "x1.0", "x1.5", "Stop"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Speech Speed");
        builder.setItems(styleList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position) {
                if (position == 0) {
                    tts.setSpeechRate((float) 0.1);
                    tts.speak(txtLyrics.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
                } else if (position == 1) {
                    tts.setSpeechRate((float) 0.5);
                    tts.speak(txtLyrics.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
                } else if (position == 2) {
                    tts.setSpeechRate((float) 1.0);
                    tts.speak(txtLyrics.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
                } else if (position == 3) {
                    tts.setSpeechRate((float) 1.5);
                    tts.speak(txtLyrics.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
                } else if (position == 4) {
                    tts.stop();

                }

            }
        });
        builder.show();
    }

    public class TaskGetData extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            mProgDlg.show();
            //textView.append("Get data ...\n\n");
        }

        @Override
        protected String doInBackground(String... params) {
            return Util.getDataHttpUriConnection(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mProgDlg.hide();
            try {
                String lyric = processLyricXml(result);
                lyrics = lyric;
                txtLyrics.setText(lyrics);

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    public static String processLyricXml(String s)
            throws XmlPullParserException, IOException {
        XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
        xppf.setNamespaceAware(true);
        XmlPullParser xpp = xppf.newPullParser();
        xpp.setInput(new StringReader(s));
        int et = xpp.getEventType();
        boolean done = false;
        while (et != XmlPullParser.END_DOCUMENT) {
            if (et == XmlPullParser.START_DOCUMENT) {
                System.out.println("Start document");
            } else if (et == XmlPullParser.START_TAG) {
                System.out.println("Start tag " + xpp.getName());
                if (xpp.getName().equals("Lyric")) {
                    done = true;
                }
            } else if (et == XmlPullParser.END_TAG) {
                System.out.println("End tag " + xpp.getName());

            } else if (et == XmlPullParser.TEXT) {
                if (done) {
                    System.out.println("Text " + xpp.getText());
                    done = false;
                    String lyricsText = xpp.getText().trim();
                    if (!lyricsText.isEmpty()) {
                        return xpp.getText();
                    }
                }
            }
            et = xpp.next();
        }
        System.out.println("End document");
        return "No Lyrics";
    }

    public void TextToSpeechButton(View view) {
        tts.speak(txtLyrics.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
    }

    void deletionDialog(View v) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        AppCompatActivity appCompatActivity = new AppCompatActivity();
        b.setTitle("Delete " + txtTitle.getText().toString() + " ?");
        b.setMessage("Are you sure you want to delete " + txtTitle.getText().toString() + " ?");
        b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DBManager.getManager().deleteItem(Common.getInstance().selectedInfo.getId().toString());
                //parentLinearLayout.removeView(v);
                finish();
            }
        });
        //historyFragment.lnContainer.removeView((View) v.getParent());
        b.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        b.create().show();
    }

    private void savePDF() {
        PdfDocument pdf = new PdfDocument();
        Paint p = new Paint();
        Paint txt = new Paint();
        Paint genre = new Paint();

        String[] arr = lyrics.split("\n");
        int height = 130;
        for (int i = 0; i < arr.length; i++) {
            //canvas.drawText(arr[i], 50, height, title);
            height += 25;
        }
        height += 100;
        PdfDocument.PageInfo info = new PdfDocument.PageInfo.Builder(pagewidth, height, 1).create();
        PdfDocument.Page page = pdf.startPage(info);
        Canvas cnvs = page.getCanvas();
        txt.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        txt.setTextSize(15);
        cnvs.drawText(Common.getInstance().selectedInfo.getSongTitle(), 50, 50, txt);
        cnvs.drawText("Artist: " + Common.getInstance().selectedInfo.getSongArtist(), 50, 80, txt);
        cnvs.drawText("Release Date: " + Common.getInstance().selectedInfo.getSongReleaseDate(), 50, 110, txt);
        if (txtLyrics.getTypeface().isBold() == true && txtLyrics.getTypeface().isItalic() == false) {
            txt.setTypeface(Typeface.defaultFromStyle((Typeface.BOLD)));
        } else if (txtLyrics.getTypeface().isItalic() == true && txtLyrics.getTypeface().isBold() == false) {
            txt.setTypeface(Typeface.defaultFromStyle((Typeface.ITALIC)));
        } else if (txtLyrics.getTypeface().isItalic() == true && txtLyrics.getTypeface().isBold() == true) {
            txt.setTypeface(Typeface.defaultFromStyle((Typeface.BOLD_ITALIC)));
        } else if (txtLyrics.getTypeface().isItalic() == false && txtLyrics.getTypeface().isBold() == false) {
            txt.setTypeface(Typeface.defaultFromStyle((Typeface.NORMAL)));
        }

        txt.setColor(txtLyrics.getCurrentTextColor());
        txt.setTextSize(15);

        arr = lyrics.split("\n");
        height = 140;
        for (int i = 0; i < arr.length; i++) {
            cnvs.drawText(arr[i], 50, height, txt);
            height += 25;
        }
        //canvas.drawText(Common.getInstance().lyrics, 50, 150, title);
        pdf.finishPage(page);

        String fileName = Common.getInstance().selectedInfo.getSongArtist() + " - " + Common.getInstance().selectedInfo.getSongTitle() + ".pdf";
        File file = new File(Environment.getExternalStorageDirectory(), fileName);

        try {
            pdf.writeTo(new FileOutputStream(file));
            Toast.makeText(DetailActivity.this, "Pdf document was generated successfully.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pdf.close();
    }

    private boolean checkPermission() {
        int perm1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int perm2 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return perm1 == PackageManager.PERMISSION_GRANTED && perm2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0) {
                boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (write && read) {
                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show();
                    savePDF();
                } else {
                    Toast.makeText(this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

}
