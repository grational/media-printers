package it.italiaonline.rnd.printers

import groovy.json.JsonBuilder

/**
 * This class is used to build a CSV representation of an object
 */
class JsonMedia implements Media {

	private final def structure

	/**
	 * Groovy-like constructor, just a map with parameters is passed.
	 * <p>
	 * @param data a LinkedHashMap used to store the couples (key, value)
	 */
	JsonMedia() {
		this([:])
	}
	JsonMedia(Map map) {
		this.structure = map ?: [:]
	}

	JsonMedia(List list) {
		this.structure = list ?: []
	}

	/**
	 * This method load a couple (key, value) into the structure.
	 * Actually generate a new JsonMedia class incremented with the additional values
	 *
	 * @param name  a String used to label the value
	 * @param value  an Object among these types: Map, List, Boolean, String, Number
	 * @return Media  return an object JsonMedia that contains all the couples
	 */
	@Override
	Media with(String name, def value) {
		def media
		if (this.structure in Map)
			media = new JsonMedia(this.structure + [(name): value])
		else
			throw new UnsupportedOperationException (
				'Cannot load an Entry into a json array'
			)
		return media
	}

	Media with(Map data) {
		listWith(data)
	}

	Media with(List data) {
		listWith(data)
	}

	Media with(Number data) {
		listWith(data)
	}

	Media with(String data) {
		listWith(data)
	}

	Media with(Boolean data) {
		listWith(data)
	}

	private Media listWith(def data) {
		def media
		if (this.structure in List)
			media = new JsonMedia(this.structure << data)
		else
			throw new UnsupportedOperationException (
				"Cannot load '${data.getClass().getSimpleName()}' into a json object"
			)
		return media
	}

	String json() {
		new JsonBuilder(this.structure).toString()
	}
}
