package checkstyle.prj.cstest.src.org.jbds.cs;

public class CSTestClass {
	public static void main(String[] args) {
		// Empty catch/finally block
		try {
			System.out.println("Empty try/catch test");
		} catch (Exception e) {

		} finally {

		}

		int i = 0;
		
		// Bad Switch 
		switch (i) {
		case 1:
		case 2:
		default:
		}
		
	}
}
