/*******************************************************************************
 * This file is part of RoCaWeb.
 * 
 *  RoCaWeb is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>
 ******************************************************************************/
package com.rocaweb.commons.configuration;

/**
 * Summarizes all the functions used by a configurator. Two configurators are
 * used :
 * <ul>
 * <li>XML</li>
 * <li>JSON</li>
 * </ul>
 * Also others configurators as INI can be implemented.
 * 
 * @author Djibrilla AMADOU KOUNTCHE
 * 
 * @since 1.0.0
 */
public interface IConfiguration {

	/** Get the value of the key name as a String */
	public String getString(String name);

	/** Get the value of the key name as a Boolean */
	public boolean getBoolean(String name);

	/** Get the value of the key name as an Integer */
	public int getInt(String name);

	/** Get the value of the key name as a Double */
	public double getDouble(String name);

	/** Determines whether the configurator contains the key name */
	public boolean containsKey(String name);

}
