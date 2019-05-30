package net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.credits;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import net.ddns.andrewnetwork.ludothornsoundbox.BuildConfig;
import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.ActivityCreditsBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.PreferencesManagerActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.AppUtils;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.StringUtils;

import static net.ddns.andrewnetwork.ludothornsoundbox.utils.StringUtils.nonEmptyNonNull;

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
        mBinding.mailLudoName.setText(BuildConfig.LONG_NAME);

        if(nonEmptyNonNull( BuildConfig.MAIL_PERSONALE)) {
            mBinding.mailLudoLabel.setText(Html.fromHtml("Mail: <a href=\"mailto:" + BuildConfig.MAIL_PERSONALE + "\">" + BuildConfig.MAIL_PERSONALE + "</a>"));
        }

        mBinding.appVersionLabel.setText(String.format("%s: Versione %s", getString(R.string.app_name), BuildConfig.VERSION_NAME));

        mBinding.mailSegnalazioniLabel.setMovementMethod(LinkMovementMethod.getInstance());

        if (nonEmptyNonNull(BuildConfig.MAIL_SEGNALAZIONI)) {
            mBinding.mailSegnalazioniLabel.setText(Html.fromHtml("<a href=\"mailto:" + BuildConfig.MAIL_SEGNALAZIONI + "\">" + BuildConfig.MAIL_SEGNALAZIONI + "</a>"));
        } else {
            mBinding.mailSegnalazioneName.setText(null);
            mBinding.mailSegnalazioniLabel.setText(null);

            mBinding.mailSegnalazioniLabel.setVisibility(View.INVISIBLE);
            mBinding.mailSegnalazioneName.setVisibility(View.INVISIBLE);
        }
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
