package Utils;

import java.io.DataInput;
import java.io.IOException;
import java.io.RandomAccessFile;

public final class LeIndex implements Comparable<Object> {

    private long NIS;
    private long indArqPrincipal;
    private long indArqLocal;
    static short TamReg = 35;

    public LeIndex(DataInput din) throws IOException{
        leIndex(din);
    }

    public LeIndex() {
    }

    public void leIndex(DataInput din) throws IOException {
        byte[] nis = new byte[14];
        byte[] indAP = new byte[9];
        byte[] indAL = new byte[9];
        din.readFully(nis);
        din.readByte();
        din.readFully(indAP);
        din.readByte();
        din.readFully(indAL);
        din.readByte();

        this.NIS = Long.parseLong(new String(nis));
        this.indArqPrincipal = Long.parseLong(new String(indAP));
        this.indArqLocal = Long.parseLong(new String(indAL));
    }
    
    public void escreveIndex(RandomAccessFile din) throws IOException{
        din.writeBytes((String.format("%014d",(NIS))));
        din.writeBytes(" ");
        din.writeBytes(String.format("%09d",(indArqPrincipal)));
        din.writeBytes(" ");
        din.writeBytes(String.format("%09d",(indArqLocal)));
        din.writeBytes("\n");
    }
    
    @Override
    public String toString() {
        return Long.toString(NIS);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LeIndex) {
            return (this.NIS == ((LeIndex) obj).NIS);
        } else {
            return false;
        }
    }

    public long getNIS() {
        return NIS;
    }

    public void setNIS(long NIS) {
        this.NIS = NIS;
    }

    public long getIndArqPrincipal() {
        return indArqPrincipal;
    }

    public void setIndArqPrincipal(long indArqPrincipal) {
        this.indArqPrincipal = indArqPrincipal;
    }

    public long getIndArqLocal() {
        return indArqLocal;
    }

    public void setIndArqLocal(long indArqLocal) {
        this.indArqLocal = indArqLocal;
    }

    @Override
    public int compareTo(Object o) {
        return Long.compare(this.NIS, ((LeIndex) o).getNIS());
    }
}