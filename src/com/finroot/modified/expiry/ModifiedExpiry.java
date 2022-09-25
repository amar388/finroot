package com.finroot.modified.expiry;

import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ModifiedExpiry {

	public static Set<String> openShortStrikes = new HashSet<>();
	public static int previousStraddle = 0;
	public static int currentStraddle = 0;
	static Scanner input = new Scanner(System.in);

	public static void runStrategy() {
		System.out.println(":::: Enter Initial Spot ::::");
		int inp = input.nextInt();
		if (inp > 0) {
			MEOpen.initStraddleStrike(inp);
			Map<String, Integer> hedge = MEHedge.getHedgeForStraddle(MEOpen.initStrike);
			System.out.println("\n\n:::: Sell :::::");
			System.out.println("\t" + MEOpen.initStrike + " CE");
			System.out.println("\t" + MEOpen.initStrike + " PE");
			System.out.println("\n\n:::: Buy::::");
			System.out.println("\t" + hedge.get("CE") + " CE");
			System.out.println("\t" + hedge.get("PE") + " PE");
			System.out.println("\n Press 1 if traded (or) Press 0 to change the spot");
			inp = input.nextInt();
			if (inp > 0) {
				openShortStrikes.add(MEOpen.initStrike + " CE");
				openShortStrikes.add(MEOpen.initStrike + " PE");
				MEHedge.addHedgeForModifiedExpiry(hedge.get("CE"), " CE");
				MEHedge.addHedgeForModifiedExpiry(hedge.get("PE"), " PE");
				currentStraddle = MEOpen.initStrike;
				display();
				doAdjustment();
			} else {
				runStrategy();
			}
		} else {
			System.out.println("Invalid Spot, Exiting !!!");
		}
		System.out.println("Exited Successfully !!!!");
	}

	private static void savePositions() {
		if (openShortStrikes.contains(MEStraddleAdjustment.strikeToClose)) {
			openShortStrikes.remove(MEStraddleAdjustment.strikeToClose);
		}
		openShortStrikes.add(MEStraddleAdjustment.nextStraddle + " CE");
		openShortStrikes.add(MEStraddleAdjustment.nextStraddle + " PE");
		previousStraddle = currentStraddle;
		currentStraddle = MEStraddleAdjustment.nextStraddle;
		MEHedge.openLongStrikes.add(MEStraddleAdjustment.newHedgeStrike + MEStraddleAdjustment.newHedgeOption);
	}

	private static void display() {
		System.out.println("\n\n Modified Expiry =======");
		System.out.println("\nOpen Positions");
		System.out.println("\n==== Active SELL Positions ===='mm'");
		for (String strike : openShortStrikes) {
			System.out.println(strike);
		}
		System.out.println("\n=== Active Buy Positions ====");

		for (String strike : MEHedge.openLongStrikes) {
			System.out.println(strike);
		}
		System.out.println("\nCurrent Straddle :: " + currentStraddle + " CE\t" + currentStraddle + " PE");
		if (openShortStrikes.size() == 4) {
			System.out.println("!!!!! Margin capacity reached !!!!!");
		}
	}

	private static void postionsToTrade() {
		System.out.println(" ==== Buy Positions ====");
		for (String strike : MEStraddleAdjustment.buy) {
			System.out.println(strike);
		}
		System.out.println("\n ==== Sell Postions ====");
		for (String strike : MEStraddleAdjustment.sell) {
			System.out.println(strike);
		}
		System.out.println("\n ==== Straddle ==== ");
		System.out.println(MEStraddleAdjustment.nextStraddle + " CE\t" + MEStraddleAdjustment.nextStraddle + " PE");
	}

	private static void doAdjustment() {
		int inp = 0;
		while (true) {
			System.out.println("\n\n:::: Enter Next Spot ::::");
			inp = input.nextInt();
			if (inp > 0) {
				int nextStraddle = MEOpen.getStraddleStrike(inp);
				if (openShortStrikes.contains(nextStraddle + " CE")
						|| openShortStrikes.contains(nextStraddle + " PE")) {
					if (nextStraddle > currentStraddle) {
						System.out.println("\t!!! warn Market might go V-shape !!!");
						MEStraddleAdjustment.adjustupside(currentStraddle, nextStraddle, true);
					} else {
						MEStraddleAdjustment.adjustDownside(currentStraddle, nextStraddle, true);
					}
				} else {
					if (nextStraddle > currentStraddle) {
						System.out.println("\t==== Market Moving Up ====");
						MEStraddleAdjustment.adjustupside(currentStraddle, nextStraddle, false);
					} else {
						System.out.println("\t ==== Market Moving Down ====");
						MEStraddleAdjustment.adjustDownside(currentStraddle, nextStraddle, false);
					}
				}
				postionsToTrade();
				System.out.println("\n\n Press 1 if traded. or Press 0 to change the spot");
				inp = input.nextInt();
				if (inp > 0) {
					savePositions();
					display();
				}
				else {
					continue;
				}
			} else {
				break;
			}
		}
	}
}
