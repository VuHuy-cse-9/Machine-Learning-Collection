package com.example.familysecurity.Detection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.familysecurity.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetectionDetailView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetectionDetailView extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "src";
    private static final String ARG_PARAM2 = "name";
    private static final String ARG_PARAM3 = "date";
    private static final String ARG_PARAM4 = "time";

    // TODO: Rename and change types of parameters
    private byte[] src;
    private String name;
    private String date;
    private String time;

    public DetectionDetailView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param src Parameter 1.
     * @param name Parameter 2.
     * @param date Parameter 3.
     * @param time Parameter 4.
     * @return A new instance of fragment DetectionDetailView.
     */
    // TODO: Rename and change types and number of parameters
    public static DetectionDetailView newInstance(byte[] src, String name, String date, String time) {
        DetectionDetailView fragment = new DetectionDetailView();
        Bundle args = new Bundle();
        args.putByteArray(ARG_PARAM1, src);
        args.putString(ARG_PARAM2, name);
        args.putString(ARG_PARAM3, date);
        args.putString(ARG_PARAM4, time);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            src = getArguments().getByteArray(ARG_PARAM1);
            name = getArguments().getString(ARG_PARAM2);
            date = getArguments().getString(ARG_PARAM3);
            time = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detection_detail_view, container, false);
        TextView text_name = view.findViewById(R.id.text_name_detail);
        TextView text_date = view.findViewById(R.id.text_date_detail);
        TextView text_time = view.findViewById(R.id.text_time_detail);
        Button btn_download = view.findViewById(R.id.btn_download);
        Button btn_call = view.findViewById(R.id.btn_call);
        //Set Image detail
        Bitmap bmp= BitmapFactory.decodeByteArray(src,0,src.length);
        ImageView img = view.findViewById(R.id.img_detail);
        img.setImageBitmap(bmp);
        //Set name detail
        text_name.setText(this.name);
        //Set date  detail
        text_date.setText(this.date);
        //Set time detail
        text_time.setText(this.time);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Downloading!", Toast.LENGTH_SHORT).show();
            }
        });
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Calling!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}