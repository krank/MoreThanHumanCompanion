package com.urverkspel.humancompanion;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	// Drawer things
	private String[] drawerListItems;
	private ListView drawerListView;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle actionBarDrawerToggle;

	// This view
	private MainActivity thisActivity;

	// Pager things
	private ViewPager viewPager;
	private FragmentPagerAdapter pagerAdapter;

	// Tab things
	private ActionBar actionBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		thisActivity = this;

		/* Drawer things */
		drawerListItems = getResources().getStringArray(R.array.drawer_items);
		drawerListView = (ListView) findViewById(R.id.left_drawer);

		drawerListView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_listview_item, drawerListItems));

		drawerListView.setOnItemClickListener(new SlideMenuClickListener());

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		actionBarDrawerToggle = new ActionBarDrawerToggle(
				this,
				drawerLayout,
				R.drawable.ic_drawer,
				R.string.drawer_open,
				R.string.drawer_close
		);

		drawerLayout.setDrawerListener(actionBarDrawerToggle);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		/* Pager things */
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();

                /* Select the correct tab when changing page by swiping */
                viewPager.setOnPageChangeListener(
                        new ViewPager.SimpleOnPageChangeListener() {
                            @Override
                            public void onPageSelected(int position) {
                                getActionBar().setSelectedNavigationItem(position);
                            }
                        }
                );
                
                
                
                
                
                
                
                /* Display initial view */
		displayView(1); // 1 = Combat
	}

	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	}

	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	}
	
	private class SlideMenuClickListener implements ListView.OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			displayView(position);
		}

	}

	private void displayView(int menuIndex) {
		String[] tabs = {};
		
		switch (menuIndex) {
			case 0: // Roller
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
				pagerAdapter = new RollerPagerAdapter(getSupportFragmentManager());
				break;
			case 1: // Combat roll
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
				tabs = new String[]{
					this.getString(R.string.parameters),
                                        this.getString(R.string.armor),
					this.getString(R.string.results)
				};
				pagerAdapter = new CombatPagerAdapter(getSupportFragmentManager());
				break;
		}
		
		viewPager.setAdapter(pagerAdapter);

		actionBar.removeAllTabs();
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		this.drawerLayout.closeDrawer(this.drawerListView);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		this.getMenuInflater().inflate(R.menu.rollermenu, menu);
		return true;

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		actionBarDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		actionBarDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
			case R.id.roller_settings:
				Intent intent = new Intent(thisActivity, SettingsActivity.class);
				thisActivity.startActivity(intent);
				return true;
			default:
				return super.onOptionsItemSelected(item);

		}

	}
}
