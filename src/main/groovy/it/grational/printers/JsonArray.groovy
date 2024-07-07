package it.grational.printers

/**
 * This abstract class describes objects cabable of impressing themselves on a JsonMedia as arrays
 */
abstract class JsonArray implements Jsonable {

	/**
	 * Used to return the in-memory List/Array representation of the printed JsonMedia
	 * <p>
	 * @return Object return an in-memory rapresentation of the JsonMedia printed by the class
	/ */
	@Override
	def toJson() {
		this.print(new JsonMedia([])).structure()
  }

	/**
	 * Jsonable print implementation
	 * <p>
	 * @return A printed media
	 */
	@Override
	Media print() {
		this.print(new JsonMedia([]))
	}

}
