/*
 * Copyright (C) 2010   Cyril Mottier & Ludovic Perrier
 *              (http://www.digitbooks.fr/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.digitbooks.android.examples.chapitre06;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import fr.digitbooks.android.examples.R;

public class UltimateListActivity extends ListActivity {

    private static final int NUMBER_OF_ROWS = 10000;
    private static final String FAKE_TITLE_PREFIX = "Ceci est le titre #";
    private static final String FAKE_SUBTITLE_PREFIX = "Sous-titre de la cellule #";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new UltimateAdapter(this));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Toast.makeText(this, String.format("La ligne %s a �t� cliqu�e", position), Toast.LENGTH_SHORT).show();
    }

    private static class UltimateAdapter extends BaseAdapter {

        /**
         * ViewHolder contenant des r�f�rences sur les sous-vues de chaque item
         * ainsi que deux StringBuilders contenant le textes des 2 pr�c�dentes
         * vues.
         */
        static class ViewHolder {
            TextView mTitleView;
            StringBuilder mTitleBuilder;
            TextView mSubtitleView;
            StringBuilder mSubtitleBuilder;
        }

        private LayoutInflater mLayoutInflater;

        public UltimateAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return NUMBER_OF_ROWS;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                /*
                 * Une nouvelle vue doit �tre cr��e. C'est ici qu'on effectue le
                 * travail de r�cup�ration de r�f�rences des sous-vues et de
                 * cr�ation des StringBuilders.
                 */
                convertView = mLayoutInflater.inflate(R.layout.subtitle_list_item, parent, false);
                holder = new ViewHolder();
                holder.mTitleView = (TextView) convertView.findViewById(R.id.subtitle_item_title);
                holder.mTitleBuilder = new StringBuilder();
                holder.mSubtitleView = (TextView) convertView.findViewById(R.id.subtitle_item_subtitle);
                holder.mSubtitleBuilder = new StringBuilder();
                convertView.setTag(holder);
            } else {
                /*
                 * La vue existant d�j�, il suffit de r�cup�rer les informations
                 * qui lui sont attach�es.
                 */
                holder = (ViewHolder) convertView.getTag();
            }

            // R�initialise le StringBuilder
            holder.mTitleBuilder.setLength(0);
            // Ajoute le texte
            holder.mTitleBuilder.append(FAKE_TITLE_PREFIX).append(position);
            holder.mTitleView.setText(holder.mTitleBuilder);

            // R�initialise le StringBuilder
            holder.mSubtitleBuilder.setLength(0);
            // Ajoute le texte
            holder.mSubtitleBuilder.append(FAKE_SUBTITLE_PREFIX).append(position);
            holder.mSubtitleView.setText(holder.mSubtitleBuilder);

            return convertView;
        }

    }
}
