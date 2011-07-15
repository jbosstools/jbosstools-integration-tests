package org.jboss.ws;
 
import javax.jws.WebService;
 
@WebService(serviceName = "AreaService",
            endpointInterface = "org.jboss.ws.AreaService")
public class AreaServiceImpl implements AreaService {
    public float calculateRectArea(Dimensions parameters) {
        return parameters.getHeight() * parameters.getWidth();
    }
}
