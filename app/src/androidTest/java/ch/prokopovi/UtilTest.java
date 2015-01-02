package ch.prokopovi;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import android.test.AndroidTestCase;
import android.util.Log;

public class UtilTest extends AndroidTestCase {

	private static final String LOG_TAG = "UtilTest";

	public void testParseDotDouble() throws ParseException {
		double res = Util.parseDotDouble("3020.70");
		Log.d(LOG_TAG, "res: "+res);

		assertEquals("dot double parsing is wrong", 3020.70, res, 0.00001);

	}

	public void testParseCommaDouble() throws ParseException {
		double res = Util.parseCommaDouble("3110,8270");
		Log.d(LOG_TAG, "res: "+res);

		assertEquals("comma double parsing is wrong", 3110.8270, res, 0.00001);

	}

	public void testIsSameDay()  {

		long now = new Date().getTime();
		boolean sameDay = Util.isInSameDay(now, now);
		assertTrue("not the same day", sameDay);

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);

		long tomorrow = calendar.getTime().getTime();

		boolean wrongSameDay = Util.isInSameDay(now, tomorrow);
		assertFalse("the same day", wrongSameDay);

	}

}
