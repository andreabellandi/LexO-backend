# LexO-server: _REST services for Linguistic Linked Data_ 

[![Build Status](images/build-passing.png)](https://github.com/andreabellandi/LexO-backend) [![N|Solid](images/clarin.png)](https://ilc4clarin.ilc.cnr.it/) 

LexO-server is a software backend providing REST services for building and managing lexical and terminological resources in the context of the Semantic Web. 
It relies on the [_OntoLex-Lemon_](https://www.w3.org/2016/05/ontolex/) model.

Services can be consulted [here](https://lari2.ilc.cnr.it/LexO-backend-itant/) by the [Swagger](https://swagger.io/) interface 

## Features

- Targeted for web apps oriented at different lexicographic-based tasks, such as editing, linking, data visualization, dictionary making, linguistic annotation
- Lexical level implemented by the [_OntoLex-Lemon_](https://www.w3.org/2016/05/ontolex/) model
- Conceptual level implemented by the [_SKOS_](https://www.w3.org/2004/02/skos/) model
- Integrated user authentication addressed by [KeyCloak](https://www.keycloak.org/)
- Possibility to manage bibliographical items with [Zotero](https://www.zotero.org/)
- Integration with remote SPARQL endpoints
- Export data as Linked Data (RDF/XML, Turtle, N3, NQuads)

## Tech

LexO-server uses the following to work properly:

- [GraphDB Free](https://graphdb.ontotext.com/) - Semantic Graph Database, compliant with W3C Standards.
- [MySql](https://www.mysql.com/) - Open-source relational database management system (RDBMS)

## License

MIT

**Free Software, Hell Yeah!**

