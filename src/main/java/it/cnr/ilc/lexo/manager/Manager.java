package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.service.data.vocabulary.MorphologicalProperty;
import it.cnr.ilc.lexo.util.EnumUtil;
import java.util.Arrays;

/**
 *
 * @author andreabellandi
 */
public interface Manager {


    public static void validateWithEnum(String parameterName, Class<? extends Enum> enumClass, String parameterValue) throws ManagerException {
        if (!EnumUtil.containsString(enumClass, parameterValue)) {
            throw new ManagerException(parameterName + " field must be one of " + Arrays.toString(EnumUtil.stringValues(enumClass)));
        }
    }

    public static void validateMorphology(String trait, String value) throws ManagerException {
        LexinfoManager lexinfoManager = ManagerFactory.getManager(LexinfoManager.class);
        MorphologicalProperty mp = lexinfoManager.getMorphoHash().get(trait);
        if (null != mp) {
            boolean found = false;
            for (MorphologicalProperty.MorphologicalValue mv : mp.getPropertyValues()) {
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

}
