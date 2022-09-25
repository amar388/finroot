package com.finroot.modified.expiry;

import java.util.HashSet;
import java.util.Set;

public class MEOpen {
	
	public static int initSpot;
	public static int initStrike;
	public static Set<String> initStraddle = null;
	
	public static void initStraddleStrike (int spot) {
		initSpot = spot;
		initStraddle = new HashSet<>();
		initStrike = getStraddleStrike(spot);
		initStraddle.add(initStrike+" CE");
		initStraddle.add(initStrike+" PE");
	}

	public static int getStraddleStrike(int spot) {
		// TODO Auto-generated method stub
		int straddle = 0;
		int tailSpot = spot%50;
		if(tailSpot>25) {
			straddle = (spot - tailSpot) + 50;
		} else if (tailSpot<25) {
			straddle = (spot - tailSpot);
		} else {
			straddle = 0;
		}
		return straddle;
	}

}
