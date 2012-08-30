package asterisk;

import org.apache.log4j.Logger;
import org.asteriskjava.manager.*;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.event.ManagerEvent;
import org.asteriskjava.manager.response.ManagerResponse;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AsteriskClientTest {
    private static Logger log = Logger.getLogger("asterisk.AsteriskClient");

    @Test public void shouldConnect() throws IOException, AuthenticationFailedException, TimeoutException, InterruptedException {
        AsteriskRunner asteriskRunnable = new AsteriskRunner();
        new Thread(asteriskRunnable).start();

        asteriskRunnable.call("01181111048");

        Thread.sleep(200000);
    }

    public static class AsteriskRunner implements Runnable {
        private ManagerConnection managerConnection;
        private boolean open;
        private BlockingQueue<ManagerEvent> events = new LinkedBlockingQueue<ManagerEvent>();

        public AsteriskRunner() throws IOException, AuthenticationFailedException, TimeoutException {
            ManagerConnectionFactory factory =
                    new ManagerConnectionFactory("talkin.gonow.com.br", "manager", "Talkin@GoVOiP2009");
            managerConnection = factory.createManagerConnection();
            managerConnection.addEventListener(new EventListener());
            managerConnection.login();
            open = true;
        }

        @Override
        public void run() {
            try {
                while (isOpen()) {
                    ManagerEvent event = events.take();
                    if (event != null) {
                        log.debug(event);
                    }
                }
            } catch (InterruptedException e) {
                log.error(e, e);
            }
        }

        public void call(String number) throws IOException {
            OriginateAction action = newAction(number, "e1-gonow");
            managerConnection.sendAction(action, new SendActionCallback() {
                @Override
                public void onResponse(ManagerResponse response) {
                    log.debug(response);
                }
            });
        }

        private OriginateAction newAction(String number, String provider) {
            OriginateAction originateAction = new OriginateAction();
            originateAction.setChannel("SIP/" + provider + "/" + number); // call.getTalkerPhone());
            originateAction.setContext("entrada_usuarios");
            originateAction.setPriority(1);
            originateAction.setTimeout(30000L);
            originateAction.setExten("s");
            originateAction.setCallerId("wVnIgBI6qU9235bQ");  // todo wtf?!?! call.getTalkerCallerId());
            originateAction.setVariable("NCONF", "789834194258"); // call.getRoom());
            originateAction.setVariable("AUDIOUSER", "1_AUDIO_TALKER");
            originateAction.setVariable("var1", "var1");
            originateAction.setVariable("var2", "var2");
            originateAction.setVariable("var3", "var3");
            originateAction.setVariable("var4", "var4");
            originateAction.setVariable("var5", "var5");
            originateAction.setVariable("var6", "var6");
            originateAction.setVariable("var7", "var7");
            originateAction.setVariable("var8", "var8");
            originateAction.setVariable("var9", "var9");
            originateAction.setVariable("var10", "var10");
            originateAction.setVariable("TIMEOUT(absolute)", "900");

            originateAction.setVariable("call_id", "900"); // String.valueOf(call.getId()));

            originateAction.setVariable("ipaddress", "192.168.1.2"); // call.getIp());
            originateAction.setVariable("destinationnumber", number); // call.getTalkerPhone());
            originateAction.setVariable("callersnumber", number); // call.getTalkerPhone());
            originateAction.setVariable("callersname", "");
            originateAction.setVariable("url", "");

            return originateAction;
        }

        public boolean isOpen() {
            return open;
        }

        public class EventListener implements ManagerEventListener {
            public void onManagerEvent(ManagerEvent event) {
                events.add(event);
            }
        }
    }
}
