package org.pdfsam.pdf;


import java.util.Map;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

public class PdfDocumentDescriptorProduct {
	private Map<String, String> metadata = new HashMap<>();

	/**
	* @param key
	* @return  the information dictionary value for the key or an empty string
	*/
	public String getInformation(String key) {
		return StringUtils.defaultString(metadata.get(key));
	}

	public void setInformationDictionary(Map<String, String> info) {
		metadata.clear();
		metadata.putAll(info);
	}

	public void putInformation(String key, String value) {
		metadata.put(key, value);
	}
}