package com.finroot.modified.expiry;

import java.util.ArrayList;
import java.util.List;

public class MEStraddleAdjustment {

	public static List<String> buy = new ArrayList<>();
	public static List<String> sell = new ArrayList<>();
	public static int nextStraddle = 0;
	public static int newHedgeStrike = 0;
	public static String newHedgeOption = null;
	public static String strikeToClose = null;

	public static void adjustupside(int currentStraddle, int nextstraddleStrike, boolean reverse) {
		buy = new ArrayList<>();
		buy.add(currentStraddle + " CE");
		strikeToClose = currentStraddle + " CE";
		nextStraddle = nextstraddleStrike;
		// new hedge for PE
		newHedgeStrike = MEHedge.calculateHedge(nextStraddle, "PE");
		buy.add(newHedgeStrike + " PE");
		newHedgeOption = " PE";
		sell = new ArrayList<>();
		if (!reverse) {
			sell.add(nextStraddle + " CE");
		}
		sell.add(nextStraddle + " PE");
	}

	public static void adjustDownside(int currentStraddle, int nextStraddleStrike, boolean reverse) {
		buy = new ArrayList<>();
		buy.add(currentStraddle + " PE");
		strikeToClose = currentStraddle + " PE";
		nextStraddle = nextStraddleStrike;
		// new hedge for CE 
		newHedgeStrike = MEHedge.calculateHedge(nextStraddle, "CE");
		newHedgeOption = " CE";
		buy.add(newHedgeStrike + " CE");
		sell = new ArrayList<>();
		if (!reverse) {
			sell.add(nextStraddle + " PE");
		}
		sell.add(nextStraddle + " CE");
	}
}
