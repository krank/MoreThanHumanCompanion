package com.urverkspel.humancompanion;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import voltroll.VoltResult;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, RollDataHolder, AttackDataHolder {

	// Drawer things
	private String[] drawerListItems;
	private ListView drawerListView;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle actionBarDrawerToggle;

	// This view
	private MainActivity thisActivity;

	// Pager things
	private ContainerFragment currentContainerFragment;

	// Tab things
	private ActionBar actionBar;

	// Data objects
	private AttackDataFragment attackDataFragment;
	private RollDataFragment rollDataFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {

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
		
		actionBar = getActionBar();
		
		super.onCreate(savedInstanceState);
	}

	public RollData getRollData() {
		// If there's no attackDataFragment
		if (rollDataFragment == null) {
			// Find it, if it exists, using the support fragment manager
			FragmentManager fm = getSupportFragmentManager();
			rollDataFragment = (RollDataFragment) fm.findFragmentByTag("rollData");
			
			// If it still does not exist, create a new one.
			if (rollDataFragment == null) {
				rollDataFragment = new RollDataFragment();
				FragmentTransaction ft = fm.beginTransaction();
				ft.add(rollDataFragment, "rollData");
				ft.commit();
			}
		}
		return rollDataFragment.getData();
	}
	
	public AttackData getAttackData() {
		// If there's no attackDataFragment
		if (attackDataFragment == null) {
			// Find it, if it exists, using the support fragment manager
			FragmentManager fm = getSupportFragmentManager();
			attackDataFragment = (AttackDataFragment) fm.findFragmentByTag("attackData");

			// If it still does not exist, create a new one.
			if (attackDataFragment == null) {
				attackDataFragment = new AttackDataFragment();
				FragmentTransaction ft = fm.beginTransaction();
				ft.add(attackDataFragment, "attackData");
				ft.commit();
			}
		}
		return attackDataFragment.getData();
	}

	public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
		currentContainerFragment.changePage(tab.getPosition());
	}

	public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
		
	}

	public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
		
	}

	private class SlideMenuClickListener implements ListView.OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			displayView(position);
		}

	}

	private void displayView(int menuIndex) {
		
		String[] tabs = {};
		ContainerFragment fragment = null;
		
		switch (menuIndex) {
			case 0:
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
				fragment = new cFragmentRoller();
				break;
			case 1:
				actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
				tabs = new String[]{
					this.getString(R.string.parameters),
					this.getString(R.string.armor),
					this.getString(R.string.results)
				};
				fragment = new cFragmentAttack();
				break;
			default:
				break;
		}
		
		// Replace the main container fragment
		if (fragment != null) {
			
			currentContainerFragment = fragment;
			
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.pager, fragment).commit();
			drawerLayout.closeDrawer(drawerListView);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
		
		
		// Tabs setup
		actionBar.removeAllTabs();
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

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
	
	
	
	
	
	
	
	public static String buildDiceResults(VoltResult vr, Context c) {
		StringBuilder sb = new StringBuilder();
		sb.append(c.getString(R.string.black)).append(" ");
		sb.append(vr.black.value);

		sb.append(" ");

		sb.append(c.getString(R.string.white)).append(" ");
		sb.append(vr.white.value);

		return sb.toString();
	}
}
