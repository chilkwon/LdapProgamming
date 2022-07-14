import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Properties;

public class LdapApp {

    DirContext connection;

    public static void main(String[] args) throws NamingException {

        LdapApp app = new LdapApp();
        app.newConnection();
        app.getAllUsers();


    }
    public void newConnection(){
        Properties env = new Properties();

        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://localhost:10389");
        env.put(Context.SECURITY_PRINCIPAL, "uid=admin,ou=system");
        env.put(Context.SECURITY_CREDENTIALS, "secret");
        try {
            connection = new InitialDirContext(env);
            System.out.println("LDAP Connection created: " + connection);
        } catch (NamingException e) {
            e.printStackTrace();


        }
    }

    public void getAllUsers() throws NamingException {
        String searchFilter = "(ObjectClass=inetOrgPerson)";
        String[] reqAttribute ={"cn","sn"};
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(reqAttribute);

        NamingEnumeration users = connection.search("ou=users,ou=system",searchFilter,controls);
        SearchResult result = null;

        while(users.hasMore()){
            result = (SearchResult) users.next();
            Attributes attr = result.getAttributes();
            System.out.println(attr.get("cn"));
            System.out.println(attr.get("sn"));
        }

    }
}
