/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lexo.sparql;

/**
 *
 * @author andreabellandi
 */
public class SparqlRepositoryConfiguration {

    public static final String REPOSITORY_CONFIGURATION
            = "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>.\n"
            + "@prefix rep: <http://www.openrdf.org/config/repository#>.\n"
            + "@prefix sr: <http://www.openrdf.org/config/repository/sail#>.\n"
            + "@prefix sail: <http://www.openrdf.org/config/sail#>.\n"
            + "@prefix graphdb: <http://www.ontotext.com/config/graphdb#>.\n"
            + "\n"
            + "[] a rep:Repository ;\n"
            + "    rep:repositoryID \"_REPO_ID_\" ;\n"
            + "    rdfs:label \"_REPO_LABEL_\" ;\n"
            + "    rep:repositoryImpl [\n"
            + "        rep:repositoryType \"graphdb:SailRepository\" ;\n"
            + "        sr:sailImpl [\n"
            + "            sail:sailType \"graphdb:Sail\" ;\n"
            + "\n"
            + "            graphdb:base-URL \"_BASE_URL_\" ;\n"
            + "            graphdb:defaultNS \"\" ;\n"
            + "            graphdb:entity-index-size \"10000000\" ;\n"
            + "            graphdb:entity-id-size  \"32\" ;\n"
            + "            graphdb:imports \"\" ;\n"
            + "            graphdb:repository-type \"file-repository\" ;\n"
            + "            graphdb:ruleset \"_RULESET_\" ;\n"
            + "            graphdb:storage-folder \"storage\" ;\n"
            + "\n"
            + "            graphdb:enable-context-index \"false\" ;\n"
            + "\n"
            + "            graphdb:enablePredicateList \"true\" ;\n"
            + "\n"
            + "            graphdb:in-memory-literal-properties \"true\" ;\n"
            + "            graphdb:enable-literal-index \"true\" ;\n"
            + "\n"
            + "            graphdb:check-for-inconsistencies \"false\" ;\n"
            + "            graphdb:disable-sameAs  \"true\" ;\n"
            + "            graphdb:query-timeout  \"0\" ;\n"
            + "            graphdb:query-limit-results  \"0\" ;\n"
            + "            graphdb:throw-QueryEvaluationException-on-timeout \"false\" ;\n"
            + "            graphdb:read-only \"false\" ;\n"
            + "        ]\n"
            + "    ].\n"
            + "";

}
