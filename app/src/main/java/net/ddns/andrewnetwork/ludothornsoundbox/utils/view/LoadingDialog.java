package net.ddns.andrewnetwork.ludothornsoundbox.utils.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.BaseDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class LoadingDialog extends BaseDialog {

    private Context mContext;

    @Override
    public void showLoading() {
        //mBinding.progressDialog.setBackgroundColor(Color.TRANSPARENT);
        getProgressDialog().setVisibility(View.VISIBLE);
        getProgressBackground().setVisibility(View.VISIBLE);
        getProgressDialog().setIndeterminate(true);

    }

    @Override
    public void hideLoading() {
        if (getProgressDialog().isShown()) {
            getProgressDialog().setBackgroundColor(getResources().getColor(R.color.transparent));
            getProgressDialog().setVisibility(View.INVISIBLE);
            getProgressBackground().setVisibility(View.INVISIBLE);
        }
    }

    protected void showDialog(String message, DialogInterface.OnClickListener positiveListener, boolean showCancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", positiveListener);
        if (showCancel)
            builder.setNegativeButton("Annulla", (dialog, id) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) ->
                        dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.setCanceledOnTouchOutside(true);

        return dialog;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.mContext = context;
    }

    @NonNull
    @Override
    public Context getContext() {
        return super.getContext() != null ? super.getContext() : mContext;
    }

    protected abstract ProgressBar getProgressDialog();

    protected abstract View getProgressBackground();
}
