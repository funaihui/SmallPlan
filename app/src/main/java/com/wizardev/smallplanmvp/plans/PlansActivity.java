package com.wizardev.smallplanmvp.plans;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wizardev.smallplanmvp.R;
import com.wizardev.smallplanmvp.about.AboutActivity;
import com.wizardev.smallplanmvp.adapter.BaseAdapter;
import com.wizardev.smallplanmvp.adapter.BaseViewHolder;
import com.wizardev.smallplanmvp.adapter.SimpleAdapter;
import com.wizardev.smallplanmvp.addplan.AddPlanActivity;
import com.wizardev.smallplanmvp.data.Plan;
import com.wizardev.smallplanmvp.selectpicture.SelectPictureActivity;
import com.wizardev.smallplanmvp.utils.BitmapUtils;
import com.wizardev.smallplanmvp.utils.SPUtils;
import com.wizardev.smallplanmvp.widget.CircularAnim;
import com.wizardev.smallplanmvp.widget.RecycleViewEmptySupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.blurry.Blurry;


/**
 * author : wizardev
 * e-mail : wizarddev@163.com
 * time   : 2017/10/25
 * desc   : 展示计划的页面
 * version: 1.0
 */
public class PlansActivity extends AppCompatActivity implements PlansContract.View, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.fab_add_plan)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.plansList)
    RecycleViewEmptySupport mRecyclerView;
    private ImageView bgBlur;
    private ImageView mAvatar;
    private List<Plan> listPlan;
    private PlansContract.Presenter mPresenter;
    private ContentAdapter mAdapter;
    private static final String TAG = "PlansActivity";
    private boolean isList = true;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        PlansPresenter plansPresenter = new PlansPresenter(getApplicationContext(), this);
        setPresenter(plansPresenter);
        initData();
        initView();
    }

    private void initData() {
        listPlan = mPresenter.loadAllPlan();
    }


    private void initView() {
        setupToolbar();
        setupRecyclerView();
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        setupDrawerContent(navigationView);
        View view = navigationView.getHeaderView(0);//获得NavigationView头部的View
        //绑定头像的Image
        mAvatar = view.findViewById(R.id.avatar);
        mAvatar.setOnClickListener(this);
        bgBlur = view.findViewById(R.id.iv_main_blur);//绑定需要模糊的背景图
        setupAvatar();
        mPresenter.judgmentHiddenOrShow();
    }

    //设置recycleView的布局
    private void setupRecyclerView() {
        mRecyclerView.setEmptyView(findViewById(R.id.empty_layout));
        setLayoutManager(isList);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new ContentAdapter(this, listPlan, R.layout.recycle_view_list);
        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(PlansActivity.this, AddPlanActivity.class);
                intent.putExtra("planItem", mAdapter.getItem(position));
              //  intent.putExtra("id", plan.getId());
              //  intent.putExtra("remindDate", plan.getRemindDate());
                startActivityForResult(intent, 1);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    //设置Toolbar
    private void setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.inflateMenu(R.menu.show_list_grid);
        toolbar.setTitle("小计划");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        Menu menu = toolbar.getMenu();
        menuItem = menu.getItem(0);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_show_list_grid) {
                    isList = !isList;
                    //设置RecyclerView的布局
                    setLayoutManager(isList);
                }
                return false;
            }
        });
    }

    private void setLayoutManager(boolean isList) {
        if (isList) {
            menuItem.setIcon(R.drawable.ic_grid);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(PlansActivity.this));
        } else {
            menuItem.setIcon(R.drawable.ic_list);
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }
    }

    private void setupAvatar() {
        //设置头像
        String picPath = SPUtils.getString(getApplicationContext(), "", "");
        if (TextUtils.isEmpty(picPath)) {
            Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.def_pic, 100, 100);
            blurPicture(bitmap);
        } else {
            Bitmap bitmap = BitmapFactory.decodeFile(picPath);
            blurPicture(bitmap);
        }
    }

    //模糊头像的背景图片
    private void blurPicture(Bitmap bitmap) {
        Blurry.with(this)
                .radius(10)
                .sampling(8)
                .async()
                //.capture(findViewById(R.id.iv_main_blur))
                .from(bitmap)
                .into(bgBlur);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_home:
                                floatingActionButton.setVisibility(View.VISIBLE);
                                refreshAdapter();
                                break;
                            case R.id.menu_done:
                                listPlan = mPresenter.loadFinishPlan();
                                floatingActionButton.setVisibility(View.GONE);
                                setupRecyclerView();
                                break;
                            case R.id.menu_event:
                                listPlan = mPresenter.loadUnFinishPlan();
                                floatingActionButton.setVisibility(View.GONE);
                                setupRecyclerView();
                                break;
                            case R.id.menu_setting:
                                // menuItem.setChecked(true);
                                //  Toast.makeText(MainActivity.this, "" + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.menu_about:

                                Intent intent = new Intent(PlansActivity.this, AboutActivity.class);
                                startActivity(intent);
                                break;
                            case R.id.menu_help:
                                // Toast.makeText(MainActivity.this, "" + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public void setPresenter(PlansContract.Presenter presenter) {
        if (presenter != null) {
            mPresenter = presenter;
        }
    }

    @Override
    public void showAvatar() {

    }

    @Override
    public void showPopupWindow(PopupMenu popupMenu) {
        popupMenu.show();
    }

    @Override
    public void dismissPopupWindow(PopupMenu popupMenu) {
        if (popupMenu != null) {
            popupMenu.dismiss();
        }
    }

    @Override
    public void refreshAdapter() {
        listPlan = mPresenter.loadAllPlan();
        mPresenter.judgmentHiddenOrShow();
        setupRecyclerView();
    }

    @Override
    public void menuShowOrHidden() {
        if (listPlan.size() == 0) {
            menuItem.setVisible(false);
        } else {
            menuItem.setVisible(true);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatar:
                Intent intent = new Intent(this, SelectPictureActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshAdapter();
    }

    @OnClick({R.id.fab_add_plan})
    public void clickAction(View view) {
        switch (view.getId()) {
            case R.id.fab_add_plan:
                CircularAnim.fullActivity(PlansActivity.this, view)
                        .colorOrImageRes(R.color.colorPrimary)
                        .duration(500)
                        .go(new CircularAnim.OnAnimationEndListener() {
                            @Override
                            public void onAnimationEnd() {
                                Intent intent = new Intent(PlansActivity.this, AddPlanActivity.class);
                                startActivityForResult(intent, 0);
                            }
                        });
                break;
        }
    }

    class ContentAdapter extends SimpleAdapter<Plan> {
        public ContentAdapter(Context mContext, List<Plan> list, int mResId) {
            super(mContext, list, mResId);
        }

        @Override
        public void bindView(BaseViewHolder viewHolder, final Plan item) {
            TextView planContent = viewHolder.getTextView(R.id.planContent);
            if (item.getFlag() == 0) {
                //说明已经完成，则为文字添加删除线
                planContent.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                planContent.setTextColor(Color.parseColor("#5e5e5e"));
            }
            planContent.setText(item.getSomething());
            viewHolder.getTextView(R.id.planBuildTime).setText(item.getTime());
            final ImageView imageView = (ImageView) viewHolder.getView(R.id.planMenu);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.initPopupWindow(PlansActivity.this, imageView, item);
                }
            });
        }
    }
}
