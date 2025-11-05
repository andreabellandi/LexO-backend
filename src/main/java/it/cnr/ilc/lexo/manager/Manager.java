package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.service.data.vocabulary.PropertyHierarchy;
import it.cnr.ilc.lexo.service.data.vocabulary.RangedProperty;
import it.cnr.ilc.lexo.service.data.vocabulary.Value;
import it.cnr.ilc.lexo.sparql.SparqlQueryUtil;
import it.cnr.ilc.lexo.util.EnumUtil;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.util.Arrays;

/**
 *
 * @author andreabellandi
 */
public interface Manager {

    public final LexinfoManager lexinfoManager = ManagerFactory.getManager(LexinfoManager.class);

    public static boolean IDAlreadyExists(String iri) {
        return RDFQueryUtil.evaluateBQuery(SparqlQueryUtil.UNIQUE_ID.replace("_ID_", iri));
    }
    
    public static boolean validateLexinfo(String type, String value) throws ManagerException {
        boolean found = false;
        switch (type) {
            case "lexicalRel":
                for (PropertyHierarchy ph : lexinfoManager.getLexicalRel()) {
                    if (ph.getPropertyId().equals(value)) {
                        found = true;
                        break;
                    }
                }
                break;
            case "senseRel":
                for (PropertyHierarchy ph : lexinfoManager.getSenseRel()) {
                    if (ph.getPropertyId().equals(value)) {
                        found = true;
                        break;
                    }
                }
                break;
            case "formRel":
                for (PropertyHierarchy ph : lexinfoManager.getFormRel()) {
                    if (ph.getPropertyId().equals(value)) {
                        found = true;
                        break;
                    }
                }
                break;
            case "representation":
                for (Value v : lexinfoManager.getRepresentationProperties()) {
                    if (v.getValueId().equals(value)) {
                        found = true;
                        break;
                    }
                }
                break;
            case "senseDefinition":
                for (Value v : lexinfoManager.getSenseProperties()) {
                    if (v.getValueId().equals(value)) {
                        found = true;
                        break;
                    }
                }
                break;
            case "usage":
                for (RangedProperty rp : lexinfoManager.getUsage()) {
                    for (RangedProperty.RangedValue rv : rp.getPropertyValues()) {
                        if (rv.getValueId().equals(value)) {
                            found = true;
                            break;
                        }
                    }
                }
                break;
            case "lexicalCategory":
                for (RangedProperty rp : lexinfoManager.getLexicalCategory()) {
                    for (RangedProperty.RangedValue rv : rp.getPropertyValues()) {
                        if (rv.getValueId().equals(value)) {
                            found = true;
                            break;
                        }
                    }
                }
                break;
            case "formCategory":
                for (RangedProperty rp : lexinfoManager.getFormCategory()) {
                    for (RangedProperty.RangedValue rv : rp.getPropertyValues()) {
                        if (rv.getValueId().equals(value)) {
                            found = true;
                            break;
                        }
                    }
                }
                break;
            case "semanticCategory":
                for (RangedProperty rp : lexinfoManager.getSemanticCategory()) {
                    for (RangedProperty.RangedValue rv : rp.getPropertyValues()) {
                        if (rv.getValueId().equals(value)) {
                            found = true;
                            break;
                        }
                    }
                }
                break;
        }
        return found;
    }

    public static void validateWithEnum(String parameterName, Class<? extends Enum> enumClass, String parameterValue) throws ManagerException {
        if (!EnumUtil.containsString(enumClass, parameterValue)) {
            throw new ManagerException(parameterName + " field must be one of " + Arrays.toString(EnumUtil.stringValues(enumClass)));
        }
    }

    public static void validateMorphology(String trait, String value) throws ManagerException {
        RangedProperty mp = lexinfoManager.getMorphoHash().get(trait);
        if (null != mp) {
            boolean found = false;
            for (RangedProperty.RangedValue mv : mp.getPropertyValues()) {
                if (mv.getValueId().equals(value)) {
                    found = true;
                }
            }
            if (!found) {
                throw new ManagerException(value + " not recognized as value of " + trait);
            }
        } else {
            throw new ManagerException(trait + " not recognized");
        }
    }

    public static void validateLanguage(String lang) throws ManagerException {
        LexiconStatisticsManager lexiconStatisticsManager = ManagerFactory.getManager(LexiconStatisticsManager.class);
        lexiconStatisticsManager.reloadCache();
        boolean found = false;
        for (String l : lexiconStatisticsManager.getLexiconLanguages()) {
            if (l.equals(lang)) {
                found = true;
            }
        }
        if (!found) {
            throw new ManagerException(lang + " language does not exist");
        }
    }
    
    public static void validateDictLanguage(String lang) throws ManagerException {
        DictionaryStatisticsManager dictionaryStatisticsManager = ManagerFactory.getManager(DictionaryStatisticsManager.class);
        dictionaryStatisticsManager.reloadCache();
        boolean found = false;
        for (String l : dictionaryStatisticsManager.getDictionaryLanguages()) {
            if (l.equals(lang)) {
                found = true;
            }
        }
        if (!found) {
            throw new ManagerException(lang + " language does not exist");
        }
    }

}
