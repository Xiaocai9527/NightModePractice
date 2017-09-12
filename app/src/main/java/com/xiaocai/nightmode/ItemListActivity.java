package com.xiaocai.nightmode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaocai.nightmode.colorful.Colorful;
import com.xiaocai.nightmode.colorful.setter.ViewGroupSetter;
import com.xiaocai.nightmode.dummy.DummyContent;
import com.jaeger.library.StatusBarUtil;

import java.util.List;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity
{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RecyclerView recyclerView;
    private Colorful colorful;
    private SharedPreferences mPref;
    public static final String NIGHT = "night";
    public static final String NIGHT_MODE = "data_day_night_mode";
    private Toolbar toolbar;

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
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                changeThemeWithColorful();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView(recyclerView);

        if (findViewById(R.id.item_detail_container) != null)
        {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        // 初始化Colorful
        setupColorful();
    }

    private void changeThemeWithColorful()
    {
        if (!mPref.getBoolean(NIGHT, false))
        {
            colorful.setTheme(R.style.AppNightTheme);
            StatusBarUtil.setColor(this, getResources().getColor(R.color.night_primary_color), 0);
            mPref.edit().putBoolean(NIGHT, true).commit();
        } else
        {
            colorful.setTheme(R.style.AppDayTheme);
            StatusBarUtil.setColor(this, getResources().getColor(R.color.day_primary_color), 0);
            mPref.edit().putBoolean(NIGHT, false).commit();
        }
    }

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
                    if (mTwoPane)
                    {
                        Bundle arguments = new Bundle();
                        arguments.putString(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        ItemDetailFragment fragment = new ItemDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.item_detail_container, fragment)
                                .commit();
                    } else
                    {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ItemDetailActivity.class);
                        intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
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
