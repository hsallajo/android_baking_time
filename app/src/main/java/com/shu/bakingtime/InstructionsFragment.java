package com.shu.bakingtime;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shu.bakingtime.model.Step;

import org.parceler.Parcels;

import static com.shu.bakingtime.RecipeActivity.EXT_STEP_DATA;

public class InstructionsFragment extends Fragment {

    public static final String TAG = InstructionsFragment.class.getSimpleName();
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

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
