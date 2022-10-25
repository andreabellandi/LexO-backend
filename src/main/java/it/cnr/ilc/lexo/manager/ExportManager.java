/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.manager;

import it.cnr.ilc.lexo.LexOProperties;
import it.cnr.ilc.lexo.service.data.lexicon.input.ExportSetting;
import it.cnr.ilc.lexo.util.RDFQueryUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

/**
 *
 * @author andreabellandi
 */
public class ExportManager implements Manager, Cached {

    private final String idInstancePrefix = LexOProperties.getProperty("repository.instance.id");
    private static final SimpleDateFormat timestampFormat = new SimpleDateFormat(LexOProperties.getProperty("manager.operationTimestampFormat"));
    @Override
    public void reloadCache() {
    }

    public File export(ExportSetting set) throws ManagerException {
        return RDFQueryUtil.export(checkDefaultSettings(set));
    }
    
    private ExportSetting checkDefaultSettings(ExportSetting set) {
        set.setFormat(set.getFormat() != null ? set.getFormat() : "turtle");
        set.setFileName(set.getFileName()!= null ? set.getFileName() : "export");
        return set;
    }

}
