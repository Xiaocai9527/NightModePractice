package com.xiaocai.nightmode;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.xiaocai.nightmode.colorful.Colorful;
import com.xiaocai.nightmode.colorful.setter.ViewGroupSetter;
import com.xiaocai.nightmode.dummy.DummyContent;

import java.util.List;

/**
 * Created by 肖坤 on 2017/9/12.
 * 公司：依迅北斗
 * 邮箱：838494268@qq.com
 */
public class ItemListActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private Colorful colorful;
    private SharedPreferences mPref;
    public static final String NIGHT = "night";
    public static final String NIGHT_MODE = "data_day_night_mode";
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;

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
        setContentView(R.layout.activity_item_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                changeThemeWithColorful();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.item_list);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        // 初始化Colorful
        setupColorful();
    }

    /**
     * 初始化toolbar
     */
    private void initToolbar()
    {
        toolbar.setTitle(getTitle());
        toolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.more));
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.about:
                        Toast.makeText(ItemListActivity.this, "关于我", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ItemListActivity.this, AboutActivity.class);
                        startActivity(intent);
                        break;
                    default:

                        break;
                }
                return false;
            }
        });
    }

    /**
     * 初始化
     */
    private void setupColorful()
    {
        ViewGroupSetter recyclerViewSetter = new ViewGroupSetter(recyclerView, R.attr.root_view_bg);
        ViewGroupSetter toolbarSetter = new ViewGroupSetter(toolbar, R.attr.primary_color);
        recyclerViewSetter.childViewTextColor(R.id.content, R.attr.text_color);
        recyclerViewSetter.childViewTextColor(R.id.id, R.attr.text_color);
        colorful = new Colorful.Builder(this)
                .setter(toolbarSetter)
                .setter(recyclerViewSetter)//作为recyclerview的子视图，必须在前面设置；否则recyclerview的背景图变化无效
//                .backgroundColor(R.id.item_list, R.attr.root_view_bg)//前面设置了，这里就不必要设置了
                .create();
    }

    /**
     * 改变theme
     */
    private void changeThemeWithColorful()
    {
        if (!mPref.getBoolean(NIGHT, false))
        {
            animChangeColor(R.style.AppNightTheme);
            StatusBarUtil.setColor(this, getResources().getColor(R.color.night_primary_color), 0);
            mPref.edit().putBoolean(NIGHT, true).commit();
        } else
        {
            animChangeColor(R.style.AppDayTheme);
            StatusBarUtil.setColor(this, getResources().getColor(R.color.day_primary_color), 0);
            mPref.edit().putBoolean(NIGHT, false).commit();
        }
    }

    /**
     * 给夜间模式增加一个动画,颜色渐变
     *
     * @param newTheme
     */
    private void animChangeColor(final int newTheme)
    {
        final View rootView = getWindow().getDecorView();
        rootView.setDrawingCacheEnabled(true);
        rootView.buildDrawingCache(true);

        final Bitmap localBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        if (null != localBitmap && rootView instanceof ViewGroup)
        {
            final View tmpView = new View(this);
            tmpView.setBackgroundDrawable(new BitmapDrawable(getResources(), localBitmap));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ((ViewGroup) rootView).addView(tmpView, params);
            tmpView.animate().alpha(0).setDuration(400).setListener(new Animator.AnimatorListener()
            {
                @Override
                public void onAnimationStart(Animator animation)
                {
                    colorful.setTheme(newTheme);
                    System.gc();
                }

                @Override
                public void onAnimationEnd(Animator animation)
                {
                    ((ViewGroup) rootView).removeView(tmpView);
                    localBitmap.recycle();
                }

                @Override
                public void onAnimationCancel(Animator animation)
                {

                }

                @Override
                public void onAnimationRepeat(Animator animation)
                {

                }
            }).start();
        }
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView)
    {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>
    {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items)
        {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position)
        {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view)
            {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString()
            {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
