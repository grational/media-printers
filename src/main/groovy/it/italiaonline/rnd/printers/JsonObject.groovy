package it.italiaonline.rnd.printers

/**
 * This abstract class describes objects cabable of impressing themselves on a JsonMedia as objects
 */
abstract class JsonObject extends Jsonable {
	/**
	 * Used to return the in-memory Map representation of the printed JsonMedia
	 * <p>
	 * @return Object return an in-memory rapresentation of the JsonMedia printed by the class
	 */
	@Override
	def toJson() {
		this.print(new JsonMedia([:])).structure()
	}

}
