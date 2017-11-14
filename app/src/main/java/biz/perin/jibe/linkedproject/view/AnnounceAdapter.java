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
package biz.perin.jibe.linkedproject.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import biz.perin.jibe.linkedproject.MyHelper;
import biz.perin.jibe.linkedproject.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static biz.perin.jibe.linkedproject.MyHelper.json2dict;

/**
 * Created by Jean-Baptiste PERIN on 12/11/2017.
 */
public class AnnounceAdapter extends ArrayAdapter<String> {

    public AnnounceAdapter(Context context, List<String> announces) {
        super(context, 0, announces);
    }

    private class AnnounceViewHolder {
        public TextView announceId;
        public TextView description;
        public ImageView direction;
        public ImageView category;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ligne_annonce, parent, false);
        }
        AnnounceViewHolder viewHolder = (AnnounceViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new AnnounceViewHolder();
            viewHolder.announceId = (TextView) convertView.findViewById(R.id.announceId);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.direction = (ImageView) convertView.findViewById(R.id.direction);
            viewHolder.category = (ImageView) convertView.findViewById(R.id.category);
            convertView.setTag(viewHolder);
        }

        String jsAnnonce = getItem(position);
        //System.out.println(jsAnnonce);
        HashMap<String, String> dValues = MyHelper.json2dict(jsAnnonce);

        viewHolder.announceId.setText(dValues.get("id"));
        viewHolder.description.setText(dValues.get("description"));

        if (dValues.get("direction").equals("DEMAND")) {
            viewHolder.direction.setImageDrawable(new ColorDrawable(Color.GREEN));
        } else {
            viewHolder.direction.setImageDrawable(new ColorDrawable(Color.BLUE));
        }
//        int categoryColor = Color.WHITE;
//        if (annonce.getCategory() != null) {
//            switch (annonce.getCategory()) {
//                case ACTIONS_COLLECTIVES:
//                    categoryColor = Color.YELLOW;
//                    break;
//                case AIDE_A_LA_PERSONNE:
//                    categoryColor = Color.BLACK;
//                    break;
//                case ALIMENTATION:
//                    categoryColor = Color.BLACK;
//                case ATELIER_COURS:
//                    categoryColor = Color.BLUE;
//                    break;
//                case AUTRE_OBJET:
//                    categoryColor = Color.CYAN;
//                    break;
//                case AUTRE_SERVICE:
//                    categoryColor = Color.DKGRAY;
//                case BRICOLAGE:
//                    categoryColor = Color.GRAY;
//                    break;
//                case COUTURE:
//                    categoryColor = Color.GREEN;
//                    break;
//                case COVOITURAGE:
//                    categoryColor = Color.LTGRAY;
//                    break;
//                case HEBERGEMENT:
//                    categoryColor = Color.MAGENTA;
//                    break;
//                case INFORMATIQUE:
//                    categoryColor = Color.RED;
//                    break;
//                case JARDINAGE:
//                    categoryColor = Color.alpha(2993);
//                    break;
//                case JEUX_JOUETS:
//                    categoryColor = Color.alpha(6578);
//                    break;
//                case MAISON:
//                    categoryColor = Color.rgb(12, 25, 120);
//                    break;
//                case MATERIEL_OUTILLAGE:
//                    categoryColor = Color.rgb(120, 37, 120);
//                    break;
//                case MULTIMEDIA:
//                    categoryColor = Color.rgb(230, 128, 10);
//                    break;
//                case VETEMENTS_ACCESSOIRES:
//                    categoryColor = Color.rgb(120, 10, 122);
//                    break;
//
//            }
//        }
//        viewHolder.category.setImageDrawable(new ColorDrawable(categoryColor));


        return convertView;
    }
}
