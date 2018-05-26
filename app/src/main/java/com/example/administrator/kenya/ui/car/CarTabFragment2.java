package com.example.administrator.kenya.ui.car;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.kenya.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarTabFragment2 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    public static CarTabFragment2 newInstance(String param1) {
        CarTabFragment2 fragment = new CarTabFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public CarTabFragment2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myview = inflater.inflate(R.layout.fragment_car_tab_fragment2, container, false);
        return myview;
    }

}
