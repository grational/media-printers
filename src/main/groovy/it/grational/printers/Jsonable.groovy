package it.grational.printers

/**
 * This abstract class describes objects cabable of impressing themselves on a JsonMedia
 */
protected abstract class Jsonable implements Printable {

  /**
   * This method is used to print the object on a JsonMedia
   * <p>
   * @param jsonMedia a JsonMedia object
   * @return JsonMedia return a JsonMedia object that contains all the relevant fields
   */
  abstract Media print(Media media)

  /**
   * Used to return the in-memory Map representation of the printed JsonMedia
   * <p>
   * @return Object return an in-memory rapresentation of the JsonMedia printed by the class
   */
  abstract def toJson()

}
