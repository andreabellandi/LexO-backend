/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.service.helper;

import it.cnr.ilc.lexo.service.data.administration.output.SystemInfo;
import org.eclipse.rdf4j.query.BindingSet;

/**
 *
 * @author andreabellandi
 */
public class SystemInfoHelper extends TripleStoreDataHelper<SystemInfo> {

    @Override
    public void fillData(SystemInfo data, BindingSet bs) {
    }

    @Override
    public Class<SystemInfo> getDataClass() {
        return SystemInfo.class;
    }

}
