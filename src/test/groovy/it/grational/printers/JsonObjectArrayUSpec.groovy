package it.grational.printers

import spock.lang.*

import it.grational.transform.*

/**
 * This class test the correct output of the JsonMedia class
 */
class JsonObjectArrayUSpec extends Specification {

	@Shared Jsonable first
	@Shared Jsonable second

	def setupSpec() {
		
		first = Stub(Jsonable) {
			print() >> {
				new JsonMedia([:])
					.with('first', 'v1')
					.with('second', 'v2')
					.with('third', 'v3')
			}
		}

		second = Stub(Jsonable) {
			print() >> {
				new JsonMedia([])
					.with(true)
					.with(0)
					.with('one')
					.with([1, 2, 3])
					.with([key: 'value'])
			}
		}
	}

	def "Should allow to add any permitted element to the a general json array and print them as expected"() {
		given:
			Jsonable jarray = new JsonObjectArray()
			jarray << first
			jarray << second

		expect:
			jarray.print().toString() == /[{"first":"v1","second":"v2","third":"v3"},[true,0,"one",[1,2,3],{"key":"value"}]]/
	}

	def "Should omit the falsy elements when optional is set to true"() {
		given:
			Jsonable emptyArray = Stub(Jsonable) {
				print() >> { new JsonMedia([]) }
			}
			Jsonable emptyObject = Stub(Jsonable) {
				print() >> { new JsonMedia([:]) }
			}

		and:
			Jsonable jarray = new JsonObjectArray()
			jarray << first
			jarray << emptyArray
			jarray << second
			jarray << emptyObject

		expect:
			jarray.print().toString() == /[{"first":"v1","second":"v2","third":"v3"},[true,0,"one",[1,2,3],{"key":"value"}]]/
	}

}
