package com.example.mediamagictest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediamagictest.R;
import com.example.mediamagictest.databinding.ListItemBinding;
import com.example.mediamagictest.model.ImageData;
import com.example.mediamagictest.util.CommonMethods;
import com.example.mediamagictest.util.ImageLoader;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.mediamagictest.util.Constant.imageUrl;

public class ImageAdpater extends RecyclerView.Adapter<ImageAdpater.ViewHolder> {
    private ArrayList<ImageData> list;
    private Context context;

    public ImageAdpater(Context context, ArrayList<ImageData> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListItemBinding listItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItemBinding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.listItemBinding.setImagedata(list.get(position));
        holder.listItemBinding.txtAuthorName.setSelected(true);
        String path = imageUrl + list.get(position).getId() + ".jpg";
        holder.listItemBinding.imageView.setImageDrawable(null);
        if (new CommonMethods().isInternetConnection(context)) {
            ImageLoader loader = new ImageLoader(context, holder.listItemBinding.imageView, holder.listItemBinding.imgProgressBar);
            loader.execute(path);
        } else {
            holder.listItemBinding.imgProgressBar.setVisibility(View.GONE);
            holder.listItemBinding.imageView.setImageResource(R.drawable.logo);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemBinding listItemBinding;

        public ViewHolder(@NonNull ListItemBinding itemView) {
            super(itemView.getRoot());
            listItemBinding = itemView;
        }
    }
}
