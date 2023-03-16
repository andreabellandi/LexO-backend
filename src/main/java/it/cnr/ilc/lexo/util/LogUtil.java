package it.cnr.ilc.lexo.util;

import it.cnr.ilc.lexo.service.data.lexicon.input.FormFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalSenseFilter;
import java.util.regex.Pattern;

/**
 *
 * @author andreabellandi
 */
public class LogUtil {

    public static String getLogFormPayload(Object payload) {
        String log = null;
        if (payload.getClass().getName().contains("LexicalEntryFilter")) {
            LexicalEntryFilter lef = (LexicalEntryFilter) payload;
            log = getLog(lef);
        } else if (payload.getClass().getName().contains("LexicaSenseFilter")) {
            LexicalSenseFilter lsf = (LexicalSenseFilter) payload;
            log = getLog(lsf);
        } else if (payload.getClass().getName().contains("FormFilter")) {
            FormFilter ff = (FormFilter) payload;
            log = getLog(ff);
        }
        return log;
    }

    private static String getLog(LexicalEntryFilter lef) {
        return "{\n"
                + "   text: " + lef.getText() + "\n"
                + "   search mode: " + lef.getSearchMode() + "\n"
                + "   type: " + lef.getType() + "\n"
                + "   author: " + lef.getAuthor() + "\n"
                + "   formType: " + lef.getFormType() + "\n"
                + "   lang: " + lef.getLang() + "\n"
                + "   pos: " + lef.getPos() + "\n"
                + "   status: " + lef.getStatus() + "\n"
                + "   limit: " + lef.getLimit() + "\n"
                + "   offset: " + lef.getOffset() + "\n"
                + "}";
    }

    private static String getLog(LexicalSenseFilter lsf) {
        return "{\n"
                + "   text: " + lsf.getText() + "\n"
                + "   search mode: " + lsf.getSearchMode() + "\n"
                + "   field: " + lsf.getField() + "\n"
                + "   type: " + lsf.getType() + "\n"
                + "   author: " + lsf.getAuthor() + "\n"
                + "   formType: " + lsf.getFormType() + "\n"
                + "   lang: " + lsf.getLang() + "\n"
                + "   pos: " + lsf.getPos() + "\n"
                + "   status: " + lsf.getStatus() + "\n"
                + "   limit: " + lsf.getLimit() + "\n"
                + "   offset: " + lsf.getOffset() + "\n"
                + "}";
    }
    
    private static String getLog(FormFilter ff) {
        return "{\n"
                + "   text: " + ff.getText() + "\n"
                + "   search mode: " + ff.getSearchMode() + "\n"
                + "   representation type: " + ff.getRepresentationType() + "\n"
                + "   author: " + ff.getAuthor() + "\n"
                + "   limit: " + ff.getLimit() + "\n"
                + "   offset: " + ff.getOffset() + "\n"
                + "}";
    }

}
