package es.gobcan.istac.edatos.publisher.core.misc;

import java.util.Locale;

import org.siemac.edatos.common.test.CheckTranslationsTestBase;

import es.gobcan.istac.edatos.publisher.core.errors.ServiceExceptionParameters;
import es.gobcan.istac.edatos.publisher.core.errors.ServiceExceptionType;

public class StatisticalOperationsCheckTranslationsTest extends CheckTranslationsTestBase {

    // ----------------------------------------------------------------------------------------
    // MORE TESTS
    // ----------------------------------------------------------------------------------------

    // TODO EDATOS-3131
    // @Test
    // public void testCheckExistsAllTranslationsForServiceNoticeActions() throws Exception {
    // checkExistsAllTranslationsForStrings(getServiceNoticeActionClasses(), getLocalesToTranslate());
    // }
    //
    //
    // @SuppressWarnings("rawtypes")
    // public Class[] getServiceNoticeActionClasses() {
    // return new Class[]{ServiceNoticeAction.class};
    // }
    //
    // @Test
    // public void testCheckExistsAllTranslationsForServiceNoticeMessages() throws Exception {
    // checkExistsAllTranslationsForStrings(getServiceNoticeMessagesClasses(), getLocalesToTranslate());
    // }
    //
    //
    // @SuppressWarnings("rawtypes")
    // public Class[] getServiceNoticeMessagesClasses() {
    // return new Class[]{ServiceNoticeMessage.class};
    // }

    // ----------------------------------------------------------------------------------------
    // OVERRIDE METHODS
    // ----------------------------------------------------------------------------------------

    @Override
    @SuppressWarnings("rawtypes")
    public Class[] getServiceExceptionTypeClasses() {
        return new Class[]{ServiceExceptionType.class};
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Class[] getServiceExceptionParameterClasses() {
        return new Class[]{ServiceExceptionParameters.class};
    }

    @Override
    public Locale[] getLocalesToTranslate() {
        return LOCALES_TO_TRANSLATE;
    }
}
