package diana.soleil.movie.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import diana.soleil.movie.R;
import diana.soleil.movie.adapters.TVShowsAdapter;
import diana.soleil.movie.databinding.ActivitySearchBinding;
import diana.soleil.movie.listeners.TVShowsListener;
import diana.soleil.movie.models.TVShow;
import diana.soleil.movie.viewmodels.SearchViewModel;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity implements TVShowsListener {

    private ActivitySearchBinding activitySearchBinding;
    private SearchViewModel searchViewModel;
    private List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        doInitialzation();
    }

    public void doInitialzation () {
        activitySearchBinding.imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        activitySearchBinding.searchRecycleView.setHasFixedSize(true);
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        tvShowsAdapter = new TVShowsAdapter(tvShows,this);
        activitySearchBinding.searchRecycleView.setAdapter(tvShowsAdapter);
        activitySearchBinding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null ) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    currentPage = 1 ;
                                    totalAvailablePages = 1;
                                    searchTVShow(s.toString());
                                }
                            });
                        }
                    },800);
                } else {
                    tvShows.clear();
                    tvShowsAdapter.notifyDataSetChanged();
                }
            }
        });
        activitySearchBinding.searchRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activitySearchBinding.searchRecycleView.canScrollVertically(1)) {
                    if (!activitySearchBinding.inputSearch.getText().toString().isEmpty()) {
                        if (currentPage < totalAvailablePages) {
                            currentPage += 1;
                            searchTVShow(activitySearchBinding.inputSearch.getText().toString());
                        }
                    }
                }
            }
        });
        activitySearchBinding.inputSearch.requestFocus();
    }
    private void searchTVShow(String query) {
        toggleLoading();
        searchViewModel.searchTVShow(query,currentPage).observe(this,tvShowsResponse -> {
            toggleLoading();
            if (tvShowsResponse != null) {
                totalAvailablePages = tvShowsResponse.getTotalPages();
                if (tvShowsResponse.getTvShows() != null) {
                    int oldCount = tvShows.size();
                    tvShows.addAll(tvShowsResponse.getTvShows());
                    tvShowsAdapter.notifyItemRangeInserted(oldCount, tvShows.size());
                }
            }
        });
    }
    private void toggleLoading() {

        if (currentPage == 1) {
            if (activitySearchBinding.getIsLoading() != null && activitySearchBinding.getIsLoading()) {
                activitySearchBinding.setIsLoading(false);
            } else {
                activitySearchBinding.setIsLoading(true);
            }
        } else {
            if (activitySearchBinding.getIsLoadingMore() != null && activitySearchBinding.getIsLoadingMore()) {
                activitySearchBinding.setIsLoadingMore(false);
            } else {
                activitySearchBinding.setIsLoadingMore(true);
            }

        }
        /*if (currentPage ==1) {
            activityMainBinding.setIsLoading(activityMainBinding.getIsLoading() == null || !activityMainBinding.getIsLoading());
            activityMainBinding.setIsLoadingMore(activityMainBinding.getIsLoadingMore() == null || !activityMainBinding.getIsLoadingMore());
            }*/
    }
    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowDetailsActivity.class);
        intent.putExtra("tvShow", tvShow);
        startActivity(intent);
    }
}