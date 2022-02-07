package it.grational.composers

import it.grational.printers.*

class Csv implements Composer {
	private final File file
	private final List<CsvMedia> media
	private final Closure action

	Csv (
		File file,
		List<CsvMedia> media,
		Closure action
	) {
		this.file = file
		this.media = media
		this.action = action
	}

	Csv(Map params) {
		this (
			params.file,
			params.media,
			params.action
		)
	}

	@Override
	Integer write() {
		if ( !media ) this.action?.call()

		Integer i
		this.file.withWriter('utf-8') { writer ->
			for (i = 0; i < media.size(); i++) {
				if ( i == 0 ) writer.writeLine this.media[i].header()
				writer.writeLine this.media[i].row()
			}
		}
		return i
	}

}
