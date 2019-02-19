package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.view;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

import net.ddns.andrewnetwork.ludothornsoundbox.utils.StringParse;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ButtonViewPagerAdapter<T> extends PagerAdapter implements Filterable {

    private final Context mContext;
    private final StringParse<T> parser;
    private List<T> itemsAll;
    private List<T> list;
    private int maxItems;
    private final int MINIMUM_NUMBER_PAGE = 1;
    private List<ButtonsView<T>> viewList;
    private OnButtonSelectedListener<T> listener;

    public ButtonViewPagerAdapter(Context context, List<T> list, StringParse<T> parser) {
        super();

        this.mContext = context;
        this.list = list;
        this.itemsAll = new ArrayList<>(list);
        this.parser = parser;
        //make the adapter start
        this.maxItems = MINIMUM_NUMBER_PAGE;
        this.viewList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return maxItems;
    }

    @NonNull
    @Override
    public ButtonsView<T> instantiateItem(@NonNull ViewGroup collection, int position) {
        ButtonsView<T> buttonsView = new ButtonsView<>(mContext);
        buttonsView.setOnViewReadyListener(() -> {
            int numberOfViews = (int) Math.ceil(list.size() * 1.0 / buttonsView.getMaxItems());
            for (int i = 0; i < numberOfViews; i++) {
                if (buttonsView.getMaxItems() * position < Math.min(list.size(), buttonsView.getMaxItems() * (position + 1))) {
                    buttonsView.setAdapter(
                            list.subList(
                                    buttonsView.getMaxItems() * position,
                                    Math.min(list.size(), buttonsView.getMaxItems() * (position + 1))
                            )
                            , parser);

                    if(listener != null) {
                        buttonsView.setOnButtonSelectedListener(listener);
                    }
                }
            }


            //initialize items size once we know maxItems
            if(maxItems != numberOfViews) {
                this.maxItems = numberOfViews;
                notifyDataSetChanged();
            }
        });
        collection.addView(buttonsView);
        viewList.add(buttonsView);
        return buttonsView;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(@NonNull Object object) { return POSITION_NONE; }


    public void setOnButtonSelectedListener(OnButtonSelectedListener<T> listener) {

        this.listener = listener;

        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(@NonNull View container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<T> FilteredArrayNames = new ArrayList<T>();

                // perform your search here using the searchConstraint String.

                constraint = constraint.toString().toLowerCase();
                for (int i = 0; i < itemsAll.size(); i++) {
                    T dataNames = itemsAll.get(i);
                    if (parser.parseToString(dataNames).toLowerCase().startsWith(constraint.toString().toLowerCase()))  {
                        FilteredArrayNames.add(dataNames);
                    }
                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
                Log.e("VALUES", results.values.toString());

                return results;            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (List<T>) results.values;

                notifyDataSetChanged();
            }
        };
    }
}
