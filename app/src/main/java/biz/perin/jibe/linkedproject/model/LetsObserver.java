package biz.perin.jibe.linkedproject.model;

/**
 * Created by tbpk7658 on 15/11/2017.
 */
public interface LetsObserver {
    public void onNewAnnounce(Announce ann);
    public void onNewPerson(Person pers);
    public void onNewPost(ForumMessage mess);
    public void onNewTransaction(Transaction trans);
}
