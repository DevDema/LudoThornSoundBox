package net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.credits;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;

import androidx.databinding.DataBindingUtil;

import net.ddns.andrewnetwork.ludothornsoundbox.BuildConfig;
import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.ActivityCreditsBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.PreferencesManagerActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AppUtils;

public class CreditsActivity extends PreferencesManagerActivity {

    public static final int REQUEST_CREDITS = 2023;
    ActivityCreditsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            mIsHomeButtonEnabled = true;
            mIsDisplayHomeAsUpEnabled = true;
            getSupportActionBar().setHomeButtonEnabled(mIsHomeButtonEnabled);
            getSupportActionBar().setDisplayHomeAsUpEnabled(mIsDisplayHomeAsUpEnabled);
        }

        mBinding.mailAndreaLabel.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.mailAndreaLabel.setText(Html.fromHtml("Mail: <a href=\"mailto:" + AppUtils.MAIL_ANDREA + "\">" + AppUtils.MAIL_ANDREA + "</a>"));

        mBinding.mailLudoLabel.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.mailLudoLabel.setText(Html.fromHtml("Mail: <a href=\"mailto:" + AppUtils.MAIL_LUDO + "\">" + AppUtils.MAIL_LUDO + "</a>"));

        mBinding.appVersionLabel.setText(String.format("%s: Versione %s", getString(R.string.app_name), BuildConfig.VERSION_NAME));

        mBinding.mailSegnalazioniLabel.setMovementMethod(LinkMovementMethod.getInstance());
        mBinding.mailSegnalazioniLabel.setText(Html.fromHtml("<a href=\"mailto:" + AppUtils.MAIL_SEGNALAZIONI + "\">" + AppUtils.MAIL_SEGNALAZIONI + "</a>"));
    }

    @Override
    protected void setContentView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_credits);
    }

    @Override
    protected int getFragmentContainerView() {
        return 0;
    }
}
