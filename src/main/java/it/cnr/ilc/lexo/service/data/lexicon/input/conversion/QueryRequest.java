/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.data.lexicon.input.conversion;

import it.cnr.ilc.lexo.service.data.Data;

/**
 *
 * @author andreabellandi
 */
public class QueryRequest implements Data {

    public String query;
    public Long timeoutMs;
    public Boolean includeInferred;
    public String resultFormat;
    public Long maxResultBytes;
    public Integer maxRows;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Long getTimeoutMs() {
        return timeoutMs;
    }

    public void setTimeoutMs(Long timeoutMs) {
        this.timeoutMs = timeoutMs;
    }

    public Boolean getIncludeInferred() {
        return includeInferred;
    }

    public void setIncludeInferred(Boolean includeInferred) {
        this.includeInferred = includeInferred;
    }

    public String getResultFormat() {
        return resultFormat;
    }

    public void setResultFormat(String resultFormat) {
        this.resultFormat = resultFormat;
    }

    public Long getMaxResultBytes() {
        return maxResultBytes;
    }

    public void setMaxResultBytes(Long maxResultBytes) {
        this.maxResultBytes = maxResultBytes;
    }

    public Integer getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(Integer maxRows) {
        this.maxRows = maxRows;
    }

}
