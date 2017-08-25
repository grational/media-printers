package it.italiaonline.rnd.printers

import spock.lang.*

import it.italiaonline.rnd.transform.*

/**
 * This class test the correct output of the CsvMedia class
 */
class CsvMediaUSpec extends Specification {

	/**
	 * Load 3 couples key,value then compare with the expected result for header
	 * and row
	 */
	def "Should return the correct values using with"() {
		setup: 'build an empty CsvMedia'
			Media csvPrinter = new CsvMedia()
		when:
			csvPrinter
				.with('first',  'v1')
				.with('second', 'v2')
				.with('third',  'v3')
			String csvHeader = csvPrinter.header()
			String csvRow    = csvPrinter.row()
		then:
			csvHeader == 'first,second,third'
			csvRow    == 'v1,v2,v3'
	}

	def "Should return the correct values using with and a custom separator"() {
		setup: 'build an empty CsvMedia'
			Media csvPrinter = new CsvMedia(separator: separator)
		when:
			csvPrinter
				.with('first',  'v1')
				.with('second', 'v2')
				.with('third',  'v3')
			String csvHeader = csvPrinter.header()
			String csvRow    = csvPrinter.row()
		then:
			csvHeader == header
			csvRow    == row
		where:
			separator || header                         | row
			'\t'      || 'first	second	third'          | 'v1	v2	v3'
			';'       || 'first;second;third'           | 'v1;v2;v3'
			'_test_'  || 'first_test_second_test_third' | 'v1_test_v2_test_v3'
	}

	def "Should return the correct values using a map passed through constructor"() {
		setup: 'build a CsvMedia with a map passed as parameter'
			Media csvPrinter = new CsvMedia(
				fields: [
					first:  'v1',
					second: 'v2',
					third:  'v3'
				]
			)
		when:
			String csvHeader = csvPrinter.header()
			String csvRow    = csvPrinter.row()
		then:
			csvHeader == 'first,second,third'
			csvRow    == 'v1,v2,v3'
	}

	def "Should quote the needed fields passing maps"() {
		setup:
			Media csvPrinter = new CsvMedia()
		when:
			csvPrinter
				.with(h1,v1)
				.with(h2,v2)
				.with(h3,v3)
		then:
			header == csvPrinter.header()
			row    == csvPrinter.row()
		where:
			h1    | v1   | h2   | v2     | h3   | v3    || header        | row
			'h1'  | 'v1' | 'h2' | 'v2'   | 'h3' | 'v3'  || 'h1,h2,h3'    | 'v1,v2,v3'
			'h 1' | 'v1' | 'h2' | 'v2'   | 'h3' | 'v 3' || '"h 1",h2,h3' | 'v1,v2,"v 3"'
			'h,1' | 'v1' | 'h2' | 'v"2"' | 'h3' | 'v 3' || '"h,1",h2,h3' | 'v1,"v""2""","v 3"'
	}

	def "Should work also changing separator"() {
		setup: 'build a CsvMedia with a map passed as parameter'
			Media csvPrinter = new CsvMedia(
				fields: [
					first:  'v1',
					second: 'v2',
					third:  'v3'
				],
				separator: "\t"
			)
		when:
			String csvHeader = csvPrinter.header()
			String csvRow    = csvPrinter.row()
		then:
			csvHeader == 'first	second	third'
			csvRow    == 'v1	v2	v3'
	}

	def "Should apply correctly the transformation passed as parameter"() {
		setup: 'build a CsvMedia with a map passed as parameter'
			Media csvPrinter = new CsvMedia(
				fields: [
					PHONES: [mobile: '+39 348 9018484', fisso: '+39 011 8193736', fax: '+39 011 8193736'],
					IMAGES: ['https://img.pgol.it/img/R3/76/70/07/0/13296736.jpg'],
				],
				transformation: new FlattenedStructure()
			)
		when:
			String csvHeader = csvPrinter.header()
			String csvRow    = csvPrinter.row()
		then:
			csvHeader == 'PHONES,IMAGES'
			csvRow    == '''"mobile::+39 348 9018484||fisso::+39 011 8193736||fax::+39 011 8193736",https://img.pgol.it/img/R3/76/70/07/0/13296736.jpg'''
	}

	def "Should apply correctly the transformation using with"() {
		setup: 'build a CsvMedia with a map passed as parameter'
			Media csvPrinter = new CsvMedia(
				separator:      '||',
				transformation: new FlattenedStructure()
			)
		when:
			csvPrinter
				.with('PHONES', [mobile: '+39 348 9018484', fisso: '+39 011 8193736', fax: '+39 011 8193736'])
				.with('IMAGES', ['https://img.pgol.it/img/R3/76/70/07/0/13296736.jpg'])
			String csvHeader = csvPrinter.header()
			String csvRow    = csvPrinter.row()
		then:
			csvHeader == 'PHONES||IMAGES'
			csvRow    == '''"mobile::+39 348 9018484||fisso::+39 011 8193736||fax::+39 011 8193736"||https://img.pgol.it/img/R3/76/70/07/0/13296736.jpg'''
	}

	def "Should satisfy this real test case"() {
		setup: 'build a CsvMedia with a map passed as parameter'
			Media csvPrinter = new CsvMedia(
				fields: [
					ID: 'ristorantececcarelli',
					NAME: 'Ristorante Ceccarelli',
					SIGN: 'Ristorante Ceccarelli',
					DESCRIPTION: "Situato a Torino nel quartiere Borgo Po zona Gran Madre, il Ristorante Ceccarelli propone una cucina nazionale di qualità caratterizzata da piatti tipici della tradizione culinaria piemontese e toscana. Oltre ad un'ampia scelta di antipasti, il ristorante propone tante specialità a base di carne e di pesce, ottima carne chianina, specialità stagionali come i funghi, serviti in diversi modi. Particolarmente degni di nota il fritto misto, piatto tipico piemontese, i calamaretti, la zuppa con farro e scampi, il fritto di cervella e fiori di zucchina e i golosi dolci della casa. Ampia la scelta dei vini, con etichette nazionali e locali. Il Ristorante Ceccarelli è in via S. Santarosa 7/b. Per prenotazioni contattare lo 011 8193736 o il 348 9018484.",
					CATEGORY: 'Restaurant',
					ADDRESS: 'Via Santorre Di Santarosa, 7',
					LATITUDE: '45.06302',
					LONGITUDE: '7.70061',
					LOGO: 'https://img.pgol.it/img/R3/76/70/07/0/R37670070_LG.gif',
					ZIP: '10131',
					CITY: 'Torino',
					PROVINCE: 'TO',
					REGION: 'Piemonte',
					COUNTRY: 'it',
					PHONES: "mobile::+39 348 9018484||fisso::+39 011 8193736||fax::+39 011 8193736",
					IMAGES: 'https://img.pgol.it/img/R3/76/70/07/0/13296736.jpg',
					EMAILS: 'ristorantececcarelli@gmail.com',
					SITE: 'http://www.ristorantececcarelli.it',
					OPENINGS: '1:12:30:14:30,1:19:30:22:30,2:closed,3:19:30:22:30,4:12:30:14:30,4:19:30:22:30,5:12:30:14:30,5:19:30:22:30,6:12:30:14:30,6:19:30:22:30,7:12:30:14:30,7:19:30:22:30',
					SERVICES: 'ampia scelta di vini||pranzi aziendali||servizio di catering',
					SPECIALTIES: 'antipasti||carne chianina||cucina nazionale||cucina piemontese||cucina toscana||dolci della casa||fritto misto||pesce||specialità carne||specialità funghi||specialità stagionali',
					PAYMENT_OPTIONS: 'bancomat||carte di credito||solo contanti'
				],
				transformation: new Identity()
			)
		when:
			String csvHeader = csvPrinter.header()
			String csvRow    = csvPrinter.row()
		then:
			csvHeader == 'ID,NAME,SIGN,DESCRIPTION,CATEGORY,ADDRESS,LATITUDE,LONGITUDE,LOGO,ZIP,CITY,PROVINCE,REGION,COUNTRY,PHONES,IMAGES,EMAILS,SITE,OPENINGS,SERVICES,SPECIALTIES,PAYMENT_OPTIONS'
			csvRow    == '''ristorantececcarelli,"Ristorante Ceccarelli","Ristorante Ceccarelli","Situato a Torino nel quartiere Borgo Po zona Gran Madre, il Ristorante Ceccarelli propone una cucina nazionale di qualità caratterizzata da piatti tipici della tradizione culinaria piemontese e toscana. Oltre ad un'ampia scelta di antipasti, il ristorante propone tante specialità a base di carne e di pesce, ottima carne chianina, specialità stagionali come i funghi, serviti in diversi modi. Particolarmente degni di nota il fritto misto, piatto tipico piemontese, i calamaretti, la zuppa con farro e scampi, il fritto di cervella e fiori di zucchina e i golosi dolci della casa. Ampia la scelta dei vini, con etichette nazionali e locali. Il Ristorante Ceccarelli è in via S. Santarosa 7/b. Per prenotazioni contattare lo 011 8193736 o il 348 9018484.",Restaurant,"Via Santorre Di Santarosa, 7",45.06302,7.70061,https://img.pgol.it/img/R3/76/70/07/0/R37670070_LG.gif,10131,Torino,TO,Piemonte,it,"mobile::+39 348 9018484||fisso::+39 011 8193736||fax::+39 011 8193736",https://img.pgol.it/img/R3/76/70/07/0/13296736.jpg,ristorantececcarelli@gmail.com,http://www.ristorantececcarelli.it,"1:12:30:14:30,1:19:30:22:30,2:closed,3:19:30:22:30,4:12:30:14:30,4:19:30:22:30,5:12:30:14:30,5:19:30:22:30,6:12:30:14:30,6:19:30:22:30,7:12:30:14:30,7:19:30:22:30","ampia scelta di vini||pranzi aziendali||servizio di catering","antipasti||carne chianina||cucina nazionale||cucina piemontese||cucina toscana||dolci della casa||fritto misto||pesce||specialità carne||specialità funghi||specialità stagionali","bancomat||carte di credito||solo contanti"'''
	}
}
