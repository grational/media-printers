package it.grational.composers

import spock.lang.*
import it.grational.printers.CsvMedia
import it.grational.specification.*

class CsvISpec extends Specification {

	@Shared
	TempFileFactory tff = new TempFileFactory()

	def "Should correctly compose a collection of Csv Medias into a file"() {
		given: 'an output empty temporary csv file'
			def outfile = tff.create()
		and: 'a list of csv media'
			def mediaList = (1..3).collect {
				new CsvMedia (
					fields: [
						H1: "R${it}_1",
						H2: "R${it}_2",
						H3: "R${it}_3"
					],
					uppersnake: true,
					separator: ';'
				)
			}

		when:
			def mediaWritten = new Csv (
				file: outfile,
				media: mediaList
			).write()

		then: 'the number of media writter are equal to the number of lines minus one (the header)'
			def fileLines = outfile.readLines()
			mediaWritten  == (fileLines.size() - 1)
		and: 'each line is equal to the expected content'
			fileLines[0] == 'H1;H2;H3'
			fileLines[1] == 'R1_1;R1_2;R1_3'
			fileLines[2] == 'R2_1;R2_2;R2_3'
			fileLines[3] == 'R3_1;R3_2;R3_3'
	}

	def "Should be capable of handling an empty media list"() {
		given:
			def outfile = tff.create()
		and:
			def emptyMediaList = []

		when:
			def mediaWritten = new Csv (
				file: outfile,
				media: emptyMediaList
			).write()

		then: 'the number of media written is zero'
			mediaWritten == 0
		and: 'also the content of the outfile is empty'
			outfile.readLines().size() == 0
	}

	def "Should execute the action passed when there are no media to write"() {
		given:
			def outfile = tff.create()
		and:
			def emptyMediaList = []
		and:
			def exceptionMessage = "No media to write"
			def action = { throw new IllegalStateException(exceptionMessage) }

		when:
			def mediaWritten = new Csv (
				file: outfile,
				media: emptyMediaList,
				action: action
			).write()

		then: 
			def exception = thrown(IllegalStateException)
			exception.message == exceptionMessage
	}
}
