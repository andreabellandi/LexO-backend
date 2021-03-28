/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
@ApiModel(description = "Class representing lexical entries filter options (input for lexicon/lexicalEntries service)")
public class LexicalEntryFilter implements Data {

    @ApiModelProperty(value = "chars sequence to search", example = "a")
    private String text;
    @ApiModelProperty(value = "starts with the given chars sequence", example = "true")
    private boolean startsWith;
    @ApiModelProperty(value = "contains the given chars sequence", example = "false")
    private boolean contains;
    @ApiModelProperty(value = "ends the given chars sequence", example = "false")
    private boolean ends;
    @ApiModelProperty(value = "lexcial entry types", example = "word", allowableValues = "word, multi-word expression, affix")
    private String type;
    @ApiModelProperty(value = "parts of speech", example = "verb")
    private String pos;
    @ApiModelProperty(value = "author", example = "user3")
    private String author;
    @ApiModelProperty(value = "language (empty for all languages)", example = "it")
    private String lang;
    @ApiModelProperty(value = "verified", example = "false")
    private boolean verified;
    @ApiModelProperty(value = "result set offset", example = "0")
    private int offset;
    @ApiModelProperty(value = "result set limit", example = "500")
    private int limit;

    public LexicalEntryFilter() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public boolean isStartsWith() {
        return startsWith;
    }

    public void setStartsWith(boolean startsWith) {
        this.startsWith = startsWith;
    }

    public boolean isContains() {
        return contains;
    }

    public void setContains(boolean contains) {
        this.contains = contains;
    }

    public boolean isEnds() {
        return ends;
    }

    public void setEnds(boolean ends) {
        this.ends = ends;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
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

}
