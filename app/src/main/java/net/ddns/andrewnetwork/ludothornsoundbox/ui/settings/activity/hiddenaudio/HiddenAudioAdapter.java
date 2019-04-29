package net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.hiddenaudio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.model.LudoAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.ItemHiddenAudioBinding;

import java.util.List;

class HiddenAudioAdapter extends RecyclerView.Adapter {

    private final SettingsHiddenAudioActivity mContext;
    private final List<LudoAudio> list;
    private boolean[] selectedArray;
    HiddenAudioAdapter(@NonNull SettingsHiddenAudioActivity context, List<LudoAudio> hiddenAudioList) {
        this.mContext = context;
        this.list = hiddenAudioList;

        selectedArray = new boolean[hiddenAudioList.size()];
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private ItemHiddenAudioBinding mBinding;

        private ViewHolder(View v) {
            super(v);

            mBinding = ItemHiddenAudioBinding.bind(v);
        }

        public void set(LudoAudio audio, int position) {

            mBinding.checkbox.setOnCheckedChangeListener(null);

            if(audio != null) {
                mBinding.titleLabel.setText(audio.getTitle());
                mBinding.orderNumberLabel.setText(mContext.getString(R.string.audio_number_description_label, audio.getOrder()));

                mBinding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    selectedArray[position] = isChecked;

                    mContext.refreshButtonStatus();

                    audio.setHidden(!isChecked);
                });

                mBinding.getRoot().setOnClickListener(v -> mBinding.checkbox.performClick());

            } else {
                mBinding.titleLabel.setText(mContext.getString(R.string.info_not_available_label));
                mBinding.orderNumberLabel.setText(mContext.getString(R.string.info_not_available_label));
                mBinding.checkbox.setEnabled(false);
            }

            mBinding.checkbox.setChecked(selectedArray[position]);


        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_hidden_audio, parent, false);



        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        LudoAudio audio = list.get(position);

        if(viewHolder instanceof ViewHolder) {
            ((ViewHolder) viewHolder).set(audio, position);
        }

        viewHolder.itemView.setTag(audio.getOrder());
}

    @Override
    public int getItemCount() {
        return list.size();
    }

    boolean[] getSelectedArray() {
        return selectedArray;
    }

    void selectAll() {
        for(int i=0;i<selectedArray.length;i++) {
            selectedArray[i] = true;
        }

        for(LudoAudio audio : list) {
            audio.setHidden(false);
        }

        notifyDataSetChanged();
        mContext.refreshButtonStatus();

    }
}
