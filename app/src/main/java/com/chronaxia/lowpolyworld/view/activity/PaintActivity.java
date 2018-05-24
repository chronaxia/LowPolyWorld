package com.chronaxia.lowpolyworld.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.chronaxia.lowpolyworld.R;
import com.chronaxia.lowpolyworld.app.LowPolyWorldApp;
import com.chronaxia.lowpolyworld.presenter.PaintPresenter;
import com.chronaxia.lowpolyworld.presenter.contract.PaintContract;
import com.chronaxia.lowpolyworld.util.BitmapUtils;
import com.chronaxia.lowpolyworld.view.custom.PaletteView;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class PaintActivity extends BaseActivity implements PaintContract.View, PaletteView.Callback{

    @BindView(R.id.iv_undo)
    ImageView ivUndo;
    @BindView(R.id.iv_redo)
    ImageView ivRedo;
    @BindView(R.id.iv_pen)
    ImageView ivPen;
    @BindView(R.id.iv_eraser)
    ImageView ivEraser;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.iv_save)
    ImageView ivSave;
    @BindView(R.id.iv_color)
    ImageView ivColor;
    @BindView(R.id.iv_paint_back)
    ImageView ivPaintBack;
    @BindView(R.id.iv_template)
    ImageView ivTemplate;
    @BindView(R.id.palette)
    PaletteView paletteView;

    private ProgressDialog progressDialog;
    private AlertDialog colorPickerView;
    private static final int MSG_SAVE_SUCCESS = 1;
    private static final int MSG_SAVE_FAILED = 2;
    private PaintContract.Presenter presenter;
    private List<String> templateList;

    @Override
    protected int setContentView() {
        return R.layout.activity_paint;
    }

    @Override
    protected void initData() {
        presenter = new PaintPresenter(this, this);
        templateList = new ArrayList<>();
        presenter.loadTemplate(getResources().getXml(R.xml.template));
    }

    @Override
    protected void initView() {
        paletteView.setCallback(this);
        ivPen.setSelected(true);
        ivUndo.setEnabled(false);
        ivRedo.setEnabled(false);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在保存,请稍候...");
        progressDialog.setCancelable(false);
        colorPickerView = ColorPickerDialogBuilder
                .with(this)
                .initialColor(0XFFFF0000)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(9)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                })
                .setPositiveButton("确认", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        paletteView.setPenColor(selectedColor);
                        PaintActivity.this.hideButton();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        colorPickerView.dismiss();
                        PaintActivity.this.hideButton();
                    }
                })
                .build();
        colorPickerView.setCancelable(false);
        RxView.clicks(ivUndo)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        paletteView.undo();
                    }
                });
        RxView.clicks(ivRedo)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        paletteView.redo();
                    }
                });
        RxView.clicks(ivPen)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        ivPen.setSelected(true);
                        ivEraser.setSelected(false);
                        paletteView.setMode(PaletteView.Mode.DRAW);
                    }
                });
        RxView.clicks(ivEraser)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        ivEraser.setSelected(true);
                        ivPen.setSelected(false);
                        paletteView.setMode(PaletteView.Mode.ERASER);
                    }
                });
        RxView.clicks(ivClear)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        paletteView.clear();
                    }
                });
        RxView.clicks(ivSave)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        saveImage();
                    }
                });
        RxView.clicks(ivColor)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        colorPickerView.show();
                        PaintActivity.this.hideButton();
                    }
                });
        RxView.clicks(ivPaintBack)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        new MaterialDialog.Builder(PaintActivity.this)
                                .title("确认退出？")
                                .positiveText("确认")
                                .negativeText("取消")
                                .cancelable(false)
                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                        PaintActivity.this.hideButton();
                                    }
                                })
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        finish();
                                    }
                                })
                                .show();
                    }
                });
        RxView.clicks(ivTemplate)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Glide.with(PaintActivity.this)
                                .load(getResources().getIdentifier(getRandomTemplate(), "mipmap", getPackageName()))
                                .into(ivTemplate);
                    }
                });
    }

    private static void scanFile(Context context, String filePath) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(new File(filePath)));
        context.sendBroadcast(scanIntent);
    }

    public void saveImage() {
        Observable.just(0)
                .subscribeOn(Schedulers.io())
                .compose(this.<Integer>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        progressDialog.show();
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        Bitmap bm = paletteView.buildBitmap();
                        return BitmapUtils.saveImage(bm, 100);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        scanFile(PaintActivity.this, s);
                        progressDialog.dismiss();
                        Toasty.success(LowPolyWorldApp.getInstance().getApplicationContext(), "画板已保存", 0, true).show();
                        PaintActivity.this.hideButton();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        progressDialog.dismiss();
                        Toasty.error(LowPolyWorldApp.getInstance().getApplicationContext(), "保存失败", 0, true).show();
                        PaintActivity.this.hideButton();
                    }
                });

    }

    @Override
    public void updateTemplate(List<String> templateList) {
        this.templateList = templateList;
        Glide.with(this)
                .load(getResources().getIdentifier(getRandomTemplate(), "mipmap", getPackageName()))
                .into(ivTemplate);
    }

    private String getRandomTemplate() {
        if (templateList.size() != 0) {
            Random random = new Random();
            int n = random.nextInt(templateList.size());
            return templateList.get(n);
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideButton();
    }

    @Override
    public void onUndoRedoStatusChanged() {
        ivUndo.setEnabled(paletteView.canUndo());
        ivRedo.setEnabled(paletteView.canRedo());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
