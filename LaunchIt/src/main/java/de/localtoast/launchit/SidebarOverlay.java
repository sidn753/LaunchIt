package de.localtoast.launchit;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by arne on 2/15/14.
 */
public class SidebarOverlay extends Service {
    LinearLayout layout;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        layout = new LinearLayout(this);
        layout.setBackgroundColor(0x88ff0000);
        WindowManager.LayoutParams params =
            new WindowManager.LayoutParams(150, 150, WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH |
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT);
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.addView(layout, params);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (layout != null) {
            WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
            wm.removeView(layout);
        }
    }
}
