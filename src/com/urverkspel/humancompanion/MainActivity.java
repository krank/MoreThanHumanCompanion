package com.urverkspel.humancompanion;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	// Drawer things
	private String[] drawerListItems;
	private ListView drawerListView;
	private DrawerLayout drawerLayout;
	private ActionBarDrawerToggle actionBarDrawerToggle;
	
	// This view
	private MainActivity thisActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		thisActivity = this;
		
		
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
		
		

		displayView(1);
	}
	
	private class SlideMenuClickListener implements ListView.OnItemClickListener {

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			displayView(position);
		}
		
	}
	
	private void displayView(int position) {
		Fragment fragment = null;
		
		switch (position) {
			case 0:
				fragment = new RollerFragment();
				break;
			case 1:
				fragment = new CombatFragment();
			default:
				break;
		}
		
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();
			this.drawerLayout.closeDrawer(this.drawerListView);
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
}
