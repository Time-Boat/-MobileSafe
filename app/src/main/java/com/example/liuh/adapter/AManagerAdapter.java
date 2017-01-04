package com.example.liuh.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.liuh.R;
import com.example.liuh.bean.AppInfo;
import com.example.liuh.bean.ITEM_TYPE;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016-09-26.
 */
public class AManagerAdapter extends SwipeMenuAdapter<RecyclerView.ViewHolder> {

    Context context;
    List<AppInfo> ai;

//    private OnItemClickListener mOnItemClickListener;

    public AManagerAdapter(Context context, List<AppInfo> data) {
        this.ai = data;
        this.context = context;
    }

//    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//        this.mOnItemClickListener = onItemClickListener;
//    }

//    @Override
//    public int getItemCount() {
//        return ai == null ? 0 : ai.size();
//    }

    @Override
    public int getItemCount() {
        if (ai != null && ai.size() > 0) {
            //这里的10是根据分页查询，一页该显示的条数
            if (ai.size() > 4) {
                return ai.size() + 1;
            } else {
                return ai.size();
            }
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() > 10 && position + 1 == getItemCount()) {

            return ITEM_TYPE.TYPE_FOOTER.ordinal();
        } else {
            return ITEM_TYPE.TYPE_ITEM.ordinal();
        }
    }

//    @Override
//    public View onCreateContentView(ViewGroup parent, int viewType) {
//        return LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_app_manager, parent, false);
//    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.TYPE_ITEM.ordinal()) {
            return LayoutInflater.from(context).inflate(R.layout.adapter_app_manager, parent, false);
        } else if (viewType == ITEM_TYPE.TYPE_FOOTER.ordinal()) {
            return LayoutInflater.from(context).inflate(R.layout.recyclerview_item_foot, parent, false);
        }
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        if (viewType == ITEM_TYPE.TYPE_ITEM.ordinal()) {
            return new AMViewHolder(realContentView);
        } else if (viewType == ITEM_TYPE.TYPE_FOOTER.ordinal()) {
            return new FootViewHolder(realContentView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof AMViewHolder){
            AMViewHolder holder1 = (AMViewHolder)holder;
            holder1.title.setText(ai.get(position).getName());
            holder1.subhead.setText("版本："+ai.get(position).getVerName());
            holder1.size.setText(Formatter.formatFileSize(context,ai.get(position).getAppSize()));
            holder1.iv.setImageDrawable(ai.get(position).getIcon());
//            holder1.setOnItemClickListener(mOnItemClickListener);
        }else{
            FootViewHolder holder1 = (FootViewHolder)holder;
        }

    }

    static class CommonViewHolder extends RecyclerView.ViewHolder{
        public CommonViewHolder(View itemView) {
            super(itemView);
        }
    }

    static class AMViewHolder extends CommonViewHolder /*implements View.OnClickListener*/ {

        TextView title;
        TextView subhead;
        TextView size;
        ImageView iv;
//        OnItemClickListener mOnItemClickListener;

        public AMViewHolder(View view) {
            super(view);
//            view.setOnClickListener(this);
            title = (TextView)view.findViewById(R.id.tv_app_title);
            subhead = (TextView)view.findViewById(R.id.tv_app_subhead);
            size = (TextView)view.findViewById(R.id.tv_app_size);
            iv = (ImageView)view.findViewById(R.id.img_app_icon);
            view.setClickable(true);
        }

//        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
//            this.mOnItemClickListener = onItemClickListener;
//        }

//        @Override
//        public void onClick(View v) {
//            if (mOnItemClickListener != null) {
//                mOnItemClickListener.onItemClick(getAdapterPosition());
//            }
//        }
    }

    //可以将其放入baseAdapter中
    static class FootViewHolder extends CommonViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }

//    public interface OnItemClickListener {
//
//        void onItemClick(int position);
//    }

}
