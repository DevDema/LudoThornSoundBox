package net.ddns.andrewnetwork.ludothornsoundbox.ui.settings.activity.navigationItems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.databinding.ItemNavigationItemBinding;

import java.util.Collections;
import java.util.List;

class NavigationItemsAdapter extends RecyclerView.Adapter {

    private final SettingsNavigationItemsActivity mContext;
    private final List<LudoNavigationItem> list;
    private boolean[] checkedArray;
    private int currentPosition;
    private int firstPosition;

    NavigationItemsAdapter(@NonNull SettingsNavigationItemsActivity context, List<LudoNavigationItem> ludoNavigationItemList, int currentPosition, int firstPosition) {
        this.mContext = context;
        this.list = ludoNavigationItemList;
        this.checkedArray = new boolean[ludoNavigationItemList.size()];
        this.currentPosition = currentPosition;
        this.firstPosition = firstPosition;

        for (int i = 0; i < ludoNavigationItemList.size(); i++) {
            checkedArray[i] = ludoNavigationItemList.get(i).getVisible();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        private ItemNavigationItemBinding mBinding;


        private ViewHolder(View v) {
            super(v);

            mBinding = ItemNavigationItemBinding.bind(v);
        }

        public void set(LudoNavigationItem navigationItem, int position) {
            mBinding.checkbox.setOnCheckedChangeListener(null);

            mBinding.imageBlock.setImageResource(navigationItem.getDrawableId());
            mBinding.titleLabel.setText(navigationItem.getName());

            mBinding.checkbox.setChecked(checkedArray[position]);

            mBinding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                checkedArray[position] = isChecked;

                navigationItem.setVisible(isChecked);

                changeVisibleBackground(position);
            });

            mBinding.arrowUp.setOnClickListener(v -> swapBackWard(position));

            mBinding.arrowDown.setOnClickListener(v -> swapForward(position));

            changeArrowsByPosition(position);

            changeVisibleBackground(position);

            if (navigationItem.getId() == currentPosition || navigationItem.getId() == firstPosition) {
                mBinding.checkbox.setEnabled(false);
                mBinding.errorLabel.setVisibility(View.VISIBLE);
                if (navigationItem.getId() == currentPosition) {
                    mBinding.errorLabel.setText(mContext.getString(R.string.error_current_position));
                } else if (navigationItem.getId() == firstPosition) {
                    mBinding.errorLabel.setText(mContext.getString(R.string.error_first_position));
                }
            } else {
                mBinding.errorLabel.setVisibility(View.GONE);
            }
        }

        private void changeArrowsByPosition(int position) {
            if (position == list.size() - 1) {
                mBinding.arrowDown.setVisibility(View.INVISIBLE);
            } else {
                mBinding.arrowDown.setVisibility(View.VISIBLE);
            }

            if (position == 0) {
                mBinding.arrowUp.setVisibility(View.INVISIBLE);
            } else {
                mBinding.arrowUp.setVisibility(View.VISIBLE);
            }
        }

        private void changeVisibleBackground(int position) {
            if (checkedArray[position]) {
                mBinding.arrowDown.setVisibility(View.VISIBLE);
                mBinding.arrowUp.setVisibility(View.VISIBLE);

                mBinding.getRoot().setBackgroundColor(mContext.getResources().getColor(R.color.transparent));

                changeArrowsByPosition(position);
            } else {
                mBinding.arrowDown.setVisibility(View.INVISIBLE);
                mBinding.arrowUp.setVisibility(View.INVISIBLE);

                mBinding.getRoot().setBackgroundColor(mContext.getResources().getColor(R.color.gray));
            }
        }
    }

    private void swapForward(int position) {
        Collections.swap(list, position, position + 1);

        boolean temp = checkedArray[position];
        checkedArray[position] = checkedArray[position + 1];
        checkedArray[position + 1] = temp;

        notifyItemMoved(position, position + 1);

        notifyItemChanged(position);

        notifyItemChanged(position + 1);
    }

    private void swapBackWard(int position) {
        Collections.swap(list, position - 1, position);

        boolean temp = checkedArray[position - 1];
        checkedArray[position - 1] = checkedArray[position];
        checkedArray[position] = temp;

        notifyItemMoved(position - 1, position);

        notifyItemChanged(position - 1);

        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_navigation_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        LudoNavigationItem navigationItem = list.get(position);

        if (viewHolder instanceof ViewHolder) {
            ((ViewHolder) viewHolder).set(navigationItem, position);
        }

        viewHolder.itemView.setTag(navigationItem.getDrawableId());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
