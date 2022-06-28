# LexO-server: _REST services for Linguistic Linked Data_ 

[![Build Status](images/build-passing.png)](https://github.com/andreabellandi/LexO-backend) [![N|Solid](images/clarin.png)](https://ilc4clarin.ilc.cnr.it/) 

LexO-server is a software backend providing REST services for building and managing lexical and terminological resources in the context of the Semantic Web. 
It relies on the [_OntoLex-Lemon_](https://www.w3.org/2016/05/ontolex/) model.

Services can be consulted [here](https://lari2.ilc.cnr.it/LexO-backend-itant/) via a [Swagger](https://swagger.io/) interface 

## Features

- Targeted for web apps oriented at different lexicographic-based tasks, such as editing, linking, data visualization, dictionary making, linguistic annotation
- Lexical level implemented by the [_OntoLex-Lemon_](https://www.w3.org/2016/05/ontolex/) model
- Conceptual level implemented by the [_SKOS_](https://www.w3.org/2004/02/skos/) model 
- Integrated user authentication addressed by [KeyCloak](https://www.keycloak.org/) (forthcoming)
- Possibility to manage bibliographical items with [Zotero](https://www.zotero.org/) (forthcoming)
- Integration with remote SPARQL endpoints (forthcoming)
- Export data as Linked Data (RDF/XML, Turtle, N3, NQuads)

## Tech

LexO-server uses the following technology to work properly:

- Java 15 or later
- Apache Tomcat 9 or later
- [GraphDB Free](https://graphdb.ontotext.com/) - Semantic Graph Database, compliant with W3C Standards.
- [MySql](https://www.mysql.com/) - Open-source relational database management system (RDBMS)

## Installation

1. [Install](https://graphdb.ontotext.com/documentation/free/quick-start-guide.html) GraphDB. 
2. [Create](https://graphdb.ontotext.com/documentation/free/creating-a-repository.html) an empty GraphDB repository with default values.
2. Download the project.
3. Edit the pom.xml file, as follows:

```     <profile>
            <id>release</id>
            <properties>
                <db.jdbcUrl>leave_empty</db.jdbcUrl>
                <db.user>leave_empty</db.user>
                <db.password>leave_empty</db.password>
                <graphdb.url>$graphdb_intallation_url$</graphdb.url>
                <graphdb.repository>$repo_name$</graphdb.repository>
                <graphdb.poolSize>5</graphdb.poolSize>
            </properties>
        </profile>
```
   where $graphdb_intallation_url$ is the url of your GrapDB installation (typically on port 7200), and $repo_name$ is the name of the repository to connect to.
4. Compile the project with Maven.
5. Run the build.
6. Open the browser at http://localhost:8080/LexO-backend/, and the swagger sholud appear.

## License

MIT

**Free Software, Hell Yeah!**

