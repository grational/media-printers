package it.italiaonline.rnd.transform

class FlattenedStructure implements Transformation {

	// closure initialization block
	// TODO: split into classes as soon as possible
	{
		String l1s = '||' // List level 1 separator
		String l2s = '__' // List level 2 separator
		String kvs = '::' // Map  key/value separator
		String hs  = '--' // Hours separator

		String.metaClass.stringify {
			delegate
		}

		List.metaClass.stringify {
			def list
			switch(delegate.first()) {
				case Map:
					list = delegate.collect { Map m ->
					         m.stringify(l2s)
					       }
					break
				default:
					list = delegate
			}
			list.join(l1s)
		}

		Map.metaClass.stringify { sep=l1s ->
			def map
			switch(delegate.values().first()) {
				case List:
					map = delegate.collectEntries { k, v ->
						[(k): v.join(l2s)]
					}
					break
				default:
					map = delegate
			}
			map.collect { k, v ->
				return "${k}${kvs}${v}"
			}.join(sep)
		}

		Object.metaClass.nestingLevels {
			def nestingLevels = 1
			switch(delegate) {
				case List:
					nestingLevels += delegate.first().nestingLevels()
					break
				case Map:
					nestingLevels += delegate.values().first().nestingLevels()
					break
				default:
					nestingLevels = 0
			}
			return nestingLevels
		}
	}

	@Override
	def transform(def input) {
		def nestedLevels = input.nestingLevels()
		if ( nestedLevels > 2 )
			throw new IllegalArgumentException("The data structure passed is too complex (nested levels: ${nestedLevels})")
		return input.stringify()
	}
}
