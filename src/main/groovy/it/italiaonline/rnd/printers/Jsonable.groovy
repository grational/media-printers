package it.italiaonline.rnd.printers

/**
 * This abstract class describes objects cabable of impressing themselves on a JsonMedia
 */
abstract class Jsonable {

  /**
   * This method is used to print the object on a JsonMedia
   * <p>
   * @param jsonMedia a JsonMedia object
   * @return JsonMedia return a JsonMedia object that contains all the relevant fields
   */
  abstract JsonMedia print(JsonMedia media)

  /**
   * Used to return the in-memory representation of the printed JsonMedia
   * <p>
   * @return Object return an in-memory rapresentation of the JsonMedia printed by the class
   */
  def toJson() {
		this.print(new JsonMedia()).structure()
  }

}
