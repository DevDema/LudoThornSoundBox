package net.ddns.andrewnetwork.ludothornsoundbox.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.InterstitialAd;

import net.ddns.andrewnetwork.ludothornsoundbox.R;
import net.ddns.andrewnetwork.ludothornsoundbox.controller.FavoriteController;
import net.ddns.andrewnetwork.ludothornsoundbox.model.FavoriteAudio;
import net.ddns.andrewnetwork.ludothornsoundbox.model.User;
import net.ddns.andrewnetwork.ludothornsoundbox.view.BaseActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class HomeFragment extends GifFragment {

    TableLayout tl;
    int pag = 0;
    ImageView left;
    ImageView right;
    ImageView reverse;
    List<FavoriteAudio> filteredAudioList = new ArrayList<>();
    Typeface typeFace;
    Spinner spinner;
    int rows;
    TextView counter;
    int columns;
    int width;
    LinearLayout controls;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);

        filteredAudioList = favoriteAudioList;
        return inflater.inflate(R.layout.scroll_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        spinner = view.findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                sortBy(favoriteAudioList, pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.order_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        reverse = view.findViewById(R.id.reverse);
        reverse.setOnClickListener(view2 -> {
                    Collections.reverse(favoriteAudioList);
                    createTableLayout();
                }
        );
        controls = view.findViewById(R.id.controls);
        tl = view.findViewById(R.id.table);
        rows = tl.getChildCount();
        left = view.findViewById(R.id.left);
        right = view.findViewById(R.id.right);
        sortBy(favoriteAudioList, 0);
        counter = view.findViewById(R.id.counter);
        left.setVisibility(View.INVISIBLE);
        right.setOnClickListener(view2 -> {
            pag++;
            refreshLayout();
        });
        left.setOnClickListener(view2 -> {
            pag--;
            refreshLayout();
        });
        reverse = view.findViewById(R.id.reverse);
        reverse.setOnClickListener(view2 -> {
                    Collections.reverse(favoriteAudioList);
                    createTableLayout();
                }
        );
        controls = view.findViewById(R.id.controls);
        tl = view.findViewById(R.id.table);
        rows = tl.getChildCount();
        left = view.findViewById(R.id.left);
        right = view.findViewById(R.id.right);
        left.setMaxWidth(width / columns);
        right.setMaxWidth(width / columns);
        counter = view.findViewById(R.id.counter);
        left.setVisibility(View.INVISIBLE);
        right.setOnClickListener(view2 -> {
            pag++;
            refreshLayout();
        });
        left.setOnClickListener(view2 -> {
            pag--;
            refreshLayout();
        });
        adjustPages();
        searchstring.addTextChangedListener(new TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                                    if (!searchstring.getText().toString().matches("")) {
                                                        filteredAudioList = filterAudio(charSequence.toString().toLowerCase());
                                                        refreshLayout();;

                                                    }
                                                }

                                                @Override
                                                public void afterTextChanged(Editable editable) {
                                                    if (!searchstring.getText().toString().matches("")) {
                                                        String countertext;
                                                        if (filteredAudioList.size() <= rows * columns)
                                                            countertext = filteredAudioList.size() + "/" + filteredAudioList.size();
                                                        else countertext = rows * columns * (pag + 1) + "/" + filteredAudioList.size();
                                                        controls.setVisibility(View.GONE);
                                                        counter.setGravity(Gravity.CENTER_HORIZONTAL);

                                                    } else {
                                                        controls.setVisibility(View.VISIBLE);

                                                        filteredAudioList = favoriteAudioList;
                                                        refreshLayout();

                                                    }
                                                }
                                            }
        );
    }



        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            if(isHorizontal()) columns = 3;
            else columns = 2;
            if(isTablet()) columns++;
            typeFace = Typeface.createFromAsset(this.getActivity().getAssets(), "font/knewave.ttf");
            width=bundle.getInt("width");
    }




    private void clearTableLayout() {
        TableRow tr;
        for (int currentRow = 0; currentRow < rows; currentRow++) {
            tr = (TableRow) tl.getChildAt(currentRow);
            for (int currentColumn = 0; currentColumn < columns; currentColumn++) {
                Button element = (Button) tr.getChildAt(currentColumn);
                if (element != null) element.setVisibility(View.GONE);
            }
        }
    }

    private void createTableLayout() {

        clearTableLayout();
        int currentColumn = 0;
        int currentRow = 0;
        ListIterator<FavoriteAudio> it = null;
        int iterator = rows * columns * pag;
        if (iterator > filteredAudioList.size()) {
            pag = 0;
            iterator = 0;
        }
        if (filteredAudioList.size() > 0)
            it = filteredAudioList.listIterator(iterator);

        if (it != null)
            while (it.hasNext()) {
                FavoriteAudio favaudio = it.next();
                if (currentColumn > columns - 1) {
                    currentColumn = 0;
                    currentRow++;
                }
                if (currentRow > rows - 1 && currentRow > columns - 1) break;

                TableRow tr = (TableRow) tl.getChildAt(currentRow);
                Button button = (Button) tr.getChildAt(currentColumn);
                button.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  InterstitialAd interstitialAd = ((BaseActivity) HomeFragment.this.getActivity()).getInterstitialAd();
                                                  if (!User.loadAds(interstitialAd, mediaPlayer)) {
                                                      if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                                                          mediaPlayer.stop();
                                                          mediaPlayer.reset();
                                                      }
                                                      int audionumber = favaudio.getAudio();
                                                      mediaPlayer = MediaPlayer.create(HomeFragment.this.getActivity(), audionumber);
                                                      HomeFragment.this.mediaPlayer.setOnCompletionListener(completionListener);
                                                      play_pause.setImageResource(R.drawable.ic_action_pause);
                                                      HomeFragment.this.mediaPlayer.start();

                                                  }
                                              }
                                          }
                );
                button.setOnLongClickListener(view -> {
                    Context context = this.getActivity().getApplicationContext();
                    try {
                        /* PREMIUM USER ***********************/

                        if (!User.isPremiumUser() && FavoriteController.loadFavorite(context).size() == User.getFavlimit()) {
                            Toast.makeText(context, "Hai raggiunto il massimo dei preferiti!", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        if (!FavoriteController.alreadyExists(context, favaudio))
                            if (FavoriteController.saveFavorite(context, favaudio)) {
                                Toast.makeText(context, '"' + favaudio.getTitle() + '"' + " aggiunto ai preferiti!", Toast.LENGTH_SHORT).show();
                                return true;
                            } else {
                                Toast.makeText(context, "Errore nell'aggiunta dei preferiti!", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        else {
                            Toast.makeText(context, "La nota '" + favaudio.getTitle() + "' esiste già!", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return false;
                    }
                });
                button.setWidth(width / columns);
                button.setMaxLines(2);
                button.setText(favaudio.getTitle());
                button.setTypeface(typeFace);
                button.setVisibility(View.VISIBLE);
                button.getBackground().setColorFilter(ContextCompat.getColor(this.getActivity(), R.color.white), PorterDuff.Mode.MULTIPLY);
                tr.setGravity(Gravity.CENTER_VERTICAL);
                currentColumn++;
            }

    }

    private void adjustPages() {

        int pagsize = filteredAudioList.size() / (rows * columns);
        String text = rows * columns * (pag + 1) + "/" + filteredAudioList.size();
        if (pagsize <= 0 || filteredAudioList.size() <= rows * columns) {
            left.setVisibility(View.GONE);
            right.setVisibility(View.GONE);
        } else {
            if (pag == pagsize) {
                text = filteredAudioList.size() + "/" + filteredAudioList.size();
                left.setVisibility(View.VISIBLE);
                right.setVisibility(View.INVISIBLE);
            } else if (pag == 0) {
                left.setVisibility(View.INVISIBLE);
                right.setVisibility(View.VISIBLE);

            } else {
                left.setVisibility(View.VISIBLE);
                right.setVisibility(View.VISIBLE);
            }
        }
        if(counter != null)
        counter.setText(text);
    }

    private void sortBy(List arrayList, int criterion) {
        switch (criterion) {
            case 0:
                Collections.sort(arrayList, FavoriteAudio.COMPARE_BY_NAME);
                break;
            case 1:
                Collections.sort(arrayList, FavoriteAudio.COMPARE_BY_DATE);
                break;
            default:
                break;
        }
        createTableLayout();
        adjustPages();
    }

    private List<FavoriteAudio> filterAudio(CharSequence filter) {
        List<FavoriteAudio> filteredList = new ArrayList<>();
        for(FavoriteAudio audio : favoriteAudioList)
            if(audio.getTitle().contains(filter))
                filteredList.add(audio);
        return filteredList;
    }

    private void refreshLayout() {
        createTableLayout();
        adjustPages();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
       width=getActivityWidth();
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columns++;
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            columns--;
        }
        refreshLayout();
    }

}
