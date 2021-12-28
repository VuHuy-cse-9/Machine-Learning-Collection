package com.example.familysecurity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.familysecurity.Detection.DetectionAdapter;
import com.example.familysecurity.Detection.DetectionData;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhoneListView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhoneListView extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PhoneListView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhoneListView.
     */
    // TODO: Rename and change types and number of parameters
    public static PhoneListView newInstance(String param1, String param2) {
        PhoneListView fragment = new PhoneListView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_phone_list_view, container, false);

//        List<DetectionData> datas = new ArrayList<>();
//        datas.add(new DetectionData("", "Minh Huy", "21/12/2021", "19:00:00"));
//        datas.add(new DetectionData("", "Minh Huy", "20/9/2021", "23:00:00"));
//        datas.add(new DetectionData("", "Minh Huy", "25/5/2021", "17:00:00"));
//
//        ListView listView = view.findViewById(R.id.list_phone_view);
//        listView.setAdapter(new DetectionAdapter(requireContext(), datas));
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
//                Object o = listView.getItemAtPosition(position);
//                DetectionData data = (DetectionData) o;
//                Toast.makeText(requireContext(), "Selected :" + " " + data.date, Toast.LENGTH_LONG).show();
//            }
//        });
        return view;
    }
}