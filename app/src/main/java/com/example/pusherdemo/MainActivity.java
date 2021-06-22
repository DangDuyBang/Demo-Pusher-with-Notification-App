package com.example.pusherdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionStateChange;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.LayoutManager lManager;
    private EventAdapter adapter;
    private final Pusher pusher = new Pusher("af9c6e37c700681b3858");
    private static final String CHANNEL_NAME = "events_to_be_shown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the RecyclerView
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler_view);

        // Use LinearLayout as the layout manager
        lManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(lManager);

        // Set the custom adapter
        List<Event> eventList = new ArrayList<>();
        adapter = new EventAdapter(eventList);
        recycler.setAdapter(adapter);

        Channel channel = pusher.subscribe(CHANNEL_NAME);

        SubscriptionEventListener eventListener = (channel1, event, data) -> runOnUiThread(() -> {
            System.out.println("Received event with data: " + data);
            Gson gson = new Gson();
            Event evt = gson.fromJson(data, Event.class);
            evt.setName(event + ":");
            adapter.addEvent(evt);
            ((LinearLayoutManager)lManager).scrollToPositionWithOffset(0, 0);
        });

        channel.bind("created", eventListener);
        channel.bind("updated", eventListener);
        channel.bind("deleted", eventListener);

        pusher.connect();
        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                System.out.println("State changed to " + change.getCurrentState() +
                        " from " + change.getPreviousState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        pusher.disconnect();
    }
}