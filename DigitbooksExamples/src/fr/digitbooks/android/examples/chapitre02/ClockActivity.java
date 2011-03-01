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
package fr.digitbooks.android.examples.chapitre02;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.format.Time;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class ClockActivity extends Activity {

    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create text view
        mTextView = new TextView(this);
        updateTime();
        mTextView.setTextSize(24);
        // Create parameters for
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        // Add button on layout
        addContentView(mTextView, params);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cr�ation d'un filtre d'intention pour configurer le r�cepteur
        // d'�v�nement. Le r�cepteur sera appel� � chaque minute
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        // Enregistrement du filtre tant que l'activit� a le focus
        registerReceiver(mIntentReceiver, filter);
        // On mets l'heure � jour lorsque l'activit� a le focus
        updateTime();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // On d�sactive le r�cepteur lorsque on perds le focus
        unregisterReceiver(mIntentReceiver);
    }

    // Cr�ation du r�cepteur d'�v�nement
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Lorsque l'�v�nement est re�u on mets l'heure � jour
            updateTime();
        }
    };

    private void updateTime() {
        // What time is it?
    	Time time = new Time();
    	time.setToNow();
        mTextView.setText(time.format("%H:%M"));
    }
}
