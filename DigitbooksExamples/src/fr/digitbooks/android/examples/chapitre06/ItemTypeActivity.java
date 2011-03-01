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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import fr.digitbooks.android.examples.R;

public class ItemTypeActivity extends ListActivity {

    private static final int ITEM_VIEW_TYPE_VIDEO = 0;
    private static final int ITEM_VIEW_TYPE_SEPARATOR = ITEM_VIEW_TYPE_VIDEO + 1;

    private static final int ITEM_VIEW_TYPE_COUNT = ITEM_VIEW_TYPE_SEPARATOR + 1;

    private static class Video {
        public String title;
        public String description;

        public Video(String title) {
            this(title, "Aucune description disponible");
        }

        public Video(String title, String description) {
            this.title = title;
            this.description = description;
        }
    }

    private static final Object[] OBJECTS = {
            "Films",
            new Video("Tron Legacy", "Long-m�trage am�ricain\nGenre : Science-fiction\nR�alis� par Joseph Kosinski"),
            new Video("Harry Potter et les reliques de la mort - I",
                    "Long-m�trage am�ricain, britannique\nGenre : Fantastique\nR�alis� par David Yates"),
            new Video("Le Cinqui�me �l�ment",
                    "Long-m�trage fran�ais, am�ricain\nGenre : Science-fiction\nR�alis� par Luc Besson"),
            new Video("I, Robot", "Long-m�trage am�ricain\nGenre : Science-fiction, Action\nR�alis� par Alex Proyas"),
            new Video("The Island", "Long-m�trage am�ricain\nGenre : Science-fiction, Action\nR�alis� par Michael Bay"),
            new Video("Minority Report",
                    "Long-m�trage am�ricain\nGenre : Science-fiction\nR�alis� par Steven Spielberg"),
            new Video("Bienvenue � Gattaca",
                    "Long-m�trage am�ricain\nGenre : Science-fiction\nR�alis� par Andrew Niccol"),
            new Video("Inception",
                    "Long-m�trage am�ricain, britannique\nGenre : Science fiction, Thriller\nR�alis� par Christopher Nolan"),
            "Series", new Video("Dr House (Docteur House)"), new Video("True Blood"), new Video("Smallville"),
            new Video("Sanctuary"), new Video("Desperate Housewives"), new Video("Spartacus: Blood and Sand"),
            new Video("Lost, les disparus"), new Video("Stargate Universe"), new Video("How I Met Your Mother")
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ListView listView = getListView();
        /*
         * Puisque la ListView n'a pas �t� cr��e � partir d'un fichier XML, il
         * est n�cessaire de modifier quelques-unes des propri�t�s de cette
         * derni�re via le code Java.
         */
        listView.setCacheColorHint(Color.DKGRAY);
        /*
         * Modifie la couleur d'arri�re plan (�quivalent �
         * android:background="@android:color/white")
         */
        listView.setBackgroundColor(Color.DKGRAY);
        /*
         * Modifie le Drawable utilis� pour s�parer les �l�ments
         */
        listView.setDivider(new ColorDrawable(Color.WHITE));
        /*
         * Un ColorDrawable n'ayant pas de taille, il est n�cessaire de d�finir
         * la hauteur des s�parations.
         */
        listView.setDividerHeight(1);

        setListAdapter(new TwoRowTypesAdapter(this));
    }

    private static class TwoRowTypesAdapter extends BaseAdapter {

        static class ViewHolder {
            TextView mTitleView;
            TextView mSubtitleView;
        }

        private final LayoutInflater mLayoutInflater;

        public TwoRowTypesAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return OBJECTS.length;
        }

        public Object getItem(int position) {
            return OBJECTS[position];
        }

        public long getItemId(int position) {
            return position;
        }

        public int getViewTypeCount() {
            return ITEM_VIEW_TYPE_COUNT;
        }

        public int getItemViewType(int position) {
            return (OBJECTS[position] instanceof String) ? ITEM_VIEW_TYPE_SEPARATOR : ITEM_VIEW_TYPE_VIDEO;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            final int type = getItemViewType(position);
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = mLayoutInflater.inflate(type == ITEM_VIEW_TYPE_SEPARATOR ? R.layout.separator_list_item
                        : R.layout.video_list_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.mTitleView = (TextView) convertView.findViewById(R.id.title);
                viewHolder.mSubtitleView = (TextView) convertView.findViewById(R.id.subtitle);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            switch (type) {
                case ITEM_VIEW_TYPE_SEPARATOR:
                    viewHolder.mTitleView.setText((String) getItem(position));
                    break;

                case ITEM_VIEW_TYPE_VIDEO:
                    final Video video = (Video) getItem(position);
                    viewHolder.mTitleView.setText(video.title);
                    viewHolder.mSubtitleView.setText(video.description);
            }

            return convertView;
        }

    }

}
