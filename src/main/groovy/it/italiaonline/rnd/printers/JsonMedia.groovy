package it.italiaonline.rnd.printers

import groovy.json.JsonGenerator

/**
 * This class is used to build a Json representation of an object
 */
class JsonMedia implements Media {

	private final def content

	/**
	 * Groovy-like constructor, just a map with parameters is passed.
	 * <p>
	 * @param data a LinkedHashMap used to store the couples (key, value)
	 */
	JsonMedia() {
		this([:])
	}

	JsonMedia(Map map) {
		this.content = map ?: [:]
	}

	JsonMedia(List list) {
		this.content = list ?: []
	}

	/**
	 * This method load a couple (key, value) into the content.
	 * Actually generate a new JsonMedia class incremented with the additional values
	 *
	 * @param name  a String used to label the value
	 * @param value  an Object among these types: Map, List, Boolean, String, Number
	 * @return Media  return an object JsonMedia that contains all the couples
	 */
	@Override
	Media with(String name, def value) {
		def media
		if (this.content in Map)
			media = new JsonMedia(this.content + [(name): value])
		else
			throw new UnsupportedOperationException (
				'Cannot load an Entry into a json array'
			)
		return media
	}

	@Override
	Media with(Map data) {
		listWith(data)
	}

	@Override
	Media with(List data) {
		listWith(data)
	}

	@Override
	Media with(Number data) {
		listWith(data)
	}

	@Override
	Media with(String data) {
		listWith(data)
	}

	@Override
	Media with(Boolean data) {
		listWith(data)
	}

	@Override
	Media with(Media another) {
		if ( (this.content in Map) && !(another.structure() in Map) )
			throw new UnsupportedOperationException(
				'The second Media is not compatible with this JsonMedia type'
			)
		new JsonMedia(this.content + another.structure())
	}

	@Override
	def structure() {
		this.content
	}

	String json() {
		def jsonOutput = new JsonGenerator.Options()
		                     .disableUnicodeEscaping() // Do not escape UNICODE
		                     .addConverter(Jsonable) { it.toJson() }
		                     .build()
		jsonOutput.toJson(this.content)
	}

	private Media listWith(def data) {
		if (this.content in Map)
			throw new UnsupportedOperationException (
				"Cannot load '${data.getClass().getSimpleName()}' into a json object"
			)
		new JsonMedia(this.content.clone() << data)
	}

}
