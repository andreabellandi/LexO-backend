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
public class OntoLexEntity {

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

    public static enum LexicalEntryTypes {
        LexicalEntry(SparqlPrefix.ONTOLEX.getUri() + "LexicalEntry"),
        Word(SparqlPrefix.ONTOLEX.getUri() + "Word"),
        Etymon(SparqlPrefix.ETY.getUri() + "Etymon"),
        Multiword(SparqlPrefix.ONTOLEX.getUri() + "MultiWordExpression"),
        Affix(SparqlPrefix.ONTOLEX.getUri() + "Affix");

        private final String type;

        private LexicalEntryTypes(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum CoreClasses {
        LexicalEntry(SparqlPrefix.ONTOLEX.getUri() + "LexicalEntry"),
        LexicalSense(SparqlPrefix.ONTOLEX.getUri() + "LexicalSense"),
        Form(SparqlPrefix.ONTOLEX.getUri() + "Form");

        private final String type;

        private CoreClasses(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum LexicalConcepts {
        LexicalConcept(SparqlPrefix.ONTOLEX.getUri() + "LexicalConcept"),
        ConceptSet(SparqlPrefix.ONTOLEX.getUri() + "ConceptSet");

        private final String type;

        private LexicalConcepts(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum VartransRelationClasses {
        LexicalRelation(SparqlPrefix.VARTRANS.getUri() + "LexicalRelation"),
        SenseRelation(SparqlPrefix.VARTRANS.getUri() + "SenseRelation"),
        Translation(SparqlPrefix.VARTRANS.getUri() + "Translation"),
        TerminologicalRelation(SparqlPrefix.VARTRANS.getUri() + "TerminologicalRelation");

        private final String type;

        private VartransRelationClasses(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum LexicalConceptRel {
        evokes(SparqlPrefix.ONTOLEX.getUri() + "evokes"),
        isEvokedBy(SparqlPrefix.ONTOLEX.getUri() + "isEvokedBy"),
        isConceptOf(SparqlPrefix.ONTOLEX.getUri() + "isConceptOf"),
        lexicalizedSense(SparqlPrefix.ONTOLEX.getUri() + "lexicalizedSense"),
        isLexicalizedSenseOf(SparqlPrefix.ONTOLEX.getUri() + "isLexicalizedSenseOf"),
        conceptRel(SparqlPrefix.VARTRANS.getUri() + "conceptRel"),
        concept(SparqlPrefix.ONTOLEX.getUri() + "concept");

        private final String type;

        private LexicalConceptRel(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum Translation {
        translation(SparqlPrefix.VARTRANS.getUri() + "translation");

        private final String type;

        private Translation(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }
    
    public static enum TranslationSet {
        trans(SparqlPrefix.VARTRANS.getUri() + "trans");

        private final String type;

        private TranslationSet(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum FormTypes {
        LexicalForm(SparqlPrefix.ONTOLEX.getUri() + "lexicalForm"),
        CanonicalForm(SparqlPrefix.ONTOLEX.getUri() + "canonicalForm"),
        OtherForm(SparqlPrefix.ONTOLEX.getUri() + "otherForm");

        private final String type;

        private FormTypes(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum ReferenceTypes {
        Reference(SparqlPrefix.ONTOLEX.getUri() + "reference"),
        Denotes(SparqlPrefix.ONTOLEX.getUri() + "denotes");

        private final String type;

        private ReferenceTypes(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum LexicoSemanticProperty {
        Category(SparqlPrefix.VARTRANS.getUri() + "category"),
        Source(SparqlPrefix.VARTRANS.getUri() + "source"),
        Target(SparqlPrefix.VARTRANS.getUri() + "target");

        private final String type;

        private LexicoSemanticProperty(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum EtyLinkTypes {
        EtySource(SparqlPrefix.ETY.getUri() + "etySource");

        private final String type;

        private EtyLinkTypes(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }
    
    public static enum FrequencyRel {
        EtySource(SparqlPrefix.FRAC.getUri() + "frequency");

        private final String type;

        private FrequencyRel(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return this.type;
        }
    }

    public static enum LexicalRel {
        Cognate(SparqlPrefix.ETY.getUri() + "cognate"),
        TranslatableAs(SparqlPrefix.VARTRANS.getUri() + "translatableAs");

        private final String lexicalRel;

        private LexicalRel(String lexicalRel) {
            this.lexicalRel = lexicalRel;
        }

        @Override
        public String toString() {
            return this.lexicalRel;
        }

    }

    public static enum SenseRel {
        Translation(SparqlPrefix.VARTRANS.getUri() + "translation");

        private final String senseRel;

        private SenseRel(String senseRel) {
            this.senseRel = senseRel;
        }

        @Override
        public String toString() {
            return this.senseRel;
        }

    }

    public static enum Lexicon {
        LinguisticCatalog(SparqlPrefix.LIME.getUri() + "linguisticCatalog");

        private final String lexicon;

        private Lexicon(String lexicon) {
            this.lexicon = lexicon;
        }

        @Override
        public String toString() {
            return this.lexicon;
        }

    }

    public static enum Decomp {
        SubTerm(SparqlPrefix.DECOMP.getUri() + "subterm"),
        Constituent(SparqlPrefix.DECOMP.getUri() + "constituent"),
        CorrespondsTo(SparqlPrefix.DECOMP.getUri() + "correspondsTo");

        private final String decomp;

        private Decomp(String decomp) {
            this.decomp = decomp;
        }

        @Override
        public String toString() {
            return this.decomp;
        }

    }

    public static enum LanguageAttributes {
        Description(SparqlPrefix.DCT.getUri() + "description"),
        Language(SparqlPrefix.LIME.getUri() + "language"),
        Lexvo(SparqlPrefix.DCT.getUri() + "language"),
        Confidence(SparqlPrefix.LEXINFO.getUri() + "confidence");

        private final String languageAttributes;

        private LanguageAttributes(String languageAttributes) {
            this.languageAttributes = languageAttributes;
        }

        @Override
        public String toString() {
            return this.languageAttributes;
        }

    }

    public static enum LexicalEntryAttributes {
        Label(SparqlPrefix.RDFS.getUri() + "label"),
        Type(SparqlPrefix.RDF.getUri() + "type"),
        Status(SparqlPrefix.VS.getUri() + "term_status"),
        Note(SparqlPrefix.SKOS.getUri() + "note"),
        Language(SparqlPrefix.LIME.getUri() + "entry"),
        Denotes(SparqlPrefix.ONTOLEX.getUri() + "denotes"),
        Confidence(SparqlPrefix.LEXINFO.getUri() + "confidence");

        private final String lexicalEntryAttributes;

        private LexicalEntryAttributes(String lexicalEntryAttributes) {
            this.lexicalEntryAttributes = lexicalEntryAttributes;
        }

        @Override
        public String toString() {
            return this.lexicalEntryAttributes;
        }

    }

    public static enum FormAttributes {
        Note(SparqlPrefix.SKOS.getUri() + "note"),
        Type(SparqlPrefix.RDF.getUri() + "type"),
        WrittenRep(SparqlPrefix.ONTOLEX.getUri() + "writtenRep"),
        PhoneticRep(SparqlPrefix.ONTOLEX.getUri() + "phoneticRep"),
        Pronunciation(SparqlPrefix.LEXINFO.getUri() + "pronunciation"),
        Segmentation(SparqlPrefix.LEXINFO.getUri() + "segmentation"),
        Transliteration(SparqlPrefix.LEXINFO.getUri() + "transliteration"),
        Romanization(SparqlPrefix.LEXINFO.getUri() + "romanization"),
        Confidence(SparqlPrefix.LEXINFO.getUri() + "confidence");

        private final String formAttributes;

        private FormAttributes(String formAttributes) {
            this.formAttributes = formAttributes;
        }

        @Override
        public String toString() {
            return this.formAttributes;
        }

    }

    public static enum LexicalSenseAttributes {
        Note(SparqlPrefix.SKOS.getUri() + "note"),
        Usage(SparqlPrefix.ONTOLEX.getUri() + "usage"),
        Reference(SparqlPrefix.ONTOLEX.getUri() + "reference"),
        Topic(SparqlPrefix.DCT.getUri() + "subject"),
        Definition(SparqlPrefix.SKOS.getUri() + "definition"),
        Description(SparqlPrefix.LEXINFO.getUri() + "description"),
        Explanation(SparqlPrefix.LEXINFO.getUri() + "explanation"),
        Gloss(SparqlPrefix.LEXINFO.getUri() + "gloss"),
        SenseExample(SparqlPrefix.LEXINFO.getUri() + "senseExample"),
        SenseTranslation(SparqlPrefix.LEXINFO.getUri() + "senseTranslation"),
        Confidence(SparqlPrefix.LEXINFO.getUri() + "confidence");

        private final String senseAttributes;

        private LexicalSenseAttributes(String senseAttributes) {
            this.senseAttributes = senseAttributes;
        }

        @Override
        public String toString() {
            return this.senseAttributes;
        }

    }

    public static enum EtymologyAttributes {
        Note(SparqlPrefix.SKOS.getUri() + "note"),
        HypothesisOf(SparqlPrefix.RDFS.getUri() + "comment"),
        Label(SparqlPrefix.RDFS.getUri() + "label"),
        Confidence(SparqlPrefix.LEXINFO.getUri() + "confidence");

        private final String etymologyAttributes;

        private EtymologyAttributes(String etymologyAttributes) {
            this.etymologyAttributes = etymologyAttributes;
        }

        @Override
        public String toString() {
            return this.etymologyAttributes;
        }

    }

    public static enum EtymologicalLinkAttributes {
        Type(SparqlPrefix.ETY.getUri() + "etyLinkType"),
        Note(SparqlPrefix.SKOS.getUri() + "note"),
        Label(SparqlPrefix.RDFS.getUri() + "label"),
        Confidence(SparqlPrefix.LEXINFO.getUri() + "confidence");

        private final String etyLinkAttributes;

        private EtymologicalLinkAttributes(String etyLinkAttributes) {
            this.etyLinkAttributes = etyLinkAttributes;
        }

        @Override
        public String toString() {
            return this.etyLinkAttributes;
        }

    }

    public static enum GenericRelationDecomp {
        label(SparqlPrefix.RDFS.getUri() + "label"),
        note(SparqlPrefix.SKOS.getUri() + "note"),
        Confidence(SparqlPrefix.LEXINFO.getUri() + "confidence");

        private final String genericRelationDecomp;

        private GenericRelationDecomp(String genericRelationDecomp) {
            this.genericRelationDecomp = genericRelationDecomp;
        }

        @Override
        public String toString() {
            return this.genericRelationDecomp;
        }

    }

}
