package com.shu.bakingtime;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

    private static final String CURRENT_STEP_DATA = "current_step_data";
    private Step mStep;

    public InstructionsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(EXT_STEP_DATA)) {
            mStep = Parcels.unwrap(getArguments().getParcelable(EXT_STEP_DATA));
        }

        if(savedInstanceState != null){
            mStep = Parcels.unwrap(savedInstanceState.getParcelable(CURRENT_STEP_DATA));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.instructions, container, false);

        if (mStep != null) {
            TextView mInstructions = rootView.findViewById(R.id.tv_instructions_tablet);
            mInstructions.setText(mStep.getDescription());
        }
        return rootView;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT_STEP_DATA, Parcels.wrap(mStep));
    }
}
