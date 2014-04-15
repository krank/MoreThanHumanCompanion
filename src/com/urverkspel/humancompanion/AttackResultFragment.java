package com.urverkspel.humancompanion;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import voltroll.VoltResult;

public class AttackResultFragment extends Fragment {

	// Preferences
	SharedPreferences prefs;

	// General things
	private View rootView;

	private LinearLayout hitRollDisplayLayout;
	private LinearLayout armorDisplayLayout;
	private LinearLayout damageDisplayLayout;
	private LinearLayout extraDamageDisplayLayout;

	// TextViews
	private TextView attackParametersTextView;
	private TextView armorParametersTextView;

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
		rootView = inflater.inflate(R.layout.fragment_attack_result, container, false);

		findInterfaceElements();
		setListeners();

		prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

		return rootView;
	}

	private void findInterfaceElements() {
		attackParametersTextView = (TextView) rootView.findViewById(R.id.summary_attack_parameters);
		armorParametersTextView = (TextView) rootView.findViewById(R.id.summary_armor_parameters);

		rollButton = (Button) rootView.findViewById(R.id.button_roll);

		hitRollDisplayLayout = (LinearLayout) rootView.findViewById(R.id.display_result_hitroll);
		armorDisplayLayout = (LinearLayout) rootView.findViewById(R.id.display_result_armor);
		damageDisplayLayout = (LinearLayout) rootView.findViewById(R.id.display_result_damage);
		extraDamageDisplayLayout = (LinearLayout) rootView.findViewById(R.id.display_result_extra_damage);
	}

	private void setListeners() {
		rollButton.setOnClickListener(new Button.OnClickListener() {

			public void onClick(View v) {

				boolean useLuck = prefs.getBoolean("pref_use_luck", true);
				boolean useExtraInjury = prefs.getBoolean("pref_extra_injury", true);

				sharedAttackData.roll(useLuck, useExtraInjury);
				displayResultFromData();
				
			}
		});
	}

	private void displayResultFromData() {
		
		// HIDE DISPLAYS
		hitRollDisplayLayout.setVisibility(View.GONE);
		armorDisplayLayout.setVisibility(View.GONE);
		damageDisplayLayout.setVisibility(View.GONE);
		extraDamageDisplayLayout.setVisibility(View.GONE);

		// IF THERE ARE HIT AND DAMAGE RESULTS...
		if (sharedAttackData.hitResult != null && sharedAttackData.damageResult != null) {

			// TOHIT DISPLAY
			TextView toHitHeader = (TextView) hitRollDisplayLayout.findViewById(R.id.display_header);
			TextView toHitText = (TextView) hitRollDisplayLayout.findViewById(R.id.display_text);
			TextView toHitIcon = (TextView) hitRollDisplayLayout.findViewById(R.id.display_icon);

			// Header
			if (sharedAttackData.hitResult.bothSuccessful) {
				toHitHeader.setText(getActivity().getString(R.string.fullhit) + "!");
			} else if (sharedAttackData.hitResult.successful) {
				toHitHeader.setText(getActivity().getString(R.string.hit) + "!");
			} else {
				toHitHeader.setText(getActivity().getString(R.string.miss) + "!");
			}

			// Icon
			toHitIcon.setText(String.valueOf(sharedAttackData.hitResult.result));

			if (sharedAttackData.hitResult.wasLucky == VoltResult.LUCK_LUCKY) {
				toHitIcon.setBackgroundResource(R.drawable.yellow_bkg);
				toHitHeader.append(" (" + getActivity().getString(R.string.lucky) + ")");
			} else if (sharedAttackData.hitResult.wasLucky == VoltResult.LUCK_UNLUCKY) {
				toHitIcon.setBackgroundResource(R.drawable.black_bkg);
				toHitHeader.append(" (" + getActivity().getString(R.string.unlucky) + ")");
			} else if (sharedAttackData.hitResult.successful) {
				toHitIcon.setBackgroundResource(R.drawable.green_bkg);
			} else {
				toHitIcon.setBackgroundResource(R.drawable.red_bkg);
			}

			// Text
			toHitText.setText(getActivity().getString(R.string.white)
					+ " " + sharedAttackData.hitResult.white.value
					+ " " + getActivity().getString(R.string.black)
					+ " " + sharedAttackData.hitResult.black.value
			);

			// Make visible
			hitRollDisplayLayout.setVisibility(View.VISIBLE);

			// ARMOR DISPLAY
			// Only use if the attack hit and armor rules are active
			if (sharedAttackData.hitResult.successful && sharedAttackData.resultArmorEffect != AttackData.ARMOR_OFF) {

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
			this.buildDamageDisplay(damageDisplayLayout, sharedAttackData.damageResult, sharedAttackData.resultIsStun);

			// EXTRA DAMAGE
			if (sharedAttackData.extraDamageResult != null) {
				this.buildDamageDisplay(extraDamageDisplayLayout, sharedAttackData.extraDamageResult, false);
			}
		}
	}

	private String buildDiceResults(VoltResult vr) {
		StringBuilder sb = new StringBuilder();
		sb.append(getActivity().getString(R.string.white)).append(" ");
		sb.append(vr.white.value);

		sb.append(" ");

		sb.append(getActivity().getString(R.string.black)).append(" ");
		sb.append(vr.black.value);

		return sb.toString();
	}

	private void buildDamageDisplay(LinearLayout layout, VoltResult result, boolean isStun) {
		TextView header = (TextView) layout.findViewById(R.id.display_header);
		TextView text = (TextView) layout.findViewById(R.id.display_text);
		TextView icon = (TextView) layout.findViewById(R.id.display_icon);

		// Text box
		text.setText(buildDiceResults(result));

		// Header & icon
		if (result.successful) {
			icon.setBackgroundResource(R.drawable.green_bkg);
			if (isStun) {
				header.setText(result.result + " " + getActivity().getString(R.string.stun));
			} else {
				header.setText(result.result + " " + getActivity().getString(R.string.injury));
			}
		} else {
			header.setText(getActivity().getString(R.string.no_damage));
			icon.setBackgroundResource(R.drawable.red_bkg);
		}

		icon.setText(String.valueOf(result.result));

		if (result.wasLucky == VoltResult.LUCK_LUCKY) {
			icon.setBackgroundResource(R.drawable.yellow_bkg);
			header.append(" (" + getActivity().getString(R.string.lucky) + ")");
		} else if (result.wasLucky == VoltResult.LUCK_UNLUCKY) {
			icon.setBackgroundResource(R.drawable.black_bkg);
			header.append(" (" + getActivity().getString(R.string.unlucky) + ")");
		}

		layout.setVisibility(View.VISIBLE);
	}

	private void getParameters() {
		StringBuilder sb = new StringBuilder();

		sb.append(getActivity().getString(R.string.value)).append(": ").append(sharedAttackData.value).append("\n");
		sb.append(getActivity().getString(R.string.threshold)).append(": ").append(sharedAttackData.threshold).append("\n");
		sb.append(getActivity().getString(R.string.weapon_damage)).append(": ").append(sharedAttackData.damage).append("\n");
		
		attackParametersTextView.setText(sb.toString());
		
		sb = new StringBuilder();
		
		if (sharedAttackData.useArmor) {
			sb.append(getActivity().getString(R.string.weapon_penetration)).append(": ").append(sharedAttackData.penetration).append("\n");
			sb.append(getActivity().getString(R.string.armor_coverage)).append(": ").append(sharedAttackData.coverage).append("\n");
			sb.append(getActivity().getString(R.string.armor_protection)).append(": ")
					.append(sharedAttackData.protectionMin).append("-")
					.append(sharedAttackData.protectionMax);
			
			armorParametersTextView.setText(sb.toString());
			armorParametersTextView.setVisibility(View.VISIBLE);
		} else {
			armorParametersTextView.setVisibility(View.GONE);
		}
		
	}

	public void attachSharedAttackData() {
		if (sharedAttackData == null) {
			AttackDataHolder hold = (AttackDataHolder) getActivity();
			sharedAttackData = hold.getAttackData();
		}
	}

}
