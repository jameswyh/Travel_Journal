package com.example.james_000.traveljournal.New;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.example.james_000.traveljournal.R;

import java.util.List;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context mContext;
    private List<PoiInfo> mItems;
    private LayoutInflater mInflater;

    private OnMyItemClickListener listener;

    public void setOnMyItemClickListener(OnMyItemClickListener listener) {
        this.listener = listener;

    }

    public interface OnMyItemClickListener {
        void myClick(View v, int pos);
    }

    public MyAdapter(Context mContext, List<PoiInfo> mItems) {
        this.mItems = mItems;
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_location.setText(mItems.get(position).name);
        holder.tv_detail_location.setText(mItems.get(position).address);
        if (listener != null) {
            holder.tv_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.myClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_location;
        private TextView tv_detail_location;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_location= (TextView) itemView.findViewById(R.id.tv_location);
            tv_detail_location= (TextView) itemView.findViewById(R.id.tv_detail_location);
        }
    }
}
