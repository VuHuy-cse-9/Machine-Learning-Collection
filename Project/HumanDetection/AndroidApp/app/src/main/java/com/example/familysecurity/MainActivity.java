package com.example.familysecurity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.familysecurity.Detection.DetectionAdapter;
import com.example.familysecurity.Detection.DetectionData;
import com.example.familysecurity.Detection.DetectionDetailView;
import com.example.familysecurity.Detection.DetectionListView;
import com.example.familysecurity.Detection.DetectionViewModel;
import com.example.familysecurity.Model.DataModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Fragment detectionFragment;
    private Fragment phoneFragment;
    private DataModel model;
    private DetectionViewModel viewModel;
    private int number_of_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageButton button_phone_list = findViewById(R.id.button_phone_list);
        ImageButton button_view_list = findViewById(R.id.button_view_list);
        phoneFragment = PhoneListView.newInstance("","");
        button_view_list.setEnabled(false);
        model = new DataModel(this);
        viewModel = new ViewModelProvider(this).get(DetectionViewModel.class);
        number_of_data = -1; //Haven't checked data exist

        //Get data:
        getDataPath();

        Observer<DetectionData> detectionDataObserver = new Observer<DetectionData>() {
            @Override
            public void onChanged(DetectionData data) {
                if (data == null) {
                    return;
                }
                addFragment(DetectionDetailView.newInstance(data.src, data.name, data.date, data.time));
                viewModel.getDetectionMutableLiveData().setValue(null);
            }
        };
        viewModel.getDetectionMutableLiveData().observe(this, detectionDataObserver);

        Observer<List<byte[]>> imgDetectionListObserver = new Observer<List<byte[]>>() {
            @Override
            public void onChanged(List<byte[]> bytes) {
                List<byte[]> img_list = viewModel.getImgDetectionList().getValue();
                if (number_of_data == -2 //Have checked data and not exist
                || img_list.size() == number_of_data) {
                    detectionFragment = DetectionListView.newInstance();
                    replaceFragment(detectionFragment);
                }
            }
        };
        viewModel.getImgDetectionList().observe(this, imgDetectionListObserver);

        button_view_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(detectionFragment);
                button_phone_list.setEnabled(true);
                button_view_list.setEnabled(false);
            }
        });

        button_phone_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(phoneFragment);
                button_phone_list.setEnabled(false);
                button_view_list.setEnabled(true);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // If there's a download in progress, save the reference so you can query it later
        if (this.model.isDownloadInProgress()) {
            outState.putString("reference", this.model.getReference());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // If there was a download in progress, get its reference and create a new StorageReference
        final String stringRef = savedInstanceState.getString("reference");
        if (stringRef == null) {
            return;
        }
        this.model.continueTask(this, stringRef);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fmgr = getSupportFragmentManager();
        FragmentTransaction ft = fmgr.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    private void addFragment(Fragment fragment) {
        FragmentManager fmgr = getSupportFragmentManager();
        FragmentTransaction ft = fmgr.beginTransaction();
        ft.add(R.id.fragment_container, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void getDataPath() {
        model.getPath(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot document, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Error", error.getLocalizedMessage());
                    return;
                }
                if (document.exists()) {
                    Log.d("GetPath", "DocumentSnapshot data: " + document.getData());
                    Map<String, Object> fields = document.getData();
                    //Use to know when to stop and render UI.
                    number_of_data = fields.size();
                    //If this update is new data, clear old data
                    if (viewModel.getImgDetectionList().getValue().size() != 0) {
                        viewModel.getImgDetectionList().setValue(new ArrayList<>());
                    }
                    //Add each data to list:
                    for (Map.Entry<String, Object> entry : fields.entrySet()) {
                        Log.d("image path", entry.getKey() + " - " + entry.getValue());
                        getData((String)entry.getValue());
                    }
                } else {
                    Log.d("GetPath", "No such document");
                }
            }
        });
    }

    private void getData(String path) {
        model.getData(path, new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                if (bytes != null && bytes.length != 0) {
                    Log.d("getdata", "Download successful: " + path);
                    List<byte[]> imgList = viewModel.getImgDetectionList().getValue();
                    imgList.add(bytes);
                    viewModel.getImgDetectionList().setValue(imgList);
                    model.stopDownload();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Getdata", e.getLocalizedMessage());
                model.stopDownload();
            }
        });
    }
}