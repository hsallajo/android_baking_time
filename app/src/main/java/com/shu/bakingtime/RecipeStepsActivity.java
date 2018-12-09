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
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.shu.bakingtime.model.Ingredient;
import com.shu.bakingtime.model.Recipe;
import com.shu.bakingtime.model.Step;

import org.parceler.Parcels;

import java.util.List;

import static com.shu.bakingtime.RecipesActivity.EXTRA_RECIPE;

public class RecipeStepsActivity extends AppCompatActivity {

    public static final String TAG = RecipeStepsActivity.class.getSimpleName();
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_ITEM = 0;

    private boolean mTwoPane;

    private Recipe mRecipeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.activity_recipe_steps);

        if (getIntent().hasExtra(EXTRA_RECIPE)) {
            Parcelable p = getIntent().getParcelableExtra(EXTRA_RECIPE);
            mRecipeData = Parcels.unwrap(p);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mRecipeData.getName());
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.recipe_step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
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
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(
                new RecipeStepsRecyclerViewAdapter(this
                        , mRecipeData.getSteps()
                        , mTwoPane));
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

    public class RecipeStepsRecyclerViewAdapter
            extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final RecipeStepsActivity mParentActivity;
        private final List<Step> mSteps;
        private final boolean mTwoPane;
        //private RecyclerView mRecyclerView;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Step stepData = (Step) view.getTag();

                if (mTwoPane) {
                    setupStepDetailFragment(stepData);
                } else {
                    setupStepDetailActivity(view, stepData);
                }
            }
        };

        RecipeStepsRecyclerViewAdapter(RecipeStepsActivity parent,
                                       List<Step> steps,
                                       boolean twoPane) {
            mSteps = steps;
            mParentActivity = parent;
            mTwoPane = twoPane;
            
        }

        private void setupStepDetailFragment(Step data){

            Bundle arguments = new Bundle();
            arguments.putParcelable(RecipeStepDetailFragment.ARG_STEP_DETAIL, Parcels.wrap(data));
            RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
            fragment.setArguments(arguments);
            mParentActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_detail_container, fragment)
                    .commit();
        }

        private void setupStepDetailActivity(View view, Step data){

            Context context = view.getContext();
            Intent intent = new Intent(context, RecipeStepDetailActivity.class);
            intent.putExtra(RecipeStepDetailFragment.ARG_STEP_DETAIL, Parcels.wrap(data));
            context.startActivity(intent);

        }

 /*       @Override
          public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);

            //mRecyclerView = recyclerView;
        }*/

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            /* TYPE_ITEM */
            if (viewType == TYPE_ITEM) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.recipe_steps_item_content, parent, false);
                return new ItemViewHolder(view);
            }
            /* TYPE_HEADER */
            else {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.receipe_steps_header_content, parent, false);
                return new HeaderViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

            if (position == 0
                    && (holder.getItemViewType() == TYPE_HEADER)) {
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                headerViewHolder.bind(position);
            }
            else {
                ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
                itemViewHolder.bind(position);
                itemViewHolder.itemView.setTag(mSteps.get(position));
                itemViewHolder.itemView.setOnClickListener(mOnClickListener);
            }
        }
        
        @Override
        public int getItemCount() {
            return mSteps.size();
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
                    if(v != null){
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
            final TextView mStepDescription;

            public void bind(int position) {
                mStepId.setText("#" + Integer.toString(mSteps.get(position).getId()) + ":");
                mStepDescription.setText(mSteps.get(position).getShortDescription());
            }

            private ItemViewHolder(View view) {
                super(view);
                mStepId = (TextView) view.findViewById(R.id.tv_step_number);
                mStepDescription = (TextView) view.findViewById(R.id.tv_step_description);
            }
        }
    }
}
