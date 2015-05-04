package com.minergame.minerguide.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.minergame.minerguide.R;
import com.minergame.minerguide.db.Dao.ObjectDao;
import com.minergame.minerguide.db.Entity.ObjectTbl;
import com.minergame.minerguide.ui.activity.ChatActivity;
import com.minergame.minerguide.utils.AppAction;
import com.minergame.minerguide.utils.AppLog;
import com.minergame.minerguide.view.Adapter.ItemAdapter;
import com.minergame.minerguide.view.event.IClickCardView;

import java.util.List;

/**
 * Created by Tareq on 03/20/2015.
 */
public class ItemListFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int position,MenuId;
    private String searchText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        position= getArguments().getInt(AppAction.EXTRA.POSITION,0);
        MenuId= getArguments().getInt(AppAction.EXTRA.MENUID,0);
        AppLog.i("MenuId =>"+MenuId);
        searchText= getArguments().getString(AppAction.EXTRA.SEARCHEXTRA, "");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        FillDate();
        //mRecyclerView.setItemAnimator(new FeedItemAnimator());
        final Activity activity=getActivity();

        FloatingActionButton fab=(FloatingActionButton) rootView.findViewById(R.id.fab);
        //fab.attachToRecyclerView(mRecyclerView);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              // AppAction.OpenActivityWithFRAGMENT(activity, OneFragmentActivity.class, ChatFragment.class.getName());
                AppAction.OpenActivity(activity, ChatActivity.class);

            }
        });

        return rootView;
    }


    private void FillDate(){
        ObjectDao objectDao=new ObjectDao();
        List<ObjectTbl> objectList=null;

        AppLog.i("MenuId=="+MenuId);

        if(MenuId!=0)
            objectList= objectDao.getAllByMenu(MenuId);
        else if(searchText!="")
            objectList= objectDao.getAll(searchText);
        else objectList= objectDao.getAll();

        mAdapter = new ItemAdapter(objectList,getActivity(),new IClickCardView() {
            @Override
            public void onClick(View v, long ID) {
                AppAction.OpenActivityWithFRAGMENT(getActivity(), ItemDetailsFragment.class.getName(), ID);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }
}
