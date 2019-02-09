package net.ddns.andrewnetwork.ludothornsoundbox.utils.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

public class BaseAdapter<T> extends ArrayAdapter<T> implements ParentAdapter<T> {

    List<T> list;
    protected Context mContext;
    LayoutInflater mInflater;
    int mDropDownResource;
    int mResource;

    public BaseAdapter(@NonNull Context context, int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        list = new ArrayList<>(objects);
        mResource = mDropDownResource = resource;

    }

    public BaseAdapter(@NonNull Context context, int resource, @NonNull T[] objects) {
        super(context, resource, objects);

        list = Arrays.asList(objects);
    }

    @Override
    public List<T> getItems() {
        return list;
    }

}
