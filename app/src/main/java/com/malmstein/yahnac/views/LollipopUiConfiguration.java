package com.malmstein.yahnac.views;

import com.malmstein.yahnac.R;

public enum LollipopUiConfiguration {

    NEWS(R.string.title_launcher, R.mipmap.ic_launcher, R.color.orange, R.color.dark_orange, R.color.black);

    private final int taskTitleResourceId;
    private final int taskLogoResourceId;
    private final int taskColorResourceId;
    private final int statusBarColorResourceId;
    private final int navigationBarColorResourceId;

    LollipopUiConfiguration(int taskTitleResourceId, int taskLogoResourceId, int taskColorResourceId, int statusBarColorResourceId, int navigationBarColorResourceId) {
        this.taskTitleResourceId = taskTitleResourceId;
        this.taskLogoResourceId = taskLogoResourceId;
        this.taskColorResourceId = taskColorResourceId;
        this.statusBarColorResourceId = statusBarColorResourceId;
        this.navigationBarColorResourceId = navigationBarColorResourceId;
    }

    public int getTaskTitleResourceId() {
        return taskTitleResourceId;
    }

    public int getTaskLogoResourceId() {
        return taskLogoResourceId;
    }

    public int getTaskColorResourceId() {
        return taskColorResourceId;
    }

    public int getStatusBarColorResourceId() {
        return statusBarColorResourceId;
    }

    public int getNavigationBarColorResourceId() {
        return navigationBarColorResourceId;
    }

}
