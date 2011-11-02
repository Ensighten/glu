/*
 * Copyright (c) 2010-2010 LinkedIn, Inc
 * Portions Copyright (c) 2011 Yan Pujante
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.linkedin.glu.console.controllers

import org.linkedin.glu.grails.utils.ConsoleConfig
import org.linkedin.glu.orchestration.engine.delta.DeltaService

import org.linkedin.glu.orchestration.engine.delta.CustomGroupByDelta

import org.linkedin.glu.orchestration.engine.session.UserSession
import org.linkedin.glu.provisioner.core.model.SystemModel
import org.linkedin.glu.groovy.utils.GluGroovyLangUtils
import org.linkedin.glu.orchestration.engine.system.SystemService

/**
 * @author ypujante@linkedin.com
 */
class DashboardController extends ControllerBase
{
  DeltaService deltaService
  ConsoleConfig consoleConfig
  SystemService systemService

  def beforeInterceptor = {
    // we make sure that the fabric is always set before executing any action
    return ensureCurrentFabric()
  }

  /**
   * Redirect to computeDelta
   */
  def index = {
    redirect(action: 'delta')
  }

  /**
   * Renders only the portion below the menus
   */
  def renderDelta = {
    render(template: '/dashboard/delta', model: [delta: doComputeDelta()])
  }

  /**
   * Delta the live system: display condensed view of all apps accross all agents
   */
  def delta = {
    return [delta: doComputeDelta()]
  }

  /**
   * Called in order to customize the dashboard
   */
  def customize = {
    Map rawDelta = deltaService.computeRawDelta(request.system).delta.flatten(new TreeMap())

    def sources = [] as TreeSet

    rawDelta.values().each {
      sources.addAll(it.keySet())
    }

    [
      sources: sources
    ]
  }

  /**
   * Called for viewing the plans
   */
  def plans = {
    CustomGroupByDelta groupByDelta = doComputeDelta()

    def missingAgents = systemService.getMissingAgents(request.fabric, request.system)

    [
      title: 'tbd',
      hasDelta: groupByDelta.counts['errors'] > 0,
      missingAgents: missingAgents
    ]
  }

  private def doComputeDelta()
  {
    CustomGroupByDelta groupByDelta =
      deltaService.computeCustomGroupByDelta(request.system,
                                             request.userSession.customDeltaDefinition)
    return groupByDelta
  }
}
