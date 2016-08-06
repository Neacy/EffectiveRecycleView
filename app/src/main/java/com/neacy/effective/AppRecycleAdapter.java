package com.neacy.effective;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.neacy.effective.mvp.AppBean;

import java.util.List;

/**
 * Created by jayuchou on 16/8/6.
 */
public class AppRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<AppBean> beans;

    public AppRecycleAdapter(Context mContext, List<AppBean> beans) {
        this.mContext = mContext;
        this.beans = beans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder _holder, int position) {
        AppViewHolder holder = (AppViewHolder) _holder;
        holder.imageView.setImageDrawable(beans.get(position).appIcon);
        holder.textView.setText(beans.get(position).appName);
    }

    @Override
    public int getItemCount() {
        return beans.size();
    }

    public class AppViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public AppViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.app_icon);
            textView = (TextView) itemView.findViewById(R.id.app_name);

//             另一种设置点击事件..
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//                    Toast.makeText(mContext, beans.get(position).appName, Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }
}
