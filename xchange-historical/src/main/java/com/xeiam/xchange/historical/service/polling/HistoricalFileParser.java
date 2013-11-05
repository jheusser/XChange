package com.xeiam.xchange.historical.service.polling;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

class HistoricalFileParser<T> {
	private final File file;
	private final LineFactory<T> lineFactory;

	public HistoricalFileParser(File file, LineFactory<T> lineFactory) {
		this.file = file;
		this.lineFactory = lineFactory;
	}
	
	public List<T> parse() throws IOException {
		ImmutableList<T> matchingLines = Files.readLines(this.file, Charsets.UTF_8, 
				new LineProcessor<ImmutableList<T>>() {

			final ImmutableList.Builder<T> builder = ImmutableList.builder();

			@Override public boolean processLine(String line) {
				builder.add(lineFactory.instantiateFromLine(line));
				return true;
			}

			@Override public ImmutableList<T> getResult() {
				return builder.build();
			}

		});
		return matchingLines;
	}
}