package net.ddns.andrewnetwork.ludothornsoundbox.utils.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import net.ddns.andrewnetwork.ludothornsoundbox.utils.StringParse;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StringParsingAdapter<V> extends BaseAdapter<V> {

    private final ArrayList<V> itemsAll;
    private final ArrayList<V> suggestions;

    private StringParse<V> parser;
    private int mFieldId;
    private LayoutInflater mDropDownInflater;
    private Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return parser.parseToString( (V) resultValue);
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (V obj : itemsAll) {
                    if(parser.parseToString(obj).toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(obj);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<V> filteredList = (ArrayList<V>) results.values;
            if(results.count > 0) {
                clear();
                for (V v : filteredList) {
                    add(v);
                }
                notifyDataSetChanged();
            }
        }
    };

    public StringParsingAdapter(@NonNull Context context, int resource, @NonNull List<V> objects, StringParse<V> parser) {
        super(context, resource, objects);

        this.parser = parser;
        this.itemsAll =  new ArrayList<>(getItems());
        this.suggestions = new ArrayList<>();
    }

    public StringParsingAdapter(@NonNull Context context, int resource, @NonNull V[] objects, StringParse<V> parser) {
        super(context, resource, objects);

        this.parser = parser;
        this.itemsAll =  new ArrayList<>(getItems());
        this.suggestions = new ArrayList<>();
    }

    @Override
    public @NonNull
    View getView(int position, @Nullable View convertView,
                 @NonNull ViewGroup parent) {
        return createViewFromResource(mInflater, position, convertView, parent, mResource);
    }

    protected  @NonNull
    View createViewFromResource(@NonNull LayoutInflater inflater, int position,
                                @Nullable View convertView, @NonNull ViewGroup parent, int resource) {
        final View view;
        final TextView text;

        if (convertView == null) {
            view = inflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        try {
            if (mFieldId == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = view.findViewById(mFieldId);

                if (text == null) {
                    throw new RuntimeException("Failed to find view with ID "
                            + mContext.getResources().getResourceName(mFieldId)
                            + " in item layout");
                }
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        final V item = getItem(position);
        text.setText(parser.parseToString(item));
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        final LayoutInflater inflater = mDropDownInflater == null ? mInflater : mDropDownInflater;
        return createViewFromResource(inflater, position, convertView, parent, mDropDownResource);
    }
    @Override
    public void setDropDownViewTheme(@Nullable Resources.Theme theme) {
        if (theme == null) {
            mDropDownInflater = null;
        } else if (theme == mInflater.getContext().getTheme()) {
            mDropDownInflater = mInflater;
        } else {
            final Context context = new ContextThemeWrapper(mContext, theme);
            mDropDownInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public @Nullable
    Resources.Theme getDropDownViewTheme() {
        return mDropDownInflater == null ? null : mDropDownInflater.getContext().getTheme();
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }
}
