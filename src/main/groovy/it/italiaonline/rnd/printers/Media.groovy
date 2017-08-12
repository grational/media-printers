package it.italiaonline.rnd.printers

/**
 * This interface describe objects representing a printable media
 * (e.g, json, xml, csv)
 */
interface Media {

  /**
   * This method load a couple (key, value) into the class accumulator
   * <p>
   * @param name  a LinkedHashMap used to store the couples (key, value)
   * @param value  a LinkedHashMap used to store the couples (key, value)
   * @return Media  return an object Media that contains all the couples
   */
  Media with(String name, String value)

}
