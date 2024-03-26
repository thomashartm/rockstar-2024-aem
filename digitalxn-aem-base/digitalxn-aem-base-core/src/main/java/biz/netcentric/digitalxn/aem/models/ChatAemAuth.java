package biz.netcentric.digitalxn.aem.models;

import com.adobe.cq.wcm.core.components.models.Component;

public interface ChatAemAuth extends Component {

    String getToken();

    String getAppUrl();
}
