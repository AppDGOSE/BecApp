package mx.unam.becapp.app;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class BecAppActivity extends Activity {

	private long splashDelay = 2500;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bec_app);
		
		TimerTask task = new TimerTask() {
				@Override
				public void run() {
					Intent mainIntent;
					mainIntent = new Intent().setClass(BecAppActivity.this, LoginActivity.class);
					startActivity(mainIntent);

					/** Destruimos esta activity para prevenir que el usuario retorne aqui
						presionando el boton Atras.*/						
					finish();

				}
			};

		Timer timer = new Timer();
		// Comienza el cronometro.
		timer.schedule(task, splashDelay);
	} 	
}
