package com.noeasy.lcp.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.noeasy.lcp.R;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        final ImageView imageView = root.findViewById(R.id.imageView);
        //imageView.setImageDrawable(getActivity().getDrawable(R.drawable.img));
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        //Glide.with(root).load("https://cdn.ppomppu.co.kr/zboard/data3/2020/0407/m_20200407125014_broboimi.jpg").into(imageView);
        return root;
    }
}
