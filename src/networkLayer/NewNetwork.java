package networkLayer;

import configurator.Configurable;
import linkLayer.Link;
import physicalLayer.PipeBackedPort;

import java.io.PipedInputStream;

public class NewNetwork extends Network implements Configurable {

    private byte address;

    public byte getAddress(){return address;}
    @Override
    public void bringDown() {

    }

    @Override
    public void interrupt() {

    }

    @Override
    public void receiveFromTransport(byte[] p) {
        //lets assume p is passed in as a packet already
        //the first byte will be the addressTo
        byte to = p[0];
        for(Link l: links){
            if(((NewNetwork) ((PipeBackedPort) l.getPhysicalLayer()).getOtherPort().getLinkLayer().getNetworkLayer()).getAddress() == to){
                //maybe we need to reconfigure p first asa a link frame
                l.receiveFromNetwork(p);
            }

        }
    }

    @Override
    public void receiveFromLink(byte[] f) {

    }

    @Override
    public byte[] toRawBytes(NetworkPacket p) {
        byte[] output = new byte[p.data().length + ((Meta) p.meta()).getData().length];
        Meta meta = (Meta) p.meta();
        System.arraycopy(meta.getData(), 0, output, 0, meta.getData().length);
        System.arraycopy(p.data(), 0, output, meta.getData().length, p.data().length);
        return output;
    }

    @Override
    public NetworkPacket fromRawBytes(byte[] bits) {
        byte[] metadata = new byte[4];
        System.arraycopy(bits,0,metadata,0,4);
        Meta meta = new Meta(metadata);
        byte[] data = new byte[4];
        System.arraycopy(bits,4,data,0,4);
        NetworkPacket packet = new NetworkPacket(meta,data);
        return packet;
    }

    @Override
    public void configureWith(String s) {
        //take s and cast it to an integer
        int num = Integer.parseInt(s);
        //then cast the integer to a byte
        byte addrs = (byte) num;
        //assign the address to the byte
        address = addrs;

    }
}
