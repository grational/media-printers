package it.grational.printers

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
	 * @param value  an Object among these types: Map, List, Boolean, String, Number, Media
	 * @return Media  return an object JsonMedia that contains all the couples
	 */
	@Override
	Media with(String name, def value) {
		def media
		if (this.content in Map)
				media = new JsonMedia (
					this.content + [
						(name): ((value in Media) ? value.structure() : value)
					]
				)
		else
			throw new UnsupportedOperationException (
				'Cannot load an Entry into a json array'
			)
		return media
	}

	/**
	 * This method optionally load one of the supported data types into the map maccumulator
	 * The decision is taken according to the Groovy Truth
	 * <p>
	 * @param name  a String used to label the value
	 * @param value  an Object among these types: Map, List, Boolean, String, Number
	 * @return Media  return an object JsonMedia that contains all the couples
	 * @throws UnsupportedOperationException
	 */
	Media withOptional(String name, def value) {
		return ( value ) ?  this.with(name,value) : new JsonMedia(this.content)
	}

	@Override
	Media with(Media another) {
		if ( (this.content in Map) && !(another.structure() in Map) )
			throw new UnsupportedOperationException(
				'The second Media is not compatible with this JsonMedia type'
			)
		new JsonMedia(this.content + another.structure())
	}

	String json() {
		def jsonOutput = new JsonGenerator.Options()
		                     .addConverter(Jsonable) { it.toJson() }
		                     .build()
		jsonOutput.toJson(this.content)
	}

	/**
	 * This method returns the in-memory structure of the JsonMedia
	 * <p>
	 * @return Object return an object that contains the in-memory data structure
	 */
	@Override
	def structure() { this.content }

	/**
	 * This method load a Map into the class accumulator
	 * <p>
	 * @param data the Map to be loaded into the class accumulator
	 * @return Media  return an object Media that contains the new value
	 * @throws UnsupportedOperationException
	 */
	Media with(Map data) { listWith(data) }

	/**
	 * This method load a List into the class accumulator
	 * <p>
	 * @param data the List to be loaded into the class accumulator
	 * @return Media  return an object Media that contains the new value
	 * @throws UnsupportedOperationException
	 */
	Media with(List data) { listWith(data) }

	/**
	 * This method load a Number into the class accumulator
	 * <p>
	 * @param data the Number to be loaded into the class accumulator
	 * @return Media  return an object Media that contains the new value
	 * @throws UnsupportedOperationException
	 */
	Media with(Number data) { listWith(data) }

	/**
	 * This method load a String into the class accumulator
	 * <p>
	 * @param data the String to be loaded into the class accumulator
	 * @return Media  return an object Media that contains the new value
	 * @throws UnsupportedOperationException
	 */
	Media with(String data) { listWith(data) }

	/**
	 * This method load a Boolean into the class accumulator
	 * <p>
	 * @param data the Boolean to be loaded into the class accumulator
	 * @return Media  return an object Media that contains the new value
	 * @throws UnsupportedOperationException
	 */
	Media with(Boolean data) { listWith(data) }

	/**
	 * This method load a Boolean into the class accumulator
	 * <p>
	 * @param data the Boolean to be loaded into the class accumulator
	 * @return Media  return an object Media that contains the new value
	 * @throws UnsupportedOperationException
	 */
	Media withOptional(def data) {
		return (data) ? this.listWith(data) : new JsonMedia(this.content)
	}

	private Media listWith(def data) {
		if (this.content in Map)
			throw new UnsupportedOperationException (
				"Cannot load '${data.getClass().getSimpleName()}' into a json object"
			)
		new JsonMedia(this.content.clone() << data)
	}

}
