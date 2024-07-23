package it.grational.printers

import spock.lang.*

import it.grational.transform.*

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
			exception.message == 'Cannot load an Entry into a json array'
	}

	def "Should correctly merge two maps"() {
		given: 'build an empty JsonMedia'
			Media jsonPrinter = new JsonMedia()
		and: 'two different map Media'
			Media firstMedia  = jsonPrinter
				.with('first',  'v1')
				.with('second', 'v2')
				.with('third',  'v3')

			Media secondMedia = jsonPrinter
				.with('fourth', 'v4')
				.with('fifth',  'v5')
				.with('sixth',  'v6')
		when:
			Media mergedMedia = firstMedia.with(secondMedia)
		then:
			mergedMedia.json() == '{"first":"v1","second":"v2","third":"v3","fourth":"v4","fifth":"v5","sixth":"v6"}'
	}

	def "Should correctly merge two lists"() {
		given: 'build an empty JsonMedia'
			Media arrayJsonPrinter = new JsonMedia([])
		and: 'two different list Media'
			Media firstMedia  = arrayJsonPrinter
				.with(true)           // Boolean
				.with(0)              // Number
				.with('one')          // String
				.with([1, 2, 3])      // List
				.with([key: 'value']) // Map

			Media secondMedia = arrayJsonPrinter
				.with(false)          // Boolean
				.with(1)              // Number
				.with('eno')          // String
				.with([3, 2, 1])      // List
				.with([yek: 'eulav']) // Map
		when:
			Media mergedMedia = firstMedia.with(secondMedia, true)
		then:
			mergedMedia.json() == '''[true,0,"one",[1,2,3],{"key":"value"},false,1,"eno",[3,2,1],{"yek":"eulav"}]'''
	}

	def "Should correctly merge a list with a map"() {
		given: 'build an empty JsonMedia'
			Media arrayJsonPrinter  = new JsonMedia([])
			Media objectJsonPrinter = new JsonMedia([:])
		and: 'two differently loaded Media'
			Media firstMedia  = arrayJsonPrinter
				.with(true)           // Boolean
				.with(0)              // Number
				.with('one')          // String
				.with([1, 2, 3])      // List
				.with([key: 'value']) // Map

			Media secondMedia = objectJsonPrinter
				.with('first',  'v1')
				.with('second', 'v2')
				.with('third',  'v3')

		when:
			Media mergedMedia = firstMedia.with(secondMedia)
		then:
			mergedMedia.json() == '''[true,0,"one",[1,2,3],{"key":"value"},{"first":"v1","second":"v2","third":"v3"}]'''
	}

	def "Should correctly add a list to a map if it is passed as an entry"() {
		given: 'build a JsonObject and a JsonArray'
			Media jsonObject = new JsonMedia([:])
			Media jsonArray = new JsonMedia([])
		and: 'differently loaded'
			jsonObject = jsonObject
			.with('first', 'v1')
			.with('second', 'v2')
			.with('third', 'v3')

			jsonArray  = jsonArray
				.with(true)            // Boolean
				.with(0)               // Number
				.with('one')          // String
				.with([1, 2, 3])      // List
				.with([key: 'value']) // Map

		when:
			Media mergedObject = jsonObject.with('array',jsonArray)
		then:
			mergedObject.json() == '''{"first":"v1","second":"v2","third":"v3","array":[true,0,"one",[1,2,3],{"key":"value"}]}'''
	}

	def "Should raise an exception when you try to merge a map with a list"() {
		given: 'build empty JsonMedia'
			Media objectJsonPrinter = new JsonMedia([:])
			Media arrayJsonPrinter  = new JsonMedia([])
		and: 'two differently loaded Media'
			Media firstMedia  = objectJsonPrinter
				.with('first',  'v1')
				.with('second', 'v2')
				.with('third',  'v3')

			Media secondMedia = arrayJsonPrinter
				.with(true)           // Boolean
				.with(0)              // Number
				.with('one')          // String
				.with([1, 2, 3])      // List
				.with([key: 'value']) // Map

		when:
			Media mergedMedia = firstMedia.with(secondMedia)
		then:
			def exception = thrown(UnsupportedOperationException)
			exception.message == 'The second Media is not compatible with this JsonMedia type'
	}

	def "Should correctly handle the withOptional() method for a json object"() {
		given: 'build empty JsonMedia'
			Media objectJsonPrinter = new JsonMedia([:])
		when: 'fill it'
			Media objectMedia = objectJsonPrinter
				.withOptional('first', 'v1')
				.withOptional('second', null)
				.withOptional('third', '')

		then:
			objectMedia.json() == '{"first":"v1"}'
	}

	def "Should correctly handle the withOptional() method for a json object"() {
		given: 'build empty JsonMedia'
			Media arrayJsonPrinter  = new JsonMedia([])
		when: 'fill it with all the possible data types'
			Media arrayMedia = arrayJsonPrinter
		 	 .withOptional(true)   // Boolean included
		 	 .withOptional(1)      // Number  included
		 	 .withOptional('str')  // String  included
		 	 .withOptional([1])    // List    included
		 	 .withOptional([a: 1]) // Map     included
		 	 .withOptional(null)   // null    NOT included
		 	 .withOptional(false)  // Boolean NOT included
		 	 .withOptional(0)      // Number  NOT included
		 	 .withOptional('')     // String  NOT included
		 	 .withOptional([])     // List    NOT included
		 	 .withOptional([:])    // Map     NOT included
		then:
			arrayMedia.json() == '[true,1,"str",[1],{"a":1}]'
	}

}
