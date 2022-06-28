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

@Entity
public class Application_Settings extends SuperEntity {

    private String repoUrl;
    private String repoName;
    private String repoPoolSize;
    private String jsonDate;
    private String timestamp;
    private String prettyPrint;
    private String nsId;
    private String nsLexicon;
    private String nsLexicalConcept;
    private String nsOntology;
    private String nsBibliography;

    public Application_Settings() {
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getRepoPoolSize() {
        return repoPoolSize;
    }

    public void setRepoPoolSize(String repoPoolSize) {
        this.repoPoolSize = repoPoolSize;
    }

    public String getJsonDate() {
        return jsonDate;
    }

    public void setJsonDate(String jsonDate) {
        this.jsonDate = jsonDate;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(String prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public String getNsId() {
        return nsId;
    }

    public void setNsId(String nsId) {
        this.nsId = nsId;
    }

    public String getNsLexicon() {
        return nsLexicon;
    }

    public void setNsLexicon(String nsLexicon) {
        this.nsLexicon = nsLexicon;
    }

    public String getNsLexicalConcept() {
        return nsLexicalConcept;
    }

    public void setNsLexicalConcept(String nsLexicalConcept) {
        this.nsLexicalConcept = nsLexicalConcept;
    }

    public String getNsOntology() {
        return nsOntology;
    }

    public void setNsOntology(String nsOntology) {
        this.nsOntology = nsOntology;
    }

    public String getNsBibliography() {
        return nsBibliography;
    }

    public void setNsBibliography(String nsBibliography) {
        this.nsBibliography = nsBibliography;
    }

    public Application_Settings(String repoUrl, String repoName, String repoPoolSize, String jsonDate, String timestamp, String prettyPrint, String nsId, String nsLexicon, String nsLexicalConcept, String nsOntology, String nsBibliography) {
        this.repoUrl = repoUrl;
        this.repoName = repoName;
        this.repoPoolSize = repoPoolSize;
        this.jsonDate = jsonDate;
        this.timestamp = timestamp;
        this.prettyPrint = prettyPrint;
        this.nsId = nsId;
        this.nsLexicon = nsLexicon;
        this.nsLexicalConcept = nsLexicalConcept;
        this.nsOntology = nsOntology;
        this.nsBibliography = nsBibliography;
    }

}
