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
   * This method load a Map into the class accumulator
   * <p>
   * @param data the Map to be loaded into the class accumulator
   * @return Media  return an object Media that contains also the map
   * @throws UnsupportedOperationException
   */
	Media with(Map data)

  /**
   * This method load a List into the class accumulator
   * <p>
   * @param data the List to be loaded into the class accumulator
   * @return Media  return an object Media that contains also the list
   * @throws UnsupportedOperationException
   */
	Media with(List data)

  /**
   * This method load a Number into the class accumulator
   * <p>
   * @param data the Map to be loaded into the class accumulator
   * @return Media  return an object Media that contains also the number
   * @throws UnsupportedOperationException
   */
	Media with(Number data)

  /**
   * This method load a String into the class accumulator
   * <p>
   * @param data the Map to be loaded into the class accumulator
   * @return Media  return an object Media that contains also the string
   * @throws UnsupportedOperationException
   */
	Media with(String data)

  /**
   * This method load a Boolean into the class accumulator
   * <p>
   * @param data the Map to be loaded into the class accumulator
   * @return Media  return an object Media that contains also the boolean
   * @throws UnsupportedOperationException
   */
	Media with(Boolean data)

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
