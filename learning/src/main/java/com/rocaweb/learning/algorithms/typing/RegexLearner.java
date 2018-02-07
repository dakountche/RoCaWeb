/*******************************************************************************
 * This file is part of RoCaWeb.
 * 
 *  RoCaWeb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 ******************************************************************************/
package com.rocaweb.learning.algorithms.typing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.rocaweb.commons.recording.LearningData;
import com.rocaweb.learning.rules.Type;

/**
 * Implements a Typing algorithm which assign a predefined type to a set of
 * values.
 * 
 * @author Yacine Tamoudi
 * @since 1.0.0
 */
public class RegexLearner extends AbstractTypingAlgorithm implements Types{

	
	private final static HashMap<String, Pattern> naivepatterns = new HashMap<String, Pattern>();

	static {
		naivepatterns.put("digits", digits);
		naivepatterns.put("digitsHex", digitsHex);
		naivepatterns.put("integer", integer);
		naivepatterns.put("decimal", decimal);
		naivepatterns.put("path", path);
		naivepatterns.put("url", url);
		naivepatterns.put("urlPath", urlPath);
		naivepatterns.put("urlPathQuery", urlPathQuery);
		naivepatterns.put("frenchphone", frenchphone);
		naivepatterns.put("usphone", usphone);
		naivepatterns.put("email", email);
		naivepatterns.put("password", password);
		naivepatterns.put("textOneLine", textOneLine);
		naivepatterns.put("textMultiLine", textMultiLine);
		naivepatterns.put("level1password", level1password);
		naivepatterns.put("level2password", level2password);
		naivepatterns.put("date", date);
		naivepatterns.put("ip", ip);
		naivepatterns.put("personalname", personalname);
		naivepatterns.put("guid", guid);

		naivepatterns.put("everything", everything);

		naivepatterns.put("alphanum", alphanum);
		naivepatterns.put("alpha", alpha);

		naivepatterns.put("base64", base64);

	}

	private static List<Pattern> orderedpatterns = new ArrayList<Pattern>();

	static {

		orderedpatterns.add(everything);

		orderedpatterns.add(textMultiLine);
		orderedpatterns.add(textOneLine);

		orderedpatterns.add(path);
		orderedpatterns.add(urlPath);
		orderedpatterns.add(url);

		orderedpatterns.add(email);

		orderedpatterns.add(digitsHex);

		orderedpatterns.add(base64);

		orderedpatterns.add(date);
		orderedpatterns.add(ip);

		orderedpatterns.add(alphanum);

		orderedpatterns.add(alpha);

		orderedpatterns.add(integer);
		orderedpatterns.add(decimal);
		orderedpatterns.add(digits);

		orderedpatterns.add(guid);

		orderedpatterns.add(frenchphone);
		orderedpatterns.add(usphone);
	}



	/**
	 * @param configFileName
	 */
	public RegexLearner(String configFileName) {
		super(configFileName);
	}

	public RegexLearner() {

	}

	public static Type NaiveTyping(List<String> list) {
		Type pl = null;

		if (list.size() > 0) {

			pl = new Type();

			pl.setName("");
			pl.setUri("");
			pl.setParametertype("");

			int lg = list.get(0).length();
			pl.setMinlength(lg);
			pl.setMaxlength(lg);

			for (String s : list) {
				lg = s.length();
				if (pl.getMinlength() > lg) {
					pl.setMinlength(lg);
				}
				if (pl.getMaxlength() < lg) {
					pl.setMaxlength(lg);
				}

			}
			for (Entry<String, Pattern> e : naivepatterns.entrySet()) {
				boolean validate = true;
				Pattern pat = e.getValue();

				for (String s : list) {
					validate = validate && pat.matcher(s).matches();

				}
				if (validate) {
					pl.setNaivepattern(e.getKey());
					pl.setLearned(true);
					pl.setNaivepatternweight(lg);
				}

			}

		}

		return pl;
	}

	private static Type hierarchisedNaiveTyping(String name, String uri, String type, List<String> list,
			boolean confine) {

		Type pl = null;

		if (list.size() > 0) {

			pl = new Type();

			pl.setName(name);
			pl.setUri(uri);
			pl.setParametertype(type);

			int lg = list.get(0).length();
			pl.setMinlength(lg);
			pl.setMaxlength(lg);

			for (String s : list) {
				lg = s.length();
				if (pl.getMinlength() > lg) {
					pl.setMinlength(lg);
				}
				if (pl.getMaxlength() < lg) {
					pl.setMaxlength(lg);
				}

			}
			Iterator<Pattern> it = orderedpatterns.iterator();
			// Pattern current = Pattern.compile("");
			List<Pattern> chosenones = new ArrayList<Pattern>();
			chosenones.add(Pattern.compile(".*"));

			while (it.hasNext()) {
				Pattern pat = it.next();

				boolean validate = true;
				for (String s : list) {
					validate = validate && pat.matcher(s).matches();

				}

				if (validate) {
					chosenones.add(pat);

				}

			}

			Pattern theone = chosenones.get(chosenones.size() - 1);

			String format = "%s(?<=[\\s\\S]{%s,%s})";
			String finalpattern;
			if (confine) {

				finalpattern = String.format(format, theone, pl.getMinlength(), pl.getMaxlength());
			} else {
				finalpattern = theone.toString();
			}

		
			pl.setNaivepattern(finalpattern);
			pl.setLearned(true);
			pl.setNaivepatternweight(lg);

		}

		return pl;
	}

	public static List<Type> HierarchisedMultiNaiveTyping(LearningData old, boolean confine) {

		HashMap<String, Integer> countformat = new HashMap<String, Integer>();

		List<Type> list = new ArrayList<Type>();

		for (Entry<String, HashMap<String, HashMap<String, List<String>>>> e : old.getHashMap().entrySet()) {

			for (Entry<String, HashMap<String, List<String>>> type : e.getValue().entrySet()) {

				for (Entry<String, List<String>> param : type.getValue().entrySet()) {
					Type pl = hierarchisedNaiveTyping(param.getKey(), e.getKey(), type.getKey(), param.getValue(),
							confine);

					String regex = reverseLookup(naivepatterns, pl.getNaivepattern());

					if (regex == null) {
						regex = "no_type";
					}

					if (countformat.containsKey(regex)) {
						countformat.put(regex, countformat.get(regex) + 1);
					} else {
						countformat.put(regex, 1);
					}

					list.add(pl);

				}

			}
		}

		System.out.println(countformat);

		return list;

	}

	@SuppressWarnings({ "hiding", "rawtypes", "unchecked" })
	private static <C, Pattern> C reverseLookup(HashMap<C, Pattern> hm, Object ob) {
		C rep = null;
		for (Entry e : hm.entrySet()) {
			if (e.getValue().toString().equals(ob)) {
				rep = (C) e.getKey();
			}
		}

		return rep;
	}

	@Override
	public String process(List<String> sequences) {
		logger.trace(sequences);

		if (sequences.isEmpty())
			return "";
		else if (sequences.size() == 1)
			return sequences.get(0);
		else {
			Type type = hierarchisedNaiveTyping("", "", "", sequences, getBounded());
			logger.trace(type);
			return type.getNaivepattern().toString();
		}

	}

	public Boolean getBounded() {
		return getParameters().getBounded();
	}

	public void setBounded(Boolean bounded) {
		this.getParameters().setBounded(bounded);
	}

	public static Map<String, Pattern> getNaivePattern() {
		return naivepatterns;
	}

}
