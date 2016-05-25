package br.com.maceda.android.radioonline.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import br.com.maceda.android.radioonline.R;
import br.com.maceda.android.radioonline.fragment.MissasFragment;
import br.com.maceda.android.radioonline.fragment.RadioFragment;


/**
 * Created by josias on 28/01/2016.
 */
public class TabsAdapter extends FragmentPagerAdapter {

    private Context context;

    public TabsAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return context.getString(R.string.radio);
        } else
            return context.getString(R.string.missas);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position == 0) {
            fragment = new RadioFragment();
        } else {
            fragment = new MissasFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
