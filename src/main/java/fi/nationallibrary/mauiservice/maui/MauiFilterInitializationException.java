package fi.nationallibrary.mauiservice.maui;

/*-
 * #%L
 * fi.nationallibrary:mauiservice
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2018 National Library Finland
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

public class MauiFilterInitializationException extends Exception {
	private static final long serialVersionUID = 1L;

	public MauiFilterInitializationException(Throwable cause) {
		super(cause);
	}

	public MauiFilterInitializationException(String message) {
		super(message);
	}
	
	public MauiFilterInitializationException(String message, Throwable cause) {
		super(message, cause);
	}
}
