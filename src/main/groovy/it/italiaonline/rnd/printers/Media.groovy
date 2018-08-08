package it.italiaonline.rnd.printers

/**
 * This interface describe objects representing a printable media
 * (e.g, json, xml, csv)
 */
interface Media {

  /**
   * This method load a couple (key, value) into the class accumulator
   * <p>
   * @param name  a String label (key)
   * @param value an object with the content of the field (value)
   * @return Media  return an object Media that contains all the couples
   * @throws UnsupportedOperationException
   */
  Media with(String name, def value)

  /**
   * This method load another Media into the class accumulator
   * <p>
   * @param another a compatible media
   * @return Media  return an object Media that contains also the second Media
   * @throws UnsupportedOperationException
   */
  Media with(Media another)

  /**
   * This method return a class rapresentation of the accumulator
   * <p>
   * @return Object return an object rapresentation of the Media content
   */
  def structure()

}
