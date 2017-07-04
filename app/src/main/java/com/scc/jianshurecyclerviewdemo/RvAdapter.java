package com.scc.jianshurecyclerviewdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scc.jianshurecyclerviewdemo.bean.DatasBean;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/5/8.
 */
public class RvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DatasBean> datas;
    private Context mContext;
    private int status;
    public RvAdapter(List<DatasBean> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item, null));
        } else {
            return new MoreViewHolder(LayoutInflater.from(mContext).inflate(R.layout.load_more, null));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            ((MyViewHolder) holder).name.setText("姓名: " + datas.get(holder.getAdapterPosition()).getName());
            ((MyViewHolder) holder).sex.setText("性别: " + datas.get(holder.getAdapterPosition()).getSex());
            ((MyViewHolder) holder).price.setText("价格: " + datas.get(holder.getAdapterPosition()).getPrice());
        } else if (holder instanceof MoreViewHolder) {
            if (status == 1) {
                holder.itemView.setVisibility(View.GONE);
            } else {
                holder.itemView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return datas.size()+1;
    }


    static  class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, sex, price;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) itemView.findViewById(R.id.name);
            sex = (TextView) itemView.findViewById(R.id.sex);
            price = (TextView) itemView.findViewById(R.id.price);
        }
    }

    static class MoreViewHolder extends RecyclerView.ViewHolder {
        private MoreViewHolder(View itemView) {
            super(itemView);
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return 2;
        } else {
            return 1;
        }
    }

    public void getStatus(int i) {
        this.status = i;
    }
}
