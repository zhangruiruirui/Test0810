package lanou.loadingbtn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LoadingBtn mMainBtn;
    private boolean flag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainBtn = (LoadingBtn) findViewById(R.id.main_btn);
        mMainBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (flag) {

        mMainBtn.startLoading();
        }else {
            mMainBtn.startError();
        }
        flag = !flag;
    }
}
