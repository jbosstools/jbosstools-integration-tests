package {0};
 
import javax.jws.WebService;
import test.Person;
 
@WebService
public class {1} '{'
 
    public Person echo(Person p) '{'
        return p;
    '}'
    
    public Person test() '{'
        return new Person();
    '}'
'}'
