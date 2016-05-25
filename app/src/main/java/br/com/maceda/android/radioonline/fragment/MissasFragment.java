package br.com.maceda.android.radioonline.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.maceda.android.radioonline.R;
import br.com.maceda.android.radioonline.adapter.MissaAdapter;
import br.com.maceda.android.radioonline.util.Util;
import br.com.maceda.android.radioonline.vo.Missa;
import livroandroid.lib.fragment.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class MissasFragment extends BaseFragment {

    private RecyclerView recycleView;

    public MissasFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_missas, container, false);
        recycleView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleView.setItemAnimator(new DefaultItemAnimator());
        recycleView.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        carregaMissas();
    }

    private void carregaMissas() {
        startTask("missas", new GetMissasTask(), R.id.progress);
    }

    private class GetMissasTask implements BaseFragment.TaskListener<List<Missa>> {

        @Override
        public List<Missa> execute() throws Exception {
            return Util.getJsonFromArquivo(getContext());
        }

        @Override
        public void updateView(List<Missa> missas) {
            if (missas != null) {
                recycleView.setAdapter(new MissaAdapter(getContext(), missas));
            }
        }

        @Override
        public void onError(Exception exception) {
            alert(getContext().getString(R.string.ocorreu_erro_buscar_dados));
        }

        @Override
        public void onCancelled(String cod) {

        }
    }

}
