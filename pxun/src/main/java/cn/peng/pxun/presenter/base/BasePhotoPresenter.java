package cn.peng.pxun.presenter.base;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import net.bither.util.NativeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;
import cn.peng.pxun.R;
import cn.peng.pxun.modle.AppConfig;
import cn.peng.pxun.ui.activity.BaseActivity;
import cn.peng.pxun.ui.fragment.BaseFragment;
import cn.peng.pxun.utils.LogUtil;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

/**
 * Created by tofirst on 2017/11/9.
 */

public class BasePhotoPresenter extends BasePresenter{
    private UpLoadFileListener mUpLoadFileListener;

    public BasePhotoPresenter(BaseActivity activity) {
        super(activity);
    }

    public BasePhotoPresenter(BaseFragment fragment) {
        super(fragment);
    }

    /**
     * 检测是否有录音权限
     * @param requestCode
     */
    private void checkPermission(final int requestCode) {
        List<PermissionItem> permissionItems = new ArrayList<PermissionItem>();
        permissionItems.add(new PermissionItem(Manifest.permission.CAMERA, "拍照", R.drawable.permission_ic_camera));

        mContext.requestPermission(permissionItems, new PermissionCallback() {
            @Override
            public void onClose() {
                showToast("授权失败");
            }

            @Override
            public void onFinish() {
                startSysCamera(requestCode);
            }

            @Override
            public void onDeny(String permission, int position) {

            }

            @Override
            public void onGuarantee(String permission, int position) {

            }
        });
    }

    /**
     * 启动系统照相机
     * @param requestCode
     */
    private void startSysCamera(int requestCode) {
        Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String fileName = "temp"+ ".jpg";// 照片命名
        String filePath = AppConfig.CACHE_PATH;
        File out = new File(AppConfig.CACHE_PATH);
        if (!out.exists()) {
            out.mkdirs();
        }
        out = new File(filePath, fileName);
        String imageFilePath = filePath + fileName;// 该照片的绝对路径
        Uri photoURI = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", out);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 2);
        imageCaptureIntent.putExtra("FilePath", imageFilePath);
        mContext.startActivityForResult(imageCaptureIntent, requestCode);
    }

    /**
     * 上传文件到Bmob后台
     * @param path 文件路径
     */
    public void upLoadFile(String path) {
        String fileName = path.substring(path.lastIndexOf("/"));
        //压缩图片
        String filePath = NativeUtil.compressBitmap(path, AppConfig.JPG_PATH + fileName);

        final BmobFile bmobFile = new BmobFile(new File(filePath));
        bmobFile.uploadblock(new UploadFileListener() {

            @Override
            public void done(BmobException e) {
                if(e==null){
                    String iconPath = bmobFile.getFileUrl();
                    if (mUpLoadFileListener != null){
                        mUpLoadFileListener.onUpLoadFinish(iconPath);
                    }
                }else{
                    LogUtil.e("头像上传", e.getMessage());
                    if (mUpLoadFileListener != null) {
                        mUpLoadFileListener.onUpLoadFinish("");
                    }
                }
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
                if (mUpLoadFileListener != null) {
                    mUpLoadFileListener.onUpLoadProgress(value);
                }
            }
        });
    }

    /**
     * 启动系统照相机
     * @param requestCode
     */
    public void startCamera(int requestCode) {
        if(Build.VERSION.SDK_INT>=23) {
            checkPermission(requestCode);
        }else {
            startSysCamera(requestCode);
        }
    }

    /**
     * 启动系统图库
     * @param requestCode
     */
    public void startPicDepot(int requestCode) {
        Uri uri = android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        mContext.startActivityForResult(intent, requestCode);
    }

    /**
     * 从Intent对象中获取Uri
     * @param intent
     * @return
     */
    public Uri getUri(Intent intent) {
        Uri uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (uri != null) {
            return uri;
        }
        uri = intent.getData();
        return uri;
    }

    /**
     * 从Uri对象中获取文件路径
     * @param uri
     * @return
     */
    public String getPathFromUri(Uri uri) {
        String path = "";
        path = uri.getPath();

        try {
            ContentResolver cr = mContext.getContentResolver();
            Cursor cursor = cr.query(uri, new String[] { MediaStore.MediaColumns.DATA },
                    null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            cursor.moveToFirst();
            path = cursor.getString(column_index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public void addUpLoadFileListener(UpLoadFileListener listener){
        this.mUpLoadFileListener = listener;
    }

    /**
     * 上传文件的回调接口
     */
    public interface UpLoadFileListener {

        void onUpLoadFinish(String path);

        void onUpLoadProgress(int value);
    }
}
