package afl.features.test;

import gaj.afl.datatype.MatchDateTime;
import gaj.afl.datatype.MatchLocation;
import gaj.afl.datatype.ShortMonth;
import gaj.afl.datatype.Team;

import org.junit.Test;

import afl.features.EnvironmentFeatures;
import afl.features.FeatureVector;
import afl.features.SparseFeatureVector;
import afl.features.TeamFeatures;

public class TestFeatures {

	@Test
	public void testEnvironmentFeatures() {
		int loc_idx = 7;
		MatchLocation loc = MatchLocation.values()[loc_idx];
		String month = "May";
		String day = "Mon";
		MatchDateTime dt = new MatchDateTime("2010 "+month+" 24 ("+day+" 8:00pm)");
		FeatureVector ef = new EnvironmentFeatures(dt, loc).features;
		System.out.printf("env-features=%d\n", ef.length());
		for (int i = 0; i < ef.length(); i++)
			System.out.printf(" %3.1f", ef.get(i));
		System.out.println();
		assert ef.get(loc_idx) == 1.0;
		int mnth_idx = ShortMonth.valueOf(month).ordinal();
		int offset = MatchLocation.values().length;
		assert ef.get(offset+mnth_idx) == 1.0;
		offset += ShortMonth.values().length;
		int day_idx = MatchDateTime.Day.valueOf(day).ordinal();
		assert ef.get(offset+day_idx) == 1.0;
		System.out.printf("sq-norm=%3.2f\n", ef.dot(ef));
	}

	@Test
	public void testTeamFeatures() {
		int team_idx = 4;
		Team team = Team.values()[team_idx];
		FeatureVector tf = new TeamFeatures(team).features;
		System.out.printf("team-features=%d [%d]\n",
				tf.length(),
				Team.values().length);
		for (int i = 0; i < tf.length(); i++) {
			System.out.printf(" %3.1f", tf.get(i));
			if (i == team_idx)
				assert tf.get(i) == 1.0;
			else
				assert tf.get(i) == 0.0;
		}
		System.out.println();
		System.out.printf("sq-norm=%3.2f\n", tf.dot(tf));
	}

	@Test
	public void testSparseFeatures() {
		int[] i1 = new int[] { 1, 2 };
		double[] v1 = new double[] { 1, 1 };
		FeatureVector f1 = new SparseFeatureVector(6, i1, v1);
		printVector("f1", f1);
		int[] i2 = new int[] { 0, 2, 5 };
		double[] v2 = new double[] { 1, 1, 1 };
		FeatureVector f2 = new SparseFeatureVector(6, i2, v2);
		printVector("f2", f2);
		printVector("f1-f2=", f1.subtract(f2));
      printVector("f1+f2=", f1.add(f2));
      printVector("0.5*[(f1+f2)+(f1-f2)]=", f1.add(f2).add(f1.subtract(f2)).scale(0.5));
	}

	private static void printVector(String label, FeatureVector vector) {
		System.out.printf("%s=[", label);
		for (int i = 0; i < vector.length(); i++)
			System.out.printf(" %3.1f", vector.get(i));
		System.out.println(" ]");
	}

}
