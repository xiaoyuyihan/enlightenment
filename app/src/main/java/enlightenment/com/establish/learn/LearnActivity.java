package enlightenment.com.establish.learn;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

import enlightenment.com.base.AppActivity;
import enlightenment.com.base.R;
import enlightenment.com.module.ModuleChildBean;

/**
 * Created by lw on 2017/10/10.
 */

public class LearnActivity extends AppActivity implements OnMenuItemClickListener,
        OnMenuItemLongClickListener {

    public static final int LEARN_TYPE_LEARN = 1;
    public static final int LEARN_TYPE_CREATE = 2;

    private int writeFlag;
    public static final String WRITE_FLAG = "PostsWriteActivity";
    private FragmentManager fragmentManager;
    private Toolbar mToolbar;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private ProjectDialogFragment projectDialog;
    private AddProjectModelFragment addProjectModelFragment;
    private NewsProjectModelFragment newsProjectModelFragment;
    private TextView mToolBarTextView;

    private ProjectDialogFragment.ProjectBean mProjectBean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_write);

        writeFlag = getIntent().getExtras().getInt(LearnActivity.WRITE_FLAG);
        fragmentManager = getSupportFragmentManager();
        showProjectDialog();
        initToolbar();
        initMenuFragment();
    }

    private void showProjectDialog() {
        projectDialog = ProjectDialogFragment.newInstance(writeFlag);
        projectDialog.setItemHolderClick(new ProjectDialogFragment.OnDialogClick() {
            @Override
            public void onItemHolderClick(ProjectDialogFragment.ProjectBean projectBean) {
                mProjectBean = projectBean;
                mToolBarTextView.setText(mProjectBean.getName());
                projectDialog.dismiss();
            }

            @Override
            public void onAddHolderClick() {
                showAddProjectDialog();
            }

            @Override
            public void onBack() {
                finish();
            }
        });
        projectDialog.show(fragmentManager, "NewTypeFragmentDialog");
    }

    private void showAddProjectDialog() {
        addProjectModelFragment=new AddProjectModelFragment(new AddProjectModelFragment.OnClickListener() {
            @Override
            public void Back() {
                addProjectModelFragment.dismiss();
            }

            @Override
            public void ItemChildModuleBean(ModuleChildBean childBeanList) {
                addProjectModelFragment.dismiss();
                showNewsProjectDialog();
            }
        });
        addProjectModelFragment.show(fragmentManager, "AddProjectModelFragment");
    }

    private void showNewsProjectDialog() {
        newsProjectModelFragment=new NewsProjectModelFragment(new AddProjectModelFragment.OnClickListener() {
            @Override
            public void Back() {
                newsProjectModelFragment.dismiss();
            }

            @Override
            public void ItemChildModuleBean(ModuleChildBean childBeanList) {

            }
        });
        newsProjectModelFragment.show(fragmentManager,"NewsProjectModelFragment");
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_top);
        mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();//回退键
            }
        });
        mToolBarTextView.setText("Android开发实录");
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_write, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_write_add:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
            case R.id.action_write_subject:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * You can use any [resource, bitmap, drawable, color] as image:
     * item.setResource(...)
     * item.setBitmap(...)
     * item.setDrawable(...)
     * item.setColor(...)
     * You can set image ScaleType:
     * item.setScaleType(ScaleType.FIT_XY)
     * You can use any [resource, drawable, color] as background:
     * item.setBgResource(...)
     * item.setBgDrawable(...)
     * item.setBgColor(...)
     * You can use any [color] as text color:
     * item.setTextColor(...)
     * You can set any [color] as divider color:
     * item.setDividerColor(...)
     *
     * @return
     */
    private List<MenuObject> getMenuObjects() {


        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.ic_close);

        MenuObject send = new MenuObject("选择下划线颜色");
        send.setResource(R.drawable.ic_change_color);

        MenuObject like = new MenuObject("长按进入拍照，单击选择照片");
        like.setResource(R.drawable.ic_add_picture);

        MenuObject addFr = new MenuObject("长按进入录像，单击选择视频");
        addFr.setResource(R.drawable.ic_add_video);

        MenuObject addFav = new MenuObject("长按进入录音，单击选择音频");
        addFav.setResource(R.drawable.ic_add_audio);

        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(like);
        menuObjects.add(addFr);
        menuObjects.add(addFav);
        return menuObjects;
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {

    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {

    }
}
