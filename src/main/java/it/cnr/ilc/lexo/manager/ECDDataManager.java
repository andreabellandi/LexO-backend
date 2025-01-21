/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import static it.cnr.ilc.lexo.manager.LexiconDataManager.logger;
import it.cnr.ilc.lexo.service.data.lexicon.input.DictionaryEntryFilter;
import it.cnr.ilc.lexo.service.data.lexicon.input.ECDEntryFilter;
import it.cnr.ilc.lexo.sparql.SparqlSelectData;
import it.cnr.ilc.lexo.util.EnumUtil;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import it.cnr.ilc.lexo.util.StringUtil;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andreabellandi
 */
public class ECDDataManager implements Manager, Cached {

    static final Logger logger = LoggerFactory.getLogger(ECDDataManager.class.getName());

    private final String lexicalizationModel = LexOProperties.getProperty("skos.lexicalizationModel");
    private final String defaultLanguageLabel = LexOProperties.getProperty("skos.defaultLanguageLabel");

    @Override
    public void reloadCache() {

    }

    public TupleQueryResult getECDComponents(String id) throws ManagerException {
        String query = SparqlSelectData.DATA_ECD_COMPONENTS.replace("_ID_", id);
        return RDFQueryUtil.evaluateTQuery(query);
    }

    public TupleQueryResult getFilterdECDEntries(ECDEntryFilter def) throws ManagerException {
        logger.info(def.toString());
//        Manager.validateWithEnum("formType", FormTypes.class, lef.getFormType());
        Manager.validateWithEnum("status", EnumUtil.LexicalEntryStatus.class, def.getStatus());
        if (def.getSearchMode().isEmpty()) {
            def.setSearchMode("equals");
        }
        String filter = createFilter(def);
        int limit = def.getLimit();
        int offset = def.getOffset();
        String query = SparqlSelectData.DATA_ECD_ENTRIES.replace("[FILTER]", filter)
                //                .replace("_TYPE_", lef.getType())
                .replace("[LIMIT]", String.valueOf(limit))
                .replace("[OFFSET]", String.valueOf(offset));

        return RDFQueryUtil.evaluateTQuery(query);
    }

    private String createFilter(ECDEntryFilter def) {
        def.setText(StringUtil.escapeMetaCharacters(def.getText()));
        String text = def.getText().isEmpty() ? "*" : def.getText();
        String filter = "(" + (def.getSearchMode().equals(EnumUtil.SearchModes.Equals.toString()) ? "dictionaryEntryLabel: " + text
                : (def.getSearchMode().equals(EnumUtil.SearchModes.StartsWith.toString()) ? "dictionaryEntryLabel: " + text + "*"
                : (def.getSearchMode().equals(EnumUtil.SearchModes.Contains.toString()) ? "dictionaryEntryLabel: *" + text + "*"
                : "dictionaryEntryLabel: *" + text))) + ")";
        filter = filter + (!def.getLang().isEmpty() ? " AND entryLanguage:" + def.getLang() : "");
        filter = filter + (!def.getAuthor().isEmpty() ? " AND author:" + def.getAuthor() : "");
//        filter = filter + (!def.getPos().isEmpty() ? " AND pos:" + "\\\"" + def.getPos() + "\\\"" : "");
        filter = filter + (!def.getStatus().isEmpty() ? " AND status:" + def.getStatus() : "");
        return filter;
    }
}
