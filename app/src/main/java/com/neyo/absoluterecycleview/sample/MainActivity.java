package com.neyo.absoluterecycleview.sample;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.neyo.absoluterecycleview.R;
import com.neyo.absoluterecycleview.AbsoluteRecyclerOnScrollListener;
import com.neyo.absoluterecycleview.sample.common.CustomLoadingDialog;
import com.neyo.absoluterecycleview.sample.model.Product;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neyo on 2017/12/19.
 */

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    @BindView(R.id.recylerView)
    RecyclerView mRecylerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    protected static final int INIT = 1;
    protected static final int REFRESH = 2;
    private static final int LOAD_MORE = 3;
    private MainAdapter mMainAdapter;
    private AbsoluteRecyclerOnScrollListener mRecyclerOnScrollListener;
    private CustomLoadingDialog mCustomLoadingDialog;
    private int mStatus = 0;
    private int currentPage = 0;
    private int oldPage = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        getData(INIT);
    }

    private void initView() {
        mCustomLoadingDialog = CustomLoadingDialog.setDialog(this, getString(R.string.progress_loading), true, null);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.google_blue,
                R.color.google_green,
                R.color.google_red,
                R.color.google_yellow
        );
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecylerView.setHasFixedSize(true);
        mRecylerView.setLayoutManager(layoutManager);
        mMainAdapter = new MainAdapter(this);
        mRecylerView.setAdapter(mMainAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerOnScrollListener = new AbsoluteRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int totalItemsCount, RecyclerView view) {
                if (!mSwipeRefreshLayout.isRefreshing()) {
                    mMainAdapter.setFooterState(true);
                    getData(LOAD_MORE);
                } else {
                    mRecyclerOnScrollListener.resetState();
                }
            }
        };
        mRecylerView.addOnScrollListener(mRecyclerOnScrollListener);
    }

    @Override
    public void onRefresh() {
        getData(REFRESH);
    }

    private void getData(int status) {
        switch (status) {
            case INIT:
                currentPage = 0;
                mCustomLoadingDialog.show();
                break;

            case REFRESH:
                currentPage = 0;
                mMainAdapter.setFooterState(false);
                mRecyclerOnScrollListener.resetState();
                break;

            case LOAD_MORE:
                if (oldPage == currentPage) {
                    mMainAdapter.setFooterState(false);
                    return;
                }

                break;

            default:
                break;
        }

        mStatus = status;
        oldPage = currentPage;

        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                // currentPage = 3; 假裝 emptyView用
                done();
            }
        }.start();

    }
    private void done () {
        mCustomLoadingDialog.dismiss();
        List<Product> productList = new ArrayList<>();

        switch (currentPage){
            case 0:
                for (int i = 0; i < 4; i++) {
                    Product product = new Product();
                    product.setTitle("test" + i);
                    product.setImg("");
                    productList.add(product);
                }

                break;
            case 1:
                for (int i = 5; i < 9; i++) {
                    Product product = new Product();
                    product.setTitle("test" + i);
                    product.setImg("");
                    productList.add(product);
                }

                break;
            default:
                break;
        }

        if (mStatus == REFRESH) {
            mSwipeRefreshLayout.setRefreshing(false);
            mMainAdapter.clear();
            mRecylerView.smoothScrollToPosition(0);
        }

        if (!productList.isEmpty()) {
            mMainAdapter.setEmpty(false);
            mMainAdapter.setFooterState(true);
            currentPage++;
        } else {
            mMainAdapter.setEmpty(true);
            mMainAdapter.setFooterState(false);
        }

        mMainAdapter.addAll(productList);

    }
}
