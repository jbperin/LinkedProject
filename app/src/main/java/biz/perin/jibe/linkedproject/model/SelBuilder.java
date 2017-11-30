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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;

/**
 * Created by Jean-Baptiste PERIN on 02/11/2017.
 */
public class SelBuilder implements ISelReceiver {

    private ILocalSystemExchange model= null;

    public SelBuilder(ILocalSystemExchange model) {
        this.model = model;
    }

    private Integer nullify (String solde){
        int iSolde=0;
        if (solde == null) return (null);
        if (solde.equals("null")) return (null);
        try {
            iSolde = Integer.parseInt(solde);
        } catch (java.lang.NumberFormatException e) {
            e.printStackTrace();
            try {
                iSolde = new DecimalFormat("+#;-#").parse(solde).intValue();
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
        }
         return (iSolde);
    }


    @Override
    public void accept(Entity entity, String s) {

        JSONParser parser = new JSONParser();
        JSONObject jsonObject;

        try {
            Object obj = parser.parse(s);

            jsonObject = (JSONObject) obj;
            Class<?> c = Class.forName(entity.className());
            Constructor<?> cons = c.getConstructor();
            Object object = cons.newInstance();

            switch (entity) {
                case SEL_ACCOUNT_INFO:
                    String nb_echange = (String) jsonObject.get("nb_echange");
                    String solde = (String) jsonObject.get("solde");
                    String solde_actuel = (String) jsonObject.get("solde_actuel");
                    String derniere_publication = (String) jsonObject.get("derniere_publication");

                    ((Account)object).setLastPublish(derniere_publication);
                    ((Account)object).setNbEchange(nullify(nb_echange));
                    ((Account)object).setSolde(nullify(solde));
                    ((Account)object).setSoldeActuel(nullify(solde_actuel));

                    model.add((Account)object);
                    break;

                case SEL_ANONYMOUS_ANNOUNCE:
                    String description = (String) jsonObject.get("description");
                    String direction = (String) jsonObject.get("direction");
                    String category = (String) jsonObject.get("category");
                    String id = (String) jsonObject.get("id");

                    if ((category != null) && (!category.equals("null")))
                        ((Announce) object).setCategory(Announce.Category.fromName(category));
                    ((Announce) object).setDirection(Announce.Direction.valueOf(direction));
                    ((Announce) object).setDescription(description);
                    ((Announce) object).setId(nullify(id));

                    model.add((Announce)object);
                    break;

                case SEL_ANNOUNCE:
                    String ann_description = (String) jsonObject.get("description");
                    String ann_direction = (String) jsonObject.get("direction");
                    String ann_category = (String) jsonObject.get("category");
                    String owner_pseudo = (String) jsonObject.get("owner_pseudo");

                    if ((ann_category != null) && (!ann_category.equals("null")))
                        ((Announce) object).setCategory(Announce.Category.valueOf(ann_category));
                    ((Announce) object).setDirection(Announce.Direction.valueOf(ann_direction));
                    ((Announce) object).setDescription(ann_description);
                    ((Announce) object).setOwnerPseudo(owner_pseudo);


                    model.add((Announce)object);
                    break;

                case SEL_PERSON:

                    String name = (String) jsonObject.get("name");
                    String pseudo = (String) jsonObject.get("pseudo");
                    String address = (String) jsonObject.get("address");
                    String last_publish = (String) jsonObject.get("last_publish");
                    String phone1 = (String) jsonObject.get("phone1");
                    String phone2 = (String) jsonObject.get("phone2");
                    String numberOfExchange = (String) jsonObject.get("numberOfExchange");
                    String personsolde = (String) jsonObject.get("solde");

                    ((Person)object).setAddress(address);
                    ((Person)object).setPseudo(pseudo);
                    ((Person)object).setPhone1(phone1);
                    ((Person)object).setPhone2(phone2);
                    ((Person)object).setNumberOfExchange(nullify(numberOfExchange));
                    ((Person)object).setSolde(nullify(personsolde));
                    ((Person)object).setName(name);
                    ((Person)object).setLastPublish(last_publish);

                    model.add((Person)object);

                    break;
                case SEL_TRANSACTION:
                    String pseudoOffer = (String) jsonObject.get("pseudo");
                    String jour = (String) jsonObject.get("jour");
                    String mois = (String) jsonObject.get("mois");
                    String nature = (String) jsonObject.get("nature");
                    String valeur = (String) jsonObject.get("valeur");


                    ((Transaction)object).setPseudoOfferer(pseudoOffer);
                    ((Transaction)object).setPseudoDemander(pseudoOffer);
                    ((Transaction)object).setAmount(nullify(valeur));
                    ((Transaction)object).setDemandDescription(nature);
                    ((Transaction)object).setOfferDescription(nature);

                    model.add((Transaction)object);


                    break;
                case SEL_MESSAGE_FORUM:


                    String forum_name  = (String) jsonObject.get("forum");
                    String date = (String) jsonObject.get("date");
                    String discussion_id = (String) jsonObject.get("discussion_id");
                    String text = (String) jsonObject.get("text");
                    String pseudoMessage = (String) jsonObject.get("pseudo");
                    ((ForumMessage) object).setCategory(Forum.forum_category.fromName(forum_name));
                    ((ForumMessage) object).setDate(date);
                    ((ForumMessage) object).setDiscussionId(nullify(discussion_id));
                    ((ForumMessage) object).setPseudo(pseudoMessage);
                    ((ForumMessage) object).setText(text);
                    model.add((ForumMessage) object);

                    break;
                case SEL_PERSONNAL_INFO:

                    String code_postal  = (String) jsonObject.get("code_postal");
                    String ville = (String) jsonObject.get("ville");
                    String tel_portable = (String) jsonObject.get("tel_portable");
                    String tel_fixe = (String) jsonObject.get("tel_fixe");
                    String namePerson = (String) jsonObject.get("name");
                    String adresse = (String) jsonObject.get("adresse");
                    String vocable = (String) jsonObject.get("vocable");
                    String prenom = (String) jsonObject.get("prenom");
                    String email = (String) jsonObject.get("email");
                    String date_naissance = (String) jsonObject.get("date_naissance");
                    ((PersonnalInfo) object).setCode_postal(code_postal);
                    ((PersonnalInfo) object).setVille(ville);
                    ((PersonnalInfo) object).setTel_portable(tel_portable);
                    ((PersonnalInfo) object).setTel_fixe(tel_fixe);
                    ((PersonnalInfo) object).setNamePerson(namePerson);
                    ((PersonnalInfo) object).setAdresse(adresse);
                    ((PersonnalInfo) object).setVocable(vocable);
                    ((PersonnalInfo) object).setPrenom(prenom);
                    ((PersonnalInfo) object).setEmail(email);
                    ((PersonnalInfo) object).setDate_naissance(date_naissance);

                    model.add((PersonnalInfo) object);
                    break;

                case SEL_DISCUSSION_FORUM:

                    String name_of_forum  = (String) jsonObject.get("forum_name");
                    String title = (String) jsonObject.get("title");
                    String id_discussion = (String) jsonObject.get("id_discussion");
                    ((Discussion) object).setId(nullify(id_discussion));
                    ((Discussion) object).setTitle(title);
                    model.add ((Discussion) object,  Forum.forum_category.fromName(name_of_forum));
                    break;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
