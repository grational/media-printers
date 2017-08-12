package it.italiaonline.rnd.printers

/**
 * This class is used to build a CSV representation of an object
 */
class CsvMedia implements Media {

	private final Map accumulator
	private final String fieldsSeparator
	private final String defaultSeparator = ','

	/**
	 * Tertiary constructor: nothing passed. Use comma by default and
	 * accept just new parameters with 'with' method
	 */
	CsvMedia() {
		this(
			[:],
			',' // default separator
		)
	}

	/**
	 * Secondary constructor: use comma as separator by default
	 *
	 * @param acc a LinkedHashMap used to store the couples (key, value)
	 */
	CsvMedia(Map acc) {
		this(
			acc,
			',' // default separator
		)
	}

	/**
	 * Primary constructor
	 *
	 * @param acc a LinkedHashMap used to store the couples (key, value)
	 * @param fieldsSeparator a String used to separate fields
	 */
	CsvMedia(
		Map acc,
		String fieldsSeparator
		) {
		this.accumulator = acc
		this.fieldsSeparator = fieldsSeparator
	}

	/**
	 * This method load a couple (key, value) into the class accumulator.
	 * Actually generate a new CsvMedia class incremented with the additional values
	 *
	 * @param name  a LinkedHashMap used to store the couples (key, value)
	 * @param value  a LinkedHashMap used to store the couples (key, value)
	 * @return Media  return an object CsvMedia that contains all the couples
	 */
	@Override
	Media with(String name, String value) {
		this.accumulator.put(name, value)
		new CsvMedia(
			this.accumulator,
			this.fieldsSeparator
		)
	}

	/**
	 * Returns a CSV representation of all the keys
	 *
	 * @return String  a csv row composed by the keys in the accumulator
	 */
	String header() {
		csvify(this.accumulator.keySet())
	}

	/**
	 * Returns a CSV representation of all the values
	 *
	 * @return String  a csv row composed by the values in the accumulator
	 * Rules:
	 * - MS-DOS-style lines that end with (CR/LF) characters (optional for the last line).
	 * - An optional header record (there is no sure way to detect whether it is present, so care is required when importing).
	 * - Each record "should" contain the same number of comma-separated fields.
	 * - Any field may be quoted (with double quotes).
	 * - Fields containing a line-break, double-quote or commas should be quoted.
	 * - A (double) quote character in a field must be represented by two (double) quote characters.
	 */
	String row() {
		csvify(this.accumulator.values())
	}

	private String csvify(Collection c) {
		c.collect {
			def field = it.replaceAll('"','""')
			(field =~ /[\s"${fieldsSeparator}]/) ?  /"${field}"/ : field
		}.join(fieldsSeparator)
	}
}
