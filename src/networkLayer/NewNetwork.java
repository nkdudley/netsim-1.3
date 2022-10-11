package networkLayer;

import linkLayer.Link;

public class NewNetwork extends Network{

    @Override
    public void bringDown() {

    }

    @Override
    public void interrupt() {

    }

    @Override
    public void receiveFromTransport(byte[] p) {
        //lets assume p is passed in as a packet already
        for(Link l: links){
            //if this link is addressed to the same one as the metadata expects it to be
            //then send the message on this link
        }
    }

    @Override
    public void recieveFromLink(byte[] f) {

    }

    @Override
    public byte[] toRawBytes(NetworkPacket p) {
        byte[] output = ((Meta) p.meta()).getData() + p.data();
        return output;
    }

    @Override
    public NetworkPacket fromRawBytes(byte[] bits) {
        return null;
    }
}
