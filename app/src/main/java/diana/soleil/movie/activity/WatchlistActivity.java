package diana.soleil.movie.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import diana.soleil.movie.R;
import diana.soleil.movie.adapters.WatchlistAdapter;
import diana.soleil.movie.databinding.ActivityWatchlistBinding;
import diana.soleil.movie.listeners.WatchlistListner;
import diana.soleil.movie.models.TVShow;
import diana.soleil.movie.utilities.TempDataHolder;
import diana.soleil.movie.viewmodels.WatchlistViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WatchlistActivity extends AppCompatActivity implements WatchlistListner{

    private ActivityWatchlistBinding activityWatchlistBinding;
    private WatchlistViewModel watchlistViewModel;
    private WatchlistAdapter watchlistAdapter;
    private List<TVShow> watchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);
        activityWatchlistBinding = DataBindingUtil.setContentView(this,R.layout.activity_watchlist);
        doInitialization();
    }
    private void doInitialization() {
        watchlistViewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        activityWatchlistBinding.imageBack.setOnClickListener(v -> onBackPressed());
        watchlist = new ArrayList<>();
        loadWatchlist();
    }
    private void loadWatchlist(){
        activityWatchlistBinding.setIsLoading(true);
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(watchlistViewModel.loadWatchList().subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tvShows -> {
                    activityWatchlistBinding.setIsLoading(false);
                    if (watchlist.size()>0){
                        watchlist.clear();
                    }
                    watchlist.addAll(tvShows);
                    watchlistAdapter = new WatchlistAdapter( watchlist, this);
                    activityWatchlistBinding.watchlistRecycleView.setAdapter(watchlistAdapter);
                    activityWatchlistBinding.watchlistRecycleView.setVisibility(View.VISIBLE);
                    compositeDisposable.dispose();
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TempDataHolder.IS_WATCHLIST_UPDATED) {
            loadWatchlist();
            TempDataHolder.IS_WATCHLIST_UPDATED = false;
        }

    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(), TVShowDetailsActivity.class);
        intent.putExtra("tvShow",tvShow);
        startActivity(intent);
    }

    @Override
    public void removeTVShowFromWatchlist(TVShow tvShow, int position) {
            CompositeDisposable compositeDisposableForDelete = new CompositeDisposable();
            compositeDisposableForDelete.add(watchlistViewModel.removeTVShowFromWatchList(tvShow)
            .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        watchlist.remove(position);
                        watchlistAdapter.notifyItemRemoved(position);
                        watchlistAdapter.notifyItemRangeChanged(position,watchlistAdapter.getItemCount());
                        compositeDisposableForDelete.dispose();
                    }));
    }
}