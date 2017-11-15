/*
 * Copyright (C) 2017  Jean-Baptiste Perin <jean-baptiste.perin@orange.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package biz.perin.jibe.linkedproject.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by Jean-Baptiste PERIN on 03/11/2017.
 */
public class LocalSystemExchange extends LetsObservable implements ILocalSystemExchange{

    private final List<Person> listOfPerson = new ArrayList<>();
    private final List<Announce> listOfAnnounce = new ArrayList<>();
    private final List<Forum> listOfForum = new ArrayList<>();
    private final List<Transaction> listOfTransaction = new ArrayList<>();
    private Forum tabOfForum[] = new Forum[Forum.forum_category.values().length];
    private PersonnalInfo personnalInfo = null;
    private Account account = null;

    public LocalSystemExchange() {
        for (Forum.forum_category forcat : Forum.forum_category.values()) {
            Forum nForum = new Forum();
            nForum.setRubrique(forcat);

            listOfForum.add(nForum);
            tabOfForum[forcat.ordinal()] = nForum;
        }
    }

    @Override
    public List<Person> getListOfPerson() {
        return listOfPerson;
    }

    @Override
    public List<Announce> getListOfAnnounce() {
        return listOfAnnounce;
    }

    @Override
    public List<Forum> getListOfForum() {
        return listOfForum;
    }

    @Override
    public void add(PersonnalInfo object) {
        // TODO rename in setPersonnalInfo
        personnalInfo = object;
    }

    @Override
    public void add(Account acc) {
        // TODO rename in setAccount
        account = acc;
    }

    @Override
    public Person add(Person pers) {
        for (Person person : listOfPerson) {
            if (person.getPseudo().equals(pers.getPseudo())) {
                // TODO update fields of already existing person
                return person;
            }
        }
        if (listOfPerson.add(pers)) {
            notifyNew(pers);
            return pers;
        } else return null;
    }

    @Override
    public Announce add(Announce ann) {
        for (Announce announce : listOfAnnounce) {
            if ((announce.getDirection().equals(ann.getDirection()))
                    && (announce.getDescription().equals(ann.getDescription()))
                    ) {
                announce.setOwnerPseudo(ann.getOwnerPseudo());
                announce.setId(ann.getId());
                announce.setDescription(ann.getDescription());
                // TODO update fields of already existing announce
                return announce;
            }
        }
        if (listOfAnnounce.add(ann)) {
            notifyNew(ann);
            return ann;
        }
        return null;
    }

    @Override
    public Transaction add(Transaction trans) {
        // TODO Checck if transaction does not already exist
        if (listOfTransaction.add(trans)) {
            notifyNew(trans);
            return trans;
        }
        return null;
    }

    @Override
    public Discussion add(Discussion dis, Forum.forum_category forum_cat) {

        Forum theForum = tabOfForum[forum_cat.ordinal()];
        if (theForum == null) {
            System.out.println("");
        }
        for (Discussion discussion : theForum.getListOfDiscussion()) {
            if (discussion.getId() == dis.getId()) {
                return discussion;
            }
        }
        if (theForum.getListOfDiscussion().add(dis)) {
            return (dis);
        }
        return null;
    }

    @Override
    public ForumMessage add(ForumMessage mess) {
        Discussion disc = null;
        Forum theForum = tabOfForum[mess.getCategory().ordinal()];
        for (Discussion discussion : theForum.getListOfDiscussion()) {
            if (discussion.getId() == mess.getDiscussionId()) {
                disc = discussion;
                break;
            }
        }
        if (disc != null) {
            disc.getListOfMessages().add(mess);
            notifyNew(mess);
            return mess;
        } else {
            // TODO trigger exception
            //throw IOException();
        }
        return null;
    }
}