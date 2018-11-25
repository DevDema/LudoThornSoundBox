package net.ddns.andrewnetwork.ludothornsoundbox.controller;

import net.ddns.andrewnetwork.ludothornsoundbox.model.Channel;

import java.util.ArrayList;

public interface passVideoList {
        void onVideoListReceived(ArrayList<Channel> channels);
        void OnExecutionNumberReceived(int executionNumberVideoFragment);
}
