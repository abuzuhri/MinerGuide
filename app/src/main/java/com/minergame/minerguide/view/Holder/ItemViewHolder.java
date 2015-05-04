package com.minergame.minerguide.view.Holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.minergame.minerguide.R;
import com.minergame.minerguide.view.event.IClickCardView;

/**
 * Created by Tareq on 03/21/2015.
 */
public class ItemViewHolder extends BaseRecyclerViewHolder implements View.OnClickListener{

    public CheckBox favbutton;
    public ImageView photo;
    public ImageView watched;
    public TextView itemDescription;
    public TextView duration;
    public TextView categoty;



    public IClickCardView mListener;

    public ItemViewHolder(View itemLayoutView, IClickCardView listener) {
        super(itemLayoutView);
        mListener=listener;

        //favbutton=(CheckBox) itemLayoutView.findViewById(R.id.favbutton);
        photo=(ImageView) itemLayoutView.findViewById(R.id.photo);
        watched=(ImageView) itemLayoutView.findViewById(R.id.watched);
        itemDescription=(TextView) itemLayoutView.findViewById(R.id.itemDescription);
        duration=(TextView) itemLayoutView.findViewById(R.id.duration);
        categoty=(TextView) itemLayoutView.findViewById(R.id.categoty);
        itemLayoutView.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        mListener.onClick(v,getID());
    }


}
