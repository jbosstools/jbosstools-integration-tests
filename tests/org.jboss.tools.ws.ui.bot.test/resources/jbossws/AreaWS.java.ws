package org.jboss.ws.impl;
 
import javax.jws.WebService;
import org.jboss.ws.AreaService;
import org.jboss.ws.Dimensions;
 
@WebService(serviceName = "AreaService",
            endpointInterface = "org.jboss.ws.AreaService")
public class AreaServiceImpl implements AreaService {
    public float calculateRectArea(Dimensions parameters) {
        return parameters.getHeight() * parameters.getWidth();
    }
}
