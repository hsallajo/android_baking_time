package com.shu.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.shu.bakingtime.model.Ingredient;
import com.shu.bakingtime.model.Recipe;
import com.shu.bakingtime.model.Step;

import org.parceler.Parcels;

import java.util.List;

import static com.shu.bakingtime.RecipesMainActivity.EXTRA_RECIPE;

public class RecipeActivity extends AppCompatActivity {

    public static final String TAG = RecipeActivity.class.getSimpleName();
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_ITEM = 0;
    public static final String EXTRA_NEXT_PREV_CLICK_EVENT = "NEXT_PREV_CLICK_EVENT";
    public static final int STEP_ACTIVITY_REQUEST_RESULT = 66;
    public static final String EXTRA_IS_NEXT_STEP = "EXTRA_IS_NEXT_STEP";
    public static final String EXTRA_IS_PREV_STEP = "EXTRA_IS_PREV_STEP";

    private boolean mTwoPane;
    private RecipeStepsRecyclerViewAdapter mAdapter;
    private Recipe mRecipeData;
    private int mCurrentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.activity_recipe);

        if (getIntent().hasExtra(EXTRA_RECIPE)) {
            Parcelable p = getIntent().getParcelableExtra(EXTRA_RECIPE);
            mRecipeData = Parcels.unwrap(p);

            mCurrentStep = 0;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mRecipeData.getName());
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.step_container) != null) {
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.recipe_step_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new RecipeStepsRecyclerViewAdapter(this
                , mRecipeData.getSteps()
                , mTwoPane);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == STEP_ACTIVITY_REQUEST_RESULT) {

            if (resultCode == RESULT_OK) {
                int click_event = data.getIntExtra(EXTRA_NEXT_PREV_CLICK_EVENT, 0);

                if (click_event == 2) {
                    previousStep();
                    return;
                }
                if (click_event == 3) {
                    nextStep();
                    return;
                }
            } }
    }

    private void setupStepActivity(Step data) {

        Intent intent = new Intent(this, StepActivity.class);
        intent.putExtra(StepFragment.ARG_STEP_DETAIL, Parcels.wrap(data));
        intent.putExtra(EXTRA_IS_NEXT_STEP, isNextStep());
        intent.putExtra(EXTRA_IS_PREV_STEP, isPreviousStep());
        startActivityForResult(intent, 66);

    }

    private void setupStepFragment(Step data) {

        Bundle arguments = new Bundle();
        arguments.putParcelable(StepFragment.ARG_STEP_DETAIL, Parcels.wrap(data));
        arguments.putBoolean(EXTRA_IS_NEXT_STEP, isNextStep());
        arguments.putBoolean(EXTRA_IS_PREV_STEP, isPreviousStep());

        StepFragment fragment = new StepFragment();
        fragment.setArguments(arguments);
        this.getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_container, fragment)
                .commit();


        PlayerFragment playerFragment = new PlayerFragment();
        playerFragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.player_fragment_container, playerFragment)
                .commit();
    }

    private void previousStep(){
        if(mCurrentStep - 1 < 0)
            return;

        mCurrentStep -= 1;
        setupStepActivity( mRecipeData.getSteps().get( mCurrentStep ));
    }

    private void nextStep(){
        if(mCurrentStep + 1 >= mRecipeData.getSteps().size())
            return;

        mCurrentStep += 1;
        setupStepActivity( mRecipeData.getSteps().get( mCurrentStep ));
    }

    private boolean isPreviousStep() {
        return mCurrentStep > 0 ? true : false;
    }

    private boolean isNextStep() {
        return mCurrentStep < (mRecipeData.getSteps().size() - 1) ? true : false;
    }

    public class RecipeStepsRecyclerViewAdapter
            extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final RecipeActivity mParentActivity;
        private final List<Step> mSteps;
        private final boolean mTwoPane;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Step stepData = (Step) view.getTag();
                mCurrentStep = stepData.getId();

                if (mTwoPane) {
                    setupStepFragment(stepData);
                    notifyDataSetChanged();
                } else {
                    setupStepActivity(stepData);
                }
            }
        };

        RecipeStepsRecyclerViewAdapter(RecipeActivity parent,
                                       List<Step> steps,
                                       boolean twoPane) {
            mSteps = steps;
            mParentActivity = parent;
            mTwoPane = twoPane;

            mCurrentStep = 0;
            if (mTwoPane) {
                Step stepData = mRecipeData.getSteps().get(mCurrentStep);
                setupStepFragment(stepData);
            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            /* TYPE_ITEM */
            if (viewType == TYPE_ITEM) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recipe_item, parent, false);
                return new ItemViewHolder(view);
            }
            /* TYPE_HEADER */
            else {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recipe_item_header, parent, false);
                return new HeaderViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: ");

            if (position == 0
                    && (holder.getItemViewType() == TYPE_HEADER)) {
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                headerViewHolder.bind(position);
            } else {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                itemViewHolder.bind(position);
                itemViewHolder.itemView.setTag(mSteps.get(position - 1));
                itemViewHolder.itemView.setOnClickListener(mOnClickListener);

                if (mCurrentStep == (position - 1)){
                    itemViewHolder.itemView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                } else{
                    itemViewHolder.itemView.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                }
        }

    }

    @Override
    public int getItemCount() {
        return mSteps.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }


    class HeaderViewHolder extends RecyclerView.ViewHolder {

        final ListView mIngredientView;
        final IngredientAdapter mIngredientAdapter;
        final List<Ingredient> mIngredients;

        public void bind(int position) {
        }

        HeaderViewHolder(View view) {
            super(view);
            mIngredientView = (ListView) view.findViewById(R.id.lv_ingredients);
            mIngredients = mRecipeData.getIngredients();
            mIngredientAdapter = new IngredientAdapter(getApplicationContext(), mIngredients);
            mIngredientView.setAdapter(mIngredientAdapter);

            int rowHeight, listHeight = 0;
            for (int i = 0; i < mIngredientAdapter.getCount(); i++) {
                View v = mIngredientAdapter.getView(i, null, mIngredientView);
                if (v != null) {
                    v.measure(
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    rowHeight = v.getMeasuredHeight();
                    listHeight = listHeight + rowHeight;
                    //Log.d(TAG, "HeaderViewHolder: rowHeight is " + rowHeight);
                }
            }
            listHeight = listHeight + mIngredientView.getDividerHeight() * mIngredients.size();
            //Log.d(TAG, "HeaderViewHolder: total height is " + listHeight);

            ViewGroup.LayoutParams p = mIngredientView.getLayoutParams();
            p.height = listHeight;
            mIngredientView.setLayoutParams(p);
            mIngredientView.requestLayout();
        }

        class IngredientAdapter extends BaseAdapter {

            private final List<Ingredient> mIngredients;
            private Context context;

            public IngredientAdapter(Context context, List<Ingredient> ingredients) {
                super();
                this.mIngredients = ingredients;
                this.context = context;
            }

            @Override
            public int getCount() {
                return mIngredients.size();
            }

            @Override
            public Object getItem(int position) {
                return mIngredients.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                if (convertView == null) {
                    convertView = LayoutInflater.from(context).
                            inflate(R.layout.ingredient, parent, false);
                }

                Ingredient currentItem = mIngredients.get(position);

                TextView ingredientView =
                        convertView.findViewById(R.id.tv_ingredient);
                TextView quantityView =
                        convertView.findViewById(R.id.tv_quantity);
                TextView measureView =
                        convertView.findViewById(R.id.tv_measure);

                ingredientView.setText(currentItem.getIngredient());
                quantityView.setText(Float.toString(currentItem.getQuantity()));
                measureView.setText(currentItem.getMeasure());

                return convertView;
            }
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        final TextView mStepId;
        final TextView mStepBrief;

        public void bind(int position) {
            mStepId.setText(Integer.toString(mSteps.get(position - 1).getId()));
            Log.d(TAG, "bind: " + Integer.toString(mSteps.get(position - 1).getId()));
            mStepBrief.setText(mSteps.get(position - 1).getShortDescription());
            Log.d(TAG, "bind: " + mSteps.get(position - 1).getShortDescription());
        }

        private ItemViewHolder(View view) {
            super(view);
            mStepId = (TextView) view.findViewById(R.id.tv_step_number);
            mStepBrief = (TextView) view.findViewById(R.id.tv_step_brief);
        }
    }
}
}
