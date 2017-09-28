package com.safeoregon.app;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.safeoregon.app.utils.VideoSliceSeekBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class VideoTrimActivity extends Activity {

    private static final String TAG = VideoTrimActivity.class.getSimpleName();

    TextView textViewLeft, textViewRight;
    VideoSliceSeekBar videoSliceSeekBar;
    VideoView videoView;
    Button videoControlBtn;
    View videoSabeBtn;

    FFmpeg ffmpeg;

    private ProgressDialog progressDialog;
    private LinearLayout llframes;
    private MediaMetadataRetriever mediaMetadataRetriever;
    private String filepath = "/storage/emulated/0/Download/big_buck_bunny_720p_5mb.mp4";
    private View llLeft;
    private View llRight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_trim);

        Intent intent = getIntent();
         filepath = intent.getStringExtra("filepath");
        if (filepath == null || filepath.equalsIgnoreCase("")) {
            Toast.makeText(VideoTrimActivity.this, "filepath = " + filepath, Toast.LENGTH_LONG).show();
            finish();
        } else {
            File file = new File(filepath);
            if (!file.exists()) {
                finish();
                Toast.makeText(VideoTrimActivity.this, "filepath not available", Toast.LENGTH_LONG).show();
            }
        }
        textViewLeft = (TextView) findViewById(R.id.left_pointer);
        textViewRight = (TextView) findViewById(R.id.right_pointer);

        videoSliceSeekBar = (VideoSliceSeekBar) findViewById(R.id.seek_bar);
        videoView = (VideoView) findViewById(R.id.video);
        videoControlBtn = (Button) findViewById(R.id.video_control_btn);
        videoSabeBtn = findViewById(R.id.saveButton);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(null);
        llLeft = findViewById(R.id.llLeft);
        llRight= findViewById(R.id.llRight);
        llframes = (LinearLayout) findViewById(R.id.llframes);

        initVideoView();

    }

    int leftThumb = 0, rightThumb = 0;

    private void initVideoView() {
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            public boolean onError(MediaPlayer mp, int what, int extra) {
                // TODO Auto-generated method stub
                Toast.makeText(VideoTrimActivity.this, "File not supported", Toast.LENGTH_LONG).show();
                finish();
                return true;
            }

        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(final MediaPlayer mp) {

                mediaMetadataRetriever = new MediaMetadataRetriever();
                mediaMetadataRetriever.setDataSource(filepath);

                loadFFMpegBinary();

                videoSliceSeekBar.setSeekBarChangeListener(new VideoSliceSeekBar.SeekBarChangeListener() {
                    @Override
                    public void SeekBarValueChanged(int leftThumb1, int rightThumb1) {

                        llLeft.getLayoutParams().width = videoSliceSeekBar.getLeftThumbXPos();
                        llLeft.requestLayout();
                        llRight.setX(videoSliceSeekBar.getRightThumbXPos());
                        textViewLeft.setText(getTimeForTrackFormat(leftThumb1, true));
                        textViewRight.setText(getTimeForTrackFormat(rightThumb1, true));
                        if (leftThumb1 != leftThumb) {
                            leftThumb = leftThumb1;
                            mp.seekTo(leftThumb);
                        } else if (rightThumb1 != rightThumb) {
                            rightThumb = rightThumb1;
                            mp.seekTo(rightThumb);
                        }

                    }
                });

                videoSliceSeekBar.setMaxValue(mp.getDuration());
                videoSliceSeekBar.setLeftProgress(0);
                videoSliceSeekBar.setRightProgress(mp.getDuration());
                // videoSliceSeekBar.setRightProgress(10000); //10 segundos como máximo de entrada
                // videoSliceSeekBar.setProgressMinDiff((5000 * 100)/mp.getDuration()); //Diferencia mínima de 5 segundos
                // videoSliceSeekBar.setProgressMaxDiff((10000 * 100)/mp.getDuration());//Diferencia máxima de 10 segundos
                // videoSliceSeekBar.setProgressMinDiff(mp.getDuration() / 10);

                addFrames(mp);
                videoControlBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        performVideoViewClick();
                    }
                });

                videoSabeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Left progress : " + videoSliceSeekBar.getLeftProgress() / 1000);
                        Log.d(TAG, "Right progress : " + videoSliceSeekBar.getRightProgress() / 1000);

                        Log.d(TAG, "Total Duration : " + mp.getDuration() / 1000);
                        executeTrimCommand(videoSliceSeekBar.getLeftProgress(), videoSliceSeekBar.getRightProgress());
                    }

                });

            }
        });


        if (filepath != null && !filepath.equalsIgnoreCase(""))
            videoView.setVideoPath(filepath);
        else
            Toast.makeText(VideoTrimActivity.this, "filepath not available", Toast.LENGTH_LONG).show();
        //videoView.setVideoURI(Uri.parse("android.resource://com.etechmavens.myapplicationdemo/" + R.raw.make_your_song));

    }

    private void addFrames(MediaPlayer mp) {
        llframes.removeAllViews();
        int addCount = mp.getDuration() / 6;
        llframes.setBackgroundColor(Color.BLUE);
        for (int i = 0; i < 6; i++) {


            Bitmap bmFrame = mediaMetadataRetriever
                    .getFrameAtTime(addCount * i * 1000); //unit in microsecond

            if (bmFrame == null) {

            } else {

                ImageView capturedImageView = new ImageView(VideoTrimActivity.this);
                capturedImageView.setImageBitmap(bmFrame);
                capturedImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                LinearLayout.LayoutParams capturedImageViewLayoutParams =
                        new LinearLayout.LayoutParams(0, 100, 1f);
                capturedImageView.setLayoutParams(capturedImageViewLayoutParams);

                llframes.addView(capturedImageView);
            }


        }

    }

    private void loadFFMpegBinary() {
        try {

            progressDialog.setMessage("Loading...");
            progressDialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (ffmpeg == null) {
                            ffmpeg = FFmpeg.getInstance(VideoTrimActivity.this);
                        }
                        ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                            @Override
                            public void onFailure() {
                                progressDialog.dismiss();
                                showUnsupportedExceptionDialog();
                            }

                            @Override
                            public void onSuccess() {
                                progressDialog.dismiss();
                                Log.d(TAG, "ffmpeg : correct Loaded");
                            }
                        });
                    } catch (Exception e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            Log.d(TAG, "EXception : " + e);
        }
    }

    private void execFFmpegBinary(final String command) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Log.d(TAG, "FAILED with output : " + s);
                }

                @Override
                public void onSuccess(String s) {
                    Log.d(TAG, "SUCCESS with output : " + s);
                }

                @Override
                public void onProgress(String s) {
                    Log.d(TAG, "Started command : ffmpeg " + command);
                    Log.d(TAG, "progress : " + s);
                }

                @Override
                public void onStart() {
                    Log.d(TAG, "Started command : ffmpeg " + command);
                    progressDialog.setMessage("Processing...");
                    progressDialog.show();
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "Finished command : ffmpeg " + command);
                    progressDialog.dismiss();

                    Intent intent = getIntent();
                    intent.putExtra("filepath", filepath);
                    setResult(RESULT_OK, intent);
                    finish();

                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
        }
    }

    private void performVideoViewClick() {

        if (videoView.isPlaying()) {
            videoControlBtn.setText("Play");
            videoView.pause();
            videoSliceSeekBar.setSliceBlocked(false);
            videoSliceSeekBar.removeVideoStatusThumb();
        } else {
            videoView.seekTo(videoSliceSeekBar.getLeftProgress());
            videoView.start();
            videoControlBtn.setText("Pause");
            videoSliceSeekBar.setSliceBlocked(true);
            videoSliceSeekBar.videoPlayingProgress(videoSliceSeekBar.getLeftProgress());
            videoStateObserver.startVideoProgressObserving();
        }
    }

    public static String getTimeForTrackFormat(int timeInMills, boolean display2DigitsInMinsSection) {
        int minutes = (timeInMills / (60 * 1000));
        int seconds = (timeInMills - minutes * 60 * 1000) / 1000;
        String result = display2DigitsInMinsSection && minutes < 10 ? "0" : "";
        result += minutes + ":";
        if (seconds < 10) {
            result += "0" + seconds;
        } else {
            result += seconds;
        }
        return result;
    }

    private void showUnsupportedExceptionDialog() {
        new AlertDialog.Builder(VideoTrimActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("device_not_supported")
                .setMessage("device_not_supported_message")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VideoTrimActivity.this.finish();
                    }
                })
                .create()
                .show();

    }


    private StateObserver videoStateObserver = new StateObserver();

    private class StateObserver extends Handler {

        private boolean alreadyStarted = false;

        private void startVideoProgressObserving() {
            if (!alreadyStarted) {
                alreadyStarted = true;
                sendEmptyMessage(0);
            }
        }

        private Runnable observerWork = new Runnable() {
            @Override
            public void run() {
                startVideoProgressObserving();
            }
        };

        @Override
        public void handleMessage(Message msg) {
            alreadyStarted = false;
            videoSliceSeekBar.videoPlayingProgress(videoView.getCurrentPosition());
            if (videoView.isPlaying() && videoView.getCurrentPosition() < videoSliceSeekBar.getRightProgress()) {
                postDelayed(observerWork, 50);
            } else {

                if (videoView.isPlaying()) videoView.pause();
                videoControlBtn.setText("Play");
                videoSliceSeekBar.setSliceBlocked(false);
                videoSliceSeekBar.removeVideoStatusThumb();
            }
        }
    }

    private void executeTrimCommand(int startMs, int endMs) {
        File storageDirectory = Environment.getExternalStorageDirectory();

        try {
            // InputStream inputStream = new FileInputStream(filepath);
            File src = new File(filepath);

            //  storeFile(inputStream, src);


            // File dest = new File(storageDirectory, "trimmed_1." + filepath.substring(filepath.lastIndexOf(".") + 1, filepath.length()));
            File destParent = new File(storageDirectory, "SafeOregon");
            if (!destParent.exists())
                destParent.mkdir();
            File dest = new File(destParent.getAbsolutePath(), "trimmed_" + src.getName());
            if (dest.exists()) {
                dest.delete();
            }


            Log.d(TAG, "startTrim: src: " + filepath);
            Log.d(TAG, "startTrim: dest: " + dest.getAbsolutePath());
            Log.d(TAG, "startTrim: startMs: " + startMs);
            Log.d(TAG, "startTrim: endMs: " + endMs);

            execFFmpegBinary("-i " + filepath + " -vcodec copy -acodec copy -ss " + startMs / 1000 + " -to " + endMs / 1000 + " " + dest.getAbsolutePath() + " -strict -2 -async 1 ");

            filepath = dest.getAbsolutePath();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void storeFile(InputStream input, File file) {
        try {
            final OutputStream output = new FileOutputStream(file);
            try {
                try {
                    final byte[] buffer = new byte[1024];
                    int read;

                    while ((read = input.read(buffer)) != -1)
                        output.write(buffer, 0, read);

                    output.flush();
                } finally {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
