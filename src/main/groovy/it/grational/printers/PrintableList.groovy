package it.grational.printers

/**
 * This interface describe objects cabable of printing themselves on a Media
 * (e.g, json, xml, csv)
 */
interface PrintableList {

  /**
   * This method is used to print the object on a Media List
   * <p>
   * @param media a Media object on which to print on this
   * @return List<Media>  each Media of the List contains all the relevant fields of the corresponding object
   */
  List<Media> print(Media media)

}
