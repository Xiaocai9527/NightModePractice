package com.xiaocai.nightmode;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaocai.nightmode.colorful.Colorful;
import com.xiaocai.nightmode.dummy.DummyContent;

import static android.content.Context.MODE_PRIVATE;
import static com.xiaocai.nightmode.ItemListActivity.NIGHT;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment
{
    private SharedPreferences mPref;
    private Colorful colorful;
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        mPref = getActivity().getSharedPreferences("data_day_night_mode", MODE_PRIVATE);
        if (!mPref.getBoolean(NIGHT, false))
        {
            getActivity().setTheme(R.style.AppDayTheme);
        } else
        {
            getActivity().setTheme(R.style.AppNightTheme);
        }
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID))
        {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null)
            {
                appBarLayout.setTitle(mItem.content);
            }
        }
        // 初始化Colorful
        setupColorful();
    }

    private void setupColorful()
    {
//        if (mPref.getBoolean(NIGHT, false))
//        {
//            colorful.setTheme(R.style.AppDayTheme);
//        } else
//        {
//            colorful.setTheme(R.style.AppNightTheme);
//        }
//        ViewGroupSetter recyclerViewSetter = new ViewGroupSetter(recyclerView, 0);
//        recyclerViewSetter.childViewTextColor(R.id.content, R.attr.text_color);
////        recyclerViewSetter.childViewTextColor(R.id.id, R.attr.text_color);
//        colorful = new Colorful.Builder(this)
//                .backgroundColor(R.id.item_list, R.attr.root_view_bg)
//                .setter(recyclerViewSetter)
//                .create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null)
        {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.details);
        }

        return rootView;
    }
}
