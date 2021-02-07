package com.ytempest.wanandroid.activity.main;

import androidx.fragment.app.Fragment;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ytempest.layoutinjector.annotation.InjectLayout;
import com.ytempest.tool.helper.ExpandFragHelper;
import com.ytempest.wanandroid.R;
import com.ytempest.wanandroid.base.activity.MvpActivity;

import butterknife.BindView;

@InjectLayout(R.layout.activity_main)
public class MainActivity extends MvpActivity<MainPresenter> implements IMainContract.View {

    @BindView(R.id.navigationBar_main_content)
    BottomNavigationBar mNavigationBar;

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        initTabs();
    }


    /*Tabs*/

    private ExpandFragHelper mFragHelper;
    private Fragment mCurFrag;

    private void initTabs() {
        mNavigationBar.clearAll();
        mNavigationBar.setTabSelectedListener(new BottomNavigationBar.SimpleOnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                super.onTabSelected(index);
                MainTab tab = MainTab.values()[index];
                switchFrag(tab.id);
            }
        });
        for (MainTab tab : MainTab.values()) {
            mNavigationBar.addItem(new BottomNavigationItem(tab.iconSelected, tab.title)
                    .setInactiveIconResource(tab.iconNormal));
        }
        mNavigationBar.initialise();

        switchFrag(MainTab.Id.HOME);
    }

    private void switchFrag(@MainTab.Id int id) {
        if (mFragHelper == null) {
            mFragHelper = new ExpandFragHelper(getSupportFragmentManager(), new MainFragConstructor());
        }
        mCurFrag = mFragHelper.switchFrag(R.id.frameLayout_main_content_container, mCurFrag, id);
    }
}