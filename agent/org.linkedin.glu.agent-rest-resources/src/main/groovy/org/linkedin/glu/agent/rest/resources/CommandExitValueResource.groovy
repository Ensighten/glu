/*
 * Copyright (c) 2012 Yan Pujante
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



package org.linkedin.glu.agent.rest.resources

import org.restlet.Context
import org.restlet.Request
import org.restlet.Response
import org.restlet.representation.Representation
import org.restlet.representation.Variant

/**
 * @author yan@pongasoft.com */
public class CommandExitValueResource extends CommandBaseResource
{
  CommandExitValueResource(Context context, Request request, Response response)
  {
    super(context, request, response);
  }

  @Override
  boolean allowGet()
  {
    return true
  }

  /**
   * GET: return the exit value of the command (blocking call with optional timeout)
   */
  public Representation represent(Variant variant)
  {
    return noException {
      return toRepresentation(res: agent.waitForCommand(requestArgs))
    }
  }

}