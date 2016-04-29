package com.inetcar.tools;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.inetcar.startup.R;

public class LoadingDialog extends ProgressDialog{

    public LoadingDialog(Context context) {
        super(context);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingdata);
    }
}
