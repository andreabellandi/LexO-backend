package it.cnr.ilc.lexo.manager.text;

import it.cnr.ilc.lexo.manager.text.model.Heading;
import it.cnr.ilc.lexo.manager.text.model.Paragraph;
import it.cnr.ilc.lexo.manager.text.model.ParsedTextDocument;
import it.cnr.ilc.lexo.manager.text.model.Sentence;
import it.cnr.ilc.lexo.manager.text.model.Token;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.XSD;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

/** Builds and serializes the RDF/NIF model using the RDF4J already used by LexO-server. */
public final class NifModelWriter {

    private static final String NIF_NS = "http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#";
    private static final String DCTERMS_NS = "http://purl.org/dc/terms/";
    private static final String DCMITYPE_NS = "http://purl.org/dc/dcmitype/";
    private static final String DOCO_NS = "http://purl.org/spar/doco/";
    private static final String PROV_NS = "http://www.w3.org/ns/prov#";

    private final ValueFactory vf = SimpleValueFactory.getInstance();
    private final String publicBaseUri;
    private final String structureNamespace;

    public NifModelWriter(String publicBaseUri, String structureNamespace) {
        this.publicBaseUri = ensureTrailingSeparator(publicBaseUri,
                "https://lexo.ilc.cnr.it/resources/texts/");
        this.structureNamespace = ensureNamespace(structureNamespace,
                "https://lexo.ilc.cnr.it/vocabulary/nif-structure#");
    }

    public String documentUri(String fileId) {
        return publicBaseUri + encode(fileId);
    }

    public void write(Path output, String fileId, String originalFileName,
                      ParsedTextDocument doc) throws IOException {
        Files.createDirectories(output.getParent());
        Model model = build(fileId, originalFileName, doc);
        try (OutputStream out = Files.newOutputStream(output)) {
            Rio.write(model, out, RDFFormat.TURTLE);
        }
    }

    public Model build(String fileId, String originalFileName, ParsedTextDocument doc) {
        Model model = new LinkedHashModel();
        model.setNamespace("rdf", RDF.NAMESPACE);
        model.setNamespace("xsd", XSD.NAMESPACE);
        model.setNamespace("dcterms", DCTERMS_NS);
        model.setNamespace("dcmitype", DCMITYPE_NS);
        model.setNamespace("nif", NIF_NS);
        model.setNamespace("doco", DOCO_NS);
        model.setNamespace("prov", PROV_NS);
        model.setNamespace("nifs", structureNamespace);

        String document = documentUri(fileId);
        IRI source = iri(document + "/source");
        IRI conllu = iri(document + "/conllu");
        IRI context = iri(document + "#context");
        String language = safeLanguageTag(doc.metadata.get("language"));

        addType(model, source, DCMITYPE_NS + "Text");
        addLiteral(model, source, DCTERMS_NS + "identifier", originalFileName, null);
        addLiteral(model, source, DCTERMS_NS + "format", mediaTypeFor(originalFileName), null);
        addLiteral(model, source, structureNamespace + "fileId", fileId, null);

        if (doc.conlluFileName != null) {
            addType(model, conllu, DCMITYPE_NS + "Dataset");
            addLiteral(model, conllu, DCTERMS_NS + "identifier", doc.conlluFileName, null);
            addLiteral(model, conllu, DCTERMS_NS + "format", "text/x-conllu", null);
            addLiteral(model, conllu, structureNamespace + "fileId", fileId, null);
        }

        addType(model, context, DCMITYPE_NS + "Text");
        addType(model, context, NIF_NS + "Context");
        addType(model, context, NIF_NS + "OffsetBasedString");
        addIri(model, context, PROV_NS + "wasDerivedFrom", source);
        if (doc.conlluFileName != null) {
            addIri(model, context, PROV_NS + "wasDerivedFrom", conllu);
        }
        addLiteral(model, context, structureNamespace + "fileId", fileId, null);
        addLiteral(model, context, structureNamespace + "segmentationMethod", doc.segmentationMethod, null);
        model.add(context, iri(structureNamespace + "frontMatterPresent"),
                vf.createLiteral(doc.frontMatterPresent));
        writeMetadata(model, context, doc.metadata, language);
        addLiteral(model, context, NIF_NS + "isString", doc.cleanText, language);
        addNonNegativeInteger(model, context, NIF_NS + "beginIndex", 0);
        addNonNegativeInteger(model, context, NIF_NS + "endIndex",
                codePointOffset(doc.cleanText, doc.cleanText.length()));

        for (Heading heading : doc.allHeadings) {
            writeHeading(model, document, context, doc.cleanText, heading, language);
        }
        for (Paragraph paragraph : doc.paragraphs) {
            writeParagraph(model, document, context, doc.cleanText, paragraph, language);
        }
        for (Sentence sentence : doc.sentences) {
            writeSentence(model, document, context, doc.cleanText, sentence, language);
        }
        for (Token token : doc.tokens) {
            writeToken(model, document, context, doc.cleanText, token, language);
        }
        return model;
    }

    private void writeMetadata(Model model, Resource context, Map<String, String> metadata,
                               String language) {
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            String key = entry.getKey().toLowerCase(Locale.ROOT);
            String value = entry.getValue();
            if ("title".equals(key)) {
                addLiteral(model, context, DCTERMS_NS + "title", value, language);
            } else if ("creator".equals(key) || "author".equals(key)) {
                addLiteral(model, context, DCTERMS_NS + "creator", value, null);
            } else if ("language".equals(key)) {
                addLiteral(model, context, DCTERMS_NS + "language", value, null);
            } else if ("date".equals(key) || "issued".equals(key)) {
                addLiteral(model, context, DCTERMS_NS + "issued", value, null);
            } else if ("license".equals(key)) {
                addIriOrLiteral(model, context, DCTERMS_NS + "license", value);
            } else if ("source".equals(key)) {
                addIriOrLiteral(model, context, DCTERMS_NS + "source", value);
            } else if ("id".equals(key)) {
                addLiteral(model, context, DCTERMS_NS + "identifier", value, null);
            } else {
                addLiteral(model, context, structureNamespace + "metadataValue",
                        key + "=" + value, null);
            }
        }
    }

    private void writeHeading(Model model, String document, IRI context, String fullText,
                              Heading heading, String language) {
        IRI headingIri = headingIri(document, heading);
        IRI titleIri = iri(headingIri.stringValue() + "/title");
        addType(model, headingIri, heading.level == 1 ? DOCO_NS + "Chapter" : DOCO_NS + "Section");
        addType(model, headingIri, NIF_NS + "Structure");
        addType(model, headingIri, NIF_NS + "OffsetBasedString");
        addLiteral(model, headingIri, DCTERMS_NS + "identifier", heading.id, null);
        addLiteral(model, headingIri, DCTERMS_NS + "title", heading.text, language);
        model.add(headingIri, iri(structureNamespace + "headingLevel"),
                vf.createLiteral(heading.level));
        if (heading.number != null) {
            addLiteral(model, headingIri, structureNamespace + "headingNumber", heading.number, null);
        }
        addIri(model, headingIri, DCTERMS_NS + "isPartOf",
                heading.parent == null ? context : headingIri(document, heading.parent));
        addIri(model, headingIri, NIF_NS + "referenceContext", context);
        addLiteral(model, headingIri, NIF_NS + "anchorOf",
                fullText.substring(heading.beginChar, heading.endChar), language);
        addNonNegativeInteger(model, headingIri, NIF_NS + "beginIndex",
                codePointOffset(fullText, heading.beginChar));
        addNonNegativeInteger(model, headingIri, NIF_NS + "endIndex",
                codePointOffset(fullText, heading.endChar));

        addType(model, titleIri,
                heading.level == 1 ? DOCO_NS + "ChapterTitle" : DOCO_NS + "SectionTitle");
        addType(model, titleIri, NIF_NS + "Title");
        addType(model, titleIri, NIF_NS + "OffsetBasedString");
        addIri(model, titleIri, DCTERMS_NS + "isPartOf", headingIri);
        addIri(model, titleIri, structureNamespace + "heading", headingIri);
        addIri(model, titleIri, NIF_NS + "referenceContext", context);
        addLiteral(model, titleIri, NIF_NS + "anchorOf", heading.titleSegment.text, language);
        addNonNegativeInteger(model, titleIri, NIF_NS + "beginIndex",
                codePointOffset(fullText, heading.titleSegment.beginChar));
        addNonNegativeInteger(model, titleIri, NIF_NS + "endIndex",
                codePointOffset(fullText, heading.titleSegment.endChar));
    }

    private void writeParagraph(Model model, String document, IRI context, String fullText,
                                Paragraph paragraph, String language) {
        IRI paragraphIri = iri(document + "#paragraph=" + paragraph.ordinal);
        addType(model, paragraphIri, DOCO_NS + "Paragraph");
        addType(model, paragraphIri, NIF_NS + "Paragraph");
        addType(model, paragraphIri, NIF_NS + "OffsetBasedString");
        addIri(model, paragraphIri, DCTERMS_NS + "isPartOf", headingIri(document, paragraph.heading));
        writeHeadingMembership(model, paragraphIri, document, paragraph.heading);
        addIri(model, paragraphIri, NIF_NS + "referenceContext", context);
        addLiteral(model, paragraphIri, NIF_NS + "anchorOf", paragraph.text, language);
        addNonNegativeInteger(model, paragraphIri, NIF_NS + "beginIndex",
                codePointOffset(fullText, paragraph.beginChar));
        addNonNegativeInteger(model, paragraphIri, NIF_NS + "endIndex",
                codePointOffset(fullText, paragraph.endChar));
    }

    private void writeSentence(Model model, String document, IRI context, String fullText,
                               Sentence sentence, String language) {
        IRI sentenceIri = charIri(document, fullText, sentence.beginChar, sentence.endChar);
        Heading directHeading = sentence.heading != null
                ? sentence.heading : sentence.paragraph.heading;
        IRI container = sentence.paragraph != null
                ? iri(document + "#paragraph=" + sentence.paragraph.ordinal)
                : iri(headingIri(document, directHeading).stringValue() + "/title");
        addType(model, sentenceIri, NIF_NS + "Sentence");
        addType(model, sentenceIri, NIF_NS + "OffsetBasedString");
        addIri(model, sentenceIri, DCTERMS_NS + "isPartOf", container);
        writeHeadingMembership(model, sentenceIri, document, directHeading);
        if (sentence.inHeadingTitle) {
            addLiteral(model, sentenceIri, structureNamespace + "structuralContainer",
                    "heading-title", null);
        }
        addIri(model, sentenceIri, NIF_NS + "referenceContext", context);
        addLiteral(model, sentenceIri, NIF_NS + "anchorOf", sentence.text, language);
        if (sentence.conlluSentId != null) {
            addLiteral(model, sentenceIri, structureNamespace + "conlluSentId",
                    sentence.conlluSentId, null);
        }
        addNonNegativeInteger(model, sentenceIri, NIF_NS + "beginIndex",
                codePointOffset(fullText, sentence.beginChar));
        addNonNegativeInteger(model, sentenceIri, NIF_NS + "endIndex",
                codePointOffset(fullText, sentence.endChar));
    }

    private void writeToken(Model model, String document, IRI context, String fullText,
                            Token token, String language) {
        IRI tokenIri = charIri(document, fullText, token.beginChar, token.endChar);
        IRI sentenceIri = charIri(document, fullText,
                token.sentence.beginChar, token.sentence.endChar);
        Heading directHeading = token.sentence.heading != null
                ? token.sentence.heading : token.sentence.paragraph.heading;
        addType(model, tokenIri, NIF_NS + "Word");
        addType(model, tokenIri, NIF_NS + "OffsetBasedString");
        addIri(model, tokenIri, DCTERMS_NS + "isPartOf", sentenceIri);
        addIri(model, tokenIri, NIF_NS + "sentence", sentenceIri);
        writeHeadingMembership(model, tokenIri, document, directHeading);
        addIri(model, tokenIri, NIF_NS + "referenceContext", context);
        addLiteral(model, tokenIri, NIF_NS + "anchorOf", token.text, language);
        addOptionalLiteral(model, tokenIri, structureNamespace + "conlluId", token.conlluId, null);
        addOptionalLiteral(model, tokenIri, NIF_NS + "lemma", token.lemma, language);
        addOptionalLiteral(model, tokenIri, structureNamespace + "upos", token.upos, null);
        addOptionalLiteral(model, tokenIri, structureNamespace + "xpos", token.xpos, null);
        addOptionalLiteral(model, tokenIri, structureNamespace + "feats", token.feats, null);
        addOptionalLiteral(model, tokenIri, structureNamespace + "head", token.head, null);
        addOptionalLiteral(model, tokenIri, structureNamespace + "deprel", token.deprel, null);
        addOptionalLiteral(model, tokenIri, structureNamespace + "deps", token.deps, null);
        addOptionalLiteral(model, tokenIri, structureNamespace + "misc", token.misc, null);
        addNonNegativeInteger(model, tokenIri, NIF_NS + "beginIndex",
                codePointOffset(fullText, token.beginChar));
        addNonNegativeInteger(model, tokenIri, NIF_NS + "endIndex",
                codePointOffset(fullText, token.endChar));
    }

    private void writeHeadingMembership(Model model, Resource subject, String document,
                                        Heading directHeading) {
        if (directHeading == null) {
            return;
        }
        IRI direct = headingIri(document, directHeading);
        addIri(model, subject, structureNamespace + "directHeading", direct);
        addIri(model, subject, structureNamespace + "inHeading", direct);
        Heading current = directHeading.parent;
        while (current != null) {
            addIri(model, subject, structureNamespace + "inAncestorHeading",
                    headingIri(document, current));
            current = current.parent;
        }
        Heading chapter = directHeading;
        while (chapter.parent != null) {
            chapter = chapter.parent;
        }
        addIri(model, subject, structureNamespace + "inChapter", headingIri(document, chapter));
    }

    private IRI headingIri(String document, Heading heading) {
        return iri(document + "#heading=" + encode(heading.id));
    }

    private IRI charIri(String document, String fullText, int beginChar, int endChar) {
        return iri(document + "#char=" + codePointOffset(fullText, beginChar)
                + "," + codePointOffset(fullText, endChar));
    }

    private void addType(Model model, Resource subject, String classIri) {
        model.add(subject, RDF.TYPE, iri(classIri));
    }

    private void addIri(Model model, Resource subject, String predicateIri, Resource object) {
        model.add(subject, iri(predicateIri), object);
    }

    private void addLiteral(Model model, Resource subject, String predicateIri,
                            String value, String language) {
        if (value == null) {
            return;
        }
        Literal literal = language == null
                ? vf.createLiteral(value) : vf.createLiteral(value, language);
        model.add(subject, iri(predicateIri), literal);
    }

    private void addOptionalLiteral(Model model, Resource subject, String predicateIri,
                                    String value, String language) {
        if (value != null && !value.isEmpty()) {
            addLiteral(model, subject, predicateIri, value, language);
        }
    }

    private void addNonNegativeInteger(Model model, Resource subject, String predicateIri, int value) {
        model.add(subject, iri(predicateIri),
                vf.createLiteral(Integer.toString(value), XSD.NON_NEGATIVE_INTEGER));
    }

    private void addIriOrLiteral(Model model, Resource subject, String predicateIri, String value) {
        if (looksLikeAbsoluteIri(value)) {
            addIri(model, subject, predicateIri, iri(value));
        } else {
            addLiteral(model, subject, predicateIri, value, null);
        }
    }

    private IRI iri(String value) {
        return vf.createIRI(value);
    }

    private static int codePointOffset(String text, int charOffset) {
        return text.codePointCount(0, charOffset);
    }

    private static String safeLanguageTag(String value) {
        if (value == null) {
            return null;
        }
        String tag = value.trim().replace('_', '-');
        return tag.matches("[A-Za-z]{2,8}(?:-[A-Za-z0-9]{1,8})*") ? tag : null;
    }

    private static boolean looksLikeAbsoluteIri(String value) {
        return value != null && value.matches("[A-Za-z][A-Za-z0-9+.-]*:.*");
    }

    private static String mediaTypeFor(String fileName) {
        if (fileName == null) {
            return "text/plain";
        }
        String lower = fileName.toLowerCase(Locale.ROOT);
        return lower.endsWith(".md") || lower.endsWith(".markdown")
                ? "text/markdown" : "text/plain";
    }

    private static String ensureTrailingSeparator(String value, String fallback) {
        String result = value == null || value.trim().isEmpty() ? fallback : value.trim();
        return result.endsWith("/") || result.endsWith("#") ? result : result + "/";
    }

    private static String ensureNamespace(String value, String fallback) {
        String result = value == null || value.trim().isEmpty() ? fallback : value.trim();
        return result.endsWith("#") || result.endsWith("/") || result.endsWith(":")
                ? result : result + "#";
    }

    private static String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8").replace("+", "%20");
        } catch (java.io.UnsupportedEncodingException impossible) {
            throw new IllegalStateException(impossible);
        }
    }
}
