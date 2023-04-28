/**
 * @author Enrico Carniani
 */

package it.cnr.ilc.lexo.service.conll;

import java.util.HashMap;
import java.util.Map;

public class Form {
	public final String FQName;
	public final String text;
	public final Map<String, String> features;

	public Form(String FQName, String text) {
		this.FQName = FQName.replaceAll("[\\.']", "-");;
		this.text = text;
		features = new HashMap<>();
	}
}
