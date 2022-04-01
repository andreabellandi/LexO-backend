/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.hibernate.entity;

/**
 *
 * @author andreabellandi
 */
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class ConfigurationParameter extends SuperEntity {

    private String key;
    private String value;

//    public enum LEXICALIZATION_TYPE {
//        LABEL,
//        SKOS,
//        SKOS_XL
//    }
//
//    private String zoteroEndpoint;
//    private String zoteroRequestPrefix;
//    private String zoteroLibrary;
//    private final int zoteroVersion = 3;
//
//    private String lexiconNamespace;
//    private String lexicalConceptNamespace;
//    private String bibliographyNamespace;
//    private String ontologyNamespace;
//
//    @Enumerated(EnumType.STRING)
//    private String skosLexicalizationModel;
//    private String skosDefaultLanguageLabel;
//    private String skosLanguages;
//
//    public String getZoteroEndpoint() {
//        return zoteroEndpoint;
//    }
//
//    public void setZoteroEndpoint(String zoteroEndpoint) {
//        this.zoteroEndpoint = zoteroEndpoint;
//    }
//
//    public String getZoteroRequestPrefix() {
//        return zoteroRequestPrefix;
//    }
//
//    public void setZoteroRequestPrefix(String zoteroRequestPrefix) {
//        this.zoteroRequestPrefix = zoteroRequestPrefix;
//    }
//
//    public String getZoteroLibrary() {
//        return zoteroLibrary;
//    }
//
//    public void setZoteroLibrary(String zoteroLibrary) {
//        this.zoteroLibrary = zoteroLibrary;
//    }
//
//    public String getLexiconNamespace() {
//        return lexiconNamespace;
//    }
//
//    public void setLexiconNamespace(String lexiconNamespace) {
//        this.lexiconNamespace = lexiconNamespace;
//    }
//
//    public String getLexicalConceptNamespace() {
//        return lexicalConceptNamespace;
//    }
//
//    public void setLexicalConceptNamespace(String lexicalConceptNamespace) {
//        this.lexicalConceptNamespace = lexicalConceptNamespace;
//    }
//
//    public String getBibliographyNamespace() {
//        return bibliographyNamespace;
//    }
//
//    public void setBibliographyNamespace(String bibliographyNamespace) {
//        this.bibliographyNamespace = bibliographyNamespace;
//    }
//
//    public String getOntologyNamespace() {
//        return ontologyNamespace;
//    }
//
//    public void setOntologyNamespace(String ontologyNamespace) {
//        this.ontologyNamespace = ontologyNamespace;
//    }
//
//    public String getSkosLexicalizationModel() {
//        return skosLexicalizationModel;
//    }
//
//    public void setSkosLexicalizationModel(String skosLexicalizationModel) {
//        this.skosLexicalizationModel = skosLexicalizationModel;
//    }
//
//    public String getSkosDefaultLanguageLabel() {
//        return skosDefaultLanguageLabel;
//    }
//
//    public void setSkosDefaultLanguageLabel(String skosDefaultLanguageLabel) {
//        this.skosDefaultLanguageLabel = skosDefaultLanguageLabel;
//    }
//
//    public String getSkosLanguages() {
//        return skosLanguages;
//    }
//
//    public void setSkosLanguages(String skosLanguages) {
//        this.skosLanguages = skosLanguages;
//    }
//
//    public int getZoteroVersion() {
//        return zoteroVersion;
//    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
