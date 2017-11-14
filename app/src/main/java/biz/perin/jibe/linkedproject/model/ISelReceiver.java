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

/**
 * Created by Jean-Baptiste PERIN on 02/11/2017.
 */
public interface ISelReceiver {


    void accept(Entity personnalInfo, String s);

    public enum Entity {
        SEL_ACCOUNT_INFO ("biz.perin.jibe.linkedproject.model.Account")
        , SEL_ANONYMOUS_ANNOUNCE ("biz.perin.jibe.linkedproject.model.Announce")
        , SEL_ANNOUNCE ("biz.perin.jibe.linkedproject.model.Announce")
        , SEL_PERSON ("biz.perin.jibe.linkedproject.model.Person")
        , SEL_TRANSACTION ("biz.perin.jibe.linkedproject.model.Transaction")
        , SEL_MESSAGE_FORUM ("biz.perin.jibe.linkedproject.model.ForumMessage")
        , SEL_PERSONNAL_INFO ("biz.perin.jibe.linkedproject.model.PersonnalInfo")
        , SEL_DISCUSSION_FORUM("biz.perin.jibe.linkedproject.model.Discussion");

        private String class_name = "";

        //Constructeur
        Entity(String name){
            this.class_name = name;
        }

        public String toString(){
            return class_name;
        }
        public String className(){
            return class_name;
        }

    }
}
