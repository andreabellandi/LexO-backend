package it.cnr.ilc.lexo.util;

import it.cnr.ilc.lexo.service.data.RepositoryData;
import it.cnr.ilc.lexo.service.data.lexicon.input.ExportSetting;
import it.cnr.ilc.lexo.service.data.lexicon.input.FormFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalConceptFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalEntryFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.LexicalSenseFilter;
import java.util.Arrays;
import org.eclipse.rdf4j.repository.config.RepositoryConfig;

/**
 *
 * @author andreabellandi
 */
public class LogUtil {

    public static String getLogFromPayload(Object payload) {
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
        } else if (payload.getClass().getName().contains("LexicalConceptFilter")) {
            LexicalConceptFilter lcf = (LexicalConceptFilter) payload;
            log = getLog(lcf);
        } else if (payload.getClass().getName().contains("ExportSetting")) {
            ExportSetting es = (ExportSetting) payload;
            log = getLog(es);
        } else if (payload.getClass().getName().contains("RepositoryData")) {
            RepositoryData rd = (RepositoryData) payload;
            log = getLog(rd);
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

    private static String getLog(LexicalConceptFilter lcf) {
        return "{\n"
                + "   text: " + lcf.getText() + "\n"
                + "   search mode: " + lcf.getSearchMode() + "\n"
                + "   label: " + lcf.getLabelType() + "\n"
                + "   author: " + lcf.getAuthor() + "\n"
                + "   limit: " + lcf.getLimit() + "\n"
                + "   offset: " + lcf.getOffset() + "\n"
                + "}";
    }

    private static String getLog(ExportSetting es) {
        return "{\n"
                + "   file name: " + es.getFileName() + "\n"
                + "   format: " + es.getFormat() + "\n"
                + "   subject: " + es.getSubject() + "\n"
                + "   predicate: " + es.getPredicate() + "\n"
                + "   object: " + es.getObject() + "\n"
                + "   context: " + Arrays.toString(es.getContext().toArray()) + "\n"
                + "}";
    }

    private static String getLog(RepositoryData rd) {
        return "{\n"
                + "   repo ID: " + rd.getRepoID() + "\n"
                + "   description: " + rd.getLabelID() + "\n"
                + "   base url: " + rd.getBaseUrl() + "\n"
                + "   ruleset name: " + rd.getRuleset() + "\n"
                + "}";
    }

}
