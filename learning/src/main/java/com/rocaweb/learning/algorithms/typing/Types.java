package com.rocaweb.learning.algorithms.typing;

import java.util.regex.Pattern;

public interface Types {
	
	public final static Pattern digits = Pattern.compile("[0-9]+");
	public final static Pattern digitsHex = Pattern.compile("[0-9a-fA-F]+");
	public final static Pattern integer = Pattern.compile("[-+]*[0-9]+");
	public final static Pattern decimal = Pattern.compile("[-+]*[0-9]+\\.[0-9]+");

	public final static Pattern alphanum = Pattern.compile("[0-9a-zA-Z]+");

	public final static Pattern alpha = Pattern.compile("[a-zA-Z]+");
	// public final static Pattern alphamaj = Pattern.compile("[A-Z]+");
	// public final static Pattern alphamin = Pattern.compile("[a-z]+");

	public final static Pattern path = Pattern.compile("[-a-zA-Z0-9/._]*/[-a-zA-Z0-9/._]*");
	public final static Pattern url = Pattern.compile("[A-Za-z]+://[A-Za-z0-9-_]+\\.[A-Za-z0-9-_.]+/?");
	public final static Pattern urlPath = Pattern
			.compile("[A-Za-z]+://[A-Za-z0-9-_]+\\.[A-Za-z0-9-_./]+\\?[A-Za-z0-9-_%&/.=]*");
	public final static Pattern urlPathQuery = Pattern
			.compile("[A-Za-z]+://[A-Za-z0-9-_]+\\.[A-Za-z0-9-_./]+\\?[A-Za-z0-9-_%&/.=]*");
	public final static Pattern frenchphone = Pattern
			.compile("^0[1-6]{1}(([0-9]{2}){4})|((\\s[0-9]{2}){4})|((-[0-9]{2}){4})$");
	public final static Pattern usphone = Pattern
			.compile("^([10]?-)*((\\(\\d{3}\\)?)|(\\d{3}))([\\s-./]+)(\\d{3})([\\s-./]+)(\\d{4})$");
	// public final static Pattern phoneInt = Pattern.compile("\\+?[-0-9. ]+");
	// static Pattern zip=Pattern.compile("[-0-9a-zA-Z]+");
	// static Pattern email=Pattern.compile("\\s*[-a-zA-Z0-9_.@]+\\s*");
	public final static Pattern email = Pattern.compile(
			"^[_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.(([0-9]{1,3})|([a-zA-Z]{2,3})|(aero|coop|info|museum|name))$");
	public final static Pattern password = Pattern.compile("\\s*[\\x09\\x20-\\xff]+\\s*");
	public final static Pattern textOneLine = Pattern.compile("\\s*[\\x09\\x20-\\xff]*\\s*");
	// static Pattern
	// textOneLinesave=Pattern.compile("\\s*[\\x09\\x20-\\xff]+\\s*");

	public final static Pattern textMultiLine = Pattern.compile("\\s*[\\x09\\x0a\\x0d\\x20-\\xff]+\\s*");
	public final static Pattern level1password = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,}$");
	public final static Pattern level2password = Pattern
			.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{8,}$");
	public final static Pattern date = Pattern.compile(
			"(?=\\d)^(?:(?!(?:10\\D(?:0?[5-9]|1[0-4])\\D(?:1582))|(?:0?9\\D(?:0?[3-9]|1[0-3])\\D(?:1752)))((?:0?[13578]|1[02])|(?:0?[469]|11)(?!\\/31)(?!-31)(?!\\.31)|(?:0?2(?=.?(?:(?:29.(?!000[04]|(?:(?:1[^0-6]|[2468][^048]|[3579][^26])00))(?:(?:(?:\\d\\d)(?:[02468][048]|[13579][26])(?!\\x20BC))|(?:00(?:42|3[0369]|2[147]|1[258]|09)\\x20BC))))))|(?:0?2(?=.(?:(?:\\d\\D)|(?:[01]\\d)|(?:2[0-8])))))([-.\\/ ])(0?[1-9]|[12]\\d|3[01])\2(?!0000)((?=(?:00(?:4[0-5]|[0-3]?\\d)\\x20BC)|(?:\\d{4}(?!\\x20BC)))\\d{4}(?:\\x20BC)?)(?:$|(?=\\x20\\d)\\x20))?((?:(?:0?[1-9]|1[012])(?::[0-5]\\d){0,2}(?:\\x20[aApP][mM]))|(?:[01]\\d|2[0-3])(?::[0-5]\\d){1,2})?$");
	public final static Pattern ip = Pattern.compile(
			"^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$");
	public final static Pattern personalname = Pattern.compile("^[a-zA-Z]+(([\\'\\,\\.\\- ][a-zA-Z ])?[a-zA-Z]*)*$");
	public final static Pattern guid = Pattern
			.compile("^[A-Za-z0-9]{8}-[A-Za-z0-9]{4}-[A-Za-z0-9]{4}-[A-Za-z0-9]{4}-[A-Za-z0-9]{12}$");

	public final static Pattern base64 = Pattern
			.compile("^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$");

	// public final static Pattern nothing = Pattern.compile("^$");
	public final static Pattern everything = Pattern.compile("[\\s\\S]*");


}
