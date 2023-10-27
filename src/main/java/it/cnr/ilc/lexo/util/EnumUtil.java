/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.util;

import it.cnr.ilc.lexo.sparql.SparqlPrefix;
import java.util.EnumSet;

/**
 *
 * @author andreabellandi
 */
public class EnumUtil {

    
    public static boolean containsString(Class<? extends Enum> enumClass, String string) {
        String[] stringValues = stringValues(enumClass);
        for (int i = 0; i < stringValues.length; i++) {
            if (stringValues[i].equals(string)) {
//            if (stringValues[i].contains(string)) {
                return true;
            }
        }
        return false;
    }

    public static String[] stringValues(Class<? extends Enum> enumClass) {
        EnumSet enumSet = EnumSet.allOf(enumClass);
        String[] values = new String[enumSet.size()];
        int i = 0;
        for (Object enumValue : enumSet) {
            values[i] = String.valueOf(enumValue);
            i++;
        }
        return values;
    }

    public static enum SearchModes {
        Equals("equals"),
        StartsWith("startsWith"),
        Contains("contains"),
        Ends("ends");

        private final String searchMode;

        private SearchModes(String searchMode) {
            this.searchMode = searchMode;
        }

        @Override
        public String toString() {
            return this.searchMode;
        }
    }

    public static enum FormTypes {
        All(""),
        Flexed("flexed"),
        Entry("entry");

        private final String formType;

        private FormTypes(String formType) {
            this.formType = formType;
        }

        @Override
        public String toString() {
            return this.formType;
        }
    }

    public static enum LexicalEntryStatus {
        All(""),
        Reviewed("reviewed"),
        Working("working"),
        Completed("completed");

        private final String status;

        private LexicalEntryStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return this.status;
        }
    }

    public static enum LexicalEntryTypes {
        All(""),
        Word("word"),
        Etymon("etymon"),
        Cognate("cognate"),
        Multiword("multi-word expression"),
        Affix("affix");

        private final String type;

        private LexicalEntryTypes(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum LexicalAspects {
        Core("core"),
        Decomp("decomposition"),
        Etym("etymology"),
        VarTrans("variation and translation"),
        SynSem("syntax and semantics");

        private final String module;

        private LexicalAspects(String module) {
            this.module = module;
        }

        @Override
        public String toString() {
            return this.module;
        }

    }

    public static enum LinkedEntityType {
        Internal("internal"),
        External("external");

        private final String linkedEntityType;

        private LinkedEntityType(String linkedEntityType) {
            this.linkedEntityType = linkedEntityType;
        }

        @Override
        public String toString() {
            return this.linkedEntityType;
        }

    }

    public static enum SearchFormTypes {
        Keyword("keyword"),
        Lemma("lemma");

        private final String searchFormTypes;

        private SearchFormTypes(String searchFormTypes) {
            this.searchFormTypes = searchFormTypes;
        }

        @Override
        public String toString() {
            return this.searchFormTypes;
        }

    }

    public static enum LexicalSenseSearchFilter {
        Definition("definition"),
        Description("description"),
        Explanation("explanation"),
        Gloss("gloss"),
        SenseExample("senseExample"),
        SenseTranslation("senseTranslation");

        private final String lexicalSenseSearchFilter;

        private LexicalSenseSearchFilter(String lexicalSenseSearchFilter) {
            this.lexicalSenseSearchFilter = lexicalSenseSearchFilter;
        }

        @Override
        public String toString() {
            return this.lexicalSenseSearchFilter;
        }

    }
    
    public static enum LexicalConceptSearchFilter {
        prefLabel("prefLabel"),
        altLabel("altLabel"),
        hiddenLabel("hiddentLabel");

        private final String lexicalConceptSearchFilter;

        private LexicalConceptSearchFilter(String lexicalConceptSearchFilter) {
            this.lexicalConceptSearchFilter = lexicalConceptSearchFilter;
        }

        @Override
        public String toString() {
            return this.lexicalConceptSearchFilter;
        }

    }
    
     
    public static enum LinguisticRelation {
        Morphology("morphology"),
        LexicalRel("lexicalRel"),
        SenseRel("senseRel"),
        FormRel("formRel"),
        ConceptRef("conceptRef"),
        EtymologicalLink("etyLink"),
        Decomp("decomp"),
        Extension("extension"),
        Lexicon("lexicon"),
        Translation("translation"),
        TranslationSet("translationSet"),
        LexicoSemanticRel("LexicosemanticRel"),
        Lexicog("lexicog"),
        //        Cognate("cognate"),
        ConceptRel("conceptRel");

        private final String linguisticRelation;

        private LinguisticRelation(String linguisticRelation) {
            this.linguisticRelation = linguisticRelation;
        }

        @Override
        public String toString() {
            return this.linguisticRelation;
        }

    }

    public static enum GenericRelation {
        Reference("reference"),
        Bibliography("bibliography"),
        Multimedia("multimedia"),
        Confidence("confidence"),
        Extension("extension"),
        Attestation("attestation"),
        ConceptRel("conceptRel"),
        Metadata("metadata"),
        FrequencyRel("frequencyRel"),
        Decomp("decomp");

        private final String genericRelation;

        private GenericRelation(String genericRelation) {
            this.genericRelation = genericRelation;
        }

        @Override
        public String toString() {
            return this.genericRelation;
        }

    }

    public static enum GenericRelationReference {
        seeAlso(SparqlPrefix.RDFS.getUri() + "seeAlso"),
        sameAs(SparqlPrefix.OWL.getUri() + "sameAs");

        private final String genericRelationReference;

        private GenericRelationReference(String genericRelationReference) {
            this.genericRelationReference = genericRelationReference;
        }

        @Override
        public String toString() {
            return this.genericRelationReference;
        }

    }

    public static enum GenericRelationConfidence {
        confidence(SparqlPrefix.LEXINFO.getUri() + "confidence"),
        translationConfidence(SparqlPrefix.LEXINFO.getUri() + "translationConfidence");

        private final String genericRelationConfidence;

        private GenericRelationConfidence(String genericRelationConfidence) {
            this.genericRelationConfidence = genericRelationConfidence;
        }

        @Override
        public String toString() {
            return this.genericRelationConfidence;
        }

    }

    public static enum PositionRelation {
        rdfListPosition(SparqlPrefix.RDF.getUri() + "_n"),
        rdfsMemeber(SparqlPrefix.RDFS.getUri() + "member");

        private final String positionRelation;

        private PositionRelation(String positionRelation) {
            this.positionRelation = positionRelation;
        }

        @Override
        public String toString() {
            return this.positionRelation;
        }

    }

    public static enum GenericRelationBibliography {
        textualReference(SparqlPrefix.RDFS.getUri() + "label"),
        note(SparqlPrefix.SKOS.getUri() + "note");

        private final String genericRelationBibliography;

        private GenericRelationBibliography(String genericRelationBibliography) {
            this.genericRelationBibliography = genericRelationBibliography;
        }

        @Override
        public String toString() {
            return this.genericRelationBibliography;
        }

    }

    public static enum GraphRelationDirection {
        incoming("incoming"),
        outgoing("outgoing");

        private final String graphRelationDirection;

        private GraphRelationDirection(String graphRelationDirection) {
            this.graphRelationDirection = graphRelationDirection;
        }

        @Override
        public String toString() {
            return this.graphRelationDirection;
        }

    }
    
    public static enum FormRepresentationType {
        WrittenRepresentation("writtenRep"),
        PhoneticRepresentation("phoneticRep"),
        Segmentation("segmentation"),
        Pronunciation("pronunciation"),
        Transliteration("transliteration"),
        Romanization("romanization");

        private final String formRepresentationType;

        private FormRepresentationType(String formRepresentationType) {
            this.formRepresentationType = formRepresentationType;
        }

        @Override
        public String toString() {
            return this.formRepresentationType;
        }

    }

    public static enum AcceptedSearchFormExtendTo {
        None(""),
        Hypernym("hypernym"),
        Hyponym("hyponym"),
        Synonym("synonym");

        private final String acceptedSearchFormExtendTo;

        private AcceptedSearchFormExtendTo(String acceptedSearchFormExtendTo) {
            this.acceptedSearchFormExtendTo = acceptedSearchFormExtendTo;
        }

        @Override
        public String toString() {
            return this.acceptedSearchFormExtendTo;
        }

    }

    public static enum AcceptedSearchFormExtensionDegree {
        One("1"),
        Two("2"),
        Three("3");

        private final String acceptedSearchFormExtensionDegree;

        private AcceptedSearchFormExtensionDegree(String acceptedSearchFormExtensionDegree) {
            this.acceptedSearchFormExtensionDegree = acceptedSearchFormExtensionDegree;
        }

        @Override
        public String toString() {
            return this.acceptedSearchFormExtensionDegree;
        }

    }
    
    public static enum MetadataTypes {
        //source, rights, title, description, publisher
        note(SparqlPrefix.SKOS.getUri() + "note"),
        label(SparqlPrefix.RDFS.getUri() + "label"),
        confidence(SparqlPrefix.LEXINFO.getUri() + "confidence"),
        description(SparqlPrefix.DCT.getUri() + "description"),
        example(SparqlPrefix.LEXINFO.getUri() + "example"),
        source(SparqlPrefix.DCT.getUri() + "source"),
        rights(SparqlPrefix.DCT.getUri() + "rights"),
        title(SparqlPrefix.DCT.getUri() + "title"),
        publisher(SparqlPrefix.DCT.getUri() + "publisher"),
        comment(SparqlPrefix.RDFS.getUri() + "comment");

        private final String metadataTypes;

        private MetadataTypes(String metadataTypes) {
            this.metadataTypes = metadataTypes;
        }

        @Override
        public String toString() {
            return this.metadataTypes;
        }

    }
    
    public static enum RulesetRepositoryConfiguration {
        rdfs("rdfs"),
        owlhorst("owl-horst"),
        owlmax("owl-max"),
        owl2rl("owl2-rl"),
        rdfsoptimized("rdfs-optimized"),
        owlhorstoptimized("owl-horst-optimized"),
        owlmaxoptimized("owl-max-optimized"),
        owl2rloptimized("owl2-rl-optimized");

        private final String types;

        private RulesetRepositoryConfiguration(String types) {
            this.types = types;
        }

        @Override
        public String toString() {
            return this.types;
        }

    }
}
