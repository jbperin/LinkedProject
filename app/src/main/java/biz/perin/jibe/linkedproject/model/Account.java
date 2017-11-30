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
public class Account {
    private Integer nbEchange = null;
    private Integer solde = null;
    private Integer soldeActuel = null;
    private String derniere_publication = null;

    public Account() {
    }
    public void setNbEchange(Integer nbEchange) {
        this.nbEchange = nbEchange;
    }

    public void setSolde(Integer solde) {
        this.solde = solde;
    }

    public void setSoldeActuel(Integer soldeActuel) {
        this.soldeActuel = soldeActuel;
    }

    public Integer getNbEchange() {
        return nbEchange;
    }

    public Integer getSolde() {
        return solde;
    }

    public Integer getSoldeActuel() {
        return soldeActuel;
    }

    public String getLastPublish() {
        return derniere_publication;
    }

    public void setLastPublish(String derniere_publication) {
        this.derniere_publication = derniere_publication;
    }
}
