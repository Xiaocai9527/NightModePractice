package com.xiaocai.nightmode;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.xiaocai.nightmode.colorful.Colorful;
import com.xiaocai.nightmode.colorful.setter.ViewGroupSetter;

import static com.xiaocai.nightmode.ItemListActivity.NIGHT;

/**
 * An activity representing a single Item detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ItemListActivity}.
 */
public class ItemDetailActivity extends AppCompatActivity
{
    private SharedPreferences mPref;
    private NestedScrollView scrollView;
    private Colorful colorful;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        mPref = getSharedPreferences("data_day_night_mode", MODE_PRIVATE);
        if (!mPref.getBoolean(NIGHT, false))
        {
            setTheme(R.style.AppDayTheme);
            StatusBarUtil.setColor(this, getResources().getColor(R.color.day_primary_color), 0);
        } else
        {
            setTheme(R.style.AppNightTheme);
            StatusBarUtil.setColor(this, getResources().getColor(R.color.night_primary_color), 0);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        scrollView = (NestedScrollView) findViewById(R.id.item_detail_container);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null)
        {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ItemDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ItemDetailFragment.ARG_ITEM_ID));
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }
        // 初始化Colorful
//        setupColorful();
    }

    private void setupColorful()
    {
        ViewGroupSetter setter = new ViewGroupSetter(scrollView, R.attr.root_view_bg);
        ViewGroupSetter toolbarSetter = new ViewGroupSetter(collapsingToolbarLayout, R.attr.primary_color);
//        recyclerViewSetter.childViewTextColor(R.id.content, R.attr.text_color);
//        recyclerViewSetter.childViewTextColor(R.id.id, R.attr.text_color);
        colorful = new Colorful.Builder(this)
                .setter(setter)
                .setter(toolbarSetter)
                .create();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
//            NavUtils.navigateUpTo(this, new Intent(this, ItemListActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
