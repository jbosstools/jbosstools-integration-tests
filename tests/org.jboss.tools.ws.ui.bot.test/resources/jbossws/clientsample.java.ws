package org.jboss.wsclient.clientsample;
 
import org.jboss.wsclient.*;
 
public class ClientSample {
 
    public static void main(String[] args) {
            System.out.println("***********************");
            System.out.println("Create Web Service Client...");
            AreaService_Service service1 = new AreaService_Service();
            System.out.println("Create Web Service...");
            AreaService port1 = service1.getAreaServiceImplPort();
            Dimensions d1 = new Dimensions();
            d1.setHeight(5);
            d1.setWidth(7.5f);
            System.out.println("Call Web Service Operation...");
            System.out.println(
                   "Server said: " + port1.calculateRectArea(d1));
    
            System.out.println("Create Web Service...");
            AreaService port2 = service1.getAreaServiceImplPort();
            Dimensions d2 = new Dimensions();
            d2.setHeight(362.1f);
            d2.setWidth(9.7f);
            System.out.println("Call Web Service Operation...");
            System.out.println(
                   "Server said: " + port2.calculateRectArea(d2));
            System.out.println("***********************");
            System.out.println("Call Over!");
    }
}
