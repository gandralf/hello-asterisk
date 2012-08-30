package asterisk;

import java.io.IOException;

import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;

public class HelloManager
{
    private ManagerConnection managerConnection;

    public HelloManager() throws IOException
    {
        ManagerConnectionFactory factory = new ManagerConnectionFactory(
                "talkin.gonow.com.br", "manager", "Talkin@GoVOiP2009");

        this.managerConnection = factory.createManagerConnection();
    }

    public void run() throws IOException, AuthenticationFailedException,
            TimeoutException
    {
        ManagerResponse originateResponse;

        // connect to Asterisk and log in
        managerConnection.login();

        // send the originate action and wait for a maximum of 30 seconds for Asterisk
        // to send a reply
        OriginateAction originateAction = new OriginateAction();
        originateAction.setChannel("SIP/e1-gonow");
        originateAction.setContext("default"); // "default - entrada_usuarios
        originateAction.setExten("1300"); // 1300 - s
        originateAction.setPriority(1);
        originateAction.setTimeout(30000L);
        originateResponse = managerConnection.sendAction(originateAction, 30000);

        // print out whether the originate succeeded or not
        System.out.println(originateResponse.getResponse());

        // and finally log off and disconnect
        managerConnection.logoff();
    }

    public static void main(String[] args) throws Exception
    {
        HelloManager helloManager;

        helloManager = new HelloManager();
        helloManager.run();
    }
}