package com.neyo.absoluterecycleview.sample;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.neyo.absoluterecycleview.AbsoluteRecyclerViewAdapter;
import com.neyo.absoluterecycleview.R;
import com.neyo.absoluterecycleview.sample.model.Product;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neyo on 2017/12/19.
 */

public class MainAdapter extends AbsoluteRecyclerViewAdapter {

    private static final int VIEW_TYPE_EMPTY = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private List<Product> mdata;
    private Context mContext;
    private int footerItemCount = 0;
    private boolean isEmpty = false;

    public MainAdapter(Context context) {
        mContext = context;
    }

    @Override
    protected int getHeaderItemCount() {
        return 0;
    }

    @Override
    protected int getFooterItemCount() {
        return footerItemCount;
    }

    @Override
    protected int getContentItemCount() {
        Log.e("Neyo", "getContentItemCount");

        if (mdata != null && mdata.size() > 0) {
            return mdata.size();
        } else {
            return 1;
        }
    }

    @Override
    protected int getContentItemViewType(int position) {

        if (mdata != null && mdata.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateHeaderItemViewHolder(ViewGroup parent, int headerViewType) {
        return null;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateFooterItemViewHolder(ViewGroup parent, int footerViewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_load_more, parent, false);
        return new FooterViewHolder(item);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateContentItemViewHolder(ViewGroup parent, int contentViewType) {
        View item;
        switch (contentViewType) {
            case VIEW_TYPE_EMPTY:
                item = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false);
                return new EmptyViewHolder(item);
            case VIEW_TYPE_NORMAL:
            default:
                item = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_swipe_layout, parent, false);
                return new ContentViewHolder(item);
        }
    }

    @Override
    protected void onBindHeaderItemViewHolder(RecyclerView.ViewHolder headerViewHolder, int position) {

    }

    @Override
    protected void onBindFooterItemViewHolder(RecyclerView.ViewHolder footerViewHolder, int position) {
    }

    @Override
    protected void onBindContentItemViewHolder(RecyclerView.ViewHolder contentViewHolder, int position) {
        int viewType = getContentItemViewType(position);

        if (viewType == VIEW_TYPE_EMPTY) {
            EmptyViewHolder viewHolder = (EmptyViewHolder) contentViewHolder;
            if (isEmpty) {
                viewHolder.emptyView.setVisibility(View.VISIBLE);
            } else {
                viewHolder.emptyView.setVisibility(View.GONE);
            }
        } else {
            ContentViewHolder viewHolder = (ContentViewHolder) contentViewHolder;
            final Product product = mdata.get(position);
            viewHolder.mTitle.setText(product.getTitle());
            viewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, product.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    public void clear() {
        mdata.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Product> products) {
        if (mdata == null) {
            mdata = new ArrayList<>();
        }

        mdata.addAll(products);
        notifyDataSetChanged();
    }

    public void setFooterState(boolean isVisible) {

        if (isVisible) {
            footerItemCount = 1;
        } else {
            footerItemCount = 0;
        }
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view)
        CardView mCardView;
        @BindView(R.id.title)
        TextView mTitle;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class EmptyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.empty_view)
        View emptyView;

        public EmptyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
