package com.noeasy.lcp;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.ImageView;

        import com.bumptech.glide.Glide;
        import com.bumptech.glide.load.engine.DiskCacheStrategy;

        import org.jsoup.Jsoup;
        import org.jsoup.nodes.Document;
        import org.jsoup.select.Elements;

        import java.io.IOException;
        import java.util.regex.Pattern;

public class SplashActivity extends AppCompatActivity {

    private final String APP_VERSION_NAME = BuildConfig.VERSION_NAME;
    private final String APP_PACKAGE_NAME = BuildConfig.APPLICATION_ID;
    private final int LOAD_SUCCESS = 1001;
    private final String STORE_URL = "https://play.google.com/store/apps/details?id=com.seongju.mrmmsborderapk";
    private AlertDialog.Builder mDialog ;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView = findViewById(R.id.splashimageView);

        Glide.with(this)
                .asGif()
                .load(R.drawable.mrm_splash)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView);
        ImageView imageView2 = findViewById(R.id.splashLogoImage);
        Glide.with(this)
                .load(R.drawable.mrm_hdlogo)
                .override(720)
                .into(imageView2);
        Animation anim = AnimationUtils.loadAnimation
                (getApplicationContext(),
                        R.anim.translate_anim);

        imageView2.startAnimation(anim);
        mDialog = new AlertDialog.Builder(this);
        startLoginActivity();
        //checkVersion(STORE_URL);
    }
    private void checkVersion(String STORE_URL){
        Thread thread = new Thread(() -> {
            try{
                Document doc = Jsoup.connect(STORE_URL).get();

                Elements Version = doc.select(".htlgb");
                Boolean oneCheck =true;
                for (int i = 0; i < Version.size(); i++) {

                    String VersionMarket = Version.get(i).text();

                    if (Pattern.matches("^[0-9]{1}.[0-9]{1}.[0-9]{1}$", VersionMarket) && oneCheck) {

                        Message message = mHandler.obtainMessage(LOAD_SUCCESS, VersionMarket);
                        mHandler.sendMessage(message);
                        oneCheck=false;
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        });

        thread.start();
    }
    private final SplashHandler mHandler = new SplashHandler();

    private class SplashHandler extends Handler {

        SplashHandler() {
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == LOAD_SUCCESS) {
                String VersionMarket = (String) msg.obj;
                if (!VersionMarket.equals(APP_VERSION_NAME)) {
                    mDialog.setMessage("최신 버전이 출시되었습니다. 업데이트 후 사용 가능합니다.")
                            .setCancelable(false)
                            .setPositiveButton("업데이트 바로가기",
                                    (dialog, id) -> {
                                        Intent marketLaunch = new Intent(
                                                Intent.ACTION_VIEW);
                                        marketLaunch.setData(Uri
                                                .parse(STORE_URL));
                                        startActivity(marketLaunch);
                                        finish();
                                    });
                    AlertDialog alert = mDialog.create();
                    alert.setTitle("업데이트 알림");
                    alert.show();
                } else {
                    startLoginActivity();
                }
            }

        }
    }


    public void startLoginActivity(){
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            //Intent intent = new Intent(SplashActivity.this, LoginActivity.class); // 로그인 View 이동
            startActivity(intent);
            finish();
        }, 1500);
    }
}
