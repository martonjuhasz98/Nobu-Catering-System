package dblayer.interfaces;

import modlayer.*;

public interface IFDBTransaction {

    public Transaction selectTransaction(int id);
    public int insertTransaction(Transaction transaction);
}