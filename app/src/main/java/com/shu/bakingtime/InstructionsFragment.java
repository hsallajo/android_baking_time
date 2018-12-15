package com.shu.bakingtime;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shu.bakingtime.model.Step;

import org.parceler.Parcels;

import static com.shu.bakingtime.RecipeActivity.EXT_STEP_DATA;

public class InstructionsFragment extends Fragment {

    private Step mStep;

    public InstructionsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(EXT_STEP_DATA)) {

            mStep = Parcels.unwrap(getArguments().getParcelable(EXT_STEP_DATA));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mStep.getShortDescription());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.instructions, container, false);

        if (mStep != null) {
            ((TextView) rootView.findViewById(R.id.tv_instruction)).setText(mStep.getDescription());
        }

        return rootView;
    }
}
