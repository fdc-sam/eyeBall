package live.sai.eyeball.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import live.sai.eyeball.Fragment.AddProductFragment;
import live.sai.eyeball.Fragment.ProductFragment;
import live.sai.eyeball.Fragment.TabFragment;
import live.sai.eyeball.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class IndexActivity extends AppCompatActivity {

    // Initialize variables
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton addProductButton;
    private FrameLayout fragmentContainer;
    private ActionBar actionBar;
    private TabAdapter adapter;
    private Animation fadeInAnimation;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        // Assign variables
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        addProductButton = findViewById(R.id.add_product);
        fragmentContainer = findViewById(R.id.fragmentContainer);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Sari-sari");

        // Load the fade-in animation
        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        // Initialize array list
        ArrayList<String> arrayList = new ArrayList<>();

        // Add titles to array list
        arrayList.add("Products");
        arrayList.add("Profile");
        arrayList.add("Pro");
        arrayList.add("Pro");

        // Setup tab layout
        tabLayout.setupWithViewPager(viewPager);

        // Prepare view pager
        adapter = new TabAdapter(getSupportFragmentManager(), arrayList);
        viewPager.setAdapter(adapter);

        // Show or hide add_product button based on the current tab
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                showHideAddProductButton(position == 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event
                onAddProductClicked(v);
            }
        });
    }

    private void showHideAddProductButton(boolean show) {
        if (show) {
            addProductButton.show();
            addProductButton.startAnimation(fadeInAnimation);
        } else {
            addProductButton.hide();
        }
    }

    private void refreshData() {
        // Perform any necessary data refreshing or reloading here
        Log.d("sai", "refresh");

        // Refresh the ProductFragment
        Fragment fragment = adapter.getItem(0);
        if (fragment instanceof ProductFragment) {
            ProductFragment productFragment = (ProductFragment) fragment;
            productFragment.refreshFirstTab();
        }

        // Stop the swipe-to-refresh animation
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private class TabAdapter extends FragmentPagerAdapter {
        // Initialize ArrayList
        private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        private ArrayList<String> stringArrayList = new ArrayList<>();

        // Create constructor
        public TabAdapter(FragmentManager supportFragmentManager, ArrayList<String> arrayList) {
            super(supportFragmentManager);
            stringArrayList = arrayList;
            initializeFragments();
        }

        private void initializeFragments() {
            for (int i = 0; i < stringArrayList.size(); i++) {
                Bundle bundle = new Bundle();
                bundle.putString("title", stringArrayList.get(i));
                Fragment fragment;
                if (i == 0) {
                    fragment = new ProductFragment();
                } else {
                    fragment = new TabFragment();
                }
                fragment.setArguments(bundle);
                fragmentArrayList.add(fragment);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return stringArrayList.get(position);
        }
    }

    public void onAddProductClicked(View view) {
        // Animate the button
        view.startAnimation(fadeInAnimation);

        // Display the AddProductFragment
        AddProductFragment addProductFragment = new AddProductFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, addProductFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        // Hide the addProductButton
        addProductButton.setVisibility(View.GONE);

        // Show the back button in ActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Add Product");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Show the addProductButton
        addProductButton.setVisibility(View.VISIBLE);

        // Hide the back button in ActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setTitle("Sari-sari");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
