package lanou.yyyydemo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


/**
 * Created by dllo on 16/11/21.
 */

public abstract class BaseActivity  extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定布局
        setContentView(getLayout());
        initViews();
        initData();
    }

    protected abstract int getLayout();
    protected abstract void initViews();
    protected abstract void initData();





    protected <T extends View>T bindView(int id){
        return (T) findViewById(id);
    }

    protected <T extends View>T bindView(View view,int id){
        return (T) view.findViewById(id);
    }

    protected void setClick(View.OnClickListener clickListener,View...views){
        for (View view : views) {
            view.setOnClickListener(clickListener);
        }
    }

}
