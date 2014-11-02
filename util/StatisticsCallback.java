package util;

/**
 * An interface for returning the statistics used
 * This one might better be replaced by an Observer pattern
 * @author harald.drillenburg
 *
 */

public interface StatisticsCallback {
	void amountUsed(long amount);
}
