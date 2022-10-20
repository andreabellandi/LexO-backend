/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.sparql;

/**
 *
 * @author andreabellandi
 */
public class SparqlVariable {

    // index names
    public static final String LEXICAL_ENTRY_INDEX = "lexicalEntryIndex";
    public static final String FORM_INDEX = "formIndex";
    public static final String LEXICAL_SENSE_INDEX = "lexicalSenseIndex";
    public static final String ETYMOLOGY_INDEX = "etymologyIndex";
    public static final String CONCEPT_REFERENCE_INDEX = "conceptReferenceIndex";
    public static final String COMPONENT_INDEX = "componentIndex";

    // varibale names
    public static final String TOTAL_HITS = "totalHits";

    // graph
    public static final String GRAPH = "graph";

    public static final String LEXICON = "lexicon";
    public static final String LEXICON_LANGUAGE = "language";
    public static final String LEXICON_LANGUAGE_INSTANCE_NAME = "languageInstanceName";
    public static final String LEXICON_LANGUAGE_LABEL = "languageLabel";
    public static final String LEXICON_LANGUAGE_LEXVO = "lexvo";
    public static final String LEXICON_LANGUAGE_DESCRIPTION = "languageDescription";
    public static final String LEXICON_LANGUAGE_CATALOG = "catalog";
    public static final String LEXICON_LANGUAGE_CREATOR = "creator";

    public static final String LEXICAL_ENTRY = "lexicalEntry";
    public static final String LEXICAL_ENTRY_INSTANCE_NAME = "lexicalEntryInstanceName";
    public static final String LEXICAL_ENTRY_STATUS = "status";
    public static final String LEXICAL_ENTRY_REVISOR = "revisor";
    public static final String LEXICAL_ENTRY_COMPLETING_AUTHOR = "author";
    public static final String LEXICAL_ENTRY_CREATION_AUTHOR = "creator";
    public static final String LEXICAL_ENTRY_POS = "pos";
    public static final String LEXICAL_ENTRY_TYPE = "type";
    public static final String LEXICAL_ENTRY_SUBTERM = "subterm";
    public static final String LEXICAL_ENTRY_CONSTITUENT = "constituent";
    public static final String LEXICAL_ENTRY_ETYMOLOGY = "etymology";

    public static final String FORM = "form";
    public static final String FORM_INSTANCE_NAME = "formInstanceName";
    public static final String FORM_TYPE = "formType";
    public static final String FORM_CREATION_AUTHOR = "creator";
    public static final String WRITTEN_REPRESENTATION = "writtenRep";
    public static final String TRANSLITERATION = "transliteration";
    public static final String SEGMENTATION = "segmentation";
    public static final String PRONUNCIATION = "pronunciation";
    public static final String ROMANIZATION = "romanization";
    public static final String PHONETIC_REPRESENTATION = "phoneticRep";
    public static final String MORPHOLOGY_TRAIT_NAME = "traitName";
    public static final String MORPHOLOGY_TRAIT_VALUE = "traitValue";
    public static final String INHERITED_MORPHOLOGY_TRAIT_NAME = "inheritedTraitName";
    public static final String INHERITED_MORPHOLOGY_TRAIT_VALUE = "inheritedTraitValue";
    public static final String MORPHOLOGY = "morpho";
    public static final String TRAIT = "trait";

    public static final String SENSE = "sense";
    public static final String SENSE_INSTANCE_NAME = "senseInstanceName";
    public static final String SENSE_DEFINITION = "definition";
    public static final String SENSE_USAGE = "usage";
    public static final String SENSE_DESCRIPTION = "description";
    public static final String SENSE_EXPLANATION = "explanation";
    public static final String SENSE_GLOSS = "gloss";
    public static final String SENSE_EXAMPLE = "senseExample";
    public static final String SENSE_TRANSLATION = "senseTranslation";
    public static final String SENSE_CREATION_AUTHOR = "creator";
    public static final String SENSE_TOPIC = "topic";

    public static final String ETYMOLOGY = "etymology";
    public static final String ETYMOLOGY_CREATION_AUTHOR = "creator";
    public static final String CONFIDENCE = "confidence";
    public static final String HYPOTHESIS_OF = "hypothesisOf";

    public static final String ETY_LINK = "etyLink";
    public static final String ETY_LINK_LABEL = "etyLinkLabel";
    public static final String ETY_LINK_TYPE = "etyLinkType";
    public static final String ETY_SOURCE = "etySuorce";
    public static final String ETY_SOURCE_LABEL = "etySourceLabel";
    public static final String ETY_TARGET = "etyTarget";
    public static final String ETY_TARGET_LABEL = "etyTargetLabel";

    public static final String COMPONENT = "component";
    public static final String COMPONENT_CREATION_AUTHOR = "creator";
    public static final String COMPONENT_POSITION = "position";

    public static final String FRAME = "frame";

    public static final String LEXICAL_FORM = "lexicalForm";

    public static final String LEXICAL_CONCEPT = "lexicalConcept";
    public static final String LEXICAL_CONCEPT_CREATOR = "creator";
    public static final String CONCEPT_SET_CREATOR = "creator";

    public static final String CONCEPT_SCHEME = "conceptScheme";
    public static final String CONCEPT_SCHEME_STATUS = "status";
    public static final String CONCEPT_SCHEME_CREATOR = "creator";

    public static final String PREF_LABEL = "prefLabel";
    public static final String ALT_LABEL = "altLabel";
    public static final String HIDDEN_LABEL = "hiddenLabel";
    public static final String DEFINITION = "definition";
    public static final String EXAMPLE = "example";
    public static final String CHANGE_NOTE = "changeNote";
    public static final String HISTORY_NOTE = "historyNote";
    public static final String EDITORIAL_NOTE = "editorialNote";
    public static final String SCOPE_NOTE = "scopeNote";
    public static final String NOTATION = "notation";

    public static final String CONCEPT = "concept";
    public static final String CONCEPT_STATUS = "status";
    public static final String CONCEPT_INSTANCE_NAME = "conceptInstanceName";

    public static final String CREATION_DATE = "created";
    public static final String COMPLETION_DATE = "dateAccepted";
    public static final String REVISION_DATE = "dateSubmitted";
    public static final String LAST_UPDATE = "modified";

    public static final String NOTE = "note";

    public static final String LABEL = "label";
    public static final String LABEL_COUNT = "labelCount";

    public static final String SEEALSO = "seeAlso";
    public static final String SAMEAS = "sameAs";

    public static final String SOURCE = "source";

    public static final String TARGET = "target";
    public static final String IRI = "IRI";
    public static final String LENGHT = "lenght";

    public static final String PROPERTY_COMMENT = "propertyComment";
    public static final String INST_COMMENT = "instComment";
    public static final String CLASS_COMMENT = "classComment";

    public static final String STATEMENTS_NUMBER = "statements";
    public static final String TYPE = "type";
    public static final String VALUE = "value";
    public static final String RELATION = "relation";
    public static final String LINK = "link";
    public static final String SOURCE_LABEL = "sourceLabel";
    public static final String TARGET_LABEL = "targetLabel";

    public static final String BIBLIOGRAPHY = "references";
    public static final String BIBLIOGRAPHY_CREATOR = "bibCreator";
    public static final String BIBLIOGRAPHY_ID = "bibId";
    public static final String BIBLIOGRAPHY_TITLE = "bibTitle";
    public static final String BIBLIOGRAPHY_AUTHOR = "bibAuthor";
    public static final String BIBLIOGRAPHY_DATE = "bibDate";
    public static final String BIBLIOGRAPHY_URL = "bibUrl";
    public static final String BIBLIOGRAPHY_NOTE = "bibNote";
    public static final String BIBLIOGRAPHY_SEE_ALSO_LINK = "bibSeeAlsoLink";
    public static final String BIBLIOGRAPHY_TEXTUAL_REF = "bibTextRef";

    public static final String HYPERNYM = "hypernym";
    public static final String HYPONYM = "hyponym";
    public static final String HOLONYM = "holonym";
    public static final String MERONYM = "meronym";
    public static final String SYNONYM = "synonym";

    public static final String HOPS = "hops";
    
    public static final String PATH_INDEX = "pathIndex";
    public static final String EDGE_INDEX = "edgeIndex";
    public static final String EDGE = "edge";

    // skos variables
    public static final String CHILD = "child";
    public static final String GCHILD = "grandchild";
    public static final String NGCHILD = "nGrandchildren";
    public static final String ROOT = "root";

}
