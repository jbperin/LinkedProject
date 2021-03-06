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
 * Created by Jean-Baptiste PERIN on 03/11/2017.
 */
public class Transaction {
    String pseudoOfferer = null;
    String pseudoDemander = null;
    String offerDescritpion = null;
    String demandDescription = null;
    Integer amount = null;

    public Transaction() {

    }

    public String getPseudoOfferer() {
        return pseudoOfferer;
    }

    public void setPseudoOfferer(String pseudoOfferer) {
        this.pseudoOfferer = pseudoOfferer;
    }

    public String getPseudoDemander() {
        return pseudoDemander;
    }

    public void setPseudoDemander(String pseudoDemander) {
        this.pseudoDemander = pseudoDemander;
    }

    public String getOfferDescritpion() {
        return offerDescritpion;
    }

    public void setOfferDescritpion(String offerDescritpion) {
        this.offerDescritpion = offerDescritpion;
    }

    public String getDemandDescription() {
        return demandDescription;
    }

    public void setDemandDescription(String demandDescription) {
        this.demandDescription = demandDescription;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "pseudoOfferer='" + pseudoOfferer + '\'' +
                ", pseudoDemander='" + pseudoDemander + '\'' +
                ", offerDescritpion='" + offerDescritpion + '\'' +
                ", demandDescription='" + demandDescription + '\'' +
                ", amount=" + amount +
                '}';
    }
}
