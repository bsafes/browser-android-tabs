package org.chromium.chrome.browser;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.chromium.chrome.R;
import org.chromium.chrome.browser.readlist.RListHelper;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.view.View.GONE;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;

public class AddReadingList extends Activity {

    TextView BtnClose, ReadContent, ReadForward;
    ImageView Forward, ReadLogo;
    TextView titleTextView;

    String sharedText = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.readinglist_dialog);
        titleTextView = (TextView) findViewById(R.id.readshare_title);
        BtnClose = (TextView) findViewById(R.id.btn_readshare_exit);
        BtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ReadContent = (TextView) findViewById(R.id.readshare_content);
        ReadLogo = (ImageView) findViewById(R.id.iv_readshare_logo);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            }
        }

        final String[] value = new String[2];

        try {
            Thread nThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        InputStream is = (InputStream) new URL(sharedText).openStream();
                        Scanner scanner = new Scanner(is);
                        String responseBody = scanner.useDelimiter("\\A").next();
                        value[0] = responseBody.substring(responseBody.indexOf("<title>") + 7, responseBody.indexOf("</title>"));
                        Pattern pattern = Pattern.compile("<link .*? href=\"(.*?.ico)\"");
                        Matcher matcher = pattern.matcher(responseBody);
                        if (matcher.find()) {
                            value[1] = matcher.group(1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            nThread.start();
            nThread.join();
        } catch (Exception e) {
        }

        displayText(sharedText, value[0], value[1]);

        ReadForward = (TextView) findViewById(R.id.btn_readshare_save);
        ReadForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSendText(sharedText, value[0], value[1]);
            }
        });
    }

    void displayText(String url, String title, String logo_url) {

        final String lg_url = logo_url;

        if (url != null) {
            titleTextView.setText(title);

            ReadContent.setText(url);

            //Picasso.get().load(lg_url).into(ReadLogo);

            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        InputStream is = (InputStream) new URL(lg_url).getContent();
                        ReadLogo.setImageBitmap(BitmapFactory.decodeStream(is));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    void handleSendText(String url, String title, String logo_url) {

        if (url != null) {
            RListHelper rListHelper = new RListHelper(getBaseContext());
            SQLiteDatabase db = rListHelper.getReadableDatabase();
            rListHelper.insertURLs(url, title, logo_url, db);


            findViewById(R.id.readshare_layout).setVisibility(GONE);

            Snackbar bar = Snackbar.make(findViewById(android.R.id.content), "Saved to Reading List", Snackbar.LENGTH_LONG).setAction("Action", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String activityToStart = "org.chromium.chrome.browser.document.ChromeLauncherActivity";
                    try {
                        Class<?> c = Class.forName(activityToStart);
                        Intent i = new Intent(getBaseContext(), c);
                        startActivity(i);
                    } catch (ClassNotFoundException ignored) {
                    }
                }
            }).setDuration(2000);
            bar.show();

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            };
            Handler handler = new android.os.Handler();
            handler.postDelayed(runnable, 2000);

            finish();
            Toast.makeText(this, "Saved to Reading List", Toast.LENGTH_LONG).show();
        }


    }

    public boolean isValidURL(String urlStr) {
        try {
            URL url = new URL(urlStr);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }


}