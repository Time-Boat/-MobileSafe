package com.example.liuh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liuh.R;
import com.example.liuh.bean.FunctionAdapterEntity;

import java.util.List;

/**
 * Created by Administrator on 2016-09-19.
 */
public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.MyViewHolder> {

    private Context context;

    List<FunctionAdapterEntity> mData;

    private onRecyclerItemClickListener orcl;

    public FunctionAdapter(Context context,List<FunctionAdapterEntity> data,onRecyclerItemClickListener listener){
        this.context = context;
        this.mData = data;
        this.orcl = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_main_recycler,parent,false);
        MyViewHolder mv = new MyViewHolder(view);
        return mv;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(mData.get(position).getTitle());
        holder.subhead.setText(mData.get(position).getSubhead());
        holder.iv.setImageBitmap(mData.get(position).getPic());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView subhead;
        TextView title;
        ImageView iv;

        public MyViewHolder(View view)
        {
            super(view);
            iv = (ImageView) view.findViewById(R.id.img_ico);
            title = (TextView) view.findViewById(R.id.tv_title);
            subhead = (TextView) view.findViewById(R.id.tv_subhead);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(orcl!=null){
                orcl.onClick(v,getAdapterPosition());
            }
        }
    }

    public static interface onRecyclerItemClickListener{
        void onClick(View view,int position);
    }
    
}
