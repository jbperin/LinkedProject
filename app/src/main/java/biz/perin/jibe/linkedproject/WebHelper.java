package biz.perin.jibe.linkedproject;

import biz.perin.jibe.linkedproject.file.FileHelper;
import biz.perin.jibe.linkedproject.model.*;
import biz.perin.jibe.linkedproject.web.IWebClient;


import java.net.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Famille PERIN on 25/10/2017.
 */
public class WebHelper {

    private static final String TAG = WebHelper.class.getSimpleName();
    private String LoginPageURL= "http://sel-des-deux-rives.org/catalogue/index.php";
    private static WebHelper mInstance = new WebHelper();

    private String login = null;
    private String password = null;

    IWebClient webClient = null;
    ISelReceiver selReceiver = null;
    private boolean authenticated = false;


    private WebHelper() {



    }
    public static WebHelper getInstance(){return mInstance;}

    public void setWebClient(IWebClient webClient) {
        this.webClient = webClient;
    }
    public void setSelReceiver(ISelReceiver selReceiver) {
        this.selReceiver = selReceiver;
    }

    public  void setAuthenticationInformation (String login, String password){

        this.login = login;
        this.password = password;

        if (webClient == null) {
            System.out.println("WebClient must be provided use SetWebClient");
            //TODO throw execption
        }
        try {
            if (webClient.connect (LoginPageURL, this.login, this.password)){
                authenticated = true;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void getPersonnalInfo(boolean onlinemode) {

        String htmlContent = null;
        final Map<String, String> dictPersonalInfo = new HashMap<>();

        if (onlinemode) {
            htmlContent = webClient.getSimpleWebPage("http://sel-des-deux-rives.org/catalogue/index.php?lien=mes_infos");
            FileHelper.getInstance().writeStringToFile(htmlContent, "MyInfo.html");
        }else {
            htmlContent = FileHelper.getInstance().readStringFromFile("MyInfo.html");
        }

        // Mail
        MyHelper.patternRunThrough(htmlContent
                , "<input name=\"mail\" type=\"text\" id=\"mail\" class=\"input300\" value=\"([a-zA-Z0-9@.]*)\" />"
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
//                        System.out.println ("Mesinfos : "
//                                + " email " + params[0]
//
//                        );
                        dictPersonalInfo.put("email", params[0] );



                    }
                });
        // Vocable
        MyHelper.patternRunThrough(htmlContent
                , "<input type=\"radio\" name=\"vocable\" value=\"([ A-Za-z]{1,10})\"[ \t\n]*checked='checked'"
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
//                        System.out.println ("Mesinfos : "
//                                + " vocable : " + params[0]
//
//                        );
                        dictPersonalInfo.put("vocable", params[0]);
                    }
                });
        // Nom
        MyHelper.patternRunThrough(htmlContent
                , "<input name=\"nom\" type=\"text\" id=\"nom\"  class=\"input300\" value=\"([^\"]*)\" />"
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
//                        System.out.println ("Mesinfos : "
//                                + " name :  " + params[0]
//
//                        );
                        dictPersonalInfo.put("name", params[0]);
                    }
                });
        // Prenom
        MyHelper.patternRunThrough(htmlContent
                , "<input name=\"prenom\" type=\"text\" id=\"prenom\"  class=\"input300\" value=\"([^\"]*)\" />"
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
//                        System.out.println ("Mesinfos : "
//                                + "prenom" + params[0]
//
//                        );
                        dictPersonalInfo.put("prenom" ,params[0]);
                    }
                });
        // Date de naissance
        MyHelper.patternRunThrough(htmlContent
                , "<input name=\"date_naiss_jour\" type=\"text\" id=\"date_naiss\"  class=\"input30\" value=\"([0-9]{0,3})\" />[ \t\n]*<input name=\"date_naiss_mois\" type=\"text\" id=\"date_naiss\"  class=\"input30\" value=\"([0-9]{0,3})\" />[ \t\n]*<input name=\"date_naiss_annee\" type=\"text\" id=\"date_naiss\"  class=\"input30\" value=\"([0-9]{0,4})\" />"
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
//                        System.out.println ("Mesinfos : "
//                                + " date naissance :  " + params[0] + " " + params[1] + " " + params[2]
//
//                        );
                        dictPersonalInfo.put("date_naissance", params[0] + "/" + params[1] + "/" + params[2]);
                    }
                });
        // Parrain
        // N° et rue

        MyHelper.patternRunThrough(htmlContent
                , "<input name=\"adresse2\" type=\"text\" id=\"adresse2\"  class=\"input300\" value=\"([^\"]*)\" /><br>"
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
//                        System.out.println ("Mesinfos : "
//                                + " adresse : " + params[0]
//
//                        );
                        dictPersonalInfo.put("adresse", params[0]);
                    }
                });

        // Ville
        //
        MyHelper.patternRunThrough(htmlContent
                , "<option value=\"([0-9]{5}) ([^\"]*)\"  selected='selected'>([^<]*)</option>"
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
//                        System.out.println ("Mesinfos : "
//                                + " code postal : " + params[0]
//                                + " ville : " + params[1]
//
//                        );
                        dictPersonalInfo.put("code_postal", params[0]);
                        dictPersonalInfo.put("ville", params[1]);
                    }
                });
        // Téléphone
        MyHelper.patternRunThrough(htmlContent
                , "<input name=\"telephone\" type=\"text\" id=\"telephone\" class=\"input300\" value=\"([0-9 ]*)\" /><br>"
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
//                        System.out.println ("Mesinfos : "
//                                + " tel fixe : " + params[0]
//                        );
                        dictPersonalInfo.put("tel_fixe", params[0]);
                    }
                });
        // Portable
        MyHelper.patternRunThrough(htmlContent
                , "<input name=\"portable\" type=\"text\" id=\"portable\" class=\"input300\" value=\"([0-9 ]*)\" />"
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
//                        System.out.println ("Mesinfos : "
//                                + " tel portable : " + params[0]
//                        );
                        dictPersonalInfo.put("tel_portable", params[0]);
                    }

                });

        //System.out.println (dictPersonalInfo.toString());
        if (selReceiver != null) selReceiver.accept(ISelReceiver.Entity.SEL_PERSONNAL_INFO, MyHelper.dict2json(dictPersonalInfo));
    }




    public void getAccountInfo(boolean onlinemode) {
        String htmlContent = null;
        final Map<String, String> dictAccountInfo = new HashMap<>();
        if (onlinemode) {
            htmlContent = webClient.getSimpleWebPage("http://sel-des-deux-rives.org/catalogue/index.php?lien=mes_clous");
            FileHelper.getInstance().writeStringToFile(htmlContent, "Account.html");
        }else {
            htmlContent = FileHelper.getInstance().readStringFromFile("Account.html");
        }

        MyHelper.patternRunThrough(htmlContent
                , "<tr><td class=\"gris_petit\">([0-9]{2}) ([0-9]{2})</td><td class=\"td_gris\">([+-][0-9]+)</td><td class=\"td_gris\">([^<]*)</td><td class=\"td_gris\">([^<]*)"
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
//                        System.out.println ("Transaction : "
//                                + " jour " + params[0]
//                                + " mois " + params[1]
//                                + " valeur " + params[2]
//                                + " nature " + params[3]
//                                + " pseudo " + params[4].substring(1).replace(")","")
//                        );

                        Map<String, String> dictTransactionInfo = new HashMap<>();
                        dictTransactionInfo.put("jour", params[0]);
                        dictTransactionInfo.put("mois" , params[1]);
                        dictTransactionInfo.put("valeur" , params[2]);
                        dictTransactionInfo.put("nature" , params[3]);
                        dictTransactionInfo.put("pseudo" , params[4].substring(1).replace(")",""));
                        if (selReceiver != null) selReceiver.accept(ISelReceiver.Entity.SEL_TRANSACTION, MyHelper.dict2json(dictTransactionInfo));
                    }
                });

        MyHelper.patternRunThrough(htmlContent
                , "<p class=\"bordure\">Solde actuel => ([0-9]*)<br />"
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
//                        System.out.println ("Solde Actuel : " + params[0]
//
//                        );
                        dictAccountInfo.put("solde_actuel", params[0]);


                    }
                });

        MyHelper.patternRunThrough(htmlContent.replaceAll("&nbsp;","")
                , "<h3>>>> Ma dernière publication de clous</h3><strong>[ \t\n]*<p>(T[0-4])([0-9]{4}) solde : ([0-9]*) nombre d'échanges : ([0-9]+)</p>"
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
//                        System.out.println (
//                                "Derniere publication : " + params[0] + " " + params[1]
//                                        + " Solde : " + params[2]
//                                        + " Nombre d'echange : " + params[3]
//
//                        );
                        dictAccountInfo.put("solde",params[2]);
                        dictAccountInfo.put("nb_echange",params[3]);
                        dictAccountInfo.put("derniere_publication",params[0] + " " + params[1]);

                    }
                });
        if (selReceiver != null) selReceiver.accept(ISelReceiver.Entity.SEL_ACCOUNT_INFO, MyHelper.dict2json(dictAccountInfo));
    }



    public List<Announce> getAnonymousAnnounces(boolean onlinemode) {
        final List<Announce> ListeDesAnnonces = new ArrayList<Announce>();

        String htmlContent = null;


        if (onlinemode) {
            htmlContent = webClient.getFormWebPage("http://sel-des-deux-rives.org/webcatalogue-anonyme/"
                    , new ArrayList<String[]>() {{
                        add(new String[] {"annonce", "offres"});
                        add(new String[] {"lien", "offres"});
                        add(new String[] {"affich", "cat"});
                        add(new String[] {"lister", "ok"});
                    }});


            FileHelper.getInstance().writeStringToFile(htmlContent, "OffresAnnon.html");
        }else {
            htmlContent = FileHelper.getInstance().readStringFromFile("OffresAnnon.html");
        }
        MyHelper.patternRunThrough(htmlContent
                , "&nbsp;&nbsp; Annonce n° ([0-9]{1,5})</p></div><div style=' float:left;  width: 55%;   padding-left: 8px;\t  text-align: center;'><p style='\tborder-color: #003148;  border-style: outset; \tborder-width: 1px; text-align:left;padding:5px;'>([^<]*)</p></div>"
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
                        //System.out.println (params[1].trim() );
                        Announce nAnnonce = new Announce();
                        nAnnonce.setId(Integer.parseInt(params[0]));
                        nAnnonce.setDescription(params[1].trim());
                        nAnnonce.setDirection(Announce.Direction.OFFER);
                        ListeDesAnnonces.add(nAnnonce);
                    }
                });


        if (onlinemode) {
            // htmlContent = SelWebClient.getDemands();
            htmlContent = webClient.getFormWebPage("http://sel-des-deux-rives.org/webcatalogue-anonyme/"
                    , new ArrayList<String[]>() {{
                        add(new String[] {"annonce", "demandes"});
                        add(new String[] {"lien", "offres"});
                        add(new String[] {"affich", "cat"});
                        add(new String[] {"lister", "ok"});
                    }});

            FileHelper.getInstance().writeStringToFile(htmlContent, "DemandesAnnon.html");
        } else {
            htmlContent = FileHelper.getInstance().readStringFromFile("DemandesAnnon.html");
        }
        MyHelper.patternRunThrough(htmlContent
                , "&nbsp;&nbsp; Annonce n° ([0-9]{1,5})</p></div><div style=' float:left;  width: 55%;   padding-left: 8px;\t  text-align: center;'><p style='\tborder-color: #003148;  border-style: outset; \tborder-width: 1px; text-align:left;padding:5px;'>([^<]*)</p></div>"
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
                        //System.out.println (params[0] + " " + params[1] );
                        //System.out.println (params[1].trim() );
                        Announce nAnnonce = new Announce();
                        nAnnonce.setId(Integer.parseInt(params[0]));
                        nAnnonce.setDescription(params[1].trim());
                        nAnnonce.setDirection(Announce.Direction.DEMAND);
                        ListeDesAnnonces.add(nAnnonce);


                    }
                });

        for (final Announce.Category cat: Announce.Category.values()){
//            System.out.println ("forum_category : " + cat.name());
            if (onlinemode) {
                //htmlContent = SelWebClient.getDemands(cat);
                htmlContent = webClient.getFormWebPage("http://sel-des-deux-rives.org/webcatalogue-anonyme/"
                        , new ArrayList<String[]>() {{
                            add(new String[]{"annonce", "demandes"});
                            add(new String[]{"rubrique", cat.name()});
                            add(new String[]{"affich", "cat"});
                        }});
                FileHelper.getInstance().writeStringToFile(htmlContent, "DemandesAnnon_" + cat.name() + ".html");
            } else {
                htmlContent = FileHelper.getInstance().readStringFromFile("DemandesAnnon_" + cat.name() + ".html");
            }
            MyHelper.patternRunThrough(htmlContent
                    , "&nbsp;&nbsp; Annonce n° ([0-9]{1,5})</p></div><div style=' float:left;  width: 55%;   padding-left: 8px;\t  text-align: center;'><p style='\tborder-color: #003148;  border-style: outset; \tborder-width: 1px; text-align:left;padding:5px;'>([^<]*)</p></div>"
                    , new MyHelper.EntryHandler() {
                        @Override
                        public void processEntry(String[] params) {
                            //System.out.println(params[0] + " " + params[1]);

                            for (Announce ann : ListeDesAnnonces) {
                                if (ann != null
                                        && ann.getId() == Integer.parseInt(params[0])
                                        && ann.getDescription().equals(params[1])
                                        && ann.getDirection() == Announce.Direction.DEMAND
                                        ) {
//                                        System.out.println("Category changed !!");
                                    ann.setCategory(cat);
                                }
                            }

                        }
                    });

        }
        for (final Announce.Category cat : Announce.Category.values()) {
//            System.out.println("forum_category : " + cat.name());
            if (onlinemode) {
                //htmlContent = SelWebClient.getOffers(cat);
                htmlContent = webClient.getFormWebPage("http://sel-des-deux-rives.org/webcatalogue-anonyme/"
                        , new ArrayList<String[]>() {{
                            add(new String[]{"annonce", "offres"});
                            add(new String[]{"rubrique", cat.name()});
                            add(new String[]{"affich", "cat"});
                        }});
                FileHelper.getInstance().writeStringToFile(htmlContent, "OffresAnnon_" + cat.name() + ".html");
            } else {
                htmlContent = FileHelper.getInstance().readStringFromFile("OffresAnnon_" + cat.name() + ".html");
            }
            MyHelper.patternRunThrough(htmlContent
                    , "&nbsp;&nbsp; Annonce n° ([0-9]{1,5})</p></div><div style=' float:left;  width: 55%;   padding-left: 8px;\t  text-align: center;'><p style='\tborder-color: #003148;  border-style: outset; \tborder-width: 1px; text-align:left;padding:5px;'>([^<]*)</p></div>"
                    , new MyHelper.EntryHandler() {
                        @Override
                        public void processEntry(String[] params) {
//                            System.out.println(params[0] + " " + params[1]);

                            for (Announce ann : ListeDesAnnonces) {
                                if (ann != null
                                        && ann.getId() == Integer.parseInt(params[0])
                                        && ann.getDescription().equals(params[1])
                                        && ann.getDirection() == Announce.Direction.OFFER
                                        ) {
//                                    System.out.println("Category changed !!");
                                    ann.setCategory(cat);
                                }
                            }

                        }
                    });

        }
        for (Announce ann: ListeDesAnnonces) {
            Map<String, String> dictAnnounceInfo = new HashMap<>();
            dictAnnounceInfo.put("id", String.format("%d",ann.getId()));
            dictAnnounceInfo.put("category", (ann.getCategory() != null)?ann.getCategory().toString(): "null");
            dictAnnounceInfo.put("description", ann.getDescription());
            dictAnnounceInfo.put("direction", ann.getDirection().toString());
            if (selReceiver != null) selReceiver.accept(ISelReceiver.Entity.SEL_ANONYMOUS_ANNOUNCE, MyHelper.dict2json(dictAnnounceInfo));
        }
        return ListeDesAnnonces;
    }

    public List<Person> getAnnuaire(boolean onlinemode) {

        final List<Person> ListeDesPersonnes = new ArrayList<Person>();
        String htmlContent = null;
        if (onlinemode) {
            final String login = this.login;
            htmlContent = webClient.getFormWebPage("http://sel-des-deux-rives.org/catalogue/index.php?lien=annuaire"
                    , new ArrayList<String[]>() {{
                        add(new String[] {"qui", login});
                    }});
            FileHelper.getInstance().writeStringToFile(htmlContent, "Annuaire.html");
        }else {
            htmlContent = FileHelper.getInstance().readStringFromFile("Annuaire.html");
        }
        MyHelper.patternRunThrough(htmlContent
                , "<option value=\"([A-Za-z]+[0-9]{1,3})\" >([0-9]{1,3}) ([^<]*)</option>"
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
                        //System.out.println ("a person has just been met : " + params[0] + " " + params[1] );

                        String[]tokens = params[0].split("<br />");
                        Person nPersonne = new Person();
                        nPersonne.setPseudo(params[0].trim());
//                        nPersonne.setId(Integer.parseInt(params[0]));
//                        nPersonne.setDescription(params[1].trim());
//                        nPersonne.setDirection(Announce.Direction.OFFER);
                        ListeDesPersonnes.add(nPersonne);

                    }
                });


        for (final Person pers : ListeDesPersonnes){

            final String pseudo = pers.getPseudo();
            htmlContent = null;
            if (onlinemode) {
//                System.out.println("Retreiving page of "+pseudo);
                htmlContent = webClient.getFormWebPage("http://sel-des-deux-rives.org/catalogue/index.php?lien=annuaire"
                        , new ArrayList<String[]>() {{
                            add(new String[] {"qui", pseudo});
                        }});
                FileHelper.getInstance().writeStringToFile(htmlContent, "Fiche_"+pseudo+".html");
            }else {
                htmlContent = FileHelper.getInstance().readStringFromFile("Fiche_"+pseudo+".html");
            }
            MyHelper.patternRunThrough(htmlContent
                    , "<p class=\\\"texte_fiche\\\">([^0-9]*[0-9]{1,3})<br />(.*)</p>"
                    , new MyHelper.EntryHandler() {
                        @Override
                        public void processEntry(String[] params) {
                            //System.out.println (pseudo + " info are : " + params[0] + "|" + params[1].replaceAll("&nbsp;","") );
                            Pattern patId = Pattern.compile("(.*) ([0-9]{1,4})");
                            Matcher matcher = patId.matcher(params[0].trim());
                            if (matcher.find()) {
                                pers.setName(matcher.group(1));
                            }
                            String fields[] = params[1].replaceAll("&nbsp;","").split("<br />");
                            if (fields.length > 1) {
                                pers.setAddress(fields[0] + "\n" + fields[1]);
                            }
//                            System.out.println (" name: " + pers.getName() + " adresse: " +  pers.getAddress()  );
                            // TODO Remplir pers avec le contenu de params[1].replaceAll("&nbsp;","")

                            List<String> phoneList = findPhoneNumbers(params[1].replaceAll("&nbsp;",""));
                            for (String phon : phoneList) {
                                if(phon.startsWith("06") || phon.startsWith("07") ){
                                    pers.setPhone2(phon);
                                } else {
                                    pers.setPhone1(phon);
                                }
                            }

                        }
                    });
            //<br /> Solde : <br />
            //<p class="grasitalic">Dernière publication : </p>
//<p class="grasitalic">Dernière publication : </p>
//<p class="texte_fiche">
//T3 2017  <br /> Solde : 245<br /> Nombres d'échanges : 0
// </p>
            MyHelper.patternRunThrough(htmlContent.replaceAll("&nbsp;","")
                    , "<br /> Solde : ([0-9]+)<br />"
                    , new MyHelper.EntryHandler() {
                        @Override
                        public void processEntry(String[] params) {
                            if (params.length > 0){
                                pers.setSolde(Integer.parseInt(params[0]));
                            }
                        }
                    });
//<br /> Nombres d'échanges : 0
            MyHelper.patternRunThrough(htmlContent.replaceAll("&nbsp;","")
                    , "<br /> Nombres d'échanges : ([0-9]+)"
                    , new MyHelper.EntryHandler() {
                        @Override
                        public void processEntry(String[] params) {
                            if (params.length > 0){
                                pers.setNumberOfExchange(Integer.parseInt(params[0]));
                            }
                        }
                    });

            MyHelper.patternRunThrough(htmlContent.replaceAll("&nbsp;","")
                    , "<p class=\"grasitalic\">Dernière publication : </p>[\n \t]*<p class=\"texte_fiche\">[\n \t]*(T[0-9] [0-9]{4})"
                    , new MyHelper.EntryHandler() {
                        @Override
                        public void processEntry(String[] params) {
                            if (params.length > 0){
                                pers.setLastPublish(params[0]);
                            }
                        }
                    });

//            MyHelper.patternRunThrough(htmlContent.replaceAll("&nbsp;","")
//                    , "****************************"
//                    , new MyHelper.EntryHandler() {
//                        @Override
//                        public void processEntry(String[] params) {
//
//                        }
//                    });


        }


        for (Person pers: ListeDesPersonnes) {
            Map<String, String> dictPersonInfo = new HashMap<>();
            dictPersonInfo.put("pseudo", pers.getPseudo());
            dictPersonInfo.put("name", pers.getName());
            dictPersonInfo.put("address", pers.getAddress());
            dictPersonInfo.put("last_publish", pers.getLastPublish());
            dictPersonInfo.put("phone1", pers.getPhone1());
            dictPersonInfo.put("phone2", pers.getPhone2());
            dictPersonInfo.put("numberOfExchange", String.format("%d",pers.getNumberOfExchange()));
            dictPersonInfo.put("solde", String.format("%d",pers.getSolde()));

            if (selReceiver != null) selReceiver.accept(ISelReceiver.Entity.SEL_PERSON, MyHelper.dict2json(dictPersonInfo));
        }
        return ListeDesPersonnes;
    }
    public void getForums(boolean onlinemode) {
        String htmlContent = null;


        String listChoix [] = {
                "Annonces hors clous"
                ,"Bons plans"
                ,"Achats groupés"
//                TODO,"Coups de gueule/Coups de coeur"
                ,"Vos recettes "
                ,"Idées sorties"
                ,"Sortir ensemble"
                ,"Vos associations"
                ,"Humour"
                ,"Autres"

        };

        for (final String forum_name :listChoix ) {

//            System.out.println ("Forum : " + forum_name);

            String url = "http://sel-des-deux-rives.org/catalogue/index.php?lien=forum&choix="+forum_name.replaceAll(" ","%20");
            if (onlinemode) {
                htmlContent = webClient.getSimpleWebPage(url);
//            htmlContent = webClient.getFormWebPage("http://sel-des-deux-rives.org/catalogue/index.php"
//                    , new ArrayList<String[]>() {{
//                        add(new String[] {"lien", "forum"});
//                        add(new String[] {"choix", "Bons plans"});
//                    }});
                FileHelper.getInstance().writeStringToFile(htmlContent, "Forums"+forum_name.replaceAll(" ","_")+"_.html");
            } else {
                htmlContent = FileHelper.getInstance().readStringFromFile("Forums"+forum_name.replaceAll(" ","_")+"_.html");
            }

            //JSONObject obj = new JSONObject();
            class lEntry{
                String id;
                String forum_name;
                String urlpart;
            }
            final List<lEntry> listUrlPart = new ArrayList<>();
            MyHelper.patternRunThrough(htmlContent.replaceAll("&nbsp;", "")
                    , "<a href=\"index.php[?]lien=forum&amp;choisir=2&amp;titre_convers=([^&]*)&amp;choix=[^&]+&amp;id_convers=([0-9]+)&amp;pour_liste=\"><" //=([^&]*)&amp;
                    , new MyHelper.EntryHandler() {
                        @Override
                        public void processEntry(String[] params) {
//                            System.out.println("Discussion : "
//                                            + " id: " + params[1]
//                                            + "  Titre: " + params[0]
////                                    + " text: " + params[2]
//                            );
                            Map<String,String> dictDiscussionInfo = new HashMap<>();
                            dictDiscussionInfo.put("forum_name", forum_name);
                            dictDiscussionInfo.put("id_discussion", params[1]);
                            dictDiscussionInfo.put("title", params[0]);
                            lEntry le = new lEntry ();
                            le.id = params[1];
                            le.forum_name = forum_name;
                            //String forum_name = "Bons%20plans";
                            String UrlPart = "choix=" + forum_name.replaceAll(" ","%20") + "&id_convers=" + params[1].trim();
                            le.urlpart = UrlPart;
                            listUrlPart.add(le);
                            if (selReceiver != null) selReceiver.accept(ISelReceiver.Entity.SEL_DISCUSSION_FORUM, MyHelper.dict2json(dictDiscussionInfo));
                            //jsForumEntry = new JSONObject();
                        }
                    });


            for (final lEntry  urlpart: listUrlPart) {
                if (onlinemode) {
                    String pageAdress = "http://sel-des-deux-rives.org/catalogue/index.php?lien=forum&choisir=2&" + urlpart.urlpart + "&pour_liste=";
                    htmlContent = webClient.getSimpleWebPage(pageAdress);
                    FileHelper.getInstance().writeStringToFile(htmlContent, "Discussion_"+urlpart.id+".html");
                } else {
                    htmlContent = FileHelper.getInstance().readStringFromFile("Discussion_"+urlpart.id+".html");
                }
                MyHelper.patternRunThrough(htmlContent.replaceAll("&nbsp;", "")
                        , "<p class=\"forum_2\"><span class=\"grasitalic\">([^0-9]+[0-9]{1,4}) ([0-9]{2})-([0-9]{2})-([0-9]{4})</span></p><p class=\"bordureforum\">([^ù]*)</p>"
                        , new MyHelper.EntryHandler() {
                            @Override
                            public void processEntry(String[] params) {
//                                System.out.println("Message : "
//                                                + " pseudo: " + params[0]
//                                                + " date: " + params[1]
//                                                + " / " + params[2]
//                                                + " / " + params[3]
//                                    + " message : " + params[4].split("</p>")[0].replace("<br />","\n")
//                                    + " text: " + params[2]
//                                );
                                Map<String,String> dictMessageInfo = new HashMap<>();
                                dictMessageInfo.put("pseudo", params[0]);
                                dictMessageInfo.put("forum", urlpart.forum_name);
                                dictMessageInfo.put("discussion_id", urlpart.id);
                                dictMessageInfo.put("date", params[1]
                                        + " / " + params[2]
                                        + " / " + params[3]);
                                dictMessageInfo.put("text", params[4].split("</p>")[0].replace("<br />","\n"));
                                if (selReceiver != null) selReceiver.accept(ISelReceiver.Entity.SEL_MESSAGE_FORUM, MyHelper.dict2json(dictMessageInfo));
                            }
                        });


            }

            //jsForumEntry.put("")
        }

        //debugDiscussion();

    }

    private void debugDiscussion() {
        String htmlContent;
        htmlContent = FileHelper.getInstance().readStringFromFile("Discussion_1126.html");
        MyHelper.patternRunThrough(htmlContent.replaceAll("&nbsp;", "")
                , "<p class=\"forum_2\"><span class=\"grasitalic\">([^0-9]+[0-9]{1,4}) ([0-9]{2})-([0-9]{2})-([0-9]{4})</span></p><p class=\"bordureforum\">([^ù]*)</p>"
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
                        System.out.println("Message : "
                                        + " pseudo: " + params[0]                                       + " date: " + params[1]
                                        + " / " + params[2]
                                        + " / " + params[3]
                                        + " message : " + params[4].split("</p>")[0].replace("<br />","")
//                                    + " text: " + params[2]
                        );
                    }
                });
    }

    private void getMyAnnounces(boolean onlinemode) {
        String htmlContent = null;
        if (onlinemode) {
            htmlContent = webClient.getSimpleWebPage("http://sel-des-deux-rives.org/catalogue/index.php?lien=mes_annonces");
            FileHelper.getInstance().writeStringToFile(htmlContent, "MyAnnounces.html");
        }else {
            htmlContent = FileHelper.getInstance().readStringFromFile("MyAnnounces.html");
        }


        MyHelper.patternRunThrough(htmlContent
                , "lien=mes_annonces&amp;action=supprimer_annonce&amp;quoi=([a-z]*)&amp;id=([0-9]+)&amp;annonce=([^\"]*)\""
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
                        System.out.println ("MyAnnounce : "
                                + " type: " + params[0]
                                + " id: " + params[1]
                                + " text: " + params[2]
                        );



                    }
                });

    }

    public static List<String> findPhoneNumbers(String s) {
        final List<String> listOfPhoneNumber = new ArrayList<>();
        MyHelper.patternRunThrough(s
                , "([0-9]{2})[. -]*([0-9]{2})[. -]*([0-9]{2})[. -]*([0-9]{2})[. -]*([0-9]{2})"
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
                        String phoneNumber = params [0] + params [1] + params [2] + params [3] + params [4] ;
                        listOfPhoneNumber.add(phoneNumber);
                    }
                });
        return listOfPhoneNumber;
    }

    public void  getAnnounces(boolean onlinemode) {
        String htmlContent = null;

        for (final Announce.Category cat : Announce.Category.values()) {


            if (onlinemode) {
                //htmlContent = SelWebClient.getDemands(cat);
                htmlContent = webClient.getFormWebPage("http://sel-des-deux-rives.org/catalogue/index.php"
                        , new ArrayList<String[]>() {{
                            add(new String[]{"lien", "demandes"});
                            add(new String[]{"rubrique", cat.toString()});
                            add(new String[]{"annonce", "demandes"});
                            add(new String[]{"affich", "cat"});
                        }});
                FileHelper.getInstance().writeStringToFile(htmlContent, "Demandes_" + cat.name() + ".html");
            } else {
                htmlContent = FileHelper.getInstance().readStringFromFile("Demandes_" + cat.name() + ".html");
            }
            MyHelper.patternRunThrough(htmlContent.replaceAll("&nbsp;", "")
                    , "<span class=\"petit\">(([0-9]{2})-([0-9]{2})-([0-9]{4}))</span>([^0-9]+[0-9]{1,4})<a href=\"index.php[?]lien=demandes&amp;qui=[^0-9]+[0-9]{1,4}&amp;texte_annonce=([^\"]*)\" target"
                    , new MyHelper.EntryHandler() {
                        @Override
                        public void processEntry(String[] params) {
//                            System.out.println(params[3] + " " + params[4]
//                                   + " " + cat.name()
//                            );

                            Map<String, String> dictAnnounceInfo = new HashMap<>();
                            dictAnnounceInfo.put("description", params[5].trim());
                            dictAnnounceInfo.put("date", params[3].trim());
                            dictAnnounceInfo.put("category", cat.name());
                            dictAnnounceInfo.put("direction", Announce.Direction.DEMAND.name());
                            dictAnnounceInfo.put("owner_pseudo", params[4].trim());
                            if (selReceiver != null) selReceiver.accept(ISelReceiver.Entity.SEL_ANNOUNCE, MyHelper.dict2json(dictAnnounceInfo));


                        }
                    });
            MyHelper.patternRunThrough(htmlContent.replaceAll("&nbsp;", "")
                    , "<span class=\"petit\"></span>([^0-9]+[0-9]{1,4})<a href=\"index.php[?]lien=demandes&amp;qui=Jacqueline208&amp;texte_annonce=([^\"]*)\" target="
                    , new MyHelper.EntryHandler() {
                        @Override
                        public void processEntry(String[] params) {
//                            System.out.println(params[3] + " " + params[4]
//                                   + " " + cat.name()
//                            );

                            Map<String, String> dictAnnounceInfo = new HashMap<>();
                            dictAnnounceInfo.put("description", params[1].trim());
                            //dictAnnounceInfo.put("date", params[3].trim());
                            dictAnnounceInfo.put("category", cat.name());
                            dictAnnounceInfo.put("direction", Announce.Direction.DEMAND.name());
                            dictAnnounceInfo.put("owner_pseudo", params[0].trim());
                            if (selReceiver != null) selReceiver.accept(ISelReceiver.Entity.SEL_ANNOUNCE, MyHelper.dict2json(dictAnnounceInfo));


                        }
                    });

            if (onlinemode) {
                //htmlContent = SelWebClient.getDemands(cat);
                htmlContent = webClient.getFormWebPage("http://sel-des-deux-rives.org/catalogue/index.php"
                        , new ArrayList<String[]>() {{
                            add(new String[]{"lien", "offres"});
                            add(new String[]{"rubrique", cat.toString()});
                            add(new String[]{"annonce", "offres"});
                            add(new String[]{"affich", "cat"});
                        }});
                FileHelper.getInstance().writeStringToFile(htmlContent, "Offres_" + cat.name() + ".html");
            } else {
                htmlContent = FileHelper.getInstance().readStringFromFile("Offres_" + cat.name() + ".html");
            }
            MyHelper.patternRunThrough(htmlContent.replaceAll("&nbsp;", "")
                    , "<span class=\"petit\">(([0-9]{2})-([0-9]{2})-([0-9]{4}))</span>([^0-9]+[0-9]{1,4})<a href=\"index.php[?]lien=offres&amp;qui=[^0-9]+[0-9]{1,4}&amp;texte_annonce=([^\"]*)\" target"
                    , new MyHelper.EntryHandler() {
                        @Override
                        public void processEntry(String[] params) {
//                            System.out.println(params[3] + " " + params[4]
//                                    + " " + cat.name()
//                            );

                            Map<String, String> dictAnnounceInfo = new HashMap<>();
                            dictAnnounceInfo.put("description", params[5].trim());
                            dictAnnounceInfo.put("date", params[3].trim());
                            dictAnnounceInfo.put("category", cat.name());
                            dictAnnounceInfo.put("direction", Announce.Direction.OFFER.name());
                            dictAnnounceInfo.put("owner_pseudo", params[4].trim());
                            if (selReceiver != null) selReceiver.accept(ISelReceiver.Entity.SEL_ANNOUNCE, MyHelper.dict2json(dictAnnounceInfo));


                        }
                    });
            MyHelper.patternRunThrough(htmlContent.replaceAll("&nbsp;", "")
                    , "<span class=\"petit\"></span>([^0-9]+[0-9]{1,4})<a href=\"index.php[?]lien=offres&amp;qui=Jacqueline208&amp;texte_annonce=([^\"]*)\" target="
                    , new MyHelper.EntryHandler() {
                        @Override
                        public void processEntry(String[] params) {
//                            System.out.println(params[0] + " " + params[1]
//                                   + " " + cat.name()
//                            );

                            Map<String, String> dictAnnounceInfo = new HashMap<>();
                            dictAnnounceInfo.put("description", params[1].trim());
                            dictAnnounceInfo.put("category", cat.name());
                            dictAnnounceInfo.put("direction", Announce.Direction.OFFER.name());
                            dictAnnounceInfo.put("owner_pseudo", params[0].trim());
                            if (selReceiver != null) selReceiver.accept(ISelReceiver.Entity.SEL_ANNOUNCE, MyHelper.dict2json(dictAnnounceInfo));



                        }
                    });


        }


    }


    public void unanonimizeAnnounces ( boolean onlinemode) {
        String htmlContent = null;
        //for (final Announce.Category cat : Announce.Category.values()) {

        if (onlinemode) {
            htmlContent = webClient.getFormWebPage("http://sel-des-deux-rives.org/catalogue/index.php"
                    , new ArrayList<String[]>() {{
                        add(new String[]{"lister", "lister"});
                        add(new String[]{"lien", "demandes"});
                        add(new String[]{"annonce", "demandes"});
                        add(new String[]{"affich", "cat"});
                    }});
            FileHelper.getInstance().writeStringToFile(htmlContent, "Demandes.html");
        } else {
            htmlContent = FileHelper.getInstance().readStringFromFile("Demandes.html");
        }

        MyHelper.patternRunThrough(htmlContent
                ,"<span class=\"petit\">(([0-9]{2})-([0-9]{2})-([0-9]{4}))</span>&nbsp;&nbsp;([A-Za-z]+[0-9]{1,3})&nbsp;&nbsp;<a href=\"index.php.lien=demandes&amp;qui=[A-Za-z]+[0-9]{1,3}&amp;texte_annonce=([^\"]*)\" target="
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
                        //System.out.println("the announce to process : " + params[0] + " " + params[4]+ " " + params[5].trim());
                        Map<String, String> dictAnnounceInfo = new HashMap<>();
                        dictAnnounceInfo.put("date", params[3].trim());
                        dictAnnounceInfo.put("description", params[5].trim());
                        dictAnnounceInfo.put("direction", Announce.Direction.DEMAND.name());
                        dictAnnounceInfo.put("owner_pseudo", params[4].trim());
                        if (selReceiver != null) selReceiver.accept(ISelReceiver.Entity.SEL_ANNOUNCE, MyHelper.dict2json(dictAnnounceInfo));


                    }
                });
        MyHelper.patternRunThrough(htmlContent
                ,"<span class=\"petit\"></span>&nbsp;&nbsp;([A-Za-z]+[0-9]{1,3})&nbsp;&nbsp;<a href=\"index.php.lien=demandes&amp;qui=[A-Za-z]+[0-9]{1,3}&amp;texte_annonce=([^\"]*)\" target="
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
                        //System.out.println("the announce to process : " + params[0] + " " + params[4]+ " " + params[5].trim());
                        Map<String, String> dictAnnounceInfo = new HashMap<>();
                        dictAnnounceInfo.put("description", params[1].trim());
                        dictAnnounceInfo.put("direction", Announce.Direction.DEMAND.name());
                        dictAnnounceInfo.put("owner_pseudo", params[0].trim());
                        if (selReceiver != null) selReceiver.accept(ISelReceiver.Entity.SEL_ANNOUNCE, MyHelper.dict2json(dictAnnounceInfo));

                    }
                });
        //}
        if (onlinemode) {
            htmlContent = webClient.getFormWebPage("http://sel-des-deux-rives.org/catalogue/index.php"
                    , new ArrayList<String[]>() {{
                        add(new String[]{"lister", "lister"});
                        add(new String[]{"lien", "offres"});
                        add(new String[]{"annonce", "offres"});
                        add(new String[]{"affich", "cat"});
                    }});
            FileHelper.getInstance().writeStringToFile(htmlContent, "Offres.html");
        } else {
            htmlContent = FileHelper.getInstance().readStringFromFile("Offres.html");
        }

        MyHelper.patternRunThrough(htmlContent
                ,"<span class=\"petit\">(([0-9]{2})-([0-9]{2})-([0-9]{4}))</span>&nbsp;&nbsp;([A-Za-z]+[0-9]{1,3})&nbsp;&nbsp;<a href=\"index.php.lien=offres&amp;qui=[A-Za-z]+[0-9]{1,3}&amp;texte_annonce=([^\"]*)\" target="
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
                        //System.out.println("the announce to process : " + params[0] + " " + params[4]+ " " + params[5].trim());
                        Map<String, String> dictAnnounceInfo = new HashMap<>();
                        dictAnnounceInfo.put("date", params[3].trim());
                        dictAnnounceInfo.put("description", params[5].trim());
                        dictAnnounceInfo.put("direction", Announce.Direction.OFFER.name());
                        dictAnnounceInfo.put("owner_pseudo", params[4].trim());
                        if (selReceiver != null) selReceiver.accept(ISelReceiver.Entity.SEL_ANNOUNCE, MyHelper.dict2json(dictAnnounceInfo));

                    }
                });
        MyHelper.patternRunThrough(htmlContent
                ,"<span class=\"petit\"></span>&nbsp;&nbsp;([A-Za-z]+[0-9]{1,3})&nbsp;&nbsp;<a href=\"index.php.lien=offres&amp;qui=[A-Za-z]+[0-9]{1,3}&amp;texte_annonce=([^\"]*)\" target="
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
                        //System.out.println("the announce to process : " + params[0] + " " + params[4]+ " " + params[5].trim());
                        Map<String, String> dictAnnounceInfo = new HashMap<>();
                        dictAnnounceInfo.put("description", params[1].trim());
                        dictAnnounceInfo.put("direction", Announce.Direction.OFFER.name());
                        dictAnnounceInfo.put("owner_pseudo", params[0].trim());
                        if (selReceiver != null) selReceiver.accept(ISelReceiver.Entity.SEL_ANNOUNCE, MyHelper.dict2json(dictAnnounceInfo));

                    }
                });
//        for (Announce ann: lAnnonces) {
//            Map<String, String> dictAnnounceInfo = new HashMap<>();
//            dictAnnounceInfo.put("id", String.format("%d",ann.getId()));
//            dictAnnounceInfo.put("category", (ann.getCategory() != null)?ann.getCategory().toString(): "null");
//            dictAnnounceInfo.put("description", ann.getDescription());
//            dictAnnounceInfo.put("direction", ann.getDirection().toString());
//            dictAnnounceInfo.put("owner_pseudo", ann.getOwnerPseudo());
//            if (selReceiver != null) selReceiver.accept(ISelReceiver.Entity.SEL_ANNOUNCE, MyHelper.dict2json(dictAnnounceInfo));
//        }

    }
    public void unanonimizeAnnounces (final List<Announce> lAnnonces, boolean onlinemode){
        String htmlContent = null;
        //for (final Announce.Category cat : Announce.Category.values()) {

        if (onlinemode) {
            htmlContent = webClient.getFormWebPage("http://sel-des-deux-rives.org/catalogue/index.php"
                    , new ArrayList<String[]>() {{
                        add(new String[]{"lister", "lister"});
                        add(new String[]{"lien", "demandes"});
                        add(new String[]{"annonce", "demandes"});
                        add(new String[]{"affich", "cat"});
                    }});
            FileHelper.getInstance().writeStringToFile(htmlContent, "Demandes.html");
        } else {
            htmlContent = FileHelper.getInstance().readStringFromFile("Demandes.html");
        }

        MyHelper.patternRunThrough(htmlContent
                ,"<span class=\"petit\">(([0-9]{2})-([0-9]{2})-([0-9]{4}))</span>&nbsp;&nbsp;([A-Za-z]+[0-9]{1,3})&nbsp;&nbsp;<a href=\"index.php.lien=demandes&amp;qui=[A-Za-z]+[0-9]{1,3}&amp;texte_annonce=([^\"]*)\""
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
                        //System.out.println("the announce to process : " + params[0] + " " + params[4]+ " " + params[5].trim());
                        for (Announce ann : lAnnonces) {
                            if (ann.getDescription().trim().equals(params[5].trim())){
                                ann.setOwnerPseudo(params[4].trim());
                                // TODO Add date attribute
                                //System.out.println("Owner of dated demand " + ann.getId()+" is " + ann.getOwnerPseudo());
                            }
                        }

                    }
                });
        MyHelper.patternRunThrough(htmlContent
                ,"<span class=\"petit\"></span>&nbsp;&nbsp;([A-Za-z]+[0-9]{1,3})&nbsp;&nbsp;<a href=\"index.php.lien=demandes&amp;qui=[A-Za-z]+[0-9]{1,3}&amp;texte_annonce=([^\"]*)\""
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
                        //System.out.println("the announce to process : " + params[0] + " " + params[4]+ " " + params[5].trim());
                        for (Announce ann : lAnnonces) {
                            if (ann.getDescription().trim().equals(params[2].trim())){
                                ann.setOwnerPseudo(params[1].trim());
                                // TODO Add date attribute
                                //System.out.println("Owner of undated demand " + ann.getId()+" is " + ann.getOwnerPseudo());
                            }
                        }

                    }
                });
        //}
        if (onlinemode) {
            htmlContent = webClient.getFormWebPage("http://sel-des-deux-rives.org/catalogue/index.php"
                    , new ArrayList<String[]>() {{
                        add(new String[]{"lister", "lister"});
                        add(new String[]{"lien", "offres"});
                        add(new String[]{"annonce", "offres"});
                        add(new String[]{"affich", "cat"});
                    }});
            FileHelper.getInstance().writeStringToFile(htmlContent, "Offres.html");
        } else {
            htmlContent = FileHelper.getInstance().readStringFromFile("Offres.html");
        }

        MyHelper.patternRunThrough(htmlContent
                ,"<span class=\"petit\">(([0-9]{2})-([0-9]{2})-([0-9]{4}))</span>&nbsp;&nbsp;([A-Za-z]+[0-9]{1,3})&nbsp;&nbsp;<a href=\"index.php.lien=offres&amp;qui=[A-Za-z]+[0-9]{1,3}&amp;texte_annonce=([^\"]*)\""
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
                        //System.out.println("the announce to process : " + params[0] + " " + params[4]+ " " + params[5].trim());
                        for (Announce ann : lAnnonces) {
                            if (ann.getDescription().trim().equals(params[5].trim())){
                                ann.setOwnerPseudo(params[4].trim());
                                // TODO Add date attribute
                                //System.out.println("Owner of dated offer " + ann.getId()+" is " + ann.getOwnerPseudo());
                            }
                        }

                    }
                });
        MyHelper.patternRunThrough(htmlContent
                ,"<span class=\"petit\"></span>&nbsp;&nbsp;([A-Za-z]+[0-9]{1,3})&nbsp;&nbsp;<a href=\"index.php.lien=offres&amp;qui=[A-Za-z]+[0-9]{1,3}&amp;texte_annonce=([^\"]*)\""
                , new MyHelper.EntryHandler() {
                    @Override
                    public void processEntry(String[] params) {
                        //System.out.println("the announce to process : " + params[0] + " " + params[4]+ " " + params[5].trim());
                        for (Announce ann : lAnnonces) {
                            if (ann.getDescription().trim().equals(params[1].trim())){
                                ann.setOwnerPseudo(params[0].trim());
                                // TODO Add date attribute
                                //System.out.println("Owner of undated offer " + ann.getId()+" is " + ann.getOwnerPseudo());
                            }
                        }

                    }
                });
        for (Announce ann: lAnnonces) {
            Map<String, String> dictAnnounceInfo = new HashMap<>();
            dictAnnounceInfo.put("id", String.format("%d",ann.getId()));
            dictAnnounceInfo.put("category", (ann.getCategory() != null)?ann.getCategory().toString(): "null");
            dictAnnounceInfo.put("description", ann.getDescription());
            dictAnnounceInfo.put("direction", ann.getDirection().toString());
            dictAnnounceInfo.put("owner_pseudo", ann.getOwnerPseudo());
            if (selReceiver != null) selReceiver.accept(ISelReceiver.Entity.SEL_ANNOUNCE, MyHelper.dict2json(dictAnnounceInfo));
        }

    }

    public boolean publishUrgentAnnounce(   ){
        boolean result = false;
        String htmlContent = webClient.getFormWebPage("http://sel-des-deux-rives.org/catalogue/index.php"
                , new ArrayList<String[]>() {{
                    add(new String[]{"lien", "mes_annonces_urgentes"});
                    add(new String[]{"annonce",""}); //
                    add(new String[]{"monTEL", "Tel : 04 75 05 93 35 - 06 45 84 78 74"}); //
                    add(new String[]{"jour_butoir","31"}); //
                    add(new String[]{"mois_butoir","10"}); //
                    add(new String[]{"annee_butoir","2017"}); //
                    add(new String[]{"quel_choix","entrer_annonce"}); //
                    add(new String[]{"bouton","Enregister"}); //

                }});
        //System.out.println(htmlContent);
        FileHelper.getInstance().writeStringToFile(htmlContent, "Result.html");
        return result;
    }
    public boolean sendContactMessage(final String name, final String mail, final String subject, final String text) {
        boolean result = false;
        String htmlContent = webClient.getFormWebPage("http://sel-des-deux-rives.org/contact/#wpcf7-f93-p27-o1"
                , new ArrayList<String[]>() {{
                    add(new String[] {"_wpcf7", "offres"});
                    add(new String[] {"_wpcf7_version", "4.9"});
                    add(new String[] {"_wpcf7_locale", "fr_FR"});
                    add(new String[] {"_wpcf7_unit_tag", "wpcf7-f93-p27-o1"});
                    add(new String[] {"_wpcf7_container_post", "27"});
                    add(new String[] {"your-name", name});
                    add(new String[] {"your-email", mail});
                    add(new String[] {"your-subject", subject});
                    add(new String[] {"your-message", text});
                }});
        System.out.println(htmlContent);
        FileHelper.getInstance().writeStringToFile(htmlContent, "ResultSendMessage.html");
        return result;
    }
    public boolean publishAnnounce(   ){
        boolean result = false;
        String htmlContent = webClient.getFormWebPage("http://sel-des-deux-rives.org/catalogue/index.php"
                , new ArrayList<String[]>() {{
                    add(new String[]{"lien", "mes_annonces"});
                    add(new String[]{"rubrique", "Alimentation"}); // "Actions collectives" "Aide à la personne" "Alimentation" "Atelier/Cours" "Autre objet" "Autre service" "Bricolage" "Couture"  "Covoiturage" "Hébergement" "Informatique (dépannage)" "Jardinage" "Jeux/Jouets" "Maison" "Matériel/Outillage" "Multimédia" "Vêtements et accessoires"
                    add(new String[]{"annonce", "Quelqu'un aurait des caquis ? "}); //
                    add(new String[]{"monTEL", "Tel : 04 75 05 93 35 - 06 45 84 78 74"}); //
                    add(new String[]{"quoi", "demandes"}); // "demandes" "offreperenes"
                    add(new String[]{"quel_choix", "entrer_annonce"}); //
                    add(new String[]{"bouton", "Enregister"}); //

                }});
        //System.out.println(htmlContent);
        FileHelper.getInstance().writeStringToFile(htmlContent, "Result.html");
        return result;
    }
    public boolean publishSold(   ){
        boolean result = false;
        String htmlContent = webClient.getFormWebPage("http://sel-des-deux-rives.org/catalogue/index.php"
                , new ArrayList<String[]>() {{
                    add(new String[]{"trimestre", "T4"});
                    add(new String[]{"annee", "2017"});
                    add(new String[]{"solde", "165"});
                    add(new String[]{"nb", "8"});
                    add(new String[]{"declarer", "ok"});
                    add(new String[]{"derniere_publ", ""});
                    add(new String[]{"memoireA", "2017"});
                    add(new String[]{"memoireT", "T3"});

                }});
        //System.out.println(htmlContent);
        FileHelper.getInstance().writeStringToFile(htmlContent, "Result.html");
        return result;
    }
    public boolean publishTansaction(   ){
        boolean result = false;
        String htmlContent = webClient.getFormWebPage("http://sel-des-deux-rives.org/catalogue/index.php"
                , new ArrayList<String[]>() {{
                    add(new String[]{"lien", "mes_clous"});
                    add(new String[]{"jour", "17"});
                    add(new String[]{"mois", "10"});
                    add(new String[]{"annee", "2017"});
                    add(new String[]{"nb", "45"});
                    add(new String[]{"nature_echange", "Informatique"});
                    add(new String[]{"qui", "Claire203"});
                    add(new String[]{"sens", "offre"});
                    add(new String[]{"mise_a_jour", "ok"});

                }});
        //System.out.println(htmlContent);
        FileHelper.getInstance().writeStringToFile(htmlContent, "Result.html");
        return result;
    }

    public boolean publishInfo(   ){
        boolean result = false;
        final String login = this.login;
        String htmlContent = webClient.getFormWebPage("http://sel-des-deux-rives.org/catalogue/index.php"
                , new ArrayList<String[]>() {{
                    add(new String[]{"mail", "jbperin@gmail.com"});
                    add(new String[]{"vocabl", "Mr"}); //"Mme""Mle" "Mr et Mme"
                    add(new String[]{"nom", "PERIN"});
                    add(new String[]{"prenom", "Jean Baptiste"});
                    add(new String[]{"date_naiss_jour", "30"});
                    add(new String[]{"date_naiss_mois", "09"});
                    add(new String[]{"date_naiss_annee", "1973"});
                    add(new String[]{"statut", "Particulier"}); //"Famille" "Organisme"
                    add(new String[]{"parrain", " "});
                    add(new String[]{"adresse1", ""});
                    add(new String[]{"adresse2", "130 chemin de la croix"});
                    add(new String[]{"ville", "26750 Geyssans"});
                    add(new String[]{"telephone", "04 75 05 93 35"});
                    add(new String[]{"portable", "06 45 84 78 74"});
                    add(new String[]{"rds", "non"}); //"oui"
                    add(new String[]{"rds_anc", "non"});
                    add(new String[]{"anc_mail", "jbperin@gmail.com"});
                    add(new String[]{"faire", "enregistrer"});
                    add(new String[]{"lien", "mes_infos"});
                    add(new String[]{"qui", login});
                    add(new String[]{"ext", "jpg"});
                }});
        //System.out.println(htmlContent);
        FileHelper.getInstance().writeStringToFile(htmlContent, "Result.html");
        return result;
    }
    private void unpublishAnnounce() {


        String htmlContent = webClient.getSimpleWebPage("http://sel-des-deux-rives.org/catalogue/index.php?lien=mes_annonces&confirmer=confirm_suppr&id=2066&quoi=demande");

        FileHelper.getInstance().writeStringToFile(htmlContent, "Result.html");

    }

    public static void main(String[] args) {

        //System.out.println("Hello world");

        List<Announce> lAnnonces = WebHelper.getInstance().getAnonymousAnnounces(false);
        String userLogin = "" ;
        String userPassword = "";
        WebHelper.getInstance().setAuthenticationInformation(userLogin, userPassword);
        List<Person> lPersonnes = WebHelper.getInstance().getAnnuaire(false);

        WebHelper.getInstance().unanonimizeAnnounces (lAnnonces, false);
        //WebHelper.getInstance().publishTansaction();
        //WebHelper.getInstance().publishSold();
        WebHelper.getInstance().getAccountInfo (false);
        //WebHelper.getInstance().publishInfo();
        //WebHelper.getInstance().publishAnnounce();
        //WebHelper.getInstance().publishUrgentAnnounce();


        //WebHelper.getInstance().unpublishAnnounce();
        //WebHelper.getInstance().getMyAnnounces(false);

        WebHelper.getInstance().getPersonnalInfo(false);

        WebHelper.getInstance().getForums(false);
        //WebHelper.getInstance().debugDiscussion();

//        for (Announce ann: lAnnonces) {
//            System.out.println (ann.getId() + " " + ann.getDirection() + " " + ann.getCategory() + " " +  ann.getDescription());
//        }
//        for (Person pers: lPersonnes) {
//            System.out.println( pers.getPseudo()+ " " + pers.getPhone1()+ " " + pers.getPhone2()+ " " + pers.getAddress()+ " " + pers.getSolde()+ " " + pers.getNumberOfExchange()+ " " + pers.getName());
//        }
        System.out.println("The end");
    }


    public boolean isAuthenticated() {
        return authenticated;
    }
}

