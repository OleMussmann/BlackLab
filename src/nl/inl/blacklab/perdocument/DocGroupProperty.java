/*******************************************************************************
 * Copyright (c) 2010, 2012 Institute for Dutch Lexicology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package nl.inl.blacklab.perdocument;

import nl.inl.blacklab.search.grouping.HitPropValue;

/**
 * Abstract base class for a property of a hit, like document title, hit text, right context, etc.
 */
public abstract class DocGroupProperty {

	static DocGroupPropertyIdentity propIdentity = new DocGroupPropertyIdentity();

	static DocGroupPropertySize propSize = new DocGroupPropertySize();

	public static DocGroupPropertyIdentity identity() { return propIdentity; }

	public static DocGroupPropertySize size() { return propSize; }

	/** Reverse comparison result or not? */
	protected boolean reverse = false;

	public abstract HitPropValue get(DocGroup result);

	/**
	 * Compares two groups on this property
	 * @param a first group
	 * @param b second group
	 * @return 0 if equal, negative if a < b, positive if a > b.
	 */
	public abstract int compare(DocGroup a, DocGroup b);

	public boolean defaultSortDescending() {
		return reverse;
	}

	public abstract String serialize();

	/**
	 * Used by subclasses to add a dash for reverse when serializing
	 * @return either a dash or the empty string
	 */
	protected String serializeReverse() {
		return reverse ? "-" : "";
	}

	public static DocGroupProperty deserialize(String serialized) {
		boolean reverse = false;
		if (serialized.length() > 0 && serialized.charAt(0) == '-') {
			reverse = true;
			serialized = serialized.substring(1);
		}
		DocGroupProperty result;
		if (serialized.equalsIgnoreCase("identity"))
			result = propIdentity;
		else
			result = propSize;
		result.setReverse(reverse);
		return result;
	}

	/**
	 * Is the comparison reversed?
	 * @return true if it is, false if not
	 */
	public boolean isReverse() {
		return reverse;
	}

	/**
	 * Set whether to reverse the comparison.
	 * @param reverse if true, reverses comparison
	 */
	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}
}
