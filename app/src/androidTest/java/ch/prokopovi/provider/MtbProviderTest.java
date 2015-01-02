package ch.prokopovi.provider;

import java.util.List;
import java.util.Set;

import android.test.AndroidTestCase;
import ch.prokopovi.api.provider.Provider;
import ch.prokopovi.api.struct.ProviderRate;
import ch.prokopovi.err.WebUpdatingException;
import ch.prokopovi.struct.Master.CurrencyCode;
import ch.prokopovi.struct.Master.ProviderCode;
import ch.prokopovi.struct.Master.RateType;
import ch.prokopovi.struct.ProviderRequirements;

public class MtbProviderTest extends AndroidTestCase {
    private static final String LOG_TAG = "MtbProviderTest";

    private Provider provider;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.provider = new MtbProvider();
    }


    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testUpdate() throws WebUpdatingException {

        RateType inRateType = RateType.CASH;

        ProviderRequirements requirements = new ProviderRequirements(ProviderCode.MTB, inRateType);
        Set<CurrencyCode> currencyCodes = requirements.getCurrencyCodes();
        currencyCodes.add(CurrencyCode.USD);
        currencyCodes.add(CurrencyCode.EUR);
        currencyCodes.add(CurrencyCode.RUR);

        List<ProviderRate> list = this.provider.update(requirements);

        assertNotNull("result is null", list);
        assertTrue("result has wrong size: "+list.size(), list.size() == 6);

    }
}
