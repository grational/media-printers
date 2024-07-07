package it.grational.printers

import groovy.transform.ToString

/**
 * This abstract class describes objects cabable of impressing themselves on a JsonMedia as arrays
 */
@ToString(includeNames = true, includeFields = true)
class JsonObjectArray extends JsonArray {

	@Delegate
	protected List<Jsonable> array = []

	/**
	 * Default print implementation
	 * <p>
	 * @return A printed media
	 */
	@Override
	Media print(Media media) {
		array.inject(media) { JsonMedia accumulator, Jsonable element ->
			accumulator.with(element.print())
		}
	}

}
