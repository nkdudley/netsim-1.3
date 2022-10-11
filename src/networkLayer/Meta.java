package networkLayer;

public class Meta {

    private int addressTo;
    private int addressFrom;
    private int packetNum;
    private int totalNum;

    public int getAddressTo(){return addressTo;}
    public int getAddressFrom(){return addressFrom;}
    public int getTotalNum(){return totalNum;}
    public int getPacketNum(){return packetNum;}

    public void setAddressTo(int to){addressTo = to;}
    public void setAddressFrom(int from){addressFrom = from;}
    public void setPacketNum(int num){packetNum = num;}
    public void setTotalNum(int num){totalNum = num;}

}
