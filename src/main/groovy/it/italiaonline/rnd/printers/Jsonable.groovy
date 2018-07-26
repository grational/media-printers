package it.italiaonline.rnd.printers

/**
 * This interface describe objects cabable of representing themselves as a Json element
 */
interface Jsonable {

  /**
   * This method is used to return the json representation of the object
   * <p>
   * @return String  return a String containing the json representation of the object
   */
  def toJson()

}
