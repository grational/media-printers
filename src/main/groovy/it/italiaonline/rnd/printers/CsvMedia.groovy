package it.italiaonline.rnd.printers

// internal deps
import it.italiaonline.rnd.transform.Transformation
import it.italiaonline.rnd.transform.Identity

/**
 * This class is used to build a CSV representation of an object
 */
class CsvMedia implements Media {

	private final Map            fields
	private final String         separator
	private final Transformation transformation

	/**
	 * Groovy-like constructor, just a map with parameters is passed.
	 * <p>
	 * @param acc a LinkedHashMap used to store the couples (key, value)
	 * @param separator a String used to separate fields
	 */
	CsvMedia(Map params) {
		this.fields         = params?.fields         ?: [:]
		this.separator      = params?.separator      ?: ','
		this.transformation = params?.transformation ?: new Identity()
	}

	/**
	 * This method load a couple (key, value) into the class fields.
	 * Actually generate a new CsvMedia class incremented with the additional values
	 *
	 * @param name  a LinkedHashMap used to store the couples (key, value)
	 * @param value  a LinkedHashMap used to store the couples (key, value)
	 * @return Media  return an object CsvMedia that contains all the couples
	 */
	@Override
	Media with(String name, def value) {
		this.fields.put(name, value)
		new CsvMedia(
			fields:         this.fields,
			separator:      this.separator,
			transformation: this.transformation
		)
	}

	@Override
	Media with(Map data) {
		throw new UnsupportedOperationException (
			'this operation is not compatible with the CsvMedia type'
		)
	}

	@Override
	Media with(List data) {
		throw new UnsupportedOperationException (
			'this operation is not compatible with the CsvMedia type'
		)
	}

	@Override
	Media with(Number data) {
		throw new UnsupportedOperationException (
			'this operation is not compatible with the CsvMedia type'
		)
	}

	@Override
	Media with(String data) {
		throw new UnsupportedOperationException (
			'this operation is not compatible with the CsvMedia type'
		)
	}

	@Override
	Media with(Boolean data) {
		throw new UnsupportedOperationException (
			'this operation is not compatible with the CsvMedia type'
		)
	}

	/**
	 * This method merge two different CsvMedia into one.
	 * Actually generate a new CsvMedia class merged with the other one
	 *
	 * @param another another CsvMedia
	 * @return Media return an object CsvMedia that contains both the fields
	 * @throws UnsupportedOperationException
	 */
	@Override
	Media with(Media another) {
		if ( ! ( another.structure() in Map ) )
			throw new UnsupportedOperationException (
				'The second Media is not compatible with the CsvMedia type'
			)

		new CsvMedia (
			fields:         this.fields << another.structure(),
			separator:      this.separator,
			transformation: this.transformation
		)
	}

	@Override
	def structure() {
		this.fields
	}

	/**
	 * Returns a CSV representation of all the keys
	 *
	 * @return String  a csv row composed by the keys in the fields
	 */
	String header() {
		csvify(this.fields.keySet())
	}

	/**
	 * Returns a CSV representation of all the values
	 *
	 * @return String  a csv row composed by the values in the fields
	 * Rules:
	 * - MS-DOS-style lines that end with (CR/LF) characters (optional for the last line).
	 * - An optional header record (there is no sure way to detect whether it is present, so care is required when importing).
	 * - Each record "should" contain the same number of comma-separated fields.
	 * - Any field may be quoted (with double quotes).
	 * - Fields containing a line-break, double-quote or commas should be quoted.
	 * - A (double) quote character in a field must be represented by two (double) quote characters.
	 */
	String row() {
		csvify(this.fields.values())
	}

	private String csvify(Collection c) {
		c.collect { f ->
			String field = this.transformation.transform(f)
			field = field.replaceAll('"','""')
			(field =~ /[\s"]|\Q${separator}\E/) ?  /"${field}"/ : field
		}.join(this.separator)
	}
}
