package net.ddns.andrewnetwork.ludothornsoundbox.utils.view;

import androidx.annotation.NonNull;

import com.google.android.material.tabs.TabLayout;

import net.ddns.andrewnetwork.ludothornsoundbox.utils.StringParse;

public class TabItem<T> extends TabLayout.Tab {

    T item;
    StringParse<T> parser;

    public TabItem() {

    }
    public TabItem(T item, StringParse<T> stringParser) {
        super();

        setItem(item, stringParser);
    }

    public TabItem<T> setItem(T item, StringParse<T> stringParser) {
        this.item = item;
        this.parser = stringParser;

        setText(stringParser.parseToString(item));

        return this;
    }

    public T getItem() {
        return item;
    }
}
