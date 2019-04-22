package com.example.welisten;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    private Button clearBtn;
    private ArrayList<InputHistory> mInputHistoryArrayList;
    private LinearLayout itemsLayout;
    private DBHandler dbHandler;

    void loadHistory(){
        mInputHistoryArrayList = dbHandler.loadHandler();


        for(InputHistory ip : mInputHistoryArrayList){
            final TextView date = new TextView(getContext());
            TextView text = new TextView(getContext());
            date.setText(ip.getDate());
            date.setTextColor(Color.parseColor("#121212"));
            date.setTextSize(20);

            text.setText(ip.getInputText());
            itemsLayout.addView(text);
            itemsLayout.addView(date);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        clearBtn = getActivity().findViewById(R.id.clearBtn);
        itemsLayout = getActivity().findViewById(R.id.itemsLayout);

        dbHandler = new DBHandler(getActivity().getApplicationContext(), null, null, 1);
        this.loadHistory();




        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHandler.clearHandler();
                itemsLayout.removeAllViews();
            }
        });

    }
}
