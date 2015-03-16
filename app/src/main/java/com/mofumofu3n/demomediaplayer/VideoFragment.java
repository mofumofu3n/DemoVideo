package com.mofumofu3n.demomediaplayer;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.IOException;
import java.util.HashMap;

public class VideoFragment extends Fragment {
    private static final String VIDEO_URL = "http://www.androidbegin.com/tutorial/AndroidCommercial.3gp";

    private VideoView mVideoView;
    private ImageView mThumbnailView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_video, container, false);
        mVideoView = (VideoView) view.findViewById(R.id.video);
        mThumbnailView = (ImageView) view.findViewById(R.id.thumbnail);
        mThumbnailView.setImageBitmap(getThumbnail());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupVideoView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mVideoView.suspend();
    }

    private void setupVideoView() {
        mVideoView.setVideoURI(Uri.parse(VIDEO_URL));
        mVideoView.setOnPreparedListener(getPreparedListener());
        mVideoView.setOnCompletionListener(getCompletionListener());
        mVideoView.setMediaController(getController());
        mVideoView.start();
    }

    private MediaController getController() {
        final MediaController controller = new MediaController(getActivity());
        return controller;
    }

    private MediaPlayer.OnCompletionListener getCompletionListener() {
        return new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.stop();
                try {
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private MediaPlayer.OnPreparedListener getPreparedListener() {
        return new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mThumbnailView.setVisibility(View.GONE);
                mediaPlayer.setLooping(false);
                mediaPlayer.start();
            }
        };
    }

    private Bitmap getThumbnail() {
        final MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(VIDEO_URL, new HashMap<String, String>());
        return retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC);
    }
}
