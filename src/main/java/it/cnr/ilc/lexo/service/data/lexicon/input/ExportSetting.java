/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;
import java.util.ArrayList;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Input model representing export settings")
public class ExportSetting implements Data {

    @ApiModelProperty(value = "file name of the export file", allowEmptyValue = true)
    private String fileName;
    @ApiModelProperty(value = "export format", allowableValues = "xml, jsonld, n3, ntriples, nquads, turtle", allowEmptyValue = true)
    private String format;
    @ApiModelProperty(value = "the IRI of the subject to export or null for all subjects", allowEmptyValue = true)
    private String subject;
    @ApiModelProperty(value = "the IRI of the predicate to export or null for all predicate", allowEmptyValue = true)
    private String predicate;
    @ApiModelProperty(value = "the IRI or the datatype value of the object to export or null for all objects", allowEmptyValue = true)
    private String object;
    @ApiModelProperty(value = "The context(s) to get the data from. If no contexts are supplied the service operates on the entire repository", allowEmptyValue = true)
    private ArrayList<String> context;
    @ApiModelProperty(value = "if inferred triples have to be included", allowEmptyValue = true)
    private boolean inferred;

    public ExportSetting() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public ArrayList<String> getContext() {
        return context;
    }

    public void setContext(ArrayList<String> context) {
        this.context = context;
    }

    public boolean isInferred() {
        return inferred;
    }

    public void setInferred(boolean inferred) {
        this.inferred = inferred;
    }

}
