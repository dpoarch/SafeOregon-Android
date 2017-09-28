package com.safeoregon.app.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.safeoregon.app.R;

public class VideoSliceSeekBar extends ImageView {
    private static final int SELECT_THUMB_LEFT = 1;
    private static final int SELECT_THUMB_RIGHT = 2;
    private static final int SELECT_THUMB_NON = 0;


    //params
    private Bitmap thumbSliceLeft = BitmapFactory.decodeResource(getResources(), R.drawable.sel_left);
    private Bitmap thumbSliceRight = BitmapFactory.decodeResource(getResources(), R.drawable.sel_right);
    private Bitmap thumbCurrentVideoPosition = BitmapFactory.decodeResource(getResources(), R.drawable.leftthumb);
    private int progressMinDiff = 15; //percentage
    private int progressColor = getResources().getColor(android.R.color.darker_gray);
    private int secondaryProgressColor = getResources().getColor(android.R.color.holo_blue_dark);
    private int progressHalfHeight = 3;
    private int thumbPadding = getResources().getDimensionPixelOffset(R.dimen.default_margin);
    private int maxValue = 100;


    private int progressMinDiffPixels;
    private int thumbSliceLeftX, thumbSliceRightX, thumbCurrentVideoPositionX;
    private int thumbSliceLeftValue, thumbSliceRightValue;
    private int thumbSliceY, thumbCurrentVideoPositionY;
    private Paint paint = new Paint();
    private Paint paintThumb = new Paint();
    private int selectedThumb;
    private int thumbSliceHalfWidth, thumbCurrentVideoPositionHalfWidth;
    private SeekBarChangeListener scl;

    private int progressTop;
    private int progressBottom;

    private boolean blocked;
    private boolean isVideoStatusDisplay;

    public VideoSliceSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public VideoSliceSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoSliceSeekBar(Context context) {
        super(context);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        init();
    }

    private void init() {
        if (thumbSliceLeft.getHeight() > getHeight())
            getLayoutParams().height = thumbSliceLeft.getHeight();

        thumbSliceY = (getHeight() / 2) - (thumbSliceLeft.getHeight() / 2);
        thumbCurrentVideoPositionY = (getHeight() / 2) - (thumbCurrentVideoPosition.getHeight() / 2);

        thumbSliceHalfWidth = thumbSliceLeft.getWidth() / 2;
        thumbCurrentVideoPositionHalfWidth = thumbCurrentVideoPosition.getWidth() / 2;
        if (thumbSliceLeftX == 0 || thumbSliceRightX == 0) {
            thumbSliceLeftX = thumbPadding;
            thumbSliceRightX = getWidth() - thumbPadding;
        }
        progressMinDiffPixels = calculateCorrds(progressMinDiff) - 2 * thumbPadding;
        progressTop = getHeight() / 2 - progressHalfHeight;
        progressBottom = getHeight() / 2 + progressHalfHeight;
        invalidate();
    }

    public void setSeekBarChangeListener(SeekBarChangeListener scl) {
        this.scl = scl;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect;
        //generate and draw progress
        paint.setColor(progressColor);
        rect = new Rect(thumbPadding, progressTop, thumbSliceLeftX, progressBottom);
        canvas.drawRect(rect, paint);
        rect = new Rect(thumbSliceRightX, progressTop, getWidth() - thumbPadding, progressBottom);
        canvas.drawRect(rect, paint);

        //generate and draw secondary progress
        paint.setColor(secondaryProgressColor);
        rect = new Rect(thumbSliceLeftX-5, progressTop, thumbSliceRightX+5, progressBottom);
        canvas.drawRect(rect, paint);

        if (!blocked) {
            //generate and draw thumbs pointer
            canvas.drawBitmap(thumbSliceLeft, thumbSliceLeftX - thumbSliceHalfWidth*2, thumbSliceY, paintThumb);
            canvas.drawBitmap(thumbSliceRight, thumbSliceRightX , thumbSliceY, paintThumb);
        }
        if (isVideoStatusDisplay) {
            //generate and draw video thump pointer
            canvas.drawBitmap(thumbCurrentVideoPosition, thumbCurrentVideoPositionX - thumbCurrentVideoPositionHalfWidth,
                    thumbCurrentVideoPositionY, paintThumb);
        }
    }


    public int getLeftThumbXPos()
    {
        return thumbSliceLeftX - thumbSliceHalfWidth*2;
    }

    public int getRightThumbXPos()
    {
        return thumbSliceRightX - thumbSliceHalfWidth*2;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!blocked) {
            int mx = (int) event.getX();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (mx >= thumbSliceLeftX - thumbSliceHalfWidth
                            && mx <= thumbSliceLeftX + thumbSliceHalfWidth || mx < thumbSliceLeftX - thumbSliceHalfWidth) {
                        selectedThumb = SELECT_THUMB_LEFT;
                    } else if (mx >= thumbSliceRightX - thumbSliceHalfWidth
                            && mx <= thumbSliceRightX + thumbSliceHalfWidth || mx > thumbSliceRightX + thumbSliceHalfWidth) {
                        selectedThumb = SELECT_THUMB_RIGHT;
                    } else if (mx - thumbSliceLeftX + thumbSliceHalfWidth < thumbSliceRightX - thumbSliceHalfWidth - mx) {
                        selectedThumb = SELECT_THUMB_LEFT;
                    } else if (mx - thumbSliceLeftX + thumbSliceHalfWidth > thumbSliceRightX - thumbSliceHalfWidth - mx) {
                        selectedThumb = SELECT_THUMB_RIGHT;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if ((mx <= thumbSliceLeftX + thumbSliceHalfWidth + progressMinDiffPixels && selectedThumb == SELECT_THUMB_RIGHT) ||
                            (mx >= thumbSliceRightX - thumbSliceHalfWidth - progressMinDiffPixels && selectedThumb == SELECT_THUMB_LEFT)) {
                        selectedThumb = SELECT_THUMB_NON;
                    }

                    if (selectedThumb == SELECT_THUMB_LEFT) {
                        thumbSliceLeftX = mx;
                    } else if (selectedThumb == SELECT_THUMB_RIGHT) {
                        thumbSliceRightX = mx;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    selectedThumb = SELECT_THUMB_NON;
                    break;
            }
            notifySeekBarValueChanged();
        }
        return true;
    }

    private void notifySeekBarValueChanged() {
        if (thumbSliceLeftX < thumbPadding)
            thumbSliceLeftX = thumbPadding;

        if (thumbSliceRightX < thumbPadding)
            thumbSliceRightX = thumbPadding;

        if (thumbSliceLeftX > getWidth() - thumbPadding)
            thumbSliceLeftX = getWidth() - thumbPadding;

        if (thumbSliceRightX > getWidth() - thumbPadding)
            thumbSliceRightX = getWidth() - thumbPadding;

        invalidate();
        if (scl != null) {
            calculateThumbValue();
            scl.SeekBarValueChanged(thumbSliceLeftValue, thumbSliceRightValue);
        }
    }

    private void calculateThumbValue() {
        thumbSliceLeftValue = (maxValue * (thumbSliceLeftX - thumbPadding)) / (getWidth() - 2 * thumbPadding);
        thumbSliceRightValue = (maxValue * (thumbSliceRightX - thumbPadding)) / (getWidth() - 2 * thumbPadding);
    }


    private int calculateCorrds(int progress) {
        return (int) (((getWidth() - 2d * thumbPadding) / maxValue) * progress) + thumbPadding;
    }

    public void setLeftProgress(int progress) {
        if (progress < thumbSliceRightValue - progressMinDiff) {
            thumbSliceLeftX = calculateCorrds(progress);
        }
        notifySeekBarValueChanged();
    }

    public void setRightProgress(int progress) {
        if (progress > thumbSliceLeftValue + progressMinDiff) {
            thumbSliceRightX = calculateCorrds(progress);
        }
        notifySeekBarValueChanged();
    }

    public int getLeftProgress() {
        return thumbSliceLeftValue;
    }

    public int getRightProgress() {
        return thumbSliceRightValue;
    }

    public void setProgress(int leftProgress, int rightProgress) {
        if (rightProgress - leftProgress > progressMinDiff) {
            thumbSliceLeftX = calculateCorrds(leftProgress);
            thumbSliceRightX = calculateCorrds(rightProgress);
        }
        notifySeekBarValueChanged();
    }

    public void videoPlayingProgress(int progress) {
        isVideoStatusDisplay = true;
        thumbCurrentVideoPositionX = calculateCorrds(progress);
        invalidate();
    }

    public void removeVideoStatusThumb() {
        isVideoStatusDisplay = false;
        invalidate();
    }

    public void setSliceBlocked(boolean isBLock) {
        blocked = isBLock;
        invalidate();
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public void setProgressMinDiff(int progressMinDiff) {
        this.progressMinDiff = progressMinDiff;
        progressMinDiffPixels = calculateCorrds(progressMinDiff);
    }

    public void setProgressHeight(int progressHeight) {
        this.progressHalfHeight = progressHalfHeight / 2;
        invalidate();
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        invalidate();
    }

    public void setSecondaryProgressColor(int secondaryProgressColor) {
        this.secondaryProgressColor = secondaryProgressColor;
        invalidate();
    }

    public void setThumbSliceLeft(Bitmap thumbSliceLeft) {
        this.thumbSliceLeft = thumbSliceLeft;
        init();
    }

    public void setThumbSliceRight(Bitmap thumbSliceRight) {
        this.thumbSliceRight = thumbSliceRight;
        init();
    }


    public void setThumbCurrentVideoPosition(Bitmap thumbCurrentVideoPosition) {
        this.thumbCurrentVideoPosition = thumbCurrentVideoPosition;
        init();
    }

    public void setThumbPadding(int thumbPadding) {
        this.thumbPadding = thumbPadding;
        invalidate();
    }

    public interface SeekBarChangeListener {
        void SeekBarValueChanged(int leftThumb, int rightThumb);
    }
}


