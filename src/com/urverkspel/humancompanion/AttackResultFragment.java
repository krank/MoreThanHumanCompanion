package com.urverkspel.humancompanion;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AttackResultFragment extends Fragment {

	// Preferences
	SharedPreferences prefs;

	// General things
	private View rootView;

	private LinearLayout hitRollDisplayLayout;
	private LinearLayout armorDisplayLayout;
	private LinearLayout damageDisplayLayout;

	// TextViews
	private TextView parametersTextView;
	private TextView resultTextView;

	// Button
	private Button rollButton;

	// Data reference
	public AttackData sharedAttackData;

	@Override
	public void onAttach(Activity activity) { // Happens on rotation, before others
		this.attachSharedAttackData();
		super.onAttach(activity);
	}

	@Override
	public void onStart() { // Happens on switch from distant tab, before setMenuVisibility on rotation
		if (sharedAttackData != null) {
			getParameters();
			displayResultFromData();
		}
		super.onStart();
	}

	@Override
	public void setMenuVisibility(final boolean visible) { // Happens on switch from other tab, before onStart() unless rotating
		super.setMenuVisibility(visible);

		if (visible) {
			if (sharedAttackData != null) {
				getParameters();
				displayResultFromData();
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

		return rootView;
	}

	private void findInterfaceElements() {
		parametersTextView = (TextView) rootView.findViewById(R.id.text_parameters);

		rollButton = (Button) rootView.findViewById(R.id.button_roll);

		hitRollDisplayLayout = (LinearLayout) rootView.findViewById(R.id.display_result_hitroll);
		armorDisplayLayout = (LinearLayout) rootView.findViewById(R.id.display_result_armor);
		damageDisplayLayout = (LinearLayout) rootView.findViewById(R.id.display_result_damage);
	}

	private void setListeners() {
		rollButton.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {

				//System.out.println("Results before: " + sharedAttackData.hitResult + ", " + sharedAttackData.damageResult);
				sharedAttackData.roll(false);
				//System.out.println("Results after: " + sharedAttackData.hitResult + ", " + sharedAttackData.damageResult);

				displayResultFromData();
			}
		});
	}

	private void displayResultFromData() {
		hitRollDisplayLayout.setVisibility(View.GONE);
		armorDisplayLayout.setVisibility(View.GONE);
		damageDisplayLayout.setVisibility(View.GONE);
		if (sharedAttackData.hitResult != null && sharedAttackData.damageResult != null) {

			// TOHIT DISPLAY
			TextView toHitHeader = (TextView) hitRollDisplayLayout.findViewById(R.id.display_header);
			TextView toHitText = (TextView) hitRollDisplayLayout.findViewById(R.id.display_text);
			TextView toHitIcon = (TextView) hitRollDisplayLayout.findViewById(R.id.display_icon);

			toHitIcon.setText(String.valueOf(sharedAttackData.hitResult.result));
			if (sharedAttackData.resultHits > 0) {
				toHitIcon.setBackgroundResource(R.drawable.green_bkg);
			} else {
				toHitIcon.setBackgroundResource(R.drawable.red_bkg);
			}

			if (sharedAttackData.resultHits == 2) {
				toHitHeader.setText(getActivity().getString(R.string.fullhit) + "!");
			} else if (sharedAttackData.resultHits == 1) {
				toHitHeader.setText(getActivity().getString(R.string.hit) + "!");
			} else {
				toHitHeader.setText(getActivity().getString(R.string.miss) + "!");
			}

			toHitText.setText(getActivity().getString(R.string.white)
					+ " " + sharedAttackData.hitResult.white.value
					+ " " + getActivity().getString(R.string.black)
					+ " " + sharedAttackData.hitResult.black.value
			);

			hitRollDisplayLayout.setVisibility(View.VISIBLE);

			// ARMOR DISPLAY
			if (sharedAttackData.resultHits > 0 && sharedAttackData.resultArmorEffect != AttackData.ARMOR_OFF) {

				TextView armorHeader = (TextView) armorDisplayLayout.findViewById(R.id.display_header);
				TextView armorText = (TextView) armorDisplayLayout.findViewById(R.id.display_text);

				switch (sharedAttackData.resultArmorEffect) {
					case AttackData.ARMOR_OFF:
						armorHeader.setText(getActivity().getString(R.string.armor_effect_header_off));
						armorText.setText(getActivity().getString(R.string.armor_effect_desc_off));
						break;
					case AttackData.ARMOR_NOT_COVERED:
						armorHeader.setText(getActivity().getString(R.string.armor_effect_header_not_covered));
						armorText.setText(getActivity().getString(R.string.armor_effect_desc_not_covered));
						break;
					case AttackData.ARMOR_FULLY_PENETRATED:
						armorHeader.setText(getActivity().getString(R.string.armor_effect_header_fully_penetrated));
						armorText.setText(getActivity().getString(R.string.armor_effect_desc_fully_penetrated));
						break;
					case AttackData.ARMOR_PARTIALLY_PENETRATED:
						armorHeader.setText(getActivity().getString(R.string.armor_effect_header_partially_penetrated));
						armorText.setText(getActivity().getString(R.string.armor_effect_desc_partially_penetrated));
						break;
					case AttackData.ARMOR_COMPLETELY_PROTECTED:
						armorHeader.setText(getActivity().getString(R.string.armor_effect_header_completely_protected));
						armorText.setText(getActivity().getString(R.string.armor_effect_desc_completely_protected));
						break;
				}
				armorDisplayLayout.setVisibility(View.VISIBLE);
			}

			// DAMAGE DISPLAY
			TextView damageHeader = (TextView) damageDisplayLayout.findViewById(R.id.display_header);
			TextView damageText = (TextView) damageDisplayLayout.findViewById(R.id.display_text);
			TextView damageIcon = (TextView) damageDisplayLayout.findViewById(R.id.display_icon);

			if (sharedAttackData.resultDamage > 0) {
				damageIcon.setBackgroundResource(R.drawable.green_bkg);
				if (sharedAttackData.resultIsStun) {
					damageHeader.setText(sharedAttackData.resultDamage + " " + getActivity().getString(R.string.stun));
				} else {
					damageHeader.setText(sharedAttackData.resultDamage + " " + getActivity().getString(R.string.injury));
				}
			} else {
				damageHeader.setText(getActivity().getString(R.string.no_damage));
				damageIcon.setBackgroundResource(R.drawable.red_bkg);
			}

			damageIcon.setText(String.valueOf(sharedAttackData.resultDamage));

			damageText.setText(getActivity().getString(R.string.white)
					+ " " + sharedAttackData.damageResult.white.value
					+ " " + getActivity().getString(R.string.black)
					+ " " + sharedAttackData.damageResult.black.value
			);

			damageDisplayLayout.setVisibility(View.VISIBLE);
		}
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

	public void attachSharedAttackData() {
		if (sharedAttackData == null) {
			AttackDataHolder hold = (AttackDataHolder) getActivity();
			sharedAttackData = hold.getAttackData();
		}
	}

}
