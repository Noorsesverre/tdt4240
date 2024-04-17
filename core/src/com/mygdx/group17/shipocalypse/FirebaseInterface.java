package com.mygdx.group17.shipocalypse;

import java.util.Map;

public interface FirebaseInterface {

    void writeToDatabase(String gameID, Map<String, Object> data);
}
