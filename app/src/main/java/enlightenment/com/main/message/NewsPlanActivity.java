package enlightenment.com.main.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TimePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import enlightenment.com.base.AppActivity;
import enlightenment.com.base.R;

/**
 * Created by admin on 2018/7/28.
 */

public class NewsPlanActivity extends AppActivity {
    @BindView(R.id.timePicker)
    TimePicker timePicker;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_plan;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this);
        timePicker.setIs24HourView(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void clearData() {

    }
}
