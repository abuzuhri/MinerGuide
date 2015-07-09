package com.minergame.minerguide.ui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.minergame.minerguide.R;
import com.minergame.minerguide.utils.AppAction;

import java.net.URI;

/**
 * Created by tareq on 06/30/2015.
 */
public class VideoPlayerActivity extends  Activity {
    private VideoView mVideoView;
    private ProgressBar progressbar;
    //private static ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_video_view);

        Intent intent = getIntent();
        String VideoURL = intent.getStringExtra(AppAction.EXTRA.URLEXTRA);
        final VideoView videoView = (VideoView) findViewById(R.id.VideoView);
        final ProgressBar progressbar = (ProgressBar) findViewById(R.id.progressbar);
        try {
            //String VideoURL = "https://drive.google.com/uc?export=download&id=0B8CwmPxP19h2XzZMcUszV2hwdGc";//http://pass2you.com/0ilhechxgwu.webm";//"https://www.dropbox.com/s/67u9c6urev13bd8/0i43tisxmd4.webm?dl=1";
            try {
                // Start the MediaController
                MediaController mediacontroller = new MediaController(this);
                mediacontroller.setAnchorView(videoView);
                // Get the URL from String VideoURL
                Uri video = Uri.parse(VideoURL);
                videoView.setMediaController(mediacontroller);
                videoView.setVideoURI(video);

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            videoView.requestFocus();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                // Close the progress bar and play the video
                public void onPrepared(MediaPlayer mp) {
                    progressbar.setVisibility(View.GONE);
                    videoView.setVisibility(View.VISIBLE);
                    videoView.start();

                }

            });
        } catch (Exception e) {
            this.finish();
            e.printStackTrace();
        }
    }

}
