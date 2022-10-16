package applicationLayer;

import configurator.Logger;
import exceptions.LayerNotConfigured;
import networkLayer.NewNetwork;

public class LogSpoolApp extends Application {
    /**
     * Spools what ever is received from the transport to the log stream
     * @param data byte[] of the Application data
     */
    @Override
    public void receiveFromTransport(byte[] data) {
        System.out.println(((NewNetwork) getTransport().getNetworkLayer()).getAddress());
        String output = new String(data);
        Logger.log(output);
    }

}
