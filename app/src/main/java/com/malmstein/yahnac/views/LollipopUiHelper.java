package com.malmstein.yahnac.views;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import static android.app.ActivityManager.TaskDescription;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class LollipopUiHelper {

    private final Activity activity;
    private final ColorTweaker colorTweaker;
    private final LollipopUiConfiguration configuration;

    public LollipopUiHelper(Activity activity, ColorTweaker colorTweaker, LollipopUiConfiguration configuration) {
        this.activity = activity;
        this.colorTweaker = colorTweaker;
        this.configuration = configuration;
    }

    private static boolean isAtLeastLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setTaskDescriptionOnLollipopAndLater() {
        if (isAtLeastLollipop()) {
            TaskDescription taskDescription = createTaskDescription();
            activity.setTaskDescription(taskDescription);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected TaskDescription createTaskDescription() {
        Resources resources = activity.getResources();
        String taskTitle = resources.getString(configuration.getTaskTitleResourceId());
        Bitmap logo = BitmapFactory.decodeResource(resources, configuration.getTaskLogoResourceId());
        int taskColor = resources.getColor(configuration.getTaskColorResourceId());
        return new TaskDescription(taskTitle, logo, taskColor);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setSystemBarsColorOnLollipopAndLater() {
        if (isAtLeastLollipop()) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            Resources resources = activity.getResources();
            int statusBarColor = resources.getColor(configuration.getStatusBarColorResourceId());
            int navigationBarColor = resources.getColor(configuration.getNavigationBarColorResourceId());
            window.setStatusBarColor(statusBarColor);
            window.setNavigationBarColor(navigationBarColor);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setStatusBarColorMaybe(int color) {
        if (isAtLeastLollipop()) {
            int statusBarColor = colorTweaker.getStatusBarVariantOf(color);
            activity.getWindow().setStatusBarColor(statusBarColor);
        }
    }

}
