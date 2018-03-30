package com.funstill.kelefun.ui.send;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.funstill.kelefun.R;
import com.funstill.kelefun.base.BaseBackFragment;
import com.funstill.kelefun.data.api.StatusApi;
import com.funstill.kelefun.data.model.Status;
import com.funstill.kelefun.http.BaseRetrofit;
import com.funstill.kelefun.http.SignInterceptor;
import com.funstill.kelefun.util.ToastUtil;
import com.funstill.library.config.IHandlerCallBack;
import com.funstill.library.config.ImageSelector;
import com.funstill.library.config.SelectorConfig;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class SendStatusFragmentChild extends BaseBackFragment {
    private final static String TAG = "SendStatusFragmentChild";
    private Toolbar mToolbar;
    private EditText editText;
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private SelectorConfig selectorConfig;
    private IHandlerCallBack iHandlerCallBack;
    private ImageView rvResultPhoto;

    private String imageUrl = null;

    public static SendStatusFragmentChild newInstance() {
        Bundle args = new Bundle();
        SendStatusFragmentChild fragment = new SendStatusFragmentChild();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_status_child, container, false);
        initGallery();
        initView(view);
        return view;
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle("+Fun");
        mToolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
        mToolbar.inflateMenu(R.menu.send_menu);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.send_button:
                        postStatus();
                        break;
                    default:
                        ToastUtil.showToast(getContext(),"error menu");
                        break;
                }
                return true;

            }
        });
        initToolbarNav(mToolbar);

        editText = (EditText) view.findViewById(R.id.statusEdit);
        rvResultPhoto = (ImageView) view.findViewById(R.id.rvResultPhoto);

        selectorConfig = new SelectorConfig.Builder()
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .provider("com.funstill.kelefun.photo.fileprovider")   // provider(必填)
//                .multiSelect(true,3)//多选,最多3个
                .showCamera(true)                     // 是否现实相机按钮  默认：false
                .build();
        rvResultPhoto.setOnClickListener(v -> {
            if (initPermissions()) {
                ImageSelector.getInstance().setSelectorConfig(selectorConfig).open(_mActivity);
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    }

    // 授权管理
    private boolean initPermissions() {
        if (ContextCompat.checkSelfPermission(_mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Log.d(TAG, "需要授权 ");
            if (ActivityCompat.shouldShowRequestPermissionRationale(_mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.d(TAG, "拒绝过了");
                Toast.makeText(_mActivity, "请在 设置-应用管理 中开启此应用的储存授权。", Toast.LENGTH_SHORT).show();
            } else {
                //    Log.d(TAG, "进行授权");
                ActivityCompat.requestPermissions(_mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
            return false;
        } else {
            // Log.d(TAG, "不需要授权 ");
            return true;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    /**
     * 发送
     */
    private void postStatus() {
        ToastUtil.showToast(_mActivity, "正在发布中...");
//        backToHome();
        StatusApi api = BaseRetrofit.retrofit(new SignInterceptor()).create(StatusApi.class);
        if (imageUrl != null) {//带图片
            RequestBody status = RequestBody.create(MediaType.parse("text/plain"), editText.getText().toString());
            //TODO 如果不是gif则压缩图片
            Luban.with(_mActivity)
                    .load(imageUrl)
                    .ignoreBy(1024 * 2)//2MB
                    .putGear(4)
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onSuccess(File file) {
                            RequestBody photo = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                            MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), photo);
                            Call<Status> call = api.uploadPhoto(status, body);
                            enqueue(call);
                        }

                        @Override
                        public void onError(Throwable e) {
                        }
                    }).launch();


        } else {//只有文字
            if ("".equals(editText.getText().toString())) {
                ToastUtil.showToast(_mActivity, "输入不能为空");
                return;
            }
            String status = editText.getText().toString();
            Map<String, String> partMap = new ArrayMap<>();
            partMap.put("status", status);
            Call<Status> call = api.postStatus(partMap);
            enqueue(call);
        }


    }

    //
    private void enqueue(Call<Status> call) {
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.code() == 200) {
                    //清除页面已填数据
                    editText.setText("");
                    rvResultPhoto.setWillNotDraw(true);
                    ToastUtil.showToast(_mActivity, "发布消息成功");
                } else {
                    ToastUtil.showToast(_mActivity, "发布消息失败\n" + response.message());
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                ToastUtil.showToast(_mActivity, "发布消息失败");
            }

        });
    }

    private void initGallery() {
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
                Log.i(TAG, "onStart: 开启");
            }

            @Override
            public void onSuccess(List<String> photoList) {
                Log.i(TAG, "onSuccess: 返回数据");
                if (photoList.size() > 0) {
                    Glide.with(getContext())
                            .load(photoList.get(0))
                            .into(rvResultPhoto);
                    imageUrl = photoList.get(0);
                }
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel: 取消");
            }

            @Override
            public void onFinish() {
                Log.i(TAG, "onFinish: 结束");
            }

            @Override
            public void onError() {
                Log.i(TAG, "onError: 出错");
            }
        };
    }

    /**
     * 返回首页
     */
    private void backToHome() {
        _mActivity.onBackPressed();
    }


}
