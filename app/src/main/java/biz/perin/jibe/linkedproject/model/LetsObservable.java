package biz.perin.jibe.linkedproject.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tbpk7658 on 15/11/2017.
 */
public class LetsObservable {
    private transient List<LetsObserver> listOfObservers = new ArrayList <> ();

    public void attach(LetsObserver anObserver) {
        listOfObservers.add(anObserver);
    }

    void notifyNew (Person pers){
        for (LetsObserver obs: listOfObservers){
            obs.onNewPerson(pers);
        }
    }

    void notifyNew (Account acc){
        for (LetsObserver obs: listOfObservers){
            obs.onNewAccount(acc);
        }
    }


    void notifyNew (Announce ann){
        for (LetsObserver obs: listOfObservers){
            obs.onNewAnnounce(ann);
        }
    }

    void notifyNew (ForumMessage mess){
        for (LetsObserver obs: listOfObservers){
            obs.onNewPost(mess);
        }
    }

    void notifyNew (Transaction trans){
        for (LetsObserver obs: listOfObservers){
            obs.onNewTransaction(trans);
        }
    }


}
