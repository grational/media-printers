package it.italiaonline.rnd.transform

import spock.lang.*

class FlattenedStructureUSpec extends Specification {

	def "Should make no trasformation if a String is passed"() {
		given: 'A String data structure'
			String ds = 'dataStructure'
		when:
			def flattenedStructure = new FlattenedStructure().transform(ds)
		then:
			flattenedStructure == ds
	}

	def "Should return a double-vertical-bar-separated list if a plain List is passed"() {
		given: 'A List data structure'
			List ds = [1, 2, 3]
		when:
			def flattenedStructure = new FlattenedStructure().transform(ds)
		then:
			flattenedStructure == '1||2||3'
	}

	def "Should return a double-vertical-bar-separated map if a plain Map is passed"() {
		given: 'A Map data structure'
			Map ds = [1: 'a', 2: 'b', 3: 'c']
		when:
			def flattenedStructure = new FlattenedStructure().transform(ds)
		then:
			flattenedStructure == '1::a||2::b||3::c'
	}

	def "Should return the correct stringified version of a list of maps"() {
		given: 'A List of maps data structure'
			List ds = [ [1: 'a', 2: 'b'], [ 3: 'c', 4: 'd'] ]
		when:
			def flattenedStructure = new FlattenedStructure().transform(ds)
		then:
			flattenedStructure == '1::a__2::b||3::c__4::d'
	}

	def "Should return the correct stringified version of a map of lists"() {
		given: 'A Map of lists data structure'
			Map ds = [ a: [1, 2, 3], b: [4, 5, 6] ]
		when:
			def flattenedStructure = new FlattenedStructure().transform(ds)
		then:
			flattenedStructure == 'a::1__2__3||b::4__5__6'
	}

	def "Should raise an IllegalArgumentException if a too complex structure is passed"() {
		given: 'A data structure with 3 nesting levels'
			Map ds = [ a: [ b: [4, 5, 6] ] ]
		when:
			new FlattenedStructure().transform(ds)
		then:
			def exception = thrown(IllegalArgumentException)
			exception.message == "The data structure passed is too complex (nested levels: 3)"
	}
	
	def "Should return an empty String when an empty List or Map is passed"() {
		when:
			def output = new FlattenedStructure().transform(ds)
		then:
			output == expected
		where:
			ds  || expected
			[]  || ''
			[:] || ''
	}
}
