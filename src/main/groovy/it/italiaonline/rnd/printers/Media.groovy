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
   */
  Media with(String name, def value)

}
