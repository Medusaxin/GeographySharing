package com.example.lenovo.geographysharing.Issue;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lenovo.geographysharing.BaseClass.BaseActivity;
import com.example.lenovo.geographysharing.R;

import java.io.File;
import java.io.IOException;

import static com.example.lenovo.geographysharing.Utils.CompressImageUtil.compressImageUtil;


/**
 * Created by lenovo on 2018/1/2.
 */

public class IssueDetailActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_Upload_Pic; //上传图片按钮
    private Button btn_Publish; //发布设备按钮
    private ImageView img = null;//加载设备的图片


    // 返回码：系统图库
    private static final int RESULT_IMAGE = 100;
    // 返回码：相机
    private static final int RESULT_CAMERA = 200;

    private Uri imageUri = Uri.parse("content://media/external/images/media/1");

    private File outputImage = null;//相机拍摄图片的缓存位置



    @Override
    protected int getLayoutId() {
        return R.layout.activity_issue_details;
    }

    @Override
    protected void initView() {
        setSupportActionBar();//表示当前页面支持ActionBar
        setSupportArrowActionBar(true);
        setTitle("填写发布设备信息");
        img = bindViewId(R.id.issue_detail_pic);
        btn_Publish = bindViewId(R.id.issue);//发布设备按钮
        btn_Upload_Pic = bindViewId(R.id.issue_pic);//上传图片按钮

        btn_Publish.setOnClickListener(this);
        btn_Upload_Pic.setOnClickListener(this);

    /*
    * 以下被注释的方法适合一个button时用
    * */
//        btn_Upload_Pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Chose_Pic_or_Cam();
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.issue:
                //发布设备按钮实现接口
                break;
            case R.id.issue_pic:
                Choose_Pic_or_Cam();//上传图片时，拍照还是图库。
                break;
            default:
                break;

        }

    }

    private void Choose_Pic_or_Cam() {
        //将文件读写权限写在此处。
        if (ContextCompat.checkSelfPermission(IssueDetailActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(IssueDetailActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);//参数为0,表示不跳向图库activity。
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                  /*.setTitle("选择图片：")
                  * */
                .setItems(new String[]{"相机", "图库"}, new android.content.DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                takePhoto();
                                break;
                            case 1:
                                openAlbum();
                                break;
                            default:
                                break;
                        }

                    }
                });
        builder.create().show();
    }

    /**
     * 打开相册
     */
    public void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_IMAGE);
    }

    /**
     * 拍摄
     */
    private void takePhoto() {
        outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(IssueDetailActivity.this, "com.example.lenovo.geographysharing.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        //测试点
        Toast.makeText(IssueDetailActivity.this, imageUri.toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, RESULT_CAMERA);
    }

    /**
     * 图库访问许可后调用图片    此APP未用
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openAlbum();
            }
        }
    }


    /**
     * 回调
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            try {

                compressImageUtil.CreateFiles();

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (requestCode == RESULT_IMAGE && data != null) {
                // 相册
                if (Build.VERSION.SDK_INT >= 19) {//版本兼容
                    handleImageOnKitKat(data);
                } else {
                    handleImageBeforeKitKat(data);
                }
            } else if (requestCode == RESULT_CAMERA) {
                // 相机
                //Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                Bitmap showBitmap = compressImageUtil.getSmallBitmap_File(outputImage,245,180);//压缩图片
                img.setImageBitmap(showBitmap);
            }
        }


    }

    /**
     * 相册选择图片
     * 大于4.4版本SDK
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String documentId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = documentId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(documentId));
                imagePath = getImagePath(contentUri, null);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                imagePath = uri.getPath();
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                imagePath = uri.getPath();
            }
            displayImage(imagePath);
        }
    }

    /**
     * 相册选择图片
     * 小于于4.4版本SDK
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    /**
     * 获取相册图片路径
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 显示图片
     */
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//显示缩略图
            //Bitmap showbitmap = ThumbnailUtils.extractThumbnail(bitmap, 245, 180);//显示缩略图
            Bitmap showBitmap = compressImageUtil.getSmallBitmap_String(imagePath,245,180);
            img.setImageBitmap(showBitmap);

        } else {
            Toast.makeText(this, "请选择正确的图片路径！", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void initData() {

    }

    public static void launchIssueDetailActivity(Activity activity) {
        Intent intent = new Intent(activity, IssueDetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }

}

