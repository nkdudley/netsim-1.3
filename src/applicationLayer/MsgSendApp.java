package applicationLayer;

import configurator.Logger;

import java.nio.charset.StandardCharsets;

public class MsgSendApp extends Application implements Runnable {
    /** thread for sending the characters */
    Thread execThread;
    /** message text to send */
    String myMsg;
    byte destination;

    /**
     * Does nothing
     * @param data byte[] of the Application data
     */
    @Override
    public void receiveFromTransport(byte[] data) {

    }

    /**
     * Starts a thread to send each message character
     */
    @Override
    public void bringUp() {
        super.bringUp();
        execThread = new Thread(this);
        execThread.start();
    }

    /**
     * Sets the message to be displayed by this application
     * @param args all the arguments to this application
     */
    @Override
    public void recvLaunchArgs(String args) {
        String[] msgInfo = args.split(" ");

        destination = (byte) Integer.parseInt(msgInfo[0]);
        myMsg = msgInfo[1];
    }

    /**
     * Thread to send the characters ~500ms apart
     */
    @Override
    public void run() {
        byte[] packet = new byte[myMsg.length() + 1];
        byte[] msg = myMsg.getBytes();
        packet[0] = destination;
        for(int i = 1; i <= myMsg.length(); i++){
            packet[i] = msg[i-1];
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {}
        getTransport().receiveFromApplication(this, packet);
        getTransport().removeApplication(this);
    }
}
