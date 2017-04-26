package com.example.intelligentmanager.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.intelligentmanager.R;
import com.example.intelligentmanager.activity.CommuncityMainActivity;
import com.example.intelligentmanager.activity.SportsMainActivity;
import com.example.intelligentmanager.activity.TransactionActivity;
import com.example.intelligentmanager.finance.View.activity.FinanceMainActivity;

/**
 * Created by 易水柔 on 2017/3/8.
 */
public class Home_fragment extends Fragment implements View.OnClickListener {
    private ImageButton sports;
    private ImageButton finance;
    private ImageButton transaction;
    private ImageButton community;
    private String username;

    public Home_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.homepage_fragment,null);

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sports=(ImageButton)getActivity().findViewById(R.id.home_sports);
        finance=(ImageButton)getActivity().findViewById(R.id.home_finance);
        transaction=(ImageButton)getActivity().findViewById(R.id.home_transaction);
        community=(ImageButton)getActivity().findViewById(R.id.home_communication);


        username=getArguments().getString("username");

        sports.setOnClickListener(this);
        finance.setOnClickListener(this);
        transaction.setOnClickListener(this);
        community.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_sports:
                Bundle bundle=new Bundle();
                bundle.putString("username",username);
                Intent intent = new Intent(getActivity(), SportsMainActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.home_finance:
                Bundle bud=new Bundle();
                bud.putString("username",username);
                Intent intent2 = new Intent(getActivity(), FinanceMainActivity.class);
                intent2.putExtras(bud);
                startActivity(intent2);
                break;
            case R.id.home_transaction:
                Intent intent3 = new Intent(getActivity(), TransactionActivity.class);
                startActivity(intent3);
                break;
            case R.id.home_communication:
                Bundle bd=new Bundle();
                bd.putString("username",username);
                Intent intent4 = new Intent(getActivity(), CommuncityMainActivity.class);
                intent4.putExtras(bd);
                startActivity(intent4);
                break;

        }
    }
}
