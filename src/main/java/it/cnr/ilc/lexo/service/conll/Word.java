/**
 * @author Enrico Carniani
 */

package it.cnr.ilc.lexo.service.conll;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Word {
	public String FQName;
	public Form canonicalForm;
	public String partOfSpeech;
	public String language;
	public String conceptFQN;
        final public Map<String, String> senses = new HashMap<>();
	final public Map<String, String> additionalInfo = new HashMap<>();
	final private Map<String, Form> otherForms = new HashMap<>();
	static final private IdGenerator idGenerator = new IdGenerator();

	public Word() {}

	public Word(String lemma, String partOfSpeech, String language) {
		reuse(lemma, partOfSpeech, language);
	}

	public void reuse(String lemma, String partOfSpeech, String language) {
		this.partOfSpeech = partOfSpeech;
		this.language = language;

		String FQName = idGenerator.getId(String.format("%s+%s+%s", lemma, partOfSpeech, language));

		if (partOfSpeech != null) {
			this.FQName = String.format(":le_%s_%s", FQName, partOfSpeech.split(":")[1]);
		} else {
			this.FQName = String.format(":le_%s", FQName);
		}

		String canonicalFormFQN = String.format("%s_lemma", this.FQName);
		canonicalForm = new Form(canonicalFormFQN, lemma);
		otherForms.clear();
                senses.clear();
		additionalInfo.clear();
	}

	public void addOtherForm(Form form) {
		otherForms.put(form.text, form);
	}

	public Collection<Form> getOtheForms() {
		return otherForms.values();
	}

	public Form findForm(String text) {
		if (canonicalForm.text.equals(text)) return canonicalForm;
		return otherForms.get(text);
	}

}
