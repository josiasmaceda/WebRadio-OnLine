package br.com.maceda.android.radioonline.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.maceda.android.radioonline.R;
import br.com.maceda.android.radioonline.adapter.TabsAdapter;
import livroandroid.lib.fragment.BaseFragment;
import livroandroid.lib.utils.Prefs;

/**
 * Created by josias on 28/01/2016.
 */
public class TabFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(new TabsAdapter(getContext(), getChildFragmentManager()));

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        int cor = getContext().getResources().getColor(R.color.white);
        tabLayout.setTabTextColors(cor, cor);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.radio)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.missas)));

        // Listener para tratar eventos de clique na tab
        tabLayout.setOnTabSelectedListener(this);
        // Se mudar o ViewPager atualiza a tab Selecionada
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(Prefs.getInteger(getContext(), "tabIndice"));

        return view;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        // Se alterar a tab, atualiza o ViewPager
        viewPager.setCurrentItem(tab.getPosition());
        Prefs.setInteger(getContext(), "tabIndice", viewPager.getCurrentItem());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
