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

/**
 * A fragment representing a single RecipeStep detail screen.
 */
public class StepFragment extends Fragment {

    public static final String ARG_STEP_DETAIL = "item_id";

    private Step mStep;

    public StepFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_STEP_DETAIL)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mStep = Parcels.unwrap(getArguments().getParcelable(ARG_STEP_DETAIL));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
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
