package com.ytempest.tool.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ytempest.tool.util.DataUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author heqidu
 * @since 2020/8/1
 */
public abstract class CoreRecyclerAdapter<Item> extends RecyclerView.Adapter<CoreViewHolder> {

    private final ArrayList<Item> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private RecyclerView mRecyclerView;

    public CoreRecyclerAdapter() {
    }

    public LayoutInflater getInflater() {
        return mInflater;
    }

    public CoreRecyclerAdapter(Item... items) {
        if (items != null) {
            Collections.addAll(mData, items);
        }
    }

    public CoreRecyclerAdapter(List<Item> items) {
        if (items != null) {
            mData.addAll(items);
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mInflater = LayoutInflater.from(recyclerView.getContext());
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mRecyclerView = null;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @NonNull
    @Override
    public CoreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CoreViewHolder viewHolder = onCreateView(mInflater, viewGroup, i);
        setupClick(viewHolder);
        return viewHolder;
    }

    /*Click*/

    private void setupClick(CoreViewHolder viewHolder) {
        View rootView = viewHolder.itemView;
        if (viewHolder.isNeedClick()) {
            if (mClickMap == null) {
                mClickMap = new HashMap<>(getItemCount());
            }
            if (mClickListener == null) {
                mClickListener = view -> {
                    CoreViewHolder holder = mClickMap != null ? mClickMap.get(view) : null;
                    if (holder != null) {
                        int pos = holder.getAdapterPosition();
                        if (pos >= 0 && pos < getItemCount()) {
                            onItemClick(holder, view, pos);
                        }
                    }
                };
            }
            rootView.setOnClickListener(mClickListener);
            mClickMap.put(rootView, viewHolder);
        }

        if (viewHolder.isNeedLongClick()) {
            if (mLongClickMap == null) {
                mLongClickMap = new HashMap<>(getItemCount());
            }
            if (mLongClickListener == null) {
                mLongClickListener = view -> {
                    CoreViewHolder holder = mLongClickMap != null ? mLongClickMap.get(view) : null;
                    if (holder != null) {
                        int pos = holder.getAdapterPosition();
                        if (pos >= 0 && pos < getItemCount()) {
                            return onItemLongClick(holder, view, pos);
                        }
                    }
                    return false;
                };
            }
            rootView.setOnClickListener(mClickListener);
            mLongClickMap.put(rootView, viewHolder);
        }

    }

    private Map<View, CoreViewHolder> mClickMap;
    private View.OnClickListener mClickListener;

    protected void onItemClick(CoreViewHolder holder, View view, int position) {

    }

    private Map<View, CoreViewHolder> mLongClickMap;
    private View.OnLongClickListener mLongClickListener;

    protected boolean onItemLongClick(CoreViewHolder holder, View view, int position) {
        return false;
    }

    /*Bind*/

    @Override
    public void onBindViewHolder(@NonNull CoreViewHolder coreViewHolder, int position) {
        Item item = mData.get(position);
        onBindData(coreViewHolder, item, position);
        afterBindData(coreViewHolder, position);
    }


    protected abstract CoreViewHolder onCreateView(LayoutInflater inflater, ViewGroup viewGroup, int position);

    protected abstract void onBindData(CoreViewHolder coreViewHolder, Item item, int position);

    protected void afterBindData(CoreViewHolder coreViewHolder, int position) {
    }

    /*Data-get*/

    public Item getData(int pos) {
        return mData.get(pos);
    }

    @Nullable
    public Item getDataSafe(int pos) {
        return DataUtils.get(mData, pos);
    }

    public List<Item> getDataList() {
        return new ArrayList<>(mData);
    }

    public List<Item> getSrcDataList() {
        return mData;
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public boolean contains(Item item) {
        return mData.contains(item);
    }

    public int indexOf(Item item) {
        return mData.indexOf(item);
    }

    /*Data-set*/

    public void addData(Item... items) {
        if (items != null && Collections.addAll(mData, items)) {
            notifyItemRangeInserted(mData.size() - items.length, items.length);
        }
    }

    public void addData(Collection<? extends Item> items) {
        if (items != null && mData.addAll(items)) {
            notifyItemRangeInserted(this.mData.size() - items.size(), items.size());
        }
    }

    public void insert(int pos, Item item) {
        int safePos = Math.min(pos, mData.size());
        mData.add(safePos, item);
        notifyItemInserted(safePos);
    }

    /*Data*/

    @Nullable
    public Item remove(int pos) {
        if (pos < mData.size()) {
            Item remove = mData.remove(pos);
            notifyItemRemoved(pos);
            return remove;
        }
        return null;
    }

    public boolean remove(Item item) {
        int pos = mData.indexOf(item);
        if (pos >= 0) {
            mData.remove(item);
            notifyItemRemoved(pos);
            return true;
        }
        return false;
    }

    public void display(List<Item> list) {
        if (mData != list) {
            mData.clear();
            if (list != null) {
                mData.addAll(list);
            }
        }
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    public void refreshAll() {
        notifyDataSetChanged();
    }

    public boolean refresh(Item data) {
        int pos = mData.indexOf(data);
        if (pos >= 0) {
            mData.set(pos, data);
            notifyItemChanged(pos);
            return true;
        }
        return false;
    }

}
