package it.grational.printers

/**
 * This abstract class describes objects cabable of impressing themselves on a JsonMedia
 */
protected interface Jsonable extends Printable {

  /**
   * Force an object to provide a default appropriate JsonMedia form: array or object
   * <p>
   * @return Object return an in-memory rapresentation of the JsonMedia printed by the class
   */
  Media print()

  /**
   * Used to return the in-memory Map representation of the printed JsonMedia
   * <p>
   * @return Object return an in-memory rapresentation of the JsonMedia printed by the class
   */
  def toJson()

}
