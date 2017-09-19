package com.xiaocai.nightmode;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.xiaocai.nightmode.about.AbsAboutActivity;
import com.xiaocai.nightmode.about.Card;
import com.xiaocai.nightmode.about.Category;
import com.xiaocai.nightmode.about.Contributor;
import com.xiaocai.nightmode.about.Line;
import com.xiaocai.nightmode.colorful.Colorful;

import me.drakeet.multitype.Items;

import static com.xiaocai.nightmode.ItemListActivity.NIGHT;
import static com.xiaocai.nightmode.ItemListActivity.NIGHT_MODE;

/**
 * Created by 肖坤 on 2017/9/19.
 * company：exsun
 * email：838494268@qq.com
 */

public class AboutActivity extends AbsAboutActivity
{
    private SharedPreferences mPref;
    private Colorful colorful;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        mPref = getSharedPreferences(NIGHT_MODE, MODE_PRIVATE);
        boolean flag = mPref.getBoolean(NIGHT, false);
        if (!flag)
        {
            setTheme(R.style.AppDayTheme);
            StatusBarUtil.setColor(this, getResources().getColor(R.color.day_primary_color), 0);
        } else
        {
            setTheme(R.style.AppNightTheme);
            StatusBarUtil.setColor(this, getResources().getColor(R.color.night_primary_color), 0);
        }
        super.onCreate(savedInstanceState);
        // 初始化Colorful
//        setupColorful();//当前页面有夜间模式按钮才会初始化这个
    }

    private void setupColorful()
    {
//        ViewGroupSetter linearSetter = new ViewGroupSetter(headerContentLayout, R.attr.primary_color);
//        ViewGroupSetter collapsingtoolbarSetter = new ViewGroupSetter(collapsingToolbar, R.attr.primary_color);
//        colorful = new Colorful.Builder(this)
//                .setter(collapsingtoolbarSetter)
//                .setter(linearSetter)
//                .create();
    }

    @Override
    protected void onCreateHeader(@NonNull ImageView icon, @NonNull TextView slogan, @NonNull TextView version)
    {
        icon.setImageResource(R.mipmap.ic_launcher);
        slogan.setText("肖坤");
        version.setText("v" + BuildConfig.VERSION_NAME);
    }

    @Override
    protected void onItemsCreated(@NonNull Items items)
    {
        items.add(new Category("介绍与帮助"));
        items.add(new Card(getString(R.string.card_content)));
        items.add(new Line());

        items.add(new Category("Developers"));
        items.add(new Contributor(R.mipmap.ic_launcher, "xiaokun", "Developer & designer", "https://github.com/xiaokun19931126"));
    }


}
