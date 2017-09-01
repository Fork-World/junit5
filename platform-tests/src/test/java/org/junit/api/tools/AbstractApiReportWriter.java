/*
 * Copyright 2015-2017 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.junit.api.tools;

import java.io.PrintWriter;
import java.nio.CharBuffer;
import java.util.EnumSet;
import java.util.List;

import org.junit.platform.commons.meta.API.Usage;

/**
 * @since 1.0
 */
abstract class AbstractApiReportWriter implements ApiReportWriter {

	private final ApiReport apiReport;

	AbstractApiReportWriter(ApiReport apiReport) {
		this.apiReport = apiReport;
	}

	@Override
	public void printReportHeader(PrintWriter out) {
		out.println(h1("`@API` Declarations"));
		out.println();
		out.printf("Discovered %d types with `@API` declarations.%n%n", this.apiReport.getTypes().size());
	}

	@Override
	public void printDeclarationInfo(PrintWriter out, EnumSet<Usage> usages) {
		// @formatter:off
		this.apiReport.getDeclarationsMap().entrySet().stream()
				.filter(e -> usages.contains(e.getKey()))
				.forEach(e -> this.printDeclarationSection(e.getKey(), e.getValue(), out));
		// @formatter:on
	}

	protected void printDeclarationSection(Usage usage, List<Class<?>> types, PrintWriter out) {
		printDeclarationHeader(usage, types, out);
		if (types.size() > 0) {
			printDeclarationTableHeader(out);
			types.forEach(type -> this.printDeclarationTableDetails(type, out));
			printDeclarationTableFooter(out);
			out.println();
		}
	}

	protected void printDeclarationHeader(Usage usage, List<Class<?>> types, PrintWriter out) {
		out.println(h2(String.format("`@API(%s)`", usage)));
		out.println();
		out.printf("Discovered %d `@API(%s)` declarations.%n%n", types.size(), usage);
	}

	protected abstract String h1(String header);

	protected abstract String h2(String header);

	protected abstract void printDeclarationTableHeader(PrintWriter out);

	protected abstract void printDeclarationTableDetails(Class<?> type, PrintWriter out);

	protected abstract void printDeclarationTableFooter(PrintWriter out);

	protected String getKind(Class<?> type) {
		if (type.isAnnotation()) {
			return "annotation";
		}
		if (type.isEnum()) {
			return "enum";
		}
		if (type.isInterface()) {
			return "interface";
		}
		return "class";
	}

	protected String code(String element) {
		return "`" + element + "`";
	}

	protected String dashes(int length) {
		return CharBuffer.allocate(length).toString().replace('\0', '-');
	}

}