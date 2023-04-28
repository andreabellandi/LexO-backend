/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Input model representing lexical concept filter options")
public class LexicalConceptFilter implements Data {

    @ApiModelProperty(value = "chars sequence to search", allowEmptyValue = true)
    private String text;
    @ApiModelProperty(value = "search type to perform (it cannot be empty)", example = "startsWith", allowableValues = "equals, startsWith, contains, endsWith", allowEmptyValue = false)
    private String searchMode;
    @ApiModelProperty(value = "the type of label the search is performed on",
            allowableValues = "prefLabel, hiddenLabel, alternativeLabel", allowEmptyValue = false)
    private String labelType;
    @ApiModelProperty(value = "author (empty means all)", allowEmptyValue = true)
    private String author;
    @ApiModelProperty(value = "result set offset", example = "0", allowEmptyValue = false)
    private int offset;
    @ApiModelProperty(value = "result set limit", example = "500", allowEmptyValue = false)
    private int limit;

    public LexicalConceptFilter() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSearchMode() {
        return searchMode;
    }

    public void setSearchMode(String searchMode) {
        this.searchMode = searchMode;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getLabelType() {
        return labelType;
    }

    public void setLabelType(String labelType) {
        this.labelType = labelType;
    }

}
