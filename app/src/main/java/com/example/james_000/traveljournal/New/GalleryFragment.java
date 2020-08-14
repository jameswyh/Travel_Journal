package com.example.james_000.traveljournal.New;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.james_000.traveljournal.R;

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";
    private static final int RESULT_IMAGE=100;
    //设置MIME码：表示image所有格式的文件均可
    private static final String IMAGE_TYPE="image/*";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        //设置返回码：标识本地图库

//        //实例化Intent,传入ACTION_PICK,表示从Item中选取一个数据返回
//        Intent intent=new Intent(Intent.ACTION_PICK,null);
//        //设置Data和Type属性，前者是URI：表示系统图库的URI,后者是MIME码
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,IMAGE_TYPE);
//        //启动这个intent所指向的Activity
//        startActivityForResult(intent,RESULT_IMAGE);

        return view;
    }
}
