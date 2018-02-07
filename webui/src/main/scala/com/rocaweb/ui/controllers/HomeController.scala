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

package com.rocaweb.ui.controllers

import javax.inject._
import play.api._
import play.api.mvc._

@Singleton
class HomeController @Inject() (cc: ControllerComponents, config: play.api.Configuration, env: play.api.Environment) extends RocawebController(cc, config, env) {

  /**
   * Create an Action to access the Dashboard of RoCaweb
   */
  def dashboard = Action {
    Ok(views.html.dashboard(getKibanaHost, getKibanaPort, ""))
  }

  /**
   * Creates an Action to access the Learning of RoCaWeb
   * The learning process includes many algorithms which this
   * views allows to use and configure
   */
  def learning = Action {
    Ok(views.html.learning(""))
  }

  /**
   * Creates an Action to allow the visualization and manipulation
   * of the generated profiles.
   */
  def profile = Action {
    Ok(views.html.profile())
  }

  /**
   * Creates an Action to allow the manipulation of the data sources used
   * for the Learning process. Many data sources are possible such as:
   *    - Local PDML files
   *    - Elasticsearch indexes constructed from Modsecurity alerts
   */
  def data = Action {
    Ok(views.html.data())
  }

  /**
   * Creates a views to allow the manipulation of the current connect user
   * The configuration files can be saved in:
   *   - the local user home directory
   *   - in a data base
   */
  def user = Action {
    Ok(views.html.user())
  }

  /**
   * Creates an Action to manages different notifications related to RoCaWeb.
   * The includes:
   *   - security alerts related to:
   *     - Playframework
   *     - Scala
   *     - Java
   *     - Elastic
   *     - ModSecurity
   *   - events:
   *     - Conferences
   *     - New articles
   *
   *   - Updates of a new Version
   */
  def notifications = Action {
    Ok(views.html.notifications())
  }

  /**
   * Creates an Action to manage the configration of RoCaWeb
   * This configuration is done for a given user and can touch:
   *  - The algorithms
   *  - The servers
   *  - etc
   */
  def rocawebconfigview = Action {
    Ok(views.html.configuration())
  }

  /**
   * Creates an Action to visualize the current state of the RoCaWeb Agent
   */
  def agent = Action {
    Ok(views.html.agent(getAgentHost, getAgentPort))
  }
  /**
   * Creates a view to visualze the documentation related to RoCaWeb
   */
  def help = Action {
    Ok(views.html.help())
  }

}
