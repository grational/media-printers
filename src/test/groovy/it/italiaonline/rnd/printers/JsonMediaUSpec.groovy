package it.italiaonline.rnd.printers

import spock.lang.*

import it.italiaonline.rnd.transform.*

/**
 * This class test the correct output of the JsonMedia class
 */
class JsonMediaUSpec extends Specification {

	def "Should return the correct values using a map passed through constructor"() {
		given: 'build a JsonMedia with a map passed as parameter'
			Media jsonPrinter = new JsonMedia (
				first:  'v1',
				second: 'v2',
				third:  'v3'
			)
		when:
			String json = jsonPrinter.json()
		then:
			json == '{"first":"v1","second":"v2","third":"v3"}'
	}

	def "Should return the correct values using with"() {
		given: 'build an empty JsonMedia'
			Media jsonPrinter = new JsonMedia()
		when:
			String json = jsonPrinter
			                .with('first',  'v1')
			                .with('second', 'v2')
			                .with('third',  'v3')
			                .json()
		then:
			json == '{"first":"v1","second":"v2","third":"v3"}'
	}

	def "Should add any permitted element to a list"() {
		given:
			Media jsonPrinter = new JsonMedia([])
		when:
			String json = jsonPrinter
			                .with(true)           // Boolean
			                .with(0)              // Number
			                .with('one')          // String
			                .with([1, 2, 3])      // List
			                .with([key: 'value']) // Map
			                .json()
		then:
			json == '''[true,0,"one",[1,2,3],{"key":"value"}]'''
	}

	def "Should raise an UnsupportedOperationException when trying to add anything but entries into a Map structure"() {
		given:
			def jsonPrinter = new JsonMedia([:])
		when:
			jsonPrinter.with(data)
		then:
			def exception = thrown(UnsupportedOperationException)
			exception.message == "Cannot load '${dataClass}' into a json object"
		where:
			data    || dataClass
			[a: 1]  || 'LinkedHashMap'
			[1,2,3] || 'ArrayList'
			"hello" || 'String'
			5       || 'Integer'
			true    || 'Boolean'
	}

	def "Should raise an UnsupportedOperationException when trying to add an Entry into a List structure"() {
		given:
			def jsonPrinter = new JsonMedia([])
		when:
			jsonPrinter
				.with('label','value')
		then:
			def exception = thrown(UnsupportedOperationException)
			exception.message == "Cannot load an Entry into a json array"
	}
}
