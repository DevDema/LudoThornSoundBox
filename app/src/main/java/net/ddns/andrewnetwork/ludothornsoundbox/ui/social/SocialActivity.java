package net.ddns.andrewnetwork.ludothornsoundbox.ui.social;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.google.gson.reflect.TypeToken;

import net.ddns.andrewnetwork.ludothornsoundbox.BuildConfig;
import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.AltriSocial;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.Social;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.ActivitySocialBinding;
import net.ddns.andrewnetwork.ludothornsoundbox.ui.base.PreferencesManagerActivity;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.JsonUtil;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.view.SocialItem;

import java.util.List;

public class SocialActivity extends PreferencesManagerActivity {

    public static final int REQUEST_SOCIAL = 4044;
    private ActivitySocialBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            mIsHomeButtonEnabled = true;
            mIsDisplayHomeAsUpEnabled = true;
            getSupportActionBar().setHomeButtonEnabled(mIsHomeButtonEnabled);
            getSupportActionBar().setDisplayHomeAsUpEnabled(mIsDisplayHomeAsUpEnabled);
        }

        List<Social> socialList = JsonUtil.getGson().fromJson(BuildConfig.SOCIAL, new TypeToken<List<Social>>(){}.getType());
        List<AltriSocial> altriSocialList = JsonUtil.getGson().fromJson(BuildConfig.ALTRI_SOCIAL, new TypeToken<List<AltriSocial>>(){}.getType());

        for(int i=0; i<socialList.size();i++) {
            Social social = socialList.get(i);
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if(i>0) {
                marginLayoutParams.topMargin = 40;
            }
            SocialItem socialItem = new SocialItem(this);

            socialItem.setSocial(social);

            socialItem.setLayoutParams(marginLayoutParams);

            mBinding.socialLayout.addView(socialItem);
        }

        for(int i=0; i<altriSocialList.size();i++) {
            AltriSocial social = altriSocialList.get(i);
            ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if(i>0) {
                marginLayoutParams.topMargin = 40;
            }
            SocialItem socialItem = new SocialItem(this);

            socialItem.setSocial(social);

            socialItem.setLayoutParams(marginLayoutParams);

            mBinding.altriSocialLayout.addView(socialItem);
        }

        mBinding.altriSocialLabel.setText(getString(R.string.altra_roba_label, BuildConfig.SHORT_NAME));
    }

    @Override
    protected void setContentView() {
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_social);
    }

    @Override
    protected int getFragmentContainerView() {
        return 0;
    }
}
