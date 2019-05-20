package net.ddns.andrewnetwork.ludothornsoundbox.ui.main.fragments.home.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.data.AppDataManager_Factory;
import net.ddns.andrewnetwork.ludothornsoundbox.utils.StringParse;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.zip.CheckedOutputStream;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

@SuppressWarnings("unchecked")
public class ButtonViewPagerAdapter<T> extends PagerAdapter implements Filterable {

    private final Context mContext;
    private final StringParse<T> parser;
    private List<List<T>> itemsAll;
    private List<List<T>> list;
    private OnButtonSelectedListener<T> listener;
    private int maxItemsPerPage;
    private FilterByListener<T> otherFilters;
    private ViewGroup collection;
    private ButtonsView[] viewList;
    private FilteredResultsListener<T> filteredResultsListener;

   public interface FilteredResultsListener<T> {

       void onFilteredResults(List<T> list);
   }

    public interface FilterByListener<T> {
        boolean filterBy(T object);
    }

    public interface ChangingListener<T> {

        void change(List<T> list);
    }

    public ButtonViewPagerAdapter(Context context, List<T> list, StringParse<T> parser, ViewPager viewPager) {
        super();

        this.mContext = context;
        this.collection = viewPager;
        this.list = groupByPage(list);
        this.itemsAll = new ArrayList<>(this.list);
        this.viewList = new ButtonsView[this.list.size()];
        this.parser = parser;
        this.otherFilters = audio -> true;

    }

    public ButtonViewPagerAdapter(Context context, List<T> list, StringParse<T> parser, ViewPager viewPager, FilterByListener<T> filterBy) {
        this(context, list, parser, viewPager);

        this.otherFilters = filterBy;
    }

    private List<List<T>> groupByPage(List<T> list) {

        computeMaxItemsPerPage();
        List<List<T>> separatedList = new ArrayList<>();

        for (int i = 0; i < Math.ceil(list.size() * 1.0 / maxItemsPerPage); i++) {

            separatedList.add(list.subList(maxItemsPerPage * i,
                    Math.min(list.size(), maxItemsPerPage * (i + 1))));
        }

        return separatedList;
    }

    private List<T> ungroupList(List<List<T>> list) {
        List<T> ungroupedList = new ArrayList<>();

        for (List<T> subList : list) {
            ungroupedList.addAll(subList);
        }

        return ungroupedList;
    }

    private void computeMaxItemsPerPage() {
        int buttonWidth = ButtonsView.getWidthFromPreferences(mContext);
        int buttonHeight = (int) mContext.getResources().getDimension(R.dimen.input_size_xxxs);

        final int MAX_COLUMNS = (int) Math.floor(collection.getWidth() * 1.0 / buttonWidth);
        final int MAX_ROWS = (int) Math.floor(collection.getHeight() * 1.0 / buttonHeight);
        maxItemsPerPage = MAX_COLUMNS * MAX_ROWS;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    public List<List<T>> getItemsAll() {
        return itemsAll;
    }

    public List<List<T>> getList() {
        return list;
    }

    public void changeItemList(@NonNull ChangingListener<T> changingListener) {
        List<T> list = ungroupList(getList());

        changingListener.change(list);

        this.list = groupByPage(list);

    }

    public void changeItemsAll(@NonNull ChangingListener<T> changingListener) {
        List<T> itemsAll = ungroupList(getItemsAll());

        changingListener.change(itemsAll);

        this.itemsAll = groupByPage(itemsAll);
    }

    @NonNull
    @Override
    public ButtonsView<T> instantiateItem(@NonNull ViewGroup collection, int position) {
        this.collection = collection;

        ButtonsView<T> buttonsView = getViewSingleton(collection, position);


        return buttonsView;
    }

    private void configureButtonsView(ButtonsView<T> buttonsView, int position) {


        buttonsView.setAdapter(
                list.get(position)
                , parser);

        if (listener != null) {
            buttonsView.setOnButtonSelectedListener(listener);
        }

    }

    private static void  addViewToCollection(ViewGroup collection, ButtonsView buttonsView) {
        if (buttonsView.getParent() == null) {
            collection.removeView(buttonsView);
            collection.addView(buttonsView);
        }
    }

    private ButtonsView<T> getViewSingleton( ViewGroup collection, int position) {
        ButtonsView<T> buttonsView;
        if (viewList[position] == null) {
            buttonsView = new ButtonsView<>(mContext);
            buttonsView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int width  = buttonsView.getMeasuredWidth();
                    int height = buttonsView.getMeasuredHeight();
                    if(width != 0 || height != 0) {
                        buttonsView.inflateButtons(mContext, 5);

                        configureButtonsView(buttonsView, position);

                        buttonsView.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                    }
                }
            });
            viewList[position] = buttonsView;

            addViewToCollection(collection, buttonsView);

        } else {
            buttonsView = viewList[position];

            configureButtonsView(buttonsView, position);

            addViewToCollection(collection, buttonsView);
        }

        return buttonsView;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }


    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }


    public void setOnButtonSelectedListener(OnButtonSelectedListener<T> listener) {

        this.listener = listener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<T> FilteredArrayNames = new ArrayList<T>();
                // perform your search here using the searchConstraint String.
                List<T> ungroupedList = ungroupList(itemsAll);
                if (constraint != null) {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < ungroupedList.size(); i++) {
                        T dataNames = ungroupedList.get(i);
                        if (parser.parseToString(dataNames).toLowerCase().startsWith(constraint.toString().toLowerCase()) && otherFilters.filterBy(dataNames)) {
                            FilteredArrayNames.add(dataNames);
                        }
                    }
                } else {
                    for (int i = 0; i < ungroupedList.size(); i++) {
                        T dataNames = ungroupedList.get(i);
                        if (otherFilters.filterBy(dataNames)) {
                            FilteredArrayNames.add(dataNames);
                        }
                    }
                }

                results.count = FilteredArrayNames.size();
                results.values = FilteredArrayNames;
                Log.v("VALUES", results.values.toString());

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = groupByPage((List<T>) results.values);

                notifyDataSetChanged();

                if(filteredResultsListener != null) {
                    filteredResultsListener.onFilteredResults(ungroupList(list));
                }
            }
        };
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }

    public List<T> getUngroupedItems() {
        return ungroupList(list);
    }

    public void setOnFilteredResults(FilteredResultsListener<T> filteredResultsListener) {
       this.filteredResultsListener = filteredResultsListener;
    }
}
