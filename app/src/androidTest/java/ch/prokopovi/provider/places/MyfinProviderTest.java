package ch.prokopovi.provider.places;

import java.util.List;
import java.util.Map.Entry;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import android.test.AndroidTestCase;
import android.util.Log;
import ch.prokopovi.api.provider.PlacesProvider;
import ch.prokopovi.api.struct.BestRatesRecord;
import ch.prokopovi.err.WebUpdatingException;
import ch.prokopovi.provider.ProviderUtils;
import ch.prokopovi.struct.Master.CurrencyCode;
import ch.prokopovi.struct.Master.OperationType;
import ch.prokopovi.struct.Master.Region;

public class MyfinProviderTest extends AndroidTestCase {

    private static final String LOG_TAG = "MyfinProviderTest";

    private static final Double USD_VALUE = 9530d;

    private PlacesProvider provider;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.provider = new MyfinPlacesProvider();
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testUpdate() throws WebUpdatingException {

        List<Entry<Long, BestRatesRecord>> places = this.provider.getPlaces(Region.BREST);

        for (Entry<Long, BestRatesRecord> entry : places) {

            Log.d(LOG_TAG, "place " + entry.getKey()+ " : "+entry.getValue());
        }

        assertNotNull("result is null", places);
        assertTrue("result is empty", places.size() > 0);
    }

    public void testParsing() throws WebUpdatingException, XPatherException {


        HtmlCleaner cleaner = ProviderUtils.initHtmlCleaner();

        String testStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"+
                "<root>"+
                "<bank>"+
                "<bankid>5</bankid>"+
                "<filialid>1143</filialid>"+
                "<date>17.01.2014</date>"+
                "<bankname>ЗАО \"Абсолютбанк\"</bankname>"+
                "<bankaddress>Минский район, 9-ый км.Московского шоссе (м-н \"Виталюр\")</bankaddress>"+
                "<bankphone>+375 (17) 266 06 79</bankphone>"+
                "<filialname>Касса №6 ЗАО \"Абсолютбанк\"</filialname>"+
                "<usd_buy>"+USD_VALUE.intValue()+"</usd_buy>"+
                "<usd_sell>9620</usd_sell>"+
                "<eur_buy>12940</eur_buy>"+
                "<eur_sell>13120</eur_sell>"+
                "<rur_buy>281</rur_buy>"+
                "<rur_sell>288</rur_sell>"+
                "<pln_buy>3080</pln_buy>"+
                "<pln_sell>3150</pln_sell>"+
                "<eurusd_buy>1.3477</eurusd_buy>"+
                "<eurusd_sell>1.374</eurusd_sell>"+
                "</bank>"+
                "</root>";


        TagNode node = cleaner.clean(testStr);

        Object[] nodes = node.evaluateXPath("//root/*");

        if (nodes != null) {

            for (Object obj : nodes) {
                TagNode tmp = (TagNode) obj;

                // Log.d(LOG_TAG, "string " + tmp.getText());

                List<Entry<Long, BestRatesRecord>> entries = MyfinPlacesProvider.parsePlace(tmp);

                for (Entry<Long, BestRatesRecord> entry : entries) {
                    BestRatesRecord rec = entry.getValue();
                    assertNotNull("rate should not be null. rate: "+entry, rec);

                    if (CurrencyCode.USD.getId() == rec.getCurrencyId() && OperationType.BUY.getId() == rec.getExchangeTypeId()) {
                        assertTrue("value doesn't match", Math.abs(rec.getValue() - USD_VALUE) <= 0.0001);
                    }
                }

            }
        }

    }
}
