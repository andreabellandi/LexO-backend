/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.util;

import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryException;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

/**
 *
 * @author andreabellandi
 */
public class RepositoryRegistry {

    private static final ConcurrentHashMap<String, Repository> REPOS = new ConcurrentHashMap<>();

    public static Repository create(String fileId) {
        Repository repo = new SailRepository(new MemoryStore());
        repo.init();
        REPOS.put(fileId, repo);
        return repo;
    }

    public static Repository get(String fileId) {
        return REPOS.get(fileId);
    }

    public static void remove(String fileId) throws Exception {
        Repository r = REPOS.remove(fileId);
        if (r != null) {
            try {
                r.shutDown();
            } catch (RepositoryException e) {
                throw e;
            }
        }
    }
}
