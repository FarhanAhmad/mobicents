package org.mobicents.slee.container.component.deployment.jaxb.descriptors.profile.query;

import java.util.ArrayList;
import java.util.List;

/**
 * Start time:17:26:08 2009-01-18<br>
 * Project: mobicents-jainslee-server-core<br>
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public class MQuery {

  private String name;

  private List<MQueryParameter> queryParameters;
  private MQueryOptions queryOptions;
  private MQueryExpression queryExpression;

  public MQuery(org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.Query query11)
  {
    this.name = query11.getName();

    this.queryParameters = new ArrayList<MQueryParameter>();

    if (query11.getQueryParameter() != null && query11.getQueryParameter().size() > 0)
    {
      for (org.mobicents.slee.container.component.deployment.jaxb.slee11.profile.QueryParameter queryParameter11 : query11.getQueryParameter())
      {
        this.queryParameters.add(new MQueryParameter(queryParameter11));
      }
    }

    if (query11.getQueryOptions() != null)
    {
      this.queryOptions = new MQueryOptions(query11.getQueryOptions());
    }

    //get(0) - xml validation takes care of it, we always have exactly one at this stage
    this.queryExpression = new MQueryExpression(query11.getCompareOrRangeMatchOrLongestPrefixMatchOrHasPrefixOrAndOrOrOrNot().get(0));
  }

  public List<MQueryParameter> getQueryParameters() {
    return queryParameters;
  }

  public MQueryOptions getQueryOptions() {
    return queryOptions;
  }

  public MQueryExpression getQueryExpression() {
    return queryExpression;
  }

  public String getName() {
    return name;
  }

}
