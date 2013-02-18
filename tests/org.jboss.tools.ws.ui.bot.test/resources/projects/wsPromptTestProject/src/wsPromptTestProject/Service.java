package wsPromptTestProject;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@Path("/rest")
public class Service {

	@GET
	@Path("/{id}")
	public String mainService(@PathParam("id") @DefaultValue("0") Integer id,
							  @MatrixParam("m1") @DefaultValue("m1") String m1,
							  @QueryParam("q1") @DefaultValue("q1") String q1) {
		return id + " " + m1 + " " + q1;
	}
	
}
