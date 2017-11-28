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

import java.util.List;

/**
 * Created by Jean-Baptiste PERIN on 03/11/2017.
 */
public interface ILocalSystemExchange {
    public Person add (Person pers);
    public Announce add (Announce ann);
    public Transaction add (Transaction trans);
    public Discussion add (Discussion dis, Forum.forum_category forum_cat);
    public ForumMessage add (ForumMessage mess);
    public List<Person> getListOfPerson();
    public List<Announce> getListOfAnnounce();
    public List<Forum> getListOfForum();
    public List<Transaction> getListOfTransaction();
    public PersonnalInfo getPersonnalInfo();
    public Account getAccount();

    public void add(PersonnalInfo object);
    public void add(Account object);
}

