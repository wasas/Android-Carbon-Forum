package com.lincanbin.carbonforum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lincanbin.carbonforum.R;
import com.lincanbin.carbonforum.config.APIAddress;
import com.lincanbin.carbonforum.util.TimeUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by 灿斌 on 10/13/2015.
 */
public class PostAdapter extends RecyclerView.Adapter{
    private Context context;
    private LayoutInflater layoutInflater;
    public interface OnRecyclerViewListener {
        void onItemClick(int position);
        boolean onItemLongClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    private static final String TAG = PostAdapter.class.getSimpleName();
    private List<Map<String,Object>> list;
    public PostAdapter(Context context){
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    public void setData(List<Map<String,Object>> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = layoutInflater.inflate(R.layout.item_post_list, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new topicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        final topicViewHolder holder = (topicViewHolder) viewHolder;
        holder.position = i;
        Map<String,Object> topic = list.get(i);
        holder.UserName.setText(topic.get("UserName").toString());
        holder.Time.setText(TimeUtil.formatTime(context, Long.parseLong(topic.get("PostTime").toString())));
        String contentHTML = "<span style=\"color:#616161;\">";
        contentHTML += topic.get("Content").toString().replace("=\"/", "=\"" + APIAddress.DOMAIN_NAME.replace(APIAddress.WEBSITE_PATH, "") + "/");
        contentHTML += "</span>";
        holder.Content.loadDataWithBaseURL(null, contentHTML, "text/html", "utf-8", null);
        Glide.with(context).load(APIAddress.MIDDLE_AVATAR_URL(topic.get("UserID").toString(), "middle")).into(holder.Avatar);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class topicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        public View rootView;
        ImageView Avatar;
        TextView Time;
        TextView UserName;
        WebView Content;
        public int position;

        public topicViewHolder(View itemView) {
            super(itemView);

            UserName = (TextView) itemView.findViewById(R.id.username);

            Content = (WebView) itemView.findViewById(R.id.content);
            // http://stackoverflow.com/questions/15133132/android-webview-doesnt-display-web-page-in-some-cases
            Content.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            Content.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//优先使用缓存
            Time = (TextView) itemView.findViewById(R.id.time);
            Avatar = (ImageView)itemView.findViewById(R.id.avatar);
            rootView = itemView.findViewById(R.id.topic_item);
            rootView.setOnClickListener(this);
            rootView.setOnLongClickListener(this);
        }
        @Override
        //点击事件
        public void onClick(View v) {
            //Toast.makeText(context, "onItemClick" + list.get(position).get("Topic").toString(), Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(context, TopicActivity.class);
            //intent.putExtra("Topic", list.get(position).get("Topic").toString());
            //intent.putExtra("TopicID", list.get(position).get("ID").toString());
            //intent.putExtra("TargetPage", "1");
            //context.startActivity(intent);
            //if (null != onRecyclerViewListener) {
            //onRecyclerViewListener.onItemClick(position);
            //}
        }

        @Override
        //长按事件
        public boolean onLongClick(View v) {
            if(null != onRecyclerViewListener){
                return onRecyclerViewListener.onItemLongClick(position);
            }
            return false;
        }
    }
}