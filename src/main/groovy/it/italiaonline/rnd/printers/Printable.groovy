package it.italiaonline.rnd.printers

/**
 * This interface describe objects cabable of printing themselves on a Media
 * (e.g, json, xml, csv)
 */
interface Printable {

  /**
   * This method is used to print the object on a Media
   * <p>
   * @param media a Media object on which to print on this
   * @return Media  return an object Media that contains all the relevant fields of the object
   */
  Media print(Media media)

}
