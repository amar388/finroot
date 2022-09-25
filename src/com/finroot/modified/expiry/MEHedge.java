package com.finroot.modified.expiry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MEHedge {

	public static Map<String, Map<Integer, Integer>> hedges = new HashMap<>();
	public static List<String> openLongStrikes = new ArrayList<>();

	public static int calculateHedge(int strike, String option) {
		int hedge = 0;

		switch (option) {
		case "CE":
			hedge = strike + 300;
			break;

		case "PE":
			hedge = strike - 300;
			break;
		}
		return hedge;
	}

	public static Map<String, Integer> getHedgeForStraddle(int strike) {
		Map<String, Integer> hedge = new HashMap<>();
		int hedg = 0;
		hedg = calculateHedge(strike, "CE");
		hedge.put("CE", hedg);
//addHedgeForModifiedExpiry (hedg,qty, "CE");
		hedg = calculateHedge(strike, "PE");
		hedge.put("PE", hedg);
//addHedgeForModifiedExpiry (hedg,qty, "PE");
		return hedge;
	}

	public static void addHedgeForModifiedExpiry(Integer strike, Integer qty, String option) {

		if (hedges.get(option) != null) {
			hedges.get(option).put(strike, qty);
		} else {
			Map<Integer, Integer> hedgeWithQty = new HashMap<>();
			hedgeWithQty.put(strike, qty);
			hedges.put(option, hedgeWithQty);
		}
	}

	public static void addHedgeForModifiedExpiry(Integer strike, String option) {
		openLongStrikes.add(strike + option);
	}
}
