package com.urverkspel.humancompanion;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class AttackResultFragment extends Fragment {

	// Preferences
	SharedPreferences prefs;

	// General things
	private View rootView;

	// TextViews
	private TextView parametersTextView;
	private TextView resultTextView;

	// Button
	private Button rollButton;

	// Data reference
	public AttackData sharedAttackData;

	@Override
	public void onAttach(Activity activity) {
		AttackDataHolder hold = (AttackDataHolder) activity;
		sharedAttackData = hold.getAttackData();
		super.onAttach(activity);

	}

	@Override
	public void setMenuVisibility(final boolean visible) {
		super.setMenuVisibility(visible);
		if (visible) {
			if (sharedAttackData != null) {
				getParameters();
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Create local references to general things
		rootView = inflater.inflate(R.layout.fragment_combat_result, container, false);

		findInterfaceElements();
		setListeners();

		getParameters();

		return rootView;
	}

	private void findInterfaceElements() {
		parametersTextView = (TextView) rootView.findViewById(R.id.text_parameters);
		resultTextView = (TextView) rootView.findViewById(R.id.text_result);

		rollButton = (Button) rootView.findViewById(R.id.button_roll);
	}
	
	private void setListeners() {
		rollButton.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {
				sharedAttackData.roll();
				String resultString = sharedAttackData.makeResultString();
				resultTextView.setText(resultString);
			}
		});
	}

	private void getParameters() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Value: ").append(sharedAttackData.value).append("\n");
		sb.append("Threshold: ").append(sharedAttackData.threshold).append("\n");
		sb.append("Damage: ").append(sharedAttackData.damage).append("\n");
		if (sharedAttackData.useArmor) {
			sb.append("Penetration: ").append(sharedAttackData.penetration).append("\n");
			sb.append("Coverage: ").append(sharedAttackData.coverage).append("\n");
			sb.append("Protection: ")
					.append(sharedAttackData.protectionMin).append("-")
					.append(sharedAttackData.protectionMax).append("\n");
		}
		parametersTextView.setText(sb.toString());
	}

}
