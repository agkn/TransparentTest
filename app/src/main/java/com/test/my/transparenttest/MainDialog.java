package com.test.my.transparenttest;

import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * @author Andrey Kulik (Andrey.Kulik@ascom.com).
 */

public class MainDialog extends DialogFragment implements SurfaceHolder.Callback {

    private Thread mDrawThread;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_main, container);
        view.findViewById(R.id.mainButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        SurfaceView surfaceView = (SurfaceView) view.findViewById(R.id.surfaceView);
        surfaceView.getHolder().addCallback(this);
        getDialog().setTitle("Test dialog.");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics display = getResources().getDisplayMetrics();
        getDialog().getWindow().setLayout((int)(display.widthPixels * 0.9), WindowManager.LayoutParams.MATCH_PARENT);

    }

    @Override
    public void surfaceCreated(final SurfaceHolder holder) {
        mDrawThread = new Thread() {
            @Override
            public void run() {
                Surface surface = holder.getSurface();
                float[] hsv = {0, 1, 1};
                try {
                    while (!isInterrupted()) {
                        int color = Color.HSVToColor(hsv);
                        Canvas canvas = surface.lockCanvas(holder.getSurfaceFrame());
                        canvas.drawRGB(Color.red(color), Color.green(color), Color.blue(color));
                        surface.unlockCanvasAndPost(canvas);
                        sleep(50);
                        hsv[0] += 2;
                        if ( hsv[0] > 360 ) hsv[0] = 0;
                    }
                } catch (InterruptedException ignored) {
                }
            }
        };
        mDrawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mDrawThread != null) {
            mDrawThread.interrupt();
            mDrawThread = null;
        }

    }
}
