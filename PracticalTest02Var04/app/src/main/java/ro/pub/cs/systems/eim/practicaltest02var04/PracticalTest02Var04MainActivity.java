package ro.pub.cs.systems.eim.practicaltest02var04;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ro.pub.cs.systems.eim.practicaltest02var04.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02var04.network.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02var04.network.ServerThread;

public class PracticalTest02Var04MainActivity extends AppCompatActivity {

    // Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    // Client widgets
    private EditText clientPortEditText = null;
    private EditText urlEditText = null;
    private Button getUrlButton = null;
    private TextView urlTextView = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }

    }

    private GetUrlButtonClickListener getUrlButtonClickListener = new GetUrlButtonClickListener();
    private class GetUrlButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = new String("localhost");
            String clientPort = clientPortEditText.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String url = urlEditText.getText().toString();
            if (url == null || url.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from url should be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            urlTextView.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(
                    clientAddress, Integer.parseInt(clientPort), url, urlTextView
            );
            clientThread.start();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onCreate() callback method has been invoked");
        setContentView(R.layout.activity_practical_test02_var04_main);

        serverPortEditText = (EditText)findViewById(R.id.server_port);
        connectButton = (Button)findViewById(R.id.server_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        clientPortEditText = serverPortEditText;
        urlEditText = (EditText)findViewById(R.id.edit_url);
        getUrlButton = (Button)findViewById(R.id.get_url_button);
        getUrlButton.setOnClickListener(getUrlButtonClickListener);
        urlTextView = (TextView)findViewById(R.id.url_text_view);
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }

}