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

import java.util.*;

/**
 * Created by Jean-Baptiste PERIN on 03/11/2017.
 */
public class Forum {

    private forum_category rubrique;
    private List<Discussion> listOfDiscussion = null;

    public forum_category getRubrique() {
        return rubrique;
    }

    public List<Discussion> getListOfDiscussion() {
        return listOfDiscussion;
    }

    protected void setRubrique(forum_category rubrique) {
        this.rubrique = rubrique;
    }

    public Forum() {
        listOfDiscussion = new ArrayList<>();
    }

    public static enum forum_category {
        ANNONCE_HORS_CLOUS ("Annonces hors clous")
                ,BON_PLAN ("Bons plans")
                ,ACHATS_GROUPE ("Achats groupés")
//                TODO,"Coups de gueule/Coups de coeur"
                ,RECETTES ("Vos recettes ")
                ,IDEES_SORTIES ("Idées sorties")
                ,SORTIR_ENSEMBLE ("Sortir ensemble")
                ,VOS_ASSOCIATIONS ("Vos associations")
                ,HUMOUR ("Humour")
                ,AUTRES ("Autres")
               ;
        private String name;
        forum_category(String name) {this.name = name;}
        @Override
        public String toString() {
            return  name ;
        }

        private static Map<String, forum_category> stringMap = new HashMap<String, forum_category>();

        static {
            stringMap.put("Annonces hors clous", ANNONCE_HORS_CLOUS);
            stringMap.put("Bons plans", BON_PLAN);
            stringMap.put("Achats groupés" , ACHATS_GROUPE);
            stringMap.put("Vos recettes "                                    , RECETTES);
            stringMap.put("Idées sorties"                                  , IDEES_SORTIES);
            stringMap.put("Sortir ensemble"                                      , SORTIR_ENSEMBLE);
            stringMap.put("Vos associations"                                         , VOS_ASSOCIATIONS);
            stringMap.put("Autres"                                     , AUTRES);
            stringMap.put("Humour"                                     , HUMOUR);

            stringMap = Collections.unmodifiableMap(stringMap);
        }
        public static forum_category fromName(String name) {return stringMap.get(name);}


    }
}
