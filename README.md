# LexO-server: _REST services for Linguistic Linked Data_ 

[![Build Status](images/build-passing.png)](https://github.com/andreabellandi/LexO-backend) [![N|Solid](images/clarin.png)](https://ilc4clarin.ilc.cnr.it/) 

LexO-server is a software backend providing REST services for building and managing linguistic resources in the context of the Semantic Web, in particular:
- lexical and terminological resources based on the [_OntoLex-Lemon_](https://www.w3.org/2016/05/ontolex/) model;
- lexicograohic dictionaries (dictionaries) based on the [_Lexicog_](https://www.w3.org/2019/09/lexicog/) model.

LexO-server uses the [Swagger](https://swagger.io/) open source tool. It helps one to design and to document APIs at scale, for easing and supporting the front end GUI development process

## Features

- Targeted for web apps oriented at different lexicographic-based tasks, such as editing, linking, data visualization, dictionary making, linguistic annotation
- Lexical level implemented by the [_OntoLex-Lemon_](https://www.w3.org/2016/05/ontolex/) and the [_Lexicog_](https://www.w3.org/2019/09/lexicog/) models.
- Conceptual level implemented by the [_SKOS_](https://www.w3.org/2004/02/skos/) model 
- Integrated user authentication addressed by [KeyCloak](https://www.keycloak.org/) 
- Possibility to manage bibliographical items with [Zotero](https://www.zotero.org/) 
- Integration with remote SPARQL endpoints 
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
3. Download the project.
4. Edit the pom.xml file, as follows:

```     
    <profile>
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

   where _graphdb\_intallation\_url_ is the url of your GraphDB installation (typically on port 7200), and _repo\_name_ is the name of the repository to connect to.

5. Compile the project with Maven.
6. Run the build.
7. Open the browser at http://localhost:8080/LexO-backend/, and the swagger sholud appear.

## License

MIT

**Free Software, Hell Yeah!**

