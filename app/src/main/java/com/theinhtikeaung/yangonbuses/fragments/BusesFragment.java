package com.theinhtikeaung.yangonbuses.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.quinny898.library.persistentsearch.SearchBox;
import com.theinhtikeaung.yangonbuses.R;
import com.theinhtikeaung.yangonbuses.adapters.BusAdapter;
import com.theinhtikeaung.yangonbuses.adapters.BusLinesAdapter;
import com.theinhtikeaung.yangonbuses.api.YangonBusesService;
import com.theinhtikeaung.yangonbuses.constants.Api;
import com.theinhtikeaung.yangonbuses.constants.Application;
import com.theinhtikeaung.yangonbuses.factory.ServiceFactory;
import com.theinhtikeaung.yangonbuses.models.Bus;
import com.theinhtikeaung.yangonbuses.models.buses.BusLine;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusesFragment extends SuperRootFragment{

    private View v;
    private BusLinesAdapter adapter;
    private BusAdapter busAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Bus> buses;
    private SearchBox searchBox;
    private Toolbar toolbar;
    private LinearLayout LLSearch;
    private Snackbar snackbar;
    private CoordinatorLayout coordinatorLayout;

    private com.theinhtikeaung.yangonbuses.models.buses.Bus myBus;
    private ArrayList<BusLine> busLines;

    private static final int API_GET_BUSLINES = 9001;

    private static final String TAG = "BusesFragment";

    public BusesFragment() {
        // Required empty public constructor
    }

    private void initUI() {

        coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.coordinatorLayout);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        searchBox = (SearchBox) v.findViewById(R.id.searchbox);
        LLSearch = (LinearLayout) v.findViewById(R.id.LLSearch);

        snackbar = Snackbar.make(coordinatorLayout, "Loading....", Snackbar.LENGTH_LONG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_buses, container, false);
        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI();

        searchBox.getSearch().setHint("Search Bus Lines....");
        openSearch();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


        getBusLines();

    }


    public void openSearch() {
        try {
            searchBox.setSearchString("");
            searchBox.revealFromMenuItem(R.id.LLSearch, getActivity());
            searchBox.setSearchListener(new SearchBox.SearchListener() {
                @Override
                public void onSearchOpened() {

                }

                @Override
                public void onSearchCleared() {

                }

                @Override
                public void onSearchClosed() {

                }

                @Override
                public void onSearchTermChanged(String term) {

                }

                @Override
                public void onSearch(String result) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    View.OnClickListener clickListenerToPreviousScreen = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };


    private void getBusLines() {

        YangonBusesService service = ServiceFactory.createRetrofitService(YangonBusesService.class, Application.BASE_URL);
        service.getBusline()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<com.theinhtikeaung.yangonbuses.models.buses.Bus>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(com.theinhtikeaung.yangonbuses.models.buses.Bus bus) {
                        myBus = bus;
                        busLines = myBus.getData().getBuslines();
                        busAdapter = new BusAdapter(getActivity(), busLines);
                        recyclerView.setAdapter(busAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
