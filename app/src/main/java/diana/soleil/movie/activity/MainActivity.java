package diana.soleil.movie.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import diana.soleil.movie.R;
import diana.soleil.movie.adapters.TVShowsAdapter;
import diana.soleil.movie.databinding.ActivityMainBinding;
import diana.soleil.movie.models.TVShow;
import diana.soleil.movie.viewmodels.MostPopularTVShowsViewModel;

import android.os.Bundle;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    private MostPopularTVShowsViewModel viewModel;
    private List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvaiablePages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        doInitialization();
    }

    private  void doInitialization() {
        activityMainBinding.tvShowsRecycleView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        tvShowsAdapter = new TVShowsAdapter(tvShows);
        activityMainBinding.tvShowsRecycleView.setAdapter(tvShowsAdapter);
        activityMainBinding.tvShowsRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activityMainBinding.tvShowsRecycleView.canScrollVertically(1)){
                    if (currentPage<=totalAvaiablePages) {
                        currentPage +=1;
                        getMostPopularTVShows();
                    }
                }
            }
        });
        getMostPopularTVShows();
    }
    public void getMostPopularTVShows(){
        toggleLoading();
        viewModel.getMostPopularTVShows(currentPage).observe(this, mostPopularTVShowsResponse ->
        {
            toggleLoading();
             if (mostPopularTVShowsResponse != null ) {
                 totalAvaiablePages = mostPopularTVShowsResponse.getTotalPages();
                 if (mostPopularTVShowsResponse.getTvShows() != null) {
                     int oldCount = tvShows.size();
                     tvShows.addAll(mostPopularTVShowsResponse.getTvShows());
                     tvShowsAdapter.notifyItemRangeInserted(oldCount,tvShows.size());
                 }
             }
        });
    }

    private void toggleLoading() {
        if (currentPage ==1) {
            activityMainBinding.setIsLoading(activityMainBinding.getIsLoading() == null || !activityMainBinding.getIsLoading());
            activityMainBinding.setIsLoadingMore(activityMainBinding.getIsLoadingMore() == null || !activityMainBinding.getIsLoadingMore());
            }
        }
    }
