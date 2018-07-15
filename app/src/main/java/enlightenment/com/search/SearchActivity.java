package enlightenment.com.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import enlightenment.com.base.AppActivity;
import enlightenment.com.base.R;
import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.SearchView;
import scut.carson_ho.searchview.bCallBack;

/**
 * Created by lw on 2017/8/24.
 */

public class SearchActivity extends AppActivity {
    public SearchView mSearchView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void init() {
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAciton() {
                finish();
            }
        });
        mSearchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAciton(String string) {
                Toast.makeText(SearchActivity.this, string, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initData() {

    }
}
