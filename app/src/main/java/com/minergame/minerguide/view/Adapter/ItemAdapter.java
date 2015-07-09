package com.minergame.minerguide.view.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.minergame.minerguide.R;
import com.minergame.minerguide.db.Entity.Information;
import com.minergame.minerguide.db.Entity.ObjectTbl;
import com.minergame.minerguide.utils.AppLog;
import com.minergame.minerguide.view.Holder.ItemViewHolder;
import com.minergame.minerguide.view.event.IClickCardView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Tareq on 03/21/2015.
 */

public class ItemAdapter  extends RecyclerView.Adapter<ItemViewHolder>  {

    public List<ObjectTbl> mDataset;
    private Context context;
    private IClickCardView mListener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ItemAdapter(List<ObjectTbl> myDataset, Context context, IClickCardView mListener) {
        mDataset = myDataset;
        this.context=context;
        this.mListener=mListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_object, null);

        // create ViewHolder
        ItemViewHolder viewHolder = new ItemViewHolder(itemLayoutView, new IClickCardView() {
            @Override
            public void onClick(View v,long ID) {
                mListener.onClick(v, ID);
            }
        });
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ItemViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        ObjectTbl obj = mDataset.get(position);

        viewHolder.setID(obj.getId());
        viewHolder.itemDescription.setText(obj.Name);
        viewHolder.watched.setVisibility(View.INVISIBLE);
        String categoryText = "";
        categoryText = obj.Category;
        if (obj.SubCategory != null && !obj.SubCategory.equals("")) {
            categoryText = categoryText + " > " + obj.SubCategory;
        }
        viewHolder.categoty.setText(categoryText);


        String extnsion = ".png";
        if (obj.Category.equals("Biome"))
            extnsion = ".jpg";

        String uri = "icons/icon_" + obj.getId() + extnsion;

        if (!obj.Category.equals("Video")) {
            if (obj.Informations() != null && obj.Informations().size() > 0) {
                Information info = obj.Informations().get(0);
                viewHolder.duration.setText(info.Information);
            }
        } else {
            //viewHolder.itemDescription.setMaxLines(2);
            viewHolder.duration.setText(obj.Name);
        }

        try {
            // get input stream
            InputStream ims = context.getAssets().open(uri);
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            viewHolder.photo.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if(mDataset != null) {
            AppLog.i("mDataset ==> " + mDataset.size());
            return mDataset.size();
        }else{
            return 0;
        }

    }


}
