package com.example.familysecurity.Detection;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.familysecurity.MainActivity;
import com.example.familysecurity.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetectionListView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetectionListView extends Fragment {
    public DetectionListView() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DetectionListView newInstance() {
        return new DetectionListView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detection_list_view, container, false);
        DetectionViewModel viewModel = new ViewModelProvider(requireActivity()).get(DetectionViewModel.class);

        List<DetectionData> datas = new ArrayList<>();
        List<byte[]> img_data_list = viewModel.getImgDetectionList().getValue();
        for (byte[] img_data : img_data_list) {
            datas.add(new DetectionData(img_data, "Minh Huy", "21/12/2021", "19:00:00"));
        }

        ListView listView = view.findViewById(R.id.list_detection_view);
        listView.setAdapter(new DetectionAdapter(requireContext(), datas));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                DetectionData data = (DetectionData) o;
                viewModel.getDetectionMutableLiveData().setValue(data);
            }
        });
        return view;
    }
}